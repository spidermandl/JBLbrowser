package com.jbl.browser.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.jbl.browser.activity.RecommendMainActivity;
import com.jbl.browser.bean.History;
import com.jbl.browser.db.HistoryDao;
import com.jbl.browser.fragment.BottomMenuFragment;
import com.jbl.browser.fragment.TopMenuFragment;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.UrlUtils;

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
	private PopupWindow popWindow;
	private View popview;
	private BottomMenuFragment toolbarFragment;
	private TopMenuFragment topActionbarFragment;
	private FragmentManager fragmentManager;
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
	public void setPopWindow(PopupWindow popWindow) {
		this.popWindow = popWindow;
	}
	public void setPopview(View popview) {
		this.popview = popview;
	}
	public void setToolbarFragment(BottomMenuFragment toolbarFragment) {
		this.toolbarFragment = toolbarFragment;
	}
	public void setTopActionbarFragment(TopMenuFragment topActionbarFragment) {
		this.topActionbarFragment = topActionbarFragment;
	}
	public void setFragmentManager(FragmentManager fragmentManager) {
		this.fragmentManager = fragmentManager;
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
	
	@Override
	public void loadUrl(String url) {
		if(url.contains("jbl")){
			/**
			 * jbl关键字截断
			 */
			Intent in=new Intent();
			in.setClass(mContext,RecommendMainActivity.class);
			mContext.startActivity(in);
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
			if(JBLPreference.getInstance(mContext).readInt(JBLPreference.FULL_SCREEN_TYPE)==0){  //全屏模式
				if(url.equals(UrlUtils.URL_GET_HOST)){                //主页：显示上下菜单栏，不显示悬浮按钮
					if(toolbarFragment!=null&&topActionbarFragment!=null){
						fragmentManager.beginTransaction().show(toolbarFragment).show(topActionbarFragment).commit();
	            	if(popWindow!=null&&popWindow.isShowing()){
	            		popWindow.dismiss(); }
					}
				}else{                                              //不是主页：不显示上下菜单栏，显示悬浮按钮
					if(toolbarFragment!=null&&topActionbarFragment!=null){	
						fragmentManager.beginTransaction().hide(toolbarFragment).hide(topActionbarFragment).commit();
						if(popWindow!=null){
							popWindow.showAtLocation(popview, Gravity.RIGHT|Gravity.BOTTOM, 0, 60);
						}
					}
				}
			}
			if(JBLPreference.getInstance(mContext).readInt(JBLPreference.TURNING_TYPE)==0){  //翻页模式
				if(url.equals(UrlUtils.URL_GET_HOST)){                //主页：显示上下菜单栏，不显示悬浮按钮
					if(toolbarFragment!=null&&topActionbarFragment!=null){
						fragmentManager.beginTransaction().show(toolbarFragment).show(topActionbarFragment).commit();
	            	if(popWindow!=null&&popWindow.isShowing()){
	            		popWindow.dismiss(); }
					}
				}
			}
			super.onPageStarted(view, url, favicon);
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			if(JBLPreference.getInstance(mContext).readInt(JBLPreference.HISTORY_CACHE_TYPE)==1||
					JBLPreference.getInstance(mContext).readInt(JBLPreference.HISTORY_CACHE_TYPE)==-1){   //判断不是无痕浏览，添加历史记录
				if(url!=""){           
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
				}
			}
			
			super.onPageFinished(view, url);
		}
	}
}
