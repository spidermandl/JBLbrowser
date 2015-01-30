package com.jbl.browser.activity;

import com.jbl.browser.MyPagerAdapter;
import com.jbl.browser.R;
import com.jbl.browser.ViewPagerPresenter;
import com.jbl.browser.R.id;
import com.jbl.browser.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainPageActivity extends Activity {

	/**
	 * 1.title
	 *   1.1 mImageViewSearch  搜索图标
	 *   1.2 mEditTextInput    输入网址
	 *   1.3 mButtonCode       二维码搜索
	 *   1.4 mButtonLand       登陆注册
	 * 2.webview
	 *   2.1 mWebView
	 * 3.操作栏
	 *   3.1 mImageViewBack   后退
	 *   3.2 mImageViewInto   前进
	 *   3.3 mImageViewHome   Home
	 *   3.4 mImageViewChange 切换多页模式
	 *   3.5 mImageViewOption 选项菜单 
	 */
	
	/*  定义菜单控件  */
	private ImageView mImageViewSearch; //1.1  mImageViewSearch  搜索图标
	private EditText mEditTextInput;  //1.2 mEditTextInput   输入网址
	private Button mButtonCode;  //1.3 mButtonCode       二维码搜索
	private Button mButtonLand;  //1.4 mButtonLand       登陆注册
	/*  定义webview控件   */
	//private WebView mWebView; //主控件  webview
	/*  定义操作栏控件   */
	private ImageView mImageViewBack;  // 3.1 mImageViewBack   后退
	private ImageView mImageViewInto;  // 3.2 mImageViewInto   前进
	private ImageView mImageViewHome;  // 3.3 mImageViewHome   Home
	private ImageView mImageViewChange;// 3.4 mImageViewChange 切换多页模式
	private ImageView mImageViewOption;// 3.5 mImageViewOption 选项菜单
	 private static final String TAG = "ViewPagerTestActivity";  
	 private ViewPager mViewPager;  //viewpager实现水平滑动效果
	 private PagerAdapter mPageAdapter;  
	 private ViewPagerPresenter mPresenter;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mImageViewSearch=(ImageView)findViewById(R.id.mImageViewSearch); //1.1  mImageViewSearch  搜索图标
		mEditTextInput=(EditText)findViewById(R.id.mEditTextInput); //1.2 mEditTextInput   输入网址
		mButtonCode=(Button)findViewById(R.id.mButtonCode);//1.3 mButtonCode       二维码搜索
		mButtonLand=(Button)findViewById(R.id.mButtonLand); //1.4 mButtonLand       登陆注册
		//mWebView=(WebView)findViewById(R.id.mWebView); //webview
		mImageViewBack=(ImageView)findViewById(R.id.mImageViewBack);  // 3.1 mImageViewBack   后退
		mImageViewInto=(ImageView)findViewById(R.id.mImageViewInto);  // 3.2 mImageViewInto   前进
		mImageViewHome=(ImageView)findViewById(R.id.mImageViewHome);  // 3.3 mImageViewHome   Home
		mImageViewChange=(ImageView)findViewById(R.id.mImageViewChange); // 3.4 mImageViewChange 切换多页模式
		mImageViewOption=(ImageView)findViewById(R.id.mImageViewOption); // 3.5 mImageViewOption 选项菜单
		 init();//调用滑动方法
		/*   设置title各个控件监听       */
		/* 1.1 search */
		mImageViewSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		/*   1.2 输入网址 获取网址信息   url为获取到的输入网址  为1.1 search获得数据*/
		String url=mEditTextInput.getText().toString();
		
		/*   1.3 点击事件 启动二维码扫描  */
		mButtonCode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		/*  1.4 设置监听事件  启动注册登陆 */
		mButtonLand.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				//intent.setClass(MainPageActivity.this, CaptureActivity.class);
				startActivity(intent);
			}
		});
		
		/* 2.0 WebView touch监听 */
		/*mWebView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});*/
		
		/*  3.1 返回监听  */
		/*mImageViewBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mWebView.goBack();
			}
		});*/
		
		/*  3.2 前进监听   */
		mImageViewInto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		/*  3.3 返回home主界面  */
		mImageViewHome.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		/*  3.4 切换多页模式  */
		mImageViewChange.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		/*  3.5 选项菜单 */
		mImageViewOption.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		    
				
			}
		});
		
		/*  设置webview */
		//setWebStyle();
	} 
	//实现滑动方法
	 private void init()  
	    {  
	        mViewPager = (ViewPager) findViewById(R.id.test_viewpager);  
	        mPresenter = new ViewPagerPresenter(this);  
	        mPageAdapter = new MyPagerAdapter(mPresenter.getPageViews());  
	        mViewPager.setAdapter(mPageAdapter);  
	        mViewPager.setOnPageChangeListener(new OnPageChangeListener(){

				@Override
				public void onPageScrollStateChanged(int arg0) {
					
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onPageSelected(int arg0) {
					// TODO Auto-generated method stub
					
				}
	        	
	        });
	    }  
	/*private void setWebStyle() {
		// TODO Auto-generated method stub
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebView.requestFocus();
		mWebView.loadUrl("http://www.baidu.com/");
		mWebView.setWebViewClient(new MyWebViewClient());
	}*/
/*       webcilent         */
	/*class MyWebViewClient extends WebViewClient{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view,String url_){
			view.loadUrl(url_);
			return true;
		}
	}
*/

	
	
}
