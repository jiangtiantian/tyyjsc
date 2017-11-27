package com.hemaapp.tyyjsc.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 省份
 */
public class ProvinceChildren extends XtomObject implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;//"1",
	private String parentid;//"0",
	private String name;//"������",
	private ArrayList<CityChildren> children=new ArrayList<CityChildren>();//

	public ProvinceChildren(JSONObject jsonObject) throws DataParseException {
		if (jsonObject != null) {
			try {
				id = get(jsonObject, "id");
				name = get(jsonObject, "name");
				parentid = get(jsonObject, "parentid");
				
				if (!jsonObject.isNull("children")
						&& !isNull(jsonObject.getString("children"))) {
					JSONArray jsonList = jsonObject.getJSONArray("children");
					int size = jsonList.length();
					children = new ArrayList<CityChildren>();
					for (int i = 0; i < size; i++) {
						children.add(new CityChildren(jsonList.getJSONObject(i)));
					}
				}
				log_i(toString());
			} catch (JSONException e) {
				throw new DataParseException(e);
			}
		}
	}

	public ProvinceChildren(String id, String parentid, String name,
			ArrayList<CityChildren> children) {
		super();
		this.id = id;
		this.parentid = parentid;
		this.name = name;
		this.children = children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<CityChildren> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<CityChildren> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return "CityChildren [id=" + id + ", parentid=" + parentid + ", name="
				+ name + ", children=" + children + "]";
	}

}