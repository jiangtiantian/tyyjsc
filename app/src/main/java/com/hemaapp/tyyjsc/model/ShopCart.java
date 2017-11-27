package com.hemaapp.tyyjsc.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 购物车商品实体类
 */

public class ShopCart extends XtomObject implements Serializable {
    private String shopid;//店铺id
    private String shopname;//店铺名
    private String s;
    private Boolean ischecked=false;
    private String starttime;

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getCleantime() {
        return cleantime;
    }

    public void setCleantime(String cleantime) {
        this.cleantime = cleantime;
    }

    public String getNowtime() {
        return nowtime;
    }

    public void setNowtime(String nowtime) {
        this.nowtime = nowtime;
    }

    private String cleantime;
    private String nowtime;
    public Boolean getIschecked() {
        return ischecked;
    }
    public void setIschecked(Boolean ischecked) {
        this.ischecked = ischecked;
    }
    private ArrayList<CartGoodsInfo> goods = new ArrayList<>();
    public String getShopname() {
        return shopname;
    }
    public void setShopname(String shopname) {
        this.shopname = shopname;
    }
    public String getShopid() {
        return shopid;
    }
    public void setShopid(String shopid) {
        this.shopid = shopid;
    }
    public ArrayList<CartGoodsInfo> getGoods() {
        return goods;
    }
    public void setGoods(ArrayList<CartGoodsInfo> goods) {
        this.goods = goods;
    }
    public ShopCart(JSONObject jsonObject) throws DataParseException {
        if(jsonObject != null){
            try {
                shopid = get(jsonObject, "shopid");
                starttime=get(jsonObject,"starttime");
                cleantime=get(jsonObject,"cleantime");
                nowtime=get(jsonObject,"nowtime");
                shopname = get(jsonObject, "shopname");
                s = get(jsonObject, "goodsinfo");
                if(!isNull(s)){
                    JSONArray array = new JSONArray(s);
                    for(int i = 0 ;i < array.length(); i++){
                        CartGoodsInfo time = new CartGoodsInfo(array.getJSONObject(i));
                        goods.add(time);
                    }
                }
                log_d(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

}

