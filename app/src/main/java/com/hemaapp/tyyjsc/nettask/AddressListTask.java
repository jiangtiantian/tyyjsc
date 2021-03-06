package com.hemaapp.tyyjsc.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.Address;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;


/**
 * 地址列表
 */

public class AddressListTask extends BaseNetTask {

    public AddressListTask(BaseHttpInformation information,
                           HashMap<String, String> params) {
        super(information, params);
    }

    public AddressListTask(BaseHttpInformation information,
                           HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {

        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<Address> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public Address parse(JSONObject jsonObject) throws DataParseException {
            return new Address(jsonObject);
        }
    }
}
