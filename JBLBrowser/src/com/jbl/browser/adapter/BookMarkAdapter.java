package com.jbl.browser.adapter;

import java.util.ArrayList;
import java.util.List;

import com.jbl.browser.R;
import com.jbl.browser.bean.BookMark;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class BookMarkAdapter extends BaseAdapter{
	List<BookMark> list_bookmark=new ArrayList<BookMark>();
	 private LayoutInflater mInflater; 
	 private Context mContext;
	 public BookMarkAdapter(Context context, List<BookMark> list) {  
		    mContext=context;  
	        mInflater = LayoutInflater.from(context);  
	        list_bookmark= list;  
	    } 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_bookmark==null?0:list_bookmark.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list_bookmark.get(position);
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
        holder.urlName.setText(list_bookmark.get(position).getWebName());
        holder.urlAddress.setText(list_bookmark.get(position).getWebAddress());
		return convertView;
	}
	public class ViewHolder  
    {  
        public View line; 
        public TextView urlName,urlAddress;  
        public ImageView image;
    }  
}
