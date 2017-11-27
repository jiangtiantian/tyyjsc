package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.FileUploadResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;


/**
 * 图片上传
 */
public class FileUploadTask extends BaseNetTask {

    public FileUploadTask(BaseHttpInformation information,
                          HashMap<String, String> params) {
        super(information, params);
    }

    public FileUploadTask(BaseHttpInformation information,
                          HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<FileUploadResult> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public FileUploadResult parse(JSONObject jsonObject)
                throws DataParseException {
            return new FileUploadResult(jsonObject);
        }

    }
}
