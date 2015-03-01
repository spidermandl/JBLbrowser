package com.jbl.browser.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.R;
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
				// TODO Auto-generated method stub
				
			}
		});
		//屏幕亮度监听
		mMenuSetIntensity.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "您选择了:1", 1).show();
			}
		});
		//关于我们监听
		mMenuSetAbout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
/*	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
			AlertDialog.Builder builder1=new Builder(getActivity());
			builder1.setTitle("字体大小");
			final String[] items=new String[]{"小","中","大"};
			builder1.setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getActivity(), "您选择的字体为:"+items[which], 1).show();
					switch (which) {
					case 0:
						JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.FONT_TYPE, JBLPreference.FONT_MIN);
						((BaseFragActivity)getActivity()).navigateTo(MainPageFragment.class, null, false,MainPageFragment.TAG);
						break;
					case 1:
						JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.FONT_TYPE, JBLPreference.FONT_MEDIUM);
						((BaseFragActivity)getActivity()).navigateTo(MainPageFragment.class, null, false,MainPageFragment.TAG);
						break;
					case 2:	
						JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.FONT_TYPE, JBLPreference.FONT_MAX);
						((BaseFragActivity)getActivity()).navigateTo(MainPageFragment.class, null, false,MainPageFragment.TAG);
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
			break;
		case 1 :
			BrightnessSettings bs=new BrightnessSettings();
			bs.showBrightnessSettingsDialog(getActivity());
			break;
		case 2:
			AlertDialog.Builder builder3=new Builder(getActivity());
			builder3.setTitle("旋转屏幕");
			final String[] items1=new String[]{"跟随系统","锁定竖屏","锁定横屏"};
			builder3.setSingleChoiceItems(items1, 1, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getActivity(), "您选择了:"+items1[which], 1).show();
					switch (which) {
					case 0:
						JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.SCREEN_TYPE, JBLPreference.FOLLOW_SYSTEM);
						break;
					case 1:
						JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.SCREEN_TYPE, JBLPreference.LOCK_VERTAICAL);
						break;
					case 2:	
						JBLPreference.getInstance(getActivity()).writeInt(JBLPreference.SCREEN_TYPE, JBLPreference.LOCK_HORZON);
						break;
					default:
						break;
					}
				}
			});
			builder3.setPositiveButton("取消",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			builder3.create().show();
			break;
		case 3:  //关于浏览器信息
			AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
			dialog.show();
			Window window = dialog.getWindow();
			window.setContentView(R.layout.activity_about);
			break;
		default:
			break;
		}

	}
	
	private String getFontType(){
		switch (JBLPreference.getInstance(getActivity()).readInt(JBLPreference.FONT_TYPE)) {
		case JBLPreference.FONT_MIN:
			return "小";
		case JBLPreference.INVALID:
        case JBLPreference.FONT_MEDIUM:
			BrowserSettings.textSize = WebSettings.TextSize.NORMAL;
			return "中";
        case JBLPreference.FONT_MAX:
	        BrowserSettings.textSize = WebSettings.TextSize.LARGER;
	        return "大";
		default:
			return "中";
		}
	}
	private String getScreenType(){
		switch (JBLPreference.getInstance(getActivity()).readInt(JBLPreference.SCREEN_TYPE)) {
		case JBLPreference.FOLLOW_SYSTEM:
			return "跟随系统";
		case JBLPreference.INVALID:
        case JBLPreference.LOCK_VERTAICAL:
			return "锁定竖屏";
        case JBLPreference.LOCK_HORZON:
	        return "锁定横屏";
		default:
			return "锁定竖屏";
		}
	}*/
}
