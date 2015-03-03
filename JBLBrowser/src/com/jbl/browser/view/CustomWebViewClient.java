package com.jbl.browser.view;
import com.jbl.browser.R;
import com.jbl.browser.activity.MultipageActivity;
import com.jbl.browser.utils.ApplicationUtils;
import com.jbl.browser.utils.Constants;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CustomWebViewClient extends WebViewClient{
private MultipageActivity mMultipageActivity;
	
	public CustomWebViewClient(MultipageActivity mainActivity) {
		super();
		mMultipageActivity = mainActivity;
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {			
		((CustomWebView) view).notifyPageFinished();
		mMultipageActivity.onPageFinished(url);
		
		super.onPageFinished(view, url);
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		
		// Some magic here: when performing WebView.loadDataWithBaseURL, the url is "file:///android_asset/startpage,
		// whereas when the doing a "previous" or "next", the url is "about:start", and we need to perform the
		// loadDataWithBaseURL here, otherwise it won't load.
		if (url.equals(Constants.URL_ABOUT_START)) {
			view.loadDataWithBaseURL("file:///android_asset/startpage/",
					ApplicationUtils.getStartPage(view.getContext()), "text/html", "UTF-8", "about:start");
		}
		
		((CustomWebView) view).notifyPageStarted();
		
		
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(view.getResources().getString(R.string.Commons_SslWarningsHeader));
		sb.append("\n\n");
		
		
		
		ApplicationUtils.showContinueCancelDialog(view.getContext(),
				android.R.drawable.ic_dialog_info,
				view.getResources().getString(R.string.Commons_SslWarning),
				sb.toString(),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						handler.proceed();
					}

				},
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						handler.cancel();
					}
		});
}
	}
