#!/usr/bin/env python
# -*- coding: utf-8 -*-

def wash_Graph():
    #输出的Graph.txt格式为
    #第1行为nodes
    #第2行为nnz
    #其他每行第一个元素为行，第2个元素为列，第3个元素为值
    f_g_in=file('/home/f13_qiuxiaojie/Data/ID_To_ID.txt','r')
    data_g_temp=f_g_in.readlines()
    f_g_in.close()
    
    f_g_out=file('/home/f13_qiuxiaojie/wangxy/total/Graph.txt','w')
    #下一行由于此次数据中有误而做了调整
    f_g_out.write(str(int(data_g_temp[0][:-2]))+'\n')
    f_g_out.write(str(len(data_g_temp)-1)+'\n')
    data_g=[]
    for line in data_g_temp[1:]:
        temp=(line.replace('->',':')).split(':')
        if len(temp)!=3:
            print 'error in ID_To_ID.txt'
            continue
        temp[0],temp[1]=int(temp[1]),int(temp[0])
        data_g.append(temp)
    data_g.sort()
    for line in data_g:
        line[0]=str(line[0])
        line[1]=str(line[1])
        line[2]=str(float(line[2]))+'\n'
        f_g_out.write(' '.join(line))
    f_g_out.close()

def wash_id():
    f_in=file('/home/f13_qiuxiaojie/Data/ID_To_URL.txt')
    data=f_in.readlines()
    f_in.close()
    f_out=file('/home/f13_qiuxiaojie/wangxy/total/ID_To_URL.txt','w')
    for line in data:
        temp=line.split(' : ')
        if len(temp)!=2:
            print 'error in ID_To_URL.txt'
        temp[1]='"'+temp[1][:-2]+'"'+'\n'
        f_out.write(','.join(temp))
    f_out.close()

if __name__=='__main__':
    print 'start wash data'
    wash_Graph()
    wash_id()
    print 'wash end'
