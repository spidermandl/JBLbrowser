package com.jbl.browser.fragment;

import com.jbl.browser.R;
import com.jbl.browser.fragment.RecommendMainFragment.MyOnClickListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class RecommendCustomFragment extends Fragment{

	TextView textView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View recommendCustom= inflater.inflate(R.layout.fragment_recomment_main, container,false);
		 textView=(TextView) recommendCustom.findViewById(R.id.textView1);
		 textView.setOnClickListener(new MyOnClickListener());
		return recommendCustom;
		
	}
	
	class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.textView1:
				Toast.makeText(getActivity().getApplicationContext(), "推荐页面", 1).show();
				break;

			default:
				break;
			}
		}
		
	}
}
