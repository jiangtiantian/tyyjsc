package com.hemaapp.tyyjsc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.HmHelpDialog;

import xtom.frame.util.XtomToastUtil;

/**
 * Created by wangyuxia on 2017/8/11.
 * 银行卡提现
 */
public class ActivityTixianByUnionpay extends BaseActivity implements View.OnClickListener{

    private ImageButton left;
    private TextView title;
    private ImageButton right;

    private EditText editText;
    private TextView tv_feeaccount;
    private RelativeLayout layout;
    private LinearLayout layout_bank;
    private TextView tv_bank_name;
    private TextView tv_bank_username;
    private TextView tv_bank_no;
    private TextView bt_submit;

    private String money, bank_name, bank_username, bank_number, bank_address;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tixian_unionpay);
        super.onCreate(savedInstanceState);
        user = BaseApplication.getInstance().getUser();
        tv_feeaccount.setText(money+"元");
        bank_name = user.getBankname();
        bank_username = user.getBankuser();
        bank_number = user.getBankcard();
        bank_address = user.getBankaddress();
        if(isNull(bank_name) || isNull(bank_username) || isNull(bank_number)){
            layout_bank.setVisibility(View.GONE);
        }else{
            layout_bank.setVisibility(View.VISIBLE);
            tv_bank_name.setText(bank_name);
            tv_bank_username.setText(bank_username);
            tv_bank_no.setText(bank_number);
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case CASH_ADD:
                showProgressDialog("请稍后");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case CASH_ADD:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
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
        switch (information){
            case CASH_ADD:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
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

        editText = (EditText) findViewById(R.id.edittext);
        tv_feeaccount = (TextView) findViewById(R.id.tv_feeaccount);
        layout = (RelativeLayout) findViewById(R.id.layout_bank);
        layout_bank = (LinearLayout) findViewById(R.id.linearlayout);
        tv_bank_name = (TextView) findViewById(R.id.tv_bank_name);
        tv_bank_username = (TextView) findViewById(R.id.tv_bank_user_name);
        tv_bank_no = (TextView) findViewById(R.id.tv_bank_num);
        bt_submit = (TextView) findViewById(R.id.bt_submit);
    }

    @Override
    protected void getExras() {
        money = mIntent.getStringExtra("money");
    }

    @Override
    protected void setListener() {
        title.setText("银行卡提现");
        right.setVisibility(View.INVISIBLE);
        left.setOnClickListener(this);
        layout.setOnClickListener(this);
        bt_submit.setOnClickListener(this);

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
        switch (view.getId()){
            case R.id.back_left:
                finish();
                break;
            case R.id.layout_bank:
                Intent it = new Intent(mContext, EditBankInforActivity.class);
                it.putExtra("bank_name", bank_name);
                it.putExtra("bank_card", bank_number);
                it.putExtra("bank_user", bank_username);
                it.putExtra("bank_address", bank_address);
                startActivityForResult(it, R.id.layout);
                break;
            case R.id.bt_submit:
                if(layout_bank.getVisibility() == View.GONE){
                    showTextDialog("抱歉，请填写银行卡信息");
                    return;
                }

                value = editText.getText().toString();
                if(isNull(value)){
                    showTextDialog("抱歉，请输入提现金额");
                    return;
                }

                double d = Double.parseDouble(value);
                double m = Double.parseDouble(money);
                if(d > m){
                    showTextDialog("抱歉，您的账户余额不足，无法提现");
                    return;
                }

//                if( d % 100 > 0){
//                    showTextDialog("抱歉，提现金额必须是100的整数倍");
//                    return;
//                }

                if(d > 9999999.99){
                    showTextDialog("抱歉，提现金额过大，请重新输入");
                    return;
                }
                showPwdPop();

                break;
        }
    }

    private String value;
    private HmHelpDialog dialog = null;//密码提示框
    private void showPwdPop(){
        if (dialog == null) {
            dialog = new HmHelpDialog(ActivityTixianByUnionpay.this, 2);
        }
        dialog.setPayTxt("支付");
        dialog.setTitleName("请输入支付密码");
        dialog.setLeftButtonText("取消");
        dialog.setRightButtonText("确定");
        dialog.setListener(new HmHelpDialog.onPwdListener() {
            @Override
            public void onSetPwd() {//设置密码
                Intent intent = new Intent(ActivityTixianByUnionpay.this, ActivitySetPayPwd.class);
                startActivity(intent);
            }

            @Override
            public void onGetPwd() {
                Intent intent = new Intent(ActivityTixianByUnionpay.this, ActivitySetPayPwd.class);
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
                    XtomToastUtil.showShortToast(ActivityTixianByUnionpay.this, "请先设置支付密码");
                    return;
                }
                if (isNull(password)) {
                    XtomToastUtil.showShortToast(ActivityTixianByUnionpay.this, "请输入支付密码");
                    return;
                }
                getNetWorker().cashAdd(user.getToken(), value, "2", password);
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK)
            return;
        switch (requestCode){
            case R.id.layout:
                bank_username = data.getStringExtra("bank_user");
                bank_number = data.getStringExtra("bank_card");
                bank_name = data.getStringExtra("bank_name");
                bank_address = data.getStringExtra("bank_address");
                layout_bank.setVisibility(View.VISIBLE);
                tv_bank_name.setText(bank_name);
                tv_bank_username.setText(bank_username);
                tv_bank_no.setText(bank_number);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
