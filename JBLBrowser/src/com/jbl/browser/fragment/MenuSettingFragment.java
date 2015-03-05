package com.jbl.browser.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.utils.BrightnessSettings;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.SlipButton;
import com.jbl.browser.utils.SlipButton.OnChangedListener;

/**
 * 菜单设置选项fragment
 */
public class MenuSettingFragment extends SherlockFragment {
	
	public final static String TAG="MenuSettingFragment";
	
	//菜单设置选项内容 1 字体大小 2屏幕亮度 3默认浏览器 4 关于我们 5 清除数据 6恢复默认设置
	private SlipButton mMenuSettingsSb;
	private Button mMenuSetFont,mMenuSetIntensity,mMenuSetBrowser,mMenuSetAbout,mMenuSetClear,
					mMentSetSetting;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menuset_fragment, container, false);
		mMenuSetFont=(Button)view.findViewById(R.id.menuset_but_font);
		mMenuSetIntensity=(Button)view.findViewById(R.id.menuset_but_intensity);
		mMenuSetAbout=(Button)view.findViewById(R.id.menuset_but_about);
		mMenuSetClear=(Button)view.findViewById(R.id.menuset_but_clear);
		mMentSetSetting=(Button)view.findViewById(R.id.menuset_but_settings);
		mMenuSettingsSb=(SlipButton)view.findViewById(R.id.menuset_splitbutton);
		mMenuSetBrowser=(Button)view.findViewById(R.id.menuset_but_browser);
		//字体大小监听
		mMenuSetFont.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder1=new Builder(getActivity());
				builder1.setTitle("字体大小");
				final String[] items=new String[]{"小","中","大"};
				builder1.setSingleChoiceItems(items, 1, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(getActivity(), "您选择的字体为:"+items[which], 1).show();
						switch (which) {
						case 0:
							JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.FONT_TYPE, JBLPreference.FONT_MIN);
						//	((BaseFragActivity)getActivity()).navigateTo(MainPageFragment.class,null,false,MainPageFragment.TAG);
							break;
						case 1:
							JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.FONT_TYPE, JBLPreference.FONT_MEDIUM);
							//((BaseFragActivity)getActivity()).navigateTo(MainPageFragment.class,null,false,MainPageFragment.TAG);
							break;
						case 2:	
							JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.FONT_TYPE, JBLPreference.FONT_MAX);
							//((BaseFragActivity)getActivity()).navigateTo(MainPageFragment.class,null,false,MainPageFragment.TAG);
							break;
						default:
							break;
						}
						
					}
				});
				builder1.setNegativeButton("取消",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {				
					}
				});
				builder1.create().show();
				
			}
		});
		//屏幕亮度监听
		mMenuSetIntensity.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BrightnessSettings bs=new BrightnessSettings();
				bs.showPopSeekBrightness(getActivity());
				Toast.makeText(getActivity(), "您选择了:1", 1).show();
			}
		});
		//关于我们监听
		mMenuSetAbout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
				dialog.show();
				Window window = dialog.getWindow();
				window.setContentView(R.layout.activity_about);
				Toast.makeText(getActivity(), "您选择了:2", 1).show();
			}
		});
		//清除数据监听
		mMenuSetClear.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "您选择了:3", 1).show();
			}
		});
		//恢复默认设置设置监听
		mMentSetSetting.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "您选择了:4", 1).show();
			}
		});
		//监听设置默认浏览器滑动开关
		mMenuSettingsSb.SetOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				//mMenuSetBrowser.setText(CheckState ? "　默认浏览器打开" : "　默认浏览器关闭");
				
			}
		});
		return view;
	}
}
