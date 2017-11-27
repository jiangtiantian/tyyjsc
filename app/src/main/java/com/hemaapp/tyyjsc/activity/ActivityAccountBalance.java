package com.hemaapp.tyyjsc.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.DealCountNoteAdapter;
import com.hemaapp.tyyjsc.model.MyDealItem;
import com.hemaapp.tyyjsc.model.SysInitInfo;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.CheckCardDialog;
import com.hemaapp.tyyjsc.view.HmHelpDialog;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

import static android.R.attr.id;
import static com.hemaapp.tyyjsc.R.id.bt_charge;
import static com.hemaapp.tyyjsc.R.id.money;
import static com.hemaapp.tyyjsc.R.id.tv_gold;

/**
 * 账户余额
 */
public class ActivityAccountBalance extends BaseActivity implements View.OnClickListener {
    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧文字按钮
    private TextView txtView = null;
    private XtomListView listView = null;//收入
    private int page = 0;
    private RefreshLoadmoreLayout refreshLoadmoreLayout = null;
    private User user = null;//用户信息
    private TextView tip, tip_charge, tip_tixian;
    private OnMoneyChangeReceiver onMoneyChangeReceiver = null;
    private CheckCardDialog checkCardDialog = null;//核销验证码弹框
    private ArrayList<MyDealItem> deals = new ArrayList<>();
    private DealCountNoteAdapter dealNoteAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_money_card);
        user = BaseApplication.getInstance().getUser();
        super.onCreate(savedInstanceState);
        setColor(mContext);
        if (user != null) {
            getNetWorker().clientGet(user.getToken(), user.getId());
        } else {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
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
        tip_charge = (TextView) findViewById(R.id.bt_charge);
        tip_tixian = (TextView) findViewById(R.id.bt_tixian);

        tip.setText("当前余额");
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(R.id.users);
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
        hmBarNameView.setText(R.string.hm_hlxs_txt_2);
        hmBackBtn.setOnClickListener(this);
        hmRightBtn.setOnClickListener(this);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                feeaccount_list();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                feeaccount_list();
            }
        });
        tip_charge.setOnClickListener(this);
        tip_tixian.setOnClickListener(this);
    }

    //明细详情
    public void freshDealData() {
        if (dealNoteAdapter == null) {
            dealNoteAdapter = new DealCountNoteAdapter(mContext, deals);
            dealNoteAdapter.setEmptyString("暂无数据");
            dealNoteAdapter.isHiddenFirstDivider(true);
            listView.setAdapter(dealNoteAdapter);
        } else {
            dealNoteAdapter.setData(deals);
            dealNoteAdapter.setEmptyString("暂无数据");
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
                BaseApplication.getInstance().setUser(user);
                init();
                feeaccount_list();
                break;
            case FEEACCOUNT_LIST:
                String page = hemaNetTask.getParams().get("page");
                int sysPagesize = BaseApplication.getInstance().getSysInitInfo()
                        .getSys_pagesize();
                HemaArrayResult<MyDealItem> nResult = (HemaArrayResult<MyDealItem>) hemaBaseResult;
                ArrayList<MyDealItem> list = nResult.getObjects();
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
            case CARD_ADD:
                if (checkCardDialog != null) {
                    checkCardDialog.clearET();
                }
                this.page = 0;
                break;
        }
    }

    public void init() {
        txtView.setText((isNull(user.getFeeaccount()) ? "0" : user.getFeeaccount()));
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
            case FEEACCOUNT_LIST:
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
                showTextDialog("网络请求错误");
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.back_left:
                finish();
                break;
            case bt_charge:
                it = new Intent(ActivityAccountBalance.this, ActivityPay.class);
                it.putExtra("keytype", "1");
                it.putExtra("money", money);//剩余支付金额
                it.putExtra("from", "1");
                it.putExtra("order_id", id);
                startActivityForResult(it, 1);
                break;
            case tv_gold:
                showPhoneDialog();
                break;
            case R.id.bt_tixian:
                showDailog();
                break;
            default:
                break;
        }
    }

    private SysInitInfo sysInitInfo;
    private Dialog dialog;
    private void showDailog(){
        sysInitInfo= BaseApplication.getInstance().getSysInitInfo();
        dialog = new Dialog(this, R.style.Theme_Light_Dialog);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.item_index_dialog,null);
        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.dialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        dialog.setContentView(dialogView);
        TextView tv_customPhone= (TextView) dialogView.findViewById(R.id.tv_customPhone);
        tv_customPhone.setVisibility(View.GONE);
        TextView tv_lineTel= (TextView) dialogView.findViewById(R.id.tv_lineTel);
        tv_lineTel.setVisibility(View.GONE);

        TextView tv_phone= (TextView) dialogView.findViewById(R.id.name_phone);
        tv_phone.setText("支付宝提现");
        TextView tv_qq = (TextView) dialogView.findViewById(R.id.name_qq);
        tv_qq.setText("银行卡提现");

        LinearLayout phone = (LinearLayout) dialogView.findViewById(R.id.ll_phone);
        LinearLayout tel = (LinearLayout) dialogView.findViewById(R.id.ll_tel);
        LinearLayout  cancel = (LinearLayout) dialogView.findViewById(R.id.ll_cancel);

        //支付宝提现
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, ActivityTixianByAlipay.class);
                intent.putExtra("money", user.getFeeaccount());
                startActivityForResult(intent, R.id.layout);
            }
        });

        //银行卡提现
        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, ActivityTixianByUnionpay.class);
                intent.putExtra("money", user.getFeeaccount());
                startActivityForResult(intent, R.id.layout);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
