#include <stdio.h>
#include <stdlib.h>

int pagerank(float *coovala,int *coorowinda,int *coocolinda,int nnz,int nodes,float q,int max_iters,float accept_key,float *pr,int *real_iters,float *real_key);

int main()
{
    FILE *fp=NULL;
    
    float *value=NULL,*pr=NULL;
    int *row=NULL,*col=NULL;
    
    float elapsedTime;
    cudaEvent_t start, stop;
    
    double sum=0.0;
    float q=0.8,accept_key=0.0000001,error_value;
    int i,nnz,nodes,max_iters=100,real_iters;
    
    fp=fopen("Graph.txt","r");
    if(fp==NULL)
    {
        printf("can't read matrix.txt!\n");
        return(1);
    }
    
    printf("start load data.\n");
    
    fscanf(fp,"%d",&nodes);
    fscanf(fp,"%d",&nnz);
    
    printf("nodes=%d,nnz=%d.\nif it is not right,then please check ID_To_ID.txt,ke neng you yi xie te shu zi fu zai wen jian kai tou.\n",nodes,nnz);
    
    value=(float *)malloc(sizeof(float)*nnz);
    row=(int *)malloc(sizeof(int)*nnz);
    col=(int *)malloc(sizeof(int)*nnz);
    pr=(float *)malloc(sizeof(float)*nodes);
    
    //待定_直接使用型
    for(i=0;i<nnz;i++)
    {
        fscanf(fp,"%d",row+i);
        fscanf(fp,"%d",col+i);
        fscanf(fp,"%f",value+i);
        row[i]--;
        col[i]--;
        //test ok
        //printf("%d,%d,%f.\n",row[i],col[i],value[i]);
        //getchar();
    }
    fclose(fp);
    fp=NULL;
    
    printf("data load is ok.\nstart solve pr value.\n");
    
    //test ok
    //printf("%d,%d,%d,%d,%d.\n",row[0],row[1],row[2],row[3],row[4]);
    
    cudaEventCreate(&start); 
    cudaEventCreate(&stop);
    cudaEventRecord(start, 0);
    
    pagerank(value,row,col,nnz,nodes,q,max_iters,accept_key,pr,&real_iters,&error_value);
    
    cudaEventRecord(stop, 0); 
    cudaEventSynchronize(stop); 
    cudaEventElapsedTime(&elapsedTime, start, stop);
    
    printf("pr value solve ok.\nstart write answ.\n");
    
    fp=fopen("pr_out.txt","w");
    if(fp==NULL)
    {
        printf("can't write pr_out.txt!\n");
        return(1);
    }
    fprintf(fp,"nodes:%d\n",nodes);
    fprintf(fp,"nnz:%d\n",nnz);
    fprintf(fp,"real_iters:%d\n",real_iters);
    fprintf(fp,"sigma:%f\n",error_value);
    for(i=0;i<nodes;i++)
        sum+=pr[i];
    fprintf(fp,"the sum pr value:%f\n",sum);
    fprintf(fp,"cuda_use_time_ms:%fms\n",elapsedTime);
    fclose(fp);
    fp=NULL;
    
    fp=fopen("pr_value.txt","w");
    if(fp==NULL)
    {
        printf("can't write pr_value.txt!\n");
        return(1);
    }
    for(i=0;i<nodes;i++)
        fprintf(fp,"%f\n",pr[i]*10000);
    fclose(fp);
    fp=NULL;
    
    return(0);
}
