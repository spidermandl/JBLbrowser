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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.activity.HistoryFavourateActivity;
import com.jbl.browser.activity.MainFragActivity;
import com.jbl.browser.activity.RecommendMainActivity;
import com.jbl.browser.adapter.HistoryAdapter;
import com.jbl.browser.bean.BookMark;
import com.jbl.browser.bean.History;
import com.jbl.browser.db.HistoryDao;
import com.jbl.browser.interfaces.deleteHistory;
import com.jbl.browser.utils.JBLPreference;

/**
 * 历史记录fragment
 * @author huyingying
 *
 */
public class HistoryFragment extends SherlockFragment implements deleteHistory{
	
	
	public final static String TAG="HistoryFragment";
	//历史记录listView
	public  ListView listview;
	//历史记录数据
	public  List<History> list=new ArrayList<History>();
	/*//历史记录时间段标示
	TextView tv_history_time;*/
	//历史记录适配器
	public  HistoryAdapter historyAdapter;
	//无历史记录
	public  ImageView noHistory;
	//返回图标
	ImageView back;

	ModeCallback mCallback;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.history_fragment, container, false);
		listview=(ListView)view.findViewById(R.id.list_view_history_today);
		noHistory=(ImageView)view.findViewById(R.id.cloud_history_empty);		
		initDataHistory();
		if(this.getActivity().getClass().equals(HistoryFavourateActivity.class))     //历史书签的activity
			((HistoryFavourateActivity)this.getActivity()).setInterface(this);  //设置回调接口
		else                                                             //推荐activity
			((RecommendMainActivity)this.getActivity()).setInterface(this);  //设置回调接口
		return view;
	}
	/**
	 * 初始化ListView中历史记录的数据
	 * */
	@SuppressLint("NewApi")
	public  void initDataHistory() {	
		listview.setVisibility(View.GONE);
		list=new HistoryDao(getActivity()).queryAll();//从数据库中获得数据
		if(list.size()==0){      //没有历史记录时屏幕中间显示“没有历史记录”文字
			noHistory.setVisibility(View.VISIBLE);
		}else{                   //有历史记录时显示历史记录
			Collections.sort(list,new Comparator<History>() { //倒序排列

				@Override
				public int compare(History lhs, History rhs) {
					// TODO Auto-generated method stub
					if(lhs.getId()<rhs.getId())
						return 1;
					return -1;
				}
			});
			noHistory.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
			historyAdapter=new HistoryAdapter(getActivity(), list,listview);
			listview.setAdapter(historyAdapter);
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
            historyAdapter.notifyDataSetChanged();
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
	        		builder.setTitle(R.string.delete_history);
	        		builder.setMessage(R.string.delete_item_history);
	        		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
	        			public void onClick(DialogInterface dialog, int which) {
	        				long[] checkedItemIds=listview.getCheckedItemIds();	        				
	        				for(int i=0;i<checkedItemIds.length;i++){
	        					new HistoryDao(getActivity()).deleteHistoryById(list.get((int)checkedItemIds[i]).getId());
	        				}
	        				Toast.makeText(getActivity(), R.string.delete_succeed, 100).show();
	        				initDataHistory();
	        			}
	        		});
	        		builder.setNeutralButton(R.string.cancel,new DialogInterface.OnClickListener() {
	        			public void onClick(DialogInterface dialog, int which) {
	        				initDataHistory();
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
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		//删除全部历史记录
		AlertDialog.Builder builder=new Builder(getActivity());
		//2所有builder设置一些参数
		builder.setTitle(R.string.clear_history);
		builder.setMessage(R.string.confirm_clear);
		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Boolean flag=new HistoryDao(getActivity()).clearHistory();	//清空记录	
				if(flag){
					initDataHistory();
					Toast.makeText(getActivity(), R.string.delete_succeed, 1000).show();
				}else{
					Toast.makeText(getActivity(), R.string.delete_failed, 1000).show();
				}
					
				}
			});
			builder.setNeutralButton(R.string.cancel,new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			builder.create().show();
	}
}