package com.hemaapp.tyyjsc.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.hemaapp.tyyjsc.adapters.CommentAdapter;
import com.hemaapp.tyyjsc.adapters.GoodsViewPagerAdapter;
import com.hemaapp.tyyjsc.adapters.ImgViewPagerAdapter;
import com.hemaapp.tyyjsc.model.AttrInfo;
import com.hemaapp.tyyjsc.model.CartGoodsInfo;
import com.hemaapp.tyyjsc.model.Comment;
import com.hemaapp.tyyjsc.model.GoodsBriefIntroduction;
import com.hemaapp.tyyjsc.model.HM_GoodsInfo;
import com.hemaapp.tyyjsc.model.HM_ImgInfo;
import com.hemaapp.tyyjsc.model.IdInfo;
import com.hemaapp.tyyjsc.model.ShopCart;
import com.hemaapp.tyyjsc.model.SysInitInfo;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.AttrSelectorDialog;
import com.hemaapp.tyyjsc.view.AutoHeightViewPager;
import com.hemaapp.tyyjsc.view.ButtonDialog;
import com.hemaapp.tyyjsc.view.CusImageView;
import com.hemaapp.tyyjsc.view.FlowLayout;
import com.hemaapp.tyyjsc.view.NoScrollListView;
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
 * Created by wangyuxia on 2017/9/5.
 * 精品推荐商品/普通商品
 */
public class ActivityTuijianGoodInfo extends BaseActivity implements View.OnClickListener {

    private ImageButton left;//返回
    private TextView tv_title_name;//标题栏名字
    private ImageButton title_share;//标题栏右侧图标按钮
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
    private FlowLayout flowLayout;//标签
    private String[] labels = null;
    private HemaWebView webView;//图文详情
    private String path = "";
    private NoScrollListView listView;//商品评价
    private ArrayList<Comment> totalComments = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private TextView cmNumView;//总评论数
    private TextView starsContainer;//星级

    //猜你喜欢
    private LinearLayout guessLikeLay;
    private ViewPager likeViewPager;
    private RelativeLayout likeLayout;
    private LinearLayout like_lay;
    private GoodsViewPagerAdapter goodsViewPagerAdapter;
    private ArrayList<GoodsBriefIntroduction> likes;

    //加入购物车-收藏
    private LinearLayout addToCartLayout;//加入购物车
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
    private String invite = "", weight;
    private LinearLayout quan_layout;
    private CusImageView item_img;
    private TextView shop_name, shop_where;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_tuijian_info);
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
        SysInitInfo initInfo = BaseApplication.getInstance().getSysInitInfo();
        String sys_plugins = initInfo.getSys_plugins();
        sharesharepath = sys_plugins + "title_share/sdk.php?id=" + good_id + "&keytype=1";
        colImg.setVisibility(View.VISIBLE);
        getGoodList("9", good_id, "");
    }

    //获取商品详情
    public void getGoodsInfo() {
        getNetWorker().getGoodsInfo(good_id, "1");
    }

    //评论列表
    public void getCommentList(String keyid) {
        getNetWorker().getCommentList("1", keyid, String.valueOf(0));
    }

    //获取喜欢列表
    public void getGoodList(String keytype, String id, String keyword) {
        getNetWorker().getGoodsList(keytype, id, keyword, "0", "");
    }

    //获取购物车商量
    public void getCartList(String type) {
        getNetWorker().getCartGoodsList(user.getToken(), type);
    }

    @Override
    protected void findView() {
        left = (ImageButton) findViewById(R.id.back_left);
        left.setVisibility(View.VISIBLE);
        tv_title_name = (TextView) findViewById(R.id.bar_name);
        tv_title_name.setText(R.string.hm_hlxs_txt_225);
        title_share = (ImageButton) findViewById(R.id.share);
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
        //商品参数
        webView = (HemaWebView) findViewById(R.id.webview);
        webView.setFocusable(false);
        listView = (NoScrollListView) findViewById(R.id.cms);
        quan_layout = (LinearLayout) findViewById(R.id.quan_layout);
        cmNumView = (TextView) findViewById(R.id.total_cm_num);
        starsContainer = (TextView) findViewById(R.id.stars);
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
        addToCartLayout = (LinearLayout) findViewById(R.id.add_cart);
        buyBtn = (TextView) findViewById(buy);
        cartLayout = (LinearLayout) findViewById(R.id.cart);
        cartNumView = (TextView) findViewById(R.id.cart_num);
        colImg = (ImageButton) findViewById(R.id.collection);
    }

    @Override
    protected void getExras() {
        good_id = mIntent.getStringExtra("id");
    }

    @Override
    protected void setListener() {
        left.setOnClickListener(this);
        title_share.setOnClickListener(this);
        addToCartLayout.setOnClickListener(this);
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
        title_share.setOnClickListener(this);
        select_property.setOnClickListener(this);
        starsContainer.setOnClickListener(this);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_GET:
                showProgressDialog(R.string.hm_hlxs_txt_59);
                break;
            case CART_LIST:
            case REPLY_LIST:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_GET:
                cancelProgressDialog();
            case CART_LIST:
            case REPLY_LIST:
                break;
        }
    }

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
                getCommentList(good_id);
                break;
            case GOODS_LIST:
                HemaPageArrayResult<GoodsBriefIntroduction> likeResult = (HemaPageArrayResult<GoodsBriefIntroduction>) hemaBaseResult;
                likes = likeResult.getObjects();
                guessLike();
                break;
            case CART_ADD:
                showTextDialog("添加成功");
                getCartList("1");
                break;
            case LOVE_ADD:
                String keytype = hemaNetTask.getParams().get("keytype");
                switch (keytype) {
                    case "1":
                        if (info != null) {
                            info.setCollect("1");
                        }
                        updateColUI();
                        break;
                    case "2":
                        if (info != null) {
                            info.setCollect("0");
                        }
                        updateColUI();
                        break;
                }
                break;
            case CART_LIST:
                HemaArrayResult<ShopCart> nResult = (HemaArrayResult<ShopCart>) hemaBaseResult;
                ArrayList<ShopCart> data = nResult.getObjects();
                getCartGoodsNum(data);
                break;
            case REPLY_LIST:
                HemaPageArrayResult<Comment> result = (HemaPageArrayResult<Comment>) hemaBaseResult;
                totalComments = result.getObjects();
                if (user != null) {
                    getCartList("1");
                } else
                    cartNumView.setText(String.valueOf(0) + "+");
                freshData();
                break;
            case SHARE:
                HemaArrayResult<IdInfo> valueResult = (HemaArrayResult<IdInfo>) hemaBaseResult;
                if (valueResult.getObjects() != null && valueResult.getObjects().size() > 0) {
                    IdInfo idInfo = valueResult.getObjects().get(0);
                    if (dialog == null) {
                        dialog = new ButtonDialog(mContext);
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
        }
    }

    //显示购物车中商品总数
    public void getCartGoodsNum(ArrayList<ShopCart> data) {
        buycount = 0;
        for (ShopCart info : data) {
            buycount += info.getGoods() == null ? 0 : info.getGoods().size();
        }
        cartNumView.setText(String.valueOf(buycount));
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
            bannerAdapter = new ImgViewPagerAdapter(mContext, banners, layout);
            bannerViewPager.setTime(3000);
            bannerViewPager.setAdapter(bannerAdapter);
            bannerViewPager.setOnPageChangeListener(new PageChangeListener(bannerAdapter));
            bannerViewPager.setCurrentItem(10000);
        } else {
            bannerAdapter.setInfors(banners);
            bannerAdapter.notifyDataSetChanged();
        }
    }

    //猜你喜欢
    public void guessLike() {
        if (likes == null || likes.size() == 0) {//如果该商品尚未设置猜你喜欢商品时，隐藏猜你喜欢布局，反之，显示；
            like_lay.setVisibility(View.GONE);
        } else {
            guessLikeLay.setVisibility(View.VISIBLE);
            if (goodsViewPagerAdapter == null) {
                goodsViewPagerAdapter = new GoodsViewPagerAdapter(mContext, likes, likeLayout);
                likeViewPager.setAdapter(goodsViewPagerAdapter);
                likeViewPager.setOnPageChangeListener(new GoodsPageChangeListener(goodsViewPagerAdapter));
                likeViewPager.setOffscreenPageLimit(10);
            } else {
                goodsViewPagerAdapter.setInfors(likes);
                goodsViewPagerAdapter.notifyDataSetChanged();
            }
        }
    }

    public void freshData() {
        freshBannerData();
        //商品简介
        goodNameView.setText(info.getName());
        goodsPriceView.setText("￥" + info.getPrice());  //商品的价格
        BaseUtil.setThroughSpan(goodsOldPriceView, getString(R.string.hm_hlxs_txt_120) + info.getOriginalprice());

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
         * keytype:商品类型1普通商品；2限时抢购；3特价预订；4超值套餐；
         * 赠送代金券：1、2、3、4
         * 可用积分：1、3、4 限时抢购没有可用积分
         */
        if (isNull(info.getVouchers_money())) {
            quan_layout.setVisibility(View.GONE);
        } else
            goodsTicketView.setText("可用代金券额度" + info.getVouchers_money());
        goodScoreView.setText("可用积分为" + info.getScore());
//        //运费
        if (isNull(info.getByprice()))
            goodsExpressFeeView.setText("配运费" + info.getShipment() + "元");
        else
            goodsExpressFeeView.setText("配运费" + info.getShipment() + "元 满￥" + info.getByprice() + "包邮");

        //已售
        goodsSaleNumView.setText(getString(R.string.hm_hlxs_txt_237) + info.getDisplaysales());
        /**
         * 库存：限时抢购、特价预订、超值套餐有库存；其他类型没有库存
         */
        goodsStoreNumView.setVisibility(View.VISIBLE);
        goodsStoreNumView.setText(getString(R.string.hm_hlxs_txt_239) + info.getStock());
        starsContainer.setText(info.getGoodsreply() + "%");

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
        /**
         * 1普通商品；2限时抢购；3特价预订；4超值套餐；
         * 抢购商品：无猜你喜欢，存在WebView和评论列表
         * 推荐商品、特价预订：存在猜你喜欢、WebView和评论列表
         * 超值套餐：无猜你喜欢，存在套餐列表、WebView和评论列表
         * 特价预订：存在猜你喜欢
         */
        String id = info.getId();
        path = BaseApplication.getInstance().getSysInitInfo().getSys_web_service() + "webview/parm/goods/id/" + id;
        webView.loadUrl(path);

        cmNumView.setText("评价" + info.getReplycount() + "");
        //评论
        if (commentAdapter == null) {
            commentAdapter = new CommentAdapter(mContext, totalComments, "1");
            commentAdapter.setEmptyString("暂无评论");
            commentAdapter.setKeyid(good_id);
            listView.setAdapter(commentAdapter);
        } else {
            commentAdapter.setData(totalComments);
            commentAdapter.setEmptyString("暂无评论");
            commentAdapter.notifyDataSetChanged();
            commentAdapter.setKeyid(good_id);
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_GET:
            case CART_ADD:
            case LOVE_ADD:
            case LOVE_CANCEL:
            case CART_LIST:
            case REPLY_LIST:
            case SHARE:
                showTextDialog(hemaBaseResult.getMsg());
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_GET:
                refreshLoadmoreLayout.refreshFailed();
                break;
            case CART_ADD:
            case REPLY_LIST:
            case LOVE_ADD:
            case LOVE_CANCEL:
            case CART_LIST:
            case SHARE:
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
            case R.id.collection://收藏（特价预订限时抢购无收藏）
                toCollection();
                break;
            case R.id.cart://查看购物车（限时抢购无查看购物车
                toCheckShopCart();
                break;
            case R.id.add_cart://加入购物车或购物清单
                toAddShopCart();
                break;
            case buy://立即购买
                toBuy();
                break;
            case R.id.share:
                showShare();
                break;
            case R.id.select_property:
                toSelectProperty();
                break;
            case R.id.stars:
                Intent intent = new Intent(mContext, ActivityComment.class);
                intent.putExtra("keytype", "1");
                startActivity(intent);
                break;
        }
    }

    private void toCollection() {
        user = BaseApplication.getInstance().getUser();
        if (user == null) {
            Intent intent = new Intent(mContext, Login.class);
            startActivity(intent);
        } else if (info != null) {//取消或添加收藏
            if ("1".equals(info.getCollect())) {
                //取消收藏
                getNetWorker().loveAdd(user.getToken(), "2", "1", info.getId());
            } else {
                //收藏
                getNetWorker().loveAdd(user.getToken(), "1", "1", info.getId());
            }
        }
    }

    private void toCheckShopCart() {
        user = BaseApplication.getInstance().getUser();
        if (user == null) {
            Intent intent = new Intent(mContext, Login.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(mContext, ActivityIndex.class);
            intent.putExtra("which", "3");
            startActivity(intent);
        }
    }

    private void toAddShopCart() {
        user = BaseApplication.getInstance().getUser();
        if (user == null) {
            Intent intent = new Intent(mContext, Login.class);
            startActivity(intent);
        } else {
            if (win == null) {
                win = new AttrSelectorDialog(mContext, info);
            }
            win.setGoodInfo(info);
            win.setAddTxt("加入购物车");
            win.setListener(new AttrSelectorDialog.onAddCardListener() {
                @Override
                public void onAddCart(String specName, String spec_id, String num, String price, String weight) {
                    win.cancel();
                    getNetWorker().cartAdd(user.getToken(), "1", info.getId(), spec_id, num);
                }
            });
            win.show();
        }
    }

    private void toBuy() {
        int stock = Integer.parseInt(isNull(info.getStock()) ? "0" : info.getStock());
        if (stock <= 0) {//已售罄
            showTextDialog("库存不足");
            return;
        }
        user = BaseApplication.getInstance().getUser();
        if (user == null) {
            Intent intent = new Intent(mContext, Login.class);
            startActivity(intent);
        } else {
            if (win == null) {
                win = new AttrSelectorDialog(mContext, info);
            }
            win.setGoodInfo(info);
            win.setAddTxt(buyBtn.getText().toString());
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
                        double d = Double.parseDouble(weight);
                        double d1 = Double.parseDouble(num);

                        CartGoodsInfo goodsInfo = new CartGoodsInfo("", info.getId(), info.getShopid(), info.getShopname(), info.getByprice(),
                                info.getName(), path, price, num, spec_id, specs_name, info.getScore(), true, "", "", info.getCoupon(), "1",
                                info.getWeight(), BaseUtil.get2double(d * d1));
                        goodsInfo.setGoodsprice(Double.parseDouble(price) * Double.parseDouble(num) + "");
                        goodsInfo.setGoodsid(info.getId());

                        data.add(goodsInfo);
                        Intent it = new Intent(mContext, ActivityConfirmOrder.class);
                        it.putExtra("data", data);
                        it.putExtra("count", 1);
                        it.putExtra("justbuy", true);
                        it.putExtra("keytype", "1");//立即购买
                        startActivity(it);
                    }
                }
            });
            win.show();
        }
    }

    private void toSelectProperty() {
        if (win == null) {
            win = new AttrSelectorDialog(mContext, info);
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
                    new ShareAction(mContext)
                            .setPlatform(SHARE_MEDIA.QQ)
                            .setCallback(new OnUmShareListener())
                            .withTitle(getResources().getString(R.string.app_name))
                            .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withTargetUrl(sharesharepath)
                            .withMedia(new UMImage(mContext, R.mipmap.ic_launcher))
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
//                            .title_share();
                    break;

                case 2://微信
                    new ShareAction(mContext)
                            .setPlatform(SHARE_MEDIA.WEIXIN)
                            .setCallback(new OnUmShareListener())
                            .withTitle(getResources().getString(R.string.app_name))
                            .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withTargetUrl(sharesharepath)
                            .withMedia(new UMImage(mContext, R.mipmap.ic_launcher))
                            .share();
                    break;
                case 3://朋友圈
                    new ShareAction(mContext)
                            .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                            .setCallback(new OnUmShareListener())
                            .withTitle(getResources().getString(R.string.app_name))
                            .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withTargetUrl(sharesharepath)
                            .withMedia(new UMImage(mContext, R.mipmap.ic_launcher))
                            .share();
                    break;
                case 4://新浪
                    new ShareAction(mContext)
                            .setPlatform(SHARE_MEDIA.QZONE)
                            .setCallback(new OnUmShareListener())
                            .withTitle(getResources().getString(R.string.app_name))
                            .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withTargetUrl(sharesharepath)
                            .withMedia(new UMImage(mContext, R.mipmap.ic_launcher))
                            .share();
                    break;
            }
        }
    }

    // 猜你喜欢商品适配器
    private class GoodsPageChangeListener implements ViewPager.OnPageChangeListener {
        GoodsViewPagerAdapter mAdapter;

        public GoodsPageChangeListener(GoodsViewPagerAdapter mAdapter) {
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


    //友盟分享回调
    class OnUmShareListener implements UMShareListener {

        @Override
        public void onResult(SHARE_MEDIA share_media) {

            XtomToastUtil.showShortToast(mContext, "分享成功");
            tv_title_name.postDelayed(new Runnable() {
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
            tv_title_name.post(new Runnable() {
                @Override
                public void run() {
                    XtomToastUtil.showShortToast(mContext, "分享失败");
                }
            });
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            tv_title_name.post(new Runnable() {
                @Override
                public void run() {
                    XtomToastUtil.showShortToast(mContext, "分享取消");
                }
            });
        }
    }
    /* 分享相关end */
}
