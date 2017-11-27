package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by wangyuxia on 2017/8/17.
 */

public class RecordInfor extends XtomObject{

    private String id;
    private String username;
    private String nickname;
    private String money;
    private String convertmoney;
    private String regdate;

    public RecordInfor(JSONObject jsonObject) throws DataParseException {
        if(jsonObject != null){
            try {
                id = get(jsonObject, "id");
                username = get(jsonObject, "username");
                nickname = get(jsonObject, "nickname");
                money = get(jsonObject, "money");
                convertmoney = get(jsonObject, "convertmoney");
                regdate = get(jsonObject, "regdate");

                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "RecordInfor{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", money='" + money + '\'' +
                ", convertmoney='" + convertmoney + '\'' +
                ", regdate='" + regdate + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMoney() {
        return money;
    }

    public String getConvertmoney() {
        return convertmoney;
    }

    public String getRegdate() {
        return regdate;
    }
}
