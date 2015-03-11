package com.jbl.browser.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.activity.MainFragActivity;
import com.jbl.browser.adapter.BookMarkAdapter;
import com.jbl.browser.bean.BookMark;
import com.jbl.browser.db.BookMarkDao;
import com.jbl.browser.utils.JBLPreference;

/**
 * 书签fragment
 * @author huyingying
 *
 */
public class BookMarkFragment extends SherlockFragment {
	
	public final static String TAG="BookMarkFragment";
	
	//书签listView
	ListView listview;
	//书签数据
	List<BookMark> list= null;

	//网址
	String webAddress="";
	//网名
	String webName="";
	//没有书签
	ImageView noBookmark;
	BookMarkAdapter bookMarkAdapter;
	
	ModeCallback mCallback;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_bookmark, container, false);	
		listview=(ListView)view.findViewById(R.id.list_view_bookmark);
		
		noBookmark=(ImageView)view.findViewById(R.id.cloud_favorite_empty);
		initDataFavorites();

		return view;
	}
	
	/**
	 * 初始化ListView中书签的数据
	 * */
	@SuppressLint("NewApi")
	private void initDataFavorites() {
		listview.setVisibility(View.GONE);		
		list=new BookMarkDao(getActivity()).queryBookMarkAllByisRecommend(false);//从数据库中获得数据		
		if(list.size()==0){//没有书签时屏幕中间显示“没有书签”文字
			noBookmark.setVisibility(View.VISIBLE);
		}else{            //有书签时显示书签
			Collections.sort(list,new Comparator<BookMark>() { //倒序排列

				@Override
				public int compare(BookMark lhs, BookMark rhs) {
					// TODO Auto-generated method stub
					if(lhs.getId()<rhs.getId())
						return 1;
					return -1;
				}
			});
			noBookmark.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
			bookMarkAdapter=new BookMarkAdapter(getActivity(), list,listview);
			listview.setAdapter(bookMarkAdapter);
			listview.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
					String webAddress=list.get(position).getWebAddress();
					JBLPreference.getInstance(getActivity()).writeString(JBLPreference.BOOKMARK_HISTORY_KEY, webAddress);
					getActivity().finish();
				    Intent intent=new Intent();
				    intent.setClass(getActivity(), MainFragActivity.class);
				    startActivity(intent);
				} 
			});
		}	
		mCallback=new ModeCallback();
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);	
		listview.setMultiChoiceModeListener(mCallback);		
	}
	

	@SuppressLint("NewApi")
	private class ModeCallback implements ListView.MultiChoiceModeListener {
        private View mMultiSelectActionBarView;
        private TextView mSelectedCount;
        private ImageView deleteIcon;
        @Override
        public void onDestroyActionMode(ActionMode mode) {
        	listview.clearChoices();
        }

        @SuppressLint("NewApi")
		@Override
        public void onItemCheckedStateChanged(ActionMode mode,
                int position, long id, boolean checked) {
            updateSeletedCount();
            mode.invalidate();
            bookMarkAdapter.notifyDataSetChanged();
        }
        
        @SuppressLint("NewApi")
		public void updateSeletedCount(){
        	mSelectedCount.setText(Integer.toString(listview.getCheckedItemCount()));
        }

		@Override
		public boolean onCreateActionMode(ActionMode mode,
				android.view.Menu menu) {
			 // actionmode的菜单处理
            if (mMultiSelectActionBarView == null) {
                mMultiSelectActionBarView = LayoutInflater.from(getActivity())
                    .inflate(R.layout.list_multi_select_actionbar, null);
                mSelectedCount =
                    (TextView)mMultiSelectActionBarView.findViewById(R.id.selected_conv_count);
                deleteIcon=(ImageView)mMultiSelectActionBarView.findViewById(R.id.delete_icon);
            }
            mode.setCustomView(mMultiSelectActionBarView);
            deleteIcon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder builder=new Builder(getActivity());
	        		//2所有builder设置一些参数
	        		builder.setTitle(R.string.delete_bookmark);
	        		builder.setMessage(R.string.delete_item);
	        		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
	        			public void onClick(DialogInterface dialog, int which) {
	        				long[] checkedItemIds=listview.getCheckedItemIds();
	        				for(int i=0;i<checkedItemIds.length;i++){
	        					new BookMarkDao(getActivity()).deleteBookMarkById(list.get((int)checkedItemIds[i]).getId());
	        				}
	        				Toast.makeText(getActivity(), R.string.delete_succeed, 100).show();
	        				initDataFavorites();
	        			}
	        		});
	        		builder.setNeutralButton(R.string.cancel,new DialogInterface.OnClickListener() {
	        			public void onClick(DialogInterface dialog, int which) {
	        				initDataFavorites();
	        			}
	        		});
	        		
	        		builder.create().show();
				}
			});
            return true;
		}

		@SuppressLint("NewApi")
		@Override
		public boolean onPrepareActionMode(ActionMode mode,
				android.view.Menu menu) {
			 if (mMultiSelectActionBarView == null) {
	                ViewGroup v = (ViewGroup)LayoutInflater.from(getActivity())
	                    .inflate(R.layout.list_multi_select_actionbar, null);
	                mode.setCustomView(v);
	                mSelectedCount = (TextView)v.findViewById(R.id.selected_conv_count);
	                deleteIcon=(ImageView)mMultiSelectActionBarView.findViewById(R.id.delete_icon);
	            }            
	            return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			// TODO Auto-generated method stub
			return false;
		}

    }
}
