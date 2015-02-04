package com.jbl.browser.utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager.OnCancelListener;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import com.jbl.browser.R;

/** 
 * 系统亮度设置 
 *  
 * @author qiw 
 * @version 1.0, 2013-9-13 
 */  
public class BrightnessSettings {  
    private static final String TAG = BrightnessSettings.class.getSimpleName();  
    static Context context;  
  
    /** 
     * 获得当前系统的亮度模式 
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度 
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度 
     */  
    public static int getBrightnessMode() {  
        int brightnessMode = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;  
        try {  
            brightnessMode = Settings.System.getInt(context.getContentResolver(),  
                Settings.System.SCREEN_BRIGHTNESS_MODE);  
        } catch (Exception e) {  
            Log.e(TAG, "获得当前屏幕的亮度模式失败：", e);  
        }  
        return brightnessMode;  
    }  
  
    /** 
     * 设置当前系统的亮度模式 
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度 
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度 
     */  
    public static void setBrightnessMode(int brightnessMode) {  
        try {  
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, brightnessMode);  
        } catch (Exception e) {  
            Log.e(TAG, "设置当前屏幕的亮度模式失败：", e);  
        }  
    }  
  
    /** 
     * 获得当前系统的亮度值： 0~255 
     */  
    public static int getSysScreenBrightness() {  
        int screenBrightness = MAX_BRIGHTNESS;  
        try {  
            screenBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);  
        } catch (Exception e) {  
            Log.e(TAG, "获得当前系统的亮度值失败：", e);  
        }  
        return screenBrightness;  
    }  
  
    /** 
     * 设置当前系统的亮度值:0~255 
     */  
    public static void setSysScreenBrightness(int brightness) {  
        try {  
            ContentResolver resolver = context.getContentResolver();  
            Uri uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);  
            Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness);  
            resolver.notifyChange(uri, null); // 实时通知改变  
        } catch (Exception e) {  
            Log.e(TAG, "设置当前系统的亮度值失败：", e);  
        }  
    }  
  
    /** 
     * 设置屏幕亮度，这会反映到真实屏幕上 
     *  
     * @param activity 
     * @param brightness 
     */  
    public static void setActScreenBrightness(final Activity activity, final int brightness) {  
        final WindowManager.LayoutParams lp = activity.getWindow().getAttributes();  
        lp.screenBrightness = brightness / (float) MAX_BRIGHTNESS;  
        activity.getWindow().setAttributes(lp);  
    }  
  
    /** 
     * 显示调节亮度的设置对话框 
     *  
     * @param act 
     */  
    public static void showBrightnessSettingsDialog(final Activity act) {  
        if (act == null) {  
            return;  
        }  
        final int oldBrightness = getSysScreenBrightness(); // 当前系统的亮度  
        final int oldBrightnessMode = getBrightnessMode(); // 当前系统的亮度模式  
        boolean autoMode = (oldBrightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC); // 是否自动调节亮度模式  
        LayoutInflater inflater = act.getLayoutInflater();  
        View dialogLayout = inflater.inflate(R.layout.seekbar_dialog, null);  
        CheckBox cbAutoMode = (CheckBox) dialogLayout.findViewById(R.id.cb_auto_mode); // 自动调节亮度  
        cbAutoMode.setChecked(autoMode);  
        final SeekBar seekbar = (SeekBar) dialogLayout.findViewById(R.id.seekbar); // 手动调节亮度  
        int progress = oldBrightness - MIN_BRIGHTNESS; // SeekBar的值范围：0~225，代表的亮度值是30~255。  
        seekbar.setProgress(progress < 0 ? 0 : progress);  
        seekbar.setMax(MAX_BRIGHTNESS - MIN_BRIGHTNESS); // 最大值：225  
        // 自动调节亮度选项改变时  
        cbAutoMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
            @Override  
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {  
                if (isChecked) {  
                    seekbar.setVisibility(View.GONE);  
                    setBrightnessMode(Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);  
                } else {  
                    seekbar.setVisibility(View.VISIBLE);  
                    setBrightnessMode(Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);  
                }  
            }  
        });  
        // 手动调节亮度滑块滑动时  
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {  
            @Override  
            public void onStopTrackingTouch(SeekBar seekBar) {  
            }  
  
            @Override  
            public void onStartTrackingTouch(SeekBar seekBar) {  
            }  
  
            @Override  
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {  
                int brightness = progress + MIN_BRIGHTNESS; // 实际亮度  
                // 亮度滑块滑动时，实时改变屏幕亮度  
                setActScreenBrightness(act, brightness); // 改变当前屏幕亮度  
                setSysScreenBrightness(brightness); // 改变系统屏幕亮度  
            }  
        });  
        // 是否显示手动调节亮度滑块  
        if (autoMode) {  
            seekbar.setVisibility(View.GONE);  
        }  
        // 显示调节亮度的dialog  
        AlertDialog.Builder builder = new AlertDialog.Builder(act);  
        builder.setTitle("调节亮度");  
        builder.setView(dialogLayout);  
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int whichButton) {  
                // 点击确定按钮，恢复activity亮度设置  
                setActScreenBrightness(act, -MAX_BRIGHTNESS);  
            }  
        });  
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int whichButton) {  
                // 点击取消按钮，则需要还原亮度模式和亮度值的设置  
                recoverBrightnessSetting(act, oldBrightnessMode, oldBrightness);  
            }  
        });  
        /*builder.setOnCancelListener(new OnCancelListener() { // 点击返回键/dialog外部等让dialog消失的事件  
            @Override  
            public void onCancel(DialogInterface dialog) {  
                recoverBrightnessSetting(act, oldBrightnessMode, oldBrightness);  
            }
        });  */
        builder.show();  
    }  
  
    /** 
     * 还原亮度模式和亮度值的设置 
     *  
     * @param act 
     * @param brightnessMode 
     * @param brightness 
     */  
    private static void recoverBrightnessSetting(final Activity act, final int brightnessMode, final int brightness) {  
        setBrightnessMode(brightnessMode);  
        setSysScreenBrightness(brightness);  
        setActScreenBrightness(act, -MAX_BRIGHTNESS);  
    }  
  
    /** 可调节的最小亮度值 */  
    public static final int MIN_BRIGHTNESS = 30;  
    /** 可调节的最大亮度值 */  
    public static final int MAX_BRIGHTNESS = 255;  
  
}  