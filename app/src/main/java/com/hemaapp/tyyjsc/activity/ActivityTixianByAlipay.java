package com.hemaapp.tyyjsc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.HmHelpDialog;

import xtom.frame.util.XtomToastUtil;

/**
 * Created by wangyuxia on 2017/8/11.
 * 支付宝提现
 */
public class ActivityTixianByAlipay extends BaseActivity implements View.OnClickListener {

    private ImageButton left;
    private TextView title;
    private ImageButton right;

    private TextView text_feeaccount;
    private EditText editText;
    private TextView text_alipay_no;
    private LinearLayout layout_alipay;
    private TextView tv_submit;

    private User user;
    private String money, alipay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tixian_alipay);
        super.onCreate(savedInstanceState);
        user = BaseApplication.getInstance().getUser();
        text_feeaccount.setText(money + "元");
        alipay = user.getAlipay();
        text_alipay_no.setText(isNull(alipay) ? "" : alipay);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CASH_ADD:
                showProgressDialog("请稍后");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CASH_ADD:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CASH_ADD:
                showTextDialog(hemaBaseResult.getMsg());
                title.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK, mIntent);
                        finish();
                    }
                }, 1000);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CASH_ADD:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CASH_ADD:
                showTextDialog("抱歉，提交申请失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        left = (ImageButton) findViewById(R.id.back_left);
        right = (ImageButton) findViewById(R.id.bar_right_img);
        title = (TextView) findViewById(R.id.bar_name);

        text_feeaccount = (TextView) findViewById(R.id.tv_feeaccount);
        editText = (EditText) findViewById(R.id.edittext);
        layout_alipay = (LinearLayout) findViewById(R.id.linearlayout);
        text_alipay_no = (TextView) findViewById(R.id.tv_alipay_no);
        tv_submit = (TextView) findViewById(R.id.bt_submit);
    }

    @Override
    protected void getExras() {
        money = mIntent.getStringExtra("money");
    }

    @Override
    protected void setListener() {
        title.setText("支付宝提现");
        right.setVisibility(View.INVISIBLE);
        left.setOnClickListener(this);
        layout_alipay.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = editable.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2)
                {
                    editable.delete(posDot + 3, posDot + 4);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.back_left:
                finish();
                break;
            case R.id.linearlayout:
                it = new Intent(mContext, EditAlipayActivity.class);
                startActivityForResult(it, R.id.layout);
                break;
            case R.id.bt_submit:
                value = editText.getText().toString();
                if (isNull(value)) {
                    showTextDialog("抱歉，请输入提现金额");
                    return;
                }

                double d = Double.parseDouble(value);
                double m = Double.parseDouble(money);
                if (d > m) {
                    showTextDialog("抱歉，您的账户余额不足，无法提现");
                    return;
                }

//                if (d % 100 > 0) {
//                    showTextDialog("抱歉，提现金额必须是100的整数倍");
//                    return;
//                }

                if (d > 9999999.99) {
                    showTextDialog("抱歉，提现金额过大，请重新输入");
                    return;
                }

                showPwdPop();
                break;
        }
    }

    private HmHelpDialog dialog = null;//密码提示框
    private String value;

    private void showPwdPop() {
        if (dialog == null) {
            dialog = new HmHelpDialog(ActivityTixianByAlipay.this, 2);
        }
        dialog.setPayTxt("支付");
        dialog.setTitleName("请输入支付密码");
        dialog.setLeftButtonText("取消");
        dialog.setRightButtonText("确定");
        dialog.setListener(new HmHelpDialog.onPwdListener() {
            @Override
            public void onSetPwd() {//设置密码
                Intent intent = new Intent(ActivityTixianByAlipay.this, ActivitySetPayPwd.class);
                startActivity(intent);
            }

            @Override
            public void onGetPwd() {
                Intent intent = new Intent(ActivityTixianByAlipay.this, ActivitySetPayPwd.class);
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
                    XtomToastUtil.showShortToast(ActivityTixianByAlipay.this, "请先设置支付密码");
                    return;
                }
                if (isNull(password)) {
                    XtomToastUtil.showShortToast(ActivityTixianByAlipay.this, "请输入支付密码");
                    return;
                }
                getNetWorker().cashAdd(user.getToken(), value, "1", password);
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case R.id.layout:
                alipay = data.getStringExtra("alipay");
                text_alipay_no.setText(alipay);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
