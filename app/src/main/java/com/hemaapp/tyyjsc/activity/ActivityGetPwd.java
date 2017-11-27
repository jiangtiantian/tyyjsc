package com.hemaapp.tyyjsc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;


/**
 * 找回登录密码
 * 找回支付密码
 */
public class ActivityGetPwd extends BaseActivity implements View.OnClickListener{

    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧文字按钮

    private EditText userNameET = null;//手机号输入框
    private EditText userPwdET = null;//密码输入框
    private TextView code = null;//显示验证码发送位置
    private TextView send = null;// 发送验证码
    private Button nextBtn = null;//下一步

    private String username;//用户名
    private TimeThread timeThread;//60s倒计时

    private String keytype = "";//密码类型 1 登录密码 2 支付密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_get_pwd);
        super.onCreate(savedInstanceState);
        setColor(mContext);
    }
    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);

        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText("忘记密码");

        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.INVISIBLE);

        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.INVISIBLE);

        userNameET = (EditText)findViewById(R.id.userName);
        userNameET.setFilters(new InputFilter[] { new InputFilter.LengthFilter(11)});
        userPwdET = (EditText)findViewById(R.id.userPwd);
        send = (TextView)findViewById(R.id.send);
        nextBtn = (Button)findViewById(R.id.next);
        code = (TextView)findViewById(R.id.code);
    }

    @Override
    protected void getExras() {
        keytype = getIntent().getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        if("1".equals(keytype)){
            hmBarNameView.setText("修改密码");
        }else if("2".equals(keytype)){
            hmBarNameView.setText("忘记密码");
        }
        hmBackBtn.setOnClickListener(this);
        send.setOnClickListener(new SendButtonListener());
        nextBtn.setOnClickListener(this);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                showProgressDialog("请稍后");
                break;
            case CODE_GET:
                showProgressDialog("请稍后");
                break;
            case CODE_VERIFY:
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
            case CLIENT_VERIFY:
            case CODE_GET:
            case CODE_VERIFY:
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
            case CLIENT_VERIFY:
                username = hemaNetTask.getParams().get("username");
                getNetWorker().codeGet(username);
                break;
            case CODE_GET:
                code.setText("已发送到"+HemaUtil.hide(username, "1"));
                code.setVisibility(View.VISIBLE);
                timeThread = new TimeThread(new TimeHandler(this));
                timeThread.start();
                break;
            case CODE_VERIFY:
                if (timeThread != null) {
                    timeThread.cancel();
                    timeThread = null;
                }
                code.setVisibility(View.INVISIBLE);
                userPwdET.setText("");
                @SuppressWarnings("unchecked")
                HemaArrayResult<String> sResult = (HemaArrayResult<String>) hemaBaseResult;
                String tempToken = sResult.getObjects().get(0);
                Intent it = new Intent(mContext, ActivityGetPwdNext.class);
                it.putExtra("username", username);
                it.putExtra("tempToken", tempToken);
                it.putExtra("keytype", keytype);
                startActivity(it);
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            case CODE_GET:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            case CODE_VERIFY:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                break;
            case CODE_GET:
                break;
            case CODE_VERIFY:
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_left:
                finish();
                break;
            case R.id.next:
                String username = userNameET.getText().toString();
                if (isNull(username)) {
                    showTextDialog("请输入用户名");
                    return;
                }
                String code = userPwdET.getText().toString().trim();
                if (isNull(code)) {
                    showTextDialog("请输入验证码");
                }
                getNetWorker().codeVerify(username, code);
                break;
            default:
                break;
        }
    }
    private class SendButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // 保证一次只开启一个线程，避免重复开启线程
            if (timeThread != null && timeThread.curr != 0) {
                return;
            }
            String username = userNameET.getText().toString();
            if (isNull(username)) {
                showTextDialog("请输入用户名");
                return;
            }

            String mobile = "^[1][3-8]+\\d{9}";
            if (!username.matches(mobile)) {
                showTextDialog("您输入的手机号有误");
                return;
            }
            getNetWorker().clientVerify(username);
        }
    }
    private class TimeThread extends Thread {
        private int curr;

        private TimeHandler timeHandler;

        public TimeThread(TimeHandler timeHandler) {
            this.timeHandler = timeHandler;
        }

        void cancel() {
            curr = 0;
        }

        @Override
        public void run() {
            curr = 60;
            while (curr > 0) {
                timeHandler.sendEmptyMessage(curr);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // ignore
                }
                curr--;
            }
            timeHandler.sendEmptyMessage(-1);
        }
    }
    private static class TimeHandler extends Handler {
        ActivityGetPwd activity;

        public TimeHandler(ActivityGetPwd activity) {
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    activity.send.setText("重新发送");
                    activity.send.postInvalidate();
                    activity.code.setVisibility(View.INVISIBLE);
                    activity.send.setVisibility(View.VISIBLE);
                    break;
                default:
                    activity.code.setVisibility(View.VISIBLE);
                    activity.send.setVisibility(View.VISIBLE);
                    // 处理倒计时显示问题
                    SpannableString str = new SpannableString(msg.what + "秒");
//                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(
//                            activity.getResources().getColor(R.color.sort_txt_p));
//                    str.setSpan(colorSpan, 4,
//                            String.valueOf(msg.what).length() + 4,
//                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    activity.send.setText(str);
                    activity.send.postInvalidate();
                    break;
            }
        }
    }
    @Override
    protected void onDestroy() {
        if (timeThread != null) {
            timeThread.cancel();
            timeThread = null;
        }
        super.onDestroy();
    }
}
