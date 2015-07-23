package com.search.control;

import java.util.Comparator;


	public class SortList implements Comparator<Object > {
		 public int compare(Object o1, Object o2) {
		  UrlItem item1 = (UrlItem) o1;
		  UrlItem item2 = (UrlItem) o2;
		  if (item1.ID*1000>item2.ID*1000)
		   return -1;
		  else
		  return 0;
		 }
		}

