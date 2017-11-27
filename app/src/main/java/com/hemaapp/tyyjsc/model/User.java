package com.hemaapp.tyyjsc.model;

import com.hemaapp.hm_FrameWork.HemaUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.exception.DataParseException;

/**
 * 用户信息(注意User信息必须继承HemaUser,并且User中不用再包含token,android_must_update,
 * android_last_version,android_update_url字段)
 */
public class User extends HemaUser implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String id;// 用户主键
    private String username;// 登录名
    private String password;// 登陆密码 服务器端存储的是32位的MD5加密串
    private String paypassword;// 支付密码 服务器端存储的是32位的MD5加密串
    private String feeaccount;// 账户余额
    private String debitaccound;//储值卡余额
    private String consumfee;
    private String score;// 用户积分
    private String sum_score;//累计积分
    private String signdays;//签到天数
    private String signscore;//签到积分
    private String experience_value;//经验值
    private String recommend_code;
    private String pointcoupon;//总点券
    private String convertmoney;  //兑换金
    private String exchangemoney;//可兑换金额


    public String getDebitaccound() {
        return debitaccound;
    }

    public void setDebitaccound(String debitaccound) {
        this.debitaccound = debitaccound;
    }

    private String nickname;// 用户昵称
    private String sex;// 用户性别
    private String charindex;// 用户昵称的汉语拼音首字母索引
    private String mobile;// 手机号码 此为登录手机账户（一旦注册不能更改）
    private String avatar;// 个人主页头像图片（小） 如果为空请显示系统默认头像（小）
    private String avatarbig;// 个人主页头像图片（大） 如果为空请显示系统默认头像（大）
    private String onlineflag;
    private String validflag;// 用户状态标记 0冻结1有效
    private String vestflag;
    private String lastsigntime;//最近签到时间
    private String lng; // 经度
    private String lat; // 纬度
    private String deviceid;// 客户端硬件标识码 等同百度推送userid
    private String devicetype;// 用户客户端类型 1：苹果 2：安卓
    private String chanelid;// 百度推送渠道id
    private String lastlogintime;// 最后一次登录的时间
    private String lastloginversion;// 最后一次登录的版本
    private String thirdtype;
    private String thirduid;
    private String content;
    private String regdate;// 用户注册时间
    private String bankuser;//开户人姓名
    private String bankname;//银行卡名
    private String bankcard;//银行卡
    private String bankaddress;//开户行地址
    private String alipay;
    private String sign;//是否签到
    private String dxmoney;//一个积分可抵现金额
    private String signday;
    private String friendflag;//二维码
    private String full_fee;//满减条件
    private String level;// 	会员等级
    private String mermber_value;//	会员等级进度值
    private String value_info;// 	会员等级范围
    private String commission;//佣金
    private String download;//二维码地址
    private String qrcode;//二维码图片
    private String pointcoupon_money;

    private String invitenum;
    private String allconvertmoney;
    private String allinvitenum;
    private String allsalemoney;
    private String is_max;


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    private ValueInfo valueInfo = null;

    public String getDxmoney() {
        return dxmoney;
    }

    public void setDxmoney(String dxmoney) {
        this.dxmoney = dxmoney;
    }

    public User(JSONObject jsonObject) throws DataParseException {
        super(jsonObject);
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                username = get(jsonObject, "username");
                password = get(jsonObject, "password");
                paypassword = get(jsonObject, "paypassword");
                feeaccount = get(jsonObject, "feeaccount");
                debitaccound = get(jsonObject, "debitaccound");
                consumfee = get(jsonObject, "consumfee");
                score = get(jsonObject, "score");
                signdays = get(jsonObject, "signdays");
                signscore = get(jsonObject, "signscore");
                experience_value = get(jsonObject, "experience_value");
                recommend_code = get(jsonObject, "recommend_code");
                nickname = get(jsonObject, "nickname");
                sex = get(jsonObject, "sex");
                charindex = get(jsonObject, "charindex");
                mobile = get(jsonObject, "mobile");
                avatar = get(jsonObject, "avatar");
                avatarbig = get(jsonObject, "avatarbig");
                onlineflag = get(jsonObject, "onlineflag");
                validflag = get(jsonObject, "validflag");
                vestflag = get(jsonObject, "vestflag");
                lastsigntime = get(jsonObject, "lastsigntime");
                lng = get(jsonObject, "lng");
                lat = get(jsonObject, "lat");
                devicetype = get(jsonObject, "devicetype");
                deviceid = get(jsonObject, "deviceid");
                chanelid = get(jsonObject, "chanelid");
                lastlogintime = get(jsonObject, "lastlogintime");
                lastloginversion = get(jsonObject, "lastloginversion");
                thirdtype = get(jsonObject, "thirdtype");
                thirduid = get(jsonObject, "thirduid");
                content = get(jsonObject, "content");
                regdate = get(jsonObject, "regdate");
                bankuser = get(jsonObject, "bankuser");//开户名
                bankname = get(jsonObject, "bank"); //银行名称
                bankcard = get(jsonObject, "bankcard"); //银行卡号
                bankaddress = get(jsonObject, "bankname"); //支行名称
                alipay = get(jsonObject, "alipayname");
                sign = get(jsonObject, "sign");
                signday = get(jsonObject, "signday");
                level = get(jsonObject, "level");
                mermber_value = get(jsonObject, "mermber_value");
                value_info = get(jsonObject, "value_info");
                commission = get(jsonObject, "commission");
                sum_score = get(jsonObject, "sum_score");
                full_fee = get(jsonObject, "full_fee");
                friendflag = get(jsonObject, "friendflag");
                download = get(jsonObject, "download");//二维码

                qrcode = get(jsonObject, "qrcode");//二维码
                dxmoney = get(jsonObject, "dxmoney");
                pointcoupon = get(jsonObject, "pointcoupon");
                convertmoney = get(jsonObject, "convertmoney");
                exchangemoney = get(jsonObject, "exchangemoney");
                pointcoupon_money = get(jsonObject, "pointcoupon_money");

                invitenum = get(jsonObject, "invitenum");
                allconvertmoney = get(jsonObject, "allconvertmoney");
                allinvitenum = get(jsonObject, "allinvitenum");
                allsalemoney = get(jsonObject, "allsalemoney");
                is_max = get(jsonObject, "is_max");


                if (!isNull(value_info)) {
                    JSONArray array = new JSONArray(value_info);
                    if (array.length() > 0) {
                        valueInfo = new ValueInfo(array.getJSONObject(0));
                    } else {
                        valueInfo = new ValueInfo("0", "0", "0");
                    }
                }
                log_d(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public User(String token, String id, String username, String password, String paypassword, String feeaccount,
                String card_feeaccount, String consumfee, String score, String signdays, String signscore, String experience_value,
                String recommend_code, String nickname, String sex, String charindex, String mobile, String avatar, String avatarbig,
                String onlineflag, String validflag, String vestflag, String lastsigntime, String lng, String lat, String deviceid,
                String devicetype, String chanelid, String lastlogintime, String lastloginversion, String thirdtype, String thirduid,
                String content, String regdate, String bankuser, String bankname, String bankcard, String bankaddress, String alipay,
                String is_sign, String friendflag, String qrcode, String download, String level, String dxmoney, String pointcoupon,
                String convertmoney, String exchangemoney, String invitenum, String allconvertmoney, String allinvitenum, String allsalemoney,
                String is_max
    ) {
        super(token);
        this.id = id;
        this.username = username;
        this.password = password;
        this.paypassword = paypassword;
        this.feeaccount = feeaccount;
        this.debitaccound = card_feeaccount;
        this.consumfee = consumfee;
        this.score = score;
        this.signdays = signdays;
        this.signscore = signscore;
        this.experience_value = experience_value;
        this.recommend_code = recommend_code;
        this.nickname = nickname;
        this.sex = sex;
        this.charindex = charindex;
        this.mobile = mobile;
        this.avatar = avatar;
        this.avatarbig = avatarbig;
        this.onlineflag = onlineflag;
        this.validflag = validflag;
        this.vestflag = vestflag;
        this.lastsigntime = lastsigntime;
        this.lng = lng;
        this.lat = lat;
        this.deviceid = deviceid;
        this.devicetype = devicetype;
        this.chanelid = chanelid;
        this.lastlogintime = lastlogintime;
        this.lastloginversion = lastloginversion;
        this.thirdtype = thirdtype;
        this.thirduid = thirduid;
        this.content = content;
        this.regdate = regdate;
        this.bankuser = bankuser;
        this.bankname = bankname;
        this.bankcard = bankcard;
        this.bankaddress = bankaddress;
        this.alipay = alipay;
        this.sign = is_sign;
        this.friendflag = friendflag;
        this.qrcode = qrcode;
        this.download = download;
        this.level = level;
        this.dxmoney = dxmoney;
        this.pointcoupon = pointcoupon;
        this.convertmoney = convertmoney;
        this.exchangemoney = exchangemoney;

        this.invitenum = invitenum;
        this.allconvertmoney = allconvertmoney;
        this.allinvitenum = allinvitenum;
        this.allsalemoney = allsalemoney;
        this.is_max = is_max;
    }

    public String getFull_fee() {
        return full_fee;
    }

    public String getFriendflag() {
        return friendflag;
    }

    public ValueInfo getValueInfo() {
        return valueInfo;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPaypassword() {
        return paypassword;
    }

    public String getFeeaccount() {
        return feeaccount;
    }

    public String getPointcoupon_money() {
        return pointcoupon_money;
    }

    public String getConsumfee() {
        return consumfee;
    }

    public String getScore() {
        return score;
    }

    public String getSignday() {
        return signday;
    }

    public void setSignday(String signday) {
        this.signday = signday;
    }

    public String getSignscore() {
        return signscore;
    }

    public String getExperience_value() {
        return experience_value;
    }

    public String getRecommend_code() {
        return recommend_code;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSex() {
        return sex;
    }

    public String getCharindex() {
        return charindex;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAvatarbig() {
        return avatarbig;
    }

    public String getOnlineflag() {
        return onlineflag;
    }

    public String getValidflag() {
        return validflag;
    }

    public String getVestflag() {
        return vestflag;
    }

    public String getLastsigntime() {
        return lastsigntime;
    }

    public String getLng() {
        return lng;
    }

    public String getLat() {
        return lat;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public String getChanelid() {
        return chanelid;
    }

    public String getLastlogintime() {
        return lastlogintime;
    }

    public String getLastloginversion() {
        return lastloginversion;
    }

    public String getThirdtype() {
        return thirdtype;
    }

    public String getThirduid() {
        return thirduid;
    }

    public String getContent() {
        return content;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getBankuser() {
        return bankuser;
    }

    public String getBankname() {
        return bankname;
    }

    public String getBankcard() {
        return bankcard;
    }

    public String getBankaddress() {
        return bankaddress;
    }

    public String getAlipay() {
        return alipay;
    }

    public String getIs_Sign() {
        return sign;
    }

    public void setIs_sign(String is_sign) {
        this.sign = is_sign;
    }


    public String getMermber_value() {
        return mermber_value;
    }

    public void setPaypassword(String paypassword) {
        this.paypassword = paypassword;
    }

    public void setFeeaccount(String feeaccount) {
        this.feeaccount = feeaccount;
    }

    public String getCommission() {
        return commission;
    }

    public String getSum_score() {
        return sum_score;
    }

    public String getDownload() {
        return download;
    }

    public String getQrcode() {
        return qrcode;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSigndays() {
        return signdays;
    }

    public String getPointcoupon() {
        return pointcoupon;
    }

    public String getConvertmoney() {
        return convertmoney;
    }

    public String getExchangemoney() {
        return exchangemoney;
    }

    public String getSign() {
        return sign;
    }

    public String getValue_info() {
        return value_info;
    }

    public void setBankuser(String bankuser) {
        this.bankuser = bankuser;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public void setBankaddress(String bankaddress) {
        this.bankaddress = bankaddress;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getInvitenum() {
        return invitenum;
    }

    public String getAllconvertmoney() {
        return allconvertmoney;
    }

    public String getAllinvitenum() {
        return allinvitenum;
    }

    public String getAllsalemoney() {
        return allsalemoney;
    }

    public String getIs_max() {
        return is_max;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", paypassword='" + paypassword + '\'' +
                ", feeaccount='" + feeaccount + '\'' +
                ", debitaccound='" + debitaccound + '\'' +
                ", consumfee='" + consumfee + '\'' +
                ", score='" + score + '\'' +
                ", sum_score='" + sum_score + '\'' +
                ", signdays='" + signdays + '\'' +
                ", signscore='" + signscore + '\'' +
                ", experience_value='" + experience_value + '\'' +
                ", recommend_code='" + recommend_code + '\'' +
                ", pointcoupon='" + pointcoupon + '\'' +
                ", convertmoney='" + convertmoney + '\'' +
                ", exchangemoney='" + exchangemoney + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", charindex='" + charindex + '\'' +
                ", mobile='" + mobile + '\'' +
                ", avatar='" + avatar + '\'' +
                ", avatarbig='" + avatarbig + '\'' +
                ", onlineflag='" + onlineflag + '\'' +
                ", validflag='" + validflag + '\'' +
                ", vestflag='" + vestflag + '\'' +
                ", lastsigntime='" + lastsigntime + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", deviceid='" + deviceid + '\'' +
                ", devicetype='" + devicetype + '\'' +
                ", chanelid='" + chanelid + '\'' +
                ", lastlogintime='" + lastlogintime + '\'' +
                ", lastloginversion='" + lastloginversion + '\'' +
                ", thirdtype='" + thirdtype + '\'' +
                ", thirduid='" + thirduid + '\'' +
                ", content='" + content + '\'' +
                ", regdate='" + regdate + '\'' +
                ", bankuser='" + bankuser + '\'' +
                ", bankname='" + bankname + '\'' +
                ", bankcard='" + bankcard + '\'' +
                ", bankaddress='" + bankaddress + '\'' +
                ", alipay='" + alipay + '\'' +
                ", sign='" + sign + '\'' +
                ", dxmoney='" + dxmoney + '\'' +
                ", signday='" + signday + '\'' +
                ", friendflag='" + friendflag + '\'' +
                ", full_fee='" + full_fee + '\'' +
                ", level='" + level + '\'' +
                ", mermber_value='" + mermber_value + '\'' +
                ", value_info='" + value_info + '\'' +
                ", commission='" + commission + '\'' +
                ", download='" + download + '\'' +
                ", qrcode='" + qrcode + '\'' +
                ", pointcoupon_money='" + pointcoupon_money + '\'' +
                ", invitenum='" + invitenum + '\'' +
                ", allconvertmoney='" + allconvertmoney + '\'' +
                ", allinvitenum='" + allinvitenum + '\'' +
                ", allsalemoney='" + allsalemoney + '\'' +
                ", is_max='" + is_max + '\'' +
                ", valueInfo=" + valueInfo +
                '}';
    }
}
