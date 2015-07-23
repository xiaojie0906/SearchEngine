<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.lang.*" %>
<%@ page import="com.search.control.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Result</title>
<style type="text/css">
p.thick {font-weight: bold}
p.big
{
line-height: 8
}
p {font-size:20px;}
</style>

</head>
<body text = "black" link = "green" >
<form  action="search" method="POST">
	<div ><input type="text" name="search" style = "width:440px;height:30px;"/> 
	<input type="submit" value="Yunsearch"style = "height:30px;"/>
	<input type="hidden" name="pageno" value="1"/>
	</div>
</form>

<%


	//String keyword = "";

request.setCharacterEncoding("UTF-8");
String keyword =request.getParameter("search");

ArrayList<String[]> urlList= new ArrayList<String[]>();
//urlList.clear();
urlList = MySearch.createSearch(keyword);
System.out.print("keyword = "+keyword+"\n");

int pageno = Integer.parseInt(request.getParameter("pageno"));
int i, j = pageno;

//out.print("Page: " + pageno);
System.out.print("list size "+urlList.size()+"\nj="+j+"\n");
if(0!= urlList.size()){
if(j * 15 < urlList.size())
{
	j--;
	for(i = 0;i < 15;i++ ){
		out.print("<br>");
		out.print("<a href = ");  
		out.print((urlList.get(i + j * 15)[0]));
		out.print(">");
		out.print((urlList.get(i + j * 15)[0]));
		out.print("</a>");
		
		out.print("<br>");
		out.print((urlList.get(i + j * 15)[1]));
		out.print("<br>");
		out.print("<br>");
		out.print("PageRank X 10000 : "+(urlList.get(i + j * 15)[2]));
		out.print("<br>");
	}
}
else{
	for(i = 0;i < urlList.size();i++ ){
		out.print("<br>");
		out.print("<a href = ");  
		out.print((urlList.get(i )[0]));
		out.print(">");
		out.print((urlList.get(i )[0]));
		out.print("</a>");
		
		out.print("<br>");
		out.print((urlList.get(i)[1]));
		out.print("<br>");
		out.print("<br>");
		out.print("PageRank X 10000 : "+(urlList.get(i )[2]));
		out.print("<br>");
	}
	
	//response.setContentType("text/html; charset=UTF-8");//ISO-8859-1
	//RequestDispatcher rd = request.getRequestDispatcher("result.jsp");
	//rd.forward(request, response);	
	}
}
else
	out.print("no result");
%>
<br>
<form  action="search" method="POST">
	<input type="hidden" name="search" value="<%=request.getParameter("search") %>"/>
	<input type="submit" value="Next" style = "width:100px;height:30px;"/>
	<input type="hidden" name="pageno" value="<%=Integer.parseInt(request.getParameter("pageno")) + 1%>"/>
</form>

</body>
</html>