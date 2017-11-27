package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 *商品详情中图片列表实体类
 */
public class HM_ImgInfo extends XtomObject implements Serializable{
	
	
	private String id;
	private String client_id;
	private String content;
	private String keytype;
	private String keyid;
	private String imgurl;
	private String imgurlbig;
	private String regdate;
	private String orderby;
	
	public HM_ImgInfo(JSONObject jsonObject) throws DataParseException {
		if(jsonObject != null){
			
			try {
				id = get(jsonObject, "id");
				client_id = get(jsonObject, "client_id");
				content = get(jsonObject, "content");
				keytype = get(jsonObject, "keytype");
				keyid = get(jsonObject, "keyid");
				imgurl = get(jsonObject, "imgurl");
				imgurlbig = get(jsonObject, "imgurlbig");
				regdate = get(jsonObject, "regdate");
				orderby = get(jsonObject, "orderby");
				
				log_d(toString());
				
			} catch (JSONException e) {
				throw new DataParseException(e);
			}
		}
	}
	public HM_ImgInfo(String imgurl){
		this.imgurl = imgurl;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getImgurlbig() {
		return imgurlbig;
	}

	public void setImgurlbig(String imgurlbig) {
		this.imgurlbig = imgurlbig;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	@Override
	public String toString() {
		return "HM_ImgInfo [id=" + id + ", client_id=" + client_id
				+ ", content=" + content + ", keytype=" + keytype + ", keyid="
				+ keyid + ", imgurl=" + imgurl + ", imgurlbig=" + imgurlbig
				+ ", regdate=" + regdate + ", orderby=" + orderby + "]";
	}
}
