package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 签到实体类
 */
public class SortItem1 extends XtomObject implements Serializable {
    private String id;//id
    private String name;//分类名
    private String orderby;//排序
    private String imgurlbig;//图片
    private String imgurl;//
    public SortItem1(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                orderby = get(jsonObject, "orderby");
                imgurlbig = get(jsonObject, "imgurlbig");
                imgurl = get(jsonObject, "imgurl");
                log_d(toString());
            } catch (JSONException e) {
                e.printStackTrace();
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

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public void setImgurlbig(String imgurlbig) {
        this.imgurlbig = imgurlbig;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    @Override
    public String toString() {
        return "SortItem1{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", orderby='" + orderby + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", imgurl='" + imgurl + '\'' +
                '}';
    }
}
