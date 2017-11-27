package com.hemaapp.tyyjsc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import xtom.frame.util.XtomLogger;

/**
 *
 */
public class BaseDBHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "demo.db";
    /**
     * 系统初始化信息
     */
    protected static final String SYSINITINFO = "sysinfo";
    /**
     * 当前登录用户信息
     */
    protected static final String USER = "user";
    /**
     * 访问城市缓存信息
     */
    protected static final String VISIT_CITYS = "visit_citys";
    /**
     * 搜索词缓存
     */
    protected static final String SYS_CASCADE_SEARCH = "sys_cascade_search";

    /**
     *足迹
     */
    protected  static final String HISTORY = "history";

    public BaseDBHelper(Context context) {
        super(context, DBNAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建系统初始化信息缓存表
        String sys = "sys_web_service text,sys_plugins text,sys_show_iospay text, android_must_update text,"
                + "android_last_version text, iphone_must_update text, iphone_last_version text,"
                + "sys_chat_ip text, sys_chat_port text,sys_pagesize text,"
                + "sys_service_phone text,android_update_url text,"
                + "iphone_update_url text,apad_update_url text,ipad_update_url text,"
                + "iphone_comment_url text,msg_invite text, start_img text, sys_service_qq text";
        String sysSQL = "create table " + SYSINITINFO
                + " (id integer primary key," + sys + ")";
        // 创建系统初始化信息缓存表
        db.execSQL(sysSQL);

        // 创建当前登录用户信息缓存表
        String user = "token text, id text,username text,password text,paypassword text,feeaccount text,card_feeaccount text," +
                "consumfee text,score text,signdays text,  signscore text,  experience_value text,  recommend_code text,  " +
                "nickname text,  sex text,   charindex text,  mobile text,  avatar text,  avatarbig text,  onlineflag text, " +
                " validflag text,  vestflag text,  lastsigntime text,  lng text,  lat text,  deviceid text,  devicetype text, " +
                " chanelid text,  lastlogintime text,  lastloginversion text,  thirdtype text,  thirduid text,  content text, " +
                " regdate text,  bankuser text,  bankname text,  bankcard text,  bankaddress text,  alipay text,  " +
                "is_sign text,  friendflag text, qrcode text, download text,level text,dxmoney text";
        String userSQL = "create table " + USER + " (" + user + ")";
        db.execSQL(userSQL);
       // 创建访问城市缓存表
        String citys = "id text primary key,name text,parentid text,nodepath text,"
                + "namepath text,charindex text,level text,orderby text";
        String citysSQL = "create table " + VISIT_CITYS + " (" + citys + ")";
        db.execSQL(citysSQL);
        // 创建搜索词缓存表
        String search = "searchname text, mark text";
        String searchSQL = "create table " + SYS_CASCADE_SEARCH + " (" + search
                + ")";
        db.execSQL(searchSQL);

        //创建经典分类缓存表
        String sql = "id text,name text,imgurl text,price text,old_price text,sum_volume text, username text";
        String sortSQL = "create table " + HISTORY + "(" + sql + ")";
        db.execSQL(sortSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j) {
        /**
         * 如果数据库存在手机中，旧数据库版本号和当前最新安装的数据库版本号做比较，更加版本号进行先删除表再创建表
         * 如果数据库尚未存在手机中，则执行onCreate方法创建数据库，不再执行onUpgrade方法
         */
        if (i == 1 && j == 2) {
            XtomLogger.w("onUpgrade", "缓存数据库升级1-->2");
            String sql = "drop table " + HISTORY;
            sqlitedatabase.execSQL(sql);
            // 重新 创建用户足迹表
            String trackSql = "id text,name text,imgurl text,price text,old_price text,sum_volume text, username text";
            String userSQL = "create table " + HISTORY + " (" + trackSql + ")";
            sqlitedatabase.execSQL(userSQL);
        }
        if(i == 2 && j == 3){
            XtomLogger.w("onUpgrade", "缓存数据库升级2-->3");
            String sql = "drop table " + USER;
            sqlitedatabase.execSQL(sql);
            //重新创建信息表
            String user = "token text, id text,username text,password text,paypassword text,feeaccount text,card_feeaccount text," +
                    "consumfee text,score text,signdays text,  signscore text,  experience_value text,  recommend_code text,  " +
                    "nickname text,  sex text,   charindex text,  mobile text,  avatar text,  avatarbig text,  onlineflag text, " +
                    " validflag text,  vestflag text,  lastsigntime text,  lng text,  lat text,  deviceid text,  devicetype text, " +
                    " chanelid text,  lastlogintime text,  lastloginversion text,  thirdtype text,  thirduid text,  content text, " +
                    " regdate text,  bankuser text,  bankname text,  bankcard text,  bankaddress text,  alipay text,  " +
                    "is_sign text,  friendflag text, qrcode text, download text,level text,dxmoney text";
            String userSQL = "create table " + USER + " (" + user + ")";
            sqlitedatabase.execSQL(userSQL);
        }

        if(i == 3 && j == 4){
            XtomLogger.w("onUpgrade", "缓存数据库升级3-->4");
            String sql = "drop table " + USER;
            sqlitedatabase.execSQL(sql);
            //重新创建信息表
            String user = "token text, id text,username text,password text,paypassword text,feeaccount text,card_feeaccount text," +
                    "consumfee text,score text,signdays text,  signscore text,  experience_value text,  recommend_code text,  " +
                    "nickname text,  sex text,   charindex text,  mobile text,  avatar text,  avatarbig text,  onlineflag text, " +
                    " validflag text,  vestflag text,  lastsigntime text,  lng text,  lat text,  deviceid text,  devicetype text, " +
                    " chanelid text,  lastlogintime text,  lastloginversion text,  thirdtype text,  thirduid text,  content text, " +
                    " regdate text,  bankuser text,  bankname text,  bankcard text,  bankaddress text,  alipay text,  " +
                    "is_sign text,  friendflag text, qrcode text, download text,level text,dxmoney text, pointcoupon text, " +
                    "convertmoney text, exchangemoney text, invitenum text, allconvertmoney text, allinvitenum text, allsalemoney text, " +
                    "is_max text ";
            String userSQL = "create table " + USER + " (" + user + ")";
            sqlitedatabase.execSQL(userSQL);
        }
    }
}
