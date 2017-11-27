package com.hemaapp.tyyjsc.db;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.hemaapp.tyyjsc.model.GoodsBriefIntroduction;

import java.util.ArrayList;

/**
 * 足迹:根据username存存储遍历删除足迹;
 */
public class HistoryDBHelper extends BaseDBHelper{
    private static HistoryDBHelper mClient;
    @SuppressWarnings("unused")
    private static Context mContext;

    String columns = "id,name,imgurl,price,old_price,sum_volume,username";

    String updateColumns = "id=?,name=?,imgurl=?, price=?,old_price=?, sum_volume=?, username=?";


    private String tableName = HISTORY;

    private HistoryDBHelper(Context context) {
        super(context);
    }

    public static HistoryDBHelper get(Context context) {
        mContext = context;
        return mClient == null ? mClient = new HistoryDBHelper(context)
                : mClient;
    }
    /**
     * @param data
     * @return
     */
    public void insert(ArrayList<GoodsBriefIntroduction> data, String username) {
        SQLiteDatabase db = getWritableDatabase();
        if(data != null && data.size() > 0){
            for (int i = 0; i < data.size(); i++){
                GoodsBriefIntroduction item = data.get(i);
                if (!isExited(item, username)) {//不存在插入
                    try {
                        db.execSQL(
                                ("insert into " + tableName + " (" + columns + ") values (?,?,?,?,?,?,?)"),
                                new Object[]{item.getId(), item.getName(), item.getImgurl(), item.getPrice(), item.getOld_price(), item.getSum_volume(), username});

                    } catch (SQLException e) {
                        Log.d("TAG", "insert: " + e);
                    }
                }else{//存在更新
                    try {
                        String conditions = " where id=" + item.getId() + " and username = " + username;
                        String sql = "update " + tableName + " set " + updateColumns + conditions;
                        Object[] bindArgs = new Object[]{item.getId(), item.getName(), item.getImgurl(), item.getPrice(),item.getOld_price(), item.getSum_volume(), username};
                        try {
                            db.execSQL(sql, bindArgs);
                        } catch (SQLException e) {
                        }
                    } catch (SQLException e) {
                        Log.d("TAG", "insert: 1" + e);
                    }
                }
            }
        }
        db.close();
    }

    /**
     *判断是否存在
     */
    public boolean isExited(GoodsBriefIntroduction item, String username) {
        boolean isExited = false;
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from " + tableName + " where id = ? and username = ?", new String[]{item.getId(), username});
            if (cursor.getCount() > 0) {
                isExited = true;
            }
        } catch (SQLException e) {
            Log.w("select", "select e=" + e);
        }
        return isExited;
    }

    /**
     *清空
     */
    public void clear(String username) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("delete from " + tableName + " where username = " + username);
        } catch (SQLiteException e) {
            Log.w("clear", "clear e=" + e);
        }
        db.close();
    }

    /**
     * 删除一个
     */
    public void del(String id, String username){
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("delete from " + HISTORY + " where id = ? and username = ?", new Object[]{id, username});
        } catch (SQLiteException e) {
            Log.w("del", "del e=" + e);
        }
        db.close();
    }

    /**
     * @return
     */
    public boolean isEmpty(String username) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(tableName, new String[]{"id","name","imgurl","price","old_price","sum_volume","username"}, "username = ?", new String[]{username}, null, null, null);
        int num = cursor.getCount();
        cursor.close();
        db.close();
        return 0 == num;
    }

    /**
     * @return
     */
    public ArrayList<GoodsBriefIntroduction> select(String username) {
        ArrayList<GoodsBriefIntroduction> counts = null;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(tableName, new String[]{"id","name","imgurl","price","old_price","sum_volume","username"}, "username = ?", new String[]{username}, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                counts = new ArrayList<GoodsBriefIntroduction>();
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    GoodsBriefIntroduction item = new GoodsBriefIntroduction(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
                    counts.add(0, item);
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
