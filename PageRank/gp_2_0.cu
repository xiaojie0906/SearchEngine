#include <stdio.h>
#include <stdlib.h>

#include "cusparse_v2.h"

#define CLEANUP(s)                                   \
do {                                                 \
    printf ("%s\n", s);                              \
    /*if (yHostPtr)           free(yHostPtr);          \
    if (zHostPtr)           free(zHostPtr);          \
    if (xIndHostPtr)        free(xIndHostPtr);       \
    if (xValHostPtr)        free(xValHostPtr);       \
    if (cooRowIndexHostPtr) free(cooRowIndexHostPtr);\
    if (cooColIndexHostPtr) free(cooColIndexHostPtr);\
    if (cooValHostPtr)      free(cooValHostPtr);     \
    if (y)                  cudaFree(y);             \
    if (z)                  cudaFree(z);             \
    if (xInd)               cudaFree(xInd);          \
    if (xVal)               cudaFree(xVal);          \
    if (csrRowPtr)          cudaFree(csrRowPtr);     \
    if (cooRowIndex)        cudaFree(cooRowIndex);   \
    if (cooColIndex)        cudaFree(cooColIndex);   \
    if (cooVal)             cudaFree(cooVal);        \
    if (descr)              cusparseDestroyMatDescr(descr);\
    if (handle)             cusparseDestroy(handle); \
    cudaDeviceReset();          \
    fflush (stdout);*/                                 \
} while (0)

__global__ void assign(float *data,int length,float value)
{
    int id=blockIdx.x*blockDim.x+threadIdx.x;
    if(id<length)
        data[id]=value;
}

__global__ void reduction_normal(float *in,int length,float *out)
{
    __shared__ float partialSum[1024];
    int index=blockIdx.x*blockDim.x + threadIdx.x;
    int id=threadIdx.x;
    if(index<length)
        partialSum[id]=in[index];
    else
        partialSum[id]=0.0f;
    __syncthreads();

    for(unsigned int stride = blockDim.x / 2;stride > 0;stride /= 2)
    {
        __syncthreads();
        if(id < stride)
            partialSum[id]+=partialSum[id + stride];
    }
    if(threadIdx.x==0)
        out[blockIdx.x]=partialSum[0];
}

void reduction(float *data,int length,float *temp_1,float *temp_2,float *answ)
{
    float *in,*out,*temp;
    int blocksize=1024;
    
    in=data;
    out=temp_1;
    if(length<=blocksize)
        out=answ;

    reduction_normal<<<length/blocksize+1,blocksize>>>(in,length,out);
    cudaThreadSynchronize();

    if(length%blocksize==0)
        length/=blocksize;
    else
        length=length/blocksize+1;

    in=out;
    out=temp_2;

    while(length>blocksize)
    {
        reduction_normal<<<length/blocksize+1,blocksize>>>(in,length,out);
        cudaThreadSynchronize();

        if(length%blocksize==0)
            length/=blocksize;
        else
            length=length/blocksize+1;

        temp=in;
        in=out;
        out=temp;
    }

    out=answ;
    reduction_normal<<<length/blocksize+1,blocksize>>>(in,length,out);
    cudaThreadSynchronize();
}

__global__ void step_2_1(float *y,float *r,int nodes,float *answ)
{
    int id=blockIdx.x*blockDim.x+threadIdx.x;
    if(id<nodes)
    {
        float value_r=r[id];
        float value_y=y[id];
        answ[id]=value_r-value_y;
    }
}

void step_2(float *y,float *r,int nodes,float *temp_0,float *temp_1,float *d)
{
    step_2_1<<<nodes/256+1,256>>>(y,r,nodes,temp_1);
    cudaThreadSynchronize();
    reduction(temp_1,nodes,temp_0,temp_1,d);
}

__global__ void step_3(float *y,int nodes,float *d,float *r)
{
    int id=blockIdx.x*blockDim.x+threadIdx.x;
    if(id<nodes)
        r[id]=y[id]+*d/nodes;
}

__global__ void step_4_1(float *r0,float *r1,int nodes,float *r_adder_abs)
{
    int id=blockIdx.x*blockDim.x+threadIdx.x;
    if(id<nodes)
        r_adder_abs[id]=abs(r0[id]-r1[id]);
}

void step_4(float *r0,float *r1,int nodes,float *temp_0,float *temp_1,float *sigma_cpu)
{
    step_4_1<<<nodes/256+1,256>>>(r0,r1,nodes,temp_1);
    cudaThreadSynchronize();
    reduction(temp_1,nodes,temp_0,temp_1,temp_0);
    cudaMemcpy(sigma_cpu,temp_0,sizeof(float),cudaMemcpyDeviceToHost);
}

int pagerank(float *coovala,int *coorowinda,int *coocolinda,int nnz,int nodes,float q,int max_iters,float accept_key,float *pr,int *real_iters,float *real_key)
{
    //矩阵数据
    float *gcoovala;
    int *gcoorowinda,*gcoocolinda;

    //gpu数据
    float *r[3],*y,*sum_temp[2];
    float *d;

    //cpu端数据
    float sigma;
    int i=0;

    //申请gpu空间
    cudaMalloc((void **)&gcoovala,sizeof(float)*nnz);
    cudaMalloc((void **)&gcoorowinda,sizeof(int)*nnz);
    cudaMalloc((void **)&gcoocolinda,sizeof(int)*nnz);
    cudaMalloc((void **)&r[0],sizeof(int)*(nodes*6+1));
    r[1]=r[0]+nodes;
    r[2]=r[1]+nodes;
    y=r[2]+nodes;
    sum_temp[0]=y+nodes;
    sum_temp[1]=sum_temp[0]+nodes;
    d=sum_temp[1]+nodes;

    //复制矩阵到gpu
    cudaMemcpy(gcoovala,coovala,sizeof(float)*nnz,cudaMemcpyHostToDevice);
    cudaMemcpy(gcoorowinda,coorowinda,sizeof(int)*nnz,cudaMemcpyHostToDevice);
    cudaMemcpy(gcoocolinda,coocolinda,sizeof(int)*nnz,cudaMemcpyHostToDevice);
    
    //test
    /*int temp[5];
    cudaMemcpy(temp,gcoorowinda,5,cudaMemcpyDeviceToHost);
    printf("%d,%d,%d,%d,%d.\n",coorowinda[0],coorowinda[1],coorowinda[2],coorowinda[3],coorowinda[4]);
    printf("%d,%d,%d,%d,%d.\n",temp[0],temp[1],temp[2],temp[3],temp[4]);
    getchar();*/
    
    //转换为csr矩阵
    int *csrRowPtr;
    float beta=0;

    cudaError_t cudaStat;
    cusparseStatus_t status;
    cusparseHandle_t handle=0;
    cusparseMatDescr_t descr=0;
    status= cusparseCreate(&handle);
    if (status != CUSPARSE_STATUS_SUCCESS) {
        CLEANUP("CUSPARSE Library initialization failed");
        return 1;
    }
    status= cusparseCreateMatDescr(&descr);
    if (status != CUSPARSE_STATUS_SUCCESS) {
        CLEANUP("Matrix descriptor initialization failed");
        return 1;
    }
    cusparseSetMatType(descr,CUSPARSE_MATRIX_TYPE_GENERAL);
    cusparseSetMatIndexBase(descr,CUSPARSE_INDEX_BASE_ZERO);
    cudaStat = cudaMalloc((void**)&csrRowPtr,(nodes+1)*sizeof(int));
    if (cudaStat != cudaSuccess) {
        CLEANUP("Device malloc failed (csrRowPtr)");
        return 1;
    }
    status= cusparseXcoo2csr(handle,gcoorowinda,nnz,nodes,
                             csrRowPtr,CUSPARSE_INDEX_BASE_ZERO);
    if (status != CUSPARSE_STATUS_SUCCESS) {
        CLEANUP("Conversion from COO to CSR format failed");
        return 1;
    }
    //转换完成

    //初始化数据
    assign<<<nodes/256+1,256>>>(r[0],nodes,1.0/nodes);
    cudaThreadSynchronize();
    
    while(1)
    {
        //step_1
        //利用cusparse计算
        status= cusparseScsrmv(handle,CUSPARSE_OPERATION_NON_TRANSPOSE, nodes, nodes, nnz,
            &q, descr, gcoovala, csrRowPtr, gcoocolinda,
            r[i%3],&beta, y);
        if (status != CUSPARSE_STATUS_SUCCESS) {
            CLEANUP("Matrix-vector multiplication failed");
            return 1;
        }
        cudaThreadSynchronize();
        
        //test
        /*cudaMemcpy(pr,y,sizeof(float)*nodes,cudaMemcpyDeviceToHost);
        printf("%f,%f,%f,%f,%f,%f.\n",pr[0],pr[1],pr[2],pr[3],pr[4],pr[5]);
        getchar();*/

        //step 2
        step_2(y,r[i%3],nodes,sum_temp[0],sum_temp[1],d);

        //step 3
        step_3<<<nodes/256+1,256>>>(y,nodes,d,r[(i+1)%3]);
        cudaThreadSynchronize();

        //step 4
        step_4(r[i%3],r[(i+1)%3],nodes,sum_temp[0],sum_temp[1],&sigma);
        if(sigma<accept_key||i+1>=max_iters)
            break;

        i++;
    }

    cudaMemcpy(pr,r[(i+1)%3],sizeof(float)*nodes,cudaMemcpyDeviceToHost);
    *real_key=sigma;
    *real_iters=i+1;

    cudaFree(gcoovala);
    cudaFree(gcoorowinda);
    cudaFree(gcoocolinda);
    cudaFree(r[0]);

    //终止使用cusparse
    cudaFree(csrRowPtr);
    status = cusparseDestroyMatDescr(descr);
    descr = 0;
    if (status != CUSPARSE_STATUS_SUCCESS) {
        CLEANUP("Matrix descriptor destruction failed");
        return 1;
    }
    status = cusparseDestroy(handle);
    handle = 0;
    if (status != CUSPARSE_STATUS_SUCCESS) {
        CLEANUP("CUSPARSE Library release of resources failed");
        return 1;
    }
    return(1);
}
