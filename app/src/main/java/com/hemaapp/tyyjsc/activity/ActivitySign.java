package com.hemaapp.tyyjsc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.SignValueInfo;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.ButtonDialog;

/**
 * 签到
 */
public class ActivitySign extends BaseActivity implements View.OnClickListener{
    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private TextView hmRightTxtView = null;//标题栏右侧文字按钮
    private TextView sign;
    private TextView score;
    private TextView signTxt = null;
    private TextView tipView = null;
    private User user = null;//用户个人信息
    private SignValueInfo signValueInfo = null;//签到可获取经验值
    private ButtonDialog dialog = null;//积分提示框
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sign);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        user = BaseApplication.getInstance().getUser();
        getNetWorker().clientGet(user.getToken(), user.getId());
    }
    //签到
    public void getExperienceToday(){
        getNetWorker().getSignValue(user.getToken());
    }
    //根据签到状态显示对应布局
    public void init(){
            if(user != null && "0".equals(user.getIs_Sign())){//未签到
                tipView.setText("连续签到"+user.getSignday()+"天");
                sign.setText(R.string.hm_hlxs_txt_146);
            }else{//已签到
                tipView.setVisibility(View.VISIBLE);
                tipView.setText("连续签到"+user.getSignday()+"天");
                sign.setText("已签到");
                score.setText(Integer.parseInt(score.getText().toString())+1+"");
            }
            score.setText(user.getSignscore());
    }

    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);
        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText("签到");
        sign= (TextView) findViewById(R.id.sign);
        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.VISIBLE);
        hmRightTxtView.setText("签到攻略");
        score= (TextView) findViewById(R.id.score);
        tipView = (TextView)findViewById(R.id.tipview);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        hmBackBtn.setOnClickListener(this);
        hmRightTxtView.setOnClickListener(this);
        sign.setOnClickListener(this);
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
                tipView.setVisibility(View.VISIBLE);
                tipView.setText("连续签到"+user.getSignday()+"天");
                sign.setText("已签到");
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
                tipView.setVisibility(View.VISIBLE);
                tipView.setText("连续签到"+user.getSignday()+"天");
                sign.setText("已签到");
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
            case R.id.sign:
                if(user != null && "0".equals( user.getIs_Sign())){//未签到
                    getExperienceToday();
                }else{//已签到
                    showTextDialog(getString(R.string.hm_hlxs_txt_155));
                }
                break;
            case R.id.bar_right_txt:
                Intent intent = new Intent(ActivitySign.this, ActivityWebView.class);
                intent.putExtra("keytype", "4");
                intent.putExtra("keyid", "0");
                startActivity(intent);
                break;
        }
    }
}
