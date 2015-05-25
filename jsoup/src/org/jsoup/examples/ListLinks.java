package org.jsoup.examples;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Example program to list links from a URL.
 */
public class ListLinks {
	
	static public boolean OUTSIDE = false;
    public static void main(String[] args) throws IOException {
        //Validate.isTrue(args.length == 1, "usage: supply url to fetch");
        //String url = "http://f.youku.com/player/getFlvPath/sid/130258503437697_01/st/flv/fileid/03000201004DA277E47AC503E2C7936B543E15-DFED-EE85-31E0-869A7BE2F5B8?K=457eb55affadc5c5161b9af9&hd=0";
        		//"https://www.baidu.com/link?url=K5IaijMC_ik14ZjwDy7kv1RzWYOCN14W3Gw6PQhCkDbHNzUXk3jsY8fvm2GGsGq_KL3r6fGD7zgRDY9navaeyK&wd=android%20%E8%8E%B7%E5%8F%96%20%C2%A0%E9%87%8D%E5%AE%9A%E5%90%91url&issp=1&f=8&ie=utf-8&tn=baiduhome_pg&oq=android%20huoqu&inputT=17622&sug=android%20%E8%8E%B7%E5%8F%96%20acname";
        //"http://www.baidu.com";//args[0];
        //print("Fetching %s...", url);
        
        String str="http://s.click.taobao.com/t_8?e=7HZ6jHSTbIWe2i09Xwsn1aVJaeeIzzaXjLq3EUj6DdQ00A%3D%3D&pid=mm_14507426_0_0&unid=&etaosource=0A0A799A6A0A0";
		URL mUrl = new URL(str);
		HttpURLConnection conn=(HttpURLConnection)mUrl.openConnection();
		conn.getResponseCode();
		String realUrl=conn.getURL().toString();
		System.out.println("真实URL:"+realUrl);
		
		/***
		 * 
		 * 解析重定向url
		 */
		str=OUTSIDE?"http://211.142.211.10/?wlanacname=1087.0731.731.00&wlanuserip=10.70.111.73&ssid=CMCC-EDU":realUrl;
		print("解析url参数");
		
		String wanlanacname=null,wlanuserip=null,ssid=null;
		Pattern pattern = Pattern.compile("wlanacname=([^&]*)(&|\\b)");
		Matcher matcher =pattern.matcher(str);
		if(matcher.find()){
		     wanlanacname=matcher.group(0).replace("wlanacname=", "").replace("&", "");
		     print("wanlanacname  = %s",wanlanacname);
		}
		
		pattern = Pattern.compile("wlanuserip=([^&]*)(&|\\b)");
		matcher =pattern.matcher(str);
		if(matcher.find()){
		     wlanuserip=matcher.group(0).replace("wlanuserip=", "").replace("&", "");
		     print("wlanuserip  = %s",wlanuserip);
		}
		
		pattern = Pattern.compile("ssid=([^&]*)(&|\\b)");
		matcher =pattern.matcher(str);
		if(matcher.find()){
		     ssid=matcher.group(0).replace("ssid=", "").replace("&", "");
		     print("ssid  = %s",ssid);
		}

		/***
		 * 
		 * 抓取域名
		 */
        String domain="http://211.142.211.10/suiexingclient.jsp";
		File file = new File("resource/cmcc_edu_test.html");
		InputStream in = OUTSIDE?null:conn.getInputStream();   //通过输入流获得网站数据   
        
		try {
            // 一次读一个字节
            if(OUTSIDE)
                in = new FileInputStream(file);
            byte[] getData = readInputStream(in);     //获得网站的二进制数据  
            String data = new String(getData, OUTSIDE?"utf-8":"gb2312");  
            print("登录网页");
            print(data);
            Document doc = Jsoup.parse(data);//Jsoup.connect(url).get();
            Elements inputs = doc.select("input");
            for (Element input : inputs) {
            	if(input.attr("name").equals("url")){
            		domain=input.attr("value");
            		break;
            	}
            }
    	    print("网页抓取域名 %s", domain);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            print("网页抓取域名 %s", domain);
            return;
        }

		conn.disconnect();
		
		/**
		 * post 登录请求
		 */
		String USER="test_cxw",PWD="cxw@013";
		String param="USER=" + USER + "&PWD=" + PWD + "&wlanacname=" + wanlanacname
				+ "&wlanuserip=" + wlanuserip + "&actiontype=" + "LOGIN"
				+ "&pwdtype=" + "1" + "&clienttype=" + ""
				+ "&forceflag=" + "1";
		try {
			sendPostRequest(domain, param );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
    
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        while((len = inputStream.read(buffer)) != -1) {  
            bos.write(buffer, 0, len);  
        }  
          
        bos.close();  
        return bos.toByteArray();  
    }  
    
    public static String sendPostRequest(String domain,String content) throws Exception {
        
    	URL url=new URL(domain);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         
        conn.setConnectTimeout(1000);
        conn.setReadTimeout(10000000);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
         
        OutputStream output = conn.getOutputStream();
         
        output.write(content.getBytes());
        output.flush();
        output.close();
         
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuffer buffer = new StringBuffer("");
         
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
         
        String result = buffer.toString();
         
        result = URLDecoder.decode(result, "UTF-8");
        print("登录返回页面");
        print(result);
        return result;
    }
    
    
//	public static String doPost(String url, Map<String, String> params) {
//		/* 建立HTTPost对象 */
//		HttpPost httpRequest = new HttpPost(url);
//		/*
//		 * NameValuePair实现请求参数的封装
//		 */
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("u", "沈大海"));
//		params.add(new BasicNameValuePair("p", "123"));
//		try {
//			/* 添加请求参数到请求对象 */
//			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//			/* 发送请求并等待响应 */
//			HttpResponse httpResponse = new DefaultHttpClient()
//					.execute(httpRequest);
//			/* 若状态码为200 ok */
//			if (httpResponse.getStatusLine().getStatusCode() == 200) {
//				/* 读返回数据 */
//				String strResult = EntityUtils.toString(httpResponse
//						.getEntity());
//			} else {
//			}
//		} catch (ClientProtocolException e) {
//			mTextView1.setText(e.getMessage().toString());
//			e.printStackTrace();
//		} catch (IOException e) {
//			mTextView1.setText(e.getMessage().toString());
//			e.printStackTrace();
//		} catch (Exception e) {
//			mTextView1.setText(e.getMessage().toString());
//			e.printStackTrace();
//		}
//	}
    
//	/**
//	 * Http URL重定向
//	 */
//	private static void redirect02() {
//		DefaultHttpClient httpclient = null;
//		String url = "http://hotels.ctrip.com/hotel/hong-kong58";
//		try {
//			httpclient = new DefaultHttpClient();
//			httpclient.setRedirectStrategy(new RedirectStrategy() {
//
//				@Override
//				public HttpUriRequest getRedirect(HttpRequest arg0,
//						HttpResponse arg1, HttpContext arg2)
//						throws ProtocolException {
//					// TODO Auto-generated method stub
//					return null;
//				}
//
//				@Override
//				public boolean isRedirected(HttpRequest arg0,
//						HttpResponse arg1, HttpContext arg2)
//						throws ProtocolException {
//					// TODO Auto-generated method stub
//					return false;
//				}	//设置重定向处理方式
//
//
//			});
//
//			// 创建httpget.
//			HttpGet httpget = new HttpGet(url);
//			// 执行get请求.
//			HttpResponse response = httpclient.execute(httpget);
//
//			int statusCode = response.getStatusLine().getStatusCode();
//			if (statusCode == HttpStatus.SC_OK) {
//				// 获取响应实体
//				HttpEntity entity = response.getEntity();
//				if (entity != null) {
//					// 打印响应内容长度
//					System.out.println("Response content length: "
//							+ entity.getContentLength());
//					// 打印响应内容
//					System.out.println("Response content: "
//							+ EntityUtils.toString(entity));
//				}
//			} else if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY
//					|| statusCode == HttpStatus.SC_MOVED_PERMANENTLY) {
//				
//				System.out.println("当前页面发生重定向了---");
//				
//				Header[] headers = response.getHeaders("Location");
//				if(headers!=null && headers.length>0){
//					String redirectUrl = headers[0].getValue();
//					System.out.println("重定向的URL:"+redirectUrl);
//					
//					redirectUrl = redirectUrl.replace(" ", "%20");
//					get(redirectUrl);
//				}
//			}
//
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			// 关闭连接,释放资源
//			httpclient.getConnectionManager().shutdown();
//		}
//	}
//
//	/**
//	 * 发送 get请求
//	 */
//	private static void get(String url) {
//
//		HttpClient httpclient = new DefaultHttpClient();
//
//		try {
//			// 创建httpget.
//			HttpGet httpget = new HttpGet(url);
//			System.out.println("executing request " + httpget.getURI());
//			// 执行get请求.
//			HttpResponse response = httpclient.execute(httpget);
//			
//			// 获取响应状态
//			int statusCode = response.getStatusLine().getStatusCode();
//			if(statusCode==HttpStatus.SC_OK){
//				// 获取响应实体
//				HttpEntity entity = response.getEntity();
//				if (entity != null) {
//					// 打印响应内容长度
//					System.out.println("Response content length: "
//							+ entity.getContentLength());
//					// 打印响应内容
//					System.out.println("Response content: "
//							+ EntityUtils.toString(entity));
//				}
//			}
//			
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			// 关闭连接,释放资源
//			httpclient.getConnectionManager().shutdown();
//		}
//	}


}
