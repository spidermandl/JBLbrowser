package com.jbl.browser.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.activity.WifiOptionActivity;

/**
 * wifi
 * @author osondesmond
 *
 */
public class WifiOptionFragment extends SherlockFragment implements OnClickListener{

	ImageView connectView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wifi, container, false);
		connectView=(ImageView)view.findViewById(R.id.wifi);
		connectView.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wifi:
			((WifiOptionActivity)this.getActivity()).startConnection();
			break;

		default:
			break;
		}
	}
}
