package com.hemaapp.tyyjsc.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.SignValueInfo;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 签到可获取经验值任务
 */
public class SignValueInfoTask extends BaseNetTask {

    public SignValueInfoTask(BaseHttpInformation information,
                             HashMap<String, String> params) {
        super(information, params);
    }

    public SignValueInfoTask(BaseHttpInformation information,
                             HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<SignValueInfo> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public SignValueInfo parse(JSONObject jsonObject) throws DataParseException {
            return new SignValueInfo(jsonObject);
        }

    }
}