/*
 * Zirco Browser for Android
 * 
 * Copyright (C) 2010 - 2011 J. Devauchelle and contributors.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.jbl.browser.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.Browser;

public class ZircoBookmarksContentProvider extends ContentProvider {
	
	public static final String AUTHORITY = "org.zirco.providers.zircobookmarkscontentprovider";
	
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zirco.bookmarks";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.zirco.bookmarks";
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "bookmarks.db";
	
	public static final String BOOKMARKS_TABLE = "bookmarks";
	
	private static final String BOOKMARKS_TABLE_CREATE = "CREATE TABLE " + BOOKMARKS_TABLE + " (" + 
			Browser.BookmarkColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			Browser.BookmarkColumns.TITLE + " TEXT, " +
			Browser.BookmarkColumns.URL + " TEXT, " +
			Browser.BookmarkColumns.VISITS + " INTEGER, " +
			Browser.BookmarkColumns.DATE + " LONG, " +
			Browser.BookmarkColumns.CREATED + " LONG, " +
			Browser.BookmarkColumns.BOOKMARK + " INTEGER, " +
			Browser.BookmarkColumns.FAVICON + " BLOB DEFAULT NULL);";
	
	private static final int BOOKMARKS = 1;
	private static final int BOOKMARKS_BY_ID = 2;
	
	private static final UriMatcher sUriMatcher;
	
	private SQLiteDatabase mDb;
	private DatabaseHelper mDbHelper;
	
	private Context mContext;
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, BOOKMARKS_TABLE, BOOKMARKS);
		sUriMatcher.addURI(AUTHORITY, BOOKMARKS_TABLE + "/#", BOOKMARKS_BY_ID);
	}
	
	@Override
	public boolean onCreate() {
		mContext = getContext();
		mDbHelper = new DatabaseHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();
		return true;
	}

	@Override
	public int delete(Uri uri, String whereClause, String[] whereArgs) {
		int count = 0;
		
		switch (sUriMatcher.match(uri)) {
		case BOOKMARKS:
			count = mDb.delete(BOOKMARKS_TABLE, whereClause, whereArgs);
			break;
			
		default: throw new IllegalArgumentException("Unknown URI " + uri);
		}		
		
		if (count > 0) {
			mContext.getContentResolver().notifyChange(uri, null);
		}
		
		return count;
	}
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int count = 0;
		switch (sUriMatcher.match(uri)) {
		case BOOKMARKS:
			count = mDb.update(BOOKMARKS_TABLE, values, selection, selectionArgs);
			break;
			
		default: throw new IllegalArgumentException("Unknown URI " + uri);
		}
						
		if (count > 0) {
			mContext.getContentResolver().notifyChange(uri, null);
		}
		
		return count;
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(BOOKMARKS_TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }		
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

}
