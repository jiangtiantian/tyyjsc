package com.hemaapp.tyyjsc.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.User;

/**
 * 账户管理
 */
public class ActivityAccountManager extends BaseActivity implements View.OnClickListener {

    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧微文字按钮
    private LinearLayout forgetPwdLy = null;//忘记支付或提现密码
    private LinearLayout loginLy = null;//修改登录密码
    private View lineView = null;//分割线
    private User user = null;
    private OnPayChangeReceiver payChangeReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_account_manager);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        init();
        payChangeReceiver = new OnPayChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BaseConfig.BROADCAST_PWD);
        registerReceiver(payChangeReceiver, filter);
    }

    public void init() {
        user = BaseApplication.getInstance().getUser();
//        if (user!= null || "".equals(user.getPaypassword())) {
//            setPwdTV.setText(R.string.hm_hlxs_txt_24);
//        } else {
//            setPwdTV.setText(R.string.hm_hlxs_txt_25);
//        }
    }

    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);

        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText(R.string.hm_hlxs_txt_26);

        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.GONE);

        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.INVISIBLE);
        forgetPwdLy = (LinearLayout) findViewById(R.id.forget_pwd_ly);
        loginLy = (LinearLayout) findViewById(R.id.login_ly);
        lineView = findViewById(R.id.line);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        if(HemaUtil.isThirdSave(mContext)){//如果为第三方登录， 隐藏修改登录密码
            loginLy.setVisibility(View.GONE);
            lineView.setVisibility(View.GONE);
        }else{
            loginLy.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);
        }
        hmBackBtn.setOnClickListener(this);
        forgetPwdLy.setOnClickListener(this);
        loginLy.setOnClickListener(this);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_left:
                finish();
                break;
//            case R.id.set_pwd_ly:
//                if (user.getPaypassword() == null || "".equals(user.getPaypassword())) {//设置支付提现密码
//                    intent = new Intent(ActivityAccountManager.this, ActivityRegNext.class);
//                    intent.putExtra("keytype", "1");
//                    startActivity(intent);
//                } else {//修改支付提现密码
//                    intent = new Intent(ActivityAccountManager.this, ActivityModifyPwd.class);
//                    intent.putExtra("keytype", "2");
//                    startActivity(intent);
//                }
               // break;
            case R.id.forget_pwd_ly:
                intent = new Intent(ActivityAccountManager.this, ActivityGetPwd.class);
                intent.putExtra("keytype", "2");
                startActivity(intent);
                break;
            case R.id.login_ly:
                intent = new Intent(ActivityAccountManager.this, ActivityModifyPwd.class);
                intent.putExtra("keytype", "1");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(payChangeReceiver);
    }
    class OnPayChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            init();
        }
    }
}
