package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 签到实体类
 */
public class SignInfo extends XtomObject implements Serializable {
    private String score;//总积分
    private String signdays;//连续签到天数
    private String signscore;//一次签到获得的积分

    public SignInfo(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                score = get(jsonObject, "score");
                signdays = get(jsonObject, "signdays");
                signscore = get(jsonObject, "signscore");

                log_d(toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getScore() {
        return score;
    }

    public String getSigndays() {
        return signdays;
    }

    public String getSignscore() {
        return signscore;
    }

    @Override
    public String toString() {
        return "SignInfo{" +
                "score='" + score + '\'' +
                ", signdays='" + signdays + '\'' +
                ", signscore='" + signscore + '\'' +
                '}';
    }
}