package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.User;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 登录：登录成功后返回用户信息
 */
public class ClientLoginTask extends BaseNetTask {

    public ClientLoginTask(BaseHttpInformation information,
                           HashMap<String, String> params) {
        super(information, params);
    }

    public ClientLoginTask(BaseHttpInformation information,
                           HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<User> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public User parse(JSONObject jsonObject) throws DataParseException {
            return new User(jsonObject);
        }

    }
}
