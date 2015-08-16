package com.zxw.madaily.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xzwszl on 2015/8/8.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "mad.db";
    private static final int DB_VERSION = 1;
    //news table
    private static final String CREATE_NEWS_TABLE = "CREATE TABLE table_news(" +
            "date VARCHAR(20) PRIMARY KEY NOT NULL," +
            "content TEXT NOT NULL);";

    //themes table
    private static final String CREATE_THEME_TABLE = "CREATE TABLE table_themes(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "kid INTEGER NOT NULL," +
            "content TEXT NOT NULL);";

    //content table
    private static final String CREATE_CONTENT_TABLE = "CREATE TABLE table_content(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "kid INTEGER NOT NULL," +
            "content TEXT NOT NULL);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_CONTENT_TABLE);
        db.execSQL(CREATE_NEWS_TABLE);
        db.execSQL(CREATE_THEME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS table_news");
            db.execSQL("DROP TABLE IF EXISTS table_themes");
            db.execSQL("DROP TABLE IF EXISTS table_content");

            onCreate(db);
        }
    }

}
