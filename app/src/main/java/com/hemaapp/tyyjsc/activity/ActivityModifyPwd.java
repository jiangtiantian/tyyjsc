package com.hemaapp.tyyjsc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomToastUtil;


/**
 * 1 修改登录密码； 2 修改支付密码
 */
public class ActivityModifyPwd extends BaseActivity {

    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧文字按钮

    private EditText oldPwdET = null;
    private EditText newPwdET = null;
    private EditText newPwdAgainET = null;
    private Button confirmBtn = null;

    private String keytype = "";//1 修改登录密码； 2 修改支付密码
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_modify_pwd);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        user = BaseApplication.getInstance().getUser();
    }

    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);

        hmBarNameView = (TextView) findViewById(R.id.bar_name);

        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.INVISIBLE);
        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.GONE);


        oldPwdET = (EditText) findViewById(R.id.oldpwd);
        newPwdET = (EditText) findViewById(R.id.newpwd);
        newPwdAgainET = (EditText) findViewById(R.id.newpwdagain);
        confirmBtn = (Button) findViewById(R.id.confirm);
    }

    @Override
    protected void getExras() {
        keytype = getIntent().getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        if("1".equals(keytype)){
            hmBarNameView.setText("修改登录密码");
        }else{
            hmBarNameView.setText("修改支付/提现密码");
        }
        hmBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 提交
        confirmBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String oldpwd = oldPwdET.getText().toString().trim();
                String newpwd = newPwdET.getText().toString().trim();
                String newpwdagain = newPwdAgainET.getText().toString().trim();
                if (isNull(oldpwd)) {
                    XtomToastUtil.showShortToast(ActivityModifyPwd.this, "请输入当前密码");
                    return;
                }
                if (isNull(newpwd)) {
                    XtomToastUtil.showShortToast(ActivityModifyPwd.this, "请输入新密码");
                    return;
                }
                if (newpwd.length() < 6 || newpwd.length() > 16) {
                    XtomToastUtil.showShortToast(ActivityModifyPwd.this, "请设置6-12位密码");
                    return;
                }
                if (isNull(newpwdagain)) {
                    XtomToastUtil.showShortToast(ActivityModifyPwd.this, "请再次输入新密码");
                    return;
                }
                if (!newpwd.equals(newpwdagain)) {
                    XtomToastUtil.showShortToast(ActivityModifyPwd.this, "两次输入密码不一致");
                    return;
                }
                getNetWorker().passwordSave(user.getToken(),keytype, oldpwd, newpwd);
            }
        });
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PASSWORD_SAVE:
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
            case PASSWORD_SAVE:
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
            case PASSWORD_SAVE:
               showTextDialog(getString(R.string.hm_hlxs_txt_60));
                if("1".equals(keytype)){//修改登录密码时，更新本地密码
                    XtomSharedPreferencesUtil.save(ActivityModifyPwd.this,"password", hemaNetTask.getParams().get("new_password"));
                }
                hmBarNameView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },500);
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PASSWORD_SAVE:
               showTextDialog(hemaBaseResult.getMsg());
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PASSWORD_SAVE:
                break;
            default:
                break;
        }
    }
}
