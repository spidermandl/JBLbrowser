package com.jbl.broswer.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jbl.broswer.bean.BookMark;
import com.jbl.broswer.bean.History;
import com.jbl.broswer.bean.UserInfo;
/*
 * 继承OrmLiteSqliteOpenHelper，实现DatabaseHelper
 * 包括创建数据库，升级数据库
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	//数据库名称
	private static final String DATABASE_NAME="jbl_broswer.db";
	//数据库版本
	private static final int DATABASE_VERSION=1;
	private Map<String, Dao> daos = new HashMap<String, Dao>();
	
	public DatabaseHelper(Context context){
		super(context, DATABASE_NAME, null,DATABASE_VERSION);
	}
	public DatabaseHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion) {
		super(context, databaseName, factory, databaseVersion);
		
	}

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
		try {
			//创建表
			TableUtils.createTable(connectionSource, BookMark.class);
			TableUtils.createTable(connectionSource, History.class);
			TableUtils.createTable(connectionSource, UserInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVersion,
			int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, BookMark.class, true);
			TableUtils.dropTable(connectionSource, History.class, true);			
			TableUtils.dropTable(connectionSource, UserInfo.class, true);			
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
