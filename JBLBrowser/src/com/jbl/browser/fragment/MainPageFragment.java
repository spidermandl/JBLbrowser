package com.jbl.browser.fragment;

import java.util.concurrent.ScheduledExecutorService;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;
import cn.hugo.android.scanner.CaptureActivity;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.BrowserSettings;
import com.jbl.browser.R;
import com.jbl.browser.activity.BaseFragActivity;
import com.jbl.browser.activity.BrowserSettingActivity;
import com.jbl.browser.activity.HistoryFavourateActivity;
import com.jbl.browser.activity.MainFragActivity;
import com.jbl.browser.adapter.MultipageAdapter;
import com.jbl.browser.bean.BookMark;
import com.jbl.browser.db.BookMarkDao;
import com.jbl.browser.interfaces.LoadURLInterface;
import com.jbl.browser.interfaces.SettingItemInterface;
import com.jbl.browser.interfaces.ToolbarItemInterface;
import com.jbl.browser.interfaces.TopActionbarInterface;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.StringUtils;
import com.jbl.browser.utils.UrlUtils;
import com.jbl.browser.view.ProgressWebView;
import com.viewpager.indicator.PageIndicator;

/**
 * 浏览器主页
 * 
 * @author desmond.duan
 * 
 */
public class MainPageFragment extends SherlockFragment implements 
                                              SettingItemInterface,
                                              ToolbarItemInterface,
                                              TopActionbarInterface,
                                              LoadURLInterface{

	public final static String TAG = "MainPageFragment";
	/* 定义webview控件 */
	public ProgressWebView mWebView; // 主控件 webview
	public WebSettings settings;
	public BottomMenuFragment toolbarFragment;//底部toolbar
	public SettingPagerFragment settingFragment;//底部弹出菜单 fragment
	public TopMenuFragment topActionbarFragment; //顶部actionbar
	
	public String cur_url; // 设置初始网址
	public String webName=""; // 网页名
	
	private MultipageAdapter multipageAdapter;//多页效果适配器 
	private ScheduledExecutorService scheduledExecutorService;

	View popview;//翻页按钮布局
	PopupWindow popWindow;//悬浮窗口
	View multipagePanel;//多页布局
	PageIndicator multipageIndicator;
	
	
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//cur_url=UrlUtils.URL_GET_HOST;
		View view = inflater.inflate(R.layout.fragment_main_page, container,false);
		mWebView = (ProgressWebView) view.findViewById(R.id.mWebView);// webview
//		//Intent intent = getActivity().getIntent();  //监听webview跳转，实现activity跳转到推荐页面
		mWebView.setInterface(this);//设置回调接口
		
		toolbarFragment=(BottomMenuFragment)(this.getActivity().getSupportFragmentManager().findFragmentById(R.id.bottom_toolbar_fragment));
		toolbarFragment.setInterface(this);//设置回调接口
		
		
		settingFragment=new SettingPagerFragment();
		settingFragment.setInterface(this);//设置回调接口
		
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
				if(JBLPreference.getInstance(getActivity()).readInt(JBLPreference.FULL_SCREEN_TYPE)==0  //当全屏模式：触摸屏幕不显示上下菜单栏
						&&toolbarFragment.isVisible()&&topActionbarFragment.isVisible()
						&&!mWebView.getUrl().equals(UrlUtils.URL_GET_HOST)){
					createPopShrinkFullScreen();
					popWindow.showAtLocation(popview, Gravity.RIGHT|Gravity.BOTTOM, 0, 60);
					getFragmentManager().beginTransaction().hide(toolbarFragment).commit();
		        	getFragmentManager().beginTransaction().hide(topActionbarFragment).commit();
				}
				return false;
			}
		});
		
		/* 设置webview */
		initWebView();
		return view;
	}
	
	@Override
	public void onDestroyView() {      //销毁内嵌的fragment
        getFragmentManager().beginTransaction().remove(toolbarFragment).commit();  
        getFragmentManager().beginTransaction().remove(settingFragment).commit();
        getFragmentManager().beginTransaction().remove(topActionbarFragment).commit();
		super.onDestroyView();
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
		String urlAddress=JBLPreference.getInstance(getActivity()).readString(JBLPreference.BOOKMARK_HISTORY_KEY);
		if(urlAddress==null||urlAddress.length()==0){
			mWebView.loadUrl(UrlUtils.URL_GET_HOST);
		}else{
			JBLPreference.getInstance(getActivity()).writeString(JBLPreference.BOOKMARK_HISTORY_KEY,null);
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
//		mViewPager.setVisibility(View.GONE);
//		settingPanel.setVisibility(View.GONE);
	}
	@Override
	public void listBookMark() {
		
		Intent intent=new Intent();
		intent.setClass(this.getActivity(), HistoryFavourateActivity.class);
		intent.putExtra("TAG", BookMarkFragment.TAG);
		this.startActivity(intent);
		//((BaseFragActivity) getActivity()).navigateTo(BookMarkFragment.class, null, true,BookMarkFragment.TAG);
	}
	@Override
	public void browserSetting() {
		//((BaseFragActivity) getActivity()).navigateTo(MenuSettingFragment.class, null, true,MenuSettingFragment.TAG);
		Intent intent=new Intent();
		intent.setClass(this.getActivity(), BrowserSettingActivity.class);
		this.startActivity(intent);
	}
	@Override
	public void listHistory() {
		Intent intent=new Intent();
		intent.setClass(this.getActivity(), HistoryFavourateActivity.class);
		intent.putExtra("TAG", HistoryFragment.TAG);
		this.startActivity(intent);
		//((BaseFragActivity) getActivity()).navigateTo(HistoryFragment.class, null, true,HistoryFragment.TAG);
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
		
	}
	@Override
	public void fitlerPicLoading() {
		operate(JBLPreference.getInstance(getActivity()).readInt(
				JBLPreference.PIC_CACHE_TYPE), JBLPreference.PIC_CACHE_TYPE,
				JBLPreference.NO_PICTURE, JBLPreference.YES_PICTURE,
				StringUtils.OPEN_NO_PICTURE, StringUtils.CLOSE_NO_PICTURE);
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
	}

	@Override
	public void refresh() {     //刷新当前界面
		// TODO Auto-generated method stub
		mWebView.reload();
	}

	@Override
	public void withoutTrace() { // 无痕浏览
		// TODO Auto-generated method stub
		operate(JBLPreference.getInstance(getActivity()).readInt(
				JBLPreference.HISTORY_CACHE_TYPE),
				JBLPreference.HISTORY_CACHE_TYPE, JBLPreference.NO_HISTORY,
				JBLPreference.YES_HISTORY, StringUtils.OPEN_NO_HISTORY,
				StringUtils.CLOSE_NO_HISTORY);
	}

	@Override
	public void fullScreen() {     //全屏浏览
		// TODO Auto-generated method stub
		operate(JBLPreference.getInstance(getActivity()).readInt(
				JBLPreference.FULL_SCREEN_TYPE),
				JBLPreference.FULL_SCREEN_TYPE, JBLPreference.NO_FULL,
				JBLPreference.YES_FULL, StringUtils.OPEN_NO_FULL,
				StringUtils.CLOSE_NO_FULL);
	}
	
	@SuppressWarnings("deprecation")
	public void operate(int type,String strType,int no,int yes,String open,String close){
		switch (type) {
		case -1:
		case 1:
			JBLPreference.getInstance(getActivity()).writeInt(strType,yes);
			Toast.makeText(getActivity(), open, 100).show();
			if(strType==JBLPreference.PIC_CACHE_TYPE){        //当要开启无图模式时
				mWebView.getSettings().setBlockNetworkImage(true);
			}
			if(strType==JBLPreference.FULL_SCREEN_TYPE){     //当要开启全屏浏览模式时，隐藏顶部状态栏、底部菜单栏和顶部搜索栏
				WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
	            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
	            getActivity().getWindow().setAttributes(lp);
	            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);	            
	            createPopShrinkFullScreen();
	            if(!mWebView.getUrl().equals(UrlUtils.URL_GET_HOST)){
	            	getFragmentManager().beginTransaction().hide(toolbarFragment).commit();
		            getFragmentManager().beginTransaction().hide(topActionbarFragment).commit();
	            	popWindow.showAtLocation(popview, Gravity.RIGHT|Gravity.BOTTOM, 0, 60);
	            }            
			}
			break;
		case 0:
			JBLPreference.getInstance(getActivity()).writeInt(strType,no);
			Toast.makeText(getActivity(), close, 100).show();
			if(strType==JBLPreference.PIC_CACHE_TYPE){         //当要关闭无图模式时
				mWebView.getSettings().setBlockNetworkImage(false);
			}
			if(strType==JBLPreference.FULL_SCREEN_TYPE){      //当要关闭全屏浏览模式时，显示顶部状态栏、底部菜单栏和顶部搜索栏
				popWindow.dismiss();
				WindowManager.LayoutParams attr = getActivity().getWindow().getAttributes();
	            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
	            getActivity().getWindow().setAttributes(attr);
	            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	            getFragmentManager().beginTransaction().show(toolbarFragment).commit();
            	getFragmentManager().beginTransaction().show(topActionbarFragment).commit();
			}
		default:
			break;
		}
	}
	//显示全屏模式下为显示上下菜单的悬浮按钮
	private void createPopShrinkFullScreen(){
        LayoutInflater mLayoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popview=(View)mLayoutInflater.inflate(R.layout.shrink_full_screen, null);
		popWindow=new PopupWindow(popview,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		ImageView shrinkFullScreen=(ImageView)popview.findViewById(R.id.shrinkFullScreen);
        shrinkFullScreen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getFragmentManager().beginTransaction().show(toolbarFragment).commit();
	            getFragmentManager().beginTransaction().show(topActionbarFragment).commit();
	            popWindow.dismiss();
				}
			});            
    }
	
	@Override
	public void goBack() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		} else {
			Toast.makeText(getActivity(), "不能后退了！", Toast.LENGTH_SHORT).show();
		}

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
		try {
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if (settingFragment.isRemoving() || getFragmentManager().findFragmentByTag(SettingPagerFragment.TAG) == null) {
				transaction.replace(R.id.main_setting_panel, settingFragment,SettingPagerFragment.TAG);
				transaction.addToBackStack(null);
			} else {
				transaction.remove(settingFragment);
			}
			transaction.commitAllowingStateLoss();
			this.getActivity().getSupportFragmentManager().executePendingTransactions();
		} catch (Exception e) {
		}
	}
	@Override
	public void goMultiWindow() {
		((BaseFragActivity)getActivity()).navigateTo(MultipageFragment.class, null, true,MultipageFragment.TAG);
		//Toast.makeText(getActivity(), "已进入多页模式", 1).show();
		//Intent intent=new Intent(getActivity(),MainActivity.class);
		//startActivity(intent);
		
	}
	
	//点击搜索图标
	@Override
	public void goSearch() {
		// TODO Auto-generated method stub
		
	}
	//输入框点击监听
	@Override
	public void goEditInput() {
		((BaseFragActivity) this.getActivity()).navigateTo(UrlRedirectFragment.class, null, true, UrlRedirectFragment.TAG);
	}
	//二维码跳转
	@Override
	public void goCode() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), CaptureActivity.class);
		startActivity(intent);
	}
	//登录注册监听
	@Override
	public void goLand() {
		mWebView.loadUrl(UrlUtils.URL_LOGIN);
	}
	
	@Override
	public void startPage(String url) {
		if(JBLPreference.getInstance(this.getActivity()).readInt(JBLPreference.FULL_SCREEN_TYPE)==0){  //全屏模式
			if(url.equals(UrlUtils.URL_GET_HOST)){                //主页：显示上下菜单栏，不显示悬浮按钮
				
				getFragmentManager().beginTransaction().show(toolbarFragment).show(topActionbarFragment).commit();
            	if(popWindow.isShowing()){
            		popWindow.dismiss();
				}
			}else{                                              //不是主页：不显示上下菜单栏，显示悬浮按钮
				getFragmentManager().beginTransaction().hide(toolbarFragment).hide(topActionbarFragment).commit();
				if(popWindow!=null){
					popWindow.showAtLocation(popview, Gravity.RIGHT|Gravity.BOTTOM, 0, 60);
				}
			}
		}
		if(JBLPreference.getInstance(this.getActivity()).readInt(JBLPreference.TURNING_TYPE)==0){  //翻页模式
			if(url.equals(UrlUtils.URL_GET_HOST)){                //主页：显示上下菜单栏，不显示悬浮按钮
				
				getFragmentManager().beginTransaction().show(toolbarFragment).show(topActionbarFragment).commit();
            	if(popWindow!=null&&popWindow.isShowing()){
            		popWindow.dismiss();
            	}
				
			}
		}
		
	}

}
