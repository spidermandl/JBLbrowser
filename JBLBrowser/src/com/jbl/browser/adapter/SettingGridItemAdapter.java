package com.jbl.browser.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbl.browser.R;
import com.jbl.browser.fragment.SettingPagerFragment;

/**
 * 设置界面中每一个小项布局界面
 * @author Desmond
 *
 */
public class SettingGridItemAdapter extends BaseAdapter  
{  
    private LayoutInflater mInflater;  
    private List<String> mList; 
    private Context mContext;  
    private List<Integer> images;
    /*
	 * 菜单图标
	 */
	private int[] girdview_menu_image =new int[] {R.drawable.menu_add_bookmark_selector,R.drawable.menu_combine_selector,R.drawable.menu_setting_selector,
			R.drawable.menu_nightmode_selector,R.drawable.menu_share_selector,R.drawable.no_pic_mode_selector,R.drawable.menu_download_selector,
			R.drawable.menu_quit_selector,R.drawable.menu_scrollbutton,R.drawable.menu_wuhen_selector,R.drawable.menu_fullscreen_selector,
			R.drawable.menu_refresh_selector,R.drawable.menu_feedback_selector,R.drawable.ic_launcher};
    public SettingGridItemAdapter(Context context, List<String> list) {  
        mContext = context;  
        mInflater = LayoutInflater.from(mContext);  
        mList = list;  
    }  
    @Override  
    public int getCount()  
    {  
        return mList.size();  
    }  
    @Override  
    public Object getItem(int position)  
    {  
        return mList.get(position);  
    }  
    @Override  
    public long getItemId(int position)  
    {  
        return position;  
    }  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent)  
    {  
        ViewHolder holder;  
        if (convertView == null) {  
            convertView = mInflater.inflate(R.layout.main_setting_item, null);  
            holder = new ViewHolder();  
            holder.icon = (ImageView) convertView.findViewById(R.id.viewpage_test_icon);  
            holder.text = (TextView) convertView.findViewById(R.id.viewpage_test_text);  
            convertView.setTag(holder);  
        } else {  
            holder = (ViewHolder) convertView.getTag();
        }  
        holder.text.setText(mList.get(position));
       if(position==0){
    	   holder.icon.setBackgroundResource(R.drawable.menu_add_bookmark_selector);
       }
       if(position==1){
    	   holder.icon.setBackgroundResource(R.drawable.menu_combine_selector);
       }
       if(position==2){
    	   holder.icon.setBackgroundResource(R.drawable.menu_setting_selector);
       }
       if(position==3){
    	   holder.icon.setBackgroundResource(R.drawable.menu_nightmode_selector);
       }
       if(position==4){
    	   holder.icon.setBackgroundResource(R.drawable.menu_share_selector);
       }
       if(position==5){
    	   holder.icon.setBackgroundResource(R.drawable.no_pic_mode_selector);
       }
       if(position==6){
    	   holder.icon.setBackgroundResource(R.drawable.menu_download_selector);
       }
       if(position==7){
    	   holder.icon.setBackgroundResource(R.drawable.menu_quit_selector);
       }
       if(position==8){
    	   holder.icon.setBackgroundResource(R.drawable.ic_launcher);
       }
    
        return convertView;  
 }  
    public final class ViewHolder  
    {  
        public ImageView icon;  
        public TextView text;  
  
    }  
}  