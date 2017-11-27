package com.hemaapp.tyyjsc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.User;

/**
 * Created by wangyuxia on 2017/8/11.
 * 编辑支付宝账号
 */
public class EditAlipayActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton left;
    private TextView title;
    private ImageButton right;
    private EditText editText;
    private TextView tv_submit;

    private User user;
    private String alipay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_alipay);
        super.onCreate(savedInstanceState);
        user = BaseApplication.getInstance().getUser();
        alipay = user.getAlipay();
        if (!isNull(alipay)) {
            editText.setText(alipay);
            editText.setSelection(alipay.length());
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ALI_SAVE:
                showProgressDialog("正在保存");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ALI_SAVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ALI_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
                user.setAlipay(alipay);
                BaseApplication.getInstance().setUser(user);
                title.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIntent.putExtra("alipay", alipay);
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
            case ALI_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ALI_SAVE:
                showTextDialog("保存失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        left = (ImageButton) findViewById(R.id.back_left);
        title = (TextView) findViewById(R.id.bar_name);
        right = (ImageButton) findViewById(R.id.bar_right_img);

        editText = (EditText) findViewById(R.id.edittext);
        tv_submit = (TextView) findViewById(R.id.bt_submit);
    }

    @Override
    protected void getExras() {
    }

    @Override
    protected void setListener() {
        title.setText("支付宝账号");
        right.setVisibility(View.INVISIBLE);
        tv_submit.setText("提交");
        left.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_left:
                finish();
                break;
            case R.id.bt_submit:
                alipay = editText.getText().toString();
                if (isNull(alipay)) {
                    showTextDialog("抱歉，请输入支付宝账号");
                    return;
                }
                getNetWorker().aliSave(user.getToken(), alipay);
                break;
        }
    }
}
