package com.jbl.browser.utils;

import java.io.File;
import java.util.HashMap;

import com.jbl.browser.tools.HttpTool;

import android.content.Context;
import android.os.Environment;

public class UrlUtils {
	
	public static boolean TEST = false;
	/**测试地址*/
	public static String URL_TEST_HOST = "http://218.104.200.106:8002";

	/**服务器地址存储的url*/
	public static final String URL_GET_HOST =  "http://m.hi2345.net/home.php";//取正式服地址
			                                   //"http://114.141.132.167/url.php";//取测试服地址
	/**版本检查url*/
	public static final String URL_VERSION_CHECK = "http://m.hi2345.net/download/version.htm";
	/**cmcc 下线url*/
	public static final String URL_CMCC_LOGOUT = "http://211.142.211.10/suiexingclient.jsp";
	//http://m.hi2345.net/login.php
	/**手机验证url*/
	public static final String URL_AUTH = "http://m.hi2345.net/userreg.php?cid=";
	/**个人中心url*/
	public static final String URL_PERSONAL_MANAGEMENT = "http://m.hi2345.net/personalct.php?cid=";
	/**api post请求域名*/
	public static final String URL_API_DOMAIN = "http://m.hi2345.net/api/";
	
	
	public static final String HOTPOT_NAME="CMCC-EDU";//"CMCC-WEB";//"CMCC-EDU";
	public static final LOCATION WIFI_LOCATION=LOCATION.CHANGSHA;
	public static enum LOCATION{
		SHANGHAI, //上海
		CHANGSHA, //长沙
	};//地点测试宏
	/**
	 * 地点码
	 */
	public static final HashMap<LOCATION,String> LOCATION_CODE=new HashMap<UrlUtils.LOCATION, String>(){
		{
			put(LOCATION.SHANGHAI, "上海");
			put(LOCATION.CHANGSHA, "湖南");
		}
	};
	/**
	'01' => '北京',
	'02' => '天津',
	'03' => '河北',
	'04' => '山西',
	'05' => '内蒙古',
	'06' => '辽宁',
	'07' => '吉林',
	'08' => '黑龙江',
	'09' => '上海',
	'10' => '江苏',
	'11' => '浙江',
	'12' => '安徽',
	'13' => '福建',
	'14' => '江西',
	'15' => '山东',
	'16' => '河南',
	'17' => '湖北',
	'18' => '湖南',
	'19' => '广东',
	'20' => '广西',
	'21' => '海南',
	'22' => '四川',
	'23' => '贵州',
	'24' => '云南',
	'25' => '西藏',
	'26' => '陕西',
	'27' => '甘肃',
	'28' => '青海',
	'29' => '宁夏',
	'30' => '新疆',
	'31' => '重庆'
	**/
	/**服务器资源路径*/
	public static String URL_HEAD2 =  null;
	/**android服务器host*/
	public static String URL_HEAD =  null;
	
	/**注册页对应的url*/
	public static String URL_REGISTER ="app-account";  
	/**登录页对应的url*/
	public static String URL_LOGIN = "http://m.hi2345.net/login.php"; 
	/**修改名称页对应的url*/
	public static String URL_UPDATENAME = "player/profile/updt-team"; 
	/**游戏首页对应的url*/
	public static String URL_HOME_PAGE = "top/index.tpl";
	/**游戏首页*/
	public static String URL_TOP = "top/";
	/**我的页面*/
	public static String URL_PAGE = "app-my";
	/**召唤*/
	public static String URL_CALL = "app-gacha";
	/**故事模式*/
	public static String URL_STORY = "app-quest";
	/**决斗*/
	public static String URL_FIGHT = "app-battle";
	
	/**分享奖励获得页：卡片*/
	public static String URL_REWARD_CARD = "gacha/gpoint/add-card";
	/**分享奖励获得页：友情pt*/
	public static String URL_REWARD_PT = "gacha/gpoint/add-gpoint";
	/**分享奖励获得页：道具*/
	public static String URL_REWARD_ITEM = "gacha/gpoint/add-product";
	/**支付交易号请求*/
	public static String URL_TRADE_NO="gacha/getgems/getorderid";
	/**支付交易回调地址*/
	public static String URL_PAYMENT_CALLBACK="http://118.126.10.219:8112/index.php/order/confirm";
	/**购买页*/
	public static String URL_GET_GEMS = "my/apple/index";
	
	/**获取临时凭据的地址*/
	public static String URL_GET_TEMP_TOKEN = "gacha/gettemptoken/get-temp";
	/**获取凭据的地址*/
	public static String URL_GET_TOKEN = "gacha/getaccesstoken/get-access";
	/**购买验证地址*/
	public static String URL_CHECK = "gacha/getgems/get-gems";
	
	/**下载页*/
	public static final String URL_DOWNLOAD= "http://t.cn/zjX5pA1";
	
}
