package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 支付宝交易签名
 */
public class AlipayTrade extends XtomObject implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String alipaysign;

    public AlipayTrade(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                alipaysign = get(jsonObject, "alipaysign");

                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "AlipayTrade [alipaysign=" + alipaysign + "]";
    }

    /**
     * @return the alipaysign
     */
    public String getAlipaysign() {
        return alipaysign;
    }

}
