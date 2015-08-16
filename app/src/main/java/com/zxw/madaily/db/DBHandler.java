package com.zxw.madaily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xzwszl on 2015/8/11.
 */
public class DBHandler {

    private static SQLiteOpenHelper dbHelper;

    public static SQLiteOpenHelper getInstance(Context context) {
        if (dbHelper == null) {
            synchronized (DBHandler.class) {

                if (dbHelper == null) {
                        dbHelper = new DBHelper(context);
                }
            }
        }
        return dbHelper;
    }

    public static void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
