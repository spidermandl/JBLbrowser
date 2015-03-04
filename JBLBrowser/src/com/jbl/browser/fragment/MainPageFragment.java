package com.jbl.browser.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ScheduledExecutorService;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Display;
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
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.hugo.android.scanner.CaptureActivity;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.BrowserSettings;
import com.jbl.browser.R;
import com.jbl.browser.WebWindowManagement;
import com.jbl.browser.activity.BaseFragActivity;
import com.jbl.browser.activity.BrowserSettingActivity;
import com.jbl.browser.activity.DownloadManageActivity;
import com.jbl.browser.activity.HistoryFavourateActivity;
import com.jbl.browser.activity.MainFragActivity;
import com.jbl.browser.activity.MultipageActivity;
import com.jbl.browser.adapter.MultipageAdapter;
import com.jbl.browser.bean.BookMark;
import com.jbl.browser.bean.History;
import com.jbl.browser.db.BookMarkDao;
import com.jbl.browser.db.HistoryDao;
import com.jbl.browser.interfaces.LoadURLInterface;
import com.jbl.browser.interfaces.SettingItemInterface;
import com.jbl.browser.interfaces.ToolbarItemInterface;
import com.jbl.browser.interfaces.TopActionbarInterface;
import com.jbl.browser.utils.BrightnessSettings;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.JBLPreference.BoolType;
import com.jbl.browser.utils.StringUtils;
import com.jbl.browser.utils.UrlUtils;
import com.jbl.browser.view.ProgressWebView;
import com.jbl.browser.view.UserDefinedDialog;
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
	private ProgressWebView mWebView; // 主控件 webview
	private WebSettings settings;
	private BottomMenuFragment toolbarFragment;//底部toolbar
	private SettingPagerFragment settingFragment;//底部弹出菜单 fragment
	private TopMenuFragment topActionbarFragment; //顶部actionbar
	private FrameLayout webFrame;//webview父控件	
	public String cur_url; // 设置初始网址
	public String webName=""; // 网页名	
	private MultipageAdapter multipageAdapter;//多页效果适配器 
	private ScheduledExecutorService scheduledExecutorService;
	View popview;//翻页按钮布局
	PopupWindow popWindow;//悬浮窗口
	View multipagePanel;//多页布局
	PageIndicator multipageIndicator;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//cur_url=UrlUtils.URL_GET_HOST;
		View view = inflater.inflate(R.layout.fragment_main_page, container,false); 
		mWebView = WebWindowManagement.getInstance().getMainWebView();
		//判断mWebView是否存在parent view
		ViewGroup p = (ViewGroup) mWebView.getParent(); 
         if (p != null) { 
             p.removeAllViewsInLayout(); 
         }
	    webFrame=((FrameLayout) view.findViewById(R.id.web_view_frame));
	    webFrame.addView(mWebView);// webview
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
				if(JBLPreference.getInstance(getActivity()).readInt(BoolType.FULL_SCREEN.toString())==JBLPreference.YES_FULL //当全屏模式：触摸屏幕不显示上下菜单栏
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
	public void onDestroyView() {  
		//移除重复使用的view
		webFrame.removeView(mWebView);
		//销毁内嵌的fragment
        getFragmentManager().beginTransaction().remove(toolbarFragment).commit();  
        getFragmentManager().beginTransaction().remove(settingFragment).commit();
        getFragmentManager().beginTransaction().remove(topActionbarFragment).commit();
		super.onDestroyView();
	}
	/**
	 * 初始化webview
	 */
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
				Intent intent = new Intent();
				intent.setClass(getActivity(), DownloadManageActivity.class);
				startActivity(intent);
				((MainFragActivity)getActivity()).startDownload(url);
			}
		});
	}
	
	/**
	 * 添加书签
	 */
	private void addNewBookMark(boolean isRecommend) {
		boolean flag=false;
		BookMark bookMark =new BookMark();
		bookMark.setWebName(mWebView.getWebName());
		bookMark.setWebAddress(mWebView.getCurrentUrl());
		bookMark.setRecommend(isRecommend);
		flag=new BookMarkDao(getActivity()).addBookMark(bookMark);
		if (flag)
			Toast.makeText(getActivity(), R.string.add_bookmark_succeed, Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(getActivity(), R.string.add_bookmark_fail, Toast.LENGTH_SHORT).show();
	}
	

	/**
	 * 点击搜索时将搜索网址名称和网址存入数据库
	 * 在UrlRedirectFragment中调用
	 * *//*
	public void addSearchRecord(boolean isRecommend){
		boolean flag=true;
		BookMark bookMark =new BookMark();
		bookMark.setWebName(mWebView.getWebName());
		bookMark.setWebAddress(mWebView.getCurrentUrl());
		bookMark.setRecommend(isRecommend);
		flag=new BookMarkDao(getActivity()).addBookMark(bookMark);
	}*/
	
	/**
	 * 设置主页网页模式
	 * @param type  设置类型
	 */
	private void webBoolSetting(BoolType type){
		int value=-1;
		switch (type) {
		case FULL_SCREEN:
			value=JBLPreference.getInstance(getActivity()).readInt(BoolType.FULL_SCREEN.toString());
			if(value!=JBLPreference.YES_FULL){
				//当要开启全屏浏览模式时，隐藏顶部状态栏、底部菜单栏和顶部搜索栏
				hideStatusBar();
	            createPopShrinkFullScreen();
	            if(!mWebView.getUrl().equals(UrlUtils.URL_GET_HOST)){
	            	getFragmentManager().beginTransaction().hide(toolbarFragment).commit();
		            getFragmentManager().beginTransaction().hide(topActionbarFragment).commit();
	            	popWindow.showAtLocation(popview, Gravity.RIGHT|Gravity.BOTTOM, 0, 60);
	            } 
	    		JBLPreference.getInstance(getActivity()).writeInt(type.toString(),JBLPreference.YES_FULL);//写入缓存
	            Toast.makeText(getActivity(), StringUtils.OPEN_NO_FULL, Toast.LENGTH_SHORT).show();
			}else{
				//当要关闭全屏浏览模式时，显示顶部状态栏、底部菜单栏和顶部搜索栏
				popWindow.dismiss();
				showStatusBar();
	            getFragmentManager().beginTransaction().show(toolbarFragment).commit();
            	getFragmentManager().beginTransaction().show(topActionbarFragment).commit();
            	Toast.makeText(getActivity(), StringUtils.CLOSE_NO_FULL, Toast.LENGTH_SHORT).show();
	    		JBLPreference.getInstance(getActivity()).writeInt(type.toString(),JBLPreference.NO_FULL);//写入缓存
			}
			break;

		case PIC_CACHE:
			value=JBLPreference.getInstance(getActivity()).readInt(BoolType.PIC_CACHE.toString());
			if(value!=JBLPreference.YES_PICTURE){
				//当要开启无图模式时
				mWebView.getSettings().setBlockNetworkImage(false);
	    		JBLPreference.getInstance(getActivity()).writeInt(type.toString(),JBLPreference.YES_PICTURE);//写入缓存
				Toast.makeText(getActivity(), StringUtils.CLOSE_NO_PICTURE, Toast.LENGTH_SHORT).show();
			}else{
				//当要关闭无图模式时
				mWebView.getSettings().setBlockNetworkImage(true);
	    		JBLPreference.getInstance(getActivity()).writeInt(type.toString(),JBLPreference.NO_PICTURE);//写入缓存
				Toast.makeText(getActivity(), StringUtils.OPEN_NO_PICTURE, Toast.LENGTH_SHORT).show();
			}
			break;
			
		case TURNNING:
			value=JBLPreference.getInstance(getActivity()).readInt(BoolType.TURNNING.toString());
			if(value!=JBLPreference.OPEN_TURNING_BUTTON){
				//当要开启翻页模式
	            createTurningPage();
	            if(!mWebView.getUrl().equals(UrlUtils.URL_GET_HOST)){
	            	popWindow.showAtLocation(popview, Gravity.RIGHT, 0, 0);
	            } 
	    		JBLPreference.getInstance(getActivity()).writeInt(type.toString(),JBLPreference.OPEN_TURNING_BUTTON);//写入缓存
	            Toast.makeText(getActivity(), StringUtils.OPEN_TURNING_BUTTON, Toast.LENGTH_SHORT).show();
			}else{
				//当要关闭翻页模式
				popWindow.dismiss();
            	Toast.makeText(getActivity(), StringUtils.CLOSE_TURNING_BUTTON, Toast.LENGTH_SHORT).show();
	    		JBLPreference.getInstance(getActivity()).writeInt(type.toString(),JBLPreference.CLOSE_TURNING_BUTTON);//写入缓存
			}
			break;
			
		case HISTORY_CACHE:
			value=JBLPreference.getInstance(getActivity()).readInt(BoolType.HISTORY_CACHE.toString());
			if(value!=JBLPreference.CLOSE_HISTORY){
				//关闭无痕
				JBLPreference.getInstance(getActivity()).writeInt(type.toString(),JBLPreference.CLOSE_HISTORY);//写入缓存
	            Toast.makeText(getActivity(), StringUtils.CLOSE_NO_HISTORY, Toast.LENGTH_SHORT).show();
			}else{
				//开启无痕
				JBLPreference.getInstance(getActivity()).writeInt(type.toString(),JBLPreference.OPEN_HISTORY);//写入缓存
	            Toast.makeText(getActivity(), StringUtils.OPEN_NO_HISTORY, Toast.LENGTH_SHORT).show();
			}
			break;
		case BRIGHTNESS_TYPE:
			value=JBLPreference.getInstance(getActivity()).readInt(BoolType.BRIGHTNESS_TYPE.toString());
			if(value!=JBLPreference.NIGHT_MODEL){
				//夜间模式
				JBLPreference.getInstance(getActivity()).writeInt(type.toString(),JBLPreference.NIGHT_MODEL);
				BrightnessSettings.showPopSeekBrightness(getActivity());
				
			}else{
				//日间模式
				JBLPreference.getInstance(getActivity()).writeInt(type.toString(),JBLPreference.DAY_MODEL);
				BrightnessSettings.setActScreenBrightness(getActivity(), -1);
			}
			break;
		default:
			break;
		}
	}

	//显示翻页模式
	private void createTurningPage(){
		LayoutInflater mLayoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popview=(View)mLayoutInflater.inflate(R.layout.pop_window_nextpager, null);
		popWindow=new PopupWindow(popview,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		//popWindow.showAtLocation(popview, Gravity.RIGHT, 0, 0);
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
	//隐藏状态栏
	public void hideStatusBar(){
		WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getActivity().getWindow().setAttributes(lp);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}
	//显示状态栏
	public void showStatusBar(){
		WindowManager.LayoutParams attr = getActivity().getWindow().getAttributes();
        attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().setAttributes(attr);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}
	
	@Override
	public void addBookMark() {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_bookmark_dialog, null);
		final Dialog addBookMark=UserDefinedDialog.getInstance().defineViewDialog(getActivity(), null, null);
		addBookMark.setContentView(view);  //设置其整个的样式
		TextView addToBookMark=(TextView)view.findViewById(R.id.add_to_bookmark_tv);
		TextView addToRecommend=(TextView)view.findViewById(R.id.add_to_recommend_tv);
		addToBookMark.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainPageFragment.this.addNewBookMark(false);
				addBookMark.dismiss();
			}
		});
		addToRecommend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainPageFragment.this.addNewBookMark(true);
				addBookMark.dismiss();
			}
		});
		Button addBookMarkCancel=(Button)view.findViewById(R.id.add_bookmark_cancel);
		addBookMarkCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addBookMark.dismiss();
			}
		});
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
		webBoolSetting(BoolType.PIC_CACHE);
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
	public void refresh() {     //刷新当前界面
		// TODO Auto-generated method stub
		mWebView.reload();
	}
	@Override
	public void withoutTrace() { // 无痕浏览
		// TODO Auto-generated method stub
		webBoolSetting(BoolType.HISTORY_CACHE);
	}
	@Override
	public void fullScreen() {     //全屏浏览
		// TODO Auto-generated method stub
		webBoolSetting(BoolType.FULL_SCREEN);
	}
	@Override
	public void pageTurningSwitch() {//翻页模式
		// TODO Auto-generated method stub
		webBoolSetting(BoolType.TURNNING);
	}
	@Override
	public void nightBright() {     //夜间模式
		// TODO Auto-generated method stub
		webBoolSetting(BoolType.BRIGHTNESS_TYPE);
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
		//((BaseFragActivity)getActivity()).navigateTo(MultipageFragment.class, null, true,MultipageFragment.TAG);
		//Toast.makeText(getActivity(), "已进入多页模式", 1).show();
		Intent intent=new Intent();
		intent.setClass(getActivity(), MultipageActivity.class);
		startActivity(intent);	
	}
	//点击搜索图标
	@Override
	public void goSearch() {
		
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
	//载入网页监听
	@Override
	public void startPage(String url) {
		if(JBLPreference.getInstance(this.getActivity()).readInt(BoolType.FULL_SCREEN.toString())==JBLPreference.YES_FULL){  //全屏模式
			hideStatusBar();              //当运行后开启全屏，退出程序，再运行时需重新建popwindow和隐藏状态栏
			if(popWindow==null){
				createPopShrinkFullScreen();
			}
			if(url.equals(UrlUtils.URL_GET_HOST)){                //主页：显示上下菜单栏，不显示悬浮按钮
				getFragmentManager().beginTransaction().show(toolbarFragment).show(topActionbarFragment).commit();
				popview.post(new Runnable() {                   //activity的生命周期函数全部执行完毕,才可以执行popwindow
					   public void run() {
			            		popWindow.dismiss();
					 }
				});
				
			}else{                                              //不是主页：不显示上下菜单栏，显示悬浮按钮
				getFragmentManager().beginTransaction().hide(toolbarFragment).hide(topActionbarFragment).commit();
				popview.post(new Runnable() {                   //activity的生命周期函数全部执行完毕,才可以执行popwindow
					   public void run() {
						   popWindow.showAtLocation(popview, Gravity.RIGHT|Gravity.BOTTOM, 0, 60);
						   }
						});
			}
		}
		if(JBLPreference.getInstance(this.getActivity()).readInt(BoolType.TURNNING.toString())==JBLPreference.OPEN_TURNING_BUTTON){  //全屏模式
			if(popWindow==null){
				createTurningPage();;
			}      		
			if(url.equals(UrlUtils.URL_GET_HOST)){                //主页：显示上下菜单栏，不显示悬浮按钮
				if(popWindow.isShowing()){
            		popWindow.dismiss();
				}
			}else{                                              //不是主页：不显示上下菜单栏，显示悬浮按钮
				popview.post(new Runnable() {                   //activity的生命周期函数全部执行完毕,才可以执行popwindow
					   public void run() {
						   popWindow.showAtLocation(popview, Gravity.RIGHT, 0, 0);
					 }
				});
			}
		}
		
	}

	@Override
	public void stopPage(WebView view,String url) {
		// TODO Auto-generated method stub
		if(JBLPreference.getInstance(getActivity()).readInt(BoolType.HISTORY_CACHE.toString())==JBLPreference.CLOSE_HISTORY){   //判断不是无痕浏览，添加历史记录
			if(url!=""){           
				String date = new SimpleDateFormat("yyyyMMdd", Locale.CHINA)
						.format(new Date()).toString();
				String temp=UrlUtils.URL_GET_HOST.substring(0, UrlUtils.URL_GET_HOST.length());
				if(!url.equals(temp)){      //当加载默认网址时不加入历史记录
					History history = new History();
					history.setWebAddress(url);
					history.setWebName(view.getTitle());
					// 加载完加入历史记录
					new HistoryDao(getActivity()).addHistory(history);
				}
			}
		}
		//以下代码加上后：在主activity界面设置夜间模式后到其他activity中调转回来出现黑屏
		/*//判断是夜间模式需再设置下activity亮度
		if(JBLPreference.getInstance(getActivity()).readInt(BoolType.BRIGHTNESS_TYPE.toString())==JBLPreference.NIGHT_MODEL){
			int brightness=JBLPreference.getInstance(getActivity()).readInt(JBLPreference.NIGHT_BRIGHTNESS_VALUS);
			BrightnessSettings.setActScreenBrightness(getActivity(),brightness);
		}*/
	}
}
