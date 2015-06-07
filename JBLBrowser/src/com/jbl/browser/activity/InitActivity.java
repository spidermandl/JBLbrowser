/**
 * Copyright (c) 2013 An Yaming,  All Rights Reserved
 */
package com.jbl.browser.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import com.jbl.browser.Common;
import com.jbl.browser.R;
import com.jbl.browser.utils.UrlUtils;

/**
 * @ClassName: InitActivity
 * @Description: TODO(初始化)
 * 
 */
public class InitActivity extends Activity {
	private final String TAG = InitActivity.class.getSimpleName();
	private ImageView startpage;
	long m_newVerCode; // 最新版的版本号
	String m_newVerName; // 最新版的版本名
	String m_appNameStr; // 下载到本地要给这个APP命的名字

	Handler m_mainHandler;
	ProgressDialog m_progressDlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		 //初始化相关变量  
        initVariable();
        new checkNewestVersionAsyncTask().execute();  

	}
	 private void initVariable()  
	    {  
	        
	        m_progressDlg =  new ProgressDialog(this);  
	        m_progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
	         // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确       
	        m_progressDlg.setIndeterminate(false);      
	        m_appNameStr = "JBLbrowser.apk";  
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

	/*
	 * 离开本界面到主界面去
	 */
	private void goMainActivity() {

		SharedPreferences sp = getSharedPreferences(UrlUtils.SP_SaveUserInfo,
				Context.MODE_APPEND);
		Log.i(TAG,
				"sp=" + sp.getBoolean(UrlUtils.SP_SaveUserInfo_Second, false));
		if (sp.getBoolean(UrlUtils.SP_SaveUserInfo_Second, false)) {
			Intent intent = new Intent(this, MainFragActivity.class);
			sp.edit().putBoolean(UrlUtils.SP_SaveUserInfo_Second, true)
					.commit();
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, NavigationActivity.class);
			startActivity(intent);
		}
		this.finish();
	}

	class checkNewestVersionAsyncTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if (postCheckNewestVersionCommand2Server()) {
				int vercode = Common.getVerCode(getApplicationContext()); // 用到前面第一节写的方法
				if (m_newVerCode > vercode) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}
		
		      
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if (result) {// 如果有最新版本
				doNewVersionUpdate(); // 更新新版本
			} else {
				notNewVersionDlgShow(); // 提示当前为最新版本
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}

	/**
	 * 从服务器获取当前最新版本号，如果成功返回TURE，如果失败，返回FALSE
	 * 
	 * @return
	 */
	private Boolean postCheckNewestVersionCommand2Server() {
		StringBuilder builder = new StringBuilder();
		JSONArray jsonArray = null;
		try {
			// 构造POST方法的{name:value} 参数对
			List<NameValuePair> vps = new ArrayList<NameValuePair>();
			// 将参数传入post方法中
			vps.add(new BasicNameValuePair("action", "checkNewestVersion"));
			builder = Common.post_to_server(vps);
			jsonArray = new JSONArray(builder.toString());
			if (jsonArray.length() > 0) {
				
					m_newVerName = jsonArray.getJSONObject(0).getString(
							"name");
					m_newVerCode = jsonArray.getJSONObject(0)
							.getLong("version");

					return true;
				
			}

			return false;
		} catch (Exception e) {
			//Log.e("msg", e.getMessage());
			m_newVerName = "";
			m_newVerCode = -1;
			return false;
		}
	}

	/**
	 * 提示更新新版本
	 */
	private void doNewVersionUpdate() {
		int verCode = Common.getVerCode(getApplicationContext());
		String verName = Common.getVerName(getApplicationContext());

		String str = "当前版本：" + verName + " Code:" + verCode + " ,发现新版本："
				+ m_newVerName + " Code:" + m_newVerCode + " ,是否更新？";
		Dialog dialog = new AlertDialog.Builder(this)
				.setTitle("软件更新")
				.setMessage(str)
				// 设置内容
				.setPositiveButton("更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								m_progressDlg.setTitle("正在下载");
								m_progressDlg.setMessage("请稍候...");
								downFile(Common.UPDATESOFTADDRESS); // 开始下载
							}
						})
				.setNegativeButton("暂不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 点击"取消"按钮之后退出程序
								goMainActivity();
							}
						}).create();// 创建
		// 显示对话框
		dialog.show();
	}

	/**
	 * 提示当前为最新版本
	 */
	private void notNewVersionDlgShow() {
		int verCode = Common.getVerCode(this);
		String verName = Common.getVerName(this);
		String str = "当前版本:" + verName + " Code:" + verCode + ",/n已是最新版,无需更新!";
		Dialog dialog = new AlertDialog.Builder(this).setTitle("软件更新")
				.setMessage(str)// 设置内容
				.setPositiveButton("确定",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								goMainActivity();
							}
						}).create();// 创建
		// 显示对话框
		dialog.show();
	}

	private void downFile(final String url) {
		m_progressDlg.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();

					m_progressDlg.setMax((int) length);// 设置进度条的最大值

					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								m_appNameStr);
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0) {
								m_progressDlg.setProgress(count);
							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void down() {
		m_mainHandler.post(new Runnable() {
			public void run() {
				m_progressDlg.cancel();
				update();
			}
		});
	}

	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), m_appNameStr)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

}
