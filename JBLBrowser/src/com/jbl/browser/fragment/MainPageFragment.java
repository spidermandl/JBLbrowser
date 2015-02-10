package com.jbl.browser.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ScheduledExecutorService;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.AnimationUtils;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;
import cn.hugo.android.scanner.CaptureActivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.jbl.browser.BrowserSettings;
import com.jbl.browser.R;
import com.jbl.browser.activity.BaseFragActivity;
import com.jbl.browser.activity.MainFragActivity;
import com.jbl.browser.adapter.SettingPagerAdapter;
import com.jbl.browser.bean.BookMark;
import com.jbl.browser.bean.History;
import com.jbl.browser.db.BookMarkDao;
import com.jbl.browser.db.HistoryDao;
import com.jbl.browser.interfaces.SettingItemInterface;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.StringUtils;
import com.viewpager.indicator.LinePageIndicator;
import com.viewpager.indicator.PageIndicator;

/**
 * 浏览器主页
 * 
 * @author desmond.duan
 * 
 */
public class MainPageFragment extends SherlockFragment implements SettingItemInterface{

	public final static String TAG = "MainPageFragment";

	/**
	 * 1.title 1.1 mImageViewSearch 搜索图标 1.2 mEditTextInput 输入网址 1.3 mButtonCode
	 * 二维码搜索 1.4 mButtonLand 登陆注册 2.webview 2.1 mWebView 3.操作栏 3.1
	 * mImageViewBack 后退 3.2 mImageViewInto 前进 3.3 mImageViewHome Home 3.4
	 * mImageViewChange 切换多页模式 3.5 mImageViewOption 选项菜单
	 */
	/*
	 * 定义菜单控件 private ImageView mImageViewSearch; //1.1 mImageViewSearch 搜索图标
	 * private EditText mEditTextInput; //1.2 mEditTextInput 输入网址 private Button
	 * mButtonCode; //1.3 mButtonCode 二维码搜索 private Button mButtonLand; //1.4
	 * mButtonLand 登陆注册
	 */
	/* 定义webview控件 */
	public  WebView mWebView; // 主控件 webview
	public  WebSettings settings;
	public String cur_url =StringUtils.CUR_URL; // 设置初始网址
	public String webName = "";// 网页名
	/* 定义操作栏控件 */
	private ImageView mImageViewBack; // 3.1 mImageViewBack 后退
	private ImageView mImageViewInto; // 3.2 mImageViewInto 前进
	private ImageView mImageViewHome; // 3.3 mImageViewHome Home
	private ImageView mImageViewChange;// 3.4 mImageViewChange 切换多页模式
	private ImageView mImageViewOption;// 3.5 mImageViewOption 选项菜单
	private ViewPager mViewPager; // 水平实现滑动效果
	private PagerAdapter mPageAdapter;
	private ScheduledExecutorService scheduledExecutorService;
	View settingPanel;// 设置主界面
	PageIndicator mIndicator;
	int count;
	private boolean visibile=true;//标示是否显示菜单栏

	
	View popview;//翻页按钮布局
	PopupWindow popWindow;//悬浮翻页窗口
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem item = menu.add(0, 0, 0, "Search").setIcon(
				android.R.drawable.ic_menu_search);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		View searchView = SearchViewCompat.newSearchView(getActivity());
		if (searchView != null) {
			SearchViewCompat.setOnQueryTextListener(searchView,
					new OnQueryTextListenerCompat() {
						@Override
						public boolean onQueryTextSubmit(String query) {
							// TODO Auto-generated method stub

							return false;
						}
					});
		} 
		/* 添加扫描二维码icon 对应ItemID 1 */

		menu.add(0, 1, 0, "Code").setIcon(R.drawable.actionbar_title_caode)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		/* 添加注册登录icon */

		menu.add(0, 2, 2, "Land").setIcon(R.drawable.refresh_up)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		/* 二维码ID 1 主册登录ID 2 */

		switch (item.getItemId()) {
		case 0:
			// 点击搜索。fragment跳转；
			/* 这里监听不到searchview点击事件 无法跳转fragment 但是fragment方法可是使用 */
			((BaseFragActivity) this.getActivity()).navigateTo(
					UrlRedirectFragment.class, null, true,
					UrlRedirectFragment.TAG);
			break;
		case 1:
			// 二维码
			Intent intent = new Intent();
			intent.setClass(getActivity(), CaptureActivity.class);
			startActivity(intent);

			break;
		case 2:
			// 主册登录
			mWebView.loadUrl("http://www.hmudq.edu.cn/");
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	//覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {  
        	mWebView.goBack(); //goBack()表示返回WebView的上一页面  
            return true;  
        }  
        return false; 
    }


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_page, container,
				false);
		mWebView = (WebView) view.findViewById(R.id.mWebView);// webview
		//Intent intent = getActivity().getIntent();  //监听webview跳转，实现activity跳转到推荐页面
		mImageViewBack = (ImageView) view.findViewById(R.id.mImageViewBack); // 3.1
																				// mImageViewBack
																				// 后退
		mImageViewInto = (ImageView) view.findViewById(R.id.mImageViewInto); // 3.2
																				// mImageViewInto
																				// 前进
		mImageViewHome = (ImageView) view.findViewById(R.id.mImageViewHome); // 3.3
																				// mImageViewHome
																				// Home
		mImageViewChange = (ImageView) view.findViewById(R.id.mImageViewChange); // 3.4
																					// mImageViewChange
																					// 切换多页模式
		mImageViewOption = (ImageView) view.findViewById(R.id.mImageViewOption); // 3.5
																					// mImageViewOption
																					// 选项菜单
		mViewPager = (ViewPager) view.findViewById(R.id.setting_viewpager);
		mIndicator = (LinePageIndicator)view.findViewById(R.id.setting_indicator);

		settingPanel = view.findViewById(R.id.main_setting_panel);
		// 设置友好交互，即如果该网页中有链接，在本浏览器中重新定位并加载，而不是调用系统的浏览器
		mWebView.requestFocus();
		// mWebView.setDownloadListener(new myDownloaderListener());
		
		/*
		 * 设置webview字体大小
		 */
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		BrowserSettings.getInstance().addObserver(mWebView.getSettings());
		int fontSize=JBLPreference.getInstance(this.getActivity()).readInt(JBLPreference.FONT_TYPE);
		switch (fontSize) {
		case JBLPreference.FONT_MIN:
			BrowserSettings.textSize = WebSettings.TextSize.SMALLER;
			break;
		case JBLPreference.INVALID:
        case JBLPreference.FONT_MEDIUM:
			BrowserSettings.textSize = WebSettings.TextSize.NORMAL;
			break;
        case JBLPreference.FONT_MAX:
	        BrowserSettings.textSize = WebSettings.TextSize.LARGER;
	        break;
		default:
			break;
		}

		BrowserSettings.getInstance().update();
		/*
		 * 设置title各个控件监听 1.1 search mImageViewSearch.setOnClickListener(new
		 * View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * } });
		 * 
		 * 1.2 输入网址 获取网址信息 url为获取到的输入网址 为1.1 search获得数据 String
		 * url=mEditTextInput.getText().toString();
		 * 
		 * 1.3 点击事件 启动二维码扫描 mButtonCode.setOnClickListener(new
		 * View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Intent intent=new Intent();
		 * //intent.setClassName(MainPageFragment.class, CaptureActivity.class);
		 * startActivity(intent); } });
		 * 
		 * 1.4 设置监听事件 启动注册登陆 mButtonLand.setOnClickListener(new
		 * View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * } });
		 */

		/*
		 * 2.0 WebView touch监听
		 * 
		 * 这里与webview冲突
		 */
		mWebView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				/*
				 * mViewPager.setVisibility(View.GONE);
				 * settingPanel.setVisibility(View.GONE);
				 */
				return false;
			}
		});

		/* 3.1 返回监听 */
		mImageViewBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mWebView.goBack();
			}
		});

		/* 3.2 前进监听 */
		mImageViewInto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});

		/* 3.3 返回home主界面 */
		mImageViewHome.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				

			}
		});

		/* 3.4 切换多页模式 */
		mImageViewChange.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});

		/* 3.5 选项菜单 */
		mImageViewOption.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				count++;
				init(visibile);
			}
		});

		/* 设置webview */
		setWebStyle();
		return view;
	}

	/* 点击webview取消菜单栏展示 */

	private void init(boolean visibile) {
		if (visibile) {
			SettingPagerFragment settingPager = new SettingPagerFragment(this.getActivity());
			settingPager.setInterface(this);
			mPageAdapter = new SettingPagerAdapter(settingPager.getPageViews());
			mViewPager.setAdapter(mPageAdapter);
			mIndicator.setViewPager(mViewPager);
			mViewPager.setVisibility(View.VISIBLE);
			settingPanel.setVisibility(View.VISIBLE);
			mViewPager.startAnimation(// 加载弹出菜单栏的动画效果
					AnimationUtils.loadAnimation(getActivity(),R.anim.menu_bar_appear));
			this.visibile=false;
		} else {
			mViewPager.setVisibility(View.GONE);
			settingPanel.setVisibility(View.GONE);
			mViewPager.startAnimation(// 退出菜单栏时的动画效果
					AnimationUtils.loadAnimation(getActivity(),R.anim.menu_bar_disappear));
			this.visibile=true;
		}

	}
	// 添加书签
	public void addNewBookMark() {
		boolean flag=false;
		BookMark bookMark =new BookMark();
		bookMark.setWebName(webName);
		bookMark.setWebAddress(cur_url);
		flag=new BookMarkDao(getActivity()).addBookMark(bookMark);
		if (flag)
			Toast.makeText(getActivity(), R.string.add_bookmark_succeed, 80).show();
		else
			Toast.makeText(getActivity(), R.string.add_bookmark_fail, 80).show();
	}
	

	private void setWebStyle() {
		// TODO Auto-generated method stub
		// mWebView.getSettings().setJavaScriptEnabled(true);
		// mWebView.getSettings().setSupportZoom(true);
		// mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		// mWebView.requestFocus();
		// mWebView.setScrollBarStyle(View.GONE);

		// webView.getSettings().setUseWideViewPort(true);
		// webView.getSettings().setLoadWithOverviewMode(true);
		
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setAppCacheMaxSize(8 * 1024 * 1024);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		// webView.getSettings().setPluginsEnabled(true);
		mWebView.getSettings().setPluginState(PluginState.ON);
		//取得书签和历史记录界面传过来的值
		if (JBLPreference.getInstance(getActivity()).readString(JBLPreference.BOOKMARK_HISTORY_KEY)!="") {
			cur_url =JBLPreference.getInstance(getActivity()).readString(JBLPreference.BOOKMARK_HISTORY_KEY);
		}
		//取得推荐页面的网址
		if (JBLPreference.getInstance(getActivity()).readString(JBLPreference.RECOMMEND_KEY)!="") {
			cur_url =JBLPreference.getInstance(getActivity()).readString(JBLPreference.RECOMMEND_KEY);
		}
		mWebView.loadUrl(cur_url);
		/* 这里是推荐监听页面     暂时先注释  因为我监听的事主页百度*/
		/*if(cur_url.equals("http://www.baidu.com/?tn=95406117_hao_pg")){
			Intent in=new Intent();
			in.setClass(getActivity(),RecommendMainActivity.class);
			startActivity(in);
		}*/
		mWebView.setDownloadListener(new DownloadListener() {
			
			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype, long contentLength) {
				// TODO Auto-generated method stub
				((MainFragActivity)getActivity()).startDownload(url);
			}
		});
		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				super.onReceivedTitle(view, title);
				webName = title;
			}
		});
	}
	/* webcilent */
	class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			cur_url=url;
			view.loadUrl(cur_url);
			return true;
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			String date = new SimpleDateFormat("yyyyMMdd", Locale.CHINA)
					.format(new Date()).toString();
			String temp=StringUtils.CUR_URL.substring(0, StringUtils.CUR_URL.length());
			if(!url.equals(temp)){      //当加载默认网址时不加入历史记录
				History history = new History();
				history.setWebAddress(url);
				history.setWebName(webName);
				// 加载完加入历史记录
				new HistoryDao(getActivity()).addHistory(history);
			}
			super.onPageFinished(view, url);
		}
	}
	
	
	@Override
	public void addBookMark() {
		MainPageFragment.this.addNewBookMark();
		mViewPager.setVisibility(View.GONE);
		settingPanel.setVisibility(View.GONE);
	}

	@Override
	public void listBookMark() {
		((BaseFragActivity) getActivity()).navigateTo(BookMarkFragment.class, null, true,BookMarkFragment.TAG);
	}

	@Override
	public void browserSetting() {
		((BaseFragActivity) getActivity()).navigateTo(MenuSetFragment.class, null, true,MenuSetFragment.TAG);
	}

	@Override
	public void listHistory() {
		((BaseFragActivity) getActivity()).navigateTo(HistoryFragment.class, null, true,HistoryFragment.TAG);
	}

	@Override
	public void share() {
    	Intent shareIntent = new Intent(Intent.ACTION_SEND);
    	shareIntent.setType("text/plain");
    	shareIntent.putExtra(Intent.EXTRA_TEXT, mWebView.getUrl());
    	shareIntent.putExtra(Intent.EXTRA_SUBJECT,mWebView.getTitle());
    	
    	try {
    		getActivity().startActivity(Intent.createChooser(shareIntent, getActivity().getString(R.string.main_share_chooser_title)));
        } catch(android.content.ActivityNotFoundException ex) {
           
        }
    	
		mViewPager.setVisibility(View.GONE);
		settingPanel.setVisibility(View.GONE);
		
	}

	@Override
	public void fitlerPicLoading() {
		switch (JBLPreference.getInstance(getActivity()).readInt(JBLPreference.PIC_CACHE_TYPE)) {
		case JBLPreference.NO_PICTURE:
			JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.PIC_CACHE_TYPE, JBLPreference.YES_PICTURE);
			Toast.makeText(getActivity(), StringUtils.OPEN_NO_PICTURE, 100).show();
			mWebView.getSettings().setBlockNetworkImage(true);
			break;
		case JBLPreference.INVALID:
		case JBLPreference.YES_PICTURE:
			JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.PIC_CACHE_TYPE,JBLPreference.NO_PICTURE);
			Toast.makeText(getActivity(), StringUtils.OPEN_YES_PICTURE, 100).show();
			mWebView.getSettings().setBlockNetworkImage(false);
		default:
			break;
		}
		mViewPager.setVisibility(View.GONE);
		settingPanel.setVisibility(View.GONE);
		
	}

	@Override
	public void manageDownload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void quit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pageTurningSwitch() {
		// TODO Auto-generated method stub
		
	}
}
