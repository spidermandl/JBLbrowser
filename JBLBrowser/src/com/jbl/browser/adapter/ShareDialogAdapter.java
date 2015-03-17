package com.jbl.browser.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jbl.browser.R;

public class ShareDialogAdapter extends BaseAdapter{

	/**
	 * 分享界面适配器
	 * */
	private LayoutInflater mInflater; 
	private Context mContext;
	List<Integer> imv=new ArrayList<Integer>();
	public ShareDialogAdapter(Context mContext,List<Integer> imv) {
		super();
		this.mContext = mContext;
		this.imv = imv;
		mInflater = LayoutInflater.from(mContext); 
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imv==null?0:imv.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imv.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {  
	           convertView = mInflater.inflate(R.layout.share_gridview_item, null);  
	           holder = new ViewHolder();
	           holder.image=(ImageView)convertView.findViewById(R.id.imv_item);
	           convertView.setTag(holder);  
	       } else {  
	           holder = (ViewHolder) convertView.getTag();
	       }
		holder.image.setBackgroundResource(imv.get(position));
		return convertView;
	}
	public class ViewHolder  
	   {  
	       public ImageView image;//显示的图标
	   }  

}
