package com.jbl.browser.tools;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.jbl.browser.model.ErrorInfo;
import com.jbl.browser.utils.StringUtils;
import com.jbl.browser.utils.UrlUtils;

/**
 * 处理http请求的工具类，该类的实例必须在主线程中获取
 * 
 * */
public class HttpTool {

	private final String TAG = "HttpTool";
	/**服务器返回的状态码*/
	public final static String STATUS_CODE = "status_code";
	/**服务器返回的结果*/
	public final static String RESPONSE = "response";
	
	private static HttpTool httpTool;
	
	private DefaultHttpClient httpClient = null;
	
	private HttpTool() {

	}
	
	/**
	 * 获取网络通信工具的实例
	 * 
	 * */
	public static HttpTool getInstance() {
		if(httpTool == null){
			httpTool = new HttpTool();
		}
		return httpTool;
	}
	
	/**
	 * 释放资源
	 * */
	public static void destroy(){
		httpTool = null;
	}
	
	/**
	 * 使用post请求，往指定地址传输数据
	 * 
	 * @param context 上下文
	 * 
	 * @param url 指定地址
	 * 
	 * @param params 数据键值对
	 * 
	 * @param encoding 编码方式
	 * 
	 * @throws ErrorInfo 请求失败的异常
	 * 
	 * @return 请求成功后返回的数据
	 * 
	 * */
	public String postData(Context context,String url,List<NameValuePair> params,String encoding) throws ErrorInfo{
		HttpPost httpPost = null;
		HttpResponse httpResponse = null;
		String response = null;
		try {
			if(httpClient == null){
				httpClient = new DefaultHttpClient();
			}
			httpPost  = new HttpPost(url);
			httpPost.setHeader("User-Agent", String.format("%s/%s (Linux; Android %s; %s Build/%s)", "hai1", "dalu", "1.0", "D2C", "1"));
			//httpPost.setHeader("HTTP_USER_AGENT", String.format("%s/%s (Linux; Android %s; %s Build/%s)", "hai1", "dalu", "1.0", "D2C", "1"));
			httpPost.setEntity(new UrlEncodedFormEntity(params, encoding));
			httpResponse = httpClient .execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == 200) {
    			response = EntityUtils.toString(httpResponse.getEntity(),encoding);
    			if(StringUtils.DEBUG){
    				Log.v(TAG, "请求结果：" + response);
    			}
//    			Header[] headers = httpResponse.getAllHeaders();
//    			Log.e(TAG, "headers元素数量：" +  headers.length);
//    			for(int i = 0;i < headers.length;i++){
//    				Log.e(TAG, headers[i].getName() + ":" + headers[i].getValue());
//    			}
    			if(response.contains("500")){
    				return response;
    			}
    			//保存cookie到webview
    			List<Cookie> cookies = httpClient.getCookieStore().getCookies(); 
    			if(!cookies.isEmpty()){
    				CookieSyncManager.createInstance(context);  
    			    CookieManager cookieManager = CookieManager.getInstance();  
    				String cookie = null;
    				for(int i = 0;i < cookies.size();i++){
    					cookie = cookies.get(i).getName() + "=" + cookies.get(i).getValue() 
    							+ ";domain=" + cookies.get(i).getDomain();
        				if(StringUtils.DEBUG){
        					Log.v(TAG,"保存cookie:" + cookie);
        	        	}
        				cookieManager.setCookie(UrlUtils.URL_HEAD, cookie);
        			}
    			}
    			
    			return response;
    		}
			else{
				if(StringUtils.DEBUG){
					Log.v(TAG, "请求失败：" + statusCode + httpResponse.getStatusLine().getReasonPhrase());
    			}
				throw new Exception(new ErrorInfo("请求失败", statusCode));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ErrorInfo(e);
			
		}finally{
			httpPost = null;
			httpResponse = null;
			response = null;
		}
	}
	
}
