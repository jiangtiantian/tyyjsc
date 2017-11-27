package com.hemaapp.tyyjsc.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.User;
import xtom.frame.util.XtomToastUtil;

/**
 * 意见反馈
 */
public class ActivityAdvice extends BaseActivity implements View.OnClickListener{
    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧微文字按钮

    private EditText adviceET = null;
    private TextView numView = null;

    private Button sendBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_advice);
        super.onCreate(savedInstanceState);
        setColor(mContext);
    }

    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);

        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText(R.string.hm_hlxs_txt_58);

        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.GONE);

        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.INVISIBLE);

        adviceET = (EditText)findViewById(R.id.advice);
        numView = (TextView)findViewById(R.id.num);
        adviceET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(140)});
        sendBtn = (Button)findViewById(R.id.handin);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        hmBackBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        adviceET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                numRedChange(String.valueOf(s.toString().length()).length(), String.valueOf(s.toString().length()) + "/140");
            }
        });
    }
    public void numRedChange(int length, String str){
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.index_search_bg));
        ssb.setSpan(span, 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        numView.setText(ssb);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADVICE_ADD:
                showProgressDialog(getString(R.string.hm_hlxs_txt_59));
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADVICE_ADD:
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
            case ADVICE_ADD:
               showTextDialog("感谢您的意见反馈");
                adviceET.setText("");
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
            case ADVICE_ADD:
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
            case ADVICE_ADD:

                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if(v == hmBackBtn){
            finish();
        }else if(v == sendBtn){
            String advice = adviceET.getText().toString().trim();
            if(isNull(advice)){
                XtomToastUtil.showShortToast(ActivityAdvice.this, getString(R.string.hm_hlxs_txt_61));
                return;
            }
            User user = BaseApplication.getInstance().getUser();
            getNetWorker().adviceAdd(user.getToken(),"Android Phone", HemaUtil.getAppVersionForSever(mContext), android.os.Build.MODEL,android.os.Build.VERSION.RELEASE, advice);
        }
    }
}
