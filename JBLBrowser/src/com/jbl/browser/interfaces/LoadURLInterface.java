package com.jbl.browser.interfaces;

import android.webkit.WebView;

/**
 * webview 运行接口
 * @author yyjoy-mac3
 *
 */
public interface LoadURLInterface {

	void startPage(String url);
	void stopPage(WebView view,String url);
}
