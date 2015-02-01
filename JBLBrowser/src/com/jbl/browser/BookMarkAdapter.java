package com.jbl.browser;

import java.util.ArrayList;
import java.util.List;

import com.jbl.browser.MyListAdapter.ViewHolder;
import com.jbl.browser.fragment.BookMarkFragment;
import com.jbl.browser.model.BookMarkBean;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookMarkAdapter extends BaseAdapter{
	List<BookMarkBean> list_bookmark=new ArrayList<BookMarkBean>();
	 private LayoutInflater mInflater; 
	 private BookMarkFragment fragment;
	 private Context mContext;  
	 public BookMarkAdapter(BookMarkFragment bookMarkFragment, List<BookMarkBean> list) {  
	        this.fragment=bookMarkFragment;  
	        mInflater = LayoutInflater.from(bookMarkFragment.getActivity());  
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
            convertView.setTag(holder);  
        } else {  
            holder = (ViewHolder) convertView.getTag();
        }
        holder.urlName.setText(list_bookmark.get(position).getName());
        holder.urlAddress.setText(list_bookmark.get(position).getUrl());
		return null;
	}
	public class ViewHolder  
    {  
        public View line; 
        public TextView urlName,urlAddress;  
  
    }  
}
