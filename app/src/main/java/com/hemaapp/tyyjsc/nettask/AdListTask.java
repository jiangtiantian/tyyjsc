package com.hemaapp.tyyjsc.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.AdList;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by zuo15 on 2017/8/7.
 */

public class AdListTask extends BaseNetTask {
    public AdListTask(BaseHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    public AdListTask(BaseHttpInformation information, HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new AdListResult(jsonObject);
    }
    public class AdListResult extends HemaArrayResult<AdList> {
        public AdListResult(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public AdList parse(JSONObject jsonObject) throws DataParseException {
            return new AdList(jsonObject);
        }
    }
}
