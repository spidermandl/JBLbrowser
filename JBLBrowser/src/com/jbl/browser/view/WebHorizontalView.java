package com.jbl.browser.view;


import com.jbl.browser.R;
import com.jbl.browser.WebWindowManagement;
import com.jbl.browser.adapter.MultipageAdapter;
import com.jbl.browser.adapter.WebHorizontalViewAdapter;
import com.viewpager.indicator.PageIndicator;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 
 * @author Desmond
 * 装载webview的水平滑动控件
 *
 */
public class WebHorizontalView extends HorizontalScrollView {

    private static final String TAG = "WebHorizontalScrollView";
    
    public interface ContainerInterface{
    	void setTitle(String text);
    	void updatePageNum();
    }
    /**
     * viewpager 翻页监听
     */
    private final PageListener pageListener = new PageListener();
	public ContainerInterface containerListener;

	/**
	 * viewpager 外部注入
	 */
	private ViewPager pager;
	

    /**
     * HorizontalListView中的LinearLayout
     */
    private LinearLayout mContainer;

    /**
     * 子元素的宽度
     */
    private int mChildWidth;
    /**
     * 子元素的高度
     */
    private int mChildHeight;
    /**
     * 数据适配器
     */
    private WebHorizontalViewAdapter mAdapter;
    /**
     * 索引点
     */
    private PageIndicator mIndicator;
    /**
     * 每屏幕最多显示的个数
     */
    private int mCountOneScreen;
    /**
     * 屏幕的宽度
     */
    private int mScreenWitdh;
    /**
     * 屏幕高度
     */
    private int mScreenHeight;
    /**
     * 间隔
     */
    private int mGap;
    /**
     * 边界空距离
     */
    private int mEdge;
    /**
     * 当前webview的index
     */
	private int currentPosition = 0;
	
    private int scrollOffset = 52;

	private int lastScrollX = 0;
	
	
	private LayoutInflater mInflater;
	
	private float lastOffset=0.0f;
	
	private int adjastOffset=0;
	
	boolean isScolling = false;
	boolean direction = false;//left false; right true
	
	/**
	 * 删除webview事件
	 */
	private View.OnClickListener deleteWebviewListener;
	
	/**
	 * action down是否在page内,
	 * 如果在，值为page index
	 * 如果不再，值为－1
	 */
	private int isInPage=-1;
	

    public WebHorizontalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获得屏幕宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWitdh = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        deleteWebviewListener=new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				View view=WebWindowManagement.getInstance().deleteWebViewWithIndex(currentPosition);
				if(view!=null){
					mContainer.removeView(view);
					updateView();
					((MultipageAdapter)pager.getAdapter()).removeItem(currentPosition);
					currentPosition=currentPosition<mAdapter.getCount()?currentPosition:currentPosition-1;
					setPosition(currentPosition);	
					mIndicator.setCurrentItem(currentPosition);
					if(containerListener!=null)
						containerListener.updatePageNum();
				}else{
					
				}
				
			}
		};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mContainer = (LinearLayout) getChildAt(0);
    }

    /**
     * 设置控制滚动的viewpager
     * @param pager
     */
	public void setViewPager(ViewPager pager) {
		this.pager = pager;

		if (pager.getAdapter() == null) {
			throw new IllegalStateException("ViewPager does not have adapter instance.");
		}
		
		//pager.setOnPageChangeListener(pageListener);

		notifyDataSetChanged();
	}
	/**
	 * 设置indicator
	 * @param indicator
	 */
	public void setIndicator(PageIndicator indicator){
		mIndicator=indicator;
		if(mIndicator!=null)
			mIndicator.setOnPageChangeListener(pageListener);
	}
	
	public void notifyDataSetChanged() {

		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
				Log.e("onGlobalLayout", "onGlobalLayout");
				setPosition(currentPosition);
				mIndicator.setCurrentItem(currentPosition);
			}
		});

	}


	public void setTitleListener(ContainerInterface i){
		containerListener=i;
	}
	
    /**
     * 初始化数据，设置数据适配器
     * 
     * @param mAdapter
     */
    public void initDatas(WebHorizontalViewAdapter mAdapter) {
        this.mAdapter = mAdapter;
        mContainer = (LinearLayout) getChildAt(0);
        mContainer.removeAllViews();

        // 强制计算当前View的宽和高
        if (mChildWidth == 0 && mChildHeight == 0) {
            mChildWidth = 2*mScreenWitdh/3;
            mChildHeight = mScreenHeight/2;
            mGap= mScreenWitdh/12;
            mEdge = mScreenWitdh/2;
            // 计算每次加载多少个View
            mCountOneScreen = (mScreenWitdh / mChildWidth == 0) ? 
            		mScreenWitdh / mChildWidth + 1 : mScreenWitdh / mChildWidth + 2;

            Log.e(TAG, "mCountOneScreen = " + mCountOneScreen + " ,mChildWidth = " + mChildWidth);

        }
        // 初始化第一屏幕的元素
        initFirstScreenChildren(mCountOneScreen);
    }

    /**
     * 加载第一屏的View
     * 
     * @param mCountOneScreen
     */
    public void initFirstScreenChildren(int mCountOneScreen) {
    
        int count= mAdapter.getCount();
        mContainer.setPadding(mEdge,0 ,mEdge, 0);
        for (int i = 0; i < count; i++) {
        	View view=mInflater.inflate(R.layout.multi_static_webview, null, false);
        	view.findViewById(R.id.delete_webview).setOnClickListener(deleteWebviewListener);
        	mContainer.addView(view);
            View webview = mAdapter.getView(i, null, (RelativeLayout)view.findViewById(R.id.static_webview_container));
            //设置大小、属性
            webview.getLayoutParams().width = mChildWidth;
            webview.getLayoutParams().height = mChildHeight;
            ((ProgressWebView)webview).setScrollSetting();
            if(i!=0){
            	((LinearLayout.LayoutParams)view.getLayoutParams()).leftMargin=mGap;
            }
        }

        currentPosition = WebWindowManagement.getInstance().getCurrentWebviewIndex();
        
        this.post(new Runnable() {
			
			@Override
			public void run() {
		        setPosition(currentPosition);
			}
		});
    }

    /**
     * 刷新界面
     */
    private void updateView(){
        View[] views=new View[mAdapter.getCount()];
        for(int i=0;i<views.length;i++){
        	views[i]=mContainer.getChildAt(i);
        }
        
        mContainer.removeAllViews();
        for (int i = 0; i < views.length; i++) {
        	mContainer.addView(views[i]);
            if(i!=0){
            	((LinearLayout.LayoutParams)views[i].getLayoutParams()).leftMargin=mGap;
            }
            if (i==0)   
            	((LinearLayout.LayoutParams)views[i].getLayoutParams()).leftMargin=0;
            if(i==views.length-1)
            	((LinearLayout.LayoutParams)views[i].getLayoutParams()).rightMargin=mGap;
        }

        currentPosition = WebWindowManagement.getInstance().getCurrentWebviewIndex();
        
        this.post(new Runnable() {
			
			@Override
			public void run() {
		        setPosition(currentPosition);
			}
		});
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

    	/**
    	 * 事件分发给子控件
    	 */
    	return super.onInterceptTouchEvent(ev);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
    	if(ev.getAction()==MotionEvent.ACTION_DOWN){
    		float x=ev.getX();
    		if(x<=mScreenWitdh/2-mChildWidth/2-mGap){
    			if(currentPosition==0){
    				isInPage=-1;
    			}else{
    				isInPage=currentPosition-1;
    			}
    		}else if(x>mScreenWitdh/2-mChildWidth/2&&x<mScreenWitdh/2+mChildWidth/2){
    			isInPage=currentPosition;
    		}else if(x>mScreenWitdh/2+mChildWidth/2+mGap){
    			if(currentPosition==mAdapter.getCount()-1){
    				isInPage=-1;
    			}else{
    				isInPage=currentPosition+1;
    			}
    		}else{
    			isInPage=-1;
    		}
    	}
    	/**
    	 * 不吸收任何事件
    	 */
    	return false;
    }

    /**
     * 调整当页的位置
     * @param page
     */
    private void setPosition(int page){
    	adjustPosition(mEdge+(mGap+mChildWidth)*page);
    }
    /**
     * 调整位置
     * @param X
     * 2/3  2/3+3/4
     * 2/3 + 3/4*num
     */
    private void adjustPosition(int X){
    	float temp = (float)(X-mEdge-mChildWidth/2)/(float)(mChildWidth+mGap);
    	temp=temp<0?0:temp;
    	currentPosition = (temp-(int)temp<0.5)?(int)temp:(int)temp+1;
    	if(currentPosition==0){
    		scrollTo(mEdge+mChildWidth/2-mScreenWitdh/2, 0);
    		return;
    	}
    	smoothScrollTo((currentPosition)*(mChildWidth+mGap)+mEdge+mChildWidth/2-mScreenWitdh/2, 0);
    }

    /**
     * 返回选中page index
     * @return
     */
    public int isClickInWebView(){
    	return isInPage;
    }
   /**
    * 翻页监听器
    *
    */
    private class PageListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			/**
			 * positionOffset:[0,1)
			 */
			currentPosition = position;

			//Log.e("onPageScrolled", position+" "+positionOffset+" "+positionOffsetPixels);
			
			//0---0.99 左－－－－右
//			if((position - lastOffset)>0.95){
//				if(adjastOffset==1)
//					adjastOffset=0;
//				else
//					adjastOffset = -1;
//			}
//			//0.99---0 右------左
//			if((lastOffset-position)>0.95){
//				if(adjastOffset==-1)
//					adjastOffset=0;
//				else
//					adjastOffset = 1;
//			}
//			Log.e("onPageScrolled", (positionOffset+adjastOffset)+" ");
			
//			if(isScolling){
//				if(positionOffsetPixels>lastOffset){
//					scrollToChild(position, (int) ((positionOffset-1) * mChildWidth));
//				}else{
//					scrollToChild(position, (int) ((positionOffset) * mChildWidth));
//				}
//			}else
//				setPosition(position);

			scrollToChild(position, (int) ((positionOffset) * mChildWidth));
			lastOffset=positionOffsetPixels;
			
			//invalidate();

		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				Log.e("onPageScrollStateChanged", "onPageScrollStateChanged");
				adjastOffset=0;
				isScolling=false;
			}

			if(state == ViewPager.SCROLL_STATE_SETTLING){
				adjastOffset=0;
				isScolling=false;
			}
			
			if(state == ViewPager.SCROLL_STATE_DRAGGING){
				isScolling=true;
			}
			if (containerListener != null) {
				containerListener.setTitle(WebWindowManagement.getInstance().getTitleWithIndex(currentPosition));
			}
		}

		@Override
		public void onPageSelected(int position) {
			if (containerListener != null) {
				containerListener.setTitle(WebWindowManagement.getInstance().getTitleWithIndex(currentPosition));
			}
		}

	}
    
    /**
     * scrollview滑动
     * @param position
     * @param offset
     */
	private void scrollToChild(int position, int offset) {

		if (mAdapter.getCount() == 0) {
			return;
		}

		int newScrollX = mEdge+mChildWidth/2+(mChildWidth+mGap)*position-mScreenWitdh/2 + offset;

//		if (position > 0 || offset > 0) {
//			newScrollX -= scrollOffset;
//		}

		if (newScrollX != lastScrollX) {
			lastScrollX = newScrollX;
			Log.e("scroll distance", newScrollX+"");
			this.smoothScrollTo(newScrollX, 0);
		}

	}
	
	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.currentPosition = currentPosition;
		return savedState;
	}

	static class SavedState extends BaseSavedState {
		int currentPosition;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			currentPosition = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(currentPosition);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}