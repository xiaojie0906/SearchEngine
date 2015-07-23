package com.search.control;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;

/**
 * <br>
 * 标题: <br>
 * 功能概要: <br>
 * 版权: cityyouth.cn (c) 2005 <br>
 * 公司:上海城市青年网 <br>
 * 创建时间:2005-12-21 <br>
 * 修改时间: <br>
 * 修改原因：
 * 
 * @author 张伟
 * @version 1.0
 */
public class HtmlParserTest 
{
 public static void testHtml() 
 {
  try 
  {
   String sCurrentLine;
   String sTotalString;
   sCurrentLine = "";
   sTotalString = "";
   java.io.InputStream l_urlStream;
   java.net.URL l_url = new java.net.URL("http://blog.sina.com.cn/s/blog_4c687df40102e231.html?tj=1");
   java.net.HttpURLConnection l_connection = (java.net.HttpURLConnection) l_url.openConnection();
   l_connection.connect();
   l_urlStream = l_connection.getInputStream();
   java.io.BufferedReader l_reader = new java.io.BufferedReader(new java.io.InputStreamReader(l_urlStream));
   while ((sCurrentLine = l_reader.readLine()) != null) 
   {
    sTotalString += sCurrentLine;
   }
   System.out.println(sTotalString);
   
   System.out.println("====================");
   String testText = extractText(sTotalString);
   System.out.println(testText);
  }
  catch (Exception e) 
  {
   e.printStackTrace();
  }
 }
 /**
 * 抽取纯文本信息
 * 
 * @param inputHtml
 * @return
 */
 public static String extractText(String inputHtml) throws Exception 
 {
  StringBuffer text = new StringBuffer();
  Parser parser = Parser.createParser(new String(inputHtml.getBytes(),"UTF-8"), "UTF-8");
  // 遍历所有的节点
  NodeList nodes = parser.extractAllNodesThatMatch(new NodeFilter() {
   public boolean accept(Node node) {
    return true;
   }
  });
  Node node = nodes.elementAt(0);
  text.append(new String(node.toPlainTextString().getBytes("UTF-8")));
  return text.toString();
 }
 /**
     * 读取文件的方式来分析内容. filePath也可以是一个Url.
     * 
     * @param resource
     *            文件/Url
     */
    public static void test5(String resource) throws Exception {
        Parser myParser = new Parser(resource);

        // 设置编码
        myParser.setEncoding("UTF-8");
        String filterStr = "table";
        NodeFilter filter = new TagNameFilter(filterStr);
        NodeList nodeList = myParser.extractAllNodesThatMatch(filter);
        TableTag tabletag = (TableTag) nodeList.elementAt(11);
            
            System.out.println(tabletag.toHtml());
            
            System.out.println("==============");

    }

    /*
     * public static void main(String[] args) { TestYahoo testYahoo = new
     * TestYahoo(); testYahoo.testHtml(); }
     */
    public static void main(String[] args) throws Exception {
       // test5("http://sports.yahoo.com/nba/scoreboard");
    	testHtml() ;
    
    }
}
