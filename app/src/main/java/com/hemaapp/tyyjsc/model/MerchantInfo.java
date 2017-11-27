package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 *商家详情实体类
 */
public class MerchantInfo extends XtomObject {

	private String id;// �̼�����id
	private String name;// �̼�����
	private String mobile;// �̼ҵ绰
	private String address;// �̼ҵ�ַ
	private String lng;// ����	
	private String lat;// γ��
	private String imgurl;//�̼�
	
	 

	public MerchantInfo(JSONObject jsonObject) throws DataParseException {
		if (jsonObject != null) {
			try {
				id = get(jsonObject, "id");
				name = get(jsonObject, "name");
				mobile = get(jsonObject, "mobile");
				address = get(jsonObject, "address");
				lng = get(jsonObject, "lng");
				lat = get(jsonObject, "lat");
				imgurl = get(jsonObject, "imgurl");
				 
				log_i(toString());
			} catch (JSONException e) {
				throw new DataParseException(e);
			}
		}
	}
	/**
	 * @return the imgurl
	
	 */
	public String getImgurl() {
		return imgurl;
	}



	public String getId() {
		return id;
	}



	public String getName() {
		return name;
	}



	public String getMobile() {
		return mobile;
	}



	public String getAddress() {
		return address;
	}



	public String getLng() {
		return lng;
	}



	public String getLat() {
		return lat;
	}



	@Override
	public String toString() {
		return "MerchantInfo [id=" + id + ", name=" + name + ", mobile="
				+ mobile + ", address=" + address + ", lng=" + lng + ", lat="
				+ lat + "]";
	}	 
}
