package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.SortItem;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 类别任务
 */

public class TypeListTask extends BaseNetTask {

	public TypeListTask(BaseHttpInformation information,
			HashMap<String, String> params) {
		super(information, params);	
	}

	public TypeListTask(BaseHttpInformation information,
			HashMap<String, String> params, HashMap<String, String> files) {
		super(information, params, files);		
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		 
		return new Result(jsonObject);
	}

	private class Result extends HemaArrayResult<SortItem> {

		public Result(JSONObject jsonObject) throws DataParseException {
			super(jsonObject);
		}

		public SortItem parse(JSONObject jsonObject) throws DataParseException {
			
			return new SortItem(jsonObject);
		}
		
	}
}
