package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.City;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;


/**
 * 三级地址列表
 */
public class AddressThirdInfoTask extends BaseNetTask {

	public AddressThirdInfoTask(BaseHttpInformation information,
			HashMap<String, String> params) {
		super(information, params);
	}

	public AddressThirdInfoTask(BaseHttpInformation information,
			HashMap<String, String> params, HashMap<String, String> files) {
		super(information, params, files);
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new Result(jsonObject);
	}

	private class Result extends HemaArrayResult<City> {

		public Result(JSONObject jsonObject) throws DataParseException {
			super(jsonObject);
		}

		@Override
		public City parse(JSONObject jsonObject) throws DataParseException {
			return new City(jsonObject);
		}

	}
}
