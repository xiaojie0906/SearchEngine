#!/usr/bin/env python
# -*- coding: utf-8 -*-

def out():
    print '''start out ID_TO_URL_wangxy.txt to /home/f13_qiuxiaojie/Data/'''
    f_in=file('/home/f13_qiuxiaojie/wangxy/total/ID_To_URL.txt','r')
    f_add=file('/home/f13_qiuxiaojie/wangxy/total/pr_value.txt','r')
    f_out=file('/home/f13_qiuxiaojie/Data/ID_TO_URL_wangxy.txt','w')
    data_in=f_in.readlines()
    f_in.close()
    data_add=f_add.readlines()
    f_add.close()
    data_out=[]
    for i in data_in:
        temp=i.split(',')
        num=int(temp[0])
        data_out.append([i[:-1],float(data_add[num-1][:-1])])
    for line in data_out:
        temp=line[0].find(',')
        line[0]=list(line[0])
        line[0][temp]='#'
        line[0]=''.join(line[0])
        f_out.write(line[0].replace('"','')+'#'+format(line[1],'f')+'\n')
    f_out.close()
    print 'out end'

if __name__=='__main__':
    out()
