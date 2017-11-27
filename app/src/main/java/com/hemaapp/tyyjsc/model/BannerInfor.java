package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 图片轮播实体类
 */
public class BannerInfor extends XtomObject {

    private String id;
    private String jump_type;
    private String ad_content;
    private String relative_content;
    private String url;
    private String imgurl;

    public String getRelative_content() {
        return relative_content;
    }

    public void setRelative_content(String relative_content) {
        this.relative_content = relative_content;
    }

    public String getAd_content() {
        return ad_content;
    }

    public void setAd_content(String ad_content) {
        this.ad_content = ad_content;
    }

    public String getJump_type() {
        return jump_type;
    }

    public void setJump_type(String jump_type) {
        this.jump_type = jump_type;
    }

    private String imgurlbig;

    public BannerInfor(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                jump_type = get(jsonObject, "jump_type");
                url = get(jsonObject, "url");
                imgurl = get(jsonObject, "imgurl");
                imgurlbig = get(jsonObject, "imgurlbig");
                ad_content = get(jsonObject, "ad_content");
                relative_content = get(jsonObject, "relative_content");
                log_i(toString());

            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public BannerInfor(String url) {
        this.url = url;
    }
    public String getId() {
        return id;
    }
    public String getUrl() {
        return url;
    }
    public String getImgurl() {
        return imgurl;
    }
    public String getImgurlbig() {
        return imgurlbig;
    }

    @Override
    public String toString() {
        return "BannerInfor{" +
                "id='" + id + '\'' +
                ", jump_type='" + jump_type + '\'' +
                ", ad_content='" + ad_content + '\'' +
                ", relative_content='" + relative_content + '\'' +
                ", url='" + url + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                '}';
    }
}
