package com.hemaapp.tyyjsc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.ValueMealAdapter;
import com.hemaapp.tyyjsc.model.GoodsBriefIntroduction;
import com.hemaapp.tyyjsc.view.MyScrollView;
import com.hemaapp.tyyjsc.view.NoScrollListView;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 特价预订(一成购车)
 */
public class ActivityListGoodsTejia extends BaseActivity implements View.OnClickListener {

    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧微文字按钮
    private ValueMealAdapter goodsAdapter = null;
    private NoScrollListView gridView = null;
    private ArrayList<GoodsBriefIntroduction> data = new ArrayList<>();
    private TextView emptyView = null;//暂无数据
    private RefreshLoadmoreLayout layout = null;
    private FrameLayout topView = null;//滑动顶部按钮
    private MyScrollView sv = null;
    private int page = 0;//页数

    private TextView tv_order_price;
    private TextView tv_order_sale;
    private String orderby = "1";  //1：价格，2:销量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_list_goods_tejia);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        getGoodList();
    }

    //获取商品列表
    public void getGoodList() {
        getNetWorker().getGoodsList("3", "", "", String.valueOf(page), orderby);
    }

    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);

        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.GONE);

        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.INVISIBLE);

        tv_order_price = (TextView) findViewById(R.id.tv_order_price);
        tv_order_sale = (TextView) findViewById(R.id.tv_order_sale);

        //刷新控件
        layout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        gridView = (NoScrollListView) findViewById(R.id.grid);
        sv = (MyScrollView) findViewById(R.id.sv);
        emptyView = (TextView) findViewById(R.id.empty);
        //浮动按钮
        topView = (FrameLayout) findViewById(R.id.top);
        hmBarNameView.setText("一成购车");
    }

    @Override
    protected void getExras() {
    }

    @Override
    protected void setListener() {
        hmBackBtn.setOnClickListener(this);
        topView.setOnClickListener(this);
        tv_order_price.setOnClickListener(this);
        tv_order_sale.setOnClickListener(this);

        layout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getGoodList();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                getGoodList();
            }
        });
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_LIST:
                showProgressDialog(getString(R.string.hm_hlxs_txt_88));
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_LIST:
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
            case GOODS_LIST:
                String page = hemaNetTask.getParams().get("page");
                int sysPagesize = BaseApplication.getInstance().getSysInitInfo()
                        .getSys_pagesize();
                HemaPageArrayResult<GoodsBriefIntroduction> nResult = (HemaPageArrayResult<GoodsBriefIntroduction>) hemaBaseResult;
                ArrayList<GoodsBriefIntroduction> list = nResult.getObjects();
                if ("0".equals(page)) {// 刷新
                    layout.refreshSuccess();
                    data.clear();
                    data.addAll(list);
                    if (list.size() < sysPagesize)
                        layout.setLoadmoreable(false);
                    else
                        layout.setLoadmoreable(true);
                } else {// 更多
                    layout.loadmoreSuccess();
                    if (list.size() > 0) {
                        data.addAll(list);
                        if (list.size() < sysPagesize)
                            layout.setLoadmoreable(false);
                        else
                            layout.setLoadmoreable(true);
                    } else {
                        layout.setLoadmoreable(false);
                        XtomToastUtil.showShortToast(ActivityListGoodsTejia.this, getString(R.string.hm_hlxs_txt_89));
                    }
                }
                freshData();
                break;
            default:
                break;
        }
    }

    public void freshData() {
        if (data == null || data.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            topView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            topView.setVisibility(View.VISIBLE);
            if (goodsAdapter == null) {
                goodsAdapter = new ValueMealAdapter(this, data);
                goodsAdapter.setEmptyString(getString(R.string.hm_hlxs_txt_90));
                goodsAdapter.setKeytype("3");
                gridView.setAdapter(goodsAdapter);
            } else {
                goodsAdapter.setData(data);
                goodsAdapter.setKeytype("3");
                goodsAdapter.setEmptyString(getString(R.string.hm_hlxs_txt_91));
                goodsAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_LIST:
                showTextDialog("数据出错");
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_LIST:
                String page = hemaNetTask.getParams().get("page");
                if (isNull(page) || "0".equals(page)) {
                    layout.refreshFailed();
                } else {
                    layout.loadmoreFailed();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_left:
                finish();
                break;
            case R.id.top:
                sv.smoothScrollTo(0, 0);
                break;
            case R.id.tv_order_price:
                orderby = "1";
                page = 0;
                getGoodList();
                tv_order_price.setTextColor(getResources().getColor(R.color.cl_3ea600));
                tv_order_sale.setTextColor(getResources().getColor(R.color.cl_888686));
                break;
            case R.id.tv_order_sale:
                orderby = "2";
                page = 0;
                getGoodList();
                tv_order_price.setTextColor(getResources().getColor(R.color.cl_888686));
                tv_order_sale.setTextColor(getResources().getColor(R.color.cl_3ea600));
                break;
        }
    }
}

