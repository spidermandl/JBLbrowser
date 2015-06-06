/**
 * Copyright (c) 2013 An Yaming,  All Rights Reserved
 */
package com.jbl.browser.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.jbl.browser.R;
import com.jbl.browser.utils.UrlUtils;

/**
 * @ClassName: InitActivity
 * @Description: TODO(初始化)
 * @author tanghuan
 * @date 2014年12月3日 下午7:42:28
 * 
 */
public class InitActivity extends Activity {
	private final String TAG = InitActivity.class.getSimpleName();
/*
	// 全局
	private MyApplication application;
	// 版本升级的工具
	private VersionUpdateUtil util;
	// private RunMITUtil runMITUtil;
	// 内存卡管理的帮助类
	private SdcardHelper sdHelper;*/


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		goMainActivity();
	/*	try {
			this.unregisterReceiver(goShoppingCartBroadcastReceiver);
		} catch (Exception e) {
		}
		application = (MyApplication) getApplication();*/

		// 异步图片的加载初始化 有物理缓存
		// AsyImgConfig.init(true, 6, Confing.imgCache);
		// 没有物理缓存
		/*AsyImgConfig.init(true, 6);

		util = VersionUpdateUtil.init(this);
		// runMITUtil = RunMITUtil.init();

		sdHelper = new SdcardHelper();
		if (!sdHelper.ExistSDCard()) {
			Toast.makeText(InitActivity.this, R.string.nosdcard,
					Toast.LENGTH_SHORT).show();
		}*/

		// MyTimerTask task = new MyTimerTask();
		// // 延时3.5秒跳转页面
		// Timer timer = new Timer();
		// timer.schedule(task, 3500);
		
		/*
		 * 沉睡1秒
		 */
		// RunMITStaticQueue queue = new RunMITStaticQueue();
		// queue.setCls(Thread.class);
		// queue.setMethodName("sleep");
		// queue.setParms(new Object[] { 1000 });
		// queue.setCallBack(new IRunMITCallBack() {
		//
		// @Override
		// public void onRuned(RunMITQueue queue) {
		// }
		// });
		// runMITUtil.runQueue(queue);
	}

	// 重新onkeydown拦截返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	

	/**
	 * 检测版本
	 *//*
	private void upversion() {
		VersionUpdateUtil util = VersionUpdateUtil.init(this);
		util.doSelectVersion(GetDataConfing.ip,
				GetDataConfing.Action_selectAppVersionInfo, null, false,
				new IVersionSelectedCallBack() {

					@Override
					public void netError(String arg0) {
						goMainActivity();
					}

					@Override
					public boolean isUpdate(String serviceInfo,
							String versionName, int versionCode) {
						String infoStr = serviceInfo;
						if (null != infoStr) {
							if (infoStr.startsWith("<?xml")) {
								infoStr = infoStr.substring(infoStr
										.indexOf(">") + 1);
								infoStr = infoStr.trim();
								if (infoStr.startsWith("<string")) {
									infoStr = infoStr.substring(infoStr
											.indexOf(">") + 1);
									infoStr = infoStr.trim();
									if (infoStr.endsWith("</string>")) {
										infoStr = infoStr.substring(0,
												infoStr.length() - 9);
									}
								}
							}
						}
						serviceInfo = infoStr;
						if (ShowGetDataError.isCodeIsNot1(InitActivity.this,
								serviceInfo)) {
							List<JsonMap<String, Object>> infoData = JsonParseHelper
									.getJsonMap_List_JsonMap(serviceInfo,
											JsonKeys.Key_Info);
							if (infoData.size() == 0) {
								goMainActivity();

							} else {
								JsonMap<String, Object> info = infoData.get(0);
								if (info.getStringNoNull("VersionNo").equals(
										versionName)
										|| "".equals(info
												.getStringNoNull("VersionNo"))
										|| "".equals(info
												.getStringNoNull("VersionPath"))) {
									goMainActivity();
								} else {
									showNewVersion(info, versionName,
											versionCode);
								}
							}

						} else {
							goMainActivity();
						}
						return false;
					}
				});

	}
*/
	/**
	 * 提示用户有新版本
	 *//*
	private void showNewVersion(final JsonMap<String, Object> info,
			String versionName, int versionCode) {
		// 给用户提示框
		Builder builder = new Builder(InitActivity.this);
		builder.setTitle(R.string.app_version_select);
		builder.setMessage(info.getStringNoNull("Description"));
		builder.setPositiveButton(R.string.app_version_update,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String appName = getString(R.string.app_name);
						util.doUpdateVersion(
								info.getStringNoNull("VersionPath"),
								Confing.productPath + appName + ".apk",
								appName, R.drawable.icon, true);
						goMainActivity();
					}
				});
		builder.setNegativeButton(R.string.app_version_iknow,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						goMainActivity();
					}
				});
		builder.show();
	}
*/
	/**
	 * 离开本界面到主界面去
	 */
	private void goMainActivity() {

		SharedPreferences sp = getSharedPreferences(UrlUtils.SP_SaveUserInfo,
				Context.MODE_APPEND);
		Log.i(TAG, "sp=" + sp.getBoolean(UrlUtils.SP_SaveUserInfo_Second, false));
		if (sp.getBoolean(UrlUtils.SP_SaveUserInfo_Second, false)) {
			Intent intent = new Intent(this, MainFragActivity.class);
			sp.edit().putBoolean(UrlUtils.SP_SaveUserInfo_Second, true).commit();
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, NavigationActivity.class);
			startActivity(intent);
		}
		this.finish();
	}
}
