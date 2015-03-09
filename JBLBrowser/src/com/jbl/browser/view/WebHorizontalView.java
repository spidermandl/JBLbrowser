package com.jbl.browser.view;

import java.util.HashMap;
import java.util.Map;

import com.jbl.browser.adapter.WebHorizontalViewAdapter;
import com.viewpager.indicator.PageIndicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * 
 * @author Desmond
 *
 */
public class WebHorizontalView extends HorizontalScrollView {

    /**
     * 条目点击时的回调
     * 
     */
    public interface OnItemClickListener {
        void onClick(View view, int pos);
    }


    private static final String TAG = "WebHorizontalScrollView";
    
    private final PageListener pageListener = new PageListener();
	public OnPageChangeListener delegatePageListener;

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
     * 当前最后一张图片的index
     */
    private int mCurrentIndex;
    /**
     * 当前第一张图片的下标
     */
    private int mFristIndex;
    /**
     * 当前第一个View
     */
    private View mFirstView;
    /**
     * 数据适配器
     */
    private WebHorizontalViewAdapter mAdapter;
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
     * x方向移动距离
     */
    private float xDistance;
    /**
     * x移动前位置
     */
    private float xLast;

	private int currentPosition = 0;
	
	private float currentPositionOffset = 0f;
	
    private int scrollOffset = 52;

	private int lastScrollX = 0;
	

    public WebHorizontalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获得屏幕宽度
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWitdh = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mContainer = (LinearLayout) getChildAt(0);
    }

    
	public void setViewPager(ViewPager pager) {
		this.pager = pager;

		if (pager.getAdapter() == null) {
			throw new IllegalStateException("ViewPager does not have adapter instance.");
		}

		//pager.setOnPageChangeListener(pageListener);

		notifyDataSetChanged();
	}
	
	public void setIndicator(PageIndicator indicator){
		if(indicator!=null)
			indicator.setOnPageChangeListener(pageListener);
	}
	
	public void notifyDataSetChanged() {

		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
					getViewTreeObserver().removeGlobalOnLayoutListener(this);
				} else {
					getViewTreeObserver().removeOnGlobalLayoutListener(this);
				}

				currentPosition = pager.getCurrentItem();
				scrollToChild(currentPosition, 0);
			}
		});

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
            mCountOneScreen = (mScreenWitdh / mChildWidth == 0) ? mScreenWitdh
                    / mChildWidth + 1 : mScreenWitdh / mChildWidth + 2;

            Log.e(TAG, "mCountOneScreen = " + mCountOneScreen
                    + " ,mChildWidth = " + mChildWidth);

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
    
        int count= mAdapter.getCount()<mCountOneScreen?mAdapter.getCount():mCountOneScreen;
        mContainer.setPadding(mEdge,0 ,mEdge, 0);
        for (int i = 0; i < count; i++) {
            View view = mAdapter.getView(i, null, mContainer);
            view.getLayoutParams().width = mChildWidth;
            view.getLayoutParams().height = mChildHeight;
            if(i!=0){
            	((LinearLayout.LayoutParams)view.getLayoutParams()).leftMargin=mGap;
            }
        }

        mCurrentIndex = 0;
        
        this.post(new Runnable() {
			
			@Override
			public void run() {
		        adjustPosition(0);
			}
		});
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//
//    	switch (ev.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			xDistance  = 0f;
//			xLast = ev.getX();
//			break;
//		case MotionEvent.ACTION_MOVE:
//			final float curX = ev.getX();
//
//			xDistance += Math.abs(curX - xLast);
//			xLast = curX;
//
//			/**
//			 * 当滑动距离大于mChildWidth+mGap时，不做处理
//			 */
//			if (xDistance > mChildWidth+mGap) {
//				return false;
//			}
//		}
//    	return super.onInterceptTouchEvent(ev);
//    }
//    
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//        case MotionEvent.ACTION_MOVE:
////            // Log.e(TAG, getScrollX() + "");
////
////            int scrollX = getScrollX();
////            // 如果当前scrollX为view的宽度，加载下一张，移除第一张
////            if (scrollX >= mChildWidth) {
////                loadNextImg();
////            }
////            // 如果当前scrollX = 0， 往前设置一张，移除最后一张
////            if (scrollX == 0) {
////                loadPreImg();
////            }
//            break;
//        case MotionEvent.ACTION_UP:
//        	int X=getScrollX();
//        	adjustPosition(X+mScreenWitdh/2);
//        	break;
//        default:
//        	break;
//        }
//        return super.onTouchEvent(ev);
//    }
//
//    @Override
//    public void fling(int velocityX) {
//    	/**
//    	 * 禁用fling操作
//    	 */
//    	super.fling(velocityX/3);
//    }
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
    	mCurrentIndex = (temp-(int)temp<0.5)?(int)temp:(int)temp+1;
    	if(mCurrentIndex==0){
    		scrollTo(mEdge+mChildWidth/2-mScreenWitdh/2, 0);
    		return;
    	}
    	smoothScrollTo((mCurrentIndex)*(mChildWidth+mGap)+mEdge+mChildWidth/2-mScreenWitdh/2, 0);
    }

    
    
    private class PageListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			currentPosition = position;
			currentPositionOffset = positionOffset;

			scrollToChild(position, (int) (positionOffset * mChildWidth));

			invalidate();

			if (delegatePageListener != null) {
				delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				scrollToChild(pager.getCurrentItem(), 0);
				setPosition(pager.getCurrentItem());
			}

			if (delegatePageListener != null) {
				delegatePageListener.onPageScrollStateChanged(state);
			}
		}

		@Override
		public void onPageSelected(int position) {
			if (delegatePageListener != null) {
				delegatePageListener.onPageSelected(position);
			}
		}

	}
    
	private void scrollToChild(int position, int offset) {

		if (mAdapter.getCount() == 0) {
			return;
		}

		int newScrollX = mEdge+mChildWidth/2+(mChildWidth+mGap)*position-mScreenWitdh/2 + offset;

		if (position > 0 || offset > 0) {
			newScrollX -= scrollOffset;
		}

		if (newScrollX != lastScrollX) {
			lastScrollX = newScrollX;
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