package com.hemaapp.tyyjsc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetWorker;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.ShowAdapter;
import com.hemaapp.tyyjsc.model.User;

import xtom.frame.util.XtomSharedPreferencesUtil;
/**
 * 引导页
 */
public class ActivityGuide extends BaseActivity implements ShowAdapter.onFinishListener {
    private ViewPager mViewPager;
    private RelativeLayout layout;
    private TextView start;
    private ShowAdapter mAdapter;
    public boolean isAutomaticLogin = false;// 是否自动登录
    private User user;
    private int[] imgs = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_guide);
        super.onCreate(savedInstanceState);
        imgs = new int[]{R.mipmap.banner01, R.mipmap.banner02,R.mipmap.banner03,R.mipmap.banner04};
        mAdapter = new ShowAdapter(mContext, imgs, layout);
        mViewPager.setAdapter(mAdapter);
        mAdapter.setListener(this);
    }
    @Override
    protected void findView() {
        start = (TextView) findViewById(R.id.button);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        layout = (RelativeLayout) findViewById(R.id.layout);
    }
    @Override
    protected void getExras() {

    }
    @Override
    protected void setListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                ViewGroup indicator = mAdapter.getmIndicator();
                if (indicator != null) {
                    RadioButton rbt = (RadioButton) indicator
                            .getChildAt(position);
                    if (rbt != null)
                        rbt.setChecked(true);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
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
            case CLIENT_LOGIN:
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
            case CLIENT_LOGIN:
                HemaArrayResult<User> uResult = (HemaArrayResult<User>) hemaBaseResult;
                user = uResult.getObjects().get(0);
                BaseApplication.getInstance().setUser(user);
                toMain();
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
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
            case CLIENT_LOGIN:

                break;
            default:
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        return false;
    }

    @Override
    protected boolean onKeyMenu() {
        return false;
    }

    @Override
    public void finish() {
        // 判断是否自动登录
        if (isAutoLogin()) {
            String username = XtomSharedPreferencesUtil.get(this, "username");
            String password = XtomSharedPreferencesUtil.get(this, "password");
            if (!isNull(username) && !isNull(password)) {
                BaseNetWorker netWorker = getNetWorker();
                netWorker.clientLogin();
            } else {
                toMain();
            }
        } else {
            toMain();
        }
        super.finish();
    }

    //进入首页
    private void toMain() {
        Intent it = new Intent(mContext, ActivityIndex.class);
        startActivity(it);
    }

    //标记已执行过引导页，下次初始化后直接进入首页
    @Override
    protected void onDestroy() {
        XtomSharedPreferencesUtil.save(mContext, "isShowed", "true");
        super.onDestroy();
    }

    // 检查是否自动登录
    private boolean isAutoLogin() {
        isAutomaticLogin = "false".equals(XtomSharedPreferencesUtil.get(
                mContext, "isAutoLogin"));
        return !isAutomaticLogin;
    }

    @Override
    public void onFinish() {
        finish();
    }
}
