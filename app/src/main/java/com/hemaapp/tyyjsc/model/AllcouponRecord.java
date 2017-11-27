package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;

/**
 *
 */

public class AllcouponRecord extends XtomObject implements Serializable {
    private String id;
    private String keytype;
    private String amount;
    private String content;
    private String regdate;

    public AllcouponRecord(JSONObject jsonObject){
        if(jsonObject != null){
            try {
                id = get(jsonObject, "id");
                keytype = get(jsonObject, "keytype");
                amount = get(jsonObject, "amount");
                content = get(jsonObject, "content");
                regdate = get(jsonObject, "regdate");

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

    public String getKeytype() {
        return keytype;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    @Override
    public String toString() {
        return "AllcouponRecord{" +
                "id='" + id + '\'' +
                ", keytype='" + keytype + '\'' +
                ", amount='" + amount + '\'' +
                ", content='" + content + '\'' +
                ", regdate='" + regdate + '\'' +
                '}';
    }
}
