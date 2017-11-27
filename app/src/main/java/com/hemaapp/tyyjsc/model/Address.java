package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 地址实体类
 */
public class Address extends XtomObject implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String id;  //地址主键id
	private String cid;  //用户主键id
	private String name; //收货人姓名
	private String tel; //收货人电话
	private String address; //收货人地址
	private String province;//省
	private String city;//市
	private String province_id;//省id
	private String city_id;//市id
	private String district_id;//区id
	private String district;//区
	private String zipcode;//邮编
	private String area;
	private String totaladdress;
	private String rec;//是否为默认地址1.默认，2。不是默认

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Address(JSONObject jsonObject) throws DataParseException {
		if (jsonObject != null) {
			try {
				id = get(jsonObject, "id");
				cid = get(jsonObject, "cid");
				name = get(jsonObject, "name");
				tel = get(jsonObject, "tel");
				province = get(jsonObject, "province");
				city = get(jsonObject, "city");
				province_id = get(jsonObject, "province_id");

				city_id = get(jsonObject, "city_id");
				district_id = get(jsonObject, "district_id");
				address = get(jsonObject, "address");
				rec = get(jsonObject, "rec");
				area=get(jsonObject,"area");
				district = get(jsonObject, "district");
				zipcode = get(jsonObject, "zipcode");
				totaladdress = get(jsonObject, "totaladdress");
				log_i(toString());
			} catch (JSONException e) {
				throw new DataParseException(e);
			}
		}
	}

	public Address(String id, String client_name, String mobile, String address, String province, String city, String province_id, String city_id, String district_id, String district, String zip_code, String is_default) {
		this.id = id;
		this.name = client_name;
		this.tel = mobile;
		this.address = address;
		this.province = province;
		this.city = city;
		this.province_id = province_id;
		this.city_id = city_id;
		this.district_id = district_id;
		this.district = district;
		this.zipcode = zip_code;
		this.rec = is_default;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince_id() {
		return province_id;
	}

	public void setProvince_id(String province_id) {
		this.province_id = province_id;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getDistrict_id() {
		return district_id;
	}

	public void setDistrict_id(String district_id) {
		this.district_id = district_id;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getTotaladdress() {
		return totaladdress;
	}

	public void setTotaladdress(String totaladdress) {
		this.totaladdress = totaladdress;
	}

	public String getRec() {
		return rec;
	}

	public void setRec(String rec) {
		this.rec = rec;
	}
}
