package com.hemaapp.tyyjsc.nettask;

import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by zuozhongqian on 2017/8/8.
 */

public class Exchangemoney extends BaseNetTask {
    public Exchangemoney(BaseHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    public Exchangemoney(BaseHttpInformation information, HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new HemaBaseResult(jsonObject);
    }
}
