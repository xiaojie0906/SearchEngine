package com.search.control;
import java.io.BufferedReader; 
import java.io.BufferedWriter; 
import java.io.File; 
import java.io.FileReader; 
import java.io.FileWriter; 
import java.io.IOException; 
import java.nio.charset.Charset;
import java.util.ArrayList; 
import java.util.Iterator; 
import java.util.List; 

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public class readCSV {
	
	public static void main(String[] args) throws Exception {  
		//writeCsv();
		
		readeCsv("E:\\course\\eclipse-workspace\\idtourl\\ID_TO_URL_wangxy.txt");
			
	}
	
	public static ArrayList<String[]>  readeCsv(String csvFilePath){
		// String[] url_pr={"null","null"};
		ArrayList<String[]> csvList = new ArrayList<String[]>(); //������������
        try {    
             
           
      
            CsvReader reader = new CsvReader(csvFilePath,'#',Charset.forName("UTF-8"));    //SJISһ�����������Ϳ�����    
         
            while(reader.readRecord()){  
        	//  int ID= Integer.parseInt(reader.getValues()[0]);
                      //���ж������ͷ������    
                 csvList.add(reader.getValues());
                 System.out.println(reader.getValues()[0]+reader.getValues()[1]+reader.getValues()[2]);
                 //     System.out.print(ID);        
          }    
         // System.out.print("here\n");
           //  reader.readHeaders(); // ������ͷ   �����Ҫ��ͷ�Ļ�����Ҫд��䡣
           
          //  System.out.print(reader.getValues()[0]);
           
             
                       
             reader.close();
          /*   
             int search_id= Integer.parseInt(id);
             System.out.print(search_id);      
             url_pr[0]=csvList.get(search_id)[1];
             url_pr[1]=csvList.get(search_id)[2];
             System.out.print(url_pr[0]+url_pr[1]);
             */
             
//              for(int row=0;row<csvList.size();row++){
//            	
//                System.out.println(csvList.get(row)[0]+csvList.get(row)[1]+csvList.get(row)[2]);
//                             }
//                          
             
             System.out.println("reada csv  success\n");
        }catch(Exception ex){
            System.out.println(ex);
        }
        
		return csvList;
    }
    
    /**
     * д��CSV�ļ�
     */
    public static void writeCsv(){
        try {
            
            String csvFilePath = "E:\\course\\eclipse-workspace\\idtourl\\test.txt";
             CsvWriter wr =new CsvWriter(csvFilePath,',',Charset.forName("SJIS"));
             String[] contents = {"aaaaa","bbbbb","cccccc","ddddddddd"};                    
             wr.writeRecord(contents);
             wr.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
    }
	
	
	
}

