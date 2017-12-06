package com.hemaapp.tyyjsc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.alipay.PayResult;
import com.hemaapp.tyyjsc.model.AlipayTrade;
import com.hemaapp.tyyjsc.model.OrderInfo;
import com.hemaapp.tyyjsc.model.UnionTrade;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.model.WeixinTrade;
import com.hemaapp.tyyjsc.view.HmHelpDialog;
import com.nostra13.universalimageloader.utils.L;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;

import java.util.ArrayList;

import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomToastUtil;

/**
 * 支付页面
 * from = 1: 充值，包括,keytype=1:余额充值,有银联，keytype=2:充值卡充值, 没有银联
 * from = 2: 提交订单
 * from = 3: 订单详情
 * from = 4: 订单列表
 */
public class ActivityPay extends BaseActivity implements View.OnClickListener {

    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧微文字按钮

    //充值
    private LinearLayout inputLy = null;//输入框
    private EditText inputMoneyET = null;
    private LinearLayout payLy = null;//应付金额
    private LinearLayout ownerLy = null;//余额 储蓄卡
    //购买储值卡
    private View line = null;
    private LinearLayout cardLy = null;//储值卡支付
    private ImageView selfPay;//余额支付
    private ImageView card_pay_img;//储值卡支付
    private ImageView alipayImg;//支付宝支付
    private ImageView weixinPayImg;//微信支付
    private ImageView unionPayImg;//银联支付
    private TextView feeaccountTv;//账户余额
    private TextView cardFeecountTv;//储值卡余额
    private TextView realPayTv;//实际支付金额
    private Button pay;//支付按钮
    //统一管理支付方式，实现单选效果
    private ArrayList<ImageView> ways = new ArrayList<>();//支付方式按钮
    private int payIndex = -1;
    private User user = null;
    private HmHelpDialog dialog = null;//密码提示框
    private HmHelpDialog exitDialog = null;//退出提示框
    //参数
    private String money = "0";//支付金额
    private String order_id = "";//订单编号
    private String from = "";//页面来源 1 ： 储值卡列表； 2 订单列表； 3：订单详情;4:确认订单（提交订单）
    private String keytype = "";//1：账户余额充值,2：支付订单,3：账户储蓄卡充值
    private OrderInfo order = null;//仅仅由订单列表进入支付页面是传递参数
    private String give_amount;
    private LinearLayout selfpay_ln;

    IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pay);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        msgApi.registerApp(BaseConfig.APPID_WEIXIN);
        user = BaseApplication.getInstance().getUser();
        getClientInfo();
    }

    //用户详情
    public void getClientInfo() {
        getNetWorker().clientGet(user.getToken(), user.getId());
    }

    // 支付宝
    private void alipayTrade(String keytype, String keyid) {
        User user = BaseApplication.getInstance().getUser();
        String token = user.getToken();
        getNetWorker().alipay(token, "1", keytype, keyid,
                money);
    }

    // 银联
    private void uppayTrade(String keytype, String keyid) {
        User user = BaseApplication.getInstance().getUser();
        String token = user.getToken();
        getNetWorker().unionpay(token, "2", keytype, keyid,
                money);
    }

    // 微信
    private void weixinTrade(String keytype, String keyid) {
        User user = BaseApplication.getInstance().getUser();
        String token = user.getToken();
        getNetWorker().weixinpay(token, "3", keytype, keyid,
                money);
    }

    @Override
    protected void findView() {
        //标题
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);

        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText("点券支付");

        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.GONE);

        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.INVISIBLE);

        inputLy = (LinearLayout) findViewById(R.id.input_ly);
        inputMoneyET = (EditText) findViewById(R.id.input_money_et);
        payLy = (LinearLayout) findViewById(R.id.pay_ly);
        ownerLy = (LinearLayout) findViewById(R.id.owner_ly);

        line = findViewById(R.id.line);
        cardLy = (LinearLayout) findViewById(R.id.card_ly);
        selfpay_ln = (LinearLayout) findViewById(R.id.selfpay_ln);
        selfPay = (ImageView) findViewById(R.id.self_pay);
        card_pay_img = (ImageView) findViewById(R.id.card_pay_img);
        alipayImg = (ImageView) findViewById(R.id.alipay_img);
        weixinPayImg = (ImageView) findViewById(R.id.weixin_pay_img);
        unionPayImg = (ImageView) findViewById(R.id.union_pay_img);
        feeaccountTv = (TextView) findViewById(R.id.feeaccount_tv);
        cardFeecountTv = (TextView) findViewById(R.id.card_feecount_tv);

        realPayTv = (TextView) findViewById(R.id.real_pay_tv);//实际支付金额
        pay = (Button) findViewById(R.id.pay);
    }

    @Override
    protected void getExras() {
        keytype = getIntent().getStringExtra("keytype");
        money = getIntent().getStringExtra("money");
        order_id = getIntent().getStringExtra("order_id");
        from = getIntent().getStringExtra("from");
        order = (OrderInfo) getIntent().getSerializableExtra("order");
    }

    @Override
    protected void setListener() {
//        if ("1".equals(keytype)) {//充值只有支付宝支付、银联支付和微信支付(默认支付宝支付)
//            hmBarNameView.setText("充值");
//            payLy.setVisibility(View.GONE);
//            ownerLy.setVisibility(View.GONE);
//            inputLy.setVisibility(View.VISIBLE);
//            ways.clear();
//            ways.add(alipayImg);
//            ways.add(weixinPayImg);
//            ways.add(unionPayImg);
//            payIndex = 2;
//            setPayWayChecked(alipayImg);
//        } else if ("3".equals(keytype)) {//购买储值卡只支持第三方支付
//            hmBarNameView.setText("支付");
//            payLy.setVisibility(View.VISIBLE);
//            selfpay_ln.setVisibility(View.GONE);
//            ownerLy.setVisibility(View.VISIBLE);
//            inputLy.setVisibility(View.GONE);
//            cardLy.setVisibility(View.GONE);
//            line.setVisibility(View.GONE);
//            ways.clear();
//            ways.add(selfPay);
//            ways.add(alipayImg);
//            ways.add(weixinPayImg);
//            ways.add(unionPayImg);
//            realPayTv.setText("应付金额¥ " + money);
//            payIndex = 0;
//            setPayWayChecked(selfPay);
//        } else if ("2".equals(keytype)) {//购买商品默认余额支付
//            hmBarNameView.setText("支付");
//            payLy.setVisibility(View.VISIBLE);
//            ownerLy.setVisibility(View.VISIBLE);
//            inputLy.setVisibility(View.GONE);
//            ways.clear();
//            ways.add(selfPay);
//            ways.add(card_pay_img);
//            ways.add(alipayImg);
//            ways.add(weixinPayImg);
//            ways.add(unionPayImg);
//            realPayTv.setText("应付金额¥ " + money);
//            payIndex = 0;
//            setPayWayChecked(selfPay);
//        }

        if ("1".equals(from)) {//充值，只有支付宝支付、银联支付和微信支付(默认支付宝支付)
            if ("1".equals(keytype)) {
                hmBarNameView.setText("充值");
                payLy.setVisibility(View.GONE);
                ownerLy.setVisibility(View.GONE);
                inputLy.setVisibility(View.VISIBLE);
                ways.clear();
                ways.add(alipayImg);
                ways.add(weixinPayImg);
                ways.add(unionPayImg);
                payIndex = 2;
                setPayWayChecked(alipayImg);
            } else if ("2".equals(keytype)) {
                hmBarNameView.setText("支付");
                payLy.setVisibility(View.VISIBLE);
                selfpay_ln.setVisibility(View.GONE);
                ownerLy.setVisibility(View.VISIBLE);
                inputLy.setVisibility(View.GONE);
                cardLy.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                ways.clear();
                ways.add(selfPay);
                ways.add(alipayImg);
                ways.add(weixinPayImg);
                ways.add(unionPayImg);
                realPayTv.setText("应付金额¥ " + money);
                payIndex = 0;
                setPayWayChecked(selfPay);
            }
        } else {
            hmBarNameView.setText("支付");
            payLy.setVisibility(View.VISIBLE);
            ownerLy.setVisibility(View.VISIBLE);
            inputLy.setVisibility(View.GONE);
            ways.clear();
            ways.add(selfPay);
            ways.add(card_pay_img);
            ways.add(alipayImg);
            ways.add(weixinPayImg);
            ways.add(unionPayImg);
            realPayTv.setText("应付金额¥ " + money);
            payIndex = 0;
            setPayWayChecked(selfPay);
        }

        hmBackBtn.setOnClickListener(this);
        if (selfPay.getVisibility() == View.VISIBLE) {
            selfPay.setOnClickListener(this);
        }
        if (card_pay_img.getVisibility() == View.VISIBLE) {
            card_pay_img.setOnClickListener(this);
        }
        alipayImg.setOnClickListener(this);
        weixinPayImg.setOnClickListener(this);
        unionPayImg.setOnClickListener(this);
        pay.setOnClickListener(this);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ALIPAY:
            case UNIONPAY:
            case WEIXINPAY:
            case ORDER_PAY:
            case CLIENT_GET:
            case CARD_PAY:
                showProgressDialog(R.string.hm_hlxs_txt_59);
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ALIPAY:
            case UNIONPAY:
            case WEIXINPAY:
            case ORDER_PAY:
            case CLIENT_GET:
            case CARD_PAY:
                cancelProgressDialog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ALIPAY:
                @SuppressWarnings("unchecked")
                HemaArrayResult<AlipayTrade> aResult = (HemaArrayResult<AlipayTrade>) hemaBaseResult;
                AlipayTrade trade = aResult.getObjects().get(0);
                String orderInfo = trade.getAlipaysign();
                new AlipayThread(orderInfo).start();
                break;
            case UNIONPAY:
                @SuppressWarnings("unchecked")
                HemaArrayResult<UnionTrade> uResult = (HemaArrayResult<UnionTrade>) hemaBaseResult;
                UnionTrade uTrade = uResult.getObjects().get(0);
                String uInfo = uTrade.getTn();
//                UPPayAssistEx.startPayByJAR(this, PayActivity.class, null, null,
//                        uInfo, BaseConfig.UNIONPAY_TESTMODE);
                UPPayAssistEx.startPay(this, null, null, uInfo, BaseConfig.UNIONPAY_TESTMODE);
                break;
            case WEIXINPAY:
                @SuppressWarnings("unchecked")
                HemaArrayResult<WeixinTrade> wResult = (HemaArrayResult<WeixinTrade>) hemaBaseResult;
                WeixinTrade wTrade = wResult.getObjects().get(0);
                goWeixin(wTrade);
                break;
            case CLIENT_GET:
                HemaArrayResult<User> userResult = (HemaArrayResult<User>) hemaBaseResult;
                user = userResult.getObjects().get(0);
                feeaccountTv.setText("(当前余额" + user.getFeeaccount() + ")");
                cardFeecountTv.setText("(当前余额" + user.getDebitaccound() + ")");
                break;
            case ORDER_PAY:
            case CARD_PAY:
                showTextDialog("支付成功");
                if ("1".equals(from)) {//储值卡列表
                    //nothing
                    showTextDialog("支付成功");
                    hmBarNameView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cancelTextDialog();
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }, 500);

                } else if ("4".equals(from)) {//订单列表
                    if (order != null) {
                        sendBroadCast();
                    }
                } else if ("3".equals(from)) {//订单详情
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    sendBroadCast();
                } else {//确认订单
                    //nothing

                }
                hmBarNameView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK, mIntent);
                        finish();
                    }
                }, 500);
                break;
            default:
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

    //增送代金券提示文本
    public String getVourcher() {
        if ("3".equals(keytype) && !isNull(give_amount)) {
            return "\n赠送" + "+" + give_amount + "代金券";
        }
        return "";
    }

    private class AlipayThread extends Thread {
        String orderInfo;
        AlipayHandler alipayHandler;

        public AlipayThread(String orderInfo) {
            this.orderInfo = orderInfo;
            alipayHandler = new AlipayHandler(ActivityPay.this);
        }

        @Override
        public void run() {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(ActivityPay.this);
            // 调用支付接口，获取支付结果
            String result = alipay.pay(orderInfo);
            Message msg = new Message();
            msg.obj = result;
            alipayHandler.sendMessage(msg);
        }
    }

    private class AlipayHandler extends Handler {
        ActivityPay activity;

        public AlipayHandler(ActivityPay activity) {
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
                    if ("1".equals(keytype)) {
                        showTextDialog("恭喜\n充值成功");
                        user.setFeeaccount(BaseUtil.transData(Double.parseDouble(user.getFeeaccount()) + Double.parseDouble(money)));
                        BaseApplication.getInstance().setUser(user);
                        Intent intent = new Intent();
                        intent.setAction(BaseConfig.BROADCAST_MONEY);
                        sendBroadcast(intent);
                    } else {
                        showTextDialog("恭喜\n购买成功");
                        if ("1".equals(from)) {//储值卡列表
                            activity.feeaccountTv.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    activity.setResult(RESULT_OK, activity.mIntent);
                                    activity.finish();
                                }
                            }, 1000);
                        } else if ("2".equals(from)) {//订单列表
                            if (order != null) {
                                sendBroadCast();
                            }
                        } else if ("3".equals(from)) {//订单详情
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            sendBroadCast();
                        }
                    }

                    setResult(activity.RESULT_OK, activity.mIntent);
                    activity.finish();
                    break;
                default:
                    showTextDialog("支付取消");
                    break;
            }
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ALIPAY:
            case UNIONPAY:
            case WEIXINPAY:
            case ORDER_PAY:
            case CLIENT_GET:
            case CARD_PAY:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ALIPAY:
            case UNIONPAY:
            case WEIXINPAY:
            case ORDER_PAY:
            case CLIENT_GET:
            case CARD_PAY:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_left:
                if ("2".equals(keytype)) {
                    exitPay();
                } else {
                    finish();
                }
                break;
            case R.id.self_pay:
                setPayWayChecked(v);
                payIndex = 0;
                break;
            case R.id.card_pay_img:
                setPayWayChecked(v);
                payIndex = 1;
                break;
            case R.id.alipay_img:
                setPayWayChecked(v);
                payIndex = 2;
                break;
            case R.id.weixin_pay_img:
                setPayWayChecked(v);
                payIndex = 3;
                break;
            case R.id.union_pay_img:
                setPayWayChecked(v);
                payIndex = 4;
                break;
            case R.id.pay://支付

                /**
                 * from = 1: 充值，包括,keytype=1:余额充值,有银联，keytype=2:充值卡充值, 没有银联
                 * from = 2: 提交订单
                 * from = 3: 订单详情
                 * from = 4: 订单列表
                 * */

                if ("1".equals(from) && "1".equals(keytype)) {
                    money = inputMoneyET.getText().toString();
                }
                //购买商品时可能会存在支付金额为0的情况,要求用户余额支付
                if (payIndex != 0 && !"1".equals(keytype) && Double.parseDouble(money) == 0) {
                    XtomToastUtil.showShortToast(ActivityPay.this, "支付金额为0\n请利用余额支付");
                    return;
                }
                //充值或者是购买储值卡，实际支付金额不能为0
                if (isNull(money) || ("1".equals(from) && Double.parseDouble(money) == 0)) {
                    XtomToastUtil.showShortToast(ActivityPay.this, "请输入有效金额");
                    return;
                }
                //支付金额不能大于50000
                if (Double.parseDouble(money) > 50000) {
                    XtomToastUtil.showShortToast(ActivityPay.this, "金额超限");
                    return;
                }
                if (payIndex == -1) {
                    XtomToastUtil.showShortToast(ActivityPay.this, "请选择支付方式");
                    return;
                }
                switch (payIndex) {
                    case 0://余额支付 | 储值卡购买
                        user = BaseApplication.getInstance().getUser();
                        //判断余额是否充足
                        if (Double.parseDouble(isNull(money) ? "0" : money) > Double.parseDouble(isNull(user.getFeeaccount()) ? "0" : user.getFeeaccount())) {
                            XtomToastUtil.showShortToast(ActivityPay.this, "余额不足");
                            return;
                        }
                        if (dialog == null) {
                            dialog = new HmHelpDialog(ActivityPay.this, 2);
                        }
                        dialog.setPayTxt("支付");
                        dialog.setTitleName("请输入支付密码");
                        dialog.setLeftButtonText("取消");
                        dialog.setRightButtonText("确定");
                        dialog.setListener(new HmHelpDialog.onPwdListener() {
                            @Override
                            public void onSetPwd() {//设置密码
                                Intent intent = new Intent(ActivityPay.this, ActivitySetPayPwd.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onGetPwd() {
                                Intent intent = new Intent(ActivityPay.this, ActivitySetPayPwd.class);
                                startActivity(intent);
                            }
                        });
                        dialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
                            @Override
                            public void onCancel(int type) {
                                dialog.cancel();
                            }

                            @Override
                            public void onConfirm(String msg) {
                                dialog.cancel();
                                String password = dialog.getPassword();
                                dialog.clearPwdET();
                                if (user.getPaypassword() == null || "".equals(user.getPaypassword())) {
                                    XtomToastUtil.showShortToast(ActivityPay.this, "请先设置支付密码");
                                    return;
                                }
                                if (isNull(password)) {
                                    XtomToastUtil.showShortToast(ActivityPay.this, "请输入支付密码");
                                    return;
                                }

                                //余额支付，只能用户支付订单的相关内容
                                getNetWorker().orderPay(user.getToken(), password, order_id, "1");
                            }
                        });
                        dialog.show();
                        break;
                    case 1://储值卡支付
                        //判断余额是否充足
                        if (Double.parseDouble(isNull(money) ? "0" : money) > Double.parseDouble(isNull(user.getDebitaccound()) ? "0" : user.getDebitaccound())) {
                            XtomToastUtil.showShortToast(ActivityPay.this, "余额不足");
                            return;
                        }
                        if (dialog == null) {
                            dialog = new HmHelpDialog(ActivityPay.this, 2);
                        }
                        dialog.setPayTxt("支付");
                        dialog.setTitleName("请输入支付密码");
                        dialog.setLeftButtonText("取消");
                        dialog.setRightButtonText("确定");
                        dialog.setListener(new HmHelpDialog.onPwdListener() {
                            @Override
                            public void onSetPwd() {//设置密码
                                Intent intent = new Intent(ActivityPay.this, ActivitySetPayPwd.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onGetPwd() {
                                Intent intent = new Intent(ActivityPay.this, ActivitySetPayPwd.class);
                                startActivity(intent);
                            }
                        });
                        dialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
                            @Override
                            public void onCancel(int type) {
                                dialog.cancel();
                            }

                            @Override
                            public void onConfirm(String msg) {
                                user = BaseApplication.getInstance().getUser();
                                dialog.cancel();
                                String password = dialog.getPassword();
                                dialog.clearPwdET();
                                if (user.getPaypassword() == null || "".equals(user.getPaypassword())) {
                                    XtomToastUtil.showShortToast(ActivityPay.this, "请先设置支付密码");
                                    return;
                                }
                                if (isNull(password)) {
                                    XtomToastUtil.showShortToast(ActivityPay.this, "请输入支付密码");
                                    return;
                                }
                                dialog.clearPwdET();
                                getNetWorker().orderPay(user.getToken(), password, order_id, "2");
                                dialog.clearPwdInput();
                            }
                        });
                        dialog.show();
                        break;
                    case 2://支付宝支付
                        if ("1".equals(from)) {
                            if ("1".equals(keytype))
                                alipayTrade("1", "0");
                            else if ("2".equals(keytype))
                                alipayTrade("3", order_id);
                        } else {
                            alipayTrade("2", order_id);
                        }
                        break;
                    case 3://微信支付
                        if ("1".equals(from)) {
                            if ("1".equals(keytype))
                                weixinTrade("1", "0");
                            else if ("2".equals(keytype))
                                weixinTrade("3", order_id);
                        } else {
                            weixinTrade("2", order_id);
                        }

                        break;
                    case 4://银联支付
                        showTextDialog("暂未开通");
//                        if ("0".equals(keytype)) {
//                            uppayTrade("1", "0");
//                        } else if ("1".equals(keytype)) {
//                            uppayTrade("3", order_id);
//                        } else {
//                            uppayTrade("2", order_id);
//                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*************************************************
         * 处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            if ("1".equals(from) && "1".equals(keytype)) {
                showTextDialog("恭喜\n" +
                        "充值成功");
                user.setFeeaccount(BaseUtil.transData(Double.parseDouble(user.getFeeaccount()) + Double.parseDouble(money)));
                BaseApplication.getInstance().setUser(user);
                Intent intent = new Intent();
                intent.setAction(BaseConfig.BROADCAST_MONEY);
                sendBroadcast(intent);
            } else {
                showTextDialog("恭喜\n购买成功");
                if ("1".equals(from)) {//储值卡列表
                    feeaccountTv.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setResult(RESULT_OK, mIntent);
                            finish();
                        }
                    }, 1000);
                } else if ("2".equals(from)) {//订单列表
                    sendBroadCast();
                } else if ("3".equals(from)) {//订单详情
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    sendBroadCast();
                } else {//确认订单

                }
            }
            hmBarNameView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setResult(RESULT_OK, mIntent);
                    finish();
                }
            }, 500);
        } else if (str.equalsIgnoreCase("fail")) {
            showTextDialog("支付失败");
        } else if (str.equalsIgnoreCase("cancel")) {
            showTextDialog("支付取消");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void sendBroadCast() {
        if (order != null) {
            Intent intent = new Intent();
            intent.setAction(BaseConfig.BROADCAST_ORDER);
            order.setStatus("2");
            intent.putExtra("order", order);
            sendBroadcast(intent);
        }
    }

    @Override
    protected void onResume() {
        String wx = XtomSharedPreferencesUtil
                .get(getApplicationContext(), "wx");
        if ("0".equals(wx)) {

            if ("0".equals(keytype)) {
                showTextDialog("恭喜\n充值成功");
                user.setFeeaccount(BaseUtil.transData(Double.parseDouble(user.getFeeaccount()) + Double.parseDouble(money)));
                BaseApplication.getInstance().setUser(user);
                Intent intent = new Intent();
                intent.setAction(BaseConfig.BROADCAST_MONEY);
                sendBroadcast(intent);
            } else {
                showTextDialog("恭喜\n购买成功");
                if ("1".equals(from)) {//储值卡列表
                    feeaccountTv.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setResult(RESULT_OK, mIntent);
                            finish();
                        }
                    }, 1000);
                } else if ("2".equals(from)) {//订单列表
                    sendBroadCast();
                } else if ("3".equals(from)) {//订单详情
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    sendBroadCast();
                }
            }
            hmBarNameView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setResult(RESULT_OK, mIntent);
                    finish();
                }
            }, 500);
        } else if ("-1".equals(wx)) {
            showTextDialog("支付失败");
        } else if ("-2".equals(wx)) {
            showTextDialog("支付取消");
        }
        XtomSharedPreferencesUtil.save(getApplicationContext(), "wx", "");
        super.onResume();
    }

    //单选效果
    public void setPayWayChecked(View curView) {
        for (ImageView item : ways) {
            item.setImageResource(R.mipmap.address_uncheck);
        }
        ImageView curImg = (ImageView) curView;
        curImg.setImageResource(R.mipmap.address_checked);
    }

    //退出提示框
    public void exitPay() {
        if (exitDialog == null) {
            exitDialog = new HmHelpDialog(ActivityPay.this, 0);
        }
        exitDialog.setLeftButtonText("取消");
        exitDialog.setRightButtonText("确定");
        exitDialog.setTitleName("订单将会在24小时后失效, 确定退出？");
        exitDialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
            @Override
            public void onCancel(int type) {
                exitDialog.cancel();
            }

            @Override
            public void onConfirm(String msg) {
                exitDialog.cancel();
                finish();
            }
        });
        exitDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ("1".equals(from)) {//充值不需要
                setResult(RESULT_OK, mIntent);
                finish();
            } else {
                exitPay();
            }
        }
        return true;
    }
}
