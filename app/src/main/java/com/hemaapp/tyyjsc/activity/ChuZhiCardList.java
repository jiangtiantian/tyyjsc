package com.hemaapp.tyyjsc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.DealNoteAdapter;
import com.hemaapp.tyyjsc.model.DealItem;
import com.hemaapp.tyyjsc.model.RecommendUserInfo;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.CheckCardDialog;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 账户余额、储值卡、积分、收入四个页面
 */
public class ChuZhiCardList extends BaseActivity implements View.OnClickListener{
    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧文字按钮
    //1.账户余额 2.积分
    private TextView txtView = null;
    private XtomListView listView = null;//收入
    private ArrayList<RecommendUserInfo> data = new ArrayList<>();
    private int page = 0;
    private RefreshLoadmoreLayout refreshLoadmoreLayout = null;
    private PopupWindow popupWindow = null;
    private User user = null;//用户信息
    private CheckCardDialog checkCardDialog = null;//核销验证码弹框
    private ArrayList<DealItem> deals = new ArrayList<>();
    private DealNoteAdapter dealNoteAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.base_list);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        user=BaseApplication.getInstance().getUser();
        if(user!=null)
        {
            getRecordList();
        }else
        {
            Intent intent=new Intent(this,Login.class);
            startActivity(intent);
        }
    }
    //获取明细
    public void getRecordList(){
        getNetWorker().debitcard_record(user.getToken(),String.valueOf(page));
    }
    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);
        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.GONE);
        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.GONE);
        txtView = (TextView)findViewById(R.id.txt);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout)findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView)findViewById(R.id.users);
    }
    @Override
    protected void getExras() {

    }
    @Override
    protected void setListener() {
            hmBarNameView.setText("购买记录");
            freshDealData();
            hmBackBtn.setOnClickListener(this);
            hmRightBtn.setOnClickListener(this);
            refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
                @Override
                public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                        page = 0;
                    getRecordList();
                }
                @Override
                public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                    page++;
                    getRecordList();
                }
            });
        }

    //明细详情
    public void freshDealData(){
        if(dealNoteAdapter == null){
            dealNoteAdapter = new DealNoteAdapter(mContext, deals);
            dealNoteAdapter.setEmptyString("暂无数据");
            dealNoteAdapter.isHiddenFirstDivider(true);
            dealNoteAdapter.setKeytype("0");
            listView.setAdapter(dealNoteAdapter);
        }else{
            dealNoteAdapter.setData(deals);
            dealNoteAdapter.setEmptyString("暂无数据");
            dealNoteAdapter.setKeytype("0");
            dealNoteAdapter.notifyDataSetChanged();
        }
        BaseUtil.setListViewHeightBasedOnChildren(listView);
    }


    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
            case RECOMMEND_LIST:
            case CARD_ADD:
                showProgressDialog(getString(R.string.hm_hlxs_txt_11));
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
            case RECOMMEND_LIST:
            case CARD_ADD:
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
            case DEBITCARD_RECORD:
                String page1 = hemaNetTask.getParams().get("page");
                int sysPagesize1 = BaseApplication.getInstance().getSysInitInfo()
                        .getSys_pagesize();
                HemaPageArrayResult<DealItem> nResult1 = (HemaPageArrayResult<DealItem>) hemaBaseResult;
                ArrayList<DealItem> list1 = nResult1.getObjects();
                if ("0".equals(page1)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    deals.clear();
                    deals.addAll(list1);
                    if (list1.size() < sysPagesize1)
                        refreshLoadmoreLayout.setLoadmoreable(false);
                    else
                        refreshLoadmoreLayout.setLoadmoreable(true);
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (list1.size() > 0) {
                        deals.addAll(list1);
                        if (list1.size() < sysPagesize1)
                            refreshLoadmoreLayout.setLoadmoreable(false);
                        else
                            refreshLoadmoreLayout.setLoadmoreable(true);
                    } else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        XtomToastUtil.showShortToast(mContext, getString(R.string.hm_hlxs_txt_89));
                    }
                }
                freshDealData();
                break;
            case CARD_ADD:
                if(checkCardDialog != null){
                    checkCardDialog.clearET();
                }
                this.page = 0;
                getNetWorker().clientGet(user.getToken(), user.getId());
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
            case CLIENT_GET:
            case RECOMMEND_LIST:
            case CARD_ADD:
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
            case CLIENT_GET:
                break;
            case RECOMMEND_LIST:
                break;
            case CARD_ADD:
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.back_left:
                finish();
                break;
            case R.id.bar_right_img://说明
//                intent = new Intent(ActivityAccountBalance.this, ActivityWebView.class);
//                intent.putExtra("keytype", "5");
//                intent.putExtra("keyid", "0");
//                startActivity(intent);
                break;
            default:
                break;
        }
    }
}

