/**
 * Copyright (c) 2013 An Yaming,  All Rights Reserved
 */
package com.jbl.browser.activity;

import org.json.JSONException;
import org.json.JSONObject;

import orm.sqlite.db.UserDao;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.jbl.browser.R;
import com.jbl.browser.bean.UserInfo;
import com.jbl.browser.db.UserInfoDao;
import com.jbl.browser.model.ErrorInfo;
import com.jbl.browser.tools.BusinessCallback;
import com.jbl.browser.tools.BusinessTool;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.StringUtils;
import com.mozillaonline.providers.DownloadManager;
import com.mozillaonline.providers.DownloadManager.Request;

/**
 * 起始页面 版本检查
 * 
 * @author Desmond
 * 
 */
public class HomePageActivity extends BaseFragActivity {
	
	private final String TAG = HomePageActivity.class.getSimpleName();
	
	DownloadManager mDownloadManager;
	private ImageView frontPage;

	Handler m_mainHandler;
	ProgressDialog m_progressDlg;

	@Override
	protected void onCreate(Bundle arg0) {  
		setContentView(R.layout.activity_init);
		mDownloadManager = new DownloadManager(getContentResolver(),getPackageName());
		
//		UserInfo user=new UserInfo();
//		user.setDeviceID(BusinessTool.getDeviceID(this));
//		user.setPhoneID("13585871125");
//		new UserInfoDao(this).userApproved(user);
		
		BusinessTool.getInstance().versionCheck(new BusinessCallback() {
			
			@Override
			public void fail(ErrorInfo e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void error(ErrorInfo e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void complete(Bundle values) {
				try {
					JSONObject jsonObj=new JSONObject(values.getString(StringUtils.DATA));
					jsonObj=jsonObj.getJSONObject("update");
					PackageInfo pinfo = HomePageActivity.this.getPackageManager().getPackageInfo(
							HomePageActivity.this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
					if(!jsonObj.getString("version").equals(pinfo.versionName)){
						/**
						 * 版本有更新
						 */
						final String url= jsonObj.getString("url");
						new AlertDialog.Builder(HomePageActivity.this)
						.setTitle("检测到最新版本，是否下载？")
						.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {

								Intent intent = new Intent();
								intent.setClass(HomePageActivity.this, DownloadManageActivity.class);
								startActivity(intent);
								Uri srcUri = Uri.parse(url);
								DownloadManager.Request request = new Request(srcUri);
								request.setDestinationInExternalPublicDir(
										Environment.DIRECTORY_DOWNLOADS, "/");
								mDownloadManager.enqueue(request);
								HomePageActivity.this.finish();
								
							}
						})
						.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								HomePageActivity.this.finish();
							}
						})
						.show();
					}else{
						/**
						 * 进入应用
						 */
						if (JBLPreference.getInstance(HomePageActivity.this).readBool(JBLPreference.NOT_FIRST_RUN)) {
							Intent intent = new Intent(HomePageActivity.this,MainFragActivity.class);
							startActivity(intent);
						} else {
							Intent intent = new Intent(HomePageActivity.this,NavigationActivity.class);
							startActivity(intent);
						}
						HomePageActivity.this.finish();
						
					}
				} catch (JSONException e) {
					Toast.makeText(HomePageActivity.this, "请检查网络", 1000).show();
					e.printStackTrace();
				}catch (NameNotFoundException e) {
					Toast.makeText(HomePageActivity.this, "报名错误", 1000).show();
					e.printStackTrace();
				}
				
			}
		});
		super.onCreate(arg0);
	}


}
