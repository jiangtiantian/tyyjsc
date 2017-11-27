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
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.CommissionAdapter;
import com.hemaapp.tyyjsc.adapters.DealNoteAdapter;
import com.hemaapp.tyyjsc.model.DealItem;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.CheckCardDialog;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 账户余额、储值卡、积分、收入四个页面
 */
public class ChuZhiCard extends BaseActivity implements View.OnClickListener, CommissionAdapter.AddCardListener {
    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧文字按钮
    private TextView buy_list;
    //1.账户余额 2.积分
    private TextView txtView = null;
    private XtomListView listView = null;//收入
    private CommissionAdapter commissionAdapter = null;
    private int page = 0;
    private RefreshLoadmoreLayout refreshLoadmoreLayout = null;
    private PopupWindow popupWindow = null;
    private User user = null;//用户信息
    private OnMoneyChangeReceiver onMoneyChangeReceiver = null;
    private CheckCardDialog checkCardDialog = null;//核销验证码弹框
    private ArrayList<DealItem> deals = new ArrayList<>();
    private DealNoteAdapter dealNoteAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chuzhi_card);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        getDebitCardList();
//        user = BaseApplication.getInstance().getUser();
    }

    @Override
    protected void onResume() {
        user = BaseApplication.getInstance().getUser();
        if (user != null) {
            init();
            getNetWorker().clientGet(user.getToken(), user.getId());
        } else {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        super.onResume();
    }

    //获取佣金
    public void getRecommentList() {
        getNetWorker().getRecommentList(user.getToken(), String.valueOf(page));
    }

    //获取储值卡列表
    public void getDebitCardList() {
        getNetWorker().debitcard_list(String.valueOf(page));
    }

    //充值卡充值
    public void cardAdd(String card, String verification_code) {
        getNetWorker().cardAdd(user.getToken(), card, verification_code);
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
        txtView = (TextView) findViewById(R.id.countfee);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(R.id.users);
        buy_list = (TextView) findViewById(R.id.buy_list);
    }

    @Override
    protected void getExras() {
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
        hmBarNameView.setText(R.string.hm_hlxs_txt_4);
        buy_list.setOnClickListener(this);
        hmBackBtn.setOnClickListener(this);
        hmRightBtn.setOnClickListener(this);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getDebitCardList();

            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                getDebitCardList();

            }
        });
    }

    //储值卡适配器
    public void freshData() {
        if (commissionAdapter == null) {
            commissionAdapter = new CommissionAdapter(ChuZhiCard.this, deals);
            commissionAdapter.setEmptyString(getString(R.string.hm_hlxs_txt_9));
            commissionAdapter.setListener(this);
            listView.setAdapter(commissionAdapter);
        } else {
            commissionAdapter.setData(deals);
            commissionAdapter.setEmptyString(getString(R.string.hm_hlxs_txt_10));
            commissionAdapter.notifyDataSetChanged();
        }
        BaseUtil.setListViewHeightBasedOnChildren(listView);
    }


    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
            case DEBITCARD_LIST:
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
            case DEBITCARD_LIST:
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
            case CLIENT_GET:
                HemaArrayResult<User> infoResult = (HemaArrayResult<User>) hemaBaseResult;
                user = infoResult.getObjects().get(0);
                BaseApplication.getInstance().setUser(user);
                init();
                break;
            case DEBITCARD_LIST:
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
                freshData();
                break;
            case CARD_ADD:
                if (checkCardDialog != null) {
                    checkCardDialog.clearET();
                }
                this.page = 0;
                break;
            default:
                break;
        }
    }

    public void init() {
        txtView.setText("￥" + (isNull(user.getDebitaccound()) ? "0" : user.getDebitaccound()));
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
            case DEBITCARD_LIST:
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
            case DEBITCARD_LIST:
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
            case R.id.buy_list:
                intent = new Intent(ChuZhiCard.this, ChuZhiCardList.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void addCart(String cardid, String money) {
        AddCart(cardid, money);
    }

    class OnMoneyChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            init();
        }
    }

    public void AddCart(String id, String money) {
//        if(checkCardDialog == null){
//            checkCardDialog = new CheckCardDialog(mContext);
//            checkCardDialog.setListener(new CheckCardDialog.OnCancelOrConfirmListener() {
//                @Override
//                public void onConfirm(String no, String code) {
//                    checkCardDialog.cancel();
//                    if(isNull(no)){
//                        XtomToastUtil.showShortToast(mContext, "请输入储值卡账号");
//                        return;
//                    }
//                    if(isNull(code)){
//                        XtomToastUtil.showShortToast(mContext, "请输入核销码");
//                        return;
//                    }
//                    //核销验证码
//                    cardAdd(no, code);
//                }
//            });
//        }
//        checkCardDialog.show();
        Intent it = new Intent(ChuZhiCard.this, ActivityPay.class);
        it.putExtra("keytype", "2");
        it.putExtra("money", money);//剩余支付金额
        it.putExtra("from", "1");
        it.putExtra("order_id", id);
        startActivityForResult(it, 1);
    }
}

