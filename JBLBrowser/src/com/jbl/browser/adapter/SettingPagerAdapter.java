package com.jbl.browser.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;
import com.viewpager.indicator.IconPagerAdapter;

/**
 * 设置界面滑动adapter
 * @author Desmond
 *
 */
public class SettingPagerAdapter extends PagerAdapter  implements IconPagerAdapter
{  private Map<Integer, GridView>  map;
    public static  List<View> mViewPages;  
    public SettingPagerAdapter(Context context, List<GridView> array) {
   }
   public SettingPagerAdapter(Context context,Map<Integer, GridView> map){
	this.map=map;
  }
  @Override
  public int getCount() {
	// TODO Auto-generated method stub
	return map.size();
  }
   @Override
  public boolean isViewFromObject(View arg0, Object arg1) {
	// TODO Auto-generated method stub
	return arg0 == arg1;
 }

  @Override
  public Object instantiateItem(View arg0, int arg1) {
	((ViewPager) arg0).addView(map.get(arg1));

	return map.get(arg1);
 }

  @Override
  public void destroyItem(View arg0, int arg1, Object arg2) {
	((ViewPager) arg0).removeView((View) arg2);
	//
  }
    @Override  
    public void restoreState(Parcelable state, ClassLoader loader)  
    {  
        // TODO Auto-generated method stub  
    }  
  
    @Override  
    public Parcelable saveState()  
    {  
        // TODO Auto-generated method stub  
        return null;  
    }  
  
    @Override  
    public void startUpdate(View container)  
    {  
        // TODO Auto-generated method stub  
    }

	@Override
	public int getIconResId(int index) {
		// TODO Auto-generated method stub
		return 0;
	}  
}  
