package com.jbl.browser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import com.jbl.browser.activity.WIFIService;
import com.jbl.browser.utils.JBLPreference;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.os.Build;

/**
 * 自定义application
 * 全局类
 * @author Desmond
 *
 */
public class JBLApplication extends Application {

	public static JBLApplication instance;
	private List<Activity> mList = new LinkedList<Activity>();
	private boolean isEntering=false;//程序进入标示
	
	public static JBLApplication getInstance(){
		return instance;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		instance=this;
		this.startService(new Intent(this, WIFIService.class));
		setEntering(true);
		this.startService(new Intent(this,WIFIService.class));
		super.onCreate();
	}
	
    /**
     * add Activity  
     * @param activity
     */
    public void addActivity(Activity activity) { 
        mList.add(activity); 
    } 
    
	/**
	 * 退出进程
	 */
	public void quit(){
		this.stopService(new Intent(this, WIFIService.class));
		clearDataBeforeQuit();
		try { 
            for (Activity activity : mList) { 
                if (activity != null) 
                    activity.finish(); 
            } 
            
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE); 
        	if (Build.VERSION.SDK_INT < 8){
        		am.restartPackage(getPackageName());
        		System.exit(0); 
        	}
			Method method = am.getClass().getMethod("killBackgroundProcesses",new Class[] {String.class });
		    method.invoke(am,"com.jbl.browser");    
		    
        } catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (Exception e) { 
        	
        } finally { 
            System.exit(0); 
        } 
		
	}
	
    public void onLowMemory() { 
        super.onLowMemory();     
        System.gc(); 
    }
	/**
	 * 退出进程前清除数据
	 */
	public void clearDataBeforeQuit(){
		JBLPreference.getInstance(this).writeString(JBLPreference.BOOKMARK_HISTORY_KEY,null);
	}
	public boolean isEntering() {
		return isEntering;
	}
	public void setEntering(boolean isEntering) {
		this.isEntering = isEntering;
	}
}
