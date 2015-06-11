package com.jbl.browser.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.jbl.browser.model.ErrorInfo;
import com.jbl.browser.utils.UrlUtils;

public class Test {

	public static void main(String[] args) {
		try {
			String j="{\"op\":\"get_wifi\",\"data\":{\"stime\":1433776332,\"province\":\"\u6e56\u5357\",\"wifiname\":\"cmcc-edu\",\"mobile\":\"13585871125\"},\"sign\":\"4d05e32cf4913902b00d12367b8f9348\"}";
			JSONObject json = new JSONObject(j);
			JSONObject data = json.getJSONObject("data");
			StringBuffer sign=new StringBuffer();
			sign.append("mobile="+data.getString("data")+"&");
			sign.append("province="+URLDecoder.decode(data.getString("province"),HTTP.UTF_8)+"&");
			//加密不用url encode
			sign.append("stime="+ data.getLong("stime")+"&");
			sign.append("wifiname="+"cmcc-edu");
			json.put("data", data);

			String key = md5(sign.toString() + data.getLong("stime") + md5(data.getString("data") + data.getLong("stime")) + data.getString("data")+ "wifi").substring(8, 25);
			json.put("sign", key);
		} catch (NoSuchAlgorithmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static private String md5(String input) throws NoSuchAlgorithmException{
		MessageDigest bmd5 = MessageDigest.getInstance("MD5");
		bmd5.update(input.getBytes());
		StringBuffer buf = new StringBuffer();
		byte[] b = bmd5.digest();
		for (int offset = 0; offset < b.length; offset++) {
			int i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}
}
