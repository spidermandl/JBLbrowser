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
