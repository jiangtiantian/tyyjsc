package com.hemaapp.tyyjsc.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.HemaWebView;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.ImgViewPagerAdapter;
import com.hemaapp.tyyjsc.adapters.MemeberGoodsViewPagerAdapter;
import com.hemaapp.tyyjsc.model.AttrInfo;
import com.hemaapp.tyyjsc.model.CartGoodsInfo;
import com.hemaapp.tyyjsc.model.HM_GoodsInfo;
import com.hemaapp.tyyjsc.model.HM_ImgInfo;
import com.hemaapp.tyyjsc.model.IdInfo;
import com.hemaapp.tyyjsc.model.MembergoodsList;
import com.hemaapp.tyyjsc.model.SysInitInfo;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.AttrSelectorDialog;
import com.hemaapp.tyyjsc.view.AutoHeightViewPager;
import com.hemaapp.tyyjsc.view.ButtonDialog;
import com.hemaapp.tyyjsc.view.CusImageView;
import com.hemaapp.tyyjsc.view.FlowLayout;
import com.hemaapp.tyyjsc.view.ShareDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

import static com.hemaapp.tyyjsc.R.id.buy;

/**
 * Created by zuozhongqian on 2017/8/9.
 * 会员商品详情
 */
public class ActivityMemeberGoodsInfo extends BaseActivity implements View.OnClickListener {

    private ImageButton hmBackBtn;//返回
    private TextView hmBarNameView;//标题栏名字
    private ImageButton share;//标题栏右侧图标按钮
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    //图片轮播
    private ArrayList<HM_ImgInfo> banners; // 广告
    private AutoHeightViewPager bannerViewPager;
    private ImgViewPagerAdapter bannerAdapter;//图片轮播适配器
    private RelativeLayout layout;
    //商品信息
    private TextView goodNameView;//商品名字
    private TextView goodsPriceView;//商品价格
    private TextView goodsOldPriceView;//商品原价
    private TextView goodsTicketView;//代金券
    private TextView goodScoreView;//可用积分
    private TextView goodsExpressFeeView;//快递费
    private TextView goodsSaleNumView;//销量
    private TextView goodsStoreNumView;//库存
    private LinearLayout goodsExpressRoleLayout;//运费
    private TextView goodsExpressFeeRoleView;//运费规则

    //标签
    private LinearLayout labelsContainer;
    private FlowLayout flowLayout;//标签
    private String[] labels;
    private HemaWebView webView;//图文详情
    private String path = "";

    //猜你喜欢
    private LinearLayout guessLikeLay;
    private ViewPager likeViewPager;
    private MemeberGoodsViewPagerAdapter goodsViewPagerAdapter;
    private RelativeLayout likeLayout;

    //加入购物车-收藏
    private TextView buyBtn;//立即购买
    private LinearLayout cartLayout;//查看购物车
    private TextView cartNumView;//购物车数量
    private ImageButton colImg;
    //购物属性弹框
    private AttrSelectorDialog win;//属性选择框
    private ArrayList<AttrInfo> attrs = new ArrayList<>();

    private int buycount = 0;//购物车数量
    private String good_id = "";//商品详情
    private HM_GoodsInfo info;//商品详情实体类
    private User user;//个人信息
    private TextView select_property;
    private LinearLayout select_property_ln;

    private ButtonDialog dialog;//分享后提示返代金券提示框
    private ShareDialog shareDialog;
    private String sharesharepath = "";//分享地址
    private SysInitInfo sysInitInfo;
    private String invite = "";
    private LinearLayout quan_layout;
    private LinearLayout like_lay;
    private CusImageView item_img;
    private TextView shop_name, shop_where;
    private ImageView iv_zixun;
    private LinearLayout ll_layout_jifen;

    private ArrayList<MembergoodsList> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_member_info);
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        setColor(mContext);
        invite = getString(R.string.hm_hlxs_txt_132);
        sysInitInfo = BaseApplication.getInstance().getSysInitInfo();
        freshBannerData();
        user = BaseApplication.getInstance().getUser();
        getGoodsInfo();
        cartNumView.setVisibility(View.GONE);
        buyBtn.setText("立即兑换");
        buyBtn.setBackgroundColor(ContextCompat.getColor(ActivityMemeberGoodsInfo.this, R.color.color_gaga));
        ll_layout_jifen.setVisibility(View.GONE);
        SysInitInfo initInfo = BaseApplication.getInstance().getSysInitInfo();
        String sys_plugins = initInfo.getSys_plugins();
        sharesharepath = sys_plugins + "share/sdk.php?id=" + good_id + "&keytype=5";
    }

    //获取商品详情
    public void getGoodsInfo() {
        getNetWorker().getGoodsInfo(good_id, "5");
    }

    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);
        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText(R.string.hm_hlxs_txt_225);
        share = (ImageButton) findViewById(R.id.share);
        //刷新
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        //图片轮播
        bannerViewPager = (AutoHeightViewPager) findViewById(R.id.viewpager);
        layout = (RelativeLayout) findViewById(R.id.layout);
        //商品信息
        goodNameView = (TextView) findViewById(R.id.good_name);
        goodsPriceView = (TextView) findViewById(R.id.good_price);
        goodsOldPriceView = (TextView) findViewById(R.id.good_old_price);
        goodsTicketView = (TextView) findViewById(R.id.quan);
        goodScoreView = (TextView) findViewById(R.id.score);
        goodsExpressFeeView = (TextView) findViewById(R.id.express_fee);
        goodsSaleNumView = (TextView) findViewById(R.id.sale_num);
        goodsStoreNumView = (TextView) findViewById(R.id.store_num);
        goodsExpressRoleLayout = (LinearLayout) findViewById(R.id.role_ll);
        goodsExpressFeeRoleView = (TextView) findViewById(R.id.role);

        //流布局标签
        flowLayout = (FlowLayout) findViewById(R.id.labels);
        labelsContainer = (LinearLayout) findViewById(R.id.labelscontainer);
        //商品参数
        webView = (HemaWebView) findViewById(R.id.webview);
        webView.setFocusable(false);
        quan_layout = (LinearLayout) findViewById(R.id.quan_layout);
        select_property = (TextView) findViewById(R.id.select_property);
        select_property_ln = (LinearLayout) findViewById(R.id.select_property_ln);
        //店铺的信息
        item_img = (CusImageView) findViewById(R.id.item_img);
        shop_name = (TextView) findViewById(R.id.shop_name);
        shop_where = (TextView) findViewById(R.id.shop_where);

        //猜你喜欢
        guessLikeLay = (LinearLayout) findViewById(R.id.guesslike);
        likeViewPager = (ViewPager) findViewById(R.id.viewpager1);
        like_lay = (LinearLayout) findViewById(R.id.like_lay);
        likeLayout = (RelativeLayout) findViewById(R.id.layout_1);

        //底部
        buyBtn = (TextView) findViewById(R.id.buy);
        cartLayout = (LinearLayout) findViewById(R.id.cart);
        cartNumView = (TextView) findViewById(R.id.cart_num);
        colImg = (ImageButton) findViewById(R.id.collection);

        iv_zixun = (ImageView) findViewById(R.id.iv_zixun);
        iv_zixun.setImageResource(R.mipmap.custom_service);

        ll_layout_jifen = (LinearLayout) findViewById(R.id.ll_layout_jifen);
    }

    @Override
    protected void getExras() {
        good_id = getIntent().getStringExtra("id");
    }

    @Override
    protected void setListener() {
        hmBackBtn.setOnClickListener(this);
        share.setOnClickListener(this);
        buyBtn.setOnClickListener(this);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getGoodsInfo();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
            }
        });
        refreshLoadmoreLayout.setLoadmoreable(false);
        cartLayout.setOnClickListener(this);
        colImg.setOnClickListener(this);
        share.setOnClickListener(this);
        select_property.setOnClickListener(this);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_GET:
                showProgressDialog(R.string.hm_hlxs_txt_59);
                break;
            case LOVE_ADD:
            case LOVE_CANCEL:
            case SHARE:
            case MEMBERGOODS_LIST:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_GET:
                cancelProgressDialog();
            case MEMBERGOODS_LIST:
            case LOVE_ADD:
            case LOVE_CANCEL:
            case SHARE:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_GET:
                refreshLoadmoreLayout.refreshSuccess();
                HemaArrayResult<HM_GoodsInfo> goodsResult = (HemaArrayResult<HM_GoodsInfo>) hemaBaseResult;
                if (goodsResult != null && goodsResult.getObjects() != null && goodsResult.getObjects().size() > 0) {
                    info = goodsResult.getObjects().get(0);
                }
                updateColUI();
                getNetWorker().membergoodsList("2", info.getId(), "0");
                break;
            case LOVE_ADD:
                String keytype = hemaNetTask.getParams().get("keytype");
                switch (keytype) {
                    case "1":
                        if (info != null)
                            info.setCollect("1");
                        updateColUI();
                        break;
                    case "2":
                        if (info != null)
                            info.setCollect("0");
                        updateColUI();
                        break;
                }
                break;
            case SHARE:
                HemaArrayResult<IdInfo> valueResult = (HemaArrayResult<IdInfo>) hemaBaseResult;
                if (valueResult.getObjects() != null && valueResult.getObjects().size() > 0) {
                    IdInfo idInfo = valueResult.getObjects().get(0);
                    if (dialog == null) {
                        dialog = new ButtonDialog(ActivityMemeberGoodsInfo.this);
                    }
                    dialog.setRes(R.mipmap.share);
                    dialog.setValueText("+" + idInfo.getVoucher_value() + getString(R.string.hm_hlxs_txt_137));
                    dialog.setText(R.string.hm_hlxs_txt_123);
                    dialog.setListener(new ButtonDialog.onConfrimListener() {
                        @Override
                        public void onConfirm() {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
                break;
            case MEMBERGOODS_LIST:
                HemaPageArrayResult<MembergoodsList> mResult1 = (HemaPageArrayResult<MembergoodsList>) hemaBaseResult;
                datas = mResult1.getObjects();
                guessLike();
                freshData();
                break;
        }
    }

    //处理收藏显示
    public void updateColUI() {
        if (info != null) {
            if ("1".equals(info.getCollect())) {//已收藏
                colImg.setImageResource(R.mipmap.collection);
            } else {//取消收藏
                colImg.setImageResource(R.mipmap.nocollection);
            }
        }
    }

    //商品图片轮播
    public void freshBannerData() {
        banners = info == null ? new ArrayList<HM_ImgInfo>() : info.getImgs();
        if (banners == null || banners.size() == 0) {
            bannerViewPager.stopNext();
            banners.add(new HM_ImgInfo(""));
        }
        if (bannerAdapter == null) {
            bannerAdapter = new ImgViewPagerAdapter(ActivityMemeberGoodsInfo.this, banners, layout);
            bannerViewPager.setTime(3000);
            bannerViewPager.setAdapter(bannerAdapter);
            bannerViewPager.setOnPageChangeListener(new PageChangeListener(bannerAdapter));
            bannerViewPager.setCurrentItem(10000);
        } else {
            bannerAdapter.setInfors(banners);
            bannerAdapter.notifyDataSetChanged();
        }
    }

    public void freshData() {
        freshBannerData();
        //商品简介
        goodNameView.setText(info.getName());
        goodsPriceView.setText(info.getCoupon() + "点券");  //商品的价格
        goodsOldPriceView.setVisibility(View.GONE);
        //运费规则
        if (isNull(info.getFull_fee())) {//无满减规则
            goodsExpressRoleLayout.setVisibility(View.GONE);
        } else {
            double fee = Double.parseDouble(info.getFull_fee());
            if (fee == 0.00) {//全场包邮
                goodsExpressRoleLayout.setVisibility(View.VISIBLE);
                goodsExpressFeeRoleView.setText(R.string.hm_hlxs_txt_229);
            } else {
                goodsExpressRoleLayout.setVisibility(View.VISIBLE);
                goodsExpressFeeRoleView.setText(getString(R.string.hm_hlxs_txt_231) + info.getFull_fee() + getString(R.string.hm_hlxs_txt_230));
            }
        }
        /**
         * keytype:商品类型1普通商品；2限时抢购；3特价预订(一成购车)；4超值套餐；
         * 赠送代金券：1、2、3、4
         * 可用积分：1、3、4 限时抢购没有可用积分
         */
        if (isNull(info.getVouchers_money())) {
            quan_layout.setVisibility(View.GONE);
        } else
            goodsTicketView.setText("可用代金券额度" + info.getVouchers_money());
        goodScoreView.setText("可用积分为" + (isNull(info.getScore())? "0" : info.getScore()));
//        //运费
        goodsExpressFeeView.setText("配运费" + 0 + "元");

        //已售
        goodsSaleNumView.setText(getString(R.string.hm_hlxs_txt_237) + info.getDisplaysales());
        /**
         * 库存：限时抢购、特价预订(一成购车)、超值套餐有库存；其他类型没有库存
         */
        goodsStoreNumView.setVisibility(View.VISIBLE);
        goodsStoreNumView.setText(getString(R.string.hm_hlxs_txt_239) + info.getStock());

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) labelsContainer.getLayoutParams();
        params.topMargin = BaseUtil.dip2px(ActivityMemeberGoodsInfo.this, 10);
        labelsContainer.setLayoutParams(params);
        //商品标签
        if (!isNull(info.getFeaturetype())) {
            labels = info.getFeaturetype().split(",");
            flowLayout.removeAllViews();
            for (int i = 0; i < labels.length; i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.label_item, null);
                TextView labelNameView = (TextView) view.findViewById(R.id.labelname);
                labelNameView.setText(labels[i]);
                flowLayout.addView(view);
            }
        } else {
            if (labels == null || labels.length == 0) {
                View view = LayoutInflater.from(this).inflate(R.layout.label_item, null);
                TextView labelNameView = (TextView) view.findViewById(R.id.labelname);
                labelNameView.setText("暂无");
                flowLayout.addView(view);
            }
        }

        //商品参数
        //属性组合
        attrs = info.getAttrs();
        if (attrs.size() == 0) {//隐藏掉
            select_property_ln.setVisibility(View.GONE);
        } else {
            select_property_ln.setVisibility(View.VISIBLE);
            select_property.setText(attrs.get(0).getName());
        }
        //店铺的信息
        ImageLoader.getInstance().displayImage(info.getShopimgurl(), item_img, BaseUtil.displayImageOption());
        shop_name.setText(info.getShopname());
        shop_where.setText(info.getShopaddress());
        //图文详情
        path = BaseApplication.getInstance().getSysInitInfo().getSys_web_service() + "webview/parm/membergoods/id/" + info.getId();
        webView.loadUrl(path);
    }

    //猜你喜欢
    public void guessLike() {
        if (datas == null || datas.size() == 0) {//如果该商品尚未设置猜你喜欢商品时，隐藏猜你喜欢布局，反之，显示；
            like_lay.setVisibility(View.GONE);
        } else {
            guessLikeLay.setVisibility(View.VISIBLE);
            if (goodsViewPagerAdapter == null) {
                goodsViewPagerAdapter = new MemeberGoodsViewPagerAdapter(ActivityMemeberGoodsInfo.this, datas, likeLayout);
                likeViewPager.setAdapter(goodsViewPagerAdapter);
                likeViewPager.setOnPageChangeListener(new GoodsPageChangeListener(goodsViewPagerAdapter));
                likeViewPager.setOffscreenPageLimit(10);
            } else {
                goodsViewPagerAdapter.setInfors(datas);
                goodsViewPagerAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_GET:
            case LOVE_ADD:
            case LOVE_CANCEL:
            case SHARE:
            case MEMBERGOODS_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_GET:
                refreshLoadmoreLayout.refreshFailed();
                break;
            case LOVE_ADD:
            case LOVE_CANCEL:
            case SHARE:
            case MEMBERGOODS_LIST:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_left:
                finish();
                break;
            case R.id.bar_right_img:
                showShare();
                break;
            case R.id.collection://收藏（特价预订(一成购车)限时抢购()无收藏）
                user = BaseApplication.getInstance().getUser();
                if (user == null) {
                    Intent intent = new Intent(ActivityMemeberGoodsInfo.this, Login.class);
                    startActivity(intent);
                } else if (info != null) {//取消或添加收藏
                    if ("1".equals(info.getCollect())) {
                        //取消收藏
                        getNetWorker().loveAdd(user.getToken(), "2", "5", info.getId());
                    } else {
                        //收藏
                        getNetWorker().loveAdd(user.getToken(), "1", "5", info.getId());
                    }
                }
                break;
            case buy://立即兑换
                int stock = Integer.parseInt(isNull(info.getStock()) ? "0" : info.getStock());
                if (stock <= 0) {//已售罄
                    showTextDialog("库存不足");
                    return;
                }
                user = BaseApplication.getInstance().getUser();
                if (user == null) {
                    Intent intent = new Intent(ActivityMemeberGoodsInfo.this, Login.class);
                    startActivity(intent);
                } else {
                    if (win == null) {
                        win = new AttrSelectorDialog(ActivityMemeberGoodsInfo.this, info);
                    }
                    win.setGoodInfo(info);
                    win.setAddTxt("立即兑换");
                    win.setListener(new AttrSelectorDialog.onAddCardListener() {
                        @Override
                        public void onAddCart(String specs_name, String spec_id, String num, String price, String weight) {
                            win.cancel();

                            ArrayList<CartGoodsInfo> data = new ArrayList<>();
                            if (info != null) {
                                String path = "";
                                if (info.getImgs() != null && info.getImgs().size() > 0) {
                                    path = info.getImgs().get(0).getImgurl();
                                }

                                double d = Double.parseDouble(isNull(weight) ? "0.0" : weight);
                                double d1 = Double.parseDouble(num);

                                CartGoodsInfo goodsInfo = new CartGoodsInfo("", info.getId(), info.getShopid(), info.getShopname(), info.getByprice(),
                                        info.getName(), path, "", num, spec_id, specs_name, info.getScore(), true, "", "", price, "5",
                                        weight, BaseUtil.get2double(d * d1));

                                goodsInfo.setCoupon(Double.parseDouble(price) + "");
                                goodsInfo.setGoodsid(info.getId());
                                data.add(goodsInfo);

                                Intent it = new Intent(ActivityMemeberGoodsInfo.this, ActivityConfirmOrder.class);
                                it.putExtra("data", data);
                                it.putExtra("count", 1);
                                it.putExtra("flag", "1");
                                it.putExtra("justbuy", true);
                                it.putExtra("keytype", "5");//立即购买
                                startActivity(it);
                            }
                        }
                    });
                    win.show();
                }
                break;
            case R.id.share:
                showShare();
                break;
            case R.id.select_property:
                //添加属性
                if (win == null) {
                    win = new AttrSelectorDialog(ActivityMemeberGoodsInfo.this, info);
                }
                win.setGoodInfo(info);
                win.setAddTxt("确定");
                win.setListener(new AttrSelectorDialog.onAddCardListener() {
                    @Override
                    public void onAddCart(String specName, String spec_id, String num, String price, String weight) {
                        win.cancel();
                        select_property.setText(specName);
                    }
                });
                win.show();
                break;
            case R.id.cart:
                showPhone();
                break;
        }
    }

    private void showPhone() {
        final SysInitInfo sysInitInfo = BaseApplication.getInstance().getSysInitInfo();
        final Dialog dialog = new Dialog(mContext, R.style.Theme_Light_Dialog);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.item_index_dialog, null);
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
                if (checkApkExist(mContext, "com.tencent.mobileqq")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum + "&version=1")));
                } else {
                    Toast.makeText(mContext, "本机未安装QQ应用", Toast.LENGTH_SHORT).show();
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


    // 猜你喜欢商品适配器
    private class GoodsPageChangeListener implements ViewPager.OnPageChangeListener {
        MemeberGoodsViewPagerAdapter mAdapter;

        public GoodsPageChangeListener(MemeberGoodsViewPagerAdapter mAdapter) {
            this.mAdapter = mAdapter;
        }

        @Override
        public void onPageSelected(int arg0) {
            if (mAdapter != null) {
                ViewGroup indicator = mAdapter.getIndicator();
                if (indicator != null) {
                    RadioButton rbt = (RadioButton) indicator.getChildAt(arg0);
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

    // 广告位的数据适配器
    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        ImgViewPagerAdapter mAdapter;

        public PageChangeListener(ImgViewPagerAdapter mAdapter) {
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
    protected void onDestroy() {
        super.onDestroy();
        //二期
        if (webView != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
            webView.removeAllViews();
            webView.destroyDrawingCache();
            webView.destroy();
        }
        super.onDestroy();
    }

    public void onCartNumChange(String offset) {

        buycount = Integer.parseInt(isNull(offset) ? "0" : offset);
        if (buycount >= 0) {
            cartNumView.setText(String.valueOf(buycount));
        }
    }

    class OnCartNumChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String offset = intent.getStringExtra("buycount");
            onCartNumChange(offset);
        }
    }

    /* 分享相关 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    //友盟分享
    public void showShare() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("请稍等...");
        Config.dialog = dialog;
        if (shareDialog == null) {
            shareDialog = new ShareDialog(mContext);
        }
        shareDialog.setListener(new onClickPlatformShareListener());
        shareDialog.show();
    }

    //各平台下的分享
    class onClickPlatformShareListener implements ShareDialog.onShareListener {

        @Override
        public void onShare(int which) {
            switch (which) {
                case 0://QQ
                    new ShareAction(ActivityMemeberGoodsInfo.this)
                            .setPlatform(SHARE_MEDIA.QQ)
                            .setCallback(new OnUmShareListener())
                            .withTitle(getResources().getString(R.string.app_name))
                            .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withTargetUrl(sharesharepath)
                            .withMedia(new UMImage(ActivityMemeberGoodsInfo.this, R.mipmap.ic_launcher))
                            .share();
                    break;
                case 1://新浪
                    showTextDialog("暂未开通");
//                    new ShareAction(ActivityGoodsInfo.this)
//                            .setPlatform(SHARE_MEDIA.SINA)
//                            .setCallback(new OnUmShareListener())
//                            .withTitle(getResources().getString(R.string.app_name))
//                            .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
//                            .withTargetUrl(sharesharepath)
//                            .withMedia(new UMImage(ActivityGoodsInfo.this, R.mipmap.hm_icon_app))
//                            .share();
                    break;

                case 2://微信
                    new ShareAction(ActivityMemeberGoodsInfo.this)
                            .setPlatform(SHARE_MEDIA.WEIXIN)
                            .setCallback(new OnUmShareListener())
                            .withTitle(getResources().getString(R.string.app_name))
                            .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withTargetUrl(sharesharepath)
                            .withMedia(new UMImage(ActivityMemeberGoodsInfo.this, R.mipmap.ic_launcher))
                            .share();
                    break;
                case 3://朋友圈
                    new ShareAction(ActivityMemeberGoodsInfo.this)
                            .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                            .setCallback(new OnUmShareListener())
                            .withTitle(getResources().getString(R.string.app_name))
                            .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withTargetUrl(sharesharepath)
                            .withMedia(new UMImage(ActivityMemeberGoodsInfo.this, R.mipmap.ic_launcher))
                            .share();
                    break;
                case 4://新浪
                    new ShareAction(ActivityMemeberGoodsInfo.this)
                            .setPlatform(SHARE_MEDIA.QZONE)
                            .setCallback(new OnUmShareListener())
                            .withTitle(getResources().getString(R.string.app_name))
                            .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withTargetUrl(sharesharepath)
                            .withMedia(new UMImage(ActivityMemeberGoodsInfo.this, R.mipmap.ic_launcher))
                            .share();
                    break;
            }
        }
    }

    //友盟分享回调
    class OnUmShareListener implements UMShareListener {

        @Override
        public void onResult(SHARE_MEDIA share_media) {

            XtomToastUtil.showShortToast(mContext, "分享成功");
            hmBarNameView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (user != null) {
                        getNetWorker().share(user.getToken(), "0");
                    }
                }
            }, 300);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            hmBarNameView.post(new Runnable() {
                @Override
                public void run() {
                    XtomToastUtil.showShortToast(mContext, "分享失败");
                }
            });
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            hmBarNameView.post(new Runnable() {
                @Override
                public void run() {
                    XtomToastUtil.showShortToast(mContext, "分享取消");
                }
            });
        }
    }
    /* 分享相关end */
}
