package com.hemaapp.tyyjsc.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
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
import com.hemaapp.tyyjsc.adapters.CommissionAdapter;
import com.hemaapp.tyyjsc.adapters.DealNoteAdapter;
import com.hemaapp.tyyjsc.model.DealItem;
import com.hemaapp.tyyjsc.model.RecommendUserInfo;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.CheckCardDialog;
import com.hemaapp.tyyjsc.view.HmHelpDialog;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

import static android.R.attr.id;
import static com.hemaapp.tyyjsc.R.id.money;

/**
 * Created by zuozhongqian on 2017/8/9.
 * 我的积分
 */
public class ActivityScoreAccountBalance extends BaseActivity implements View.OnClickListener {
    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧文字按钮
    //1.账户余额 2.积分
    private String keytype = "";//0.余额明细，1.积分明细
    private TextView txtView = null;
    private XtomListView listView = null;//收入
    private CommissionAdapter commissionAdapter = null;
    private ArrayList<RecommendUserInfo> data = new ArrayList<>();
    private int page = 0;
    private RefreshLoadmoreLayout refreshLoadmoreLayout = null;
    private PopupWindow popupWindow = null;
    private User user = null;//用户信息
    private TextView tip, tip_btn;
    private OnMoneyChangeReceiver onMoneyChangeReceiver = null;
    private CheckCardDialog checkCardDialog = null;//核销验证码弹框
    private ArrayList<DealItem> deals = new ArrayList<>();
    private DealNoteAdapter dealNoteAdapter = null;

    private TextView tv_goldNumber;
    private TextView tv_nowMoney;
    private TextView tv_gold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_account_balance);
        user = BaseApplication.getInstance().getUser();
        super.onCreate(savedInstanceState);
        setColor(mContext);
        if (user != null) {
            tip_btn.setVisibility(View.INVISIBLE);
            getNetWorker().clientGet(user.getToken(), user.getId());
        } else {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
    }

    //积分明细
    public void scorerecord_list() {
        getNetWorker().scorerecord_list(user.getToken(), String.valueOf(page));
    }

    //获取余额明细
    public void feeaccount_list() {
        getNetWorker().feeaccount_list(user.getToken(), String.valueOf(page));
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
        txtView = (TextView) findViewById(R.id.txt);
        tip = (TextView) findViewById(R.id.tip);
        tip_btn = (TextView) findViewById(R.id.tip_btn);

        tv_goldNumber = (TextView) findViewById(R.id.tv_goldNumber);
        tv_nowMoney = (TextView) findViewById(R.id.tv_nowMoney);
        tv_gold = (TextView) findViewById(R.id.tv_gold);

        if ("0".equals(keytype)) {
            tip.setText("当前余额");
            tip_btn.setText("账户充值");
        } else {
            tip.setText("当前积分");
            tip_btn.setText("积分明细");
        }
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(R.id.users);
    }

    @Override
    protected void getExras() {
        keytype = getIntent().getStringExtra("keytype");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent it) {
        if (resultCode != RESULT_OK && it == null) {
            return;
        }
        //支付成功刷新数据
        getNetWorker().clientGet(user.getToken(), user.getId());

    }

    @Override
    protected void setListener() {
        tv_gold.setOnClickListener(this);
        if ("0".equals(keytype)) {
            hmBarNameView.setText(R.string.hm_hlxs_txt_2);
        } else if ("1".equals(keytype)) {
            hmBarNameView.setText("积分");
        }
        hmBackBtn.setOnClickListener(this);
        hmRightBtn.setOnClickListener(this);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                switch (keytype) {
                    case "0":
                        page = 0;
                        feeaccount_list();
                        break;
                    case "1":
                        page = 0;
                        scorerecord_list();
                        break;
                }
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                switch (keytype) {
                    case "0":
                        page++;
                        feeaccount_list();
                        break;
                    case "1":
                        page++;
                        scorerecord_list();
                        break;
                }
            }
        });
        tip_btn.setOnClickListener(this);
    }

    //明细详情
    public void freshDealData() {
        if (dealNoteAdapter == null) {
            dealNoteAdapter = new DealNoteAdapter(mContext, deals);
            dealNoteAdapter.setEmptyString("暂无数据");
            dealNoteAdapter.isHiddenFirstDivider(true);
            dealNoteAdapter.setKeytype(keytype);
            listView.setAdapter(dealNoteAdapter);
        } else {
            dealNoteAdapter.setData(deals);
            dealNoteAdapter.setEmptyString("暂无数据");
            dealNoteAdapter.setKeytype(keytype);
            dealNoteAdapter.notifyDataSetChanged();
        }
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
                tv_goldNumber.setText("￥" + user.getConvertmoney());
                tv_nowMoney.setText("￥" + user.getExchangemoney());
                BaseApplication.getInstance().setUser(user);
                init();
                switch (keytype) {
                    case "0":
                        feeaccount_list();
                        break;
                    case "1":
                        scorerecord_list();
                        break;
                }
                break;
            case FEEACCOUNT_LIST:
                String page = hemaNetTask.getParams().get("page");
                int sysPagesize = BaseApplication.getInstance().getSysInitInfo()
                        .getSys_pagesize();
                HemaArrayResult<DealItem> nResult = (HemaArrayResult<DealItem>) hemaBaseResult;
                ArrayList<DealItem> list = nResult.getObjects();
                if ("0".equals(page)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    deals.clear();
                    deals.addAll(list);
                    if (list.size() < sysPagesize)
                        refreshLoadmoreLayout.setLoadmoreable(false);
                    else
                        refreshLoadmoreLayout.setLoadmoreable(true);
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (list.size() > 0) {
                        deals.addAll(list);
                        if (list.size() < sysPagesize)
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
            case SCORERECORD_LIST:
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
                if (checkCardDialog != null) {
                    checkCardDialog.clearET();
                }
                this.page = 0;

                break;
            case EXCHANGEMONEY:
                showTextDialog(hemaBaseResult.getMsg());

            default:
                break;
        }
    }

    public void init() {
        if ("0".equals(keytype)) {//账户余额
            txtView.setText((isNull(user.getFeeaccount()) ? "0" : user.getFeeaccount()));
        } else if ("1".equals(keytype)) {
            txtView.setText((isNull(user.getScore()) ? "0" : user.getScore()));
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
            case FEEACCOUNT_LIST:
            case SCORERECORD_LIST:
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
            case FEEACCOUNT_LIST:
            case SCORERECORD_LIST:
                showTextDialog("网络请求错误");
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_left:
                finish();
                break;
            case R.id.bar_right_img://说明
//                intent = new Intent(ActivityAccountBalance.this, ActivityWebView.class);
//                intent.putExtra("keytype", "5");
//                intent.putExtra("keyid", "0");
//                startActivity(intent);
                break;
            case R.id.tip_btn:
                if ("0".equals(keytype)) {
                    Intent it = new Intent(ActivityScoreAccountBalance.this, ActivityPay.class);
                    it.putExtra("keytype", "1");
                    it.putExtra("money", money);//剩余支付金额
                    it.putExtra("from", "1");
                    it.putExtra("order_id", id);
                    startActivityForResult(it, 1);
                }
                break;
            case R.id.tv_gold:
                showPhoneDialog();

                break;

            default:
                break;
        }
    }

    class OnMoneyChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            init();
        }
    }

    private HmHelpDialog logoutDialog;

    private void showPhoneDialog() {
        if (logoutDialog == null) {
            logoutDialog = new HmHelpDialog(this, 0);
        }
        logoutDialog.setLeftButtonText("取消");
        logoutDialog.setRightButtonText("确定");
        logoutDialog.setTitleName("确定要兑换吗");
        logoutDialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
            @Override
            public void onCancel(int type) {
                logoutDialog.cancel();
            }

            @Override
            public void onConfirm(String msg) {
                logoutDialog.cancel();
                getNetWorker().exchangemoney(user.getToken());

            }
        });
        logoutDialog.show();
    }


}
