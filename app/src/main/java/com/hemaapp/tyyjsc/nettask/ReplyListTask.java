package com.hemaapp.tyyjsc.nettask;

import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.ReplayList;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 *评论列表
 */

public class ReplyListTask extends BaseNetTask {
    public ReplyListTask(BaseHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    public ReplyListTask(BaseHttpInformation information, HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new ReplyListResult(jsonObject);
    }
    public class ReplyListResult extends HemaPageArrayResult<ReplayList>{
        public ReplyListResult(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public ReplayList parse(JSONObject jsonObject) throws DataParseException {
            return new ReplayList(jsonObject);
        }
    }

}
