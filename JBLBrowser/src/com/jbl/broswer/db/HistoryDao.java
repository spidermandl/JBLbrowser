package com.jbl.broswer.db;

import java.sql.SQLException;
import orm.sqlite.db.DatabaseHelper;
import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.jbl.broswer.bean.History;
/*
 * 创建History的Dao
 * 在这个Dao文件中对history表进行操作
 */
public class HistoryDao {
	private Context context;
	private Dao<History, Integer> HistoryDaoOpe;
	private DatabaseHelper helper;
	
	public HistoryDao(Context context){
		this.context = context;
		try
		{
			helper = DatabaseHelper.getHelper(context);
			HistoryDaoOpe = helper.getDao(History.class);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
