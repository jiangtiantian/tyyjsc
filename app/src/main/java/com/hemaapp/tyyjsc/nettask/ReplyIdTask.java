package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.ReplyId;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 评论回复id
 */
public class ReplyIdTask extends BaseNetTask {

    public ReplyIdTask(BaseHttpInformation information,
                       HashMap<String, String> params) {
        super(information, params);
    }

    public ReplyIdTask(BaseHttpInformation information,
                       HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {

        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<ReplyId> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public ReplyId parse(JSONObject jsonObject) throws DataParseException {
            return new ReplyId(jsonObject);
        }

    }
}
