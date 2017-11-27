package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by zuozhongqian on 2017/8/4.
 */

public class ReplayList extends XtomObject implements Serializable {
    private String nickname;
    private String avatar;
    private String id;
    private String client_id;
    private String keytype;
    private String keyid;
    private String content;
    private String parentid;
    private String regdate;
    private String parentnickname;
    private String pid;
    private String replytype;
    private String imgItems;
    public ReplayList(JSONObject jsonObject) throws DataParseException {
        if(jsonObject != null){
            try {
                nickname = get(jsonObject, "nickname");
                avatar=get(jsonObject,"avatar");
                id=get(jsonObject,"id");
                client_id=get(jsonObject,"client_id");
                keytype = get(jsonObject, "keytype");
                keyid = get(jsonObject, "keyid");
                content = get(jsonObject, "content");
                parentid = get(jsonObject, "parentid");
                regdate = get(jsonObject, "regdate");
                parentnickname = get(jsonObject, "parentnickname");
                pid = get(jsonObject, "pid");
                replytype = get(jsonObject, "replytype");
                imgItems = get(jsonObject, "imgItems");

                log_d(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getKeytype() {
        return keytype;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    public String getKeyid() {
        return keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getParentnickname() {
        return parentnickname;
    }

    public void setParentnickname(String parentnickname) {
        this.parentnickname = parentnickname;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getReplytype() {
        return replytype;
    }

    public void setReplytype(String replytype) {
        this.replytype = replytype;
    }

    public String getImgItems() {
        return imgItems;
    }

    public void setImgItems(String imgItems) {
        this.imgItems = imgItems;
    }
}
