package com.jbl.broswer.db;

import java.sql.SQLException;
import orm.sqlite.db.DatabaseHelper;
import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.jbl.broswer.bean.BookMark;
/*
 * 创建BookMark的Dao
 * 在这个Dao文件中对bookMark表进行操作
 */
public class BookMarkDao {
	private Context context;
	private Dao<BookMark, Integer> BookMarkDaoOpe;
	private DatabaseHelper helper;
	
	public BookMarkDao(Context context){
		this.context = context;
		try
		{
			helper = DatabaseHelper.getHelper(context);
			BookMarkDaoOpe = helper.getDao(BookMark.class);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
