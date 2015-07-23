package com.search.control;

import java.io.IOException;
import java.util.ArrayList;

public class yyquery {
	static public ArrayList<String[]> myQuerry(String keyword) throws IOException {
	ArrayList<String[]> urllist = new ArrayList<String[]>();
	String[] tmp = { "http://www.baidu.com", "baidu" };
	tmp[0] = "http://www.baidu.com";
	tmp[1] = "Baidu";
	urllist.add(tmp);
	//urllist=MySearch.createSearch(keyword);
	return urllist;
	}
	static public ArrayList<String[]> Querry(String keyword) {
		ArrayList<String[]> urllist = new ArrayList<String[]>();
		String[] tmp = { "http://www.baidu.com", "baidu" };
		tmp[0] = "http://www.baidu.com";
		tmp[1] = "Baidu";
		urllist.add(tmp);

		int i;
		for (i = 0; i < 30; i++) {
			String[] tmp1 = { "", "" };
			tmp1[0] = "http://www.sina.com";
			tmp1[1] = "sina";
			urllist.add(tmp1);
		}
		for (i = 30; i < 2000; i++) {
			String[] tmp1 = { "", "" };
			tmp1[0] = "http://www.qq.com";
			tmp1[1] = "QQ";
			urllist.add(tmp1);
		}
		return urllist;
	}
}