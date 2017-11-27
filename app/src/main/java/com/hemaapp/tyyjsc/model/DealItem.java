package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 明细实体类
 */
public class DealItem extends XtomObject implements Serializable {

    private String id;//
    private String client_id;
    private String name;
    private String type;
    private String content;

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String total_fee;
    private String balance;
    private String regdate;
    private String price;//购买价格

    private String money;//储蓄卡面额

    private String getmoney;
    private String reason;
    private String score;
    private String scorenum;
    private String keytype;
    private String amount;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getGetmoney() {
        return getmoney;
    }

    public void setGetmoney(String getmoney) {
        this.getmoney = getmoney;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getScorenum() {
        return scorenum;
    }

    public void setScorenum(String scorenum) {
        this.scorenum = scorenum;
    }

    public DealItem(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                type = get(jsonObject, "type");
                content=get(jsonObject,"content");
                total_fee = get(jsonObject, "total_fee");
                balance = get(jsonObject, "balance");
                regdate = get(jsonObject, "regdate");
                client_id = get(jsonObject, "client_id");
                scorenum=get(jsonObject,"scorenum");
                price=get(jsonObject,"price");
                money=get(jsonObject,"money");
                getmoney=get(jsonObject,"getmoney");
                reason = get(jsonObject, "reason");
                score = get(jsonObject, "score");
                scorenum = get(jsonObject, "scorenum");
                keytype = get(jsonObject, "keytype");
                amount = get(jsonObject, "amount");
                log_d(toString());
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    @Override
    public String toString() {
        return "DealItem [id=" + id + ", name=" + name + ", type=" + type
                + ", total_fee=" + total_fee + ", balance=" + balance
                + ", regdate=" + regdate + ", client_id=" + client_id + "]";
    }

    public String getReason() {
        return reason;
    }

    public String getScore() {
        return score;
    }

    public String getScore_num() {
        return scorenum;
    }

    public String getKeytype() {
        return keytype;
    }

    public String getAmount() {
        return amount;
    }
}
