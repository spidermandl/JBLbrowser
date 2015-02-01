package com.jbl.browser.model;
/**
 * 浏览记录实体类
 * @author Administrator
 *
 */
public class HistoryBean {
	//网页名
	private String name;
	//网页地址
	private String url;
	//浏览网页时间
	private int time;
	
	public HistoryBean(String name, String url, int time) {
		super();
		this.name = name;
		this.url = url;
		this.time = time;

	}

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}

}
