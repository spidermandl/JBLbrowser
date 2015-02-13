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
	
	public static final String PIC_CACHE_TYPE="PIC_CACHE_TYPE";//网页图片缓存模式
	public static final int NO_PICTURE=1;
	public static final int YES_PICTURE=0;
	public static final String OPEN_NO_PICTURE="开启无图模式";
	public static final String OPEN_YES_PICTURE="开启有图模式";
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
	public static final String BRIGHTNESS_TYPE="BRIGHTNESS";//日间 夜间 模式
	public static final int DAY_MODEL=0;//"日间模式";
	public static final int NIGHT_MODEL=1;//"夜间模式";
	public static final String EXTRA_ID_URL = "EXTRA_ID_URL";
	public static final String TURNING_TYPE="TURNING_TYPE";//页面翻转模式
	public static final int OPEN_TURNING_BUTTON=1;//"开启翻页按钮";
	public static final int COLSE_TURNING_BUTTON=0;//"关闭翻页按钮";
	public static final String FONT_SIZE="字体大小";
	public static final String SCREEN_INTENSITY="屏幕亮度";
	public static final String MODERATE="适中";
	public static final String ROTARY_SCREEN="旋转屏幕";
	public static final String ABOUT="关于";
	
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
		}
		return myPrefs;
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
