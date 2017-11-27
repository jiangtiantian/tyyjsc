package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by zuozhongqian on 2017/8/7.
 */

public class MemberorderAdd extends XtomObject implements Serializable {
    private String orderid;

    public MemberorderAdd(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                orderid = get(jsonObject, "orderid");

                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }


    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    @Override
    public String toString() {
        return "MemberorderAdd{" +
                "orderid='" + orderid + '\'' +
                '}';
    }
}
