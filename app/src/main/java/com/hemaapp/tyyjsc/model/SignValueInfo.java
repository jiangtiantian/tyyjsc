package com.hemaapp.tyyjsc.model;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;

import static com.hemaapp.tyyjsc.R.id.express_fee;

/**
 * 签到积分实体类
 *
 * 新增或编辑地址返回数据实体类
 */
public class SignValueInfo extends XtomObject implements Serializable{
    private String sign_value;//签到可获取积分
    //新增或编辑地址返回数据实体类
    private String id;
    //抢红包返回的红包金额
    private String redfee = "";
    //运费金额
    private String shipment;
    //积分兑换比例
    private String proportion;
    private String days;

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public SignValueInfo(JSONObject jsonObject) {
        if (jsonObject != null) {
            try {
                sign_value = get(jsonObject, "sign_value");
                id = get(jsonObject, "id");
                redfee = get(jsonObject, "redfee");
                shipment = get(jsonObject, "shipment");
                proportion = get(jsonObject, "proportion");
                days = get(jsonObject, "days");
                log_d(toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public String getProportion() {
        return proportion;
    }
    public String getSign_value() {
        return sign_value;
    }
    public String getId() {
        return id;
    }
    public String getRedfee() {
        return redfee;
    }

    @Override
    public String toString() {
        return "SignValueInfo{" +
                "sign_value='" + sign_value + '\'' +
                ", id='" + id + '\'' +
                ", redfee='" + redfee + '\'' +
                ", shipment='" + shipment + '\'' +
                ", proportion='" + proportion + '\'' +
                ", days='" + days + '\'' +
                '}';
    }
}
