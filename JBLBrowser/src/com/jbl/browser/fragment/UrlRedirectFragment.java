package com.jbl.browser.fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.jbl.browser.R;
import com.jbl.browser.adapter.SearchAdapter;
import com.jbl.browser.bean.BookMark;
import com.jbl.browser.bean.History;
import com.jbl.browser.db.BookMarkDao;
import com.jbl.browser.db.HistoryDao;
import com.jbl.browser.utils.JBLPreference;

/**
 * 用户输入网址fragment
 * @author Desmond
 *
 */
public class UrlRedirectFragment extends SherlockFragment implements 
               View.OnClickListener,
               TextWatcher,
               OnItemClickListener {

	public final static String TAG="UrlRedirectFragment";
	//SearchAdapter的四个参数 1.上下文 2.推荐界面数据  3.历史记录数据 4.判断数据源
	private SearchAdapter mSearchAdapter;// 下拉菜单适配器
	private EditText mSearch; //输入网址搜索
	private TextView mController; //二维码搜索
	//搜索记录 listView
	ListView listview;
	//读取推荐页面记录数据
	List<BookMark> list=null;
	//读取历史记录数据
	List<History> history=null;
	//搜索网址
	String webAddress="";
	//搜索网名
	String webName="";
	//建立一个新的list 储存数据 实现模糊搜索
	List<History> newHistory=new ArrayList<History>();
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_main_search, container, false);
		mSearch=(EditText)view.findViewById(R.id.url_search);
		mController=(TextView)view.findViewById(R.id.control_search);
		mController.setText(R.string.search_cancel);
		mController.setOnClickListener(this);
		mSearch.addTextChangedListener(this);
		mSearch.setFocusable(true);
		mSearch.setFocusableInTouchMode(true);
		mSearch.requestFocus();
		listview=(ListView)view.findViewById(R.id.mSearch_RecommnedView);
		initDataRecommend();	
		history=new HistoryDao(getActivity()).queryAll();
		listview.setOnItemClickListener(this);			
		showSoftInput(true);
		return view;
	}

	/**
	 * 从数据库中读数据
	 * 当没有输入字符是，下拉菜单显示推荐页面
	 * */
	private void initDataRecommend(){
		list=new BookMarkDao(getActivity()).queryBookMarkAllByisRecommend(true);
			//从推荐页面读取数据
			mSearchAdapter=new SearchAdapter(getActivity(), list,null,false);
			listview.setAdapter(mSearchAdapter);	
	}
	/**
	 * 从数据库中读数据
	 * 当有输入字符时，显示历史记录
	 * */
	private void initDataHistory(){
		mSearchAdapter=new SearchAdapter(getActivity(), null,newHistory,true);
		listview.setAdapter(mSearchAdapter);
		mSearchAdapter.notifyDataSetChanged();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.control_search:
			showSoftInput(false);
			getFragmentManager().popBackStack();
			if(mSearch.getEditableText().length()>0){
				String url=mSearch.getEditableText().toString();
				if(!url.contains("http://")){
					url="http://"+url;
				}
			    JBLPreference.getInstance(getActivity()).writeString(JBLPreference.BOOKMARK_HISTORY_KEY, url);
			
			}//((BaseFragActivity)this.getActivity()).removeFragment(this);
			break;

		default:
			break;
		}
		
	}
	/**
	 * 输入文字变化后监听
	 */
	@Override
	public void afterTextChanged(Editable s) {
		if(s.length()==0)
			mController.setText(R.string.search_cancel);
		else
			//这里取得历史记录中的网址，与输入框中的字符进行匹配，并且将数据更新到新的list中。
			for(int i=0;i<history.size();i++){
				 History user = history.get(i);
				 user.getWebAddress(); //取得网址
			if(user.getWebAddress().contains(s)){
				newHistory.add(history.get(i));
				}
			}
			initDataHistory();//更新历史记录，实现模糊搜索
		    mController.setText(R.string.search_forward);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	//这里清楚newhietory的数据，保证实施更新
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		newHistory.clear();
	}
	
	/**
	 * 控制软键盘显示和隐藏
	 * @param is
	 */
	private void showSoftInput(boolean is){
        InputMethodManager imm = (InputMethodManager)this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
		if(is){
			if(!imm.isActive(mSearch))
			     imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
			     //imm.showSoftInput(mSearch,InputMethodManager.SHOW_FORCED);
		}
		else{
			if(imm.isActive(mSearch))
				imm.hideSoftInputFromWindow(this.getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);  
			    //imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String webAddress=((TextView)view.findViewById(R.id.url_address)).getText().toString();
		JBLPreference.getInstance(getActivity()).writeString(JBLPreference.BOOKMARK_HISTORY_KEY, webAddress);
		getFragmentManager().popBackStack();
		//((BaseFragActivity) this.getActivity()).navigateTo(MainPageFragment.class, null, true, MainPageFragment.TAG);
		
		
	}
}
