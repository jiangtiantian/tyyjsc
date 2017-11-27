package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.IdInfo;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 订单添加成功
 */
public class OrderAddTask extends BaseNetTask {

    public OrderAddTask(BaseHttpInformation information,
                        HashMap<String, String> params) {
        super(information, params);
    }

    public OrderAddTask(BaseHttpInformation information,
                        HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {

        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<IdInfo> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public IdInfo parse(JSONObject jsonObject) throws DataParseException {
            return new IdInfo(jsonObject);
        }
    }

}