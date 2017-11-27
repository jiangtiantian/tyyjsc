package com.hemaapp.tyyjsc.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

import static org.jivesoftware.smackx.pubsub.ConfigureNodeFields.children;

/**
 * 类别实体类
 */
public class SortItem extends XtomObject {

    private String name;
    private int res;

    private String id;

    private String orderby;


    private String imgurl;
    private String imgurlbig;

    private String logo;
    private String logobig;

    private String secondtype;
    private String parentid;


    private String merchant_id;
    private String sort;
    private String type;
    private String city_id;

    public ArrayList<SortItem1> subs = null;

    private int position = -1;
    private String page;//本地缓存使用
    private boolean isChecked = false;


    public SortItem(int res, String name) {
        this.res = res;
        this.name = name;
    }

    public SortItem(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {

            try {
                id = get(jsonObject, "id");
                parentid = get(jsonObject, "parentid");
                name = get(jsonObject, "name");
                orderby = get(jsonObject, "orderby");
                secondtype = get(jsonObject, "secondtype");
                imgurlbig = get(jsonObject, "imgurlbig");
                imgurl = get(jsonObject, "imgurl");
                logo = get(jsonObject, "logo");
                logobig = get(jsonObject, "logobig");


                merchant_id = get(jsonObject, "merchant_id");
                sort = get(jsonObject, "sort");
                type = get(jsonObject, "type");
                city_id = get(jsonObject, "city_id");
                if (!isNull(secondtype)) {
                    subs = new ArrayList<SortItem1>();
                    JSONArray array = new JSONArray(secondtype);
                    for (int i = 0; i < array.length(); i++) {
                        SortItem1 sort = new SortItem1(array.getJSONObject(i));
                        subs.add(sort);
                    }
                }

            } catch (JSONException e) {
                throw new DataParseException(e);
            }

        }
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public SortItem(String id, String name, String imgurl) {
        this.id = id;
        this.name = name;
        this.imgurl = imgurl;
    }

    public SortItem(String name, String id, String imgurl, String imgurlbig, String parentid, String page) {
        this.name = name;
        this.id = id;
        this.imgurl = imgurl;
        this.imgurlbig = imgurlbig;
        this.parentid = parentid;
        this.page = page;
    }

    public int getRes() {
        return res;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogobig() {
        return logobig;
    }

    public void setLogobig(String logobig) {
        this.logobig = logobig;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public String getSort() {
        return sort;
    }

    public String getId() {
        return id;
    }

    public String getParentid() {
        return parentid;
    }

    public String getName() {
        return name;
    }

    public String getOrderby() {
        return orderby;
    }


    public ArrayList<SortItem1> getSubs() {
        return subs;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getCity_id() {
        return city_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "SortItem{" +
                "name='" + name + '\'' +
                ", res=" + res +
                ", id='" + id + '\'' +
                ", orderby='" + orderby + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", logo='" + logo + '\'' +
                ", logobig='" + logobig + '\'' +
                ", parentid='" + parentid + '\'' +
                ", children='" + children + '\'' +
                ", merchant_id='" + merchant_id + '\'' +
                ", sort='" + sort + '\'' +
                ", type='" + type + '\'' +
                ", city_id='" + city_id + '\'' +
                ", subs=" + subs +
                ", position=" + position +
                '}';
    }
}
