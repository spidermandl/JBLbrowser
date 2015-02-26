package com.jbl.browser.fragment;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.adapter.MultipageAdapter;
import com.jbl.browser.view.ProgressWebView;
import com.viewpager.indicator.CirclePageIndicator;
public class MultipageFragment extends SherlockFragment{
	
	public final static String TAG="MultipageFragment";
	
	private ViewPager multiViewPager;//多页效果
	CirclePageIndicator multipageIndicator;
	private ArrayList<WebView> mViewPages;
	private LayoutInflater mInflater;
	ProgressWebView webView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_multipage, container, false);
		multiViewPager=(ViewPager) view.findViewById(R.id.multipage_viewpager);
		multipageIndicator=(CirclePageIndicator)view.findViewById(R.id.multipage_indicator);
		multiPage();
		return view;
	}
	private void multiPage() {
			mViewPages = new ArrayList<WebView>();
	        addView(mViewPages, "http://www.baidu.com");
			addView(mViewPages, "http://www.hao123.com");
			addView(mViewPages, "http://www.taobao.com");
			multiViewPager.setAdapter(new MultipageAdapter(mViewPages));
			multipageIndicator.setViewPager(multiViewPager);
	} 
	private void addView(ArrayList<WebView> viewList,String url){
		     webView=new ProgressWebView(getActivity());
			 webView.loadUrl(url);
			 viewList.add(webView);
			 webView.getSettings().setJavaScriptEnabled(true);
			 webView.getSettings().setAppCacheMaxSize(8 * 1024 * 1024);
			 webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
			 webView.getSettings().setPluginState(PluginState.ON);	 
    }
	//覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {  
        	webView.goBack(); //goBack()表示返回WebView的上一页面  
            return true;  
        }  
        return false; 
    }
}
