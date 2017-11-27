package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 红包详情
 */

public class RedBagInfo extends XtomObject {
    private String status;//	红包状态 	0未开始1已开始
    private String startdate ;//	开始时间
    private String nowTime;//服务器时间
    private String imgurl ;//	背景图片
    private String imgurlbig 	;//背景大图片

    public RedBagInfo(JSONObject jsonObject) throws DataParseException {
        if(jsonObject != null){
            try {
                status = get(jsonObject, "status");
                startdate = get(jsonObject, "startdate");
                nowTime = get(jsonObject, "nowTime");
                imgurl = get(jsonObject, "imgurl");
                imgurlbig = get(jsonObject, "imgurlbig");

                log_d(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public String getStatus() {
        return status;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public String getNowTime() {
        return nowTime;
    }

    @Override
    public String toString() {
        return "RedBagInfo{" +
                "status='" + status + '\'' +
                ", startdate='" + startdate + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                '}';
    }
}
