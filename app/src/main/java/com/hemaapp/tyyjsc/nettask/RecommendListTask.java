package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.RecommendUserInfo;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 推荐用户列表
 */
public class RecommendListTask extends BaseNetTask {

    public RecommendListTask(BaseHttpInformation information,
                             HashMap<String, String> params) {
        super(information, params);
    }

    public RecommendListTask(BaseHttpInformation information,
                             HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaPageArrayResult<RecommendUserInfo> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public RecommendUserInfo parse(JSONObject jsonObject) throws DataParseException {
            return new RecommendUserInfo(jsonObject);
        }
    }
}