package com.jbl.browser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.ActivityManager;
import android.app.Application;
import android.os.Build;

/**
 * 自定义application
 * 全局类
 * @author Desmond
 *
 */
public class JBLApplication extends Application {

	public static JBLApplication instance;
	
	public static JBLApplication getInstance(){
		return instance;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		instance=this;
		super.onCreate();
	}
	
	/**
	 * 推出进程
	 */
	public void quit(){
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE); 
    	if (Build.VERSION.SDK_INT < 8){
    		am.restartPackage(getPackageName());
    		System.exit(0); 
    	}
    	else{
			Method method;
			try {
				method = am.getClass().getMethod("killBackgroundProcesses",new Class[] {String.class });
				method.invoke(am,"com.jbl.browser");
	   		} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}	
    		//am.killBackgroundProcesses("com.diary.goal.setting"); 
    	}
	}
}
