package com.jbl.browser.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbl.browser.R;
import com.jbl.browser.bean.BookMark;
import com.jbl.browser.bean.History;

public class SearchAdapter extends BaseAdapter{

	List<BookMark> list_bookmark=new ArrayList<BookMark>(); //推荐界面数据
	List<History> list_history=new ArrayList<History>();  //历史记录数据
	private boolean mFlag=false; //默认值为false 默认历史记录为空 ，读取推荐页面数据
	 private LayoutInflater mInflater; 
	 private Context mContext;
	 public SearchAdapter(Context context, List<BookMark> list,List<History> history,Boolean mFlag) {  
		    mContext=context;  
	        mInflater = LayoutInflater.from(context);  
	        list_bookmark= list;
	        list_history=history;
	        this.mFlag=mFlag;
	    } 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(!mFlag){
			return list_bookmark==null?0:list_bookmark.size();
		}else{
			return list_history==null?0:list_history.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(!mFlag){
			return list_bookmark.get(position);
		}else{
			return list_history.get(position);
		}
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
           convertView = mInflater.inflate(R.layout.bookmark_item, null);  
           holder = new ViewHolder();  
           holder.line = (View) convertView.findViewById(R.id.line);  
           holder.urlName = (TextView) convertView.findViewById(R.id.url_name);  
           holder.urlAddress = (TextView) convertView.findViewById(R.id.url_address);
           holder.image=(ImageView)convertView.findViewById(R.id.iv_url_image);
           convertView.setTag(holder);  
       } else {  
           holder = (ViewHolder) convertView.getTag();
       }
       if(!mFlag){
    	   holder.urlName.setText(list_bookmark.get(position).getWebName());
    	   holder.urlAddress.setText(list_bookmark.get(position).getWebAddress());
       }else{
    	   holder.urlName.setText(list_history.get(position).getWebName());
    	   holder.urlAddress.setText(list_history.get(position).getWebAddress());
       }
		return convertView;
	}
	public class ViewHolder  
   {  
       public View line; 
       public TextView urlName,urlAddress;  
       public ImageView image;
   }  
	
}

