package com.jbl.browser.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.jbl.browser.BrowserSettings;
import com.jbl.browser.R;
import com.jbl.browser.activity.BaseFragActivity;
import com.jbl.browser.utils.BrightnessSettings;
import com.jbl.browser.utils.JBLPreference;

/**
 * 菜单设置选项fragment
 */
public class MenuSettingFragment extends SherlockFragment implements OnItemClickListener{
	
	public final static String TAG="MenuSettingFragment";
	
	//菜单设置选项内容 1 字体大小 2屏幕亮度 3默认浏览器 4 关于我们 5 清除数据 6恢复默认设置
	private TextView menuset_tv1,menuset_tv2,menuset_tv3,menuset_tv4,menuset_tv5,menuset_tv6;
	private ImageView menuset_imv; //滑动开关
	private RelativeLayout mRelativeLayout; //滑动开关布局点击监听
	//设置开关
	private boolean mFlag=false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menuset_fragment, container, false);
		menuset_tv1=(TextView)view.findViewById(R.id.menuset_tv1);
		menuset_tv2=(TextView)view.findViewById(R.id.menuset_tv2);
		menuset_tv3=(TextView)view.findViewById(R.id.menuset_tv3);
		menuset_tv4=(TextView)view.findViewById(R.id.menuset_tv4);
		menuset_tv5=(TextView)view.findViewById(R.id.menuset_tv5);
		menuset_tv6=(TextView)view.findViewById(R.id.menuset_tv6);
		menuset_imv=(ImageView)view.findViewById(R.id.menuset_imv);
		menuset_imv.getParent().requestDisallowInterceptTouchEvent(true);
		menuset_imv.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(!mFlag){
					menuset_imv.setBackgroundDrawable(getResources().getDrawable(R.drawable.menuset_closed));
					mFlag=true;
					notifyAll();
				}
				if(mFlag){
					menuset_imv.setBackgroundDrawable(getResources().getDrawable(R.drawable.menuset_open));
					mFlag=false;
					notifyAll();	
				}
				return true;
			}
		});
		mRelativeLayout=(RelativeLayout)view.findViewById(R.id.menuset_ll);
		
		return view;
	}
	@Override
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
	}
}
