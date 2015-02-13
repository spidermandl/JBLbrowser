package com.jbl.browser.view;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

import com.jbl.browser.R;

public class UserDefinedDialog {

	private static UserDefinedDialog dialogDefine;
	
	private UserDefinedDialog() {
		// TODO Auto-generated constructor stub
	}
	
	public static UserDefinedDialog getInstance() {
		if(dialogDefine == null){
			dialogDefine = new  UserDefinedDialog();
		}
		return dialogDefine;
	}
	
	/** 
     * 获取带有两个按钮并含有icon的弹出框，按钮内容、标题、内容、按钮监听器需要自己指定
     * 
     * @param context 上下文
     * 
     * @param iconId 图片的id
     * 
     * @param btnText01 第一个按钮的内容
     * 
     * @param btnText02 第二个按钮的内容
     * 
     * @param title 弹出框标题
     *
     * @param message  弹出框内容
     * 
     * @param listener01  第一个按钮的监听器
     * 
     * @param listener02 第二个按钮的监听器
     */
	public Dialog twoBtnDialog(Context context,int iconId,String btnText01,String btnText02,
			String title,String message,OnClickListener listener01,OnClickListener listener02) {
		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setIcon(iconId);
		builder.setPositiveButton(btnText01, listener01);
		builder.setNegativeButton(btnText02, listener02);
		return builder.create();
	}
	
	/** 
     * 获取带有三个按钮的弹出框，按钮内容、标题、内容、按钮监听器需要自己指定，第三个按钮为‘取消’，不需要监听器
     * 
     * @param context 上下文
     * 
     * @param 图片id
     * 
     * @param btnText01 第一个按钮的内容
     * 
     * @param btnText02 第二个按钮的内容
     * 
     * @param btnText03 第三个按钮的内容
     * 
     * @param title 弹出框标题
     *
     * @param message  弹出框内容
     * 
     * @param listener01  第一个按钮的监听器
     * 
     * @param listener02 第二个按钮的监听器
     */
	public Dialog threeBtnDialog(Context context,int iconId,String btnText01,String btnText02,String btnText03,
			String title,String message,OnClickListener listener01,OnClickListener listener02) {
		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setIcon(iconId);
		builder.setMessage(message);
		builder.setPositiveButton(btnText01, listener01);
		builder.setNeutralButton(btnText02, listener02);
		builder.setNegativeButton(btnText03, null);
		return builder.create();
	}
	
	
	/** 
     * 获取带有‘确定’与‘取消’的弹出框，标题、内容、确定监听器需要自己指定
     * 
     * @param context 弹出框的父组件
     * 
     * @param 图片id
     * 
     * @param title 弹出框标题
     *
     * @param message  弹出框内容
     * 
     * @param listener  确定按钮的监听器
     */
	public Dialog chooseDialog(Context context,int iconId,String title,String message,OnClickListener listener) {
		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setIcon(iconId);
		builder.setMessage(message);
		builder.setPositiveButton(context.getResources().getString(R.string.confirm), listener);
		builder.setNegativeButton(context.getResources().getString(R.string.cancel), null);
		return builder.create();
	}
	
	/** 
     * 获取带有‘确定’的弹出框，标题、内容、确定监听器需要自己指定
     * 
     * @param 图片id
     * 
     * @param context 弹出框的父组件
     * 
     * @param title 弹出框标题
     *
     * @param message  弹出框内容
     * 
     * @param listener  重试按钮的监听器
     */
	public Dialog confirmDialog(Context context,int iconId,String title,String message,OnClickListener listener) {
		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setIcon(iconId);
		builder.setMessage(message);
		builder.setPositiveButton(context.getResources().getString(R.string.confirm), listener);
		return builder.create();
	}
	
	/** 
     * 获取带有自定义布局的弹出框
     * 
     * @param context 弹出框的父组件
     * 
     * @param title 弹出框标题
     *
     * @param view  弹出框的自定义布局
     * 
     */
	public Dialog defineViewDialog(Context context,String title,View view) {
		Builder builder = new Builder(context);
		if(title != null){
			builder.setTitle(title);
		}
		builder.setView(view);
		return builder.create();
	}
	
	/** 
     * 获取进度弹出框
     * 
     * @param context 弹出框的父组件
     * 
     * @param title 弹出框标题
     *
     * @param message  弹出框内容
     * 
     */
	public ProgressDialog getProgressDialog(Context context,String title,String message) {
		ProgressDialog pb = new ProgressDialog(context);
		pb.setTitle(title);
		pb.setMessage(message);
		pb.setCancelable(true);
		return pb;
	}
}
