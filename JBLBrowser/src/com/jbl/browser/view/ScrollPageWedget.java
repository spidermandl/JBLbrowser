package com.jbl.browser.view;

import com.jbl.browser.R;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * 翻页悬浮按钮
 * @author Desmond
 *
 */
public class ScrollPageWedget extends BaseWedget {

	private ImageView moveUp;
	private ImageView moveDown;
	
	public ScrollPageWedget(Context context){
		super(context);
		
		init();
	}
	
	void init(){
		LayoutInflater mLayoutInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView=mLayoutInflater.inflate(R.layout.pop_window_nextpager, null);
		mView.setClickable(true);
		moveUp=(ImageView)mView.findViewById(R.id.scroll_up);
		moveDown=(ImageView)mView.findViewById(R.id.scroll_down);
		moveUp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				onClick(arg0);
				
			}
		});
		
    	WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		s_Width = dm.widthPixels;
    	s_Height = dm.heightPixels;
    	
    	mWidth=s_Width/8;
    	mHeight=s_Width/6+s_Width/16;
    	setWidth(s_Width);
    	setHeight(mHeight);
		
		m_top_current_x=s_Width-mWidth;
		m_top_current_y=s_Height/2-mHeight/2;
		
    	mView.setOnTouchListener(this);
    	this.setContentView(mView);
    	//this.setBackgroundDrawable()
	}

	@Override
	void onClick(View view) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
