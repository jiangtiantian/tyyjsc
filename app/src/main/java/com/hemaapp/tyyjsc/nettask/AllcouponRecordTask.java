package com.hemaapp.tyyjsc.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.AllcouponRecord;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by zuozhongqian on 2017/8/4.
 */

public  class AllcouponRecordTask extends BaseNetTask {
    public AllcouponRecordTask(BaseHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    public AllcouponRecordTask(BaseHttpInformation information, HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new AllcouponRecordResult(jsonObject);
    }

    public class AllcouponRecordResult extends HemaArrayResult<AllcouponRecord> {

        public AllcouponRecordResult(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public AllcouponRecord parse(JSONObject jsonObject) throws DataParseException {
            return new AllcouponRecord(jsonObject);
        }
    }
}
