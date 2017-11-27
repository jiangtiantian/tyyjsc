package com.hemaapp.tyyjsc.nettask;

import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.MembergoodsList;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 会员列表
 */

public class MembergoodsListTask extends BaseNetTask {
    public MembergoodsListTask(BaseHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    public MembergoodsListTask(BaseHttpInformation information, HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new MembergoodsListResult(jsonObject);
    }
    public  class MembergoodsListResult extends HemaPageArrayResult<MembergoodsList>{

        public MembergoodsListResult(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public MembergoodsList parse(JSONObject jsonObject) throws DataParseException {
            return new MembergoodsList(jsonObject);
        }
    }
}
