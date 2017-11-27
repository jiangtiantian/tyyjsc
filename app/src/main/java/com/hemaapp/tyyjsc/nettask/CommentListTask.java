package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.Comment;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 评论列表
 */
public class CommentListTask extends BaseNetTask {

    public CommentListTask(BaseHttpInformation information,
                           HashMap<String, String> params) {
        super(information, params);
    }
    public CommentListTask(BaseHttpInformation information,
                           HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }
    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {

        return new Result(jsonObject);
    }
    private class Result extends HemaPageArrayResult<Comment> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }
        @Override
        public Comment parse(JSONObject jsonObject) throws DataParseException {
            return new Comment(jsonObject);
        }
    }
}
