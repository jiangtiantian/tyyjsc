package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;

/**
 *
 * 属性实体类
 */

public class AttrInfo extends XtomObject implements Serializable{

    private String id;//属性编号
    private String path;//规格名
    private String coupon;
    private String price;//售价
    private String originalprice;//原价
    private String goods_id;//所属商品
    private String name;//属性名字
    private String stock;
    private String weight;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOriginalprice() {
        return originalprice;
    }

    public void setOriginalprice(String originalprice) {
        this.originalprice = originalprice;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getWeight() {
        return weight;
    }

    public AttrInfo(JSONObject jsonObject){
        if(jsonObject != null){
            try {
                id = get(jsonObject, "id");
                goods_id = get(jsonObject, "goods_id");
                path = get(jsonObject, "path");
                price = get(jsonObject, "price");
                originalprice = get(jsonObject, "originalprice");
                name = get(jsonObject, "name");
                stock = get(jsonObject, "stock");
                coupon = get(jsonObject, "coupon");
                weight = get(jsonObject, "weight");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getCoupon() {
        return coupon;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public String getName() {
        return name;
    }

}
