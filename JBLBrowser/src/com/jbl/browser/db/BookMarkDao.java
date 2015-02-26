package com.jbl.browser.db;

import java.sql.SQLException;
import java.util.List;
import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.jbl.browser.bean.BookMark;

/**
 * 创建BookMark的Dao
 * 在这个Dao文件中对bookMark表进行操作
 * huyingying
 */
public class BookMarkDao {
	private Context context;
	private Dao<BookMark, Integer> BookMarkDaoOpe;
	private DatabaseHelper helper;

	public BookMarkDao(Context context) {
		this.context = context;
		try {
			helper = DatabaseHelper.getHelper(context);
			BookMarkDaoOpe = helper.getDao(BookMark.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean addBookMark(BookMark bookmark) {
		boolean flag = false;
		try {
			BookMark bookmark1 = null;
			bookmark1 = BookMarkDaoOpe.queryForSameId(bookmark);
			if (bookmark1 != null) {
				flag = false;
			} else {
				int temp = BookMarkDaoOpe.create(bookmark);
				if (temp != 0) {
					flag = true;
				} else
					flag = false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	/*public int deleteBookMarkByWebAddress(String webAddress) {
		try {
			DeleteBuilder<BookMark, Integer> deleteBuilder = BookMarkDaoOpe
					.deleteBuilder();
			deleteBuilder.where().eq("webAddress", webAddress);
			return deleteBuilder.delete();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}*/
	public int deleteBookMarkById(int id){
		try {
			return BookMarkDaoOpe.deleteById(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public BookMark get(int id) {
		BookMark bookmark = null;
		try {
			bookmark = BookMarkDaoOpe.queryForId(id);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookmark;
	}
	public List<BookMark> queryBookMarkAllByisRecommend(boolean isRecommend){
		List<BookMark> bookmark=null;
		try {
			bookmark=BookMarkDaoOpe.queryForEq("isRecommend", isRecommend);
			return bookmark;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/*public List<BookMark> queryAll() {
		List<BookMark> bookmark = null;
		try {
			bookmark = BookMarkDaoOpe.queryForAll();
			return bookmark;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}*/
}
