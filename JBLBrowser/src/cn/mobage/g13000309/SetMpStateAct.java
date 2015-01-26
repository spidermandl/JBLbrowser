package cn.mobage.g13000309;



import cn.mobage.g13000309.R;

import com.jbl.browser.tools.BusinessTool;
import com.zixun.piratesfantasy.view.TitleView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;

public class SetMpStateAct extends Activity {

	private TitleView titleView;
//	private LinearLayout musicStateLayout,effectStateLayout;
	private CheckBox musicStateCheck,effectStateCheck;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.set_mp_state);
		
		titleView = (TitleView) findViewById(R.id.title_view);
//		musicStateLayout = (LinearLayout) findViewById(R.id.muisc_state_layout);
//		effectStateLayout = (LinearLayout) findViewById(R.id.effect_state_layout);
		musicStateCheck = (CheckBox) findViewById(R.id.muisc_state_check);
		effectStateCheck = (CheckBox) findViewById(R.id.effect_state_check);
		
		titleView.setElseImageVisibility(View.INVISIBLE);
		titleView.setHomeIamgeResource(R.drawable.back);
		titleView.setHomeImageVisibility(View.VISIBLE);
		titleView.getBackBtn().setVisibility(View.GONE);
		titleView.getCenterImage().setVisibility(View.GONE);
		titleView.getRightImage().setVisibility(View.GONE);
		musicStateCheck.setChecked(BusinessTool.getInstance().isOpenBgMp());
		effectStateCheck.setChecked(BusinessTool.getInstance().isOpenEffectMp());
		
		//事件监听
		titleView.setHomeImageOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
//		musicStateLayout.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				changeMusicMpState(!musicStateCheck.isChecked());
//			}
//		});
		
		musicStateCheck.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeMusicMpState(musicStateCheck.isChecked());
			}
		});
		
//		effectStateLayout.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				changeEffectMpState(!effectStateCheck.isChecked());
//			}
//		});
		
		effectStateCheck.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeEffectMpState(effectStateCheck.isChecked());
			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		resumeBgPlay();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		pauseBgMuisc();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
    	if(keyCode == KeyEvent.KEYCODE_HOME){
    		Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
    	}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
    public void onAttachedToWindow() {
    this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
           super.onAttachedToWindow();
    
    }
	
	/**暂停播放背景音乐*/
	private void pauseBgMuisc(){
		BusinessTool.getInstance().pauseBgMuisc();
	}
	
	/**继续播放背景音乐*/
	private void resumeBgPlay(){
		BusinessTool.getInstance().resumeBgPlay();
	}
	
	
	/**
	 * 设置音乐开关状态
	 * 
	 * @param openOrNot 是否开启
	 * 
	 * */
	private void changeMusicMpState(boolean openOrNot){
		try {
			BusinessTool.getInstance().setBgMpState(this, openOrNot);
			musicStateCheck.setChecked(openOrNot);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置音效开关状态
	 * 
	 * @param openOrNot 是否开启
	 * 
	 * */
	private void changeEffectMpState(boolean openOrNot){
		try {
			BusinessTool.getInstance().setEffectMpState(this,openOrNot);
			effectStateCheck.setChecked(openOrNot);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
