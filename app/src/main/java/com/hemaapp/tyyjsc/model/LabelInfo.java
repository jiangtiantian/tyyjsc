package com.hemaapp.tyyjsc.model;


import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 标签实体类
 */
public class LabelInfo extends XtomObject {

    private String id;
    private String keyword;
    private String is_remove;
    private String orderby;

    private boolean isChecked = false;

    public LabelInfo(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                keyword = get(jsonObject, "keyword");
                is_remove = get(jsonObject, "is_remove");
                orderby = get(jsonObject, "orderby");

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getIs_remove() {
        return is_remove;
    }

    public void setIs_remove(String is_remove) {
        this.is_remove = is_remove;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}