package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 购物车商品实体类
 */

public class CartGoodsInfo extends XtomObject implements Serializable {
    private String id;//购物车id
    private String cartid; //购物车id
    private String keyid;//商品id
    private String goodsid;

    private String name;//商品名称
    private String imgurl;//商品图片
    private String imgurlbig;//商品图片
    private String price;//单价
    private String goodsprice;//总价
    private String buycount;//数量
    private String propertyid;//规格id
    private String propertyname;//规格名
    private String shopid;//店铺id
    private String shopname;//店铺名
    private String score;//可用积分
    private String byprice;//包邮价
    private String status;
    private String demo;//备注
    private String coupon; //点券
    private String keytype;

    private String begintime;//抢购商品开始时间
    private String endtime;//抢购商品结束时间
    private String nowTime;//当前服务器时间
    private boolean isChecked = false;//默认选中
    private String weight;
    private String allweight;

    public CartGoodsInfo(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                cartid = get(jsonObject, "cartid");
                buycount = get(jsonObject, "buycount");
                status = get(jsonObject, "status");
                keyid = get(jsonObject, "keyid");
                keytype = get(jsonObject, "keytype");
                name = get(jsonObject, "name");
                imgurl = get(jsonObject, "imgurl");
                imgurlbig = get(jsonObject, "imgurlbig");
                price = get(jsonObject, "price");
                goodsprice = get(jsonObject, "goodsprice");
                buycount = get(jsonObject, "buycount");
                propertyid = get(jsonObject, "propertyid");
                propertyname = get(jsonObject, "propertyname");
                shopid = get(jsonObject, "shopid");
                shopname = get(jsonObject, "shopname");
                score = get(jsonObject, "score");
                byprice = get(jsonObject, "byprice");
                begintime = get(jsonObject, "begintime");
                nowTime = get(jsonObject, "nowTime");
                endtime = get(jsonObject, "endtime");
                goodsid = get(jsonObject, "goodsid");

                coupon = get(jsonObject, "coupon");
                weight = get(jsonObject, "weight");
                allweight = get(jsonObject, "allweight");
                log_d(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public CartGoodsInfo(String cartid, String name, String imgurl, String buycount, String price) {
        this.cartid = cartid;
        this.name = name;
        this.imgurl = imgurl;
        this.price = price;
        this.buycount = buycount;
    }

    public CartGoodsInfo(String id, String keyid, String shopid, String shopname, String byprice, String name, String imgurl, String price,
                         String buycount, String spec_id, String specs_name, String score, boolean isChecked, String outtime, String nowTime, String coupon,
                         String keytype, String weight, String allweight) {
        this.id = id;
        this.name = name;
        this.keyid = keyid;
        this.imgurl = imgurl;
        this.shopid = shopid;
        this.shopname = shopname;
        this.byprice = byprice;
        this.price = price;
        this.buycount = buycount;
        this.propertyid = spec_id;
        this.propertyname = specs_name;
        this.isChecked = isChecked;
        this.score = score;
        this.endtime = outtime;
        this.nowTime = nowTime;
        this.coupon=coupon;
        this.keytype = keytype;
        this.weight = weight;
        this.allweight = allweight;
    }


    @Override
    public String toString() {
        return "CartGoodsInfo{" +
                "id='" + id + '\'' +
                ", cartid='" + cartid + '\'' +
                ", keyid='" + keyid + '\'' +
                ", goodsid='" + goodsid + '\'' +
                ", name='" + name + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", price='" + price + '\'' +
                ", goodsprice='" + goodsprice + '\'' +
                ", buycount='" + buycount + '\'' +
                ", propertyid='" + propertyid + '\'' +
                ", propertyname='" + propertyname + '\'' +
                ", shopid='" + shopid + '\'' +
                ", shopname='" + shopname + '\'' +
                ", score='" + score + '\'' +
                ", byprice='" + byprice + '\'' +
                ", status='" + status + '\'' +
                ", demo='" + demo + '\'' +
                ", coupon='" + coupon + '\'' +
                ", keytype='" + keytype + '\'' +
                ", begintime='" + begintime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", nowTime='" + nowTime + '\'' +
                ", isChecked=" + isChecked +
                ", weight='" + weight + '\'' +
                ", allweight='" + allweight + '\'' +
                ", isSellerChecked=" + isSellerChecked +
                '}';
    }


    public String getCartid() {
        return cartid;
    }



    public String getGoodsid() {
        return goodsid;
    }
    public String getDemo() {
        return demo;
    }

    public String getWeight() {
        return weight;
    }

    public String getAllweight() {
        return allweight;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    private boolean isSellerChecked = false;

    public boolean isSellerChecked() {
        return isSellerChecked;
    }

    public void setSellerChecked(boolean sellerChecked) {
        isSellerChecked = sellerChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getKeytype() {
        return keytype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeyid() {
        return keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public void setImgurlbig(String imgurlbig) {
        this.imgurlbig = imgurlbig;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGoodsprice() {
        return goodsprice;
    }

    public void setGoodsprice(String goodsprice) {
        this.goodsprice = goodsprice;
    }

    public String getBuycount() {
        return buycount;
    }

    public void setBuycount(String buycount) {
        this.buycount = buycount;
    }

    public String getPropertyid() {
        return propertyid;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public void setPropertyid(String propertyid) {
        this.propertyid = propertyid;
    }

    public String getPropertyname() {
        return propertyname;
    }

    public void setPropertyname(String propertyname) {
        this.propertyname = propertyname;
    }

    public String getShopid() {
        return shopid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getByprice() {
        return byprice;
    }

    public void setByprice(String byprice) {
        this.byprice = byprice;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



}
