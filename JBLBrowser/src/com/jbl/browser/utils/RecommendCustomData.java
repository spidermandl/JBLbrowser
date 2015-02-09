package com.jbl.browser.utils;

import java.util.ArrayList;
import java.util.List;

import com.jbl.browser.R;

public class RecommendCustomData {

	public static List<Integer> image=new ArrayList<Integer>();
	public static List<String> urlName=new ArrayList<String>();
	public static List<String> urlAddress=new ArrayList<String>();
	
	static{
	image.add(R.drawable.ic_launcher);
	image.add(R.drawable.ic_launcher);
	image.add(R.drawable.ic_launcher);
	image.add(R.drawable.ic_launcher);
	image.add(R.drawable.ic_launcher);
	image.add(R.drawable.ic_launcher);
	image.add(R.drawable.ic_launcher);
	
	urlName.add("百度");
	urlName.add("搜狗");
	urlName.add("谷歌");
	urlName.add("新浪");
	urlName.add("腾讯");
	urlName.add("雅虎");
	urlName.add("搜索");
	
	urlAddress.add("Http://www.baidu.com");
	urlAddress.add("Http://www.sogou.com");
	urlAddress.add("Http://www.gugoo.com");
	urlAddress.add("Http://www.xinlang.com");
	urlAddress.add("Http://www.tengxun.com");
	urlAddress.add("Http://www.yahuu.com");
	urlAddress.add("Http://www.sousuo.com");
	}
}
