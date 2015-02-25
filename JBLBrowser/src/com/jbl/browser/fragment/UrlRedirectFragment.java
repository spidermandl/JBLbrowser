package com.jbl.browser.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
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
import com.jbl.browser.activity.MainFragActivity;
import com.jbl.browser.adapter.SearchAdapter;
import com.jbl.browser.utils.JBLPreference;

/**
 * 用户输入网址fragment
 * @author Desmond
 *
 */
public class UrlRedirectFragment extends SherlockFragment implements View.OnClickListener,TextWatcher,OnItemClickListener{

	public final static String TAG="UrlRedirectFragment";
	
	private SearchAdapter mSearchAdapter;// 下拉菜单适配器
	private EditText mSearch; //输入网址搜索
	private TextView mController; //二维码搜索
	private ListView mListRecommend; //输入框下拉推荐页面
	List<Integer> image=new ArrayList<Integer>(); //网站图标
	List<String> urlName=new ArrayList<String>();// 网站名称
	List<String> urlAddress=new ArrayList<String>();// 网站网址
	
	
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
		mListRecommend=(ListView)view.findViewById(R.id.mSearch_RecommnedView);
		mListRecommend.setOnItemClickListener(this);
		initData();	
		mListRecommend.setAdapter(mSearchAdapter);
		showSoftInput(true);
		return view;
	}

	private void initData(){
		mSearchAdapter=new SearchAdapter(getActivity(), image, urlName, urlAddress);

			image.add(R.drawable.ic_launcher);
			image.add(R.drawable.ic_launcher);
			image.add(R.drawable.ic_launcher);
			image.add(R.drawable.ic_launcher);
			image.add(R.drawable.ic_launcher);
			image.add(R.drawable.ic_launcher);
			image.add(R.drawable.ic_launcher);
			
			urlName.add("百度");
			urlName.add("搜狗");
			urlName.add("谷歌");
			urlName.add("新浪");
			urlName.add("腾讯");
			urlName.add("雅虎");
			urlName.add("搜索");
			
			urlAddress.add("Http://www.baidu.com");
			urlAddress.add("Http://www.sogou.com");
			urlAddress.add("Http://www.gugoo.com");
			urlAddress.add("Http://www.xinlang.com");
			urlAddress.add("Http://www.tengxun.com");
			urlAddress.add("Http://www.yahuu.com");
			urlAddress.add("Http://www.sousuo.com");
		
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
		    mController.setText(R.string.search_forward);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
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
		String webAddress=((TextView)view.findViewById(R.id.mSearch_tv2)).getText().toString();
		JBLPreference.getInstance(getActivity()).writeString(JBLPreference.BOOKMARK_HISTORY_KEY, webAddress);
        this.getActivity().finish();
        Intent intent=new Intent();
        intent.setClass(getActivity(), MainFragActivity.class);
        getActivity().startActivity(intent);
		
	}

}
