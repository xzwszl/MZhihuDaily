package com.zxw.madaily.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sony on 2015/8/15.
 */
public class TableHandler {
    private Context mContext;
    private final String ID = "kid";
    private final String CONTENT = "content";
    public static final String TABLE_THEMES = "table_themes";
    public static final String TABLE_CONTENT = "table_content";
    private String mTable;

    public TableHandler(Context context, String table) {
        mContext = context;
        mTable = table;
    }

    public String findContentById(String id) {

        String content = null;

        Cursor cursor = DBHandler.getInstance(mContext).getWritableDatabase()
                .query(mTable, new String[]{CONTENT},ID + "=?", new String[]{id},null,null,null);

        if (cursor != null){
            if (cursor.getCount() == 1 && cursor.moveToFirst()) {
                content = cursor.getString(cursor.getColumnIndex(CONTENT));
            }
            cursor.close();
        }
        return content;
    }

    public void insertContentById(String id, String content) {
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(CONTENT, content);
        DBHandler.getInstance(mContext).getWritableDatabase()
                .insert(mTable, null, values);
    }

    public void updateContentByIdy(String id, String content) {

        SQLiteDatabase db = DBHandler.getInstance(mContext).getWritableDatabase();

        db.beginTransaction();

        try {
            Cursor cursor = db.query(mTable, new String[]{CONTENT}, ID + "=?", new String[]{id}, null, null, null);

            if (cursor == null || cursor.getCount() <= 0) {
                insertContentById(id, content);
            } else {
                if (cursor != null) cursor.close();
                ContentValues values = new ContentValues();
                values.put(ID, id);
                values.put(CONTENT, content);
                db.update(mTable, values, ID + "=?", new String[]{id});
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
        }
    }

    public void deleteContentByIdy(String id) {
        DBHandler.getInstance(mContext).getWritableDatabase()
                .delete(mTable,ID + "=?",new String[]{id});
    }

    public void deleteAll() {
        DBHandler.getInstance(mContext).getWritableDatabase()
                .delete(mTable , null, null);
    }
}
