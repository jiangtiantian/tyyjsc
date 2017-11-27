package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 评论上传成功后返回的id
 * 申请退款成功后返回的id
 */
public class ReplyId extends XtomObject implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private String reply_id;// 回复主键id
    private String score;
    private String return_order_id;//退款成功id

    public String getScore() {
        return score;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setScore(String score) {
        this.score = score;
    }

    private String voucher_sum;//是否返代金券

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String num;//消息条数

    private String rec_client;//邀请人手机号


    public ReplyId(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                reply_id = get(jsonObject, "reply_id");
                return_order_id = get(jsonObject, "return_order_id");
                id=get(jsonObject,"id");
                voucher_sum = get(jsonObject, "voucher_sum");
                num = get(jsonObject, "num");
                score=get(jsonObject,"score");
                rec_client = get(jsonObject, "rec_client");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }


    public String getReply_id() {
        return reply_id;
    }

    public String getVoucher_sum() {
        return voucher_sum;
    }

    public String getReturn_order_id() {
        return return_order_id;
    }


    public String getRec_client() {
        return rec_client;
    }

    @Override
    public String toString() {
        return "ReplyId{" +
                "reply_id='" + reply_id + '\'' +
                ", return_order_id='" + return_order_id + '\'' +
                ", voucher_sum='" + voucher_sum + '\'' +
                ", number='" + num + '\'' +
                ", rec_client='" + rec_client + '\'' +
                '}';
    }
}
