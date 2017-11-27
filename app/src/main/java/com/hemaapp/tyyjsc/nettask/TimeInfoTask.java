package com.hemaapp.tyyjsc.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.TimeInfo;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 获取时间段任务
 */
public class TimeInfoTask extends BaseNetTask {

    public TimeInfoTask(BaseHttpInformation information,
                        HashMap<String, String> params) {
        super(information, params);
    }

    public TimeInfoTask(BaseHttpInformation information,
                        HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<TimeInfo> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public TimeInfo parse(JSONObject jsonObject) throws DataParseException {
            return new TimeInfo(jsonObject);
        }

    }
}