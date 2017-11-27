package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by zuozhongqian on 2017/8/10.
 */

public class CodeAdd extends XtomObject implements Serializable {
    private String qrcode;

    public CodeAdd(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                qrcode = get(jsonObject, "qrcode");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    @Override
    public String toString() {
        return "CodeAdd{" +
                "qrcode='" + qrcode + '\'' +
                '}';
    }
}
