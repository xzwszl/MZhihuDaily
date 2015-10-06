package com.zxw.madaily.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sony on 2015/9/17.
 */
public class ReadHandler {
    private Context mContext;
    private final String AID = "aid";
    private final String TABLE = "table_read";
    public ReadHandler(Context context) {
        this.mContext = context;
    }

    public Set<Integer> findReadSet() {
        Set<Integer> res = new HashSet<>();
        Cursor cursor = DBHandler.getInstance(mContext).getWritableDatabase()
                .query(TABLE, new String[]{AID}, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()){
           while (!cursor.isAfterLast()) {
               res.add(cursor.getInt(0));
               cursor.moveToNext();
           }
            cursor.close();
        }

        return res;
    }

    public void replaceReadSet(Set<Integer> set) {

        ContentValues cv = new ContentValues();
        for (Integer s : set) {
            cv.put(AID, s);
            DBHandler.getInstance(mContext).getWritableDatabase().replace(TABLE, null, cv);
        }
    }

    public void deleteAll() {
        DBHandler.getInstance(mContext).getWritableDatabase()
                .delete(TABLE , null, null);
    }
}
