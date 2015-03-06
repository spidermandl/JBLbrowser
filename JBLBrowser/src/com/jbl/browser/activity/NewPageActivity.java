package com.jbl.browser.activity;

import com.jbl.browser.R;
import com.jbl.browser.adapter.WebViewFlowAdapter;
import com.jbl.browser.view.coverflow.FancyCoverFlow;

import android.app.Activity;
import android.os.Bundle;

public class NewPageActivity extends Activity{

	private FancyCoverFlow fancyCoverFlow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);

        this.fancyCoverFlow = (FancyCoverFlow) this.findViewById(R.id.fancyCoverFlow);

        this.fancyCoverFlow.setAdapter(new WebViewFlowAdapter());
        this.fancyCoverFlow.setUnselectedAlpha(1.0f);
        this.fancyCoverFlow.setUnselectedSaturation(0.0f);
        this.fancyCoverFlow.setUnselectedScale(0.5f);
        this.fancyCoverFlow.setSpacing(50);
        this.fancyCoverFlow.setMaxRotation(0);
        this.fancyCoverFlow.setScaleDownGravity(0.2f);
        this.fancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);
        
	}
	
}
