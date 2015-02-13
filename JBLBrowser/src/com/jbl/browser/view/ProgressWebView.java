package com.jbl.browser.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.jbl.browser.activity.RecommendMainActivity;
import com.jbl.browser.bean.History;
import com.jbl.browser.db.HistoryDao;
import com.jbl.browser.utils.UrlUtils;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * 带进度条的webview
 * @author yyjoy-mac3
 *
 */
public class ProgressWebView extends WebView {

	private Context mContext;
	private ProgressBar progressbar;
	private String webName;//当前网页名
	private String curUrl;//当前网页
	
	public ProgressWebView(Context context) {
		super(context);
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
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 3, 0, 0));
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
		
    }
	
	/* webcilent */
	class MyWebViewClient extends WebViewClient {
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {		
			if(url.contains("www.baidu.com")){
				/* 主页百度url拦截*/
				Intent in=new Intent();
				in.setClass(mContext,RecommendMainActivity.class);
				mContext.startActivity(in);
				return true;
			}
			return false;
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			curUrl=url;
			String date = new SimpleDateFormat("yyyyMMdd", Locale.CHINA)
					.format(new Date()).toString();
			String temp=UrlUtils.URL_GET_HOST.substring(0, UrlUtils.URL_GET_HOST.length());
			if(!url.equals(temp)){      //当加载默认网址时不加入历史记录
				History history = new History();
				history.setWebAddress(url);
				history.setWebName(webName);
				// 加载完加入历史记录
				new HistoryDao(mContext).addHistory(history);
			}
			super.onPageFinished(view, url);
		}
	}
	

}