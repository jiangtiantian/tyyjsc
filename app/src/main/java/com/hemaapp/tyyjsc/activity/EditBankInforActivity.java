package com.hemaapp.tyyjsc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.BankInfo;
import com.hemaapp.tyyjsc.model.User;

import java.util.ArrayList;

/**
 * Created by wangyuxia on 2017/8/11.
 * 编辑银行卡信息
 */
public class EditBankInforActivity extends BaseActivity implements View.OnClickListener{
    private ImageButton left;
    private ImageButton right;
    private TextView title;

    private LinearLayout layout_bank;
    private TextView tv_select_bank;
    private EditText edit_bankuser;
    private EditText edit_bankcard;
    private EditText edit_bankname;
    private TextView tv_submit;

    private User user;
    private String bank_name, bank_user, bank_card, bank_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_unionpay);
        super.onCreate(savedInstanceState);
        user = BaseApplication.getInstance().getUser();

        if (!isNull(bank_name)) {
            tv_select_bank.setText(bank_name);
        }

        if (!isNull(bank_user)) {
            edit_bankuser.setText(bank_user);
            edit_bankuser.setSelection(bank_user.length());
        }

        if (!isNull(bank_card)) {
            edit_bankcard.setText(bank_card);
            edit_bankcard.setSelection(bank_card.length());
        }

        if( !isNull(bank_address)){
            edit_bankname.setText(bank_address);
            edit_bankname.setSelection(bank_address.length());
        }

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case BANK_SAVE:
                showTextDialog("正在保存...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case BANK_LIST:
            case BANK_SAVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){

            case BANK_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
                user.setBankcard(bank_card);
                user.setBankname(bank_name);
                user.setBankuser(bank_user);
                user.setBankaddress(bank_address);
                BaseApplication.getInstance().setUser(user);
                title.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIntent.putExtra("bank_card", bank_card);
                        mIntent.putExtra("bank_user", bank_user);
                        mIntent.putExtra("bank_name", bank_name);
                        mIntent.putExtra("bank_address", bank_address);
                        setResult(RESULT_OK, mIntent);
                        finish();
                    }
                }, 1000);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case BANK_LIST:
                showTextDialog("获取数据失败，请稍后重试");
                break;
            case BANK_SAVE:
                showTextDialog("保存失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        left = (ImageButton) findViewById(R.id.back_left);
        right = (ImageButton) findViewById(R.id.bar_right_img);
        title = (TextView) findViewById(R.id.bar_name);

        layout_bank = (LinearLayout) findViewById(R.id.layout_bank);
        tv_select_bank = (TextView) findViewById(R.id.tv_bank_kind);
        edit_bankuser = (EditText) findViewById(R.id.edittext_name);
        edit_bankcard = (EditText) findViewById(R.id.edittext_number);
        edit_bankname = (EditText) findViewById(R.id.edittext_bank_name);
        tv_submit = (TextView) findViewById(R.id.bt_submit);
    }

    @Override
    protected void getExras() {
        bank_name = mIntent.getStringExtra("bank_name");
        bank_user = mIntent.getStringExtra("bank_user");
        bank_card = mIntent.getStringExtra("bank_card");
        bank_address = mIntent.getStringExtra("bank_address");
    }

    @Override
    protected void setListener() {
        title.setText("银行卡信息");
        left.setOnClickListener(this);
        layout_bank.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_left:
                finish();
                break;
            case R.id.layout_bank:
                Intent it = new Intent(mContext, SelectBankActivity.class);
                startActivityForResult(it, R.id.layout_bank);
                break;
            case R.id.bt_submit:
                if(isNull(bank_name)){
                    showTextDialog("抱歉，请选择银行");
                    return;
                }
                bank_user = edit_bankuser.getText().toString().trim();
                if(isNull(bank_user)){
                    showTextDialog("抱歉，请填写户主姓名");
                    return;
                }

                bank_card = edit_bankcard.getText().toString().trim();
                if(isNull(bank_card)){
                    showTextDialog("抱歉，请填写银行卡号");
                    return;
                }

                bank_address = edit_bankname.getText().toString().trim();
                if(isNull(bank_address)){
                    showTextDialog("抱歉，请填写开户行名称");
                    return;
                }
                getNetWorker().bankSave(user.getToken(), bank_name, bank_user, bank_address, bank_card );
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK)
            return;
        switch (requestCode){
            case R.id.layout_bank:
                bank_name = data.getStringExtra("name");
                tv_select_bank.setText(bank_name);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
