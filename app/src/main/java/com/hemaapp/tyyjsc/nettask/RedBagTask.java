package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.RedBagInfo;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 红包
 */
public class RedBagTask extends BaseNetTask {

    public RedBagTask(BaseHttpInformation information,
                      HashMap<String, String> params) {
        super(information, params);
    }

    public RedBagTask(BaseHttpInformation information,
                      HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<RedBagInfo> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public RedBagInfo parse(JSONObject jsonObject) throws DataParseException {
            return new RedBagInfo(jsonObject);
        }

    }
}