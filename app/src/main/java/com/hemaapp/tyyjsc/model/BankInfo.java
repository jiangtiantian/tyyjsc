package com.hemaapp.tyyjsc.model;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 银行卡信息
 */
public class BankInfo  extends XtomObject implements Serializable {
    private String bankuser;//	银行卡用户名
    private String bankname;//		银行名称
    private String bankcard;//		银行卡号
    private String bankaddress;//		开户行地址

    private String id;
    private String name;//银行卡名字

    public BankInfo(JSONObject jsonObject) throws DataParseException {
        if(jsonObject != null){
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public BankInfo(String bankuser, String bankname, String bankcard, String bankaddress) {
        this.bankuser = bankuser;
        this.bankname = bankname;
        this.bankcard = bankcard;
        this.bankaddress = bankaddress;
    }

    public String getBankuser() {
        return bankuser;
    }

    public String getBankname() {
        return bankname;
    }

    public String getBankcard() {
        return bankcard;
    }

    public String getBankaddress() {
        return bankaddress;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
