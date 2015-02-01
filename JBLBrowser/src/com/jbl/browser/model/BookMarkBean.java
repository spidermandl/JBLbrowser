package com.jbl.browser.model;
/**
 * 书签实体类
 * @author Administrator
 *
 */
public class BookMarkBean {
	//网址
	private String url;
	//网页名
	private String name;
	
	public BookMarkBean(String url, String name) {
		super();
		this.url = url;
		this.name = name;
	}
	
	public BookMarkBean() {
		
	}

	public String getName() {
		return name;
	}
	public String getUrl() {
		return url;
	}	
	public void setName(String name) {
		this.name = name;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
