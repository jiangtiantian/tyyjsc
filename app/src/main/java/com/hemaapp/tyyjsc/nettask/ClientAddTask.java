package com.hemaapp.tyyjsc.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 用户注册
 */
public class ClientAddTask extends BaseNetTask {

	public ClientAddTask(BaseHttpInformation information,
			HashMap<String, String> params) {
		super(information, params);
	}

	public ClientAddTask(BaseHttpInformation information,
			HashMap<String, String> params, HashMap<String, String> files) {
		super(information, params, files);
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new Result(jsonObject);
	}

	private class Result extends HemaArrayResult<String> {

		public Result(JSONObject jsonObject) throws DataParseException {
			super(jsonObject);
		}

		@Override
		public String parse(JSONObject jsonObject) throws DataParseException {
			try {
				return get(jsonObject, "token");
			} catch (JSONException e) {
				throw new DataParseException(e);
			}
		}
	}

}
