package com.zixun.piratesfantasy.view;

import cn.mobage.g13000309.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TitleView extends LinearLayout{

	private Button homeImage,elseImage,centerImage,backImage,rightImage;
	private TextView title_text;
	private LinearLayout title,image_title;
	
	public TitleView(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context,attrs);
		View view = LayoutInflater.from(context).inflate(R.layout.title_view, this,true);
		homeImage = (Button) view.findViewById(R.id.home);
		elseImage = (Button) view.findViewById(R.id.else_image);
		centerImage = (Button) view.findViewById(R.id.center_image);
		rightImage = (Button) view.findViewById(R.id.right_image);
		backImage = (Button) view.findViewById(R.id.goback);
		title_text = (TextView) view.findViewById(R.id.title_text);
		title = (LinearLayout) view.findViewById(R.id.title);
		image_title = (LinearLayout) view.findViewById(R.id.image_title);
		view = null;
	}
	
	/**获得返回按钮*/
	public Button getBackBtn(){
		return backImage;
	}
	
	/** 
     * 获取Title_text的组件对象
     */
	public TextView getTitle_text(){
		return title_text;
	}

	/** 
     * 获取Home按钮的组件对象
     */
	public Button getHomeImage() {
		return homeImage;
	}

	/** 
     * 获取Else按钮的组件对象
     */
	public Button getElseImage() {
		return elseImage;
	}
	
	/** 
     * 获取中间按钮的组件对象
     */
	public Button getCenterImage() {
		return centerImage;
	}
	
	/** 
     * 获取右边按钮的组件对象
     */
	public Button getRightImage() {
		return rightImage;
	}


	/** 
     * 由资源文件设置背景图片
     */
	public void setbackgroundImage(Integer resId){
		image_title.setBackgroundResource(resId);
	}
	
	/** 
     * 直接设置背景图片
     */
	public void setbackgroundImage(Bitmap bitmap){
		image_title.setBackgroundDrawable(new BitmapDrawable(bitmap));
	}
	
	/** 
     * 直接设置文字资源 
     */
	public void setTextViewText(String message){
		title_text.setText(message);
	}
	/** 
     * 从资源文件设置文字资源
     */
	public void setTextViewText(Integer resId){
		title_text.setText(resId);
	}
	/** 
     * 设置TextView的可见性
     */
	public void setTextViewVisibility(int visibility) {
		title_text.setVisibility(visibility);
	}
	/** 
     * 设置TextView的的颜色
     */
	public void setTextViewColor(int colorId) {
		title_text.setTextColor(colorId);
	}
	/** 
     * 设置TextView的OnClickListener 
     */
	public void setTextViewOnClickListener(OnClickListener l) {
		title_text.setOnClickListener(l);
	}
	
	/** 
     * 设置homeImage的图片资资源
     */  
	public void setHomeIamgeResource(int resId){
		homeImage.setBackgroundResource(resId);
	}
	/** 
     * 直接设置homeImage文字资源 
     */
	public void setTextHomeIamge(String message){
		homeImage.setText(message);
	}
	/** 
     * 从资源文件设置homeImage文字资源 
     */
	public void setTextHomeIamgeResource(Integer resId){
		homeImage.setText(resId);
	}
	/** 
     * 设置homeImage的可见性 
     */
	public void setHomeImageVisibility(int visibility) {
		homeImage.setVisibility(visibility);
	}
	/** 
     * 设置homeImage的OnClickListener 
     */
	public void setHomeImageOnClickListener(OnClickListener l) {
		homeImage.setOnClickListener(l);
	}
	/**
	 * 设置homeImage的可用性
	 * */
	public void setHomeImageEnabled(boolean enabled) {
		homeImage.setEnabled(enabled);
	}
	/** 
     * 设置elseImage的图片资源
     */  
    public void setElseImageResource(int resId) {  
        elseImage.setBackgroundResource(resId);  
    } 
	/** 
     * 直接设置elseImage文字资源 
     */
	public void setTextElseIamge(String message){
		elseImage.setText(message);
	}
	/** 
     * 从资源文件设置elseImage文字资源 
     */
	public void setTextElseIamgeResource(Integer resId){
		elseImage.setText(resId);
	}
    /** 
     * 设置elseImage的可见性
     */
	public void setElseImageVisibility(int visibility) {
		elseImage.setVisibility(visibility);
	}
	/**
	 * 设置elseImage的可用性
	 * */
	public void setElseImageEnabled(boolean enabled) {
		elseImage.setEnabled(enabled);
	}
	 /** 
     * 设置elseImage之上的文字的颜色
     */
	public void setElseImageTextColor(int color) {
		elseImage.setTextColor(color);
	}
	/** 
     * 设置elseImage的OnClickListener 
     */
	public void setElseImageOnClickListener(OnClickListener l) {
		elseImage.setOnClickListener(l);
	}
	/** 
     * 设置TitleView的颜色
     */
	public void setColor(int color) {
		title.setBackgroundColor(color);
	}
}
