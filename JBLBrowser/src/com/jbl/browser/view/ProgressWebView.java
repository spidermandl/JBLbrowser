package com.jbl.browser.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jbl.browser.activity.RecommendMainActivity;
import com.jbl.browser.bean.History;
import com.jbl.browser.db.HistoryDao;
import com.jbl.browser.interfaces.LoadURLInterface;
import com.jbl.browser.utils.BrightnessSettings;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.JBLPreference.BoolType;
import com.jbl.browser.utils.UrlUtils;

/**
 * 带进度条的webview
 * @author yyjoy-mac3
 *
 */
public class ProgressWebView extends WebView {

	/**
	 * 载入url接口
	 */
	private LoadURLInterface urlInterface;
	
	private Context mContext;
	private ProgressBar progressbar;
	private String webName;//当前网页名
	private String curUrl;//当前网页

	public ProgressWebView(Context context) {
		super(context);
		this.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT ));
		init(context);
	}
	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public ProgressWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context){
		mContext = context;
		progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5, 0, 0));
        addView(progressbar);
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new MyWebViewClient());
	}

	@Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
	public String getCurrentUrl(){
		return curUrl;
	}
	
	public String getWebName(){
		return webName;
	}
	
	public void setInterface(LoadURLInterface i){
		this.urlInterface=i;
	}
	
	@Override
	public void loadUrl(String url) {
		if(url.contains("jbl")){
			/**
			 * jbl关键字截断
			 */
			Intent in=new Intent();
			in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			in.setClass(mContext,RecommendMainActivity.class);
			mContext.startActivity(in);
			return;
		}
		if (url.equals(this.getCurrentUrl())){
			//不重复载入网页
			return;
		}
		super.loadUrl(url);
	}
	
	public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

		@Override
		public void onReceivedTitle(WebView view, String title) {
			// TODO Auto-generated method stub
			super.onReceivedTitle(view, title);
			webName = title;
		}
		@Override
		public void onReceivedIcon(WebView view, Bitmap icon) {
			// TODO Auto-generated method stub
			super.onReceivedIcon(view, icon);
			
		}
		
    }
	
	/* webcilent */
	class MyWebViewClient extends WebViewClient {
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {		
			/**
			 * loadurl方法不会触发此方法的回调
			 */
			return super.shouldOverrideUrlLoading(view, url);
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			curUrl=url;
			if(urlInterface!=null)
				urlInterface.startPage(url);
			
			super.onPageStarted(view, url, favicon);
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			if(urlInterface!=null){
				urlInterface.stopPage(view,url);
			}
			
			super.onPageFinished(view, url);
		}
	}
}
