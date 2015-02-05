package com.jbl.browser.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jbl.browser.R;
import com.jbl.browser.bean.SetContent;
/*
 * 菜单设置选项的适配器
 */
public class MenuSetAdapter extends BaseAdapter{
	List<SetContent> list_set=new ArrayList<SetContent>();
	 private LayoutInflater mInflater; 
	 private Activity activity;
	 public MenuSetAdapter(Activity activity, List<SetContent> list) {  
	        this.activity=activity;  
	        mInflater = LayoutInflater.from(activity);  
	        list_set=list;  
	    } 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_set==null?0:list_set.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list_set.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;  
       if (convertView == null) {  
           convertView = mInflater.inflate(R.layout.menusetitem, null);  
           holder = new ViewHolder();  
           holder.line = (View) convertView.findViewById(R.id.line);  
           holder.fontText = (TextView) convertView.findViewById(R.id.set_content);  
           holder.fongSize = (TextView) convertView.findViewById(R.id.size);  
           convertView.setTag(holder);  
       } else {  
           holder = (ViewHolder) convertView.getTag();
       }
       holder.fontText.setText(list_set.get(position).getSetText());
       holder.fongSize.setText(list_set.get(position).getTextSize());
		return convertView;
	}
	public class ViewHolder  
   {  
       public View line; 
       public TextView fontText,fongSize;  
 
   }  
}
