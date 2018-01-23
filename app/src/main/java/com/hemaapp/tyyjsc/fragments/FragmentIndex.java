package com.hemaapp.tyyjsc.fragments;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseFragment;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivityBookGoods;
import com.hemaapp.tyyjsc.activity.ActivityIndex;
import com.hemaapp.tyyjsc.activity.ActivityIndexSearch;
import com.hemaapp.tyyjsc.activity.ActivityListGoodsTejia;
import com.hemaapp.tyyjsc.activity.ActivityNoticeList;
import com.hemaapp.tyyjsc.activity.ActivityTuijianGoodInfo;
import com.hemaapp.tyyjsc.activity.ActivityWebView;
import com.hemaapp.tyyjsc.activity.CityActivity;
import com.hemaapp.tyyjsc.activity.Login;
import com.hemaapp.tyyjsc.activity.TaoCanActivity;
import com.hemaapp.tyyjsc.adapters.GridViewGoodsAdapter;
import com.hemaapp.tyyjsc.adapters.MemberGoodsListAdapters;
import com.hemaapp.tyyjsc.adapters.RecyclerGoodsAdapter;
import com.hemaapp.tyyjsc.adapters.TaoCanGoodsAdapter;
import com.hemaapp.tyyjsc.adapters.TopAddViewPagerAdapter;
import com.hemaapp.tyyjsc.model.BannerInfor;
import com.hemaapp.tyyjsc.model.GoodsBriefIntroduction;
import com.hemaapp.tyyjsc.model.MembergoodsList;
import com.hemaapp.tyyjsc.model.ReplyId;
import com.hemaapp.tyyjsc.model.SysInitInfo;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.AutoHeightViewPager;
import com.hemaapp.tyyjsc.view.CityGridView;
import com.hemaapp.tyyjsc.view.DividerItemDecoration;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomToastUtil;

import static com.hemaapp.tyyjsc.R.id.image_right;
import static com.hemaapp.tyyjsc.R.id.refreshLoadmoreLayout;

/**
 * 首页
 */
public class FragmentIndex extends BaseFragment implements View.OnClickListener {

    private final static long hourLevelValue = 60 * 60 * 1000;
    private final static long minuteLevelValue = 60 * 1000;
    private final static long secondLevelValue = 1000;

    private FrameLayout hmMsgBtn;//消息按钮
    private ImageView dotsView;//消息条数
    private TextView hmSearchBtn;//跳转搜索按钮
    private ImageButton hmSignBtn;//签到按钮
    private boolean hasFreshed = false;//下拉刷新的操作

    private SwipeRefreshLayout mRefreshLayout;
    private ArrayList<BannerInfor> banners = new ArrayList<>(); // 广告
    private AutoHeightViewPager bannerViewPager;//图片轮播控件
    private TopAddViewPagerAdapter bannerAdapter;//图片轮播适配器
    private RelativeLayout bannerLayout;//导航索引

    private TextView tabLimitBuyView;//精品推荐
    private TextView tabValueBookView;//限时抢购
    private TextView tabValueMealView;//特价预订(一成购车)
    private TextView tabSaveCardView; //超值套餐
    private TextView tabMemberAir; //会员专区(全款购车)

    private ImageView yushou_ln;//限时抢购

    private RotateAnimation up = null;// 顺时针旋转180
    private RotateAnimation down = null; // 逆时针旋转180
    private GridLayoutManager gridLayoutManager1, gridLayoutManager3;

    //限时抢购
    private CountDownTimer countDownTimer = null;//倒计时
    private RecyclerGoodsAdapter recyclerGoodsAdapter;//限时抢购商品
    private GridViewGoodsAdapter recyclerRecommendAdapter;//推荐
    private GridViewGoodsAdapter recyclerTeJiaAdapter;//特价

    private RecyclerView qianggourecyclerView;

    private ArrayList<GoodsBriefIntroduction> realShowLimitBuyGoods = new ArrayList<>();
    private LinearLayout limitLayout = null;//限时抢购
    private TextView timeViewH = null;//时
    private TextView timeViewM = null;//分
    private TextView timeViewS = null;//秒
    private long period = 0;//时间差

    //头部局以及列表控件
    private ArrayList<GoodsBriefIntroduction> recommendGoods = new ArrayList<>();//推荐商品
    private ArrayList<GoodsBriefIntroduction> limitGoods = new ArrayList<>();//限时抢购商品
    private ArrayList<GoodsBriefIntroduction> tejias = new ArrayList<>();//特价商品
    private ArrayList<GoodsBriefIntroduction> taocans = new ArrayList<>();//套餐商品
    private User user = null;
    private ImageView news;

    private BannerInfor tuijian_banner;
    private LoginInfoReceiver loginInfoReceiver = null;//处理用户登录成功后发送的信息广播
    private ScrollView sv = null;
    private OnMsgNumChangeReceiver onMsgNumChangeReceiver = null;
    private TextView selectcity;

    /**
     * 修改精品推荐的相关UI
     */
    private ImageView tuijian_image; //精品推荐中的广告位
    private LinearLayout layout_good_0, layout_good_1;
    private ImageView img_good_0, img_good_1;
    private TextView tv_name_0, tv_name_1;
    private TextView tv_price_0, tv_price_1;
    private CityGridView grid_tuijian; //推荐商品
    private LinearLayout layout_tuijian;

    /**
     * 特价商品的UI
     * */
    private LinearLayout layout_tejia;
    private CityGridView grid_tejia;
    private TextView tejia_ln;//特价预订(一成购车)

    /**
     * 套餐商品UI
     * */
    private RecyclerView taocanrecyclerView;
    private TextView taocan_ln;
    private TaoCanGoodsAdapter recyclerTaoCanAdapter;//套餐
    private LinearLayout layout_taocan;

    /**
     * 会员专区(全款购车)UI
     * */
    private LinearLayout layout_member;
    private CityGridView huiyuan_recycler;
    private TextView huiyuan_ln;
    private ArrayList<MembergoodsList> datas = new ArrayList<>();
    private MemberGoodsListAdapters adapter;

    private GoodsBriefIntroduction good_0, good_1;
    private int width = 0;

    //创建一个handler，内部完成处理消息方法
    Handler freshCancel = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mRefreshLayout.setRefreshing(false);
            hasFreshed = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_index);
        super.onCreate(savedInstanceState);
        freshBannerData();
        init();
        loginInfoReceiver = new LoginInfoReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BaseConfig.BROADCAST_ACTION);
        intentFilter.addAction(BaseConfig.BROADCAST_GETCITY);
        getActivity().registerReceiver(loginInfoReceiver, intentFilter);
        getBannerList("2");
        onMsgNumChangeReceiver = new OnMsgNumChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BaseConfig.BROADCAST_MSG_NUM);
        getActivity().registerReceiver(onMsgNumChangeReceiver, filter);
        lazyLoad();
        getNetWorker().membergoodsList("1", "", "0");
        getWidth();
    }

    private void getWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
    }

    //获取未读消息
    public void getUnReadNoticeNum() {
        if (user == null) {
            dotsView.setVisibility(View.INVISIBLE);
        } else {
            getNetWorker().getUnReadNoticeNum(user.getToken());
        }
    }

    //初始化个人信息
    public void init() {
        user = BaseApplication.getInstance().getUser();
        getUnReadNoticeNum();
    }

    @Override
    public void onResume() {
        if (bannerAdapter != null)
            bannerAdapter.notifyDataSetChanged();
        super.onResume();
    }

    //获取图片轮播列表
    public void getBannerList(String type) {
        getNetWorker().imgList(type);
    }

    //获取推荐商品列表
    public void getRecommendGoodList() {
        getNetWorker().getIndexGoodsList("1");
    }

    //获取限时抢购商品列表
    public void getLimitGoodList() {
        getNetWorker().getIndexGoodsList("2");
    }

    //获取特价预定商品列表
    public void getHalfSellGoodList() {
        getNetWorker().getIndexGoodsList("3");
    }

    //获取特价预定商品列表
    public void getTaocanGoodList() {
        getNetWorker().getIndexGoodsList("4");
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BANNER_LIST:
                showProgressDialog(R.string.hm_hlxs_txt_34);
                break;
            case GOODS_LIST:
            case LABEL_LIST:
            case Limit_GOODS_LIST:
            case TIME_LIST:
            case GOODS_LIST_LIMIT:
                break;
            case UNREAD_NOTICE:
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BANNER_LIST:
                cancelProgressDialog();
                break;
            case GOODS_LIST:
            case LABEL_LIST:
            case Limit_GOODS_LIST:
            case TIME_LIST:
            case GOODS_LIST_LIMIT:
                break;
            case UNREAD_NOTICE:
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case UNREAD_NOTICE:
                HemaArrayResult<ReplyId> replyResult = (HemaArrayResult<ReplyId>) hemaBaseResult;
                if (replyResult.getObjects() != null && replyResult.getObjects().size() > 0) {
                    ReplyId id = replyResult.getObjects().get(0);
                    int num = Integer.parseInt(isNull(id.getNum()) ? "0" : id.getNum());
                    if (num > 0) {
                        if (dotsView.getVisibility() == View.INVISIBLE) {
                            dotsView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (dotsView.getVisibility() == View.VISIBLE) {
                            dotsView.setVisibility(View.INVISIBLE);
                        }
                    }
                } else {
                    if (dotsView.getVisibility() == View.VISIBLE) {
                        dotsView.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case BANNER_LIST:
                String Bannerkeytype = hemaNetTask.getParams().get("keytype");
                switch (Bannerkeytype) {
                    case "2":
                        HemaArrayResult<BannerInfor> bannerResult = (HemaArrayResult<BannerInfor>) hemaBaseResult;
                        banners = bannerResult.getObjects();
                        freshBannerData();
                        getBannerList("3");
                        getRecommendGoodList();
                        getLimitGoodList();
                        getHalfSellGoodList();
                        getTaocanGoodList();
                        break;
                    case "3":
                        HemaArrayResult<BannerInfor> bannerResult1 = (HemaArrayResult<BannerInfor>) hemaBaseResult;
                        if (bannerResult1.getObjects().size() == 0) {
                            tuijian_image.setVisibility(View.GONE);
                        } else {
                            tuijian_banner = bannerResult1.getObjects().get(0);
                            int v1 = (width - getResources().getDimensionPixelSize(R.dimen.margin_20))/2;
                            int w = v1 - getResources().getDimensionPixelSize(R.dimen.margin_30);
                            int h = w / 9 * 16;
                            try {
                                URL url = new URL(tuijian_banner.getImgurl());
                                imageWorker.loadImage(new XtomImageTask(tuijian_image, url, getActivity()));
                            } catch (MalformedURLException e) {
                                tuijian_image.setImageResource(R.mipmap.hm_banner_def);
                            }
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
                            params.setMargins(getResources().getDimensionPixelSize(R.dimen.margin_20), 0,
                                    getResources().getDimensionPixelSize(R.dimen.margin_30), 0 );
                            tuijian_image.setLayoutParams(params);
                        }
                        break;
                }
                break;
            case GOODSLIST://推荐商品列表
                HemaArrayResult<GoodsBriefIntroduction> nResult = (HemaArrayResult<GoodsBriefIntroduction>) hemaBaseResult;
                String keytype = hemaNetTask.getParams().get("keytype");
                switch (keytype) {
                    case "1":
                        ArrayList<GoodsBriefIntroduction> list = nResult.getObjects();
                        recommendGoods.clear();
                        recommendGoods.addAll(list);
                        freshRecommendData();
                        break;
                    case "2":
                        ArrayList<GoodsBriefIntroduction> list1 = nResult.getObjects();
                        limitGoods.clear();
                        limitGoods.addAll(list1);
                        freshData();
                        break;
                    case "3":
                        ArrayList<GoodsBriefIntroduction> list2 = nResult.getObjects();
                        tejias.clear();
                        tejias.addAll(list2);
                        freshHalfSellData();
                        break;
                    case "4":
                        ArrayList<GoodsBriefIntroduction> list3 = nResult.getObjects();
                        taocans.clear();
                        taocans.addAll(list3);
                        freshTaoCanData();
                        break;
                }
                break;
            case MEMBERGOODS_LIST:
                HemaPageArrayResult<MembergoodsList> result = (HemaPageArrayResult<MembergoodsList>) hemaBaseResult;
                String page1 = hemaNetTask.getParams().get("page");
                int sysPagesize = BaseApplication.getInstance().getSysInitInfo()
                        .getSys_pagesize();
                List<MembergoodsList> list1 = result.getObjects();
                if ("0".equals(page1)) {// 刷新
                    datas.clear();
                    datas.addAll(list1);
                } else {
                    if (list1.size() > 0) {
                        datas.addAll(list1);
                        if (list1.size() < sysPagesize)
                            XtomToastUtil.showShortToast(getActivity(), getString(R.string.hm_hlxs_txt_89));
                    }
                    adapter.notifyDataSetChanged();
                }
                freshMemberData();
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BANNER_LIST:
            case GOODS_LIST:
            case LABEL_LIST:
            case Limit_GOODS_LIST:
            case GOODS_LIST_LIMIT:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BANNER_LIST:
            case LABEL_LIST:
            case GOODS_LIST:
            case Limit_GOODS_LIST:
            case GOODS_LIST_LIMIT:
                showTextDialog("数据出错");
                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {
        hmMsgBtn = (FrameLayout) findViewById(R.id.image_left);
        dotsView = (ImageView) findViewById(R.id.dots);
        hmSearchBtn = (TextView) findViewById(R.id.index_search);
        hmSignBtn = (ImageButton) findViewById(image_right);
        news = (ImageView) findViewById(R.id.news);
        selectcity = (TextView) findViewById(R.id.id_city);
        //刷新
        mRefreshLayout = (SwipeRefreshLayout) findViewById(refreshLoadmoreLayout);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        sv = (ScrollView) findViewById(R.id.sv);
        //图片轮播
        bannerViewPager = (AutoHeightViewPager) findViewById(R.id.viewpager);
        bannerLayout = (RelativeLayout) findViewById(R.id.layout);

        //四大类别
        tabLimitBuyView = (TextView) findViewById(R.id.tab_limit_buy);
        tabValueBookView = (TextView) findViewById(R.id.tab_value_book);
        tabValueMealView = (TextView) findViewById(R.id.tab_value_meal);
        tabSaveCardView = (TextView) findViewById(R.id.tab_save_card);
        tabMemberAir = (TextView) findViewById(R.id.tab_member_area);

        //四大类别
        yushou_ln = (ImageView) findViewById(R.id.yushou_ln);
        tejia_ln = (TextView) findViewById(R.id.tv_tejia_more);
        taocan_ln = (TextView) findViewById(R.id.tv_taocan_more);

        //精品推荐
        layout_tuijian = (LinearLayout) findViewById(R.id.tuijian_layout);
        tuijian_image = (ImageView) findViewById(R.id.tuijian_image);
        layout_good_0 = (LinearLayout) findViewById(R.id.layout_good_0);
        img_good_0 = (ImageView) findViewById(R.id.img_good_0);
        tv_name_0 = (TextView) findViewById(R.id.tv_name_0);
        tv_price_0 = (TextView) findViewById(R.id.tv_price_0);
        layout_good_1 = (LinearLayout) findViewById(R.id.layout_good_1);
        img_good_1 = (ImageView) findViewById(R.id.img_good_1);
        tv_name_1 = (TextView) findViewById(R.id.tv_name_1);
        tv_price_1 = (TextView) findViewById(R.id.tv_price_1);
        grid_tuijian = (CityGridView) findViewById(R.id.grid_tuijian);

        //预售
        gridLayoutManager1 = new GridLayoutManager(getActivity(), 3);
        qianggourecyclerView = (RecyclerView) findViewById(R.id.yushou_grid);
        qianggourecyclerView.setNestedScrollingEnabled(false);
        qianggourecyclerView.setLayoutManager(gridLayoutManager1);
        qianggourecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.HORIZONTAL_LIST));
        limitLayout = (LinearLayout) findViewById(R.id.yushou_layout);
        timeViewH = (TextView) findViewById(R.id.time_h);
        timeViewM = (TextView) findViewById(R.id.time_m);
        timeViewS = (TextView) findViewById(R.id.time_s);

        //特价
        grid_tejia = (CityGridView) findViewById(R.id.grid_tejia);
        layout_tejia = (LinearLayout) findViewById(R.id.tejia_layout);

        //套餐
        gridLayoutManager3 = new GridLayoutManager(getActivity(), 3);
        taocanrecyclerView = (RecyclerView) findViewById(R.id.taocan_grid);
        taocanrecyclerView.setNestedScrollingEnabled(false);
        taocanrecyclerView.setLayoutManager(gridLayoutManager3);
        taocanrecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.HORIZONTAL_LIST));
        layout_taocan = (LinearLayout) findViewById(R.id.taocan_layout);
        //定位数据的刷新
        String city = XtomSharedPreferencesUtil.get(getActivity(), "city_name");
        if (isNull(city))
            selectcity.setText("济南市");
        else
            selectcity.setText(city);
        layout_member = (LinearLayout) findViewById(R.id.huiyuan_layout);
        huiyuan_recycler = (CityGridView) findViewById(R.id.grid_member);
        huiyuan_ln = (TextView) findViewById(R.id.tv_member_more);
    }

    //刷新轮播图
    public void freshBannerData() {
        if (banners == null || banners.size() == 0) {
            bannerViewPager.stopNext();
            banners.add(new BannerInfor(""));
        }
        if (bannerAdapter == null) {
            bannerAdapter = new TopAddViewPagerAdapter(getActivity(), banners, bannerLayout, 1);//1 首页图片轮播
            bannerViewPager.setTime(5000);
            bannerViewPager.setAdapter(bannerAdapter);
            bannerViewPager.setOnPageChangeListener(new PageChangeListener(bannerAdapter));
            bannerViewPager.setCurrentItem(10000);
        } else {
            bannerAdapter.setInfors(banners);
            bannerAdapter.notifyDataSetChanged();
        }
    }

    //刷新限时抢购数据
    public void freshData() {
        if (hasFreshed)
            freshCancel.sendEmptyMessage(1);
        //限时抢购倒计时
        qianggourecyclerView.setHasFixedSize(true);
        if (limitGoods == null || limitGoods.size() == 0) {//该时间段暂无抢购商品
            limitLayout.setVisibility(View.GONE);
        } else {
            countTimer(limitGoods.get(0));
            limitLayout.setVisibility(View.VISIBLE);
            realShowLimitBuyGoods.clear();
            int size = limitGoods.size() > 5 ? 5 : limitGoods.size();
            for (int i = 0; i < size; i++) {
                realShowLimitBuyGoods.add(limitGoods.get(i));
            }
            //限时抢购商品
            if (recyclerGoodsAdapter == null) {
                recyclerGoodsAdapter = new RecyclerGoodsAdapter(getActivity(), realShowLimitBuyGoods, "2");
                qianggourecyclerView.setAdapter(recyclerGoodsAdapter);
            } else {
                recyclerGoodsAdapter.setData(realShowLimitBuyGoods);
                recyclerGoodsAdapter.notifyDataSetChanged();
            }
        }
    }

    private void freshRecommendData() {
        if (hasFreshed)
            freshCancel.sendEmptyMessage(1);
        if(recommendGoods == null || recommendGoods.size() == 0)
            layout_tuijian.setVisibility(View.GONE);
        else{
            layout_tuijian.setVisibility(View.VISIBLE);
        }
        good_0 = recommendGoods.get(0);
        good_1 = recommendGoods.get(1);
        if(recommendGoods.contains(good_0))
            recommendGoods.remove(good_0);
        if(recommendGoods.contains(good_1))
            recommendGoods.remove(good_1);

        try {
            URL url = new URL(good_0.getImgurl());
            imageWorker.loadImage(new XtomImageTask(img_good_0, url, getActivity()));
        } catch (MalformedURLException e) {
            img_good_0.setImageResource(R.mipmap.hm_icon_def);
        }
        setImageSize(img_good_0);
        tv_name_0.setText(good_0.getName());
        tv_price_0.setText("￥"+good_0.getPrice());

        try {
            URL url = new URL(good_1.getImgurl());
            imageWorker.loadImage(new XtomImageTask(img_good_1, url, getActivity()));
        } catch (MalformedURLException e) {
            img_good_1.setImageResource(R.mipmap.hm_icon_def);
        }
        setImageSize(img_good_1);
        tv_name_1.setText(good_1.getName());
        tv_price_1.setText("￥"+good_1.getPrice());

        if (recyclerRecommendAdapter == null) {
            recyclerRecommendAdapter = new GridViewGoodsAdapter(getActivity(), recommendGoods, "1");
            grid_tuijian.setAdapter(recyclerRecommendAdapter);
        } else {
            recyclerRecommendAdapter.setData(recommendGoods);
            recyclerRecommendAdapter.notifyDataSetChanged();
        }
    }

    private void setImageSize(ImageView imageView){
        int v1 = (width - getResources().getDimensionPixelSize(R.dimen.margin_20))/2;
        int w = v1 - getResources().getDimensionPixelSize(R.dimen.margin_30);
        double h = w / 3 * 1.8;
        imageView.setLayoutParams(new LinearLayout.LayoutParams(w, (int)h));
    }

    //特价列表
    private void freshHalfSellData() {
        if (hasFreshed)
            freshCancel.sendEmptyMessage(1);
        if(tejias == null ||tejias.size() == 0)
            layout_tejia.setVisibility(View.GONE);
        else
            layout_tejia.setVisibility(View.VISIBLE);
        if (recyclerTeJiaAdapter == null) {
            recyclerTeJiaAdapter = new GridViewGoodsAdapter(getActivity(), tejias, "3");
            grid_tejia.setAdapter(recyclerTeJiaAdapter);
        } else {
            recyclerTeJiaAdapter.setData(tejias);
            recyclerTeJiaAdapter.notifyDataSetChanged();
        }
    }

    //套餐列表
    private void freshTaoCanData() {
        if (hasFreshed)
            freshCancel.sendEmptyMessage(1);
        if(taocans == null || taocans.size() == 0)
            layout_taocan.setVisibility(View.GONE);
        else
            layout_taocan.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        taocanrecyclerView.setLayoutManager(linearLayoutManager1);
        if (recyclerTaoCanAdapter == null) {
            recyclerTaoCanAdapter = new TaoCanGoodsAdapter(getActivity(), taocans);
            taocanrecyclerView.setAdapter(recyclerTaoCanAdapter);
        } else {
            recyclerTaoCanAdapter.setData(taocans);
            recyclerTaoCanAdapter.notifyDataSetChanged();
        }
    }

    private void freshMemberData(){
        if (hasFreshed)
            freshCancel.sendEmptyMessage(1);
        if(datas == null || datas.size() == 0)
            layout_member.setVisibility(View.GONE);
        else
            layout_member.setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new MemberGoodsListAdapters(getActivity(), datas);
            huiyuan_recycler.setAdapter(adapter);
        } else {
            adapter.setData(datas);
            adapter.notifyDataSetChanged();
        }
    }

    //倒计时
    public void countTimer(GoodsBriefIntroduction info) {
        Date starttime = null;//结束时间
        Date serviceDate = null;//服务器时间
        limitLayout.setVisibility(View.VISIBLE);
        limitLayout.postInvalidate();
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
                    limitLayout.setVisibility(View.GONE);
                }
            }.start();
        }

    }


    @Override
    protected void setListener() {
        hmMsgBtn.setOnClickListener(this);
        hmSearchBtn.setOnClickListener(this);
        hmSignBtn.setOnClickListener(this);
        layout_good_0.setOnClickListener(this);
        layout_good_1.setOnClickListener(this);
        yushou_ln.setOnClickListener(this);
        tejia_ln.setOnClickListener(this);
        taocan_ln.setOnClickListener(this);
        selectcity.setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBannerList("2");
                hasFreshed = true;
            }
        });
        // 加载动画
        up = (RotateAnimation) AnimationUtils.loadAnimation(getActivity(),
                R.anim.charge_rotate_up);
        down = (RotateAnimation) AnimationUtils.loadAnimation(getActivity(),
                R.anim.charge_rotate_down);
        news.setOnClickListener(this);
        tabLimitBuyView.setOnClickListener(this);
        tabValueBookView.setOnClickListener(this);
        tabValueMealView.setOnClickListener(this);
        tabSaveCardView.setOnClickListener(this);
        tabMemberAir.setOnClickListener(this);
        tuijian_image.setOnClickListener(this);
        huiyuan_ln.setOnClickListener(this);
    }

    @Override
    protected void lazyLoad() {
        //nothing
    }

    // 广告位的数据适配器
    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        TopAddViewPagerAdapter mAdapter;

        public PageChangeListener(TopAddViewPagerAdapter mAdapter) {
            this.mAdapter = mAdapter;
        }

        @Override
        public void onPageSelected(int arg0) {
            if (mAdapter != null) {
                ViewGroup indicator = mAdapter.getIndicator();
                if (indicator != null) {
                    RadioButton rbt = (RadioButton) indicator.getChildAt(arg0 % mAdapter.getBannerSize());
                    if (rbt != null)
                        rbt.setChecked(true);
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tuijian_image:
                if (tuijian_banner != null) {
                    if ("1".equals(tuijian_banner.getJump_type())) {//商品详情
                        intent = new Intent(getActivity(), ActivityTuijianGoodInfo.class);
                        intent.putExtra("id", tuijian_banner.getRelative_content());
                        startActivity(intent);
                    } else {//图文广告
                        intent = new Intent(getActivity(), ActivityWebView.class);
                        intent.putExtra("keytype", "5");
                        intent.putExtra("keyid", tuijian_banner.getRelative_content());
                        startActivity(intent);
                    }
                }
                break;
            case R.id.image_right: //客服电话
                showPhone();
                break;
            case R.id.tab_limit_buy:  //精品推荐
                intent = new Intent(getActivity(), ActivityBookGoods.class);
                intent.putExtra("keytype", "1");
                startActivity(intent);
                break;
            case R.id.tab_value_book: //预售抢购(今日特价)
                intent = new Intent(getActivity(), ActivityBookGoods.class);
                intent.putExtra("keytype", "2");
                startActivity(intent);
                break;
            case R.id.tab_value_meal: //特价商品
                intent = new Intent(getActivity(), ActivityListGoodsTejia.class);
                startActivity(intent);
                break;
            case R.id.tab_save_card: //套餐专区
                intent = new Intent(getActivity(), TaoCanActivity.class);
                intent.putExtra("keytype", "4");
                startActivity(intent);
                break;
            case R.id.layout_good_0: //推荐商品
                intent = new Intent(getActivity(), ActivityTuijianGoodInfo.class);
                intent.putExtra("id", good_0.getId());
                startActivity(intent);
                break;
            case R.id.layout_good_1: //推荐商品
                intent = new Intent(getActivity(), ActivityTuijianGoodInfo.class);
                intent.putExtra("id", good_1.getId());
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
            case R.id.yushou_ln:
                intent = new Intent(getActivity(), ActivityBookGoods.class);
                intent.putExtra("keytype", "2"); //预售抢购(今日特价)
                startActivity(intent);
                break;
            case R.id.tv_tejia_more:
                intent = new Intent(getActivity(), ActivityListGoodsTejia.class);
                startActivity(intent);
                break;
            case R.id.tv_taocan_more:
                intent = new Intent(getActivity(), TaoCanActivity.class);
                intent.putExtra("keytype", "4"); //套餐专区
                startActivity(intent);
                break;
            case R.id.tab_member_area: //会员专区(全款购车)
                ((ActivityIndex) getActivity()).change();
                break;
            case R.id.top:
                sv.smoothScrollTo(0, 0);
                break;
            case R.id.news:
                if (user != null) {
                    intent = new Intent(getActivity(), ActivityNoticeList.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
                }
                break;
            case R.id.index_search:
                intent = new Intent(getActivity(), ActivityIndexSearch.class);
                startActivity(intent);
                break;
            case R.id.id_city:
                intent = new Intent(getActivity(), CityActivity.class);
                startActivityForResult(intent, 4);
                break;
            case R.id.tv_member_more:
                ((ActivityIndex) getActivity()).change();
                break;
        }
    }

    private void showPhone() {
        final SysInitInfo sysInitInfo = BaseApplication.getInstance().getSysInitInfo();
        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_Light_Dialog);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.item_index_dialog, null);
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
        TextView tv_customPhone = (TextView) dialogView.findViewById(R.id.tv_customPhone);
        tv_customPhone.setText(sysInitInfo.getSys_service_phone());

        TextView tv_lineTel = (TextView) dialogView.findViewById(R.id.tv_lineTel);
        tv_lineTel.setText(sysInitInfo.getSys_service_qq());

        LinearLayout phone = (LinearLayout) dialogView.findViewById(R.id.ll_phone);
        LinearLayout tel = (LinearLayout) dialogView.findViewById(R.id.ll_tel);
        LinearLayout cancel = (LinearLayout) dialogView.findViewById(R.id.ll_cancel);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + sysInitInfo.getSys_service_phone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qqNum = sysInitInfo.getSys_service_qq();
                if (checkApkExist(getActivity(), "com.tencent.mobileqq")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum + "&version=1")));
                } else {
                    Toast.makeText(getActivity(), "本机未安装QQ应用", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (isNull(packageName) || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        bannerViewPager.stopNext();
        super.onDestroy();
        getActivity().unregisterReceiver(loginInfoReceiver);
        getActivity().unregisterReceiver(onMsgNumChangeReceiver);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

    }

    //广播消息接收器
    class LoginInfoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BaseConfig.BROADCAST_GETCITY.equals(intent.getAction())) {
                //XtomSharedPreferencesUtil.get(getActivity(),"city_name");
                String city = intent.getStringExtra("city");
                selectcity.setText(city);
            } else
                init();
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

    //系统消息广播
    class OnMsgNumChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            init();
        }
    }

}
