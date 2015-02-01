package com.jbl.browser;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbl.browser.R;

public class TextRegisterActivity extends Activity {

	/*
	 *            注册界面
	 * 
	 *  1.title_top
	 *   1.1 mImageView_top  安全图标
	 *   1.2 mTextView_top   显示title
	 *   1.3 mButton_top     刷新页面
	 *  2.title
	 *   2.1 mImageViewBack_t 返回图标
	 *   2.2 mImageViewMain title图标
	 *   2.3 mButton        注册按钮 
	 *  3.webview
	 *   3.1 mWebView
	 *  4.操作栏
	 *   4.1 mImageViewBack   后退
	 *   4.2 mImageViewInto   前进
	 *   4.3 mImageViewHome   Home
	 *   4.4 mImageViewChange 切换多页模式
	 *   4.5 mImageViewOption 选项菜单 
	 * */
	
	/*     1 title_top  
	 * 1.1 图案图标
	 * */
	private ImageView mImageView_top;	
	/* 1.2 显示title */
	private TextView mTextView_top;
	/* 1.3 刷新页面    */
	private Button mButton_top;
	/*  
	 *     2 title
	 *  2.1 mImageViewBack_t  返回图标 
	 * */
	private ImageView mImageViewBack_t;
	/*  2.2 mImageViewMain  title图标*/
	private ImageView mImageViewMain;
	/*  2.3 mButton        注册按钮 */
	private Button mButton;
	/* 
	 * 3 WebView
	 *   3.1 mWebView 
	 *  */
	private WebView mWebView;
	/* 4 定义操作栏控件   */
	private ImageView mImageViewBack;  // 4.1 mImageViewBack   后退
	private ImageView mImageViewInto;  // 4.2 mImageViewInto   前进
	private ImageView mImageViewHome;  // 4.3 mImageViewHome   Home
	private ImageView mImageViewChange;// 4.4 mImageViewChange 切换多页模式
	private ImageView mImageViewOption;// 4.5 mImageViewOption 选项菜单
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_register);
		/*  1 title top*/
		mImageView_top=(ImageView)findViewById(R.id.register_mImageView_top);
		mTextView_top=(TextView)findViewById(R.id.register_mTextView_top);
		mButton_top=(Button)findViewById(R.id.register_mButton_top);
		/*  2 tilte  */
		mImageViewBack_t=(ImageView)findViewById(R.id.register_mImageViewBack_t);
		mImageViewMain=(ImageView)findViewById(R.id.register_mImageViewMain);
		mButton=(Button)findViewById(R.id.register_mButton);
		/*  3 webview*/
		mWebView=(WebView)findViewById(R.id.register_mWebView);
		/*  4 操作栏  */
		mImageViewBack=(ImageView)findViewById(R.id.register_mImageViewBack);  // 4.1 mImageViewBack   后退
		mImageViewInto=(ImageView)findViewById(R.id.register_mImageViewInto);  // 4.2 mImageViewInto   前进
		mImageViewHome=(ImageView)findViewById(R.id.register_mImageViewHome);  // 4.3 mImageViewHome   Home
		mImageViewChange=(ImageView)findViewById(R.id.register_mImageViewChange); // 4.4 mImageViewChange 切换多页模式
		mImageViewOption=(ImageView)findViewById(R.id.register_mImageViewOption); // 4.5 mImageViewOption 选项菜单
		
		/*  对webview进行http请求  */
		setWebStyle();
		/*  对注册进行监听跳转  */
		mButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		/*  4.1 返回监听  */
		mImageViewBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mWebView.goBack();
			}
		});
		
		/*  4.2 前进监听   */
		mImageViewInto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		/*  4.3 返回home主界面  */
		mImageViewHome.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		/*  4.4 切换多页模式  */
		mImageViewChange.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		/*  4.5 选项菜单 */
		mImageViewOption.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void setWebStyle() {
		// TODO Auto-generated method stub
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebView.requestFocus();
		mWebView.loadUrl("http://www.hmudq.edu.cn/");  //链接到哈医大主页
		mWebView.setWebViewClient(new MyWebViewClient());
	}
	/*       webcilent         */
	class MyWebViewClient extends WebViewClient{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view,String url_){
			view.loadUrl(url_);
			return true;
		}
	}
}
