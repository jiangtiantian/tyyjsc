package com.hemaapp.tyyjsc.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 
 * @ClassName: City
 * @Description: 省市区三级联动列表
 * @author
 * @date 2015-9-15
 * 
 */
public class City extends XtomObject implements Serializable {

	private static final long serialVersionUID = 1265819772736012294L;
	private ArrayList<ProvinceChildren> children = new ArrayList<ProvinceChildren>();

	private String province_list;

	public City(JSONObject jsonObject) throws DataParseException {
		try {

			province_list = get(jsonObject, "children");
			JSONArray jsonList = new JSONArray(province_list);
			int size = jsonList.length();
			for (int j = 0; j < size; j++) {
				children.add(new ProvinceChildren(jsonList.getJSONObject(j)));
			}
		} catch (JSONException e1) {
			throw new DataParseException(e1);
		}
	}

	public City(ArrayList<ProvinceChildren> children) {
		super();
		this.children = children;
	}

	public ArrayList<ProvinceChildren> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<ProvinceChildren> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return "City [children=" + children + "]";
	}

}
