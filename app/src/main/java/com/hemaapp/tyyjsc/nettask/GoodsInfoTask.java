package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.HM_GoodsInfo;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 商品详情
 */
public class GoodsInfoTask extends BaseNetTask {

    public GoodsInfoTask(BaseHttpInformation information,
                         HashMap<String, String> params) {
        super(information, params);
    }

    public GoodsInfoTask(BaseHttpInformation information,
                         HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<HM_GoodsInfo> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public HM_GoodsInfo parse(JSONObject jsonObject) throws DataParseException {
            return new HM_GoodsInfo(jsonObject);
        }

    }
}