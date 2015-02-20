package com.jbl.browser.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.jbl.browser.R;
import com.jbl.browser.utils.JBLPreference;

/**
 * 用户输入网址fragment
 * @author Desmond
 *
 */
public class UrlRedirectFragment extends SherlockFragment implements View.OnClickListener,TextWatcher{

	public final static String TAG="UrlRedirectFragment";
	
	private EditText mSearch; //输入网址搜索
	private TextView mController; //二维码搜索
	
	
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
		
		showSoftInput(true);
		return view;
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

}
