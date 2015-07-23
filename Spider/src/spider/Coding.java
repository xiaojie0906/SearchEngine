package spider;

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
	
	public static String CharSet_Name(String strURL) throws MalformedURLException, IOException{
		URLConnection urlConnection = new URL(strURL).openConnection();
		Map<String, List<String>> map = urlConnection.getHeaderFields();
		Set<String> keys = map.keySet();
		String line;
		Iterator<String> iterator = keys.iterator();

		// 遍历,查找字符编码
		String key = null;
		String tmp = null;
		String strencoding = null;
		while (iterator.hasNext()) {
		    key = iterator.next();
		    tmp = map.get(key).toString().toLowerCase();
		    // 获取content-type charset
		    if (key != null && key.equals("Content-Type")) {
		        int m = tmp.indexOf("charset=");
		        if (m != -1) {
		            strencoding = tmp.substring(m + 8).replace("]", "");
		            return strencoding;
		        }
		    }
		}
		return null;
	}

	public static void main(String[] args) throws IOException {
		String strURLString = "http://www.baidu.com";
		System.out.println(CharSet_Name(strURLString));
	}

}
