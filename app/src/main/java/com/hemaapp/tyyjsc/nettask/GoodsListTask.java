package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.GoodsBriefIntroduction;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 *商品列表任务
 */
public class GoodsListTask extends BaseNetTask {

	public GoodsListTask(BaseHttpInformation information,
			HashMap<String, String> params) {
		super(information, params);		 
	}

	public GoodsListTask(BaseHttpInformation information,
			HashMap<String, String> params, HashMap<String, String> files) {
		super(information, params, files);		
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		 
		return new Result(jsonObject);
	}
	private class Result extends HemaPageArrayResult<GoodsBriefIntroduction> {

		public Result(JSONObject jsonObject) throws DataParseException {
			super(jsonObject);
		}

		@Override
		public GoodsBriefIntroduction parse(JSONObject jsonObject) throws DataParseException {
			return new GoodsBriefIntroduction(jsonObject);
		}

	}
}
