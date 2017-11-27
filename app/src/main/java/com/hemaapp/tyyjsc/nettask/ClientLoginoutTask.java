package com.hemaapp.tyyjsc.nettask;

import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 注销登录
 */
public class ClientLoginoutTask extends BaseNetTask {

	public ClientLoginoutTask(BaseHttpInformation information,
			HashMap<String, String> params) {
		super(information, params);
	}

	public ClientLoginoutTask(BaseHttpInformation information,
			HashMap<String, String> params, HashMap<String, String> files) {
		super(information, params, files);
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new HemaBaseResult(jsonObject);
	}

}
