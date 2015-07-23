package com.search.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Coding {
	
	public static String Code_Detect(String strURL) throws MalformedURLException{
		StringBuffer sb = new StringBuffer();
		String line;
		URL url = new URL(strURL);
		try {
		    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		    while ((line = in.readLine()) != null) {
		        sb.append(line);
		    }
		    in.close();
		} catch (Exception e) { 
			System.err.println(e);
		    System.err.println("Usage:   java   HttpClient   <URL>   [<filename>]");
		}
		String htmlcode = sb.toString();
		System.out.println(htmlcode);
		// 解析html源码，取出<meta />区域，并取出charset
		if(!htmlcode.contains("charset=gb") && !htmlcode.contains("charset=utf")) return null;
		String strbegin = "<meta";
		String strend = ">";
		String strtmp, strencoding;
		int begin = htmlcode.indexOf(strbegin);
		int end = -1;
		int inttmp;
		while (begin != -1) {
		    end = htmlcode.substring(begin).indexOf(strend);
		    if (begin > -1 && end > -1) {
		        strtmp = htmlcode.substring(begin, begin + end).toLowerCase();
		        inttmp = strtmp.indexOf("charset");
		        if (inttmp > -1) {
		            strencoding = strtmp.substring(inttmp + 7, end).replace(
		                    "=", "").replace("/", "").replace("\"", "")
		                    .replace("\'", "").replace(" ", "");
		            return strencoding;
		        }
		    }
		    htmlcode = htmlcode.substring(begin);
		    begin = htmlcode.indexOf(strbegin);
		}
		return null;
	}
	

	public static void main(String[] args) throws MalformedURLException {
		String strURLString = "http://www.baidu.com";
		System.out.println(Code_Detect(strURLString));
	}

}
