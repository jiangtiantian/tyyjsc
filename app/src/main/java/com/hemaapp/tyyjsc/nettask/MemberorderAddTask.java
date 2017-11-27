package com.hemaapp.tyyjsc.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.MemberorderAdd;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by zuozhongqian on 2017/8/7.
 */

public class MemberorderAddTask extends BaseNetTask {
    public MemberorderAddTask(BaseHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    public MemberorderAddTask(BaseHttpInformation information, HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new MemberorderAddResult(jsonObject);
    }

    public class  MemberorderAddResult extends HemaArrayResult<MemberorderAdd>{

        public MemberorderAddResult(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public MemberorderAdd parse(JSONObject jsonObject) throws DataParseException {
            return new MemberorderAdd(jsonObject);
        }
    }
}
