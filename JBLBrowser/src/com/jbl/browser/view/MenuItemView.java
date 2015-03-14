package com.jbl.browser.view;


import com.jbl.browser.R;
import com.jbl.browser.WebWindowManagement;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * 菜单按钮
 * @author Desmond
 *
 */
public class MenuItemView extends ImageView {

	private final static int MENU_BACK=0;//网页返回按钮
	private final static int MENU_FORWARD=1;//网页向前按钮
	private final static int MENU_HOME=2;//回到主页按钮
	private final static int MENU_MULTI_PAGE=3;//多页按钮
	private final static int MENU_SETTING=4;// 设置按钮
	
	/**
	 * 当前属性
	 */
	private int type;
	/**
	 * 屏幕宽度
	 */
	private int s_Width;
	
	private Paint mPaint;
	
	public MenuItemView(Context context) {
		super(context);
		type=-1;
		init();
	}
	
	public MenuItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.menu_item);
		type=a.getInt(R.styleable.menu_item_type, -1);
		init();
	}
	
	public MenuItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.menu_item);
		type=a.getInt(R.styleable.menu_item_type, -1);
		init();
	}
	
	private void init() {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		s_Width = dm.widthPixels;
		
		refreshBg();
		
		mPaint=new Paint();
		mPaint.setColor(0xFF000000);
		mPaint.setStyle(Style.FILL);
		mPaint.setTextSize(40);
		mPaint.setAntiAlias(true); 
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
	}
	
    /**
	 * 刷新背景图片
	 */
	private void refreshBg(){
		switch (type) {
		case MENU_BACK:
			setImageResource(R.drawable.toolbar_back_selector);
			break;
		case MENU_FORWARD:
			setImageResource(R.drawable.toolbar_forward_selector);
			break;
		case MENU_HOME:
			setImageResource(R.drawable.toolbar_home_selector);
			break;
		case MENU_MULTI_PAGE:
			setImageResource(R.drawable.toolbar_window_selector);
			break;
		case MENU_SETTING:
			setImageResource(R.drawable.toolbar_menu_selector);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		this.setMeasuredDimension(s_Width/5, 96*s_Width/144/5);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(type==MENU_MULTI_PAGE){
			int pageNum=WebWindowManagement.getInstance().getCount();
			canvas.drawText(pageNum+"", 
					getMeasuredWidth()/2-mPaint.measureText(pageNum+"")/2, 
					getMeasuredHeight()/2+getFontHeight()/4, mPaint);
		}
		super.onDraw(canvas);
	}

	/**
	 * 获取字体高度
	 * @return
	 */
	private int getFontHeight()
	{
	    FontMetrics fm = mPaint.getFontMetrics();
	    return (int) Math.ceil(fm.bottom - fm.top);
	}
	/**
	 * 设置属性
	 * @param t
	 */
	public void setType(int t){
		type=t;
		refreshBg();
	}

}
