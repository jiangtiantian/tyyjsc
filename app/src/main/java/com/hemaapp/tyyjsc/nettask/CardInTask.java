package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.DealItem;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 储值卡列表
 */
public class CardInTask extends BaseNetTask {

    public CardInTask(BaseHttpInformation information,
                            HashMap<String, String> params) {
        super(information, params);
    }

    public CardInTask(BaseHttpInformation information,
                            HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);

    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {

        return new Result(jsonObject);
    }

    private class Result extends HemaPageArrayResult<DealItem> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public DealItem parse(JSONObject jsonObject) throws DataParseException {
            return new DealItem(jsonObject);
        }
    }
}

