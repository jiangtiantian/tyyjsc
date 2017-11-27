package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;

/**
 * Created by zuo15 on 2017/8/7.
 */

public class AdList extends XtomObject implements Serializable {
    private String id;
    private String jump_type;
    private String ad_content;
    private String relative_content;
    private String imgurlbig;
    private String imgurl;
    private String ad_order;
    private String url;


    public AdList(JSONObject jsonObject){
        if(jsonObject != null){
            try {
                id = get(jsonObject, "id");
                jump_type = get(jsonObject, "jump_type");
                ad_content = get(jsonObject, "ad_content");
                relative_content = get(jsonObject, "relative_content");
                imgurlbig = get(jsonObject, "imgurlbig");
                imgurl = get(jsonObject, "imgurl");
                ad_order = get(jsonObject, "ad_order");
                url = get(jsonObject, "url");

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

    public String getJump_type() {
        return jump_type;
    }

    public void setJump_type(String jump_type) {
        this.jump_type = jump_type;
    }

    public String getAd_content() {
        return ad_content;
    }

    public void setAd_content(String ad_content) {
        this.ad_content = ad_content;
    }

    public String getRelative_content() {
        return relative_content;
    }

    public void setRelative_content(String relative_content) {
        this.relative_content = relative_content;
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

    public String getAd_order() {
        return ad_order;
    }

    public void setAd_order(String ad_order) {
        this.ad_order = ad_order;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}