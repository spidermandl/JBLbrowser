package com.jbl.browser.fragment;

import java.util.concurrent.ScheduledExecutorService;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.BrowserSettings;
import com.jbl.browser.R;
import com.jbl.browser.activity.BaseFragActivity;
import com.jbl.browser.activity.MainFragActivity;
import com.jbl.browser.adapter.MultipageAdapter;
import com.jbl.browser.adapter.SettingPagerAdapter;
import com.jbl.browser.bean.BookMark;
import com.jbl.browser.db.BookMarkDao;
import com.jbl.browser.interfaces.SettingItemInterface;
import com.jbl.browser.interfaces.ToolbarItemInterface;
import com.jbl.browser.interfaces.TopActionbarInterface;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.StringUtils;
import com.jbl.browser.utils.UrlUtils;
import com.jbl.browser.view.ProgressWebView;
import com.viewpager.indicator.LinePageIndicator;
import com.viewpager.indicator.PageIndicator;

/**
 * 浏览器主页
 * 
 * @author desmond.duan
 * 
 */
public class MainPageFragment extends SherlockFragment implements SettingItemInterface,ToolbarItemInterface,TopActionbarInterface{

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
	/*  1.0 ImageView_search  1.1 AutoCompleteTextView 1.2ImageView_Code  
	 *  1.3 ImageView_Land
	 *    */
	
//	private ImageView mImageView_Search,mImageView_Code,mImageView_Land;
//	private AutoCompleteTextView mNetAddress;
	/* 定义webview控件 */
	public  ProgressWebView mWebView; // 主控件 webview
	public  WebSettings settings;
	public BottomMenuFragment toolbarFragment;//底部toolbar
	
	public TopMenuFragment topActionbarFragment; //顶部actionbar
	
	public String cur_url; // 设置初始网址
	public String webName=""; // 网页名
	 
	
	private ViewPager mViewPager; // 水平实现滑动效果
	private PagerAdapter mPageAdapter;
	private MultipageAdapter multipageAdapter;//多页效果适配器 
	private ScheduledExecutorService scheduledExecutorService;
	View settingPanel;// 设置主界面
	PageIndicator mIndicator;
	int count;
	private boolean visibile=true;//标示是否显示菜单栏
	View popview;//翻页按钮布局
	PopupWindow popWindow;//悬浮翻页窗口
	View multipagePanel;//多页布局
	PageIndicator multipageIndicator;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		//开启全屏模式
		if(JBLPreference.getInstance(getActivity()).readInt(JBLPreference.PIC_CACHE_TYPE)==1||
				JBLPreference.getInstance(getActivity()).readInt(JBLPreference.PIC_CACHE_TYPE)==-1){
//			getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//	        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	    //关闭全屏模式
		}else{
//			getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//	        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		}
		super.onResume();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		/*final ActionBar ab = this.getSherlockActivity().getSupportActionBar();
		// set defaults for logo & home up
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		/*MenuItem item = menu.add(0, 0, 0, "Search").setIcon(
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
		} */

		/*menu.add(0, 0, 0, "Back").setIcon(R.drawable.resume_ad_close)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		menu.add(0, 1, 1, "GoTo").setIcon(R.drawable.resume_ad_close)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		menu.add(0, 2, 2, "Home").setIcon(R.drawable.resume_ad_close)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		menu.add(0, 3, 3, "Change").setIcon(R.drawable.resume_ad_close)
		        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		menu.add(0, 4, 4, "Option").setIcon(R.drawable.resume_ad_close)

		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
	}
/*	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 0-Back-返回监听    1-GoTo-前进监听  2-Home-主页监听  3-Change-多页监听  4-Option-设置监听 

		switch (item.getItemId()) {
		case 0:
			  0-Back-返回监听   
			if(mWebView.canGoBack()){
				mWebView.goBack();
			}
			else{
				Toast.makeText(getActivity(), "不能后退了！", Toast.LENGTH_SHORT).show();
			}
			
			
			((BaseFragActivity) this.getActivity()).navigateTo(
					UrlRedirectFragment.class, null, true,
					UrlRedirectFragment.TAG);
			break;
		case 1:
			  1-GoTo-前进监听  
			if(mWebView.canGoForward()){
				mWebView.goForward();
			}	
			else{
				Toast.makeText(getActivity(), "不能前进了！", Toast.LENGTH_SHORT).show();
			}
			break;
		case 2:
			mWebView.clearHistory(); //清楚浏览记录
			mWebView.loadUrl("http://m.hi2345.net/home.php"); //加载主页
			break;
		case 3:
			  3-Change-多页监听    
			((BaseFragActivity)getActivity()).navigateTo(MultipageFragment.class, null, true,MultipageFragment.TAG);
			Toast.makeText(getActivity(), "已进入多页模式", 1).show();
			break;
		case 4:
			 // 4-Option-设置监听    
			count++;
			init(visibile);
			//SettingPagerFragment fire = SettingPagerFragment.newInstance(null);  
			//fire.show(getFragmentManager(), "dialog");  
			break;
	}*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//cur_url=UrlUtils.URL_GET_HOST;
		View view = inflater.inflate(R.layout.fragment_main_page, container,
				false);
		mWebView = (ProgressWebView) view.findViewById(R.id.mWebView);// webview
//		//Intent intent = getActivity().getIntent();  //监听webview跳转，实现activity跳转到推荐页面

		
		/* 标题栏 
		 * 1.0 ImageView_search  1.1 AutoCompleteTextView 1.2ImageView_Code  
		 *  1.3 ImageView_Land
		 *    */		
//		mImageView_Search=(ImageView)view.findViewById(R.id.mImageView_Search);//搜索图标
//		mNetAddress=(AutoCompleteTextView)view.findViewById(R.id.net_address);//输入栏
//		mImageView_Code=(ImageView)view.findViewById(R.id.mImageView_Code);//二维码
//		mImageView_Land=(ImageView)view.findViewById(R.id.mImageView_Land);//登陆注册

		mViewPager = (ViewPager) view.findViewById(R.id.setting_viewpager);
		mIndicator = (LinePageIndicator)view.findViewById(R.id.setting_indicator);
		settingPanel = view.findViewById(R.id.main_setting_panel);
		toolbarFragment=(BottomMenuFragment)(this.getActivity().getSupportFragmentManager().findFragmentById(R.id.bottom_toolbar_fragment));
		toolbarFragment.setToolbarAction(this);//设置回调接口
		
		topActionbarFragment=(TopMenuFragment)(this.getActivity().getSupportFragmentManager().findFragmentById(R.id.top_menu_fragment));
		topActionbarFragment.setTopActionbar(this);//设置回调接口
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
		
//		// 3.1 返回监听 
//		mImageViewBack.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(mWebView.canGoBack()){
//					mWebView.goBack();
//				}
//				else{
//					Toast.makeText(getActivity(), "不能后退了！", Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
//
//		// 3.2 前进监听 
//		mImageViewInto.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if(mWebView.canGoForward()){
//					mWebView.goForward();
//				}
//				else{
//					Toast.makeText(getActivity(), "不能前进了！", Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
//
//		// 3.3 返回home主界面 
//		mImageViewHome.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//			mWebView.clearHistory(); //清楚浏览记录
//			mWebView.loadUrl(UrlUtils.URL_GET_HOST); //加载主页
//			}
//		});
//
//		 //3.4 切换多页模式 
//		mImageViewChange.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				((BaseFragActivity)getActivity()).navigateTo(MultipageFragment.class, null, true,MultipageFragment.TAG);
//				Toast.makeText(getActivity(), "已进入多页模式", 1).show();
//			}
//		});
//
//		// 3.5 选项菜单 
//		mImageViewOption.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				init(visibile);
//			}
//		});

		/* 设置webview */
		initWebView();
		return view;
	}

	/* 点击webview取消菜单栏展示 */

	private void init(boolean visibile) {
		if (visibile) {
			SettingPagerFragment settingPager = new SettingPagerFragment(this.getActivity());
			settingPager.setInterface(this);//设置回调接口
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
	private void addNewBookMark() {
		boolean flag=false;
		BookMark bookMark =new BookMark();
		bookMark.setWebName(mWebView.getWebName());
		bookMark.setWebAddress(mWebView.getCurrentUrl());
		flag=new BookMarkDao(getActivity()).addBookMark(bookMark);
		if (flag)
			Toast.makeText(getActivity(), R.string.add_bookmark_succeed, 80).show();
		else
			Toast.makeText(getActivity(), R.string.add_bookmark_fail, 80).show();
	}
	private void initWebView() {
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
		String urlAddress="";   //声明接收从书签和历史记录界面传来的值
		urlAddress=JBLPreference.getInstance(getActivity()).readString(JBLPreference.BOOKMARK_HISTORY_KEY);
		if(urlAddress==""){
			mWebView.loadUrl(UrlUtils.URL_GET_HOST);
		}else{
			mWebView.loadUrl(urlAddress);
		}
		//监听物理返回键
		mWebView.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				 if (event.getAction() == KeyEvent.ACTION_DOWN) { 
					 if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
						 mWebView.goBack(); //goBack()表示返回WebView的上一页面  
				         return true;  
					 }
				 }
				return false;
			}
		});
		mWebView.setDownloadListener(new DownloadListener() {
			
			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype, long contentLength) {
				// TODO Auto-generated method stub
				((MainFragActivity)getActivity()).startDownload(url);
			}
		});
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
		operate(JBLPreference.getInstance(getActivity()).readInt(JBLPreference.PIC_CACHE_TYPE),JBLPreference.PIC_CACHE_TYPE,
				JBLPreference.NO_PICTURE,JBLPreference.YES_PICTURE,StringUtils.OPEN_NO_PICTURE, StringUtils.CLOSE_NO_PICTURE);
	}
	@Override
	public void manageDownload() {

		// TODO Auto-generated method stub
		((MainFragActivity)getActivity()).showDownloadList();

	}
	@Override
	public void quit() {  //退出跳出对话框确定
		Dialog dialog=new AlertDialog.Builder(getActivity())
		.setTitle(R.string.quit)
		.setMessage(R.string.confirm_quit)
		.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub				
				getActivity().finish();
			}
		}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}			
		})
		.create();
		dialog.show();
	}
	
	@Override
	public void pageTurningSwitch() {
		switch (JBLPreference.getInstance(getActivity()).readInt(JBLPreference.TURNING_TYPE)) {
		case JBLPreference.OPEN_TURNING_BUTTON:
			JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.TURNING_TYPE, JBLPreference.COLSE_TURNING_BUTTON);
			Toast.makeText(getActivity(), StringUtils.OPEN_TURNING_BUTTON, 100).show();
			LayoutInflater mLayoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			popview=(View)mLayoutInflater.inflate(R.layout.pop_window_nextpager, null);
			popWindow=new PopupWindow(popview,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			popWindow.showAtLocation(popview, Gravity.RIGHT, 0, 0);
			Button previous_page=(Button)popview.findViewById(R.id.previous_page);
			Button next_page=(Button)popview.findViewById(R.id.next_page);
			next_page.setOnClickListener(new OnClickListener(){
				@SuppressLint("NewApi")
				@Override
				public void onClick(View v) {
					mWebView.scrollTo(0,(int) (mWebView.getHeight()+mWebView.getScaleY()));
				}
			});
			previous_page.setOnClickListener(new OnClickListener(){
				@SuppressLint("NewApi")
				@Override
				public void onClick(View v) {
					mWebView.scrollTo(0, (int) (mWebView.getScaleY()-mWebView.getHeight()));
				}
			});
			break;
		case JBLPreference.INVALID:
		case JBLPreference.COLSE_TURNING_BUTTON:
			JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.TURNING_TYPE,JBLPreference.OPEN_TURNING_BUTTON);
			Toast.makeText(getActivity(), StringUtils.COLSE_TURNING_BUTTON, 100).show();
			popWindow.dismiss();
		default:
			break;
		}
		mViewPager.setVisibility(View.GONE);
		settingPanel.setVisibility(View.GONE);	
	}

	@Override
	public void refresh() {     //刷新当前界面
		// TODO Auto-generated method stub
		mWebView.reload();
		mViewPager.setVisibility(View.GONE);
		settingPanel.setVisibility(View.GONE);
	}

	@Override
	public void withoutTrace() {   //无痕浏览
		// TODO Auto-generated method stub
		operate(JBLPreference.getInstance(getActivity()).readInt(JBLPreference.HISTORY_CACHE_TYPE),JBLPreference.HISTORY_CACHE_TYPE,
				JBLPreference.NO_HISTORY,JBLPreference.YES_HISTORY,StringUtils.OPEN_NO_HISTORY, StringUtils.CLOSE_NO_HISTORY);
	}

	@Override
	public void fullScreen() {     //全屏浏览
		// TODO Auto-generated method stub
		operate(JBLPreference.getInstance(getActivity()).readInt(JBLPreference.FULL_SCREEN_TYPE),JBLPreference.FULL_SCREEN_TYPE,
				JBLPreference.NO_FULL,JBLPreference.YES_FULL,StringUtils.OPEN_NO_FULL, StringUtils.CLOSE_NO_FULL);
	}
	
	public void operate(int type,String strType,int no,int yes,String open,String close){
		switch (type) {
		case -1:
		case 1:
			JBLPreference.getInstance(getActivity()).writeInt(strType,yes);
			Toast.makeText(getActivity(), open, 100).show();
			if(strType==JBLPreference.PIC_CACHE_TYPE){        //当要开启无图模式时
				mWebView.getSettings().setBlockNetworkImage(true);
			}
			break;
		case 0:
			JBLPreference.getInstance(getActivity()).writeInt(strType,no);
			Toast.makeText(getActivity(), close, 100).show();
			if(strType==JBLPreference.PIC_CACHE_TYPE){         //当要关闭无图模式时
				mWebView.getSettings().setBlockNetworkImage(false);
			}
		default:
			break;
		}
		mViewPager.setVisibility(View.GONE);
		settingPanel.setVisibility(View.GONE);
	}
	
	@Override
	public void goBack() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		} else {
			Toast.makeText(getActivity(), "不能后退了！", Toast.LENGTH_SHORT).show();
		}

		//((BaseFragActivity) this.getActivity()).navigateTo(UrlRedirectFragment.class, null, true, UrlRedirectFragment.TAG);
	}

	@Override
	public void goForward() {
		if (mWebView.canGoForward()) {
			mWebView.goForward();
		} else {
			Toast.makeText(getActivity(), "不能前进了！", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void goHome() {
		mWebView.clearHistory(); //清楚浏览记录
		mWebView.loadUrl(UrlUtils.URL_GET_HOST); //加载主页		
	}
	@Override
	public void goMenu() {
 		init(visibile);
		
	}
	@Override
	public void goMultiWindow() {
		((BaseFragActivity)getActivity()).navigateTo(MultipageFragment.class, null, true,MultipageFragment.TAG);
		Toast.makeText(getActivity(), "已进入多页模式", 1).show();
		
	}
	
	//点击搜索图标
	@Override
	public void goSearch() {
		// TODO Auto-generated method stub
		
	}
	//输入框点击监听
	@Override
	public void goEditInput() {
		// TODO Auto-generated method stub
		
	}
	//二维码跳转
	@Override
	public void goCode() {
		// TODO Auto-generated method stub
		
	}
	//登录注册监听
	@Override
	public void goLand() {
		// TODO Auto-generated method stub
		
	}

}
