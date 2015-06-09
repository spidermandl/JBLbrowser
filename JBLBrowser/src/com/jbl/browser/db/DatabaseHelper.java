package com.jbl.browser.db;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jbl.browser.R;
import com.jbl.browser.bean.History;
import com.jbl.browser.bean.UserInfo;
import com.jbl.browser.bean.BookMark;
/*
 * 继承OrmLiteSqliteOpenHelper，实现DatabaseHelper
 * 包括创建数据库，升级数据库
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	//数据库名称
	private static final String DATABASE_NAME="jbl_broswer.db";
	//数据库版本
	private static final int DATABASE_VERSION=2;
	private Map<String, Dao> daos = new HashMap<String, Dao>();
	public DatabaseHelper(Context context){
		super(context, getMyDatabaseName(context), null,DATABASE_VERSION);
	}
	 private static String getMyDatabaseName(Context context){
	        String databasename = DATABASE_NAME;
	        String dbPath = null;
//	        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){//SDCard是否插入
//	            dbPath = Environment.getExternalStorageDirectory().getPath() + "/database/";
//	        }else{//未插入SDCard，建在内存中
	            dbPath = context.getFilesDir().getPath() + "/database/";
//	        }
	        File dbp = new File(dbPath);
	        if(!dbp.exists()){
	            dbp.mkdirs();
	        }
	        databasename = dbPath + DATABASE_NAME;
	        return databasename;
	    }
	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
		try {
			//创建表
			TableUtils.createTable(connectionSource, BookMark.class);
			TableUtils.createTable(connectionSource, History.class);
			TableUtils.createTable(connectionSource, UserInfo.class);
			//初始化表数据
			initData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 第一次运行程序，将推荐网址记录到数据库表bookmark中
	 * @throws SQLException 
	 */
	void initData() throws SQLException{
		Dao<BookMark, Integer> BookMarkDaoOpe =getDao(BookMark.class);
		String[] resWebAddress={"http://m.baidu.com/",
				"http://sina.cn/",
	            "http://3g.163.com",
	            "http://wap.sohu.com",
	            "http://3g.qq.com",
	            "http://m.weibo.cn",
	            "http://m.taobao.com",
	            "http://m.kaixin001.com",
	            "http://mt.renren.com",
	            "http://i.ifeng.com"};
		String[] resWebName={"百度","新浪","网易","搜狐","腾讯","新浪微博","手机淘宝","开心网","人人网","凤凰网"};
		for(int i=0;i<resWebAddress.length;i++){
			BookMark bookmark=new BookMark();
			bookmark.setWebAddress(resWebAddress[i]);
			bookmark.setWebName(resWebName[i]);
			bookmark.setRecommend(true);
			try {
				BookMarkDaoOpe.create(bookmark);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVersion,
			int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, BookMark.class, true);
			TableUtils.dropTable(connectionSource, History.class, true);			
			TableUtils.dropTable(connectionSource, UserInfo.class, true);
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static DatabaseHelper instance;

	/**
	 * 单例获取该Helper
	 * 
	 * @param context
	 * @return
	 */
	public static synchronized DatabaseHelper getHelper(Context context)
	{
		context = context.getApplicationContext();
		if (instance == null)
		{
			synchronized (DatabaseHelper.class)
			{
				if (instance == null)
					instance = new DatabaseHelper(context);
			}
		}

		return instance;
	}

	public synchronized Dao getDao(Class clazz) throws SQLException
	{
		Dao dao = null;
		String className = clazz.getSimpleName();

		if (daos.containsKey(className))
		{
			dao = daos.get(className);
		}
		if (dao == null)
		{
			dao = super.getDao(clazz);
			daos.put(className, dao);
		}
		return dao;
	}

	/**
	 * 释放Dao
	 */
	@Override
	public void close()
	{
		super.close();

		for (String key : daos.keySet())
		{
			Dao dao = daos.get(key);
			dao = null;
		}
	}

}
