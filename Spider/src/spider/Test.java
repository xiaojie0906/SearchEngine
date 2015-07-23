package spider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import spider.WebGraphMemory;
import spider.Coding;

public class Test implements Runnable{
	public Integer startID, endID;
	public File file;
	public static WebGraphMemory wgm ;
	public Test(Integer startID, Integer endID){
    	this.file = new File("URL.txt");
    	this.wgm = null;
		this.startID = startID;
		this.endID = endID;
	}
	
	public static String downloadPageContent(String strUrl) {
		try {
			String en_code = Coding.Code_Detect(strUrl);
			if(en_code == null) return null;
			// 根据网址strUrl创建URL对象
			URL pageUrl = new URL(strUrl);
			// 下载网页
			BufferedReader reader = new BufferedReader(new InputStreamReader(pageUrl.openStream(),en_code));
			String line;
			// 读取网页内容
			StringBuffer pageBuffer = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				pageBuffer.append(line);
			}
			return pageBuffer.toString();
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public void run() 
	{
		/* Read The URL Web Map From A File */
		try {
			wgm = new WebGraphMemory(file);
		} catch (FileNotFoundException e) {	e.printStackTrace();} 
		catch (IOException e) {	e.printStackTrace();}
    	String link = null;
    	
  /*****************************************************************************************************/
    	/* Download Html Files */
       	for( Integer ID = startID; ID <= wgm.numNodes() && ID <= endID; ID++ )
       	{
    		/* retrive inLinks with id = ID */
    		link = wgm.IdentifyerToURL(ID);
        	/* Download pageContent with link url */
        	File fp = new File("E:\\Data\\"+ID.toString()+".html");
        	PrintWriter out = null;
        	System.out.println(link); 
			try {out = new PrintWriter(fp);	}
			catch (FileNotFoundException e) {e.printStackTrace();}
        	out.println(downloadPageContent(link));       	
        	out.close();
    	}
 /******************************************************************************************************/
		
	}
	
    public static void main(String[] args) throws FileNotFoundException, IOException {

    	Thread thread = new Thread(new Test(18618, 23507));
    	thread.start();
    }
}
