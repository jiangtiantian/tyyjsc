package com.hemaapp.tyyjsc.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.hemaapp.tyyjsc.model.User;


/**
 * 用户信息数据库帮助类
 */
public class UserDBHelper extends BaseDBHelper {
    String tableName = USER;


    String columns = "token," +
            "id," +
            "username," +
            "password," +
            "paypassword," +
            "feeaccount," +
            "card_feeaccount" +
            ",consumfee," +
            "score," +
            "signdays," +
            "signscore," +
            "experience_value," +
            "recommend_code," +
            "nickname," +
            "sex," +
            "charindex," +
            "mobile," +
            "avatar," +
            "avatarbig," +
            "onlineflag," +
            "validflag," +
            "vestflag," +
            "lastsigntime," +
            "lng," +
            "lat," +
            "deviceid," +
            "devicetype," +
            "chanelid," +
            "lastlogintime," +
            "lastloginversion," +
            "thirdtype," +
            "thirduid," +
            "content" +
            ",regdate," +
            "bankuser," +
            "bankname," +
            "bankcard," +
            "bankaddress," +
            "alipay," +
            "is_sign," +
            "friendflag" +
            ",qrcode, " +
            "download," +
            "level," +
            "dxmoney," +
            " pointcoupon, " +
            "convertmoney," +
            "exchangemoney," +
            " invitenum," +
            " allconvertmoney," +
            " allinvitenum, " +
            "allsalemoney, " +
            "is_max，" +
            "pointcoupon_money";

    String updateColumns = "token=?,id=?,username=?,password=?,paypassword=?,feeaccount=?," +
            "card_feeaccount=?,consumfee=?,score=?,signdays=?,signscore=?,experience_value=?,  " +
            "recommend_code=?,   nickname=?,  sex=?,   charindex=?,  mobile=?,  avatar=?,  avatarbig=?,  " +
            "onlineflag=?,  validflag=?,  vestflag=?,  lastsigntime=?,  lng=?,  lat=?,  deviceid=?,  " +
            "devicetype=?,  chanelid=?,  lastlogintime=?,  lastloginversion=?,  thirdtype=?,  " +
            "thirduid=?,  content=?,  regdate=?,  bankuser=?,  bankname=?,  bankcard=?, " +
            " bankaddress=?,  alipay=?,  is_sign=?,  friendflag=?, qrcode=?, download=?,level=?,dxmoney=?, " +
            "pointcoupon=?, convertmoney=?, exchangemoney=?, invitenum=?, allconvertmoney=?, allinvitenum=?, " +
            "allsalemoney=?, is_max=? ，pointcoupon_money=?";

    /**
     * 实例化系统初始化信息数据库帮助类
     *
     * @param context
     */
    public UserDBHelper(Context context) {
        super(context);
    }

    public boolean insertOrUpdate(User user) {
        if (isExist(user)) {
            return update(user);
        } else {
            return insert(user);
        }
    }

    /**
     * 插入一条记录
     *
     * @return 是否成功
     */

    public boolean insert(User user) {
        String sql = "insert into "
                + tableName
                + " ("
                + columns
                + ") values (?,?,?,?,?,?,?,?,?,?," +
                            "?,?,?,?,?,?,?,?,?,?," +
                            "?,?,?,?,?,?,?,?,?,?," +
                            "?,?,?,?,?,?,?,?,?,?," +
                            "?,?,?,?,?,?,?,?,?,?," +
                            "?,?,?)";
        Object[] bindArgs = new Object[]{user.getToken(), user.getId(), user.getUsername(),
                user.getPassword(), user.getPaypassword(), user.getFeeaccount(),
                user.getDebitaccound(), user.getConsumfee(), user.getScore(),
                user.getSignday(), user.getSignscore(), user.getExperience_value(),
                user.getRecommend_code(), user.getNickname(), user.getSex(), user.getCharindex(),
                user.getMobile(), user.getAvatar(), user.getAvatarbig(),
                user.getOnlineflag(), user.getValidflag(), user.getVestflag(),
                user.getLastsigntime(), user.getLng(), user.getLat(),
                user.getDeviceid(), user.getDevicetype(), user.getChanelid(),
                user.getLastlogintime(), user.getLastloginversion(),
                user.getThirdtype(), user.getThirduid(), user.getContent(),
                user.getRegdate(), user.getBankuser(),
                user.getBankname(), user.getBankcard(),
                user.getBankaddress(), user.getAlipay(), user.getIs_Sign(),
                user.getFriendflag(), user.getQrcode(), user.getDownload(), user.getLevel(), user.getDxmoney(),
                user.getPointcoupon(), user.getConvertmoney(), user.getExchangemoney(), user.getInvitenum(),
                user.getAllconvertmoney(), user.getAllinvitenum(), user.getAllsalemoney(), user.getIs_max(),
                user.getPointcoupon_money(),
                };

        SQLiteDatabase db = getWritableDatabase();
        boolean success = true;
        try {
            db.execSQL(sql, bindArgs);
        } catch (SQLException e) {
            success = false;
        }
        db.close();
        return success;
    }

    /**
     * 更新
     *
     * @return 是否成功
     */
    public boolean update(User user) {
        String conditions = " where id=" + user.getId();
        String sql = "update " + tableName + " set " + updateColumns
                + conditions;
        Object[] bindArgs = new Object[]{user.getToken(), user.getId(), user.getUsername(),
                user.getPassword(), user.getPaypassword(), user.getFeeaccount(),
                user.getDebitaccound(), user.getConsumfee(), user.getScore(),
                user.getSignday(), user.getSignscore(), user.getExperience_value(),
                user.getRecommend_code(), user.getNickname(), user.getSex(),
                user.getMobile(), user.getCharindex(), user.getAvatar(), user.getAvatarbig(),
                user.getOnlineflag(), user.getValidflag(), user.getVestflag(),
                user.getLastsigntime(), user.getLng(), user.getLat(),
                user.getDeviceid(), user.getDevicetype(), user.getChanelid(),
                user.getLastlogintime(), user.getLastloginversion(),
                user.getThirdtype(), user.getThirduid(), user.getContent(),
                user.getRegdate(), user.getBankuser(), user.getBankname(), user.getBankcard(),
                user.getBankaddress(), user.getAlipay(), user.getIs_Sign(), user.getFriendflag(),
                user.getQrcode(), user.getDownload(), user.getLevel(), user.getDxmoney(),
                user.getPointcoupon(), user.getConvertmoney(), user.getExchangemoney(), user.getInvitenum(),
                user.getAllconvertmoney(), user.getAllinvitenum(), user.getAllsalemoney(), user.getIs_max(),
                user.getPointcoupon_money(),
                user.getPointcoupon()};

        SQLiteDatabase db = getWritableDatabase();
        boolean success = true;
        try {
            db.execSQL(sql, bindArgs);
        } catch (SQLException e) {
            success = false;
        }
        db.close();
        return success;
    }

    public boolean isExist(User user) {
        String id = user.getId();
        String sql = ("select * from " + tableName + " where id=" + id);
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        boolean exist = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exist;
    }

    /**
     * 删除用户信息
     *
     * @param user
     */
    public void delUser(User user) {
        String id = user.getId();
        String sql = "delete from " + tableName + " where id = " + id;
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql);
        } catch (SQLException e) {
        }
        db.close();
    }

    /**
     * 清空
     */
    public void clear() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + tableName);
        db.close();
    }

    /**
     * 判断表是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + tableName, null);
        boolean empty = 0 == cursor.getCount();
        cursor.close();
        db.close();
        return empty;
    }

    /**
     * @return 用户信息
     */
    public User select(String username) {
        String conditions = " where username='" + username + "'";
        String sql = "select " + columns + " from " + tableName + conditions;

        SQLiteDatabase db = getWritableDatabase();
        User user = null;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            user = new User(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7),
                    cursor.getString(8), cursor.getString(9),
                    cursor.getString(10), cursor.getString(11),
                    cursor.getString(12), cursor.getString(13),
                    cursor.getString(14), cursor.getString(15),
                    cursor.getString(16), cursor.getString(17),
                    cursor.getString(18), cursor.getString(19),
                    cursor.getString(20), cursor.getString(21),
                    cursor.getString(22), cursor.getString(23),
                    cursor.getString(24), cursor.getString(25),
                    cursor.getString(26), cursor.getString(27),
                    cursor.getString(28), cursor.getString(29),
                    cursor.getString(30), cursor.getString(31),
                    cursor.getString(32), cursor.getString(33),
                    cursor.getString(34), cursor.getString(35),
                    cursor.getString(36), cursor.getString(37),
                    cursor.getString(38), cursor.getString(39),
                    cursor.getString(40), cursor.getString(41),
                    cursor.getString(42), cursor.getString(43),
                    cursor.getString(44), cursor.getString(45),
                    cursor.getString(46), cursor.getString(47),
                    cursor.getString(48), cursor.getString(49),
                    cursor.getString(50), cursor.getString(51),
                    cursor.getString(52)
            );
        }
        cursor.close();
        db.close();
        return user;
    }
}
