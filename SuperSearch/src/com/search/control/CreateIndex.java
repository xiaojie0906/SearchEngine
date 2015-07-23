package com.search.control;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.Analyzer;


//import org.apache.lucene.analysis.cjk.CJKAnalyzer ;




import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.lucene.document.*;

import java.io.*;
import java.nio.CharBuffer;
import java.util.Date;

import javax.swing.JTextArea;

import java.io.StringReader;
/**

 */
public class CreateIndex {



	public static void create(File fileDir , File indexDir , JTextArea lable) {
		try{
			//Directory dir = FSDirectory.open(new File("F:\\myindex")) ;
			
			Directory dir = FSDirectory.open(indexDir) ;
			
			//IndexWriter writer = new IndexWriter(dir , 
					//new CJKAnalyzer(Version.LUCENE_29) , IndexWriter.MaxFieldLength.UNLIMITED) ;
		
			Analyzer analyzer=new PaodingAnalyzer(); 
			
			IndexWriter writer=new IndexWriter(dir, analyzer, true,IndexWriter.MaxFieldLength.UNLIMITED);  
			
			//File files = new File("F:\\mydoc");
			//String[] Fnamelist = files.list();
		
			String[] Fnamelist = fileDir.list();
			
			System.out.println("let's begin...") ;
			long start = new Date().getTime(); // start time   
			for (int i = 0; i < Fnamelist.length; i++){
				
				//File file = new File(files,Fnamelist[i]);
				File file = new File(fileDir , Fnamelist[i]);
				
				Document doc = new Document();
				Field fld = new Field("path" , file.getPath() , Field.Store.YES , Field.Index.ANALYZED) ;
				doc.add(fld);
       
				FileInputStream in = new FileInputStream(file);
				parserHTML parser=new parserHTML();
				//Reader reader = new BufferedReader(new InputStreamReader(in));
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				StringBuffer sbStr = new StringBuffer();
				 String temp="";
				while((temp=reader.readLine())!=null)
				{
					sbStr.append(temp);
					sbStr.append("\r\n");
				}
				String tmpstring=sbStr.toString();
				reader.close();
				//System.out.print(tmpstring);
				
				String context=parser.parseHTML(tmpstring,"charset=utf8" );//网页解析后返回的内容
				 System.out.print(context);
				
			//	Reader conreader  = new StringReader(context);
			
				//fld = new Field("contents", conreader);
				Field fidbody = new Field("contents",context,Field.Store.YES,Field.Index.ANALYZED);//,Field.TermVector.WITH_POSITIONS_OFFSETS);
				doc.add(fld); 
				doc.add(fidbody);
				writer.addDocument(doc);
				System.out.println("Added : " + doc.get("path"));  
     
			}    
			writer.optimize();
			writer.close();
			System.out.println("Has Added Total: " + Fnamelist.length);
			long end = new Date().getTime();// start time   
			System.out.println("total time of creating index: " + (end-start) + " 毫秒。");
		}catch(Exception e){
			System.out.println("错误！路径不能为空。");
		}
  
	}

}