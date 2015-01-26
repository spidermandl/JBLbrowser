package com.jbl.browser.tools;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.jbl.browser.model.ErrorInfo;
import com.jbl.browser.model.MusicModel;
import com.jbl.browser.model.ResponseModel;
import com.jbl.browser.model.UserInfo;
import com.jbl.browser.utils.StringUtils;
import com.jbl.browser.utils.UrlUtils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;

public class BusinessTool {

	private static BusinessTool businessTool;
	private final String TAG = "BuinessTool";
	
	/**请求成功*/
	public final static int COMPLETE = 0;
	/**请求错误*/
	public final static int ERROR = -1;
	/**网络错误*/
	public final static int FAIL = 1;
	
	private ErrorInfo errorInfo;
	private BusinessCallback callback;
	private Handler myHandler;
	private Bundle values;
	
	private Context context; //默认上下文
	private MediaPlayer effectMp;  //用于播放各种音效
	private MediaPlayer bgMp;  //用于播放各种音乐
	private boolean openBgMp = true;  //是否开启背景音乐
	private boolean openEffectMp = true;  //是否开启音效
	private boolean changeAccount = false;//是否已经绑定或者切换帐号
	private boolean openSound = true;  //是否提示用户打开声音
	private MusicModel currentBgMusic; //当前的背景音乐
	private MusicModel previousBgMusic; //前一首背景音乐
	private MusicModel currentEffectMusic; //当前的音效
	private UserInfo userInfo;  //用户信息
	private static boolean loginDone = false;//是否登录完成
	public static boolean play = true; //延迟播放延迟时间到时，是否播放音乐
	private String macAddress;

	
	private String versionCode="1.4.3";

	/**获取默认上下文*/
	public Context getContext() {
		return context;
	}

	/**设置默认上下文*/
	public void setContext(Context context) {
		this.context = context;
	}
	
	/**获取背景音乐开关*/
	public boolean isOpenBgMp() {
		return openBgMp;
	}

	/**设置背景音乐开关*/
	private void setOpenBgMp(boolean openBgMp) {
		this.openBgMp = openBgMp;
		if(!openBgMp){
			stopBgMuisc();
		}
		else{
			try {
				playMusic(null, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**获取音效开关*/
	public boolean isOpenEffectMp() {
		return openEffectMp;
	}

	/**设置音效开关*/
	private void setOpenEffectMp(boolean openEffectMp) {
		this.openEffectMp = openEffectMp;
		if(!openEffectMp){
			stopEffect();
		}
	}
	
	/**获取是否已经切换帐号*/
	public boolean isChangeAccount() {
		return changeAccount;
	}

	/**设置是否已经切换帐号*/
	private void setChangeAccount(boolean changeAccount) {
		this.changeAccount = changeAccount;
	}
	
	/**是否提示打开声音*/
	public boolean isOpenSound() {
		return openSound;
	}

	/**设置是否提示打开声音*/
	private void setOpenSound(boolean openSound) {
		this.openSound = openSound;
	}

	/**从内存获取用户信息*/
    public UserInfo getUserInfo() {
		return userInfo;
	}
    
    /**设置用户信息到内存*/
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	private BusinessTool() {
		// TODO Auto-generated constructor stub
		HttpTool.getInstance();
		myHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				try {
					switch (msg.what) {
					
					case COMPLETE:
						callback.complete(values);
						break;
						
					case ERROR:
						callback.error(errorInfo);
						break;
						
					case FAIL:
						callback.fail(errorInfo);
						break;

					default:
						break;
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					
				}finally{
					callback = null;
					values = null;
					errorInfo = null;
				}
			}
			
		};
	}
	
	/**
	 * 获取业务工具类的实例
	 * 
	 * */
	public static BusinessTool getInstance() {
		if(businessTool == null){
			businessTool = new BusinessTool();
		}
		return businessTool;
	}

	/**销毁业务操作实例*/
	public void clearInstance(){
		if(bgMp != null){
			bgMp.release();
			bgMp = null;
		}
		if(effectMp != null){
			effectMp.release();
			effectMp = null;
		}
		HttpTool.destroy();
		businessTool = null;
	}
	
	/**获取手机分辨率数据*/
	public DisplayMetrics getDefaultDisplay(Activity activity){
		DisplayMetrics metrics = null;
		try {
			metrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			return metrics;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
			
		}finally {
			metrics = null;
		}
	}
	
    /** 
     * 根据原始图片和缩放比，在主线程中获指定大小的缩放图片
     * 
     * @param bitmap 原始图片
     * 
     * @param scanleWidth 图片的宽度缩放比
     * 
     * @param scanleHeight 图片的高度缩放比
     */
    public Bitmap getZoomByScanlePhoto(Bitmap bitmap,float scanleWidth,float scanleHeight){
 		 Matrix matrix = null;
         try {
        	 //原始图片的像素
        	 int mHeight = bitmap.getHeight();
             int mWidth = bitmap.getWidth();
             matrix = new Matrix();
             matrix.postScale(scanleWidth,scanleHeight);
             return Bitmap.createBitmap(bitmap,0,0,mWidth,mHeight,matrix,false);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
			
		}finally{
			matrix = null;
		}
	}
    
	public String getMac(Context context) {
		if(macAddress == null){
			WifiManager wifiMgr = null;
			WifiInfo info = null;
			try {
				wifiMgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
				info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
				if (null != info) {
					macAddress = info.getMacAddress();
					macAddress = macAddress.replaceAll(":","-");
				}
				return macAddress;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				
			}finally{
				wifiMgr = null;
				info = null;
			}
		}
		else{
			return macAddress;
		}
		return "";
	}
    
    /**从本地文件中初始化用户信息，并保存到内存，如果文件中无用户数据，则返回到内存的用户信息为空
     * @deprecated
     * */
    public void initUser(Context context){
    	if(context != null){
    		this.context = context;
    	}
		SharedPreferences sp = null;
		String username = null;
		String password = null;
		try {
			sp = this.context.getSharedPreferences(StringUtils.CONFIG_INFO, Context.MODE_PRIVATE);
			username = sp.getString(StringUtils.USERNAME,null);
			password = sp.getString(StringUtils.PASSWORD,null);
			if(username != null && password != null){
				userInfo = new UserInfo();
				userInfo.setUsername(username);
				userInfo.setPassword(password);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}finally{
			sp = null;
			username = null;
			password = null;
		}
    }
    
    /**保存内存中的用户信息到本地
     * @deprecated
     * */
    public void saveUserInfo(Context context) {
    	if(userInfo == null){
    		return;
    	}
    	if(context != null){
    		this.context = context;
    	}
		SharedPreferences sp = null;
		Editor editor = null;
		try {
			sp = this.context.getSharedPreferences(StringUtils.CONFIG_INFO, Context.MODE_PRIVATE);
			editor = sp.edit();
			editor.putString(StringUtils.USERNAME, userInfo.getUsername());
			editor.putString(StringUtils.PASSWORD, userInfo.getPassword());
			editor.commit();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}finally{
			sp = null;
			editor = null;
		}
	}
    
    /**
     * 清除本地用户信息
     * @param context
     */
    public void clearUserInfo(Context context){
    	SharedPreferences sp = null;
		Editor editor = null;
		try {
			sp = this.context.getSharedPreferences(StringUtils.CONFIG_INFO, Context.MODE_PRIVATE);
			editor = sp.edit();
			editor.putString(StringUtils.USERNAME, null);
			editor.putString(StringUtils.PASSWORD, null);
			editor.commit();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}finally{
			sp = null;
			editor = null;
		}
    }
    
	/**
	 * 获取音乐、音效开关的状态信息
	 * 
	 * @param context 上下文
	 * 
	 * */
	public void getMpStateData(Context context){
		SharedPreferences sp = null;
		try {
			sp = context.getSharedPreferences(StringUtils.CONFIG_INFO, Context.MODE_PRIVATE);
			openBgMp = sp.getBoolean(StringUtils.MUSIC_STATE, true);
			openEffectMp = sp.getBoolean(StringUtils.EFFECT_STATE, true);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}finally{
			sp = null;
		}
	}
	
	/**
	 * 获取是否切换了帐号
	 * 
	 * @param context 上下文
	 * 
	 * */
	public void getChangeAccount(Context context){
		SharedPreferences sp = null;
		try {
			sp = context.getSharedPreferences(StringUtils.CONFIG_INFO, Context.MODE_PRIVATE);
			changeAccount = sp.getBoolean(StringUtils.CHANGE_ACCOUNT,false);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}finally{
			sp = null;
		}
	}
	
	/**
	 * 获取是否弹出打开声音提示
	 * 
	 * @param context 上下文
	 * 
	 * */
	public void getOpenSound(Context context){
		SharedPreferences sp = null;
		try {
			sp = context.getSharedPreferences(StringUtils.CONFIG_INFO, Context.MODE_PRIVATE);
			openSound = sp.getBoolean(StringUtils.OPEN_SOUND,true);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}finally{
			sp = null;
		}
	}
	
	/**
	 * 保存背景音乐的开关
	 * 
	 * @param context 上下文
	 * 
	 * @param openBgMp 是否开启背景音乐
	 * 
	 * */
	public void setBgMpState(Context context,boolean openBgMp) throws Exception{
		SharedPreferences sp = null;
		Editor editor = null;
		try {
			sp = context.getSharedPreferences(StringUtils.CONFIG_INFO, Context.MODE_PRIVATE);
			editor = sp.edit();
			editor.putBoolean(StringUtils.MUSIC_STATE, openBgMp);
			editor.commit();
			this.setOpenBgMp(openBgMp);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
			
		}finally{
			sp = null;
			editor = null;
		}
	}
	
	/**
	 * 保存音效的开关
	 * 
	 * @param context 上下文
	 * 
	 * @param openBgMp 是否开启背景音乐
	 * 
	 * */
	public void setEffectMpState(Context context,boolean openEffectMp) throws Exception{
		SharedPreferences sp = null;
		Editor editor = null;
		try {
			sp = context.getSharedPreferences(StringUtils.CONFIG_INFO, Context.MODE_PRIVATE);
			editor = sp.edit();
			editor.putBoolean(StringUtils.EFFECT_STATE, openEffectMp);
			editor.commit();
			this.setOpenEffectMp(openEffectMp);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
			
		}finally{
			sp = null;
			editor = null;
		}
	}
	
	/**
	 * 保存是否切换了帐号
	 * 
	 * @param context 上下文
	 * 
	 * @param  changeAccount 是否保存是否切换了帐号
	 * 
	 * */
	public void saveChangeAccount(Context context,boolean changeAccount){
		SharedPreferences sp = null;
		Editor editor = null;
		try {
			sp = context.getSharedPreferences(StringUtils.CONFIG_INFO, Context.MODE_PRIVATE);
			editor = sp.edit();
			editor.putBoolean(StringUtils.CHANGE_ACCOUNT, changeAccount);
			editor.commit();
			this.setChangeAccount(changeAccount);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}finally{
			sp = null;
			editor = null;
		}
	}
	
	/**
	 * 保存是否提示打开声音
	 * 
	 * @param context 上下文
	 * 
	 * @param openSound 是否提示打开声音
	 * 
	 * */
	public void saveOpenSound(Context context,boolean openSound){
		SharedPreferences sp = null;
		Editor editor = null;
		try {
			sp = context.getSharedPreferences(StringUtils.CONFIG_INFO, Context.MODE_PRIVATE);
			editor = sp.edit();
			editor.putBoolean(StringUtils.OPEN_SOUND, openSound);
			editor.commit();
			this.setOpenSound(openSound);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}finally{
			sp = null;
			editor = null;
		}
	}
	/**退出游戏时，保存当前的背景音乐的音乐名，用于下次进入游戏时播放
	 * @deprecated
	 * */
	public void saveMusic(){
		if(currentBgMusic == null){
			return;
		}
		SharedPreferences sp = null;
		Editor editor = null;
		try {
			sp = context.getSharedPreferences(StringUtils.CONFIG_INFO, Context.MODE_PRIVATE);
			editor = sp.edit();
			editor.putString(StringUtils.MUSIC_NAME, currentBgMusic.getMusicName());
			editor.commit();
			this.setOpenEffectMp(openEffectMp);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}finally{
			sp = null;
			editor = null;
		}
	}
	
	/**获取上次退出时的背景音乐的名称，用于下次进入游戏时播放
	 * @deprecated
	 * */
	public String getMusic() throws Exception{
		SharedPreferences sp = null;
		try {
			sp = context.getSharedPreferences(StringUtils.CONFIG_INFO, Context.MODE_PRIVATE);
			return sp.getString(StringUtils.MUSIC_NAME, null);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
			
		}finally{
			sp = null;
		}
	}
	
	/**
	 * 不延迟播放音乐
	 * 
	 * @param context 上下文
	 * 
	 * @param musicName 音乐文件名
	 * 
	 * @param musicType 是音乐还是音效
	 * 
	 * @param loop 是否循环播放
	 * 
	 * */
	public void playMusic(Context context,String musicName,int musicType,boolean loop) throws Exception{
		MusicModel musicModel = new MusicModel();
		musicModel.setDelayTime(0);
		musicModel.setMusicType(musicType);
		if(loop){
			musicModel.setMusicLoopState(MusicModel.LOOP_TRUE);
		}
		else{
			musicModel.setMusicLoopState(MusicModel.LOOP_FALSE);
		}
		musicModel.setMusicName(musicName);
		playMusic(context, musicModel);
		musicModel = null;
	}
	
	/**
	 * 播放音乐(如果musicModel为空，则播放当前背景音乐)
	 * 
	 * @param context 上下文
	 * 
	 * @param musicModel 待播放的音乐文件
	 * 
	 * */
	public void playMusic(final Context context,final MusicModel musicModel) throws Exception{
		BusinessTool.play = false; //阻止延迟线程中的音乐播放
		if(musicModel == null){
			playBgMusic(context, currentBgMusic);
			return;
		}
		if(musicModel.getDelayTime() == 0 || musicModel.getMusicName0() != null){  //不延迟播放
			if(musicModel.getMusicType() == MusicModel.TYPE_MUSIC){
				playBgMusic(context, musicModel);
			}
			else if(musicModel.getMusicType() == MusicModel.TYPE_EFFECT){
				playEffect(context, musicModel);
			}
			else{
				throw new Exception("未知类型的音乐文件");
			}
		}
		else{ //延迟播放
			play = true;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(musicModel.getDelayTime());
						if(play){
							if(musicModel.getMusicType() == MusicModel.TYPE_MUSIC){
								playBgMusic(context, musicModel);
							}
							else if(musicModel.getMusicType() == MusicModel.TYPE_EFFECT){
								playEffect(context, musicModel);
							}
							else{
								throw new Exception("未知类型的音乐文件");
							}
						}
						
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
	
	/**
	 * 播放背景音乐
	 * 
	 * @param context 上下文
	 * 
	 * @param musicModel 待播放的音乐文件
	 * 
	 * */
	private void playBgMusic(final Context context,final MusicModel musicModel) throws Exception{
		if(currentBgMusic != null && musicModel.getMusicName().equals(currentBgMusic.getMusicName())
				&& bgMp != null && bgMp.isPlaying()){ //和当前背景音乐相同，不重复播放
			return;
		}
		if(currentBgMusic != null){  //保存上一首背景音乐
			previousBgMusic = currentBgMusic;
		}
		currentBgMusic = musicModel;
		if(!isOpenBgMp()){
			return;
		}
		if(context != null){
			this.context = context;
		}
		
		AssetFileDescriptor afd = null;
		try {
			if(musicModel.getMusicName().startsWith("story")){ //故事对话
				afd = this.context.getAssets().openFd(StringUtils.MUSIC_STORY_PATH + musicModel.getMusicName());
			}
			else{
				afd = this.context.getAssets().openFd(StringUtils.MUSIC_PATH + musicModel.getMusicName());
			}
			if(bgMp == null){
				bgMp = new MediaPlayer();
			}
			bgMp.reset();
			bgMp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(), afd.getLength());
			if(musicModel.getMusicLoopState() == MusicModel.LOOP_TRUE){
				bgMp.setLooping(true);
			}
			else if(musicModel.getMusicLoopState() == MusicModel.LOOP_FALSE){
				bgMp.setLooping(false);
			}
			else{
				bgMp.setLooping(false);
			}
			bgMp.prepare();
			bgMp.start();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
			
		}finally{
			if(afd != null){
				try {
					afd.close();
					afd = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw e;
				}
			}
		}
	}
	
	/**暂停播放背景音乐*/
	public void pauseBgMuisc(){
		if(bgMp != null && bgMp.isPlaying()){
			bgMp.pause();
		}
	}
	
	/**继续播放背景音乐*/
	public void resumeBgPlay(){
		if(!isOpenBgMp()){
			return;
		}
		if(bgMp != null && !bgMp.isPlaying() && openBgMp){
			bgMp.start();
		}
	}
	
	/**停止播放背景音乐*/
	public void stopBgMuisc(){
		try {
			if(bgMp != null){
				bgMp.stop();
				bgMp.release();
				bgMp = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * 播放音效
	 * 
	 * @param context 上下文
	 * 
	 * @param musicModel 待播放的音乐文件
	 * 
	 * */
	private void playEffect(final Context context,final MusicModel musicModel) throws Exception{
		if(!isOpenEffectMp()){
			return;
		}
		currentEffectMusic = musicModel;
		if(context != null){
			this.context = context;
		}
		AssetFileDescriptor afd = null;
		try {
			if(currentEffectMusic.isPauseBg()){
				stopBgMuisc();
			}
			if(currentEffectMusic.getMusicName0() != null){
				afd = this.context.getAssets().openFd(StringUtils.MUSIC_PATH + 
						currentEffectMusic.getMusicName0());
			}
			else{
				if(currentEffectMusic.getMusicName().startsWith("story")){ //故事对话
					afd = this.context.getAssets().openFd(StringUtils.MUSIC_STORY_PATH + 
							currentEffectMusic.getMusicName());
				}
				else{
					afd = this.context.getAssets().openFd(StringUtils.MUSIC_PATH + 
							currentEffectMusic.getMusicName());
				}
			}
			if(effectMp == null){
				effectMp = new MediaPlayer();
				effectMp.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
						if(currentEffectMusic.getMusicName0() != null && 
								currentEffectMusic.getMusicName() != null){
							currentEffectMusic.setMusicName0(null);
							try {
								playMusic(context, currentEffectMusic);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});
			}
			effectMp.reset();
			effectMp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(), afd.getLength());
			if(currentEffectMusic.getMusicLoopState() == MusicModel.LOOP_TRUE){
				effectMp.setLooping(true);
			}
			else if(currentEffectMusic.getMusicLoopState() == MusicModel.LOOP_FALSE){
				effectMp.setLooping(false);
			}
			else{
				effectMp.setLooping(false);
			}
			effectMp.prepare();
			effectMp.start();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
			
		}finally{
			if(afd != null){
				try {
					afd.close();
					afd = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw e;
				}
			}
		}
	}
	
	/**暂停播放音效*/
	public void pauseEffect(){
		try {
			if(effectMp != null && effectMp.isPlaying()){
				effectMp.pause();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**继续播放音效*/
	public void resumeEffectPlay(){
		if(!isOpenEffectMp()){
			return;
		}
		if(effectMp != null && !effectMp.isPlaying() && openEffectMp){
			effectMp.start();
		}
	}
	
	/**停止播放音效，释放资源*/
	public void stopEffect(){
		try {
			if(effectMp != null){
				effectMp.stop();
				effectMp.release();
				effectMp = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过解析data,得到音乐数据
	 * 
	 * @param data 待解析的data字符串
	 * 
	 * */
	public MusicModel parseMusicData(String data) throws Exception{
		MusicModel musicModel = null;
		String musicName = null;
		String musicName0 = null;
		String delayTime = null;
		String dataString = data;
		String subString = null;
		try {
			musicModel = new MusicModel();
			if(dataString.startsWith("proc:")){
				dataString = dataString.substring(dataString.indexOf("?") + 1,dataString.length());
				if(dataString.contains("file=")){//参数中有file音效
					subString = dataString.substring(dataString.indexOf("file="),dataString.length());
					if(subString.contains("&")){
						musicName = subString.substring(5,subString.indexOf("&")) + ".mp3";
					}
					else{
						musicName = subString.substring(5,subString.length()) + ".mp3";
					}
					if(StringUtils.DEBUG){
						Log.d(TAG, "音乐名称:" + musicName);
					}
					musicModel.setMusicName(musicName);
				}
				else if(dataString.contains("se=")){ //参数中没有file音效，只有se音效
					subString = dataString.substring(dataString.indexOf("se="),dataString.length());
					if(subString.contains("&")){
						musicName = subString.substring(3,subString.indexOf("&")) + ".mp3";
					}
					else{
						musicName = subString.substring(3,subString.length()) + ".mp3";
					}
					if(StringUtils.DEBUG){
						Log.d(TAG, "音乐名称:" + musicName);
					}
					musicModel.setMusicName(musicName);
				}
				if(dataString.contains("se=") && dataString.contains("file=")){  //参数用户中有se音效，同时有file音效
					subString = dataString.substring(dataString.indexOf("se="),dataString.length());
					if(subString.contains("&")){
						musicName0 = subString.substring(3,subString.indexOf("&")) + ".mp3";
					}
					else{
						musicName0 = subString.substring(3,subString.length()) + ".mp3";
					}
					if(StringUtils.DEBUG){
						Log.d(TAG, "音乐0名称:" + musicName0);
					}
					musicModel.setMusicName0(musicName0);
				}
				if(dataString.contains("sleep=")){ //参数中存在延迟时间
					subString = dataString.substring(dataString.indexOf("sleep="),dataString.length());
					if(subString.contains("&")){
						delayTime = subString.substring(6,subString.indexOf("&"));
					}
					else{
						delayTime = subString.substring(6,subString.length());
					}
					if(StringUtils.DEBUG){
						Log.d(TAG, "延迟播放时间:" + delayTime);
					}
					musicModel.setDelayTime(Integer.valueOf(delayTime));
				}
				if(dataString.contains("bgm=pause")){ //暂停背景音乐
					musicModel.setPauseBg(true);
				}
			}
			else if(dataString.contains("zxx_playsound")){
				musicName = dataString.substring(dataString.indexOf("!!") + 2,dataString.length()) + ".mp3";
				if(musicName.startsWith("stop")){ //表示该data对应的名称数据是停止当前背景，播放上一首背景
					musicName = previousBgMusic.getMusicName();
				}
				if(StringUtils.DEBUG){
					Log.d(TAG, "musicName:" + musicName);
				}
				musicModel.setMusicName(musicName);
				musicModel.setDelayTime(0);
			}
			//判断是背景还是音效
			if(musicName.equals("bgsound.mp3") || musicName.equals("taskbgsound.mp3")
					|| musicName.equals("battle.mp3")){
				musicModel.setMusicType(MusicModel.TYPE_MUSIC);
				musicModel.setMusicLoopState(MusicModel.LOOP_TRUE);
			}
			else{
				musicModel.setMusicType(MusicModel.TYPE_EFFECT);
				musicModel.setMusicLoopState(MusicModel.LOOP_FALSE);
			}
			return musicModel;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
			
		}finally{
			musicModel = null;
			musicName = null;
		}
	}
	
	/**
	 * 获取服务器域名
	 * */
	public void getHost(final BusinessCallback callback){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String response = null;
				HttpGet httpGet = null;
				HttpResponse httpResponse = null;
				try {
					httpGet = new HttpGet(UrlUtils.URL_GET_HOST);
					httpResponse = new DefaultHttpClient().execute(httpGet);
					int statusCode = httpResponse.getStatusLine().getStatusCode();
					if(StringUtils.DEBUG){
						Log.d(TAG, "getHost:返回代码：" + statusCode);
					}
					BusinessTool.this.callback = callback;
					if(statusCode == 200){
						response = EntityUtils.toString(httpResponse.getEntity(),HTTP.UTF_8);
						if(StringUtils.DEBUG){
							Log.d(TAG, "getHost:" + response);
						}
						values = new Bundle();
						values.putString(StringUtils.DATA, response);
						myHandler.sendEmptyMessage(COMPLETE);
					}
					else{
						myHandler.sendEmptyMessage(FAIL);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					BusinessTool.this.callback = callback;
					errorInfo = new ErrorInfo(e);
					myHandler.sendEmptyMessage(ERROR);
				}
			}
		}).start();
	}
	
	/**
	 * 登录游戏
	 * 
	 * @param context 上下文
	 * 
	 * @param username 用户名
	 * 
	 * @param password 密码
	 * 
	 * @param type 登入类型
	 * 
	 * @param callback 登录请求回调
	 * 
	 * */
	public void login(final Context context,final String username,final String password,final String type,
			final BusinessCallback callback){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<NameValuePair> params = null;
				String response = null;
				ResponseModel responseModel = null;
				try {
					params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("webtype",type));
					params.add(new BasicNameValuePair("id", username));
					params.add(new BasicNameValuePair("pass", password));
					params.add(new BasicNameValuePair("da", "fromiphone"));
					params.add(new BasicNameValuePair("mac", getMac(context)));
					params.add(new BasicNameValuePair("version", versionCode));
					if(StringUtils.DEBUG){
						Log.v(TAG, "login webtype:" + type);
						Log.v(TAG, "login id:" + username);
						Log.v(TAG, "login mac:" + getMac(context));
					}
					response = HttpTool.getInstance().postData(context,UrlUtils.URL_LOGIN, params, HTTP.UTF_8);
				    responseModel = new ResponseModel(response);
				    BusinessTool.this.callback = callback;
					values = new Bundle();
					values.putSerializable(StringUtils.DATA, responseModel);
					myHandler.sendEmptyMessage(COMPLETE);
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					BusinessTool.this.callback = callback;
					errorInfo = new ErrorInfo(e);
					if(e.getClass().getName().equals(errorInfo.getClass().getName())){ //是处理网络请求时抛出的异常
						if(((ErrorInfo)e).getErrorCode() == -1){  //网络错误(未打开网络连接)
							myHandler.sendEmptyMessage(FAIL);
						}
						else{
							myHandler.sendEmptyMessage(ERROR);
						}
					}
					else{
						myHandler.sendEmptyMessage(ERROR);
					}
					
				}finally{
					if(params != null){
						params.clear();
						params = null;
					}
					response = null;
				}
			}
		}).start();
	}
	
	/**
	 * 新用户注册
	 * 
	 * @param context 上下文
	 * 
	 * @param username 用户名
	 * 
	 * @param nickname 昵称
	 * 
	 * @param password 密码
	 * 
	 * @param type 登入类型
	 * 
	 * @param callback 注册请求回调
	 * 
	 * */
	public void signIn(final Context context,final String username,final String nickname,
			final String password,final String type,
			final BusinessCallback callback){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<NameValuePair> params = null;
				String response = null;
				ResponseModel responseModel = null;
				try {
					params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("webtype",type));
					params.add(new BasicNameValuePair("id", username));
					params.add(new BasicNameValuePair("pass", password));
					params.add(new BasicNameValuePair("nickname", nickname));
					params.add(new BasicNameValuePair("da", "fromiphone"));
					params.add(new BasicNameValuePair("mac", getMac(context)));
					if(StringUtils.DEBUG){
						Log.v(TAG, "signIn webtype:" + type);
						Log.v(TAG, "signIn id:" + username);
						Log.v(TAG, "signIn nickName:" + nickname);
						Log.v(TAG, "signIn mac:" + getMac(context));
					}
					response = HttpTool.getInstance().postData(context,UrlUtils.URL_REGISTER, params, HTTP.UTF_8);
					responseModel = new ResponseModel(response);
					BusinessTool.this.callback = callback;
					values = new Bundle();
					values.putSerializable(StringUtils.DATA, responseModel);
					myHandler.sendEmptyMessage(COMPLETE);
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					BusinessTool.this.callback = callback;
					errorInfo = new ErrorInfo(e);
					if(e.getClass().getName().equals(errorInfo.getClass().getName())){ //是处理网络请求时抛出的异常
						if(((ErrorInfo)e).getErrorCode() == -1){  //网络错误(未打开网络连接)
							myHandler.sendEmptyMessage(FAIL);
						}
						else{
							myHandler.sendEmptyMessage(ERROR);
						}
					}
					else{
						myHandler.sendEmptyMessage(ERROR);
					}
					
				}finally{
					if(params != null){
						params.clear();
						params = null;
					}
					response = null;
					responseModel = null;
				}
			}
		}).start();
	}
	
	/**
	 * 请求交易号
	 * @param context
	 * @param username  用户名
	 * @param callback  请求交易号回调
	 */
	public void tradeNo(final Context context,final String username,final String type,
			final BusinessCallback callback){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<NameValuePair> params = null;
				String response = null;
				ResponseModel responseModel = null;
				try {
					params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("webtype",type));
					params.add(new BasicNameValuePair("username", username));
					params.add(new BasicNameValuePair("da", "fromiphone"));
					params.add(new BasicNameValuePair("mac", getMac(context)));
					if(StringUtils.DEBUG){
						Log.v(TAG, "login id:" + username);
						Log.v(TAG, "login mac:" + getMac(context));
					}
					response = HttpTool.getInstance().postData(context,UrlUtils.URL_TRADE_NO, params, HTTP.UTF_8);
				    BusinessTool.this.callback = callback;
					values = new Bundle();
					values.putSerializable(StringUtils.DATA, response);
					myHandler.sendEmptyMessage(COMPLETE);
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					BusinessTool.this.callback = callback;
					errorInfo = new ErrorInfo(e);
					if(e.getClass().getName().equals(errorInfo.getClass().getName())){ //是处理网络请求时抛出的异常
						if(((ErrorInfo)e).getErrorCode() == -1){  //网络错误(未打开网络连接)
							myHandler.sendEmptyMessage(FAIL);
						}
						else{
							myHandler.sendEmptyMessage(ERROR);
						}
					}
					else{
						myHandler.sendEmptyMessage(ERROR);
					}
					
				}finally{
					if(params != null){
						params.clear();
						params = null;
					}
					response = null;
				}
			}
		}).start();
	}
	
	
	/**
	 * 修改用户名称
	 * 
	 * @param newName 新的用户名称
	 * 
	 * */
	public void changeName(final Context context,final String newName,final String cookies,
			final BusinessCallback callback){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<NameValuePair> params = null;
				String response = null;
				DefaultHttpClient httpClient = null;
				HttpResponse httpResponse = null;
				HttpPost httpPost = null;
				try {
					httpPost = new HttpPost(UrlUtils.URL_UPDATENAME);
					httpPost.setHeader("Cookie",cookies);
					httpClient = new DefaultHttpClient();
					params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("str", newName));
					httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
					httpResponse = httpClient.execute(httpPost);
					BusinessTool.this.callback = callback;
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						response = EntityUtils.toString(httpResponse.getEntity(),HTTP.UTF_8);
						if(StringUtils.DEBUG){
							Log.v(TAG, "changeName请求结果：" + response);
						}
						values = new Bundle();
						values.putSerializable(StringUtils.DATA, response);
						myHandler.sendEmptyMessage(COMPLETE);
					}
					else{
						errorInfo = new ErrorInfo("链接网络失败");
						myHandler.sendEmptyMessage(FAIL);
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					BusinessTool.this.callback = callback;
					errorInfo = new ErrorInfo(e);
					myHandler.sendEmptyMessage(ERROR);
					
				}finally{
					if(params != null){
						params.clear();
						params = null;
					}
					response = null;
				}
			}
		}).start();
	}

	public static boolean isLoginDone() {
		return loginDone;
	}

	public static void setLoginDone(boolean loginDone) {
		BusinessTool.loginDone = loginDone;
	}
	
}
