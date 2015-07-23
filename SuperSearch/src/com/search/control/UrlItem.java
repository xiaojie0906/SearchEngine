package com.search.control;

public class UrlItem {
int ID;
String url;
String content;
float pagerank;

public void setID(int id)
{
	ID=id;
}
public void setUrl(String url)
{
	this.url=url;;
}
public void setContent(String content)
{
	this.content=content;;
}
public void setUrl(float pr)
{
	this.pagerank=pr;
}
}
