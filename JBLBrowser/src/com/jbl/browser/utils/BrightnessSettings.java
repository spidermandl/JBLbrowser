package com.jbl.browser.utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import com.jbl.browser.R;
import com.jbl.browser.activity.MainFragActivity;

/** 
 * 系统亮度设置 
 *  
 * @author qiw 
 * @version 1.0, 2013-9-13 
 */  
public class BrightnessSettings {  
    private static final String TAG = BrightnessSettings.class.getSimpleName();  

    static int brightness;
    static View popview;
    static PopupWindow popWindow;
    /**
     * 判断是否开启了自动亮度调节
     * 
     * @param activity
     * @return
     */ 
    public static boolean isAutoBrightness(Activity activity) { 
        boolean isAutoAdjustBright = false; 
        try { 
            isAutoAdjustBright = Settings.System.getInt( 
                    activity.getContentResolver(), 
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC; 
        } catch (SettingNotFoundException e) { 
            e.printStackTrace(); 
        } 
        return isAutoAdjustBright; 
    } 
   
    /**
     * 获取系统屏幕的亮度
     * 
     * @param activity
     * @return
     */ 
    public static int getScreenBrightness(Activity activity) { 
        int brightnessValue = 0; 
        try { 
            brightnessValue = android.provider.Settings.System.getInt( 
                    activity.getContentResolver(), 
                    Settings.System.SCREEN_BRIGHTNESS); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return brightnessValue; 
    } 
   
    /**
     * 设置屏幕亮度
     * 
     * @param activity
     * @param brightness
     */ 
    public static void setBrightness(Activity activity, int brightness) { 
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes(); 
        if (brightness == -1) {
        	lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
        	lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
         
        activity.getWindow().setAttributes(lp); 
    } 
   
    /**
     * 关闭亮度自动调节
     * 
     * @param activity
     */ 
    public static void stopAutoBrightness(Activity activity) { 
        Settings.System.putInt(activity.getContentResolver(), 
                Settings.System.SCREEN_BRIGHTNESS_MODE, 
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL); 
    } 
   
    /**
     * 开启亮度自动调节
     * 
     * @param activity
     */ 
   
    public static void startAutoBrightness(Activity activity) { 
        Settings.System.putInt(activity.getContentResolver(), 
                Settings.System.SCREEN_BRIGHTNESS_MODE, 
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC); 
    } 
   
    /**
     * 保存亮度设置状态
     * 
     * @param activity
     * @param brightness
     */ 
    public static void saveBrightness(Activity activity, int brightness) { 
        Uri uri = android.provider.Settings.System 
                .getUriFor("screen_brightness"); 
        ContentResolver resolver = activity.getContentResolver(); 
        android.provider.Settings.System.putInt(resolver, "screen_brightness", 
                brightness); 
        resolver.notifyChange(uri, null); 
    }  
    //显示调节夜间模式亮度悬浮窗
    public static void showPopSeekBrightness(final Activity act){
    	 if (act == null) {  
             return;  
         } 
    	 /*// 如果开启了自动亮度调节，则关闭之 
         if (isAutoBrightness(act)) { 
             stopAutoBrightness(act); 
         } */
    	 int oldBrightness=JBLPreference.getInstance(act).readInt(JBLPreference.NIGHT_BRIGHTNESS_VALUS);
    	 int sysScreenBrightness=getScreenBrightness(act);
         LayoutInflater mLayoutInflater=(LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 		 popview=(View)mLayoutInflater.inflate(R.layout.pop_seekbar_brightness, null);
 		 popWindow=new PopupWindow(popview,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);   
         final SeekBar seekbar = (SeekBar)popview.findViewById(R.id.ctrl_skbProgress); // 手动调节亮度  
         int progress = oldBrightness; // SeekBar的值范围：0~255 
         seekbar.setMax(sysScreenBrightness);   //可调节的最大亮度和系统调节的亮度一样
         seekbar.setProgress(progress < 0 ? 0 : progress);  
         setBrightness(act, oldBrightness);    
         // 手动调节亮度滑块滑动时  
         seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {  
             @Override  
             public void onStopTrackingTouch(SeekBar seekBar) { 
            	 //将滑动后的亮度值写入缓存
            	 JBLPreference.getInstance(act).writeInt(JBLPreference.NIGHT_BRIGHTNESS_VALUS, seekBar.getProgress());
             }  
   
             @Override  
             public void onStartTrackingTouch(SeekBar seekBar) {  
             }  
   
             @Override  
             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {  
                 brightness = progress; // 实际亮度   
                 // 亮度滑块滑动时，实时改变屏幕亮度  
                 setBrightness(act, brightness); // 改变当前屏幕亮度
                
             }  
         });  
         // 显示调节亮度的popSeekBar
         popWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
          
    }
    public static void hideSeekBar(){
    	if(popWindow.isShowing()){
    		popWindow.dismiss();
    	}
    }
}  