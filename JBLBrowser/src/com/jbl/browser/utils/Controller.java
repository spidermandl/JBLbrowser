//package com.jbl.browser.utils;
//
//import java.util.List;
//
//import com.jbl.browser.view.CustomWebView;
//
//import android.content.SharedPreferences;
//
//public final class Controller {
//	private SharedPreferences mPreferences;
//	private List<CustomWebView> mWebViewList;
//	private List<String> mAdBlockWhiteList = null;
//	private List<String> mMobileViewUrlList = null;
//	
//	/**
//	 * Holder for singleton implementation.
//	 */
//	private static final class ControllerHolder {
//		private static final Controller INSTANCE = new Controller();
//		/**
//		 * Private Constructor.
//		 */
//		private ControllerHolder() { }
//	}
//	
//	/**
//	 * Get the unique instance of the Controller.
//	 * @return The instance of the Controller
//	 */
//	public static Controller getInstance() {
//		return ControllerHolder.INSTANCE;
//	}
//	
//	/**
//	 * Private Constructor.
//	 */
//	
//		
//	/**
//	 * Get the list of current WebViews.
//	 * @return The list of current WebViews.
//	 */
//	public List<CustomWebView> getWebViewList() {
//		return mWebViewList;
//	}
//	
//	/**
//	 * Set the list of current WebViews.
//	 * @param list The list of current WebViews.
//	 */
//	public void setWebViewList(List<CustomWebView> list) {
//		mWebViewList = list;
//	}
//	
//	/**
//	 * Get a SharedPreferences instance.
//	 * @return The SharedPreferences instance.
//	 */
//	public SharedPreferences getPreferences() {
//		return mPreferences;
//	}
//
//	/**
//	 * Set the SharedPreferences instance.
//	 * @param preferences The SharedPreferences instance.
//	 */
//	public void setPreferences(SharedPreferences preferences) {
//		this.mPreferences = preferences;
//	}
//	
//	/**
//	 * Get the current download list.
//	 * @return The current download list.
//	 */
//	
//	
//	/**
//	 * Add an item to the download list.
//	 * @param item The new item.
//	 */
//	
//	
//	/**
//	 * Get the list of white-listed url for the AdBlocker.
//	 * @param context The current context.
//	 * @return A list of String url.
//	 */	
//	
//	
//	/**
//	 * Reset the AdBlock white list, so that it will be reloaded.
//	 */
//	public void resetAdBlockWhiteList() {
//		mAdBlockWhiteList = null;
//	}	
//	
//	/**
//	 * Get the list of mobile view urls.
//	 * @param context The current context.
//	 * @return A list of String url.
//	 */
//	
//	/**
//	 * Reset the mobile view url list, so that it will be reloaded.
//	 */
//	public void resetMobileViewUrlList() {
//		mMobileViewUrlList = null;
//	}
//	
//}
//
