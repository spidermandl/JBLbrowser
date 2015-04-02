package com.jbl.browser.view;

import com.jbl.browser.utils.JBLPreference;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

/**
 * base popupwindow
 * @author yyjoy-mac3
 *
 */
public abstract class BaseWedget extends PopupWindow implements OnTouchListener{

	protected Context mContext;
	protected View mView;//window view
	protected int s_Width;//屏幕高
	protected int s_Height;//屏幕宽
	protected int mWidth=0;//window高
	protected int mHeight=0;//window宽
	protected int m_top_current_x=-1;   // 全屏按钮初始X轴位置
	protected int m_top_current_y=-1;   // 全屏按钮初始Y轴位置
	protected float detX,detY;//移动变换距离
	protected float downX,downY,upX,upY;//按下位置和点击位置
	
	WedgetClickListener listener;//和主页面交互接口
	
	
    public BaseWedget(Context context){
    	super(context);
    	mContext=context;
    }
    
    public void setInterface(WedgetClickListener listen){
    	this.listener=listen;
    }
    
	@Override
	public boolean onTouch(View v, MotionEvent event) {
	    switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			upX=downX = (int) event.getX();
			upY=downY = (int) event.getY();
			Log.e("raw", event.getRawX()+"  "+event.getRawY());
			detX = m_top_current_x - event.getRawX();
			detY = m_top_current_y - event.getRawY();
		    Log.e("x_y", m_top_current_x+" : "+m_top_current_y);
			break;
        case MotionEvent.ACTION_MOVE:
        	upX = (int) event.getX();
			upY = (int) event.getY();
			Log.e("raw", event.getRawX()+"  "+event.getRawY());
        	m_top_current_x = (int) (event.getRawX() + detY);
        	m_top_current_y = (int) (event.getRawY() + detY);
        	m_top_current_x = m_top_current_x>s_Width-mWidth?s_Width-mWidth:m_top_current_x;
        	m_top_current_x = m_top_current_x<0?0:m_top_current_x;
        	m_top_current_y = m_top_current_y>s_Height-mHeight?s_Height-mHeight:m_top_current_y;
        	m_top_current_y = m_top_current_y<0?0:m_top_current_y;
			if (downX==upX&&downY==upY) {
				mView.setPressed(false);		
			}
			this.update(m_top_current_x,m_top_current_y, -1, -1);	
			break;
        case MotionEvent.ACTION_UP:
        	if (downX==upX&&downY==upY) { //点击
        		onClick(mView);
        		this.dismiss();
        	}else{//移动
        		mView.setPressed(true);
        		JBLPreference.getInstance(mContext).writeInt(
						JBLPreference.pop_full_currentX_value,
						m_top_current_x);
				JBLPreference.getInstance(mContext).writeInt(
						JBLPreference.pop_full_currentY_value,
						m_top_current_y);
        	}
	         break;
		default:
			break;
		}

		return false;
	}
	
    
    public void show(){
    	this.showAtLocation(mView, Gravity.NO_GRAVITY, m_top_current_x, m_top_current_y);
    }
    
    abstract void onClick(View view);
    
    public interface WedgetClickListener{
    	void onFullClick();
    	void onPageScroll(boolean up);
    }
    
}
