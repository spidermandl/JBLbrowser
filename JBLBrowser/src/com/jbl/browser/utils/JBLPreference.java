package com.jbl.browser.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 封装sharedPreference
 * 
 * @author desmond.duan
 * 
 */
public class JBLPreference {
	private static JBLPreference myPrefs;// 私有化
	private SharedPreferences sp;
	private static Context globleContext;// 全局context
	private final static String JBLBROWSER_PREFERENCE = "JBLBROWSER_PREFERENCE";
	
	public static final int INVALID = -1;//无效
	
	/**
	 * 几种webview设置类型
	 */
	public static enum BoolType{
		PIC_CACHE(1), //网页图片缓存模式
		FULL_SCREEN(2), //网页全屏浏览模式
		TURNNING(3), //页面翻转模式
		HISTORY_CACHE(4),//网页无痕浏览模式
	    BRIGHTNESS_TYPE(5);
		// 定义私有变量
		private int nCode;

		// 构造函数，枚举类型只能为私有
		private BoolType(int _nCode) {
			this.nCode = _nCode;
		}

		@Override
		public String toString() {
			return String.valueOf("web_bool_type_"+this.nCode);
		}
	};

	public static final int NO_PICTURE=1;//关闭图片缓存
	public static final int YES_PICTURE=0;//开启图片缓存

	public static final int CLOSE_HISTORY=1;//关闭网页无痕浏览
	public static final int OPEN_HISTORY=0;//开启网页无痕浏览
	
	public static final int NO_FULL=1;//关闭网页全屏浏览
	public static final int YES_FULL=0;//开启网页全屏浏览
	
	public static final int OPEN_TURNING_BUTTON=0;//开启翻页按钮;
	public static final int CLOSE_TURNING_BUTTON=1;//关闭翻页按钮;
	
	public static final int DAY_MODEL=1;//"日间模式";
	public static final int NIGHT_MODEL=0;//"夜间模式";
	public static final String NIGHT_BRIGHTNESS_VALUS="夜间模式亮度值";
	
	public static final String HOST_URL_BOOLEAN="是否为主页";
	public static final int IS_HOST_URL=1;
	public static final int ISNOT_HOST_URL=0;
	//书签和历史记录传网址到主页fragment关键字
	public static final String BOOKMARK_HISTORY_KEY="webAddress";
	public static final String RECOMMEND_KEY="urlAddress";
	public static final String FONT_TYPE="FONT_TYPE";//字体类型
	public static final String SCREEN_TYPE="SCREEN_TYPE";//屏幕类型
	public static final int FONT_MIN= 0;//"小";
	public static final int FONT_MEDIUM= 1;//"中";
	public static final int FONT_MAX= 2;//"大";
	public static final int FOLLOW_SYSTEM= 0;//"跟随系统";
	public static final int LOCK_VERTAICAL= 1;//"锁定竖屏";
	public static final int LOCK_HORZON= 2;//"锁定横屏";
	public static final String DELETE_RECOMMEND="删除推荐";
	public static final String SUCCESS_DELETE="删除成功";	
	public static final String EXTRA_ID_URL = "EXTRA_ID_URL";
	
	public static final String FONT_SIZE="字体大小";
	public static final String SCREEN_INTENSITY="屏幕亮度";
	public static final String MODERATE="适中";
	public static final String ROTARY_SCREEN="旋转屏幕";
	public static final String ABOUT="关于";
	public static final String DEFAULT_BROWSER="默认浏览器";
	public static final String ABOUT_US="关于我们";
	public static final String CLEAR_DATA="清除数据";
	public static final String RESTORE_FACTORY_SETTINGS="恢复出厂设置";
	/*public static final String pop_page_currentX_value="翻页悬浮窗x轴值";
	public static final String pop_page_currentY_value="翻页悬浮窗y轴值";*/
	public static final String pop_full_currentX_value="全屏悬浮窗x轴值";
	public static final String pop_full_currentY_value="全屏悬浮窗Y轴值";
	
	public static final String IS_FIRST_RUN="是否第一次运行";
	public static final int NO_FIRST_RUN=1;
	// 提供私有的构造方法
	private JBLPreference() {
	}
	/**
	 * 对外提供的初始化方法
	 * 
	 * @return
	 */
	public static JBLPreference getInstance(Context ctx) {
		// 初始化自身对象
		if (myPrefs == null) {
			myPrefs = new JBLPreference();
			globleContext = ctx.getApplicationContext();
			myPrefs.initSharedPreferences();
			init();
			
		}
		return myPrefs;
	}
	
	/**
	 * 初始化缓存
	 */
	public static void init(){
		if(myPrefs.readInt(BoolType.FULL_SCREEN.toString())==INVALID)
			myPrefs.writeInt(BoolType.FULL_SCREEN.toString(), NO_FULL);
		if(myPrefs.readInt(BoolType.HISTORY_CACHE.toString())==INVALID)
			myPrefs.writeInt(BoolType.HISTORY_CACHE.toString(), CLOSE_HISTORY);
		if(myPrefs.readInt(BoolType.TURNNING.toString())==INVALID)
			myPrefs.writeInt(BoolType.TURNNING.toString(), CLOSE_TURNING_BUTTON);
		if(myPrefs.readInt(BoolType.PIC_CACHE.toString())==INVALID)
			myPrefs.writeInt(BoolType.PIC_CACHE.toString(), YES_PICTURE);
		if(myPrefs.readInt(BoolType.BRIGHTNESS_TYPE.toString())==INVALID)
			myPrefs.writeInt(BoolType.BRIGHTNESS_TYPE.toString(), DAY_MODEL);
		myPrefs.writeInt(NIGHT_BRIGHTNESS_VALUS, 127);
		myPrefs.writeInt(FONT_TYPE, FONT_MEDIUM);
	}
	/**
	 * 初始化SharedPreferences对象
	 * 
	 * @param context
	 */
	public JBLPreference initSharedPreferences() {
		// 获取SharedPreferences对象
		if (sp == null) {
			sp = globleContext.getSharedPreferences(JBLBROWSER_PREFERENCE,Context.MODE_PRIVATE);
		}
		return myPrefs;
	}

	/**
	 * 向SharedPreferences中写入String类型的数据
	 * 
	 * @param text
	 */
	public void writeString(String key, String value) {
		// 获取编辑器对象
		Editor editor = sp.edit();
		// 写入数据
		editor.putString(key, value);
		editor.commit();// 提交写入的数据
		
	}
	/**
	 * 向SharedPreferences中写入boolean类型的数据
	 * 
	 * @param text
	 */
	public void writeBool(String key, boolean value) {
		// 获取编辑器对象
		Editor editor = sp.edit();
		// 写入数据
		editor.putBoolean(key, value);
		editor.commit();// 提交写入的数据
	}
	
	/**
	 * 向SharedPreferences中写入integer类型的数据
	 * 
	 * @param text
	 */
	public void writeInt(String key, int value) {
		// 获取编辑器对象
		Editor editor = sp.edit();
		// 写入数据
		editor.putInt(key, value);
		editor.commit();// 提交写入的数据
	}

	/**
	 * 根据key读取SharedPreferences中的String类型的数据
	 * 
	 * @param key
	 * @return
	 */
	public String readString(String key) {
		return sp.getString(key, "");
		
	}

	/**
	 * 根据key读取SharedPreferences中的Integer类型的数据
	 * 
	 * @param key
	 * @return
	 */
	public int readInt(String key) {
		return sp.getInt(key, -1);
	}
}
