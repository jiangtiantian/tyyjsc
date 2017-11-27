package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.LabelInfo;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 标签列表
 */
public class LabelInfoListTask extends BaseNetTask {

    public LabelInfoListTask(BaseHttpInformation information,
                             HashMap<String, String> params) {
        super(information, params);
    }

    public LabelInfoListTask(BaseHttpInformation information,
                             HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<LabelInfo> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public LabelInfo parse(JSONObject jsonObject) throws DataParseException {
            return new LabelInfo(jsonObject);
        }
    }
}
