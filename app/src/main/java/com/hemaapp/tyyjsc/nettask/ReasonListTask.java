package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.BankInfo;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 原因列表
 */
public class ReasonListTask extends BaseNetTask {

    public ReasonListTask(BaseHttpInformation information,
                          HashMap<String, String> params) {
        super(information, params);
    }

    public ReasonListTask(BaseHttpInformation information,
                          HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaPageArrayResult<BankInfo> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public BankInfo parse(JSONObject jsonObject) throws DataParseException {
            return new BankInfo(jsonObject);
        }

    }
}