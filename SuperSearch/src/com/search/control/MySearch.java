package com.search.control;

import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.search.highlight.*;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**

 */
public class MySearch {
	public static String result = "";
	public static String dirpath = "E:\\course\\eclipse-workspace\\index";
	public static String docpath = "E:\\course\\eclipse-workspace\\web\\";

	public static ArrayList<UrlItem> UrlItemList = null;

	static String tmpname = new String();
	static String docname = new String();

	// private static String prefixHTML = "<font color='red'>";
	// private static String suffixHTML = "</font>";
	public static void main() throws IOException {
		createSearch("网易");

	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String[]> createSearch(String contents)
			throws IOException {

		if (UrlItemList == null) {
			ArrayList<String[]> tmp_id_url_pr = readCSV
					.readeCsv("E:\\course\\eclipse-workspace\\idtourl\\ID_TO_URL_wangxy.txt");
			UrlItemList = new ArrayList<UrlItem>();// url条目集合

			UrlItem tItem = new UrlItem();
			for (int m = 0; m < 25000; m++) {
				UrlItemList.add(tItem);

			}
			int ind = 1;
			while (ind < tmp_id_url_pr.size())// 存入
			{
				int ID = Integer.parseInt(tmp_id_url_pr.get(ind)[0]);
				UrlItem tmpItem = new UrlItem();
				tmpItem.ID = ID;
				System.out.print(ID);
				tmpItem.url = tmp_id_url_pr.get(ind)[1];
				tmpItem.pagerank = Float.parseFloat(tmp_id_url_pr.get(ind)[2]);
				UrlItemList.set(ID, tmpItem);
				ind++;
			}
		}

//		for (int jj = 0; jj < 200; jj++) {
//			System.out.print(UrlItemList.get(jj).ID + UrlItemList.get(jj).url
//					+ "\n");
//
//		}

		ArrayList<UrlItem> resultList = new ArrayList<UrlItem>();
		Directory dir = FSDirectory.open(new File(dirpath));

		try {
			System.out.print("search for " + contents);

			IndexSearcher search = new IndexSearcher(dir, true);
			long start = new Date().getTime(); // start time
			// QueryParser parser = new QueryParser(Version.LUCENE_29,
			// "contents",
			// new CJKAnalyzer(Version.LUCENE_29));
			Analyzer analyzer = new PaodingAnalyzer();
			QueryParser parser = new QueryParser(Version.LUCENE_29, "contents",
					analyzer);
			Query query = parser.parse(contents);

			TopScoreDocCollector tsdc = TopScoreDocCollector.create(100, false);// 返回多少个结果

			search.search(query, tsdc);// 开始查找
			long end = new Date().getTime(); // end time

			// 用这个进行高亮显示，默认是<b>..</b>
			// 用这个指定<read>..</read>
			SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
					"<b><font color='red'>", "</font></b>");
			// 构造高亮
			// 指定高亮的格式
			// 指定查询评分
			Highlighter highlighter = new Highlighter(simpleHTMLFormatter,
					new QueryScorer(query));
			// 这个一般等于你要返回的，高亮的数据长度
			// 如果太小，则只有数据的开始部分被解析并高亮，且返回的数据也少
			// 太大，有时太浪费了。
			highlighter.setTextFragmenter(new SimpleFragmenter(100));

			// TokenStream tokenStream =
			// TokenSources.getAnyTokenStream(searcher.getIndexReader(), i,
			// "title",new IKAnalyzer());
			ScoreDoc[] hits = tsdc.topDocs().scoreDocs;

			System.out.println("There are : " + hits.length + "results \n");
			// StringBuffer sb = new StringBuffer();

			String highlightstr = "";

			// ArrayList<String[]> textList =new ArrayList<String[]>();

			for (int i = 0; i < hits.length; i++) {

				System.out.println("result " + (i + 1) + " " + hits[i].score
						+ " " + hits[i].doc);
				Document doc = search.doc(hits[i].doc);

				String text = doc.get("contents");
				System.out.println("text is " + text);
				// String value2 = doc.get(FIELD_BODY);
				if (text != null) {

					highlightstr = highlighter.getBestFragment(analyzer,
							"contents", text);

					System.out.println(highlightstr);

				}

				// System.out.print(doc.get("path") + "--:\n");
				// String text = doc.get("contents");
				// System.out.print(text);
				/*
				 * int htmlLength = prefixHTML.length()+suffixHTML.length();
				 * System.out.println("高亮HTML的总长度为"+htmlLength);
				 * SimpleHTMLFormatter simpleHTMLFormatter = new
				 * SimpleHTMLFormatter(prefixHTML, suffixHTML);
				 * 
				 * Highlighter highlighter = new
				 * Highlighter(simpleHTMLFormatter,new QueryScorer(query));
				 * String highLightText =
				 * highlighter.getBestFragment(analyzer,"contents",text);
				 */// /////
				tmpname = doc.get("path");
				docname = tmpname.substring(docpath.length() + 1,
						tmpname.length());
				System.out.print("name:" + docname + "\n");
				String searchid = docname
						.substring(0, docname.lastIndexOf('.'));
				System.out.print(searchid + highlightstr);

				System.out.println(searchid);// 需要查找的ID
				int searchID = Integer.parseInt(searchid);
				UrlItem tmpUrlItem = UrlItemList.get(searchID);
				tmpUrlItem.content = highlightstr;
				resultList.add(tmpUrlItem);

				resultList = sort(resultList);

				// 注意，arrayList中是将元素的地址添加进去，如果oneResult不是每次申请新的话，所有
				// arrayList中保存的将会是相同的内容

				// result += "the " + (i+1) + " " + hits[i].score + " " +
				// hits[i].doc + '\n';
			}
			System.out.println(" ");
			System.out.println(" ");
			// result += "total time for serching: " + (end-start) + " 毫秒。" ;
			System.out.print("total time for serching: " + (end - start)
					+ " 毫秒。\n");

			// /resultList sort

			/*
			 * TopDocs docs=search.search(query, 100); ScoreDoc[]
			 * hits2=docs.scoreDocs; System.out.println(hits2.length);
			 * 
			 * for(int i=0;i<hits2.length;i++){ Document
			 * doc=search.doc(hits2[i].doc);
			 * System.out.print(doc.get("path")+"--:\n"); //
			 * System.out.println(doc.get("contents")+"\n"); }
			 */

			search.close();
			dir.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		// System.out.print("in mysearch size ="+urlList.size()+"\n");
		ArrayList<String[]> urllist = new ArrayList<String[]>();
		for (int p = 0; p < resultList.size(); p++) {
			String[] tmpstr = { "", "", "" };
			tmpstr[0] = resultList.get(p).url;
			tmpstr[1] = resultList.get(p).content;
			tmpstr[2] = String.valueOf(resultList.get(p).pagerank);
			urllist.add(tmpstr);

		}
		return urllist;
	}

	public static ArrayList<UrlItem> sort(ArrayList<UrlItem> oldlist) {
		int size = oldlist.size();
		int max_pr_index;
		for (int i = 0; i < size; i++) {
			max_pr_index = i;
			for (int j = i; j < size; j++) {
				if (oldlist.get(max_pr_index).pagerank < oldlist.get(j).pagerank)
					max_pr_index = j;
			}

			UrlItem tmp = new UrlItem();
			tmp = oldlist.get(i);// 交换第i个位置与当前最大项
			oldlist.set(i, oldlist.get(max_pr_index));
			oldlist.set(max_pr_index, tmp);
		}

		return oldlist;

	}

}