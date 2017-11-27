package com.hemaapp.tyyjsc.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 评论实体类
 */
public class Comment extends XtomObject implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private String client_id;
    private String content;
    private String regdate;
    private String replytype;
    private String nickname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReplytype() {
        return replytype;
    }

    public void setReplytype(String replytype) {
        this.replytype = replytype;
    }

    private String avatar;//评论头像
    private String img_list;//退款上传图片
    private ArrayList<HM_ImgInfo> imgs = new ArrayList<>();

    public Comment(JSONObject jsonObject) throws DataParseException {
        try {
            client_id = get(jsonObject, "client_id");
            content = get(jsonObject, "content");
            regdate = get(jsonObject, "regdate");
            replytype = get(jsonObject, "replytype");
            nickname = get(jsonObject, "nickname");
            avatar = get(jsonObject, "avatar");

            img_list = get(jsonObject, "imgItems");

            if (!isNull(img_list)) {
                imgs = new ArrayList<HM_ImgInfo>();
                JSONArray array = new JSONArray(img_list);
                for (int i = 0; i < array.length(); i++) {
                    HM_ImgInfo imgInfo = new HM_ImgInfo(array.getJSONObject(i));
                    imgs.add(imgInfo);
                }
            }
        } catch (Exception e) {
            throw new DataParseException(e);
        }
    }
    public Comment(){

    }
    public Comment(ArrayList<HM_ImgInfo> imgs){
        this.imgs = imgs;
    }

    public ArrayList<HM_ImgInfo> getImgs() {
        return imgs;
    }
    public String getClient_id() {
        return client_id;
    }


    public String getContent() {
        return content;
    }


    public String getRegdate() {
        return regdate;
    }




    public String getNickname() {
        return nickname;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setImgs(ArrayList<HM_ImgInfo> imgs) {
        this.imgs = imgs;
    }

}
