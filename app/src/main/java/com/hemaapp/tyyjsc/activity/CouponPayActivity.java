package com.hemaapp.tyyjsc.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.alipay.PayResult;
import com.hemaapp.tyyjsc.model.AlipayTrade;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.model.WeixinTrade;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class CouponPayActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TextView bar_name;
    private ImageButton back_left;
    private EditText input_money_et;
    private TextView tv_hint;
    private CheckBox alipay_img;
    private CheckBox weixin_pay_img;
    private CheckBox union_pay_img;
    private Button btn_pay;
    private User user;
    private String money;
    private boolean statue_ali = true;
    private boolean statue_weixin = false;
    IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_coupon_pay);
        super.onCreate(savedInstanceState);
        user = BaseApplication.getInstance().getUser();
        getNetWorker().clientGet(user.getToken(), user.getId());
        msgApi.registerApp(BaseConfig.APPID_WEIXIN);
    }

    @Override
    protected void findView() {
        bar_name = (TextView) findViewById(R.id.bar_name);
        back_left = (ImageButton) findViewById(R.id.back_left);
        input_money_et = (EditText) findViewById(R.id.input_money_et);
        alipay_img = (CheckBox) findViewById(R.id.alipay_img);
        weixin_pay_img = (CheckBox) findViewById(R.id.weixin_pay_img);
        union_pay_img = (CheckBox) findViewById(R.id.union_pay_img);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        bar_name.setText("点券充值");
        back_left.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
        alipay_img.setOnCheckedChangeListener(this);
        weixin_pay_img.setOnCheckedChangeListener(this);
        union_pay_img.setOnCheckedChangeListener(this);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ALIPAY:
                HemaArrayResult<AlipayTrade> aResult = (HemaArrayResult<AlipayTrade>) hemaBaseResult;
                AlipayTrade trade = aResult.getObjects().get(0);
                String orderInfo = trade.getAlipaysign();
                new AlipayThread(orderInfo).start();
                break;
            case WEIXINPAY:
                HemaArrayResult<WeixinTrade> wResult = (HemaArrayResult<WeixinTrade>) hemaBaseResult;
                WeixinTrade wTrade = wResult.getObjects().get(0);
                goWeixin(wTrade);
                break;
            case CLIENT_GET:
                HemaArrayResult<User> uResult = (HemaArrayResult<User>) hemaBaseResult;
                user = uResult.getObjects().get(0);
                tv_hint.setText(user.getPointcoupon_money() + "元" + "= 1点券" );
                break;
        }
    }

    //微信支付
    private void goWeixin(WeixinTrade trade) {
        msgApi.registerApp(BaseConfig.APPID_WEIXIN);
        PayReq request = new PayReq();
        request.appId = trade.getAppid();
        request.partnerId = trade.getPartnerid();
        request.prepayId = trade.getPrepayid();
        request.packageValue = trade.getPackageValue();
        request.nonceStr = trade.getNoncestr();
        request.timeStamp = trade.getTimestamp();
        request.sign = trade.getSign();
        msgApi.sendReq(request);
    }

    private class AlipayThread extends Thread {
        String orderInfo;
        AlipayHandler alipayHandler;

        public AlipayThread(String orderInfo) {
            this.orderInfo = orderInfo;
            alipayHandler = new AlipayHandler(CouponPayActivity.this);
        }

        @Override
        public void run() {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(CouponPayActivity.this);
            // 调用支付接口，获取支付结果
            String result = alipay.pay(orderInfo);

            log_i("result = " + result);
            Message msg = new Message();
            msg.obj = result;
            alipayHandler.sendMessage(msg);
        }
    }

    private class AlipayHandler extends Handler {
        CouponPayActivity activity;

        public AlipayHandler(CouponPayActivity activity) {
            this.activity = activity;
        }

        public void handleMessage(Message msg) {
            if (msg == null) {
                showTextDialog("支付失败");
                return;
            }
            PayResult result = new PayResult((String) msg.obj);
            String staus = result.getResultStatus();
            switch (staus) {
                case "9000":
                    showTextDialog("恭喜\n充值成功");
                   /* if ("1".equals(keytype)) {
                        showTextDialog("恭喜\n充值成功");
                        user.setFeeaccount(BaseUtil.transData(Double.parseDouble(user.getFeeaccount()) + Double.parseDouble(money)));
                        BaseApplication.getInstance().setUser(user);
                        Intent intent = new Intent();
                        intent.setAction(BaseConfig.BROADCAST_MONEY);
                        sendBroadcast(intent);
                    } else {
                        showTextDialog("恭喜\n购买成功");
                        if ("1".equals(from)) {//储值卡列表
                            //nothing
                        } else if ("2".equals(from)) {//订单列表
                            if (order != null) {
                                sendBroadCast();
                            }
                        } else if ("3".equals(from)) {//订单详情
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            sendBroadCast();
                        } else {//确认订单

                        }}*/

                    break;
                default:
                    showTextDialog("支付取消");
                    break;
            }
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_left:
                finish();
                break;
            case R.id.btn_pay:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        money = input_money_et.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            showTextDialog("请输入金额");
            return;
        }
        if (statue_ali == true && statue_weixin == true) {
            showTextDialog("只能选择一种支付方式");
            return;
        } else if (statue_ali == false && statue_weixin == false) {
            showTextDialog("请选择一种支付方式");
        } else {
            if (statue_ali) {//阿里支付
                getNetWorker().alipay(user.getToken(), "1", "4", "0", money);
            } else if (statue_weixin) {//微信支付
                getNetWorker().weixinpay(user.getToken(), "3", "4", "0", money);
            }
        }
    }

    //checkbox监听
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.alipay_img:
                if (isChecked == true) {
                    statue_ali = true;
                    weixin_pay_img.setChecked(false);
                } else {
                    statue_ali = false;
                }
                break;
            case R.id.weixin_pay_img:
                if (isChecked) {
                    statue_weixin = true;
                    alipay_img.setChecked(false);
                } else {
                    statue_weixin = false;
                }

                break;

        }

    }


}
