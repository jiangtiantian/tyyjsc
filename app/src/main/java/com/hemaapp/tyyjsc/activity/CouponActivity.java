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
import com.hemaapp.tyyjsc.adapters.CouponAdapter;
import com.hemaapp.tyyjsc.model.AllcouponRecord;
import com.hemaapp.tyyjsc.model.User;

import java.util.ArrayList;
import java.util.List;

import xtom.frame.view.XtomListView;

/**
 * w我的点券
 */
public class CouponActivity extends BaseActivity implements View.OnClickListener {

    private TextView bar_name;
    private ImageButton back_left;
    private TextView btn_recharge;
    private TextView tv_couponNumber;
    private XtomListView users_listview;
    private User user;
    private List<AllcouponRecord> datas=new ArrayList<>();
    private CouponAdapter adapter;
    private int page=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_coupon);
        super.onCreate(savedInstanceState);
        user= BaseApplication.getInstance().getUser();
        getNetWorker().clientGet(user.getToken(),user.getId());  //用户信息详情查询
        getNetWorker().allcouponecord(user.getToken());//点券记录
    }

    @Override
    protected void findView() {
        bar_name = (TextView) findViewById(R.id.bar_name);
        back_left = (ImageButton) findViewById(R.id.back_left);
        btn_recharge = (TextView) findViewById(R.id.btn_recharge);
        tv_couponNumber = (TextView) findViewById(R.id.tv_couponNumber);
        users_listview = (XtomListView) findViewById(R.id.users_listview);

        users_listview.setEmptyView(findViewById(R.id.tv_empty));

        adapter=new CouponAdapter(this,datas);
        users_listview.setAdapter(adapter);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        bar_name.setText("我的点券");
        back_left.setOnClickListener(this);
        btn_recharge.setOnClickListener(this);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showProgressDialog(getString(R.string.hm_hlxs_txt_11));
            case FEEACCOUNT_LIST:
            case SCORERECORD_LIST:

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
                cancelProgressDialog();
            case FEEACCOUNT_LIST:
            case SCORERECORD_LIST:
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
                tv_couponNumber.setText(user.getPointcoupon());
                break;
            case ALLCOUPON_RECORD:
                HemaArrayResult<AllcouponRecord> dataResult= (HemaArrayResult<AllcouponRecord>) hemaBaseResult;

                datas.addAll(dataResult.getObjects());
                adapter.notifyDataSetChanged();
                break;
        }
    }
    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_left:
                finish();
                break;
            case R.id.btn_recharge:
                startActivity(new Intent(this,CouponPayActivity.class));
                break;
        }
    }
}
