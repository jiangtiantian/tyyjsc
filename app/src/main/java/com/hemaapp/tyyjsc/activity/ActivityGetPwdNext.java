package com.hemaapp.tyyjsc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.User;

import xtom.frame.XtomActivityManager;
import xtom.frame.util.XtomSharedPreferencesUtil;


/**
 * 找回登录密码 设置密码
 * 找回支付提现密码 设置密码
 */
public class ActivityGetPwdNext extends BaseActivity {


    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧文字按钮
    private EditText newPwd = null;// 新密码
    private EditText newPwdAgain = null; // 再次输入密码
    private Button reg_next = null; // 下一步

    private String username = null;// 用户名
    private String temptoken = null; // 临时令牌
    private String keytype = "";//密码类型 1 登录密码 2 支付密码
    private String password = null;//密码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_get_pwd_next);
        super.onCreate(savedInstanceState);
        setColor(mContext);
    }

    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);
        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText("修改密码");
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
        keytype = getIntent().getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        if("1".equals(keytype)){
            hmBarNameView.setText("修改密码");
        }else if("2".equals(keytype)){
            hmBarNameView.setText("忘记密码");
        }
        // 处理下一步
        reg_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newsPwd = newPwd.getText().toString().trim();
                String newsPwdAgain = newPwdAgain.getText().toString().trim();
                if (isNull(newsPwd)) {
                  showTextDialog("请输入新密码");
                    return;
                }
                if (newsPwd.length() < 6 || newsPwd.length() > 12) {
                    showTextDialog("请输入6到12位有效密码");
                    return;
                }
                if (isNull(newsPwdAgain)) {
                    showTextDialog("请输入确认密码");
                    return;
                }
                if (!newsPwd.equals(newsPwdAgain)) {
                    showTextDialog("前后密码输入不一致");
                    return;
                }
                password = newsPwd;
                getNetWorker().passwordReset(temptoken, password, keytype);
            }
        });
        //返回
        hmBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
            case PASSWORD_RESET:
                showProgressDialog("请稍后");
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
            case PASSWORD_RESET:
              cancelProgressDialog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();

        switch (information) {
            case PASSWORD_RESET:
                if ("1".equals(keytype)) {
                    getNetWorker().clientLogin(username, password);
                } else {
                    showTextDialog("密码修改成功");
                    hmBarNameView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rollBackActivity();
                        }
                    }, 500);
                }
                break;
            case CLIENT_LOGIN:
                @SuppressWarnings("unchecked")
                HemaArrayResult<User> uResult = (HemaArrayResult<User>) hemaBaseResult;
                User u = uResult.getObjects().get(0);
                BaseApplication.getInstance().setUser(u);
                XtomSharedPreferencesUtil.save(mContext, "username", hemaNetTask
                        .getParams().get("username"));
                XtomSharedPreferencesUtil.save(mContext, "password", hemaNetTask
                        .getParams().get("password"));
                // 自动登录
                XtomSharedPreferencesUtil.save(mContext, "isAutoLogin", "true");
                Intent intent = new Intent();
                intent.setAction(BaseConfig.BROADCAST_ACTION);
                sendBroadcast(intent);

                //发送广播，保存设备信息并与cid绑定
                Intent clientIntent = new Intent();
                clientIntent.setAction("com.hemaapp.push.connect");
                sendBroadcast(clientIntent);

                hmBarNameView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        rollBackActivity();
                    }
                }, 500);
                break;
            default:
                break;
        }
    }
    public void rollBackActivity(){
        Activity activityGetPwd = XtomActivityManager.getActivityByClass(ActivityGetPwd.class);
        Activity activityLogin = XtomActivityManager.getActivityByClass(Login.class);
        if(activityGetPwd != null){
            activityGetPwd.finish();
        }
        if(activityLogin != null){
            activityLogin.finish();
        }
        finish();
    }
    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();

        switch (information) {
            case PASSWORD_RESET:
            case CLIENT_LOGIN:
               showTextDialog("网络错误");
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }
}
