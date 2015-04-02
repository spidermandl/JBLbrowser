package com.jbl.browser.view;

import com.jbl.browser.R;
import com.jbl.browser.utils.JBLPreference;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * 全屏浮动框
 * @author yyjoy-mac3
 *
 */
public class FullScreenWedget extends BaseWedget{


    public FullScreenWedget(Context context){
    	super(context);
    	init();
    }
    
    void init(){
    	LayoutInflater mLayoutInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	mView=(LinearLayout)mLayoutInflater.inflate(R.layout.shrink_full_screen, null);
    	mView.setClickable(true);	
    	
    	WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		s_Width = dm.widthPixels;
    	s_Height = dm.heightPixels;
    	mWidth=s_Width/7;
    	mHeight=s_Width/7;
    	setWidth(mWidth);
    	setHeight(mHeight);
    	
    	m_top_current_x=s_Width-mWidth;
    	m_top_current_y=s_Height-mHeight;
    	mView.setOnTouchListener(this);
    	this.setContentView(mView);
    	
    	ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
    }

	@Override
	void onClick(View view) {
		if(listener!=null)
		     listener.onFullClick();
		
	}
    
    

}
