package com.hemaapp.tyyjsc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.Notice;
import com.hemaapp.tyyjsc.model.SignValueInfo;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.ButtonDialog;

/**
 * 签到
 */
public class ActivityNoticeInfo extends BaseActivity implements View.OnClickListener{
    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private TextView hmRightTxtView = null;//标题栏右侧文字按钮
    private TextView date = null;
    private TextView name = null;
    private ImageView dot = null;
    private TextView del;
    private View divider;
    private Notice notice;
    private User user = null;//用户个人信息
    private SignValueInfo signValueInfo = null;//签到可获取经验值
    private ButtonDialog dialog = null;//积分提示框
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.notice_info);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        init();
    }
    public void init(){
        name.setText(notice.getContent());
        date.setText(notice.getRegdate());
    }

    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);
        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText("消息详情");
        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.GONE);
        date = (TextView)findViewById(R.id.date);
        name = (TextView)findViewById(R.id.name);
        del = (TextView)findViewById(R.id.del_tv);
        divider = findViewById(R.id.divider);
        dot = (ImageView)findViewById(R.id.dot);
    }

    @Override
    protected void getExras() {
        notice= (Notice) getIntent().getSerializableExtra("notice");
    }

    @Override
    protected void setListener() {
        hmBackBtn.setOnClickListener(this);
        hmRightTxtView.setOnClickListener(this);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
            case USER_SIGN:
            case SIGN_VALUE_GET:
                showProgressDialog(R.string.hm_hlxs_txt_11);
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
            case CLIENT_GET:
            case USER_SIGN:
            case SIGN_VALUE_GET:
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
            case CLIENT_GET:
                HemaArrayResult<User> infoResult = (HemaArrayResult<User>) hemaBaseResult;
                user = infoResult.getObjects().get(0);
                init();
                break;
            case SIGN_VALUE_GET:
                showTextDialog("签到成功");
                HemaArrayResult<SignValueInfo> signValueResult = (HemaArrayResult<SignValueInfo>) hemaBaseResult;
                signValueInfo = signValueResult.getObjects().get(0);
//                init();
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
            case CLIENT_GET: showTextDialog(hemaBaseResult.getMsg());
                break;
            case SIGN_VALUE_GET:
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
            case CLIENT_GET:
                break;
            case USER_SIGN:
                break;
            case  SIGN_VALUE_GET:
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
        }
    }
}

