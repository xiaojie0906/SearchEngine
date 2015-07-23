package com.search.control;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

//import com.sun.java_cup.internal.runtime.Scanner;



public class main {
public static void main(String[] args) throws Exception {  
	//try{	
	myCreateIndex createIndex=new myCreateIndex();
		
		File fileDir =new File("E:\\course\\eclipse-workspace\\Data\\");   
		 File indexDir =new File("E:\\course\\eclipse-workspace\\index");   
		myCreateIndex.create(fileDir,indexDir);
	    
		 String queries="";
	    InputStreamReader is_reader = new InputStreamReader(System.in);
	  ArrayList<String[]> urlList = new ArrayList<String[]>();
	// while(true)
	  //{
		  
		   System.out.println("\nEnter query: ");
		   queries = new BufferedReader(is_reader).readLine();
	
	       urlList =MySearch.createSearch(queries) ;
	      
	     for(int i=0;i<urlList.size();i++)
	        {
	        	
	        	System.out.print("\n");
	        	
	          System.out.print(i+"   URL IS----"+urlList.get(i)[0]);
	          System.out.print("\n\n");
	          System.out.print("   content---- "+urlList.get(i)[1]);
	          System.out.print("\n\n");
	          System.out.print("   pagerank---- "+urlList.get(i)[2]);
	        }
	       // urlList.clear();
	          is_reader = new InputStreamReader(System.in);
	  	   
	  // } 
	 
	//} 
	//catch (IOException e) {
	//	e.printStackTrace();
	//}
	
}
}
