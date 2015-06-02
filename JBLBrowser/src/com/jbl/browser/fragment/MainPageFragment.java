package com.jbl.browser.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.BrowserSettings;
import com.jbl.browser.JBLApplication;
import com.jbl.browser.R;
import com.jbl.browser.WebWindowManagement;
import com.jbl.browser.activity.BaseFragActivity;
import com.jbl.browser.activity.BrowserSettingActivity;
import com.jbl.browser.activity.DownloadManageActivity;
import com.jbl.browser.activity.HistoryFavourateActivity;
import com.jbl.browser.activity.MainFragActivity;
import com.jbl.browser.activity.RecommendMainActivity;
import com.jbl.browser.activity.ScannerManageActivity;
import com.jbl.browser.adapter.MultipageAdapter;
import com.jbl.browser.bean.BookMark;
import com.jbl.browser.bean.History;
import com.jbl.browser.db.BookMarkDao;
import com.jbl.browser.db.HistoryDao;
import com.jbl.browser.interfaces.LoadURLInterface;
import com.jbl.browser.interfaces.SettingItemInterface;
import com.jbl.browser.interfaces.ShareInterface;
import com.jbl.browser.interfaces.ToolbarItemInterface;
import com.jbl.browser.interfaces.TopActionbarInterface;
import com.jbl.browser.tools.BusinessTool;
import com.jbl.browser.utils.BrightnessSettings;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.JBLPreference.BoolType;
import com.jbl.browser.utils.StringUtils;
import com.jbl.browser.utils.UrlUtils;
import com.jbl.browser.view.BaseWedget.WedgetClickListener;
import com.jbl.browser.view.FullScreenWedget;
import com.jbl.browser.view.ProgressWebView;
import com.jbl.browser.view.ScrollPageWedget;
import com.jbl.browser.view.UserDefinedDialog;

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
                                              LoadURLInterface,
                                              ShareInterface,
                                              WedgetClickListener{
	public final static String TAG = "MainPageFragment";
	/* 定义webview控件 */
	public  ProgressWebView mWebView; // 主控件 webview
	private BottomMenuFragment toolbarFragment;//底部toolbar
	private SettingPagerFragment settingFragment;//底部弹出菜单 fragment
	private TopMenuFragment topActionbarFragment; //顶部actionbar
	private FrameLayout webFrame;//webview父控件	
	private MultipageAdapter multipageAdapter;//多页效果适配器 
	private ScheduledExecutorService scheduledExecutorService;
	
	View multipagePanel;//多页布局
	
	//全屏浮动按钮
    FullScreenWedget fullWedget;
    //翻页浮动按钮 
    ScrollPageWedget scrollWedget;

	int statusBarHeight;//状态栏高度
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_page, container,false);
		
		webFrame = ((FrameLayout) view.findViewById(R.id.web_view_frame));
		mWebView = WebWindowManagement.getInstance().replaceMainWebView(webFrame);
		// //Intent intent = getActivity().getIntent();
		// //监听webview跳转，实现activity跳转到推荐页面
		mWebView.setInterface(this);// 设置回调接口
		toolbarFragment = (BottomMenuFragment) (this.getActivity().getSupportFragmentManager().findFragmentById(R.id.bottom_toolbar_fragment));
		toolbarFragment.setInterface(this);// 设置回调接口

		settingFragment = new SettingPagerFragment();
		settingFragment.setInterface(this);// 设置回调接口

		topActionbarFragment = (TopMenuFragment) (this.getActivity().getSupportFragmentManager().findFragmentById(R.id.top_menu_fragment));
		topActionbarFragment.setTopActionbar(this);// 设置回调接口

		// 设置友好交互，即如果该网页中有链接，在本浏览器中重新定位并加载，而不是调用系统的浏览器
		mWebView.requestFocus();
		//全屏浮动按钮
		fullWedget=new FullScreenWedget(getActivity());
		fullWedget.setInterface(this);
		//翻页浮动按钮
		scrollWedget = new ScrollPageWedget(getActivity());
		scrollWedget.setInterface(this);
		//获取状态栏高度
		Rect frame = new Rect();
		getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		statusBarHeight = frame.top;
		/*
		 * 设置webview字体大小
		 */
		int fontSize = JBLPreference.getInstance(this.getActivity()).readInt(JBLPreference.FONT_TYPE);
		BrowserSettings.getInstance().addObserver(mWebView.getSettings());
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
	 * 动态改变字体大小
	 * */
	public void initmWebViewSize(){
		
		int fontSize = JBLPreference.getInstance(this.getActivity()).readInt(JBLPreference.FONT_TYPE);
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
	}
	/**
	 * 初始化webview
	 */
	public void initWebView() {
		mWebView.setDefaultSetting();
		String urlAddress=JBLPreference.getInstance(getActivity()).readString(JBLPreference.BOOKMARK_HISTORY_KEY);
		if(JBLApplication.getInstance().isEntering()){//初始url矫正
			JBLApplication.getInstance().setEntering(false);
			if(!urlAddress.equals(UrlUtils.URL_GET_HOST)){
				JBLPreference.getInstance(getActivity()).writeString(JBLPreference.BOOKMARK_HISTORY_KEY, UrlUtils.URL_GET_HOST);
				urlAddress=UrlUtils.URL_GET_HOST;
			}
		}
		if(urlAddress==null||urlAddress.length()==0){
			mWebView.loadUrl(UrlUtils.URL_GET_HOST);
		}else{
			mWebView.loadUrl(urlAddress);
		}

		//添加下载监听
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
				if (JBLPreference.getInstance(getActivity()).readInt(
						BoolType.FULL_SCREEN.toString()) == JBLPreference.YES_FULL // 当全屏模式：触摸屏幕不显示上下菜单栏
						&& toolbarFragment.isVisible()
						&& topActionbarFragment.isVisible()
						&& !mWebView.getUrl().equals(UrlUtils.URL_GET_HOST)) {
					fullWedget.show();
					getFragmentManager().beginTransaction().hide(toolbarFragment).commit();
					getFragmentManager().beginTransaction().hide(topActionbarFragment).commit();
				}
				return false;
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
	 * 设置主页网页模式
	 * @param type  设置类型
	 */
	private void webBoolSetting(BoolType type){
		int value=-1;
		switch (type) {
		case FULL_SCREEN:
			value=JBLPreference.getInstance(getActivity()).readInt(BoolType.FULL_SCREEN.toString());
			if(value!=JBLPreference.YES_FULL){
				//当要开启全屏浏览模式时，底部菜单栏和顶部搜索栏
	            if(!UrlUtils.URL_GET_HOST.equals(mWebView.getUrl())){
					fullWedget.show();
	            	getFragmentManager().beginTransaction().hide(toolbarFragment).commit();
		            getFragmentManager().beginTransaction().hide(topActionbarFragment).commit();	
	            } 
	    		JBLPreference.getInstance(getActivity()).writeInt(type.toString(),JBLPreference.YES_FULL);//写入缓存
	            Toast.makeText(getActivity(), StringUtils.OPEN_NO_FULL, Toast.LENGTH_SHORT).show();
			}else{
				//当要关闭全屏浏览模式时，显示顶部状态栏、底部菜单栏和顶部搜索栏
				fullWedget.dismiss();
	            getFragmentManager().beginTransaction().show(toolbarFragment).commit();
            	getFragmentManager().beginTransaction().show(topActionbarFragment).commit();
	    		JBLPreference.getInstance(getActivity()).writeInt(type.toString(),JBLPreference.NO_FULL);//写入缓存
            	Toast.makeText(getActivity(), StringUtils.CLOSE_NO_FULL, Toast.LENGTH_SHORT).show();
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
	            if(!mWebView.getUrl().equals(UrlUtils.URL_GET_HOST)){
	            	scrollWedget.show();
	            } 
	    		JBLPreference.getInstance(getActivity()).writeInt(type.toString(),JBLPreference.OPEN_TURNING_BUTTON);//写入缓存
	            Toast.makeText(getActivity(), StringUtils.OPEN_TURNING_BUTTON, Toast.LENGTH_SHORT).show();
			}else{
				//当要关闭翻页按钮
				scrollWedget.dismiss();
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
				BrightnessSettings.setBrightness(getActivity(), -1); 
			}
			break;
		default:
			break;
		}
	}

//	//隐藏状态栏
//	private void hideStatusBar(){
//		WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        getActivity().getWindow().setAttributes(lp);
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//	}
//	//显示状态栏
//	private void showStatusBar(){
//		WindowManager.LayoutParams attr = getActivity().getWindow().getAttributes();
//        attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getActivity().getWindow().setAttributes(attr);
//        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//	}
	
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
//		ShareDialog shareDialog=new ShareDialog(getActivity());
//		shareDialog.setTitle(R.string.select_share);
//		shareDialog.show();
//		shareDialog.setInterface(this);
		
		Intent shareIntent = new Intent(Intent.ACTION_SEND);		
		shareIntent.setType("text/plain");		
		shareIntent.putExtra(Intent.EXTRA_TEXT, "分享：@"+mWebView.getTitle()+"\n"+mWebView.getUrl());		
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
				getFragmentManager().beginTransaction().remove(MainPageFragment.this).commit();//必须要加 负责saveinstance 会比fragment transition 先调用
				JBLApplication.getInstance().quit();
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
	public void nightBright() {//夜间模式
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
			//当进入gridview时判断当前网页地址是不是主页，是非判断结果存到缓存中
			if(mWebView.getCurrentUrl().equals(UrlUtils.URL_GET_HOST)){
				JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.HOST_URL_BOOLEAN, JBLPreference.IS_HOST_URL);
			}else{
				JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.HOST_URL_BOOLEAN, JBLPreference.ISNOT_HOST_URL);
			}
			
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if (!settingFragment.isVisible() || getFragmentManager().findFragmentByTag(SettingPagerFragment.TAG) == null) {
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
		//移除重复使用的view
		webFrame.removeView(mWebView);
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
		intent.setClass(getActivity(), ScannerManageActivity.class);
		startActivity(intent);
	}
	//登录注册监听
	@Override
	public void goHot() {
		//mWebView.loadUrl(UrlUtils.URL_LOGIN);
		Intent in=new Intent();
		in.setClass(this.getActivity(),RecommendMainActivity.class);
		startActivity(in);
	}
	//载入网页监听
	@Override
	public void startPage(String url) {
		/**判断是否有全屏*/
		if(JBLPreference.getInstance(this.getActivity()).readInt(BoolType.FULL_SCREEN.toString())==JBLPreference.YES_FULL){  //全屏模式
			if(url.equals(UrlUtils.URL_GET_HOST)){                //主页：显示上下菜单栏，不显示悬浮按钮
				getFragmentManager().beginTransaction()
				.show(toolbarFragment)
				.show(topActionbarFragment).commit();
				fullWedget.dismiss();
				
			}else{                                              //不是主页：不显示上下菜单栏，显示悬浮按钮
				getFragmentManager().beginTransaction()
				.hide(toolbarFragment)
				.hide(topActionbarFragment).commit();
				fullWedget.show();
			}
		}
		/**判断是否有翻页*/
		if(JBLPreference.getInstance(this.getActivity()).readInt(BoolType.TURNNING.toString())==JBLPreference.OPEN_TURNING_BUTTON){  //全屏模式     		
			if(url.equals(UrlUtils.URL_GET_HOST)){                //主页：显示上下菜单栏，不显示悬浮按钮
				scrollWedget.dismiss();
			}else{                                              //不是主页：不显示上下菜单栏，显示悬浮按钮
				scrollWedget.show();
			}
		}
		/**判断是夜间模式需再设置下activity亮度*/
  		if(JBLPreference.getInstance(getActivity()).readInt(BoolType.BRIGHTNESS_TYPE.toString())==JBLPreference.NIGHT_MODEL){
  			int brightness=JBLPreference.getInstance(getActivity()).readInt(JBLPreference.NIGHT_BRIGHTNESS_VALUS);
  			BrightnessSettings.setBrightness(getActivity(),brightness);
  		}
  		
	}

	@Override
	public void stopPage(WebView view,String url) {
		// TODO Auto-generated method stub
		if(JBLPreference.getInstance(getActivity()).readInt(BoolType.HISTORY_CACHE.toString())==JBLPreference.CLOSE_HISTORY){   //判断不是无痕浏览，添加历史记录
			if(url!=""){           
				//String date = new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(new Date()).toString();
				
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

	}

	@Override
	public void shareMore() {
		// TODO Auto-generated method stub
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
    	shareIntent.setType("text/plain");
    	shareIntent.putExtra(Intent.EXTRA_TEXT, "分享：@"+mWebView.getTitle()+"\n"+mWebView.getUrl());
    	shareIntent.putExtra(Intent.EXTRA_SUBJECT,mWebView.getTitle());
    	
    	try {
    		getActivity().startActivity(Intent.createChooser(shareIntent, getActivity().getString(R.string.main_share_chooser_title)));
        } catch(android.content.ActivityNotFoundException ex) {
           
        }
	}
	
	public void onBackPressed(){
		 if(mWebView.canGoBack()) 
		     mWebView.goBack(); //goBack()表示返回WebView的上一页面  
		 else
			quit();//直接退出fragment，不会出现白色界面
	}

	@Override
	public void authSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goPersonal() {
		mWebView.loadUrl(UrlUtils.URL_PERSONAL_MANAGEMENT+BusinessTool.getDeviceID(getActivity()));
		
	}

	@Override
	public void onFullClick() {
		getFragmentManager().beginTransaction().show(toolbarFragment).commit();
		getFragmentManager().beginTransaction().show(topActionbarFragment).commit();
	}

	@SuppressLint("NewApi") @Override
	public void onPageScroll(boolean up) {
		if(up){
			float scrollY = mWebView.getScrollY();
			int y = mWebView.getHeight();
			if (scrollY > 0) {
				if (scrollY > y) {
					mWebView.scrollBy(0,(int) (mWebView.getScaleY() - mWebView.getHeight()));
				} else {
					mWebView.scrollBy(0,(int) (mWebView.getScaleY() - scrollY));
				}
			} else {
				mWebView.scrollBy(0, 0);
			}
		}else{
			// 判断向下滚动是否已经到网页底部
			float fullHeight = mWebView.getContentHeight()* mWebView.getScale();
			float contentHeight = mWebView.getHeight()+ mWebView.getScrollY();
			int y = mWebView.getHeight();
			if ((fullHeight - contentHeight) > 0) {
				if ((fullHeight - contentHeight) > y) {
					mWebView.scrollBy(0,(int) (mWebView.getHeight() + mWebView.getScaleY()));
				} else {
					mWebView.scrollBy(0, (int) (fullHeight - contentHeight));
				}
			}

		}
	}
	
}
