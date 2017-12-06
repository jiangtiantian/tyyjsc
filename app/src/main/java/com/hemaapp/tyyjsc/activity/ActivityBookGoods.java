package com.hemaapp.tyyjsc.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.GridGoodsAdapter;
import com.hemaapp.tyyjsc.model.GoodsBriefIntroduction;
import com.hemaapp.tyyjsc.view.CityGridView;
import com.hemaapp.tyyjsc.view.MyScrollView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * type = 1精品推荐, type = 2预售抢购, type = 4套餐专区，
 */
public class ActivityBookGoods extends BaseActivity implements View.OnClickListener {
    private final static long hourLevelValue = 60 * 60 * 1000;
    private final static long minuteLevelValue = 60 * 1000;
    private final static long secondLevelValue = 1000;
    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧微文字按钮
    private GridGoodsAdapter goodsAdapter = null;
    private CityGridView gridView = null;
    private ArrayList<GoodsBriefIntroduction> data = new ArrayList<>();
    private TextView emptyView = null;//暂无数据
    private RefreshLoadmoreLayout layout = null;
    private FrameLayout topView = null;//滑动顶部按钮
    private MyScrollView sv = null;
    private String keytype = "";//1：精品推荐 2:预售抢购
    private String id;
    private String name;
    private Boolean fromsort = false;
    private CountDownTimer countDownTimer = null;//倒计时
    private LinearLayout timeLay;
    private TextView timeViewH = null;//时
    private TextView timeViewM = null;//
    private TextView timeViewS = null;//秒
    private int page = 0;//页数分
    private long period = 0;//时间差

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_book_goods);
        super.onCreate(savedInstanceState);
        setColor(mContext);
    }

    //获取商品列表
    public void getGoodList(String keytype, String id, String keyword) {
        getNetWorker().getGoodsList(keytype, id, keyword, String.valueOf(page), "");
    }

    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);
        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.GONE);
        //抢购倒计时
        timeLay = (LinearLayout) findViewById(R.id.qianggou_time);
        timeViewH = (TextView) findViewById(R.id.time_h);
        timeViewM = (TextView) findViewById(R.id.time_m);
        timeViewS = (TextView) findViewById(R.id.time_s);
        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.INVISIBLE);
        //刷新控件
        layout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        gridView = (CityGridView) findViewById(R.id.grid);
        sv = (MyScrollView) findViewById(R.id.sv);
        emptyView = (TextView) findViewById(R.id.empty);
        //浮动按钮
        topView = (FrameLayout) findViewById(R.id.top);
        switch (keytype) {
            case "1":
                hmBarNameView.setText("精品推荐");
                getGoodList(keytype, "", "");
                break;
            case "2":
                hmBarNameView.setText("预售抢购");
                getGoodList(keytype, "", "");
                timeLay.setVisibility(View.VISIBLE);
                break;
            case "3":
                hmBarNameView.setText("特价预定");
                getGoodList(keytype, "", "");
                break;
            case "4":
                hmBarNameView.setText("套餐专区");
                getGoodList(keytype, "", "");
                break;
            default:
                hmBarNameView.setText(name);
                getGoodList(keytype, id, "");
                fromsort = true;
                break;
        }
    }

    //倒计时
    public void countTimer(GoodsBriefIntroduction info) {
        Date starttime = null;//结束时间
        Date serviceDate = null;//服务器时间
        timeLay.postInvalidate();
        //endDate = BaseUtil.formatDate(endTime, info.getNowtime());
        try {
            starttime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(info.getBegintime());
            serviceDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(info.getNowtime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (starttime != null) {
            period = (starttime.getTime() - serviceDate.getTime()) <= 0 ? 0 : (starttime.getTime() - serviceDate.getTime());
            countDownTimer = new CountDownTimer(period, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    period -= 1000;
                    getDifference(period, timeViewH, timeViewM, timeViewS);
                }

                @Override
                public void onFinish() {
                    timeLay.setVisibility(View.GONE);
                }
            }.start();
        }

    }

    @Override
    protected void getExras() {
        keytype = getIntent().getStringExtra("keytype");
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
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
                switch (keytype) {
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                        getGoodList(keytype, "", "");
                        break;
                    case "6":
                    case "7":
                        getGoodList(keytype, id, "");
                        break;
                }
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                switch (keytype) {
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                        getGoodList(keytype, "", "");
                        break;
                    case "6":
                    case "7":
                        getGoodList(keytype, id, "");
                        break;
                }
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
                        XtomToastUtil.showShortToast(ActivityBookGoods.this, getString(R.string.hm_hlxs_txt_89));
                    }
                }
                freshData();
                break;
        }
    }

    public void freshData() {
        if (data == null || data.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            topView.setVisibility(View.GONE);
            timeLay.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            topView.setVisibility(View.VISIBLE);
            if ("2".equals(keytype))
                countTimer(data.get(0));
            if (goodsAdapter == null) {
                goodsAdapter = new GridGoodsAdapter(this, data, null, fromsort);
                goodsAdapter.setEmptyString(getString(R.string.hm_hlxs_txt_90));
                goodsAdapter.setKeytype(keytype);
                goodsAdapter.setStatus(-2);
                gridView.setAdapter(goodsAdapter);
            } else {
                goodsAdapter.setData(data);
                goodsAdapter.setEmptyString(getString(R.string.hm_hlxs_txt_91));
                goodsAdapter.setStatus(-2);
                goodsAdapter.setKeytype(keytype);
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

    private void getDifference(long period, TextView timeViewH, TextView timeViewM, TextView timeViewS) {//根据毫秒差计算时间差
        String result = null;
        /*******计算出时间差中的时、分、秒*******/
        int hour = getHour(period);
        int minute = getMinute(period - hour * hourLevelValue);
        int second = getSecond(period - hour * hourLevelValue - minute * minuteLevelValue);

        timeViewH.setText(formatTimeStr(hour));
        timeViewM.setText(formatTimeStr(minute));
        timeViewS.setText(formatTimeStr(second));
        timeViewH.postInvalidate();
        timeViewM.postInvalidate();
        timeViewS.postInvalidate();
    }

    //格式化时间串00
    public String formatTimeStr(int n) {
        if (n < 10) {
            return "0" + String.valueOf(n);
        }
        return String.valueOf(n);
    }

    public int getHour(long period) {
        return (int) (period / hourLevelValue);
    }

    public int getMinute(long period) {
        return (int) (period / minuteLevelValue);
    }

    public int getSecond(long period) {
        return (int) (period / secondLevelValue);
    }
}
