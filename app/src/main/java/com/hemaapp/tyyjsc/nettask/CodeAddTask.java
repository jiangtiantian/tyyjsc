package com.hemaapp.tyyjsc.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.CodeAdd;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by zuozhongqian on 2017/8/10.
 */

public class CodeAddTask extends BaseNetTask {
    public CodeAddTask(BaseHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    public CodeAddTask(BaseHttpInformation information, HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new CodeAddResult(jsonObject);
    }
    public  class CodeAddResult extends HemaArrayResult<CodeAdd>{

        public CodeAddResult(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public CodeAdd parse(JSONObject jsonObject) throws DataParseException {
            return  new CodeAdd(jsonObject);
        }
    }
}
