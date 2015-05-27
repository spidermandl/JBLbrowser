package com.jbl.browser.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
import com.jbl.browser.utils.BrightnessSettings;
import com.jbl.browser.utils.JBLPreference;
import com.jbl.browser.utils.StringUtils;
import com.jbl.browser.view.ToggleImageView;

/**
 * 菜单设置选项fragment
 */
public class MenuSettingFragment extends SherlockFragment {
	
	public final static String TAG="MenuSettingFragment";
	//菜单设置选项内容 1 字体大小 2屏幕亮度 3默认浏览器 4 关于我们 5 清除数据 6恢复默认设置
	private RelativeLayout defBrowser; 
	private ToggleImageView mMenuSettingbrowse;
	private RelativeLayout mMenuSetFont,mMenuSetIntensity,mMenuSetAbout;
	private TextView mMenuSetClear,mMentSetSetting;
	private AlertDialog dialog;
	private Context context;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menuset_fragment, container, false);
		final TextView fontSize=(TextView)view.findViewById(R.id.font_valuse);
		mMenuSetFont=(RelativeLayout)view.findViewById(R.id.font_size);
		mMenuSetIntensity=(RelativeLayout)view.findViewById(R.id.screen_brightness);
		mMenuSetAbout=(RelativeLayout)view.findViewById(R.id.about_us);
		mMenuSetClear=(TextView)view.findViewById(R.id.clear_data);
		mMentSetSetting=(TextView)view.findViewById(R.id.restore_settings);
		mMenuSettingbrowse=(ToggleImageView)view.findViewById(R.id.settings_default);
		defBrowser=(RelativeLayout)view.findViewById(R.id.default_browser);
		TextView font_valuse=(TextView)view.findViewById(R.id.font_valuse);
		int fontValuse=JBLPreference.getInstance(getActivity()).readInt(JBLPreference.FONT_TYPE);
		
		if(fontValuse==0)
			font_valuse.setText(StringUtils.FONT_MIN);
		if(fontValuse==1)
			font_valuse.setText(StringUtils.FONT_MID);
		if(fontValuse==2)
			font_valuse.setText(StringUtils.FONT_MAX);

		//字体大小监听
		mMenuSetFont.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final AlertDialog.Builder builder1=new Builder(getActivity());
				builder1.setTitle("字体大小");
				final String[] items=new String[]{"小","中","大"};
	
				builder1.setSingleChoiceItems(items, 1, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.FONT_TYPE, JBLPreference.FONT_MIN);
							fontSize.setText(items[which]);
							Toast.makeText(getActivity(), "您选择的字体大小为："+items[which], 100).show();
							dialog.dismiss();	
							break;
						case 1:
							fontSize.setText(items[which]);
							JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.FONT_TYPE, JBLPreference.FONT_MEDIUM);
							Toast.makeText(getActivity(), "您选择的字体大小为："+items[which], 100).show();
							dialog.dismiss();
							break;
						case 2:	
							fontSize.setText(items[which]);
							JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.FONT_TYPE, JBLPreference.FONT_MAX);
							Toast.makeText(getActivity(), "您选择的字体大小为："+items[which], 100).show();
							dialog.dismiss();
							break;
						default:
							break;
						}
						if(JBLPreference.getInstance(context).readInt(JBLPreference.HOST_URL_BOOLEAN)==JBLPreference.ISNOT_HOST_URL){
						new MainPageFragment().initmWebViewSize();//不是主页时动态改变字体大小
						}
					}
				});
				builder1.setNegativeButton("取消",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
				dialog=builder1.create();
				dialog.show();
				
			}
		});	
		//屏幕亮度监听
		mMenuSetIntensity.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//JBLPreference.getInstance(getActivity()).writeInt(BoolType.BRIGHTNESS_TYPE.toString(),JBLPreference.NIGHT_MODEL);
				BrightnessSettings.showPopSeekBrightness(getActivity());
				
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
			}
		});
		//清除数据监听
		mMenuSetClear.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		//恢复默认设置设置监听
		mMentSetSetting.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		//监听设置默认浏览器滑动开关
		defBrowser.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMenuSettingbrowse.setToggle();
			}
		});
		return view;
	}
}
