package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.HM_ImgInfo;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 获取图片信息
 */
public class ImgInfoGetTask extends BaseNetTask {

    public ImgInfoGetTask(BaseHttpInformation information,
                          HashMap<String, String> params) {
        super(information, params);
    }

    public ImgInfoGetTask(BaseHttpInformation information,
                          HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<HM_ImgInfo> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public HM_ImgInfo parse(JSONObject jsonObject) throws DataParseException {
            return new HM_ImgInfo(jsonObject);
        }

    }
}