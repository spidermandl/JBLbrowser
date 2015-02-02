package com.jbl.browser.fragment;

import java.util.concurrent.ScheduledExecutorService;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.hugo.android.scanner.CaptureActivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.jbl.browser.R;
import com.jbl.browser.ViewPagerPresenter;
import com.jbl.browser.activity.BaseFragActivity;
import com.jbl.browser.adapter.MyPagerAdapter;
import com.jbl.browser.activity.*;
/**
 * 浏览器主页
 * @author desmond.duan
 *
 */
public class MainPageFragment extends SherlockFragment{

	public final static String TAG="MainPageFragment";
	
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
	/*  定义菜单控件  
	private ImageView mImageViewSearch; //1.1  mImageViewSearch  搜索图标
	private EditText mEditTextInput;  //1.2 mEditTextInput   输入网址
	private Button mButtonCode;  //1.3 mButtonCode       二维码搜索
	private Button mButtonLand;  //1.4 mButtonLand       登陆注册
*/	/*  定义webview控件   */
	private WebView mWebView; //主控件  webview
	public static String cur_url = "http://www.baidu.com";  //设置初始网址
	/*  定义操作栏控件   */
	private ImageView mImageViewBack;  // 3.1 mImageViewBack   后退
	private ImageView mImageViewInto;  // 3.2 mImageViewInto   前进
	private ImageView mImageViewHome;  // 3.3 mImageViewHome   Home
	private ImageView mImageViewChange;// 3.4 mImageViewChange 切换多页模式
	private ImageView mImageViewOption;// 3.5 mImageViewOption 选项菜单 
	private ViewPager mViewPager;  //水平实现滑动效果
	private PagerAdapter mPageAdapter;  
	 private ViewPagerPresenter mPresenter;  
	private LinearLayout ll;//viewpager的线性布局
	int count=0;//点击次数
	// 记录当前选中位置
	 private int currentIndex;
	 private int oldPosition = 0;//记录上一次点的位置
	 private int currentItem; //当前页面
	 private ScheduledExecutorService scheduledExecutorService;
	 //private ArrayList<View> dots;
	 View settingPanel;//设置主界面
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		final ActionBar ab = this.getSherlockActivity().getSupportActionBar();

		// set defaults for logo & home up
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		setHasOptionsMenu(true);
	}
	
	@Override
	public  void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add(0,0,0,"Search");
        item.setIcon(android.R.drawable.ic_menu_search);      
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        View searchView = SearchViewCompat.newSearchView(getActivity());        
        if (searchView!=null) {
            SearchViewCompat.setOnQueryTextListener(searchView,
                    new OnQueryTextListenerCompat() {
            	
                @Override
                public boolean onQueryTextChange(String newText) {
                    // Called when the action bar search text has changed.  Since this
                    // is a simple array adapter, we can just have it do the filtering.
                    return true;
                }
            });            
            item.setActionView(searchView);   
        }
        /*  添加扫描二维码icon  对应ItemID 1 */
  
        menu.add(0,1,0,"Code")
        .setIcon(R.drawable.actionbar_title_caode)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        /*  添加注册登录icon  */

        menu.add(0, 2, 2,"Land")
        .setIcon(R.drawable.refresh_up)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);  

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		/*  二维码ID 1   主册登录ID 2  */
		switch(item.getItemId()){
			case 0:
				// 点击搜索。fragment跳转；
       /*     这里监听不到searchview点击事件   无法跳转fragment  但是fragment方法可是使用     */ 
				((BaseFragActivity)this.getActivity()).navigateTo
				(UrlRedirectFragment.class, null, true, UrlRedirectFragment.TAG);
			break;
			case 1:
				//二维码
				Intent intent=new Intent();
				intent.setClass(getActivity(), CaptureActivity.class);
				startActivity(intent);
				
			break;
			case 2:

				//主册登录

			mWebView.loadUrl("http://www.hmudq.edu.cn/");			
			break;	

				/*//测试跳转到bookmarkfragment
				((BaseFragActivity)this.getActivity()).navigateTo(BookMarkFragment.class,null,true,BookMarkFragment.TAG);

			break;		
*/

		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_page, container, false);
		mWebView=(WebView)view.findViewById(R.id.mWebView); //webview
		mImageViewBack=(ImageView)view.findViewById(R.id.mImageViewBack);  // 3.1 mImageViewBack   后退
		mImageViewInto=(ImageView)view.findViewById(R.id.mImageViewInto);  // 3.2 mImageViewInto   前进
		mImageViewHome=(ImageView)view.findViewById(R.id.mImageViewHome);  // 3.3 mImageViewHome   Home
		mImageViewChange=(ImageView)view.findViewById(R.id.mImageViewChange); // 3.4 mImageViewChange 切换多页模式
		mImageViewOption=(ImageView)view.findViewById(R.id.mImageViewOption); // 3.5 mImageViewOption 选项菜单
		mViewPager = (ViewPager) view.findViewById(R.id.test_viewpager);  
		settingPanel=view.findViewById(R.id.main_setting_panel);
		// 设置友好交互，即如果该网页中有链接，在本浏览器中重新定位并加载，而不是调用系统的浏览器
		mWebView.requestFocus();
		//mWebView.setDownloadListener(new myDownloaderListener());
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				cur_url = url;
				return super.shouldOverrideUrlLoading(view, url);
			}

		});
		/*   设置title各个控件监听       
		 1.1 search 
		mImageViewSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		   1.2 输入网址 获取网址信息   url为获取到的输入网址  为1.1 search获得数据
		String url=mEditTextInput.getText().toString();
		
		   1.3 点击事件 启动二维码扫描  
		mButtonCode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				//intent.setClassName(MainPageFragment.class, CaptureActivity.class);
				startActivity(intent);
			}
		});
		
		  1.4 设置监听事件  启动注册登陆 
		mButtonLand.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		*/
		/* 2.0 WebView touch监听
		 * 
		 *  这里与webview冲突
		 *  */
		mWebView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mViewPager.setVisibility(View.GONE);
				settingPanel.setVisibility(View.GONE);
				return true;
			}
		});
		
		/*  3.1 返回监听  */
		mImageViewBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mWebView.goBack();
			}
		});
		
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
				//mWebView.getBackground().setAlpha(100);
				count++;
				//mWebView.setAlpha(200);
				 init();
				
			
				
			}
		});
		
		/*  设置webview */
		setWebStyle();
		return view;
	}
	
	
	/* 点击webview取消菜单栏展示*/
	
	private void init() {
		if (count % 2 != 0) {
			mPresenter = new ViewPagerPresenter(this.getActivity());
			mPageAdapter = new MyPagerAdapter(mPresenter.getPageViews());
			mViewPager.setAdapter(mPageAdapter);
			mViewPager.setVisibility(View.VISIBLE);
			settingPanel.setVisibility(View.VISIBLE);
		} else {
			mViewPager.setVisibility(View.GONE);
			settingPanel.setVisibility(View.GONE);
		}
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}
			@Override
			public void onPageSelected(int arg0) {
				

			}

		});
	}

	 private void setWebStyle() {
			// TODO Auto-generated method stub
//			mWebView.getSettings().setJavaScriptEnabled(true);
//			mWebView.getSettings().setSupportZoom(true);
//			mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//			mWebView.requestFocus();
//			mWebView.setScrollBarStyle(View.GONE);
		 
//			webView.getSettings().setUseWideViewPort(true);
//			webView.getSettings().setLoadWithOverviewMode(true);
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.getSettings().setAppCacheMaxSize(8*1024*1024);
			mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
			//webView.getSettings().setPluginsEnabled(true);
			mWebView.getSettings().setPluginState(PluginState.ON);
			mWebView.loadUrl("http://www.baidu.com/");
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
