package com.jbl.browser.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.EditText;

public class TopEdittextView extends EditText{

	int s_Width;
	public TopEdittextView(Context context) {
		super(context);
		init();
	}
	public TopEdittextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public TopEdittextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	void init(){
		WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        s_Width = dm.widthPixels;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		this.setMeasuredDimension(s_Width*4/7, 96*s_Width/144/5);
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
