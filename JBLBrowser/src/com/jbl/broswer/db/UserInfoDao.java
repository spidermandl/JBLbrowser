package com.jbl.broswer.db;

import java.sql.SQLException;

import orm.sqlite.db.DatabaseHelper;
import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.jbl.broswer.bean.UserInfo;
/*
 * 创建UserInfo的Dao
 * 在这个Dao文件中对userInfo表进行操作
 */
public class UserInfoDao {
	private Context context;
	private Dao<UserInfo, Integer> UserInfoDaoOpe;
	private DatabaseHelper helper;
	
	public UserInfoDao(Context context){
		this.context = context;
		try
		{
			helper = DatabaseHelper.getHelper(context);
			UserInfoDaoOpe = helper.getDao(UserInfo.class);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
