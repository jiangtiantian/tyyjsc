package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;

/**
 * 属性组合实体类
 */

public class AttrMixInfo extends XtomObject implements Serializable{

    private String id;//组合id
    private String goods_id;//属性所属的商品
    private String mix;
    private String mix_two;
    private String price;//该组合下商品价格
    private String stock;//该组合下商品库存
    private String specs_name;//组合名字

    public AttrMixInfo(JSONObject jsonObject){
        if(jsonObject != null){
            try {
                id = get(jsonObject, "id");
                goods_id = get(jsonObject, "goods_id");
                mix = get(jsonObject, "mix");
                mix_two = get(jsonObject, "mix_two");
                price = get(jsonObject, "price");
                stock = get(jsonObject, "stock");
                specs_name = get(jsonObject, "specs_name");

                log_d(toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public String getMix() {
        return mix;
    }

    public String getMix_two() {
        return mix_two;
    }

    public String getPrice() {
        return price;
    }

    public String getStock() {
        return stock;
    }

    public String getSpecs_name() {
        return specs_name;
    }

    @Override
    public String toString() {
        return "AttrMixInfo{" +
                "id='" + id + '\'' +
                ", goods_id='" + goods_id + '\'' +
                ", mix='" + mix + '\'' +
                ", mix_two='" + mix_two + '\'' +
                ", price='" + price + '\'' +
                ", stock='" + stock + '\'' +
                ", specs_name='" + specs_name + '\'' +
                '}';
    }
}
