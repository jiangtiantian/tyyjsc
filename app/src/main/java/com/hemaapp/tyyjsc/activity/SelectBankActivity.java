package com.hemaapp.tyyjsc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.BankListAdapter;
import com.hemaapp.tyyjsc.model.BankInfo;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by wangyuxia on 2017/8/11.
 * 银行列表
 */
public class SelectBankActivity extends BaseActivity{

    private ImageButton left;
    private TextView tv_title;

    private RefreshLoadmoreLayout layout;
    private XtomListView mListView;
    private ProgressBar progressBar;

    private ArrayList<BankInfo> infors = new ArrayList<>();
    private BankListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_listview);
        super.onCreate(savedInstanceState);
        getList();
    }

    private void getList(){
        getNetWorker().getBankList();
    }

    private void setAdapter(){
        if(adapter == null){
            adapter = new BankListAdapter(mContext, infors);
            adapter.setEmptyString("抱歉，暂时没有数据");
            mListView.setAdapter(adapter);
        }else{
            adapter.setEmptyString("抱歉，暂时没有数据");
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case BANK_LIST:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case BANK_LIST:
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case BANK_LIST:
                HemaArrayResult<BankInfo> sResult = (HemaArrayResult<BankInfo>) hemaBaseResult;
                ArrayList<BankInfo> banks = sResult.getObjects();
                layout.refreshSuccess();
                infors.addAll(banks);
                setAdapter();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case BANK_LIST:
                layout.refreshFailed();
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information){
            case BANK_LIST:
                layout.refreshFailed();
                showTextDialog("获取失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        left = (ImageButton) findViewById(R.id.back_left);
        tv_title = (TextView) findViewById(R.id.bar_name);

        layout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        mListView = (XtomListView) findViewById(R.id.listview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    protected void getExras() {
    }

    @Override
    protected void setListener() {
        tv_title.setText("选择银行");
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        layout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getList();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
            }
        });
        layout.setLoadmoreable(false);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(infors != null && infors.size() > 0){
                    mIntent.putExtra("id", infors.get(i).getId());
                    mIntent.putExtra("name", infors.get(i).getName());
                    setResult(RESULT_OK,  mIntent);
                    finish();
                }
            }
        });
    }
}
