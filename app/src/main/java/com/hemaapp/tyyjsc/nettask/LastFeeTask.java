package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.MyDealItem;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 交易明细
 */
public class LastFeeTask extends BaseNetTask {

    public LastFeeTask(BaseHttpInformation information,
                       HashMap<String, String> params) {
        super(information, params);
    }

    public LastFeeTask(BaseHttpInformation information,
                       HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);

    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {

        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<MyDealItem> {


        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public MyDealItem parse(JSONObject jsonObject) throws DataParseException {
            return new MyDealItem(jsonObject);
        }
    }
}
