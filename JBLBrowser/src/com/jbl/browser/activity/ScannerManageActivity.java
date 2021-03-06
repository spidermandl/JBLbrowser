package com.jbl.browser.activity;


import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.ClipboardManager;
import android.widget.Toast;
import cn.hugo.android.scanner.CaptureActivity;
import cn.hugo.android.scanner.R;
import cn.hugo.android.scanner.common.BitmapUtils;
import cn.hugo.android.scanner.decode.BitmapDecoder;

import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;
import com.jbl.browser.utils.JBLPreference;
public class ScannerManageActivity extends CaptureActivity{

	 private static final int PARSE_BARCODE_FAIL = 300;
	 private static final int PARSE_BARCODE_SUC = 200;
	 private static final int REQUEST_CODE = 100;
	 private Result lastResult;
	 /**
	     * 图片的路径
	     */

	 private String photoPath;
	 private Handler mHandle=new MyHandleSon(this);
	 class MyHandleSon extends Handler{
		 private WeakReference<Activity> activityReference;
	     public MyHandleSon(Activity activity) {
	         activityReference = new WeakReference<Activity>(activity);
	     }
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
            case PARSE_BARCODE_SUC: // 解析图片成功
            	ScannerManageActivity.this.showResult(msg.obj.toString());
                break;
            case PARSE_BARCODE_FAIL:// 解析图片失败
                Toast.makeText(ScannerManageActivity.this, "解析图片失败",
                        Toast.LENGTH_SHORT).show();
                break;
            default: 
                break;
        }
	}
 }
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
            final ProgressDialog progressDialog;
            switch (requestCode) {
                case REQUEST_CODE:

                    // 获取选中图片的路径
                    Cursor cursor = getContentResolver().query(
                            intent.getData(), null, null, null, null);
                    if (cursor.moveToFirst()) {
                        photoPath = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                    cursor.close();

                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("正在扫描...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    new Thread(new Runnable() {

                        @Override
                        public void run() {

                            Bitmap img = BitmapUtils
                                    .getCompressedBitmap(photoPath);

                            BitmapDecoder decoder = new BitmapDecoder(
                                    ScannerManageActivity.this);
                            Result result = decoder.getRawResult(img);

                            if (result != null) {
                                Message m = mHandle.obtainMessage();
                                m.what = PARSE_BARCODE_SUC;
                                m.obj = ResultParser.parseResult(result)
                                        .toString();
                                mHandle.sendMessage(m);
                            } else {
                                Message m = mHandle.obtainMessage();
                                m.what = PARSE_BARCODE_FAIL;
                                mHandle.sendMessage(m);
                            }

                            progressDialog.dismiss();

                        }
                    }).start();

                    break;

            }
        }
	}
	@Override
	public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
		// TODO Auto-generated method stub
		super.handleDecode(rawResult, barcode, scaleFactor);
        showResult(ResultParser.parseResult(rawResult).toString());
	}
	 public  void showResult(final String result){
		 String strPattern="[a-zA-z]+://[^\\s]*";
	  	 Pattern p = Pattern  
	       .compile(strPattern);  
	  	 Matcher m = p.matcher(result.trim());   
	  	 if(m.matches()){
	  		JBLPreference.getInstance(this).writeString(JBLPreference.BOOKMARK_HISTORY_KEY, result);
	  		finish();
	  		Intent intent=new Intent();
		    intent.setClass(ScannerManageActivity.this, MainFragActivity.class);
		    startActivity(intent);
	  	 }else{
	  		 Dialog dialog=new AlertDialog.Builder(this)
	  			.setTitle(R.string.result)
	  			.setMessage(result)
	  			.setPositiveButton(R.string.copy, new DialogInterface.OnClickListener() {
	  				@SuppressLint("NewApi")
						@SuppressWarnings("deprecation")
						@Override
	  				public void onClick(DialogInterface dialog, int which) {
	  					ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE); 
	  					cmb.setText(result.trim());  
	  					finish();
	  				}
	  			}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){

	  				@Override
	  				public void onClick(DialogInterface dialog, int which) {
	  					// TODO Auto-generated method stub
	  					restartPreviewAfterDelay(0L);
	  				}			
	  			})
	  			.create();
	  		 dialog.show();
	  	 }
	 }
	
}
