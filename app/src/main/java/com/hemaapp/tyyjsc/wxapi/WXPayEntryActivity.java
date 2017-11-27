package com.hemaapp.tyyjsc.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import xtom.frame.util.XtomSharedPreferencesUtil;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpayentry);

        api = WXAPIFactory.createWXAPI(this, BaseConfig.APPID_WEIXIN);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        //0成功，展示成功页面
        //-1错误，可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
        //-2用户取消，无需处理。发生场景：用户不支付了，点击取消，返回APP。
        Log.e("-----------", "onResp: =" + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            if (resp.errCode == 0) {
                XtomSharedPreferencesUtil.save(getApplicationContext(), "wx", "0");
            } else if (resp.errCode == -1) {
                XtomSharedPreferencesUtil.save(getApplicationContext(), "wx", "-1");
            } else {
                XtomSharedPreferencesUtil.save(getApplicationContext(), "wx", "-2");
            }
            finish();
        }
    }
}