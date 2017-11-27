package com.hemaapp.tyyjsc.activity;

import android.content.Intent;
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
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.User;

/**
 * 功能：
 * 功能一、注册第二步：设置登录密码；
 *
 * 功能二、用户初始设置支付密码；
 */
public class ActivityRegNext extends BaseActivity {


    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧文字按钮


    private EditText newPwd = null;// 新密码
    private EditText newPwdAgain = null; // 再次输入密码
    private Button reg_next = null; // 下一步
    private String username = null;// 用户名
    private String temptoken = null; // 临时令牌
    private String mobile = null;//邀请人手机号

    private String keytype = "0";//0 设置登录密码（设置密码界面） | 完善个人信息（个人信息界面） 1 设置支付密码 2 编辑用户信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_reg_next);
        super.onCreate(savedInstanceState);
        setColor(mContext);

    }
    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);
        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText("设置密码");
        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.INVISIBLE);
        hmRightTxtView = (TextView)findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.INVISIBLE);
        newPwd = (EditText) findViewById(R.id.firstPwd);
        newPwdAgain = (EditText) findViewById(R.id.secondPwd);
        reg_next = (Button) findViewById(R.id.pwdBtn);
    }

    @Override
    protected void getExras() {
        username = getIntent().getStringExtra("username");
        temptoken = getIntent().getStringExtra("tempToken");
        mobile = getIntent().getStringExtra("invite");
        keytype = getIntent().getStringExtra("keytype");
    }

    @Override
    protected void setListener() {

        if("0".equals(keytype)){
            hmBarNameView.setText("设置密码");
        }else if("1".equals(keytype)){
            hmBarNameView.setText("设置支付密码");
        }
        // 处理返回点击
        hmBackBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 处理下一步
        reg_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String newsPwd = newPwd.getText().toString().trim();
                String newsPwdAgain = newPwdAgain.getText().toString().trim();
                if (isNull(newsPwd)) {
                    showTextDialog("密码不能为空");
                    return;
                }
                if (newsPwd.length() < 6 || newsPwd.length() > 12) {
                    showTextDialog("请设置6-12位密码");
                    return;
                }
                if (isNull(newsPwdAgain)) {
                    showTextDialog("确认密码不能为空");
                    return;
                }
                if (!newsPwd.equals(newsPwdAgain)) {
                  showTextDialog("两次密码输入不一致,请重新输入");
                    return;
                }
                if("0".equals(keytype)){
                    Intent intent = new Intent(mContext, ActivityEditInfo.class);
                    intent.putExtra("temptoken", temptoken); // 临时令牌
                    intent.putExtra("username", username); // 用户名
                    intent.putExtra("password", newsPwd); // 密码
                    intent.putExtra("keytype", keytype);
                    startActivity(intent);
                }else if("1".equals(keytype)){
                    User user = BaseApplication.getInstance().getUser();
                    getNetWorker().payPasswordAdd(user.getToken(),"2",newsPwd);
                }

            }
        });
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PAY_PASSWORD_ADD:
                showProgressDialog(R.string.hm_hlxs_txt_34);
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PAY_PASSWORD_ADD:
                cancelProgressDialog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        final BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PAY_PASSWORD_ADD:
                User user = BaseApplication.getInstance().getUser();
                user.setPaypassword(hemaNetTask.getParams().get("pay_password"));
                BaseApplication.getInstance().setUser(user);
                showTextDialog(getString(R.string.hm_hlxs_txt_60));
                hmBarNameView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setAction(BaseConfig.BROADCAST_PWD);
                        sendBroadcast(intent);
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
            case PAY_PASSWORD_ADD:
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
            case PAY_PASSWORD_ADD:
                break;
            default:
                break;
        }
    }
}
