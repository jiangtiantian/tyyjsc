package com.hemaapp.tyyjsc.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.VoucherAdapter;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.model.VourcherInfo;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

import static com.hemaapp.tyyjsc.R.id.used_vouchers;
import static com.hemaapp.tyyjsc.R.id.vouchers;

/**
 * 代金券
 */
public class ActivityVoucherList extends BaseActivity implements View.OnClickListener {
    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private TextView hmRightTxtView = null;//标题栏右侧微文字按钮
    private RefreshLoadmoreLayout layout = null;//刷新
    private int page = 0;
    private ArrayList<VourcherInfo> data = new ArrayList<>();
    private ArrayList<VourcherInfo> unUserdData = new ArrayList<>();
    private XtomListView listView = null;
    private VoucherAdapter voucherAdapter = null;//未使用
    private ArrayList<VourcherInfo> usedData = new ArrayList<>();
    private XtomListView usedListView = null;
    private VoucherAdapter usedVoucherAdapter = null;
    private LinearLayout usedLayout = null;////已使用
    private TextView emptyView = null;
    private String which = "";//0 代表由确认订单页面进入代金券页面；1 代表由个人中心进入
    private User user = null;
    private String money = null;
    private String totalMoney = "";//订单金额，利用该字段过滤掉不符合要求的代金券
    private LinearLayout conversion_button;//兑换代金券的按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_voucher_list);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        user = BaseApplication.getInstance().getUser();
        getVourcherList("1");//有效的
        getVourcherList("2");//失效的
    }

    //获取代金券列表
    public void getVourcherList(String keytype) {
        getNetWorker().getVourcher(user.getToken(), keytype, String.valueOf(page));
    }

    //刷新数据
    public void freshData() {
        if (unUserdData == null && unUserdData.size() == 0) {
            listView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
            if (!isNull(totalMoney)) {//筛选可用优惠券
                for (int i = 0; i < unUserdData.size(); i++) {
                    if (Double.parseDouble(unUserdData.get(i).getMaxmoney()) > Double.parseDouble(totalMoney)) {
                        unUserdData.remove(unUserdData.get(i));
                        i--;
                    }
                }
            }
            //未使用代金券 或者是 当前订单可使用代金券
            if (voucherAdapter == null) {
                voucherAdapter = new VoucherAdapter(ActivityVoucherList.this, unUserdData, which, false);
                voucherAdapter.setVourcherInfo(totalMoney);
                listView.setAdapter(voucherAdapter);
            } else {
                voucherAdapter.setVourcherInfo(totalMoney);
                voucherAdapter.setData(unUserdData);
                voucherAdapter.notifyDataSetChanged();
            }
            BaseUtil.setListViewHeightBasedOnChildren(listView);
        }

        //已使用的代金券
        if ("1".equals(which) && usedData.size() > 0) {
            usedLayout.setVisibility(View.VISIBLE);
            if (usedVoucherAdapter == null) {
                usedVoucherAdapter = new VoucherAdapter(ActivityVoucherList.this, usedData, which, true);
                usedListView.setAdapter(usedVoucherAdapter);
            } else {
                usedVoucherAdapter.setData(usedData);
                usedVoucherAdapter.notifyDataSetChanged();
            }
            BaseUtil.setListViewHeightBasedOnChildren(usedListView);
        } else {
            usedLayout.setVisibility(View.GONE);
        }
        if ("0".equals(which) && (unUserdData == null || unUserdData.size() == 0)) {
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText(R.string.hm_hlxs_txt_141);
        } else if ("0".equals(which) && unUserdData.size() > 0) {
            emptyView.setVisibility(View.GONE);
        } else {
            if (unUserdData.size() == 0 && usedData.size() == 0) {
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText(getString(R.string.hm_hlxs_txt_9));
            } else {
                emptyView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void findView() {
        //标题
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);
        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.VISIBLE);
        layout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(vouchers);
        usedListView = (XtomListView) findViewById(used_vouchers);
        usedLayout = (LinearLayout) findViewById(R.id.usedlayout);
        emptyView = (TextView) findViewById(R.id.empty);

        conversion_button= (LinearLayout) findViewById(R.id.conversion_button);
    }

    @Override
    protected void getExras() {
        which = getIntent().getStringExtra("which");
        money = getIntent().getStringExtra("money");
        totalMoney = getIntent().getStringExtra("totalMoney");
    }

    @Override
    protected void setListener() {
        hmBackBtn.setOnClickListener(this);
        hmRightTxtView.setOnClickListener(this);
        conversion_button.setOnClickListener(this);
        hmBarNameView.setText(R.string.hm_hlxs_txt_137);
        if ("1".equals(which)) {
            hmRightTxtView.setText("使用规则");
        } else {
            hmRightTxtView.setVisibility(View.GONE);
            //hmBarNameView.setText(R.string.hm_hlxs_txt_142);
        }

        layout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getVourcherList("1");
                getVourcherList("2");//失效的
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                layout.loadmoreSuccess();
            }
        });


    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case VOUCHER_LIST:
                showProgressDialog(R.string.hm_hlxs_txt_68);
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
            case VOUCHER_LIST:
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
            case VOUCHER_LIST:
                layout.refreshSuccess();
                String keytype = hemaNetTask.getParams().get("keytype");
                HemaArrayResult<VourcherInfo> nResult = (HemaArrayResult<VourcherInfo>) hemaBaseResult;
                ArrayList<VourcherInfo> list = nResult.getObjects();
                if ("1".equals(keytype)) {
                    unUserdData.clear();
                    unUserdData.addAll(list);
                } else {
                    usedData.clear();
                    usedData.addAll(list);
                }
                // 刷新
                freshData();
                break;
            case COUPON_GET:
                HemaBaseResult result_get=hemaBaseResult;
                showTextDialog(result_get.getMsg());

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
            case VOUCHER_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            case COUPON_GET:
                HemaBaseResult result_get=hemaBaseResult;
                showTextDialog(result_get.getMsg());
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
            case VOUCHER_LIST:
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (v == hmBackBtn) {
            finish();
        } else if (v == hmRightTxtView) {
            Intent intent = new Intent(ActivityVoucherList.this, ActivityWebView.class);
            intent.putExtra("keytype", "3");
            intent.putExtra("keyid", "0");
            startActivity(intent);
        }else if (v==conversion_button){
              final Dialog dialog=new Dialog(ActivityVoucherList.this,R.style.Dialog);
                View  layout= LayoutInflater.from(ActivityVoucherList.this).inflate(R.layout.item_conversion_code,null);
                dialog.addContentView(layout, new LinearLayout.LayoutParams(
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                        , android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
                 //dialog.create();
                TextView tv_positive= (TextView) layout.findViewById(R.id.tv_positive);
                TextView tv_cancel= (TextView) layout.findViewById(R.id.tv_cancel);
                 final EditText et_conversionCode= (EditText) layout.findViewById(R.id.et_conversionCode);

                dialog.show();
            tv_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String content=    et_conversionCode.getText().toString().trim();
                    getNetWorker().Couponget(user.getToken(),content);
                    dialog.dismiss();
                }
            });
            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                dialog.dismiss();
                }
            });
        }
    }

    // 关闭当前页面
    public void finishActivity(VourcherInfo vourcherInfo) {
        Intent intent = new Intent();
        if (vourcherInfo == null) {
            //nothing
        } else {
            intent.putExtra("key", vourcherInfo);
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}
