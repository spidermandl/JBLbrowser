package com.jbl.browser.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import com.jbl.browser.JBLApplication;
import com.jbl.browser.db.UserInfoDao;
import com.jbl.browser.model.ErrorInfo;
import com.jbl.browser.utils.UrlUtils;
import com.jbl.browser.utils.UrlUtils.LOCATION;

/**
 * wifi验证工具方法类
 * @author osondesmond
 *
 */
public class WIFITool {

	String location = null;
	String[] set_cookie3 = new String[9];
	String wlanacname = null; 
	String wlanuserip = null;
	String logonsessid = null;
	
	
	private static WIFITool instance=null;
	private WIFITool(){
		
	}
	
	public static WIFITool getInstance(){
		if(instance==null){
			instance=new WIFITool();
		}
		
		return instance;
	}
	
	/**
	 * 获取免费wifi登录api
	 * @return
	 */
	public String requestWifiAccount(String url,String mobile,long stime){
		/**
			数据指令：
			op：get_wifi
			数据内容：
			data：
			    province 省份，汉字，见省份列表
			    wifiname 热点信号名称
			    mobile 手机号码
			    stime Unix时间戳(1970-01-01至今的秒数)

			数据签名：
			sign：

			数据返回：
			{"action":"ok","ret":0,"op":"get_wifi","data":{"uid":10001,"mobile":"13962988888","times":1100,"stime":1400000000,"wifiid":123456,"account":"account","pass":"pass","chid":4},"sign":"32位md5值"}
		**/
		try {
			JSONObject json=new JSONObject();
			json.put("op", "get_wifi");
			JSONObject data=new JSONObject();
			StringBuffer sign=new StringBuffer();
			data.put("mobile", mobile);
			sign.append("mobile="+mobile+"&");
			String lcode=UrlUtils.LOCATION_CODE.get(UrlUtils.WIFI_LOCATION);
			data.put("province",lcode);
			sign.append("province="+lcode+"&");
			data.put("stime", stime);
			sign.append("stime="+stime+"&");
			data.put("wifiname", wlanacname);
			sign.append("wifiname="+wlanacname);
			json.put("data", data);
			
			String key= md5(sign.toString()+stime+md5(mobile+stime)+mobile+"wifi").substring(8, 25);
			json.put("sign", key);
			String result = HttpTool.getInstance().postData(url, json.toString(), HTTP.UTF_8);
			return result;
		} catch (NoSuchAlgorithmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}  catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (ErrorInfo e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 执行登陆验证所有流程
	 * @param reqUrl
	 * @return
	 */
	public boolean loginCheck(String reqUrl){
		return isDefaultPage(reqUrl);
	}
	

	/**
	 * 同步免费wifi用时
	 * @param url 访问连接
	 * @param type 类型:start,连接成功;stop,断开连接;heartbeat,连接心跳
	 * @param wifi_id WIFI帐号编号
	 * @param uid 用户编号
	 * @param mobile 手机号码
	 * @param stime  时间戳
	 * @param times Wifi使用时长，秒数，可以理解为上次心跳间隔时间
	 */
	public String sendSyncTime(String url,String type,String wifi_id,String uid,String mobile,long stime,long times){
		/**
		 * 数据指令： op：heartbeat 数据内容： data： type
		 * 类型:start,连接成功;stop,断开连接;heartbeat,连接心跳 wifiid WIFI帐号编号 uid 用户编号
		 * mobile 手机号码 stime Unix时间戳(1970-01-01至今的秒数) times
		 * Wifi使用时长，秒数，可以理解为上次心跳间隔时间
		 * 
		 * 数据签名： sign：
		 * 
		 * 数据返回： {"action":"ok","ret":0,"op":"get_wifi","data":{"type":
		 * "start/stop/heartbeat"
		 * ,"uid":10001,"mobile":"13962988888","times":1200
		 * ,"stime":1400000000},"sign":"32位md5值"}
		 */
		try {
			JSONObject json = new JSONObject();
			json.put("op", "heratbeat");
			JSONObject data = new JSONObject();
			StringBuffer sign = new StringBuffer();
			data.put("mobile", mobile);
			sign.append("mobile=" + mobile + "&");
			data.put("stime", stime);
			sign.append("stime=" + stime + "&");
			data.put("times", times);
			sign.append("times=" + times + "&");
			data.put("type", type);
			sign.append("type=" + type + "&");
			data.put("uid", uid);
			sign.append("uid=" + uid + "&");
			data.put("wifiid", wifi_id);
			sign.append("wifiid=" + wifi_id);
			json.put("data", data);

			String key = md5(sign.toString() + stime + md5(mobile + stime) + mobile+ "wifi").substring(8, 25);
			json.put("sign", key);
			String result = HttpTool.getInstance().postData(url,json.toString(), HTTP.UTF_8);
			return result;
		} catch (NoSuchAlgorithmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ErrorInfo e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取edu默认返回页面的重定向url
	 * 第一次重定向
	 * @param reqUrl
	 * @return
	 */
	private boolean isDefaultPage(String reqUrl) {
		DefaultHttpClient httpclient = new DefaultHttpClient();

		int responseCode = 0;
		try {
			final HttpGet request = new HttpGet(reqUrl);
			HttpParams params = new BasicHttpParams();
			params.setParameter("http.protocol.handle-redirects", false); // 默认不让重定向
			// 这样就能拿到Location头了
			request.setParams(params);
			Log.e("getLocationMethod1", "before execute");
			HttpResponse response = httpclient.execute(request);
			responseCode = response.getStatusLine().getStatusCode();
			Log.e("getLocationMethod1 respond code", responseCode+"");
			if (responseCode == HttpStatus.SC_MOVED_TEMPORARILY
					|| responseCode == HttpStatus.SC_MOVED_PERMANENTLY) {
				Header locationHeader = response.getFirstHeader("Location");
				if (locationHeader != null) {
					Log.e("locationHeader", "!=null");
					location = locationHeader.getValue();
					switch (UrlUtils.WIFI_LOCATION) {
					case SHANGHAI:

					    return isJSredirect(location);
						
					case CHANGSHA:
						Log.e("重复探测网络", "重复探测网络");
						//return getLocationMethod1(location);
						if(location.contains("wlanacname"))
							return parseURLParam(location);
						return false;
					default:
						return isJSredirect(location);
					}
				}
				
				Log.e("locationHeader", "==null");
			}
			
			if(responseCode==200){
				Header[] allHeaders = response.getHeaders("Set-Cookie");
				if(allHeaders.length !=0){
					if(allHeaders[0].toString().contains("baidu.com"))
						return true;
				}
			
			}
		} catch (Exception e) {
			Log.e("验证失败", "isDefaultPage 失败");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 第二次次重定向，Host改变了
	 * @param reqUrl
	 * @return
	 */
	private boolean isJSredirect(String reqUrl) {
		DefaultHttpClient httpclient = new DefaultHttpClient();

		int responseCode = 0;
		try {
			final HttpGet request = new HttpGet(reqUrl);
			request.addHeader("Host", "gd1.wlanportal.chinamobile.com:8080");
			request.addHeader("Connection", "keep-alive");
			request.addHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");
			HttpParams params = new BasicHttpParams();
			params.setParameter("http.protocol.handle-redirects", false); // 默认不让重定向
			// 这样就能拿到Location头了
			request.setParams(params);
			HttpResponse response = httpclient.execute(request);
			responseCode = response.getStatusLine().getStatusCode();

			if (responseCode == HttpStatus.SC_MOVED_TEMPORARILY
					|| responseCode == HttpStatus.SC_MOVED_PERMANENTLY) {
				Header locationHeader = response.getFirstHeader("Location");
				if (locationHeader != null) {
					return isJSredirect(location);
				}

			}
			if (responseCode == 200) { // 这里表示与wifi真正建立有有效连接
				List<Cookie> cookies = httpclient.getCookieStore().getCookies();
				for (int i = 0; i < cookies.size(); i++) {
					set_cookie3[i] = cookies.get(i).getValue().toString();

				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		Log.e("验证失败", "isJSredirect 失败");
		//Toast.makeText(context, "mothed2 失败", 1000).show();
		return false;
	}
	
	/**
	 * 解析重定向url参数
	 * @param str
	 */
	private boolean parseURLParam(String str) {

		String ssid = null;
		Pattern pattern = Pattern.compile("wlanacname=([^&]*)(&|\\b)");
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			wlanacname = matcher.group(0).replace("wlanacname=", "")
					.replace("&", "");

		}

		pattern = Pattern.compile("wlanuserip=([^&]*)(&|\\b)");
		matcher = pattern.matcher(str);
		if (matcher.find()) {
			wlanuserip = matcher.group(0).replace("wlanuserip=", "")
					.replace("&", "");
			 System.out.println(wlanuserip);
		}

		pattern = Pattern.compile("ssid=([^&]*)(&|\\b)");
		matcher = pattern.matcher(str);
		if (matcher.find()) {
			ssid = matcher.group(0).replace("ssid=", "").replace("&", "");
			 System.out.println(ssid);
		}
		final List<NameValuePair> params = new ArrayList<NameValuePair>();
		switch (UrlUtils.WIFI_LOCATION) {
		case SHANGHAI:

			params.add(new BasicNameValuePair("wlanuserip", wlanuserip));
			params.add(new BasicNameValuePair("wlanacname", wlanacname));
			params.add(new BasicNameValuePair("username", "18800399005"));
			params.add(new BasicNameValuePair("password", "NGVQhY9P"));
			params.add(new BasicNameValuePair("forceflag", "1"));

			// get(location);
			// doPost("https://gd1.wlanportal.chinamobile.com:8443/LoginServlet",
			// / params, set_cookie[0]);

			// while (isOK) {// 循环发送登录请求，登录成功后改为isOK=false
			return sendAuth("https://gd1.wlanportal.chinamobile.com:8443/LoginServlet?wlanuserip="
					+ wlanuserip
					+ "&wlanacname="
					+ wlanacname
					+ "&username=18800399005" + "&password=NGVQhY9P");


		case CHANGSHA:
			params.add(new BasicNameValuePair("wlanuserip", wlanuserip));
			params.add(new BasicNameValuePair("wlanacname", wlanacname));
			params.add(new BasicNameValuePair("PWD", "cxw@013"));
			params.add(new BasicNameValuePair("USER", "test_cxw"));
			params.add(new BasicNameValuePair("actiontype", "LOGIN"));
			params.add(new BasicNameValuePair("forceflag", "1"));
			return sendAuth("http://211.142.211.10/suiexingclient.jsp",params);
		default:
			break;
		}
		
		return false;
	}
	
	/**
	 * 发送验证请求
	 * @param url
	 * @return
	 */
	private boolean sendAuth(String url) {

		HttpClient httpclient = new DefaultHttpClient();
		try {
			// 创建httpget.
			HttpGet httpget = new HttpGet(url);
			httpget.addHeader("Cookie", "JSESSIONID=" + set_cookie3[0]);
			httpget.addHeader("Cookie", "cookie_persiste=" + set_cookie3[1]);
			httpget.addHeader("Host", "gd1.wlanportal.chinamobile.com:8443");
			httpget.addHeader("Referer ", location);
			httpget.addHeader("Connection", "keep-alive");
			httpget.addHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");
			HttpParams params = new BasicHttpParams();
			// // 执行get请求.
			HttpResponse response = httpclient.execute(httpget);
			// // 获取响应状态
			int statusCode = response.getStatusLine().getStatusCode();
			Log.e("验证请求结果",statusCode+"");

			if (statusCode == HttpStatus.SC_OK) { // 登录成功后获取响应的实体内容，
				// 解析<span
				// class="loginsuccess">登录成功</span>判断登录成功isOK=false;停止发送登录请求
//				HttpEntity entity = response.getEntity();
//				if (entity != null) { // 打印响应内容长度 //
//					System.out.println("Response content length: "
//							+ entity.getContentLength()); // 打印响应内容
//					System.out.println("Response content: "
//							+ EntityUtils.toString(entity));
//				}
//				List<Cookie> cookies = ((AbstractHttpClient) httpclient)
//						.getCookieStore().getCookies();
//				for (int i = 0; i < cookies.size(); i++) {
//
//					set_cookie3[i] = cookies.get(i).getValue().toString();
//
//				}
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("sendAuth", "请求失败");
		} finally {
			// 关闭连接,释放资源
			httpclient.getConnectionManager().shutdown();
			Log.e("sendAuth", "结果无效");
		}
		
		return false;
		
	}
	
	/**
	 * 发送验证请求
	 * @param url
	 * @param params
	 * @return
	 */
	private boolean sendAuth(String url, List<NameValuePair> params) {
		int responseCode = 0;
		/* 建立HTTPost对象 */
		String strResult = null;
		HttpPost httpRequest = new HttpPost(url);
		Log.e("发送验证请求", url);
		try {
			/* 添加请求参数到请求对象 */

			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			/* 发送请求并等待响应 */
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			/* 若状态码为200 ok */
			Log.e("验证请求返回码", responseCode+"");
			switch (responseCode) {
			case 200:
				/* 读返回数据 */
				strResult = EntityUtils.toString(httpResponse.getEntity());
				byte[] getData = strResult.getBytes(); // 获得网站的二进制数据
				String data = new String(getData, "gb2312");
				//System.out.println(data);
				Document doc = Jsoup.parse(data);// Jsoup.connect(url).get();
				Elements inputs = doc.select("input");
				for (Element input : inputs) {
					if (input.attr("name").equals("logonsessid")) {
						logonsessid = input.attr("value");
						return true;
					}
				}
				
				break;

			default:
				Log.e("sendAuth", "结果无效");
				break;
			}

		} catch (Exception e) {
			Log.e("sendAuth", "请求失败");
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 获取下线认证
	 * */
	public boolean doPostlogout(String url) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("wlanacname", wlanacname));
		params.add(new BasicNameValuePair("wlanuserip", wlanuserip));
		params.add(new BasicNameValuePair("actiontype","LOGOUT"));
		params.add(new BasicNameValuePair("logonsessid", logonsessid));
		int responseCode = 0;
		/* 建立HTTPost对象 */
		String strResult = null;
		HttpPost httpRequest = new HttpPost(url);
		try {
			/* 添加请求参数到请求对象 */

			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			/* 发送请求并等待响应 */
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			/* 若状态码为200 ok */
			switch (responseCode) {
			case 200:
				/* 读返回数据 */
				strResult = EntityUtils.toString(httpResponse.getEntity());
				return true;

			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 计算md5
	 * @param input
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private String md5(String input) throws NoSuchAlgorithmException{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(input.getBytes());
		StringBuffer sb = new StringBuffer();
		byte[] b = md5.digest();
		for (int i = 0; i < b.length; i++) {
			sb.append(b[i]);
		}
		return sb.toString();
	}
	
}
