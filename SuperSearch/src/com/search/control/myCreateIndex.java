package com.search.control;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Date;





import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class myCreateIndex {
	public static void main(String[] args) throws Exception {  
		
		File fileDir =new File("E:\\course\\eclipse-workspace\\Data");   
		 File indexDir =new File("E:\\course\\eclipse-workspace\\index");   
		myCreateIndex.create(fileDir,indexDir);
		
	}
	
	
	
	public static void create(File fileDir , File indexDir) {
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
				FileInputStream in = new FileInputStream(file);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				StringBuffer sbStr = new StringBuffer();
				 String temp="";
				
				 System.out.println("Adding : " + file.getPath());  
				 
				 Document doc = new Document();
				Field fld = new Field("path" , file.getPath() , Field.Store.YES , Field.Index.ANALYZED) ;
				doc.add(fld);
       
				
				parserHTML parser=new parserHTML();
				//Reader reader = new BufferedReader(new InputStreamReader(in));
				
				while((temp=reader.readLine())!=null)
				{
					sbStr.append(temp);
					sbStr.append("\r\n");
				}
				String tmpstring=sbStr.toString();
				reader.close();
				//System.out.print(tmpstring);
				//String charset=Coding.Code_Detect(tmpstring);
				//String setname="charset="+charset;
				//System.out.print(charset);
					////////
				String context=parser.parseHTML(tmpstring,"charset=utf8" );//charset=
				//tmpstring=tmpstring+"\n";
				
				/////////use 
				//String context=TextExtract.parse(tmpstring);
					//context =TextExtract.preProcess(tmpstring);
				System.out.print(context);
				
				Reader conreader  = new StringReader(context);
			
				fld = new Field("contents", conreader);
				Field fidbody = new Field("contents",context,Field.Store.YES,Field.Index.TOKENIZED);//,Field.TermVector.WITH_POSITIONS_OFFSETS);
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
			System.out.println(e);
			System.out.println("错误！路径不能为空。");
		}
  
	}

}
