package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;

/**
 * 代金券实体类
 */

public class VourcherInfo extends XtomObject implements Serializable{
    private String id;//代金券id
    private String money;//	菜金卷金额
    private String is_expired;//	是否过期 	1否 2 是
    private String name;
    private String days;
    private String demo;
    private String regdate;
    private String endstate;//	使用结束时间
    private String maxmoney;//满多少可以使用代金券

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getIs_expired() {
        return is_expired;
    }

    public void setIs_expired(String is_expired) {
        this.is_expired = is_expired;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }


    public String getEndstate() {
        return endstate;
    }

    public void setEndstate(String endstate) {
        this.endstate = endstate;
    }

    public String getMaxmoney() {
        return maxmoney;
    }

    public void setMaxmoney(String maxmoney) {
        this.maxmoney = maxmoney;
    }

    public VourcherInfo(JSONObject jsonObject){
        if(jsonObject != null){
            try {
                id = get(jsonObject, "id");
                money = get(jsonObject, "money");
                is_expired = get(jsonObject, "is_expired");
                endstate = get(jsonObject, "endtime");
                maxmoney = get(jsonObject, "maxmoney");
                regdate=get(jsonObject,"regdate");
                name=get(jsonObject,"name");
                days=get(jsonObject,"days");
                demo=get(jsonObject,"demo");
                log_d(toString());
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }


}
