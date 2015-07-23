package com.search.control;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.htmlparser.Node;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.ParserException;

/**
 * 

 *
 */

public class parserHTML {
	
	PrototypicalNodeFactory typicalFactory = new PrototypicalNodeFactory();
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		parserHTML mthp = new parserHTML();
		String contents = null ;
		
		File file = new File("E:\\course\\eclipse-workspace\\web1\\");
		File[]files = file.listFiles() ;
		for(int i = 0 ; i < files.length ; i++){
			String filepath = files[i].getPath() ;
			String filename = files[i].getName() ;
			StringBuffer sbStr = new StringBuffer();
			BufferedReader reader  = new BufferedReader(new FileReader(new File(filepath)));
			String temp = "";
			while((temp=reader.readLine())!=null)
			{
				sbStr.append(temp);
				sbStr.append("\r\n");
			}
			reader.close();
			String result = sbStr.toString() ;
			try {
				String type=findCharset(result,"UTF-8");
				System.out.print(type);
				contents = mthp.parseHTML(result, "charset=utf-8");         //call the function parseHTML()
			
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			BufferedWriter writer = new BufferedWriter(new FileWriter("E:\\course\\eclipse-workspace\\doc\\" + filename));
			writer.write(contents);
			writer.close() ;
			
			
		}
		
		
		
		
	}
	
	 public static String getCharset (String content) {
	        final String CHARSET_STRING = "charset";
	        int index;
	        String ret;

	        ret = null;
	        if (null != content) {
	            index = content.indexOf (CHARSET_STRING);

	            if (index != -1){
	                content = content.substring (index + CHARSET_STRING.length ()).trim ();
	                if (content.startsWith ("=")) {
	                    content = content.substring (1).trim ();
	                    index = content.indexOf (";");
	                    if (index != -1)
	                        content = content.substring (0, index);

	                    //remove any double quotes from around charset string
	                    if (content.startsWith ("\"") && content.endsWith ("\"") && (1 < content.length ()))
	                        content = content.substring (1, content.length () - 1);

	                    //remove any single quote from around charset string
	                    if (content.startsWith ("'") && content.endsWith ("'") && (1 < content.length ()))
	                        content = content.substring (1, content.length () - 1);

	                    ret = findCharset(content, ret);
	                }
	            }
	        }

	      return ret;
	}
	 
	    @SuppressWarnings("unchecked")
		public static String findCharset (String name, String fallback){
	        String ret;

	        try{
	            Class cls;
	            Method method;
	            Object object;

	            cls = Class.forName ("java.nio.charset.Charset");
	            method = cls.getMethod ("forName", new Class[] { String.class });
	            object = method.invoke (null, new Object[] { name });
	            method = cls.getMethod ("name", new Class[] { });
	            object = method.invoke (object, new Object[] { });
	            ret = (String)object;
	        }
	        catch (ClassNotFoundException cnfe){
	            // for reflection exceptions, assume the name is correct
	            ret = name;
	        }
	        catch (NoSuchMethodException nsme){
	            // for reflection exceptions, assume the name is correct
	            ret = name;
	        }
	        catch (IllegalAccessException ia){
	            // for reflection exceptions, assume the name is correct
	            ret = name;
	        }
	        catch (InvocationTargetException ita){
	            // java.nio.charset.IllegalCharsetNameException
	            // and java.nio.charset.UnsupportedCharsetException
	            // return the default
	            ret = fallback;
	            System.out.println (
	                "unable to determine cannonical charset name for "
	                + name
	                + " - using "
	                + fallback);
	        }

	        return (ret);
	    }
	 
 	
	public String  parseHTML(String uc,String contentType) throws ParserException{
		String title="";
		StringBuilder body = new StringBuilder();
		
		Node node;
		String stringText;
		String charSet = getCharset (contentType);
		Lexer lexer = null;
		if(charSet!=null){
			
			lexer = new Lexer (new Page(uc , charSet));
		}
		else{
			
			lexer = new Lexer (new Page(uc, "utf-8" ));
		}
		
		lexer.setNodeFactory(typicalFactory);
		
		while (null != (node = lexer.nextNode ())) {        //从这里开始就是对文本的内容不停的进行提取
			//omit script tag
			if (node instanceof org.htmlparser.tags.ScriptTag){
				
				while (null != (node = lexer.nextNode ())){
					
					if (node instanceof org.htmlparser.Tag){
						
						org.htmlparser.Tag tag  = (org.htmlparser.Tag)node;
						if( tag.isEndTag() && "SCRIPT".equals(tag.getTagName())  ){
							
				////////			System.out.println("tagname:"+tag.getTagName());
							break;
						}
					}
				}
				if(null == node)
					break;
				}
			//omit script tag
			else if (node instanceof org.htmlparser.tags.StyleTag){
				
				while (null != (node = lexer.nextNode ())){
					if (node instanceof org.htmlparser.Tag){
						org.htmlparser.Tag tag  = (org.htmlparser.Tag)node;
						if( tag.isEndTag())
							break;
					}
				}
				if(null == node)
					break;
				}
			else if (node instanceof TextNode){
				
				stringText = node.toPlainTextString();
				stringText = stringText.replaceAll("[ \t\n\f\r　]+"," ");
				stringText = TextHtml.html2text(stringText.trim());
				//System.out.println(! "".equals(stringText));
				
				if (! "".equals(stringText)){
					//System.out.println("stringText.len:"+stringText.length());
					body.append(stringText);					
					body.append(" ");
					//System.out.println(body);         //-----test point-----------------------//
				}
			}
			else if (node instanceof TagNode){
			
				TagNode tagNode = (TagNode)node;
				String name = ((TagNode)node).getTagName();
					if(name.equals("OPTION")){
						//omit option
						lexer.nextNode ();
						lexer.nextNode ();
			//////////			System.out.println("tag name:"+name);
					}
					else if (name.equals("A") && !tagNode.isEndTag()){
							//String href = tagNode.getAttribute("HREF");
							
							String urlDesc = null;
							node = lexer.nextNode ();
							if(node instanceof TextNode){
								
								TextNode sNode = (TextNode)node;
								String t = sNode.getText().trim();
									
								if(title.length()>=4){
										urlDesc= t;
										System.out.println("next node:"+title);
								}
							}
						
						}
				}
			}
	//System.out.println(body) ;
	return body.toString() ;
	}
	
}
