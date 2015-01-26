package com.jbl.browser.activity;

import java.io.IOException;

import com.jbl.browser.R;
import com.jbl.browser.model.ErrorInfo;
import com.jbl.browser.model.ResponseModel;
import com.jbl.browser.model.UserInfo;
import com.jbl.browser.tools.BusinessCallback;
import com.jbl.browser.tools.BusinessTool;
import com.jbl.browser.utils.StringUtils;
import com.jbl.browser.view.UserDefinedDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends Activity {

	private EditText userName,passWord;
	private ImageView imageViewBg,imageViewBgTop,gotoAcount,login;
	private Bitmap bitmapBg,bitmapBgTop,bitmapLogin,bitmapGoAcount;
	private ProgressDialog pd;
	private DisplayMetrics metrics;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		if(dm.widthPixels >= dm.heightPixels){
			//强制横屏
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		else{
			//强制竖屏模式
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		setContentView(R.layout.activity_login);

		userName = (EditText) findViewById(R.id.username);
		passWord = (EditText) findViewById(R.id.password);
		imageViewBg = (ImageView) findViewById(R.id.mybg);
        imageViewBgTop = (ImageView) findViewById(R.id.mybgtop);
		login = (ImageView) findViewById(R.id.btn_login);
		gotoAcount = (ImageView) findViewById(R.id.go_acount);

		initUI();
		
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login();
			}
		});
        
		gotoAcount.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoAcount();
			}
		});
	}
	
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		destroy();
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    		gotoAcount();
    		return true;
    	}
		return super.onKeyDown(keyCode, event);
	}
	
    /**释放资源*/
    private void destroy(){
    	if(bitmapBg != null){
    		if(!bitmapBg.isRecycled()){
    			bitmapBg.recycle();
    		}
    		bitmapBg = null;
    	}
    	if(bitmapBgTop != null){
    		if(!bitmapBgTop.isRecycled()){
    			bitmapBgTop.recycle();
    		}
    		bitmapBgTop = null;
    	}
    	if(bitmapLogin != null){
    		if(!bitmapLogin.isRecycled()){
    			bitmapLogin.recycle();
    		}
    		bitmapLogin = null;
    	}
    	if(bitmapGoAcount != null){
    		if(!bitmapGoAcount.isRecycled()){
    			bitmapGoAcount.recycle();
    		}
    		bitmapGoAcount = null;
    	}
    }

	/**初始化界面*/
    private void initUI(){
    	if(metrics == null){
    		 metrics = BusinessTool.getInstance().getDefaultDisplay(this);
    	}
    	try {
    		//获得图片资源
        	bitmapBg = BitmapFactory.decodeStream(getAssets().open("login_bg.png"));
        	bitmapBgTop = BitmapFactory.decodeStream(getAssets().open("bg_top.png"));
        	bitmapLogin = BitmapFactory.decodeStream(getAssets().open("login.png"));
        	bitmapGoAcount = BitmapFactory.decodeStream(getAssets().open("go_acount.png"));
        	
        	//缩放处理
        	float scanleH = metrics.heightPixels/(float)bitmapBg.getHeight();
            float scanleW = metrics.widthPixels/(float)bitmapBg.getWidth();
            //if(scanleH < 1 || scanleW < 1){
            	if(scanleH > scanleW){
                	bitmapBg = BusinessTool.getInstance().getZoomByScanlePhoto(bitmapBg, scanleW, scanleW);
                	bitmapBgTop = BusinessTool.getInstance().getZoomByScanlePhoto(bitmapBgTop, scanleW, scanleW);
                	bitmapLogin = BusinessTool.getInstance().getZoomByScanlePhoto(bitmapLogin, scanleW, scanleW);
                	bitmapGoAcount = BusinessTool.getInstance().getZoomByScanlePhoto(bitmapGoAcount, scanleW, scanleW);
                }
                else{
                	bitmapBg = BusinessTool.getInstance().getZoomByScanlePhoto(bitmapBg, scanleH, scanleH);
                	bitmapBgTop = BusinessTool.getInstance().getZoomByScanlePhoto(bitmapBgTop, scanleH, scanleH);
                	bitmapLogin = BusinessTool.getInstance().getZoomByScanlePhoto(bitmapLogin, scanleH, scanleH);
                	bitmapGoAcount = BusinessTool.getInstance().getZoomByScanlePhoto(bitmapGoAcount, scanleH, scanleH);
                }
           // }
            imageViewBg.setImageBitmap(bitmapBg);
            imageViewBgTop.setImageBitmap(bitmapBgTop);
            login.setImageBitmap(bitmapLogin);
            gotoAcount.setImageBitmap(bitmapGoAcount);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	/**前往登录*/
	private void login(){
		if(userName.getText() == null || passWord.getText() == null){
			return;
		}
		pd = UserDefinedDialog.getInstance().getProgressDialog(this, getString(R.string.app_name), 
				getString(R.string.wait_login));
		pd.setCancelable(true);
		pd.show();
		BusinessTool.getInstance().login(this,userName.getText().toString(), 
				passWord.getText().toString(), StringUtils.LOGIN_TYPE_ANDROID,
				new BusinessCallback() {
					
					@Override
					public void error(ErrorInfo e) {
						// TODO Auto-generated method stub
						//登录出错的处理
						if(StringUtils.DEBUG){
							Log.e("LoginActivity", "login request error:" + e.getMessage());
						}
						if(pd != null){
							if(pd.isShowing()){
								pd.dismiss();
							}
							pd = null;
						}
						UserDefinedDialog.getInstance().confirmDialog(
								LoginActivity.this,R.drawable.app_icon2,
								getString(R.string.app_name), e.getMessage(), null).show();
					}
					
					@Override
					public void complete(Bundle values) {
						// TODO Auto-generated method stub
						ResponseModel model = (ResponseModel) values.getSerializable(StringUtils.DATA);
						if(model.getResponseCode() == 200){
							saveUserInfo(userName.getText().toString(),passWord.getText().toString());
							BusinessTool.setLoginDone(true);
							intoGame();
						}
						else{
							if(StringUtils.DEBUG){
								Log.i("login request error", "login error:" + model.getResponseMessage());
							}
							UserDefinedDialog.getInstance().confirmDialog(
									LoginActivity.this,R.drawable.app_icon2,
									getString(R.string.app_name), model.getResponseMessage(), null).show();
						}
						model = null;
						if(pd != null){
							if(pd.isShowing()){
								pd.dismiss();
							}
							pd = null;
						}
					}
					
					@Override
					public void fail(ErrorInfo e) {
						// TODO Auto-generated method stub
						if(StringUtils.DEBUG){
							Log.e("LoginActivity", "network error:" + e.getMessage());
						}
						if(pd != null){
							if(pd.isShowing()){
								pd.dismiss();
							}
							pd = null;
						}
						UserDefinedDialog.getInstance().confirmDialog(
								LoginActivity.this,R.drawable.app_icon2,
								getString(R.string.app_name),getString(R.string.error_network), null).show();
					}
				});
	}
	
	/**前往注册*/
	private void gotoAcount(){
		startActivity(new Intent(this,MainActivity.class));
		finish();
	}
	
	/**
	 * 保存用户信息
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
     * 开始游戏
     * 
     * 
     * */
	private void intoGame(){
		//Desmond
		startActivity(new Intent(this,GameActivity.class));
		//Desmond end
		finish();
		
	}
	
}
