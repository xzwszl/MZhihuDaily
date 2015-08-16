package com.zxw.madaily.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.zxw.madaily.entity.LatestNews;

/**
 * Created by xzwszl on 2015/8/13.
 */
public class NewsHandler {

    private Context mContext;
    private final String DATE = "date";
    private final String CONTENT = "content";
    private final String TABLE = "table_news";

    public NewsHandler(Context context) {
        mContext = context;
    }

    public String findNewsByDay(String day) {

        String content = null;

        Cursor cursor = DBHandler.getInstance(mContext).getWritableDatabase()
                .query(TABLE, new String[]{CONTENT},DATE + "=?", new String[]{day},null,null,null);

        if (cursor != null){
            if (cursor.getCount() == 1 && cursor.moveToFirst()) {
                content = cursor.getString(cursor.getColumnIndex(CONTENT));
            }
            cursor.close();
        }

        return content;
    }

    //最新的news
    public String findLastNews() {

        String content = null;

        Cursor cursor = DBHandler.getInstance(mContext).getWritableDatabase()
                .rawQuery("SELECT MAX(" + DATE + ")," + CONTENT + " FROM " + TABLE, null);

        if (cursor != null){
            if (cursor.getCount() == 1 && cursor.moveToFirst()) {
                content = cursor.getString(cursor.getColumnIndex(CONTENT));
            }
            cursor.close();
        }

        return content;
    }

    public void insertOrUpdateNewbyDay(String day, String news) {
        ContentValues values = new ContentValues();
        values.put("date",day);
        values.put("content", news);
        DBHandler.getInstance(mContext).getWritableDatabase()
                .replace(TABLE, null, values);
    }

    public void insertNewbyDay(String day, String news) {
        ContentValues values = new ContentValues();
        values.put("date",day);
        values.put("content", news);
        DBHandler.getInstance(mContext).getWritableDatabase()
                .insert(TABLE, null, values);
    }

    public void updateNewsByDay(String day, String news) {
        ContentValues values = new ContentValues();
        values.put("date",day);
        values.put("content", news);
        DBHandler.getInstance(mContext).getWritableDatabase()
                .update(TABLE, values, DATE + "=?", new String[]{day});
    }

    public void deleteNewsByDay(String day) {
        DBHandler.getInstance(mContext).getWritableDatabase()
                .delete(TABLE,DATE + "=?",new String[]{day});
    }

    public void deleteAll() {
        DBHandler.getInstance(mContext).getWritableDatabase()
                .delete(TABLE , null, null);
    }
}
