package com.jbl.browser.fragment;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.adapter.MultipageAdapter;
import com.viewpager.indicator.LinePageIndicator;
import com.viewpager.indicator.PageIndicator;
public class MultipageFragment extends SherlockFragment{
	public final static String TAG="MultipageFragment";
	private ViewPager multiViewPager;//多页效果
	PageIndicator multipageIndicator;
	private ArrayList<WebView> mViewPages;
	private LayoutInflater mInflater;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.multipage_panel, container, false);
		multiViewPager=(ViewPager) view.findViewById(R.id.multipage_viewpager);
		multipageIndicator=(LinePageIndicator)view.findViewById(R.id.multipage_indicator);
		multiPage();
		return view;
	}
	private void multiPage() {
			mViewPages = new ArrayList<WebView>();
	        addView(mViewPages, "http://www.baidu.com");
			addView(mViewPages, "http://www.baidu.com");
			addView(mViewPages, "http://www.baidu.com");
			multiViewPager.setAdapter(new MultipageAdapter(mViewPages));
			multipageIndicator.setViewPager(multiViewPager);
	} 
	private void addView(ArrayList<WebView> viewList,String url){
			 WebView webView=new WebView(getActivity());
			 webView.loadUrl(url);
			 viewList.add(webView);
			 webView.getSettings().setJavaScriptEnabled(true);
			 webView.getSettings().setAppCacheMaxSize(8 * 1024 * 1024);
			 webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
			 webView.getSettings().setPluginState(PluginState.ON);	 
    }
}
