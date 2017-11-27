package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 推荐用户信息
 */
public class RecommendUserInfo extends XtomObject {
    private String id;//推荐用户id
    private String nickname;//	用户昵称
    private String avatar;//	用户头像
    private String avatarbig;//用户大头像
    private String is_rebate;//是否返佣金0否 1是

    private String rebate_amount;//已返佣金
    private String consumfee;//	当月消费金额

    public RecommendUserInfo(JSONObject jsonObject) throws DataParseException {
        if(jsonObject != null){
            try {
                nickname = get(jsonObject, "nickname");
                avatar = get(jsonObject, "avatar");
                avatarbig = get(jsonObject, "avatarbig");
                id = get(jsonObject, "id");
                is_rebate = get(jsonObject, "is_rebate");
                rebate_amount = get(jsonObject, "rebate_amount");
                consumfee = get(jsonObject, "consumfee");

                log_d(toString());
            } catch (JSONException e) {
               throw  new DataParseException(e);
            }
        }
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAvatarbig() {
        return avatarbig;
    }

    public String getId() {
        return id;
    }

    public String getIs_rebate() {
        return is_rebate;
    }

    public String getRebate_amount() {
        return rebate_amount;
    }

    public String getConsumfee() {
        return consumfee;
    }

    @Override
    public String toString() {
        return "RecommendUserInfo{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", avatarbig='" + avatarbig + '\'' +
                ", is_rebate='" + is_rebate + '\'' +
                ", rebate_amount='" + rebate_amount + '\'' +
                ", consumfee='" + consumfee + '\'' +
                '}';
    }
}
