package com.hemaapp.tyyjsc.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.SignInfo;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 签到任务
 */
public class SignInfoTask extends BaseNetTask {

    public SignInfoTask(BaseHttpInformation information,
                         HashMap<String, String> params) {
        super(information, params);
    }

    public SignInfoTask(BaseHttpInformation information,
                         HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<SignInfo> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public SignInfo parse(JSONObject jsonObject) throws DataParseException {
            return new SignInfo(jsonObject);
        }

    }
}
