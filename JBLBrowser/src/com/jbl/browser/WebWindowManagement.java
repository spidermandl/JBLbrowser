package com.jbl.browser;

import java.util.LinkedList;
import java.util.Queue;

import com.jbl.browser.view.ProgressWebView;

/**
 * webView 管理类
 * @author Desmond
 *
 */
public class WebWindowManagement {

	/**
	 * webview队列
	 * 显示的主页面永远在队列第一个
	 */
	Queue<ProgressWebView> queue;
	
	public static WebWindowManagement instance=null;
	
	private WebWindowManagement(){
		queue=new LinkedList<ProgressWebView>();
	}
	
	public static WebWindowManagement getInstance(){
		if(instance==null)
			instance=new WebWindowManagement();
		return instance;
	}
	
	/**
	 * 获得主web页面
	 * @return
	 */
	public ProgressWebView getMainWebView(){
		return getWebViewWithIndex(0);
	}
	
	/**
	 * 获得web页面
	 * @param index 页面索引位置
	 * @return
	 */
	public ProgressWebView getWebViewWithIndex(int index){
		
		while(index>=queue.size()){
			ProgressWebView webView=new ProgressWebView(JBLApplication.getInstance());
			queue.add(webView);
		}
		
		for(int i=0;i<index;i++){
			queue.add(queue.remove());
		}
		
		return queue.element();
	}
}
