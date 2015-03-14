package com.jbl.browser;

import java.util.LinkedList;
import java.util.Queue;

import android.view.View;
import android.view.ViewGroup;

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
	Queue<WebPair> queue;
	public static WebWindowManagement instance=null;
	private int currentIndex=0;
	
	private WebWindowManagement(){
		queue=new LinkedList<WebPair>();
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
	public ProgressWebView replaceMainWebView(ViewGroup parent){
		return replaceWebViewWithIndex(parent,currentIndex,true);
	}
	
	/**
	 * 
	 * @param index 
	 * @return
	 */
	/**
	 * 获得web页面
	 * @param parent parent中放入webview
	 * @param index 页面索引位置
	 * @param isSort 是否将索引位置放到队列头
	 * @return
	 */
	public ProgressWebView replaceWebViewWithIndex(ViewGroup parent,int index,boolean isSort){
		
		while(index>=queue.size()){
			ProgressWebView webView=new ProgressWebView(JBLApplication.getInstance());
			WebPair pair=new WebPair();
			pair.webView=webView;
			pair.parent=parent;
			if(parent!=null)
				parent.addView(webView);
			queue.add(pair);
		}
		WebPair pair=((LinkedList<WebPair>)queue).get(index);
		if(isSort){//排到队尾
			currentIndex=index;
		}
		
		if(pair.parent!=null)
			pair.parent.removeView(pair.webView);
	
		if(parent!=null)
			parent.addView(pair.webView);
		
		pair.parent=parent;
		
		return pair.webView;
	}
	
	/**
	 * 删除 webview
	 * 返回webview的父节点
	 * @param index
	 */
	public View deleteWebViewWithIndex(int index){
		if(index>=queue.size()||queue.size()==1)//越界或容量为1
			return null;
		
		View parent = ((LinkedList<WebPair>)queue).remove(index).parent;
		if(currentIndex>= queue.size())
			currentIndex-=1;
		return parent;
	}
	/**
	 * 返回当前使用webview的index号
	 * @return
	 */
	public int getCurrentWebviewIndex(){
		return currentIndex;
	}
	
	/**
	 * 设置当前使用webview的index号
	 * @param index
	 */
	public void setCurrentWebviewIndex(int index){
		currentIndex=index;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return queue.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return ((LinkedList<WebPair>)queue).get(arg0);
	}

	public View getView(int position) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * parent 是 webview的父亲控件
	 * @author Desmond
	 *
	 */
	class WebPair{
		ProgressWebView webView;
		ViewGroup parent;
	}
}
