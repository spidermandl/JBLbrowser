package com.jbl.browser.activity;

import com.jbl.browser.R;
import com.jbl.browser.WebWindowManagement;
import com.jbl.browser.adapter.WebHorizontalViewAdapter;
import com.jbl.browser.view.WebHorizontalView;
import com.jbl.browser.view.WebHorizontalView.CurrentImageChangeListener;
import com.jbl.browser.view.WebHorizontalView.OnItemClickListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class NewPageActivity extends Activity{

//	private FancyCoverFlow fancyCoverFlow;
	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		setContentView(R.layout.activity_new_page);
//        this.fancyCoverFlow = (FancyCoverFlow) this.findViewById(R.id.fancyCoverFlow);
//
//        this.fancyCoverFlow.setAdapter(new WebViewFlowAdapter());
//        this.fancyCoverFlow.setUnselectedAlpha(1.0f);
//        this.fancyCoverFlow.setUnselectedSaturation(0.0f);
//        this.fancyCoverFlow.setUnselectedScale(0.5f);
//        this.fancyCoverFlow.setSpacing(50);
//        this.fancyCoverFlow.setMaxRotation(0);
//        this.fancyCoverFlow.setScaleDownGravity(0.2f);
//        this.fancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);
//		super.onCreate(savedInstanceState);
//		
//
//        
//	}
	
	private WebHorizontalView mHorizontalScrollView;
    private WebHorizontalViewAdapter mAdapter;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LinearLayout layout =new LinearLayout(this);
//        layout.setPadding(50, 50, 50, 50);
//        WebWindowManagement.getInstance().replaceMainWebView(layout);
//        setContentView(layout);
        setContentView(R.layout.activity_new_page);


        mHorizontalScrollView = (WebHorizontalView) findViewById(R.id.id_horizontalScrollView);
        mAdapter = new WebHorizontalViewAdapter();
        // 添加滚动回调
        mHorizontalScrollView
                .setCurrentImageChangeListener(new CurrentImageChangeListener() {
                    
					@Override
					public void onCurrentImgChanged(int position,
							View viewIndicator) {
						
					}
                });
        // 添加点击回调
        mHorizontalScrollView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onClick(View view, int position) {
            }
        });
        // 设置适配器
        mHorizontalScrollView.initDatas(mAdapter);
    }
	
}
