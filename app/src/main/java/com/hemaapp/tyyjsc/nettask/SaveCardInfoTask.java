package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.SaveCardInfo;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 储值卡列表任务
 */

public class SaveCardInfoTask extends BaseNetTask {

    public SaveCardInfoTask(BaseHttpInformation information,
                            HashMap<String, String> params) {
        super(information, params);
    }

    public SaveCardInfoTask(BaseHttpInformation information,
                            HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaPageArrayResult<SaveCardInfo> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public SaveCardInfo parse(JSONObject jsonObject) throws DataParseException {
            return new SaveCardInfo(jsonObject);
        }
    }
}
