package com.jbl.browser.view;

import com.jbl.browser.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 装载多页缩略webview的viewgroup
 * @author Desmond
 *
 */
public class BriefWindowFrame extends RelativeLayout {

	ImageView deleteWebView;
	private Context ctx;
	
	public BriefWindowFrame(Context context) {
		super(context);
		init(context);
	}
	public BriefWindowFrame(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public BriefWindowFrame(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context){
		ctx=context;
		this.setClickable(true);

	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		deleteWebView=(ImageView)this.findViewById(R.id.delete_webview);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		float x=ev.getX();
		float y=ev.getY();
		Log.e("posision", "X: "+x+" Y: "+y+" WIDTH:"+this.getMeasuredWidth()+" HEIGHT;"+this.getMeasuredHeight()+" width:"+deleteWebView.getMeasuredWidth()+" height:"+deleteWebView.getMeasuredHeight());
		if(deleteWebView!=null){
			
			if(x>=this.getMeasuredWidth()-deleteWebView.getMeasuredHeight()&&x<=this.getMeasuredWidth()
					&&
			   y>0&&y<deleteWebView.getMeasuredHeight()){
				return super.onInterceptTouchEvent(ev);
			}
		}
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
