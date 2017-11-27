package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.WeixinTrade;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 微信支付
 */
public class WeixinTradeTask extends BaseNetTask {

    public WeixinTradeTask(BaseHttpInformation information,
                           HashMap<String, String> params) {
        super(information, params);
    }
    public WeixinTradeTask(BaseHttpInformation information,
                           HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }
    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }
    private class Result extends HemaArrayResult<WeixinTrade> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }
        @Override
        public WeixinTrade parse(JSONObject jsonObject)
                throws DataParseException {
            return new WeixinTrade(jsonObject);
        }
    }
}
