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
import com.hemaapp.tyyjsc.adapters.TaoCanAdapter;
import com.hemaapp.tyyjsc.model.GoodsBriefIntroduction;
import com.hemaapp.tyyjsc.view.MyScrollView;
import com.hemaapp.tyyjsc.view.NoScrollListView;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;
/**
 * 特价预订 超值套餐
 */
public class TaoCanActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧微文字按钮
    private TaoCanAdapter goodsAdapter = null;
    private NoScrollListView gridView = null;
    private ArrayList<GoodsBriefIntroduction> data = new ArrayList<>();
    private TextView emptyView = null;//暂无数据
    private RefreshLoadmoreLayout layout = null;
    private FrameLayout topView = null;//滑动顶部按钮
    private MyScrollView sv = null;
    private String keytype = "";//1：精品推荐 2:限时抢购 3：特价预定 4:套餐列表
    private int page = 0;//页数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_list_goods);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        getGoodList();
    }
    //获取商品列表
    public void getGoodList() {
        getNetWorker().getGoodsList(keytype,"","",String.valueOf(page), "");
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
        //刷新控件
        layout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        gridView = (NoScrollListView) findViewById(R.id.grid);
        sv = (MyScrollView) findViewById(R.id.sv);
        emptyView = (TextView) findViewById(R.id.empty);
        //浮动按钮
        topView = (FrameLayout) findViewById(R.id.top);
        switch (keytype)
        {
            case "1":
                hmBarNameView.setText("精品推荐");
                break;
            case "2":
                hmBarNameView.setText("预售抢购");
                break;
            case "3":
                hmBarNameView.setText("特价预定");
                break;
            case "4":
                hmBarNameView.setText("套餐专区");
                break;
        }
    }
    @Override
    protected void getExras() {
        keytype = getIntent().getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        if ("3".equals(keytype)) {
            hmBarNameView.setText(R.string.hm_hlxs_txt_86);
        } else if ("4".equals(keytype)) {
            hmBarNameView.setText(R.string.hm_hlxs_txt_87);
        }
        hmBackBtn.setOnClickListener(this);
        topView.setOnClickListener(this);

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
                @SuppressWarnings("unchecked")
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
                        XtomToastUtil.showShortToast(TaoCanActivity.this, getString(R.string.hm_hlxs_txt_89));
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
                goodsAdapter = new TaoCanAdapter(this, data);
                goodsAdapter.setEmptyString(getString(R.string.hm_hlxs_txt_90));
                goodsAdapter.setKeytype("4");
                gridView.setAdapter(goodsAdapter);
            } else {
                goodsAdapter.setData(data);
                goodsAdapter.setKeytype("4");
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
            default:
                break;
        }
    }
}

