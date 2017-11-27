package com.hemaapp.tyyjsc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.CircleAdapter;
import com.hemaapp.tyyjsc.model.RecordInfor;
import com.hemaapp.tyyjsc.model.User;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by wangyuxia on 2017/8/17.
 * 朋友圈 -- 种子用户可见
 */
public class CircleActivity extends BaseActivity{

    private ImageButton img_back;
    private TextView tv_title;

    private RelativeLayout title_shop; //邀请的好友
    private TextView tv_shop;
    private ImageView img_shop;
    private RelativeLayout title_service; //朋友圈
    private TextView tv_service;
    private ImageView img_service;

    private LinearLayout layout_friend;
    private LinearLayout layout_circle;

    private TextView tv_person_count;
    private TextView tv_money_count;
    private RefreshLoadmoreLayout layout;
    private XtomListView mListView;

    private TextView tv_team_count;
    private TextView tv_team_money_count;

    private User user;
    private int page = 0;
    private ArrayList<RecordInfor> infors = new ArrayList<>();
    private CircleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_circle);
        super.onCreate(savedInstanceState);
        user = BaseApplication.getInstance().getUser();
        getClientInfor();
    }

    private void getClientInfor(){
        getNetWorker().clientGet(user.getToken(), user.getId());
    }

    private void getList(){
        getNetWorker().lineClientRecord(user.getToken(), page);
    }

    private void freshData(){
        if(adapter == null){
            adapter = new CircleAdapter(mContext, infors);
            adapter.setEmptyString("抱歉，您暂时没有邀请好友");
            mListView.setAdapter(adapter);
        }else{
            adapter.setEmptyString("抱歉，您暂时没有邀请好友");
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case CLIENT_GET:
                showProgressDialog("请稍后...");
                break;
            case LINECLIENT_RECORD:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case CLIENT_GET:
                break;
            case LINECLIENT_RECORD:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case CLIENT_GET:
                HemaArrayResult<User> uResult = (HemaArrayResult<User>) hemaBaseResult;
                user = uResult.getObjects().get(0);
                tv_person_count.setText(user.getInvitenum());
                tv_money_count.setText("￥"+user.getAllconvertmoney());
                tv_team_count.setText(user.getAllinvitenum());
                tv_team_money_count.setText("￥"+user.getAllsalemoney());
                getList();
                break;
            case LINECLIENT_RECORD:
                String page = hemaNetTask.getParams().get("page");
                int sysPagesize = BaseApplication.getInstance().getSysInitInfo()
                        .getSys_pagesize();
                HemaPageArrayResult<RecordInfor> nResult = (HemaPageArrayResult<RecordInfor>) hemaBaseResult;
                ArrayList<RecordInfor> list = nResult.getObjects();
                if ("0".equals(page)) {// 刷新
                    layout.refreshSuccess();
                    infors.clear();
                    infors.addAll(list);
                    if (list.size() < sysPagesize)
                        layout.setLoadmoreable(false);
                    else
                        layout.setLoadmoreable(true);
                } else {// 更多
                    layout.loadmoreSuccess();
                    if (list.size() > 0) {
                        infors.addAll(list);
                        if (list.size() < sysPagesize)
                            layout.setLoadmoreable(false);
                        else
                            layout.setLoadmoreable(true);
                    } else {
                        layout.setLoadmoreable(false);
                        XtomToastUtil.showShortToast(mContext, getString(R.string.hm_hlxs_txt_89));
                    }
                }
                freshData();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case CLIENT_GET:
                showTextDialog(hemaBaseResult.getMsg());
                getList();
                break;
            case LINECLIENT_RECORD:
                showTextDialog("抱歉，获取数据失败");
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {// 刷新
                    layout.refreshFailed();
                    freshData();
                } else {// 更多
                    layout.loadmoreFailed();
                }
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case CLIENT_GET:
                showTextDialog("获取数据失败，请稍后重试");
                getList();
                break;
            case LINECLIENT_RECORD:
                showTextDialog("抱歉，获取数据失败");
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {// 刷新
                    layout.refreshFailed();
                    freshData();
                } else {// 更多
                    layout.loadmoreFailed();
                }
                break;
        }
    }

    @Override
    protected void findView() {
        img_back = (ImageButton) findViewById(R.id.back_left);
        tv_title = (TextView) findViewById(R.id.bar_name);
        title_shop = (RelativeLayout) findViewById(R.id.layout_shop);
        tv_shop = (TextView) findViewById(R.id.tv_shop);
        img_shop = (ImageView) findViewById(R.id.img_shop);
        title_service = (RelativeLayout) findViewById(R.id.layout_service);
        tv_service = (TextView) findViewById(R.id.tv_service);
        img_service = (ImageView) findViewById(R.id.img_service);

        layout_friend = (LinearLayout) findViewById(R.id.layout_friends);
        tv_person_count = (TextView) findViewById(R.id.tv_person_count);
        tv_money_count = (TextView) findViewById(R.id.tv_money_count);
        layout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        mListView = (XtomListView) findViewById(R.id.listview);
        layout_circle = (LinearLayout) findViewById(R.id.layout_circle);

        tv_team_count = (TextView) findViewById(R.id.tv_team_count);
        tv_team_money_count = (TextView) findViewById(R.id.tv_total_money);
    }

    @Override
    protected void getExras() {
    }

    @Override
    protected void setListener() {
        tv_title.setText("我的朋友圈");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_shop.setTextColor(mContext.getResources().getColor(R.color.color_gaga));
                img_shop.setVisibility(View.VISIBLE);
                tv_service.setTextColor(0xff828181);
                img_service.setVisibility(View.INVISIBLE);
                layout_friend.setVisibility(View.VISIBLE);
                layout_circle.setVisibility(View.GONE);
            }
        });

        title_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_shop.setTextColor(0xff828181);
                img_shop.setVisibility(View.INVISIBLE);
                tv_service.setTextColor(mContext.getResources().getColor(R.color.color_gaga));
                img_service.setVisibility(View.VISIBLE);
                layout_friend.setVisibility(View.GONE);
                layout_circle.setVisibility(View.VISIBLE);
            }
        });

        layout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getClientInfor();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                getList();
            }
        });
    }
}
