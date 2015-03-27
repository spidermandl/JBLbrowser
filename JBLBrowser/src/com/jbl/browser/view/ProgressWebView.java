package com.jbl.browser.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jbl.browser.interfaces.LoadURLInterface;

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
	private ViewGroup.LayoutParams defaultLayoutParams;
	private String webName;//当前网页名
	private String curUrl;//当前网页
	private boolean blockTouch=false;

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
		defaultLayoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5));
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
	
	/**
	 * 主页页面设置属性
	 */
	public void setDefaultSetting(){
		progressbar.setVisibility(View.VISIBLE);
		setVerticalScrollBarEnabled(true);
		setHorizontalScrollBarEnabled(true);
		getLayoutParams().width=ViewGroup.LayoutParams.MATCH_PARENT;
		getLayoutParams().height=ViewGroup.LayoutParams.MATCH_PARENT;
	    //setLayoutParams(defaultLayoutParams);
		getSettings().setSupportZoom(true);
		getSettings().setJavaScriptEnabled(true);
		getSettings().setAppCacheMaxSize(8 * 1024 * 1024);
		getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		// webView.getSettings().setPluginsEnabled(true);
		getSettings().setPluginState(PluginState.ON);

		blockTouch=false;
		/*//监听物理返回键
		setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				 if (event.getAction() == KeyEvent.ACTION_DOWN) {
					 if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack()) {
						 goBack(); //goBack()表示返回WebView的上一页面  
				         return true;  
					 }else{
						 System.exit(0);
					 }
				 }
				return false;
			}
		});*/
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(blockTouch)
			return true;
		return super.onInterceptTouchEvent(ev);
	}
	/**
	 * 滑动页面webview属性
	 */
	public void setScrollSetting(){
		setVerticalScrollBarEnabled(false);
		setHorizontalScrollBarEnabled(false);
		progressbar.setVisibility(View.INVISIBLE);
		blockTouch=true;
	}
	
	public String getWebName(){
		return webName;
	}
	
	public void setInterface(LoadURLInterface i){
		this.urlInterface=i;
	}
	
	@Override
	public void loadUrl(String url) {
//		if(url.contains("jbl")){
//			/**
//			 * jbl关键字截断
//			 */
//			Intent in=new Intent();
//			in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			in.setClass(mContext,RecommendMainActivity.class);
//			mContext.startActivity(in);
//			return;
//		}
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
