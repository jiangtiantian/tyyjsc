package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by zuozhongqian on 2017/8/3.
 */

public class MembergoodsList extends XtomObject implements Serializable {
    private String id;
    private String name;
    private String coupon;
    private String displaysales;
    private String realsale;
    private String injectsale;
    private String imgurl;
    private String imgurlbig;
    private String stock;

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public MembergoodsList(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                 coupon= get(jsonObject, "coupon");
                displaysales = get(jsonObject, "displaysales");
                realsale = get(jsonObject, "realsale");
                injectsale = get(jsonObject, "injectsale");
                imgurl = get(jsonObject, "imgurl");
                imgurlbig = get(jsonObject, "imgurlbig");
                stock = get(jsonObject, "stock");

                log_i(toString());

            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
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

    public String getDisplaysales() {
        return displaysales;
    }

    public void setDisplaysales(String displaysales) {
        this.displaysales = displaysales;
    }

    public String getRealsale() {
        return realsale;
    }

    public void setRealsale(String realsale) {
        this.realsale = realsale;
    }

    public String getInjectsale() {
        return injectsale;
    }

    public void setInjectsale(String injectsale) {
        this.injectsale = injectsale;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public void setImgurlbig(String imgurlbig) {
        this.imgurlbig = imgurlbig;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "MembergoodsList{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", coupon='" + coupon + '\'' +
                ", displaysales='" + displaysales + '\'' +
                ", realsale='" + realsale + '\'' +
                ", injectsale='" + injectsale + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", stock='" + stock + '\'' +
                '}';
    }
}
