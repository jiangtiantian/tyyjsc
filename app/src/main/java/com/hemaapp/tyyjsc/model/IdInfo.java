package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 提交订单成功后返回的信息
 */
public class IdInfo extends XtomObject implements Serializable {


    private static final long serialVersionUID = 1L;
    private String trade_no;//订单编号
    private String id;//订单id
    private String voucher_value;
    private String orderid;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public IdInfo(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                trade_no = get(jsonObject, "trade_no");
                id = get(jsonObject, "id");
                orderid=get(jsonObject,"orderid");
                voucher_value = get(jsonObject, "voucher_value");
                log_d(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public String getId() {
        return id;
    }

    public String getVoucher_value() {
        return voucher_value;
    }

    @Override
    public String toString() {
        return "IdInfo{" +
                "trade_no='" + trade_no + '\'' +
                ", id='" + id + '\'' +
                ", voucher_value='" + voucher_value + '\'' +
                '}';
    }
}
