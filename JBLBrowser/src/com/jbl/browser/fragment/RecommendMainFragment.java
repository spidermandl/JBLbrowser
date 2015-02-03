package com.jbl.browser.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jbl.browser.R;

public class RecommendMainFragment extends Fragment{

	TextView textView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View recommendMain= inflater.inflate(R.layout.fragment_recomment_main, container,false);
		 textView=(TextView) recommendMain.findViewById(R.id.textView1);
		 textView.setOnClickListener(new MyOnClickListener());
		return recommendMain;
		
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
