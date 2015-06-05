package com.jbl.browser.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;

/**
 * wifi
 * @author osondesmond
 *
 */
public class WifiOptionFragment extends SherlockFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wifi, container, false);
		return view;
	}
}
