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
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.hugo.android.scanner.CaptureActivity;

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
	public  ProgressWebView mWebView; // 主控件 webview
	private BottomMenuFragment toolbarFragment;//底部toolbar
	private SettingPagerFragment settingFragment;//底部弹出菜单 fragment
	private TopMenuFragment topActionbarFragment; //顶部actionbar
	private FrameLayout webFrame;//webview父控件	
	private MultipageAdapter multipageAdapter;//多页效果适配器 
	private ScheduledExecutorService scheduledExecutorService;
	View popview_page;//翻页按钮布局
	PopupWindow popWindow_page;//翻页悬浮窗口
	LinearLayout popview_full_screen;//全屏按钮布局
	PopupWindow popWindow_full_screen;//全屏悬浮窗口
	View popview_seekBar;//亮度调节布局
	PopupWindow popWindow_seekBar;//亮度调节悬浮
	View multipagePanel;//多页布局
	
	//翻页按钮初始位置
	int mCurrentX_pop_page;
	int mCurrentY_pop_page;
	//全屏按钮初始位置
	int mCurrentX_pop_full_screen;
	int mCurrentY_pop_full_screen;
	//屏幕高和宽
	int width;  
	int height;
	int statusBarHeight;//状态栏高度
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_page, container,
				false);
		webFrame = ((FrameLayout) view.findViewById(R.id.web_view_frame));
		mWebView = WebWindowManagement.getInstance().replaceMainWebView(webFrame);
		// //Intent intent = getActivity().getIntent();
		// //监听webview跳转，实现activity跳转到推荐页面
		mWebView.setInterface(this);// 设置回调接口

		WebWindowManagement.getInstance().replaceWebViewWithIndex(null, 1,false);
		WebWindowManagement.getInstance().replaceWebViewWithIndex(null, 2,false);

		toolbarFragment = (BottomMenuFragment) (this.getActivity().getSupportFragmentManager().findFragmentById(R.id.bottom_toolbar_fragment));
		toolbarFragment.setInterface(this);// 设置回调接口

		settingFragment = new SettingPagerFragment();
		settingFragment.setInterface(this);// 设置回调接口

		topActionbarFragment = (TopMenuFragment) (this.getActivity().getSupportFragmentManager().findFragmentById(R.id.top_menu_fragment));
		topActionbarFragment.setTopActionbar(this);// 设置回调接口

		// 设置友好交互，即如果该网页中有链接，在本浏览器中重新定位并加载，而不是调用系统的浏览器
		mWebView.requestFocus();
		DisplayMetrics metric = new DisplayMetrics();
	    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
	    width=metric.widthPixels;
	    JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.SCREEN_WIDTH, width);
	    height=metric.heightPixels;
	    mCurrentX_pop_full_screen = metric.widthPixels-width/7;     // 全屏按钮初始X轴位置
		mCurrentY_pop_full_screen =metric.heightPixels-width/7;   // 全屏按钮初始Y轴位置
		 
		//获取状态栏高度
		Rect frame = new Rect();
		getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		statusBarHeight = frame.top;
		/*
		 * 设置webview字体大小
		 */
  		BrowserSettings.getInstance().addObserver(mWebView.getSettings());
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
					createPopShrinkFullScreen();
					final int x=JBLPreference.getInstance(getActivity()).readInt(JBLPreference.pop_full_currentX_value);
					final int y=JBLPreference.getInstance(getActivity()).readInt(JBLPreference.pop_full_currentY_value);
					popview_full_screen.post(new Runnable() {                   //activity的生命周期函数全部执行完毕,才可以执行popwindow
						   public void run() {
							   popWindow_full_screen.showAtLocation(popview_full_screen, Gravity.NO_GRAVITY, x, y);
							   }

							});
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
		mWebView.setDefaultSetting();
		String urlAddress=JBLPreference.getInstance(getActivity()).readString(JBLPreference.BOOKMARK_HISTORY_KEY);
		if(urlAddress==null||urlAddress.length()==0){
			mWebView.loadUrl(UrlUtils.URL_GET_HOST);
		}else{
			mWebView.loadUrl(urlAddress);
		}
		//在progressWebView中已经有监听。
		/*//监听物理返回键
		mWebView.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				 if (event.getAction() == KeyEvent.ACTION_DOWN) {
					 if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
						 mWebView.goBack(); //goBack()表示返回WebView的上一页面  
				         return true;  
					 }else{
						 System.exit(0);//直接退出fragment，不会出现白色界面
					 }
				 }
				return false;
			}
		});*/

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
				//当要开启全屏浏览模式时，隐藏顶部状态栏、底部菜单栏和顶部搜索栏
				hideStatusBar();
				createPopShrinkFullScreen();
	            if(!UrlUtils.URL_GET_HOST.equals(mWebView.getUrl())){
	            	getFragmentManager().beginTransaction().hide(toolbarFragment).commit();
		            getFragmentManager().beginTransaction().hide(topActionbarFragment).commit();	
		            popWindow_full_screen.showAtLocation(popview_full_screen, Gravity.NO_GRAVITY, mCurrentX_pop_full_screen, mCurrentY_pop_full_screen);
	            } 
	    		JBLPreference.getInstance(getActivity()).writeInt(type.toString(),JBLPreference.YES_FULL);//写入缓存
	            Toast.makeText(getActivity(), StringUtils.OPEN_NO_FULL, Toast.LENGTH_SHORT).show();
			}else{
				//当要关闭全屏浏览模式时，显示顶部状态栏、底部菜单栏和顶部搜索栏
				popview_full_screen.post(new Runnable() {                   //activity的生命周期函数全部执行完毕,才可以执行popwindow
					   public void run() {
			            		popWindow_full_screen.dismiss();
					 }
				});
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
	            		popWindow_page.showAtLocation(popview_page, Gravity.NO_GRAVITY, mCurrentX_pop_page, mCurrentY_pop_page);
	            } 
	    		JBLPreference.getInstance(getActivity()).writeInt(type.toString(),JBLPreference.OPEN_TURNING_BUTTON);//写入缓存
	            Toast.makeText(getActivity(), StringUtils.OPEN_TURNING_BUTTON, Toast.LENGTH_SHORT).show();
			}else{
				//当要关闭翻页按钮
				popview_page.post(new Runnable() {                   //activity的生命周期函数全部执行完毕,才可以执行popwindow
					   public void run() {
			            		popWindow_page.dismiss();
					 }
				});
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
				BrightnessSettings.showPopSeekBrightness(getActivity(),width);
				
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
	//显示翻页模式
	private void createTurningPage(){
		LayoutInflater mLayoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popview_page=mLayoutInflater.inflate(R.layout.pop_window_nextpager, null);
		popWindow_page=new PopupWindow(popview_page,80,240);
		mCurrentX_pop_page = width-popWindow_page.getWidth();     // 翻页按钮初始X轴位置
	    mCurrentY_pop_page =(height)/2-popWindow_page.getHeight()/2;   // 翻页按钮初始Y轴位置
		popview_page.setOnTouchListener(new OnTouchListener() {
			float mX, mY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mX = mCurrentX_pop_page - event.getRawX();
					mY = mCurrentY_pop_page - event.getRawY();
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					mCurrentX_pop_page = (int) (event.getRawX() + mX);
					mCurrentY_pop_page = (int) (event.getRawY() + mY);
					if (mCurrentX_pop_page >= width - popWindow_page.getWidth()) {
						mCurrentX_pop_page = width - popWindow_page.getWidth();
					}
					if (mCurrentX_pop_page <= 0) {
						mCurrentX_pop_page = 0;
					}
					if (mCurrentY_pop_page <= statusBarHeight) {
						mCurrentY_pop_page = statusBarHeight;
					}
					if (mCurrentY_pop_page >= height- popWindow_page.getHeight()) {
						mCurrentY_pop_page = height- popWindow_page.getHeight();
					}
					try {
						Thread.sleep(100);
						popWindow_page.update(mCurrentX_pop_page,mCurrentY_pop_page, -1, -1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
				}
				return true;
			}
		});
		Button previous_page = (Button) popview_page.findViewById(R.id.previous_page);
		Button next_page = (Button) popview_page.findViewById(R.id.next_page);
		next_page.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
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
		});
		previous_page.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// 判断向上滚动对否已经到网页顶部
				float scrollY = mWebView.getScrollY();
				int y = mWebView.getHeight();
				if (scrollY > 0) {
					if (scrollY > y) {
						mWebView.scrollBy(0,
								(int) (mWebView.getScaleY() - mWebView
										.getHeight()));
					} else {
						mWebView.scrollBy(0,
								(int) (mWebView.getScaleY() - scrollY));
					}
				} else {
					mWebView.scrollBy(0, 0);
				}
			}
		});       
   }
	//显示全屏模式下为显示上下菜单的悬浮按钮
	private void createPopShrinkFullScreen(){
        LayoutInflater mLayoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popview_full_screen=(LinearLayout)mLayoutInflater.inflate(R.layout.shrink_full_screen, null);
		popview_full_screen.setClickable(true);		
		popWindow_full_screen=new PopupWindow(popview_full_screen,width/7,width/7);
		popview_full_screen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getFragmentManager().beginTransaction().show(toolbarFragment).commit();
	            getFragmentManager().beginTransaction().show(topActionbarFragment).commit();
	            popview_full_screen.post(new Runnable() {                   //activity的生命周期函数全部执行完毕,才可以执行popwindow
					   public void run() {
			            		popWindow_full_screen.dismiss();
					 }
				});
				}
			});
		popview_full_screen.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				return false;
			}
		});
       popview_full_screen.setOnTouchListener(new OnTouchListener() {
    	    float mX,mY;
    	    boolean flag;
    	    int downX,upX,downY,upY;
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					downX = (int) event.getX();
					downY = (int) event.getY();
					mX = mCurrentX_pop_full_screen - event.getRawX();
					mY = mCurrentY_pop_full_screen - event.getRawY();

				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					upX = (int) event.getX();
					upY = (int) event.getY();
					mCurrentX_pop_full_screen = (int) (event.getRawX() + mX);
					mCurrentY_pop_full_screen = (int) (event.getRawY() + mY);
					if (mCurrentX_pop_full_screen >= width - width / 7) {
						mCurrentX_pop_full_screen = width - width / 7;
					}
					if (mCurrentX_pop_full_screen <= 0) {
						mCurrentX_pop_full_screen = 0;
					}
					if (mCurrentY_pop_full_screen <= 0) {
						mCurrentY_pop_full_screen = 0;
					}
					if (mCurrentY_pop_full_screen >= height - width / 7) {
						mCurrentY_pop_full_screen = height - width / 7;
					}
					if (Math.abs(upX - downX) > 0) {
						flag = true;
						popview_full_screen.setPressed(false);
					} else {
						flag = false;
					}
					try {
						Thread.sleep(100);

						popWindow_full_screen.update(mCurrentX_pop_full_screen,
								mCurrentY_pop_full_screen, -1, -1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if (!flag)
						popview_full_screen.setPressed(true);
					JBLPreference.getInstance(getActivity()).writeInt(
							JBLPreference.pop_full_currentX_value,
							mCurrentX_pop_full_screen);
					JBLPreference.getInstance(getActivity()).writeInt(
							JBLPreference.pop_full_currentY_value,
							mCurrentY_pop_full_screen);

				}

				return flag;
			}
		});
       popview_full_screen.setEnabled(true);
    }
	//隐藏状态栏
	private void hideStatusBar(){
		WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getActivity().getWindow().setAttributes(lp);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}
	//显示状态栏
	private void showStatusBar(){
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
				getFragmentManager().beginTransaction().remove(MainPageFragment.this).commit();//必须要加 负责saveinstance 会比fragment transition 先调用
				getActivity().finish();//会调用saveinstance
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
			//当进入gridview时判断当前网页地址是不是主页，是非判断结果存到缓存中
			if(mWebView.getCurrentUrl().equals(UrlUtils.URL_GET_HOST)){
				JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.HOST_URL_BOOLEAN, JBLPreference.IS_HOST_URL);
			}else{
				JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.HOST_URL_BOOLEAN, JBLPreference.ISNOT_HOST_URL);
			}
			
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
			if(popWindow_full_screen==null){
				createPopShrinkFullScreen();
			}
			if(url.equals(UrlUtils.URL_GET_HOST)){                //主页：显示上下菜单栏，不显示悬浮按钮
				getFragmentManager().beginTransaction().show(toolbarFragment).show(topActionbarFragment).commit();
				popview_full_screen.post(new Runnable() {                   //activity的生命周期函数全部执行完毕,才可以执行popwindow
					   public void run() {
			            		popWindow_full_screen.dismiss();
					 }
				});
				
			}else{                                              //不是主页：不显示上下菜单栏，显示悬浮按钮
				getFragmentManager().beginTransaction().hide(toolbarFragment).hide(topActionbarFragment).commit();
				popview_full_screen.post(new Runnable() {                   //activity的生命周期函数全部执行完毕,才可以执行popwindow
					   public void run() {
				            popWindow_full_screen.showAtLocation(popview_full_screen, Gravity.NO_GRAVITY, mCurrentX_pop_full_screen, mCurrentY_pop_full_screen);
						   }
						});
			}
		}
		if(JBLPreference.getInstance(this.getActivity()).readInt(BoolType.TURNNING.toString())==JBLPreference.OPEN_TURNING_BUTTON){  //全屏模式
			if(popWindow_page==null){
				createTurningPage();
			}      		
			if(url.equals(UrlUtils.URL_GET_HOST)){                //主页：显示上下菜单栏，不显示悬浮按钮
				if(popWindow_page.isShowing()){
            		popWindow_page.dismiss();
				}
			}else{                                              //不是主页：不显示上下菜单栏，显示悬浮按钮
				popview_page.post(new Runnable() {                   //activity的生命周期函数全部执行完毕,才可以执行popwindow
					   public void run() {
			            	popWindow_page.showAtLocation(popview_page, Gravity.NO_GRAVITY, mCurrentX_pop_page, mCurrentY_pop_page);
					 }
				});
			}
		}
		  //判断是夜间模式需再设置下activity亮度
  		if(JBLPreference.getInstance(getActivity()).readInt(BoolType.BRIGHTNESS_TYPE.toString())==JBLPreference.NIGHT_MODEL){
  			int brightness=JBLPreference.getInstance(getActivity()).readInt(JBLPreference.NIGHT_BRIGHTNESS_VALUS);
  			BrightnessSettings.setBrightness(getActivity(),brightness);
  		}
  		BrowserSettings.getInstance().addObserver(mWebView.getSettings());
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

	}
	
}
