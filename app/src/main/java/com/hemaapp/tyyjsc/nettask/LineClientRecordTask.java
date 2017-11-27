package com.hemaapp.tyyjsc.nettask;

import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.RecordInfor;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by wangyuxia on 2017/8/17.
 */

public class LineClientRecordTask extends BaseNetTask {

    public LineClientRecordTask(BaseHttpInformation information,
                         HashMap<String, String> params) {
        super(information, params);
    }

    public LineClientRecordTask(BaseHttpInformation information,
                         HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {

        return new Result(jsonObject);
    }
    private class Result extends HemaPageArrayResult<RecordInfor> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public RecordInfor parse(JSONObject jsonObject) throws DataParseException {
            return new RecordInfor(jsonObject);
        }

    }
}
