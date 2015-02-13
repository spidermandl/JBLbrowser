package com.jbl.browser.view;

import android.R.string;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
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
		// TODO Auto-generated constructor stub
	}
	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public ProgressWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	
	private void init(Context context){
		mContext = context;
		progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 3, 0, 0));
        addView(progressbar);
	}

	@Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
