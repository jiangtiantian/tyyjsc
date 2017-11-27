package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.Notice;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 获取系统通知列表
 */
public class NoticeListTask extends BaseNetTask {

    public NoticeListTask(BaseHttpInformation information,
                          HashMap<String, String> params) {
        super(information, params);
    }

    public NoticeListTask(BaseHttpInformation information,
                          HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaPageArrayResult<Notice> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public Notice parse(JSONObject jsonObject) throws DataParseException {
            return new Notice(jsonObject);
        }
    }
}
