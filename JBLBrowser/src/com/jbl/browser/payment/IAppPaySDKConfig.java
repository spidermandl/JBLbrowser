package com.jbl.browser.payment;

import java.util.HashMap;

/**
 *应用接入iAppPay云支付平台sdk集成信息 
 */
public class IAppPaySDKConfig{

	/**
	 * 应用名称：
	 * 应用在iAppPay云支付平台注册的名称
	 */
	public final static  String APP_NAME = "海贼幻想";

	/**
	 * 应用编号：
	 * 应用在iAppPay云支付平台的编号，此编号用于应用与iAppPay云支付平台的sdk集成 
	 */
	public final static  String APP_ID = "300003657";

	/**
	 * 商品编号：
	 * 应用的商品在iAppPay云支付平台的编号，此编号用于iAppPay云支付平台的sdk到iAppPay云支付平台查找商品详细信息（商品名称、商品销售方式、商品价格）
	 * 编号对应商品名称为：海贼币
	 */
	public final static  int WARES_ID_1=1;

	/**
	 * 商品编号：
	 * 应用的商品在iAppPay云支付平台的编号，此编号用于iAppPay云支付平台的sdk到iAppPay云支付平台查找商品详细信息（商品名称、商品销售方式、商品价格）
	 * 编号对应商品名称为：海贼币
	 */
	public final static  int WARES_ID_2=2;

	/**
	 * 商品编号：
	 * 应用的商品在iAppPay云支付平台的编号，此编号用于iAppPay云支付平台的sdk到iAppPay云支付平台查找商品详细信息（商品名称、商品销售方式、商品价格）
	 * 编号对应商品名称为：海贼币
	 */
	public final static  int WARES_ID_3=3;

	/**
	 * 商品编号：
	 * 应用的商品在iAppPay云支付平台的编号，此编号用于iAppPay云支付平台的sdk到iAppPay云支付平台查找商品详细信息（商品名称、商品销售方式、商品价格）
	 * 编号对应商品名称为：海贼币
	 */
	public final static  int WARES_ID_4=4;

	/**
	 * 商品编号：
	 * 应用的商品在iAppPay云支付平台的编号，此编号用于iAppPay云支付平台的sdk到iAppPay云支付平台查找商品详细信息（商品名称、商品销售方式、商品价格）
	 * 编号对应商品名称为：海贼币
	 */
	public final static  int WARES_ID_5=5;

	/**
	 * 商品编号：
	 * 应用的商品在iAppPay云支付平台的编号，此编号用于iAppPay云支付平台的sdk到iAppPay云支付平台查找商品详细信息（商品名称、商品销售方式、商品价格）
	 * 编号对应商品名称为：海贼币
	 */
	public final static  int WARES_ID_6=6;

	/**
	 * 商品编号：
	 * 应用的商品在iAppPay云支付平台的编号，此编号用于iAppPay云支付平台的sdk到iAppPay云支付平台查找商品详细信息（商品名称、商品销售方式、商品价格）
	 * 编号对应商品名称为：海贼币
	 */
	public final static  int WARES_ID_7=7;

	/**
	 * 商品编号：
	 * 应用的商品在iAppPay云支付平台的编号，此编号用于iAppPay云支付平台的sdk到iAppPay云支付平台查找商品详细信息（商品名称、商品销售方式、商品价格）
	 * 编号对应商品名称为：海贼币
	 */
	public final static  int WARES_ID_8=8;

	/**
	 * 应用密钥：
	 * 用于保障应用与iAppPay云支付平台sdk通讯安全及sdk初始化
	 * 此应用密钥只适用集成sdk3.2.35版本及3.2.35以后的版本，3.2.35以前版本请从商户服务平台复制密钥数据（应用密钥、支付公钥、MODKEY）
	 */
	public final static String APP_KEY = "QUU4MDcxQkU0MkZCQkM3RDk5NTMxODIyMTBBMkRBNUZFMEEzQ0Y5RE1UZ3lORFU1T0RrNE9UVTFOekl5TXpFd01ETXJNalUzTVRBeE5UZzFNVGd4TURJME16WTFPRGd6T0RjNE9EWXdOekV6TVRJME9EVTROalF4";
	
	public final static HashMap<String, Integer> IAPPPAIR= new HashMap<String, Integer>(){
		{
			put("id_coin_z_an", WARES_ID_7);
			put("id_coin_a_an", WARES_ID_6);
			put("id_coin_b_an", WARES_ID_5);
			put("id_coin_c_an", WARES_ID_4);
			put("id_coin_d_an", WARES_ID_3);
			put("id_coin_e_an", WARES_ID_2);
			put("id_coin_f_an", WARES_ID_1);
			put("id_coin_g_an", WARES_ID_8);
			
			put("id_coin_z_an"+WARES_ID_7, 388);
			put("id_coin_a_an"+WARES_ID_6, 233);
			put("id_coin_b_an"+WARES_ID_5, 153);
			put("id_coin_c_an"+WARES_ID_4, 78);
			put("id_coin_d_an"+WARES_ID_3, 45);
			put("id_coin_e_an"+WARES_ID_2, 25);
			put("id_coin_f_an"+WARES_ID_1, 6);
			put("id_coin_g_an"+WARES_ID_8, 648);
			
//			put("id_coin_z_an", WARES_ID_1);
//			put("id_coin_a_an", WARES_ID_2);
//			put("id_coin_b_an", WARES_ID_3);
//			put("id_coin_c_an", WARES_ID_4);
//			put("id_coin_d_an", WARES_ID_5);
//			put("id_coin_e_an", WARES_ID_6);
//			put("id_coin_f_an", WARES_ID_7);
//			put("id_coin_g_an", WARES_ID_8);
//			
//			put("id_coin_z_an"+WARES_ID_1, 388);
//			put("id_coin_a_an"+WARES_ID_2, 233);
//			put("id_coin_b_an"+WARES_ID_3, 153);
//			put("id_coin_c_an"+WARES_ID_4, 78);
//			put("id_coin_d_an"+WARES_ID_5, 45);
//			put("id_coin_e_an"+WARES_ID_6, 25);
//			put("id_coin_f_an"+WARES_ID_7, 6);
//			put("id_coin_g_an"+WARES_ID_8, 648);
		}
	};

}