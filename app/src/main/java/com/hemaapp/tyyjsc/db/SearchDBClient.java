package com.hemaapp.tyyjsc.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

/**
 *搜索缓存表
 */
public class SearchDBClient extends BaseDBHelper {
    private static SearchDBClient mClient;
    @SuppressWarnings("unused")
    private static Context mContext;

    private String tableName = SYS_CASCADE_SEARCH;

    private SearchDBClient(Context context) {
        super(context);
    }

    public static SearchDBClient get(Context context) {
        mContext = context;
        return mClient == null ? mClient = new SearchDBClient(context)
                : mClient;
    }

    /**
     * @param
     * @return
     */
    public boolean insert(String search, String mark) {
        SQLiteDatabase db = getWritableDatabase();
        boolean success = false;
        if (!isExited(search, mark)) {
            success = true;
            try {
                db.execSQL(
                        ("insert into " + tableName + "(searchname,mark) " + "values (?,?)"),
                        new Object[]{search, mark});

            } catch (SQLException e) {
                success = false;
                Log.w("insert", "insert e=" + e);
            }
        }
        db.close();
        return success;
    }

    /**
     *
     */
    public boolean isExited(String search, String mark) {
        boolean isExited = false;
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from " + tableName + " where searchname = ? and mark = ?", new String[]{search, mark});
            if (cursor.getCount() > 0) {
                isExited = true;
            }
        } catch (SQLException e) {
            Log.w("insert", "insert e=" + e);
        }
        return isExited;
    }

    /**
     *
     */
    public void clear(String mark) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("delete from " + tableName + " where mark = ?", new Object[]{mark});
        } catch (SQLiteException e) {
            Log.w("clear", "clear e=" + e);
        }
        db.close();
    }

    /**
     * @return
     */
    public boolean isEmpty(String mark) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + tableName + " where mark = ?", new String[]{mark});
        int num = cursor.getCount();
        cursor.close();
        db.close();
        return 0 == num;
    }

    /**
     * @return
     */
    public ArrayList<String> select(String mark) {
        ArrayList<String> counts = null;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from " + tableName + " where mark = ?", new String[]{mark});
            if (cursor != null && cursor.getCount() > 0) {
                counts = new ArrayList<String>();
                cursor.moveToFirst();
                String count;
                for (int i = 0; i < cursor.getCount(); i++) {
                    count = cursor.getString(0);
                    counts.add(i, count);
                    cursor.moveToNext();
                }
            }

        } catch (Exception e) {
            Log.w("select", "select e=" + e);
        }
        if (cursor != null)
            cursor.close();
        db.close();
        return counts;
    }
}
