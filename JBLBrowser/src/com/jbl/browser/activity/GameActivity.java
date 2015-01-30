package com.jbl.browser.activity;

import java.net.URLDecoder;

import com.jbl.browser.R;
import com.jbl.browser.model.ErrorInfo;
import com.jbl.browser.model.MusicModel;
import com.jbl.browser.model.ResponseModel;
import com.jbl.browser.model.UserInfo;
import com.jbl.browser.payment.IAppPaySDKConfig;
import com.jbl.browser.tools.BusinessCallback;
import com.jbl.browser.tools.BusinessTool;
import com.jbl.browser.utils.StringUtils;
import com.jbl.browser.utils.UrlUtils;
import com.jbl.browser.view.TitleView;
import com.jbl.browser.view.UserDefinedDialog;
import com.jbl.browser.websocket.WebSocketFactory;
import com.umeng.analytics.MobclickAgent;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class GameActivity extends Activity{

	/**打开登录、注册页;登录、注册完成*/
	public static final int LOGIN_OR_ACOUNT = 100;
	/**分享*/
	private final int SHARE = 1;
	/**微博分享请求*/
	public final static int SHARE_REQUEST = 130;
	
	/**登录mobage成功*/
	private final int LOGIN_DONE = 200;
	/**获取mobage昵称成功*/
	private final int GET_NICK_DONE = 201;
	
	private final String TAG = "GameActivity";
	
	private WebView webView;
	private TitleView titleView;
	private ImageView loading;//加载动画
	private ImageView loadingBg; //加载时背景
	private ImageView btnTop,btnPage,btnCall,btnStory,btnFight,btnRetry;
	private RelativeLayout loadingError;
	private ProgressDialog pd;
	private Dialog dialog;
	
	private Handler myHandler;
	private AnimationDrawable animationDrawable;  //加载动画
	private boolean pauseMp = true; //页面切换时是否暂停播放音乐
	private boolean pause = false;
	/**是否进入过主页面，用于底部按钮的背景歌曲切换*/
	private boolean inTopPage = false; 
	/**游戏页面是否是首次获得焦点*/
	private boolean webGetFocus = true; 
	private boolean getHostDone = false; //成功获取域名
	private String errorUrl = null; //因为网络原因加载出错的url
	private String currtentUrl = null; //当前的url
	//Desmond
	//private TransactionCallBack callBack;
	private SensorManager mSensorManager = null;  
    private Sensor mSensor = null; 
    private boolean isEmulator=true;
	//Desmond
	/**是否正在处理交易*/
	private boolean handlerBuy = false; 
	private boolean playSound = false; //加载页面时是否播放按钮声音
	private boolean showWeb = false; //是否显示了网页
	private int shareType;
	private String iappid;
	
	SensorEventListener lsn = new SensorEventListener() {  
        
        @Override  
        public void onSensorChanged(SensorEvent event) {  
            float x = event.values[SensorManager.DATA_X];  
            float y = event.values[SensorManager.DATA_Y];  
            float z = event.values[SensorManager.DATA_Z]; 

            if(x==0.0f&&y==0.0f&&isEmulator){
               GameActivity.this.finish();
            }else{
            	isEmulator=false;
            }
        }  
          
        @Override  
        public void onAccuracyChanged(Sensor sensor, int accuracy) {  
            // TODO Auto-generated method stub  
              
        }  
    };  
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		@SuppressWarnings("unused")
		Context context = getApplication().getApplicationContext();
		CookieSyncManager.createInstance(this);
		//Desmond
		//Mobage.registerMobageResource(this, "cn.mobage.g13000309.R");
		//Desmond end
	    setContentView(R.layout.activity_game);
	    if(StringUtils.DEBUG){
	    	Log.i("GameActivity", "onCreate");
	    }
	    mSensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);        
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);  
	    if(isEmulator()){
	    	this.finish();
	    	return;
	    }
	    webView = (WebView) findViewById(R.id.game_web);
	    titleView = (TitleView) findViewById(R.id.title_view);
	    loading = (ImageView) findViewById(R.id.loading);
	    loadingBg = (ImageView) findViewById(R.id.loading_bg);
	    loadingError = (RelativeLayout) findViewById(R.id.loading_error);
	    btnTop = (ImageView) findViewById(R.id.tab_top);
	    btnPage = (ImageView) findViewById(R.id.tab_page);
	    btnCall = (ImageView) findViewById(R.id.tab_call);
	    btnStory = (ImageView) findViewById(R.id.tab_story);
	    btnFight = (ImageView) findViewById(R.id.tab_fight);
	    btnRetry = (ImageView) findViewById(R.id.retry);
	    
	    titleView.getBackBtn().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				allowPlaySound();
				goBack();
			}
		});
	    
	    titleView.getCenterImage().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				allowPlaySound();
				webView.reload();
			}
		});
	    
	    titleView.getRightImage().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!inTopPage){
					return;
				}
				allowPlaySound();
				webView.loadUrl(UrlUtils.URL_TOP);
			}
		});
	    
	    titleView.setElseImageOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				allowPlaySound();
				gotoSetMp();
			}
		});
	    
	    btnTop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!inTopPage){
					return;
				}
				try {
					BusinessTool.getInstance().playMusic(GameActivity.this, "anniu.mp3", 
							MusicModel.TYPE_EFFECT, false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				webView.loadUrl("javascript:" + "openPopup('popupMenu')");
			}
		});
	    
	    btnPage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!inTopPage){
					return;
				}
				allowPlaySound();
				webView.loadUrl(UrlUtils.URL_PAGE);
			}
		});
	    
	    btnCall.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!inTopPage){
					return;
				}
				allowPlaySound();
				webView.loadUrl(UrlUtils.URL_CALL);
			}
		});
	    
	    btnStory.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!inTopPage){
					return;
				}
				allowPlaySound();
				webView.loadUrl(UrlUtils.URL_STORY);
			}
		});
	    
	    btnFight.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!inTopPage){
					return;
				}
				allowPlaySound();
				webView.loadUrl(UrlUtils.URL_FIGHT);
			}
		});
	    
	    btnRetry.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				allowPlaySound();
				webView.reload();
			}
		});
	    
	    webView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					allowPlaySound();
					break;

				default:
					break;
				}
				return false;
			}
		});
	    loadingBg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	  //Desmond
	    //initCallBack();
	  //Desmond end
	    initWebView();
	    //Desmond
	    initIAppSDK();
	    //Desmond end
	    getHost();
		
	}
	
	 @Override
	 public void onWindowFocusChanged(boolean hasFocus){
		 
		 if(webGetFocus && hasFocus){ //游戏界面首次获得焦点时加载首页，保证帧动画正常运行
			 startLoading();
			 webGetFocus = false;
		 }
		 else if(hasFocus){  //获得焦点
			 BusinessTool.getInstance().setContext(this);
			 BusinessTool.getInstance().resumeBgPlay();
		 }
	 }

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(StringUtils.DEBUG){
			Log.i("GameActivity", "onStart");
	    }
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		mSensorManager.registerListener(lsn, mSensor, SensorManager.SENSOR_DELAY_GAME);  
		pause = false;
		if(StringUtils.DEBUG){
			Log.i("GameActivity", "onResume");
	    }
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
		mSensorManager.unregisterListener(lsn);  
		pause = true;
		if(StringUtils.DEBUG){
			Log.i("GameActivity", "onPause");
	    }
		if(pauseMp){
			BusinessTool.getInstance().pauseBgMuisc();
			BusinessTool.getInstance().pauseEffect();
		}
		else{
			pauseMp = true;
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(StringUtils.DEBUG){
			Log.i("GameActivity", "onStop");	
	    }
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		//Desmond
		//Mobage.onRestart();
		//Desmond end
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//Desmond
		//Mobage.onStop();
		//Desmond
		if(BusinessTool.getInstance().getUserInfo() != null){
			BusinessTool.getInstance().stopBgMuisc();
			BusinessTool.getInstance().stopEffect();
		}
		BusinessTool.getInstance().clearInstance();
		//Desmond
		//MobageSDKTool.getInstance().clearInstance();
		//Desmond end
		webView.destroy();
		webView = null;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == LOGIN_OR_ACOUNT){
			if(BusinessTool.getInstance().getUserInfo() != null){
				getHost();
			}
			else{
				finish();
			}
		}
		
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK:
				exitOrNot();
//				if(webView.canGoBack()){
//					goBack();
//				}
//				else{
//					exitOrNot();
//				}
//				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
	
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
		 menu.add(1, Menu.FIRST, 1, getString(R.string.exit));
		 return true;
	 }
	 
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
			
		 // TODO Auto-generated method stub
		 switch (item.getItemId()) {
		 case Menu.FIRST:
			 exitOrNot();
			 break;
		 }
		 return super.onOptionsItemSelected(item);
	 }
	 
	 /**允许播放一次声音*/
	 private void allowPlaySound(){
			if(!playSound){
				playSound = true;
			}
	 }
	 
	 private void getHost(){
		 if(StringUtils.DEBUG){
			 Log.i(TAG, "getHost:获取服务器域名");
		 }
		 if(UrlUtils.URL_HEAD == null){
			 BusinessTool.getInstance().getHost(new BusinessCallback() {
				
				@Override
				public void fail(ErrorInfo e) {
					// TODO Auto-generated method stub
					SharedPreferences sharedPreferences =GameActivity.this.getSharedPreferences("nextUrl", 0);
					String url =sharedPreferences.getString("defaultUrl", null);
					if (url != null) {
						UrlUtils.URL_HEAD = url + "/index.php/";
						UrlUtils.URL_HEAD2 = url + "/";
						UrlUtils.URL_REGISTER = UrlUtils.URL_HEAD + UrlUtils.URL_REGISTER;
						UrlUtils.URL_LOGIN = UrlUtils.URL_HEAD + UrlUtils.URL_LOGIN;
						UrlUtils.URL_UPDATENAME = UrlUtils.URL_HEAD + UrlUtils.URL_UPDATENAME;
						UrlUtils.URL_HOME_PAGE = UrlUtils.URL_HEAD + UrlUtils.URL_HOME_PAGE;
						UrlUtils.URL_TOP = UrlUtils.URL_HEAD + UrlUtils.URL_TOP;
						UrlUtils.URL_PAGE = UrlUtils.URL_HEAD + UrlUtils.URL_PAGE;
						UrlUtils.URL_CALL = UrlUtils.URL_HEAD + UrlUtils.URL_CALL;
						UrlUtils.URL_STORY = UrlUtils.URL_HEAD + UrlUtils.URL_STORY;
						UrlUtils.URL_FIGHT = UrlUtils.URL_HEAD + UrlUtils.URL_FIGHT;
						UrlUtils.URL_REWARD_CARD = UrlUtils.URL_HEAD + UrlUtils.URL_REWARD_CARD;
						UrlUtils.URL_REWARD_PT = UrlUtils.URL_HEAD + UrlUtils.URL_REWARD_PT;
						UrlUtils.URL_REWARD_ITEM = UrlUtils.URL_HEAD + UrlUtils.URL_REWARD_ITEM;
						UrlUtils.URL_GET_GEMS = UrlUtils.URL_HEAD + UrlUtils.URL_GET_GEMS;
						UrlUtils.URL_GET_TEMP_TOKEN = UrlUtils.URL_HEAD + UrlUtils.URL_GET_TEMP_TOKEN;
						UrlUtils.URL_GET_TOKEN = UrlUtils.URL_HEAD + UrlUtils.URL_GET_TOKEN;
						UrlUtils.URL_CHECK = UrlUtils.URL_HEAD + UrlUtils.URL_CHECK;
						UrlUtils.URL_TRADE_NO=UrlUtils.URL_HEAD +UrlUtils.URL_TRADE_NO;
						//UrlUtils.URL_PAYMENT_CALLBACK=UrlUtils.URL_HEAD+UrlUtils.URL_PAYMENT_CALLBACK;
						getHostDone = true;
						if(getHostDone){
							getNickName();
						}
					}else {
						dialog = UserDefinedDialog.getInstance().twoBtnDialog(
								GameActivity.this, R.drawable.app_icon2, getString(R.string.retry),
								getString(R.string.exit),
								getString(R.string.app_name), getString(R.string.error_network), 
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										getHost();
									}
								}, new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										finish();
									}
								});
						dialog.setCancelable(false);
						dialog.show();
					}
					
				}
				
				@Override
				public void error(ErrorInfo e) {
					// TODO Auto-generated method stub
					SharedPreferences sharedPreferences =GameActivity.this.getSharedPreferences("nextUrl", 0);
					String url =sharedPreferences.getString("defaultUrl", null);
					if (url != null) {
						UrlUtils.URL_HEAD = url + "/index.php/";
						UrlUtils.URL_HEAD2 = url + "/";
						UrlUtils.URL_REGISTER = UrlUtils.URL_HEAD + UrlUtils.URL_REGISTER;
						UrlUtils.URL_LOGIN = UrlUtils.URL_HEAD + UrlUtils.URL_LOGIN;
						UrlUtils.URL_UPDATENAME = UrlUtils.URL_HEAD + UrlUtils.URL_UPDATENAME;
						UrlUtils.URL_HOME_PAGE = UrlUtils.URL_HEAD + UrlUtils.URL_HOME_PAGE;
						UrlUtils.URL_TOP = UrlUtils.URL_HEAD + UrlUtils.URL_TOP;
						UrlUtils.URL_PAGE = UrlUtils.URL_HEAD + UrlUtils.URL_PAGE;
						UrlUtils.URL_CALL = UrlUtils.URL_HEAD + UrlUtils.URL_CALL;
						UrlUtils.URL_STORY = UrlUtils.URL_HEAD + UrlUtils.URL_STORY;
						UrlUtils.URL_FIGHT = UrlUtils.URL_HEAD + UrlUtils.URL_FIGHT;
						UrlUtils.URL_REWARD_CARD = UrlUtils.URL_HEAD + UrlUtils.URL_REWARD_CARD;
						UrlUtils.URL_REWARD_PT = UrlUtils.URL_HEAD + UrlUtils.URL_REWARD_PT;
						UrlUtils.URL_REWARD_ITEM = UrlUtils.URL_HEAD + UrlUtils.URL_REWARD_ITEM;
						UrlUtils.URL_GET_GEMS = UrlUtils.URL_HEAD + UrlUtils.URL_GET_GEMS;
						UrlUtils.URL_GET_TEMP_TOKEN = UrlUtils.URL_HEAD + UrlUtils.URL_GET_TEMP_TOKEN;
						UrlUtils.URL_GET_TOKEN = UrlUtils.URL_HEAD + UrlUtils.URL_GET_TOKEN;
						UrlUtils.URL_CHECK = UrlUtils.URL_HEAD + UrlUtils.URL_CHECK;
						UrlUtils.URL_TRADE_NO=UrlUtils.URL_HEAD +UrlUtils.URL_TRADE_NO;
						//UrlUtils.URL_PAYMENT_CALLBACK=UrlUtils.URL_HEAD+UrlUtils.URL_PAYMENT_CALLBACK;
						getHostDone = true;
						if(getHostDone){
							getNickName();
						}
					}else {
						dialog = UserDefinedDialog.getInstance().twoBtnDialog(
								GameActivity.this,R.drawable.app_icon2, 
								getString(R.string.retry),getString(R.string.exit),
								getString(R.string.app_name), getString(R.string.error_network), 
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										getHost();
									}
								}, new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										finish();
									}
								});
						dialog.setCancelable(false);
						dialog.show();
					}
					
				}
				
				@Override
				public void complete(Bundle values) {
					// TODO Auto-generated method stub
					SharedPreferences sharedPreferences =GameActivity.this.getSharedPreferences("nextUrl", 0);
					Editor editor =  sharedPreferences.edit();
					String url = values.getString(StringUtils.DATA);
					editor.putString("defaultUrl", url).commit();
					if(StringUtils.IOS){
						url = "http://harlem.instantnow.jp";
					}
					if(StringUtils.DEBUG){
						if(UrlUtils.TEST){
							url = UrlUtils.URL_TEST_HOST;
						}
						Log.i(TAG, "getHost:获取域名成功：" + url);
					}
					if(StringUtils.IOS){
						url = "http://harlem.instantnow.jp";
					}
					UrlUtils.URL_HEAD = url + "/index.php/";
					UrlUtils.URL_HEAD2 = url + "/";
					UrlUtils.URL_REGISTER = UrlUtils.URL_HEAD + UrlUtils.URL_REGISTER;
					UrlUtils.URL_LOGIN = UrlUtils.URL_HEAD + UrlUtils.URL_LOGIN;
					UrlUtils.URL_UPDATENAME = UrlUtils.URL_HEAD + UrlUtils.URL_UPDATENAME;
					UrlUtils.URL_HOME_PAGE = UrlUtils.URL_HEAD + UrlUtils.URL_HOME_PAGE;
					UrlUtils.URL_TOP = UrlUtils.URL_HEAD + UrlUtils.URL_TOP;
					UrlUtils.URL_PAGE = UrlUtils.URL_HEAD + UrlUtils.URL_PAGE;
					UrlUtils.URL_CALL = UrlUtils.URL_HEAD + UrlUtils.URL_CALL;
					UrlUtils.URL_STORY = UrlUtils.URL_HEAD + UrlUtils.URL_STORY;
					UrlUtils.URL_FIGHT = UrlUtils.URL_HEAD + UrlUtils.URL_FIGHT;
					UrlUtils.URL_REWARD_CARD = UrlUtils.URL_HEAD + UrlUtils.URL_REWARD_CARD;
					UrlUtils.URL_REWARD_PT = UrlUtils.URL_HEAD + UrlUtils.URL_REWARD_PT;
					UrlUtils.URL_REWARD_ITEM = UrlUtils.URL_HEAD + UrlUtils.URL_REWARD_ITEM;
					UrlUtils.URL_GET_GEMS = UrlUtils.URL_HEAD + UrlUtils.URL_GET_GEMS;
					UrlUtils.URL_GET_TEMP_TOKEN = UrlUtils.URL_HEAD + UrlUtils.URL_GET_TEMP_TOKEN;
					UrlUtils.URL_GET_TOKEN = UrlUtils.URL_HEAD + UrlUtils.URL_GET_TOKEN;
					UrlUtils.URL_CHECK = UrlUtils.URL_HEAD + UrlUtils.URL_CHECK;
					UrlUtils.URL_TRADE_NO=UrlUtils.URL_HEAD +UrlUtils.URL_TRADE_NO;
					//UrlUtils.URL_PAYMENT_CALLBACK=UrlUtils.URL_HEAD+UrlUtils.URL_PAYMENT_CALLBACK;
					getHostDone = true;
					if(getHostDone){
						getNickName();
					}
				}
			});
		 }
		 else{
			 if(StringUtils.DEBUG){
					Log.i(TAG, "getHost:域名非空：" + UrlUtils.URL_HEAD);
				}
			getHostDone = true;
			if (getHostDone) {
				getNickName();
			}
		 }
	 }
	 
	//Desmond	 
	/** 获取初始化数据,如果有用户数据，且和mobage的id一致，则开始加载游戏界面,反之则注册*/
	private void init() {
		BusinessTool.getInstance().getMpStateData(this);
		BusinessTool.getInstance().getChangeAccount(this);
		BusinessTool.getInstance().getOpenSound(this);
		if (BusinessTool.getInstance().getUserInfo() != null) {
			//webView.loadUrl(UrlUtils.URL_HEAD);
			webView.loadUrl(UrlUtils.URL_HEAD+"app-login-end");
		}
		//Desmond
		else{
			BusinessTool.getInstance().initUser(this);
			if (BusinessTool.getInstance().getUserInfo() == null) {
				startActivity(new Intent(this,LoginActivity.class));
			}else{
				login(BusinessTool.getInstance().getUserInfo().getUsername(), 
						BusinessTool.getInstance().getUserInfo().getPassword());
			}
		}
		//Desmond end
		//Desmond
//		if (BusinessTool.getInstance().getUserInfo() != null) {
//			webView.loadUrl(UrlUtils.URL_HEAD);
//		}
//		try {
//			if (BusinessTool.getInstance().getUserInfo() == null) { //内存中没有保存用户数据
//				checkOrAcount(MobageSDKTool.getInstance().getUid(),nickName);
//			}
//			else{//内存中有保存id,将id与登录id进行对比
//				
//				if(BusinessTool.getInstance().getUserInfo().getUsername().equals
//						(MobageSDKTool.getInstance().getUid())){ //id与保存的id一致，表示没有切换帐号
//					BusinessTool.getInstance().getMpStateData(this);
//					BusinessTool.getInstance().getChangeAccount(this);
//					BusinessTool.getInstance().getOpenSound(this);
//					if (BusinessTool.getInstance().getUserInfo() != null) {
//						webView.loadUrl(UrlUtils.URL_HEAD);
//					}
//				}
//				else{ //id与保存的id不一致，表示有切换帐号,重新注册
//					checkOrAcount(MobageSDKTool.getInstance().getUid(),nickName);
//				}
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//
//		}
		//Desmond end
	}
	//Desmond end
	/**初始化webview*/
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView(){
		myHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
					
				case SHARE:
					share((String) msg.obj);
					break;
				case LOGIN_DONE:
					getNickName();
					break;
				case GET_NICK_DONE:
					init();
					break;

				default:
					break;
				}
			}
			
		};
		webView.setScrollBarStyle(View.GONE);
		webView.setVisibility(View.INVISIBLE);
//		webView.getSettings().setUseWideViewPort(true);
//		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setAppCacheMaxSize(8*1024*1024);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		//webView.getSettings().setPluginsEnabled(true);
		webView.getSettings().setPluginState(PluginState.ON);
		webView.setWebViewClient(new MyWebViewClient());
		webView.setWebChromeClient(new WebChromeClient(){

		});
		webView.addJavascriptInterface(new WebSocketFactory(webView), "WebSocketFactory");
		webView.addJavascriptInterface(new Object(){
			
			/**需要播放声音时调用*/
			@SuppressWarnings("unused")
			public void playSounds(final String data){
				if(StringUtils.DEBUG){
					Log.i(TAG, data);
				}
				try {
					if(data.startsWith("zxx_playsoundS:!!")){ //分享
						try {
							BusinessTool.getInstance().playMusic(GameActivity.this, "anniu.mp3", 
									MusicModel.TYPE_EFFECT, false);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message msg = myHandler.obtainMessage();
						msg.what = SHARE;
						msg.obj = data;
						myHandler.sendMessage(msg);
					}
					else{
						if(pause){
							return;
						}
						BusinessTool.getInstance().playMusic(GameActivity.this, 
								BusinessTool.getInstance().parseMusicData(data));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			/**获取背景音乐*/
			@SuppressWarnings("unused")
			public void getBgMusicName(String name) {
				if(StringUtils.DEBUG){
					Log.i(TAG, name);
				}
				if(name.equals("stop")){
					BusinessTool.getInstance().stopBgMuisc();
					return;
				}
				String musicName = null;
				try {
					if(pause){
						return;
					}
					musicName = name + ".mp3";
					BusinessTool.getInstance().playMusic(GameActivity.this, 
							musicName,MusicModel.TYPE_MUSIC,true);  //播放背景音乐
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}finally{
					musicName = null;
				}
			}
			
		},"networkMethod");
	}
	
	//Desmond
	/**初始化IAppPay*/
	private void initIAppSDK(){
		
	}
    //Desmond end
	
	/**
	 * 保存用户信息到内存
	 * 
	 * @param username 用户名
     * 
     * @param password 用户密码
     * 
	 * */
	private void saveUserInfo(String username,String password){
		UserInfo userInfo = null;
		try {
			userInfo = new UserInfo();
			userInfo.setUsername(username);
			userInfo.setPassword(password);
			BusinessTool.getInstance().setUserInfo(userInfo);  //保存用户信息到内存
			BusinessTool.getInstance().saveUserInfo(this);   //保存用户信息到本地
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}finally{
			userInfo = null;
		}
		
	}
	
	/**
	 * 验证或者注册
	 * 
	 * @param uid mobage的id
	 * 
	 * @param nickName 用户昵称
	 * 
	 * */
//	private void checkOrAcount(final String uid,final String nickName){
////		final String uId= "10841413";
//		final String uId= "11245676";
//		//Desmond
//		//MobageSDKTool.getInstance().setUid(uId);
//		//Desmond end
////		final String uId= uid;
//		BusinessTool.getInstance().signIn(this,uId,nickName,uId,StringUtils.LOGIN_TYPE_ANDROID,
//				new BusinessCallback() {
//					
//					@Override
//					public void fail(ErrorInfo e) {
//						// TODO Auto-generated method stub
//						lOrAFail(uId, e, 0);
//					}
//
//					@Override
//					public void error(ErrorInfo e) {
//						// TODO Auto-generated method stub
//						lOrAError(uId,nickName, e, 0);
//					}
//
//					@Override
//					public void complete(Bundle values) {
//						// TODO Auto-generated method stub
//						lOrADone(uId, nickName,values, 0);
//					}
//					
//				});
//	}
	
	private void tradeNo(final String username){
		BusinessTool.getInstance().tradeNo(this, username, StringUtils.LOGIN_TYPE_ANDROID,
				new BusinessCallback() {
					
					@Override
					public void fail(ErrorInfo e) {

						if(pd.isShowing())
							pd.cancel();
					}
					
					@Override
					public void error(ErrorInfo e) {

						if(pd.isShowing())
							pd.cancel();
					}
					
					@Override
					public void complete(Bundle values) {
						String trade_no = (String) values.getSerializable(StringUtils.DATA);
						int wersid=IAppPaySDKConfig.IAPPPAIR.get(iappid);
						int price=IAppPaySDKConfig.IAPPPAIR.get(iappid+wersid);
						startPay(wersid, price,trade_no);
						handlerBuy = true;
						
					}
				});
	}
	/**
	 * 登录
	 * 
	 * @param username 用户名
	 * 
	 * @param password 用户密码
	 * 
	 * */
	private void login(final String username,final String password){
		if(StringUtils.DEBUG){
			Log.i(TAG, "login,登录游戏");
	    }
		String s = username;
		if(StringUtils.IOS){
			s = "500001527";
		}
//		String js = null;
//		js = "$.post('/index.php/app-login','webtype=android&id=" + username +"&pass="+ password +
//				"&da=fromiphone" + "',function(data){window.networkMethod.loginDone(data)})";
//		webView.loadUrl("javascript:" + js);
//		js = null;
		BusinessTool.getInstance().login(this, username, password,StringUtils.LOGIN_TYPE_ANDROID,
				new BusinessCallback() {
					
					@Override
					public void fail(ErrorInfo e) {
						// TODO Auto-generated method stub
						
					    //Desmond
						//lOrAFail(username, e, 1);
						//Desmond end
					}
					
					@Override
					public void error(ErrorInfo e) {
						// TODO Auto-generated method stub
						//Desmond
						//lOrAError(username,null,e, 1);
						//Desmond end
					}
					
					@Override
					public void complete(Bundle values) {
						// TODO Auto-generated method stub
						//Desmond
						lOrADone(username,password,values, 1);
						BusinessTool.setLoginDone(true);
						//loginDone = true;
						//Desmond end
					}
				});
	}
	
	/**
	 * 注册或登录成功
	 * 
	 * @param uid 用户id
	 * 
	 * @param nickName 用户昵称
	 * 
	 * @param values 成功后返回的数据
	 * 
	 * @param type 类型，0为注册，1为登录
	 * 
	 * */
	private void lOrADone(final String uid,final String nickName,Bundle values,final int type){
		ResponseModel model = (ResponseModel) values.getSerializable(StringUtils.DATA);
		//验证成功
		if(model.getResponseCode() == 200 ||
				model.getResponseMessage().startsWith("用户名已存在")){ 
			if(model.getResponseMessage().startsWith("用户名已存在")){ //唯有注册失败时才会出现
				if(StringUtils.DEBUG){
					Log.i(TAG, "acount:用户名已经存在");
				}
				login(uid, nickName);
			}
			else if(type == 0){ //注册成功
				BusinessTool.setLoginDone(true);
				saveUserInfo(uid,nickName);
				init();
			}
			else if(type == 1){ //登录成功
				BusinessTool.setLoginDone(true);
				saveUserInfo(uid,nickName);
				init();
			}
			else{
				if(type == 0){
					Log.e(TAG, "注册错误：" + model.getResponseMessage());
				}
				else if(type == 1){
					Log.e(TAG, "登录错误：" + model.getResponseMessage());
				}
			}
		}
		//正在维护
		else if(model.getResponseMessage().contains("维护")){
			dialog = UserDefinedDialog.getInstance().confirmDialog(
					GameActivity.this,R.drawable.app_icon2,
					getString(R.string.app_name), model.getResponseMessage(), 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
						}
					});
			dialog.setCancelable(false);
			dialog.show();
		}
		//通信出错
		else{
			if(StringUtils.DEBUG) {
				if(type == 0){
					Log.i(TAG, "Acount error:" + model.getResponseMessage());
				}
				else if(type == 1){
					Log.i(TAG, "login error:" + model.getResponseMessage());
				}
			}
			Dialog dialog = UserDefinedDialog.getInstance().twoBtnDialog(
					GameActivity.this,R.drawable.app_icon2,
					getString(R.string.retry), getString(R.string.exit),
					getString(R.string.app_name), getString(R.string.login_fail), 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if(type == 0){
								//Desmond
								//checkOrAcount(uid,nickName);
								//Desmond
							}
							else if(type == 1){
								login(uid,nickName);
							}
						}
					}, 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
						}
					});
			dialog.setCancelable(false);
			dialog.show();
			//Desmond
			BusinessTool.getInstance().clearUserInfo(this);
			//Desmond end
		}
		model = null;
	}
//	
//	/**
//	 * 登录或者注册失败
//	 * 
//	 * @param uid 用户id
//	 * 
//	 * @param e 错误信息
//	 * 
//	 * @param type 类型，0为注册，1为登录
//	 * 
//	 * */
//	private void lOrAFail(String uid,ErrorInfo e,int type){
//		if(StringUtils.DEBUG){
//			if(type == 0){
//				Log.e(TAG, "account network error:" + e.getMessage());
//			}
//			else if(type == 1){
//				Log.e(TAG, "login network error:" + e.getMessage());
//			}
//		}
//		if(pd != null){
//			if(pd.isShowing()){
//				pd.dismiss();
//			}
//			pd = null;
//		}
//		String message = null;
//		if(e.getErrorCode() == 503){
//			message = getString(R.string.error_ser);
//		}
//		else if(e.getErrorCode() == 404){
//			message = getString(R.string.error_not_foundser);
//		}
//		else{
//			message = getString(R.string.error_network);
//		}
//		dialog = UserDefinedDialog.getInstance().confirmDialog(
//				GameActivity.this,R.drawable.app_icon2,
//				getString(R.string.app_name),message,
//				new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						finish();
//					}
//				});
//		dialog.setCancelable(false);
//		dialog.show();
//	}
//	
//	/**
//	 * 登录或者注册出错
//	 * 
//	 * @param uid 用户id
//	 * 
//	 * @param nickName 用户昵称
//	 * 
//	 * @param e 错误信息
//	 * 
//	 * @param type 类型，0为注册，1为登录
//	 * 
//	 * */
//	private void lOrAError(final String uid,final String nickName,ErrorInfo e,final int type){
//		if(StringUtils.DEBUG){
//			if(type == 0){
//				Log.e(TAG, "account request error:" + e.getMessage());
//			}
//			else if(type == 1){
//				Log.e(TAG, "login request error:" + e.getMessage());
//			}
//		}
//		dialog = UserDefinedDialog.getInstance().twoBtnDialog(
//				GameActivity.this,R.drawable.app_icon2,
//				getString(R.string.retry), getString(R.string.exit),
//				getString(R.string.app_name), getString(R.string.login_error), 
//				new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						if(type == 0){
//							checkOrAcount(uid,nickName);
//						}
//						else if(type == 1){
//							login(uid, uid);
//						}
//					}
//				}, 
//				new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						finish();
//					}
//				});
//		dialog.setCancelable(false);
//		dialog.show();
//	}

	/**
	 * 获得mobage用户昵称
	 * 
	 * @param url
	 * 
	 * */
	private void getNickName(){
// Desmond
//		User.Field[] fields = {User.Field.ID,
//				User.Field.NICKNAME};
//		MobageSDKTool.getInstance().getUser(MobageSDKTool.getInstance().getUid(), fields,
//				new People.OnGetUserComplete() {
//					
//					@Override
//					public void onSuccess(User user) {
//						// TODO Auto-generated method stub
//						if(StringUtils.DEBUG){
//							Log.i(TAG, "getNickName获取用户昵称成功:" + user.getNickname());
//						}
//						nickName = user.getNickname();
//						myHandler.sendEmptyMessage(GET_NICK_DONE);
////						CookieSyncManager.createInstance(GameActivity.this.getApplicationContext());
////						String cookies = CookieManager.getInstance().getCookie(url);
////						if(StringUtils.DEBUG){
////							Log.v(TAG, "Cookie:" + cookies);
////						}
////						if(BusinessTool.getInstance().getUserInfo().getNickName() != null){
////							changeName(BusinessTool.getInstance().getUserInfo().getNickName(),
////									cookies);
////						}
////						else{
////							if(StringUtils.DEBUG){
////								Log.w(TAG, "未能成功获取mobage用户昵称，无法修改用户名");
////							}
////						}
//					}
//					
//					@Override
//					public void onError(Error error) {
//						// TODO Auto-generated method stub
//						if(StringUtils.DEBUG){
//							Log.e(TAG, "getNickName获取用户昵称出错:" + error.getDescription());
//						}
//						getNickName();
//					}
//				});
		myHandler.sendEmptyMessage(GET_NICK_DONE);
	    //Desmond end
	}
	
	/**，显示页面，进入游戏*/
	private void intoPage(){
		if(StringUtils.DEBUG){
			Log.i(TAG, "intoPage:进入游戏");
		}
		if (!BusinessTool.getInstance().isChangeAccount()) {
			propmtChangeAccount();
		}
		else{
			if (BusinessTool.getInstance().isOpenSound()) {
				propmtOpenSound();
			}
		}
		if(currtentUrl.startsWith(UrlUtils.URL_TOP)){  //判断是否完成了新手任务（新手任务完成后，首页必定是top）
			inTopPage = true; //底部按钮可用
		}
	}
	 
	 /**退出确认*/
	 private void exitOrNot(){
		 UserDefinedDialog.getInstance().twoBtnDialog(
				 this,R.drawable.app_icon2,
				 getString(R.string.exit), 
				 getString(R.string.cancel_01),getString(R.string.app_name), 
				 getString(R.string.exit_propmt), 
				 new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
						}
					}, null).show();
	 }
	
	/**webview后退*/
	private void goBack(){
		if (currtentUrl!=null&&currtentUrl.contains("game/quest/progress-animate")) {
			return;
		}
		if(webView.canGoBack()){
			webView.goBack();
		}
	}
	
	/**前往设置声音*/
	private void gotoSetMp(){
		pauseMp = false;
		startActivity(new Intent(this,SetMpStateAct.class));
	}
	
	/**开始加载*/
	private void startLoading(){
		try {
			loading.setVisibility(View.VISIBLE);
			loadingBg.setVisibility(View.VISIBLE);
			if(animationDrawable == null){
				animationDrawable = (AnimationDrawable) loading.getDrawable();
			}
			animationDrawable.start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			finish();
		}
	}
	
	/**加载完成*/
	private void doneLoading(){
		if(animationDrawable != null){
			animationDrawable.stop();
		}
		loading.setVisibility(View.GONE);
		loadingBg.setVisibility(View.GONE);
		loadingError.setVisibility(View.GONE);
	}
	
	/**
	 * 分享
	 * 
	 * @param data 待解析的分享内容
	 * 
	 * */
	private void share(final String data){

		
	}
	
	/**分享到微博*/
	private void shareToWeibo(){
		
	}
	
	/**购买海贼币*/
	private void buy(String url){
		//Desmond
		if(StringUtils.DEBUG){
			Log.i(TAG, "购买url:" + URLDecoder.decode(url));
	    }
		iappid = url.substring(url.indexOf("id=") + 3,url.indexOf("&"));
		if(StringUtils.DEBUG){
			//Log.i(TAG, "购买的商品id:" + pid);
	    }

		tradeNo(BusinessTool.getInstance().getUserInfo().getUsername());
		//webView.loadUrl(UrlUtils.URL_TRADE_NO);

		pd = UserDefinedDialog.getInstance().getProgressDialog(GameActivity.this,
				GameActivity.this.getString(R.string.app_name), 
				GameActivity.this.getString(R.string.wait_buy));
		pd.setCancelable(true);
		pd.show();
	}
	
	private void startPay(int waresid, int price,String exorderno) {
		
	}
	
	//Desmond 
	/**初始化内购验证的回调监听*/
	//Desmond end
	
	/** 弹出切换帐号*/
	private void propmtChangeAccount() {
		UserDefinedDialog.getInstance().twoBtnDialog(
				GameActivity.this,R.drawable.app_icon2,
				getString(R.string.kown),getString(R.string.later),
				getString(R.string.app_name),getString(R.string.first_into),
				new DialogInterface.OnClickListener() {
			        @Override
					public void onClick(DialogInterface dialog,
							int which) {
								// TODO Auto-generated method stub
			        	propmtOpenSound();
			        	BusinessTool.getInstance().saveChangeAccount(
			        			GameActivity.this, true);
			        }
		        }, 
		        new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						propmtOpenSound();
					}
				}).show();
	}
	
	/** 弹出提示用户打开声音*/
	private void propmtOpenSound() {
		UserDefinedDialog.getInstance().twoBtnDialog(
				GameActivity.this,R.drawable.app_icon2,
				getString(R.string.no_propmt),getString(R.string.later),
				getString(R.string.app_name),getString(R.string.open_sound),
				new DialogInterface.OnClickListener() {
			        @Override
					public void onClick(DialogInterface dialog,
							int which) {
								// TODO Auto-generated method stub
			        	BusinessTool.getInstance().saveOpenSound(
			        			GameActivity.this, false);
			        }
		        }, 
		        new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).show();
	}
	public boolean isEmulator() {
	    String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
	    Log.d(TAG+"isEmulator","ANDROID_ID: "+android_id);
	    Log.d(TAG+"isEmulator","Build.PRODUCT: "+Build.PRODUCT);
	    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	    String deviceId=telephonyManager.getDeviceId();
	    boolean inEmulator = TextUtils.isEmpty(android_id) || "google_sdk".equals( Build.PRODUCT ) || "sdk".equals(Build.PRODUCT)
	    		||deviceId == null || deviceId.trim().length() == 0|| deviceId.matches("0+");           
	    return inEmulator;
	}
	
	class MyWebViewClient extends WebViewClient{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			if(url.startsWith("proc:")){
				if(StringUtils.DEBUG){
					Log.i("加载",url);
			    }
				if(!url.startsWith("proc://sound")){
					playSound = false;
					try {
						BusinessTool.getInstance().playMusic(GameActivity.this, "anniu.mp3", 
								MusicModel.TYPE_EFFECT, false);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(url.startsWith("proc://sound")){ //故事声音
				try {
					BusinessTool.getInstance().playMusic(GameActivity.this, 
							BusinessTool.getInstance().parseMusicData(url));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
			else if(url.startsWith("proc://login")){ //加载超时，重新登录
				
				startLoading();
				login(BusinessTool.getInstance().getUserInfo().getUsername(),
						BusinessTool.getInstance().getUserInfo().getPassword());
				return true;
			}
			else if(url.startsWith("proc://buy")){//内部购买
				buy(url);
				return true;
			}
			else if(url.startsWith("proc://rmcache")){ //清空缓存
				if(StringUtils.DEBUG){
					Log.d("禁止加载", "清空缓存");
			    }
				webView.clearCache(true);
				Toast.makeText(GameActivity.this, R.string.delete_done, Toast.LENGTH_SHORT).show();
				return true;
			}
			else if(url.startsWith("proc://car?")){ //新手任务后点击开始冒险
				String newUrl = URLDecoder.decode(url.substring(url.indexOf("url=") + 4));
				inTopPage = true;
				webView.loadUrl(newUrl);
				return true;
			}
			//Desmond
			else if(url.startsWith("proc://googlepushreg?")){
				if(url.indexOf("url=")==-1){
					String newUrl=UrlUtils.URL_HEAD+"?"+url.substring(21);
					//webView.loadUrl(newUrl);
				}else{
					String newUrl = URLDecoder.decode(url.substring(url.indexOf("url=") + 4));
					webView.loadUrl(newUrl);
				}
				return true;
			}
			//Desmond end
			
			return false;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			if(StringUtils.DEBUG){
				Log.i("开始加载", url);
			}
			BusinessTool.getInstance().stopEffect(); //开始加载页面时停止当前音效
			BusinessTool.play = false; //阻止延迟线程中的音乐播放
			if(BusinessTool.isLoginDone() && playSound){
				playSound = false;
				try {
					BusinessTool.getInstance().playMusic(GameActivity.this, "anniu.mp3", 
							MusicModel.TYPE_EFFECT, false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(webView.getVisibility() == WebView.INVISIBLE){
				webView.setVisibility(View.VISIBLE);
			}
			startLoading();
		}
		
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
			if(StringUtils.DEBUG){
				Log.e("加载出错",failingUrl);
		    }
			errorUrl = failingUrl;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			loadingDone(url);
		}
		
		/**页面加载完成*/
		private void loadingDone(String url){
			if(StringUtils.DEBUG){
				Log.i("加载完成", url);
			}
			if(url.equals(UrlUtils.URL_REWARD_CARD) || url.equals(UrlUtils.URL_REWARD_ITEM)
					|| url.equals(UrlUtils.URL_REWARD_PT)){

			}
			currtentUrl = url;
			if(errorUrl != null && errorUrl.equals(url)){  //加载出错
				doneLoading();
				loadingError.setVisibility(View.VISIBLE);
				errorUrl = null;
				return;
			}
			if(BusinessTool.isLoginDone() && !showWeb){ //显示web
				showWeb = true;
				intoPage();
			}
			doneLoading();
			//漏单处理
			if(!handlerBuy){
				
				// Desmond
//				if(MobageSDKTool.getInstance().getTid(GameActivity.this) != null){
//					handlerBuy = true;
//					MobageSDKTool.getInstance().lostTransaction(GameActivity.this,
//							MobageSDKTool.getInstance().getTid(GameActivity.this), callBack);
//				}
				// Desmond end
			}
			//获取背景音乐
			String js = "if(document.getElementById('bgm') != null){window.networkMethod.getBgMusicName(document.getElementById('bgm').title)}";
			webView.loadUrl("javascript:" + js);
		}

	}
}
