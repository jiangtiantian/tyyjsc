package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;

/**
 * 储值卡实体类
 */

public class SaveCardInfo extends XtomObject implements Serializable{

    private String id;//编号
    private String name;//储值卡名字
    private String value;//储值卡面值
    private String pay_amount;//支付金额
    private String voucher_id;//储值卡编号
    private String sales_volume;//销量
    private String imgurl;//小图
    private String imgurlbig;//大图
    private String give_amount;//赠送代金券

    public SaveCardInfo(JSONObject jsonObject){
        if(jsonObject != null){
            try {
                id = get(jsonObject, "id" );
                name = get(jsonObject, "name" );
                value = get(jsonObject, "value" );
                pay_amount = get(jsonObject, "pay_amount" );
                voucher_id = get(jsonObject, "voucher_id" );
                sales_volume = get(jsonObject, "sales_volume");
                imgurl = get(jsonObject, "imgurl" );
                imgurlbig = get(jsonObject, "imgurlbig" );
                give_amount = get(jsonObject, "give_amount");

                log_d(toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public String getVoucher_id() {
        return voucher_id;
    }

    public String getSales_volume() {
        return sales_volume;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public String getGive_amount() {
        return give_amount;
    }

    @Override
    public String toString() {
        return "SaveCardInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", pay_amount='" + pay_amount + '\'' +
                ", voucher_id='" + voucher_id + '\'' +
                ", sales_volume='" + sales_volume + '\'' +
                '}';
    }
}
