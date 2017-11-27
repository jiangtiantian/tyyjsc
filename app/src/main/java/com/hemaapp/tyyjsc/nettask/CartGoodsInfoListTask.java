package com.hemaapp.tyyjsc.nettask;


import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetTask;
import com.hemaapp.tyyjsc.model.ShopCart;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 购物车列表
 */
public class CartGoodsInfoListTask extends BaseNetTask {

    public CartGoodsInfoListTask(BaseHttpInformation information,
                                 HashMap<String, String> params) {
        super(information, params);
    }

    public CartGoodsInfoListTask(BaseHttpInformation information,
                                 HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<ShopCart> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public ShopCart parse(JSONObject jsonObject) throws DataParseException {
            return new ShopCart(jsonObject);
        }
    }
}