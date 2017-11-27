package com.hemaapp.tyyjsc.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.OrderInfoListAdapter;
import com.hemaapp.tyyjsc.adapters.TaoCanListAdapter;
import com.hemaapp.tyyjsc.model.CartGoodsInfo;
import com.hemaapp.tyyjsc.model.GoodsBriefIntroduction;
import com.hemaapp.tyyjsc.model.OrderInfo;
import com.hemaapp.tyyjsc.model.ReplyId;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.ButtonDialog;
import com.hemaapp.tyyjsc.view.HmHelpDialog;
import com.hemaapp.tyyjsc.view.MyScrollView;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 正常订单订单详情
 */
public class ActivityOrderInfo extends BaseActivity implements View.OnClickListener {
    private final static long hourLevelValue = 60 * 60 * 1000;
    private final static long minuteLevelValue = 60 * 1000;
    private final static long secondLevelValue = 1000;
    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧文字按钮
    private MyScrollView sv = null;
    private RefreshLoadmoreLayout layout = null;
    private ArrayList<CartGoodsInfo> data = new ArrayList<>();
    private XtomListView listView = null;
    private OrderInfoListAdapter listGoodsAdapter = null;//订单详情中商品列表
    private TextView statusTV = null;//订单状态
    private LinearLayout remainTimeLayout = null;//倒计时布局
    private TextView timeTV = null;//倒计时
    private CountDownTimer countDownTimer = null;//计时器
    private long period = 0;//时间戳
    private TextView nameView = null;//收货人信息
    private TextView telView = null;
    private TextView addressView = null;
    private TextView deliverFeeView = null;//运费
    private TextView total_fee_tv = null;//总计
    private TextView orderSnView = null;//订单号
    private TextView orderTimeView = null;//下单时间
    private TextView sendorderTime = null;//发货时间
    private TextView getTime = null;//到货时间

    private RelativeLayout layout_express;
    private TextView expressNameView = null;//快递公司
    private TextView expressSNView = null;//快递单号
    private TextView tv_check_express;


    private HmHelpDialog confirmPacgageDialog = null;//确认收货对话框
    private HmHelpDialog delOrderDialog = null;//删除订单对话框
    private TextView firstBtn = null;
    private TextView secondBtn = null;
    private String id;
    private TextView send_num, send_time;
    private LinearLayout taocan_lay;
    private String type;//1.普通订单 2.套餐订单
    private TaoCanListAdapter valueMealAdapter = null;//套餐详情列表
    private XtomListView deals = null;//超值套餐
    private TextView score_fee;
    private ArrayList<GoodsBriefIntroduction> meals = null;
    private User user = null;
    private OrderInfo orderInfo = null;
    private ButtonDialog dialog = null;//代金券提示框
    //套餐相关
    private ImageView itemImgView = null;
    private TextView itemNameView = null;
    private TextView itemPriceView = null;
    private TextView itemSaleNumView = null;
    private TextView itemSpecNameView = null;
    private TextView shop_name = null;
    private TextView num;

    private TextView tv_liuyan;
    private RelativeLayout layout_scroe;
    private TextView tv_exchange; //兑换订单显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_info);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        user = BaseApplication.getInstance().getUser();
        getOrderInfo();
    }

    //获取套餐详情的商品列表
    public void getGoodList(String keytype, String id, String keyword) {
        getNetWorker().getGoodsListNoPage(keytype, id, keyword, "0");
    }

    //订单详情
    public void getOrderInfo() {
        if (user != null) {
            getNetWorker().getOrderInfo(user.getToken(), id);
        }
    }

    //提醒发货
    public void reminderOrder() {
        if (user != null && orderInfo != null) {
            getNetWorker().OrderOperation(user.getToken(), "5", orderInfo.getId());
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            sv.smoothScrollTo(0, 0);//强制ScrollView滑动到顶部
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent it) {
        if (resultCode != RESULT_OK && it == null) {
            return;
        }
        String order_goods_id = "";
        switch (requestCode) {
            default:
                getOrderInfo();
                break;
        }

    }

    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);
        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText("订单详情");
        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.INVISIBLE);
        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.GONE);
        layout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        sv = (MyScrollView) findViewById(R.id.sv);
        statusTV = (TextView) findViewById(R.id.status_tv);
        remainTimeLayout = (LinearLayout) findViewById(R.id.remain_time_ll);
        timeTV = (TextView) findViewById(R.id.time);
        score_fee = (TextView) findViewById(R.id.score_fee);
        nameView = (TextView) findViewById(R.id.name);
        telView = (TextView) findViewById(R.id.tel);
        addressView = (TextView) findViewById(R.id.address);
        deliverFeeView = (TextView) findViewById(R.id.deliver_fee);
        total_fee_tv = (TextView) findViewById(R.id.total_fee);
        orderSnView = (TextView) findViewById(R.id.order_sn);
        orderTimeView = (TextView) findViewById(R.id.order_time);
        sendorderTime = (TextView) findViewById(R.id.sendorder_time);
        getTime = (TextView) findViewById(R.id.get_time);

        layout_express = (RelativeLayout) findViewById(R.id.layout_express);
        expressNameView = (TextView) findViewById(R.id.express_name_tv);
        expressSNView = (TextView) findViewById(R.id.express_sn_tv);
        tv_check_express = (TextView) findViewById(R.id.tv_check_express);

        //商品列表
        listView = (XtomListView) findViewById(R.id.goods);
        listView.setFocusable(false);//强制ScrollView滑动到顶部
        firstBtn = (TextView) findViewById(R.id.first);
        secondBtn = (TextView) findViewById(R.id.second);
        send_num = (TextView) findViewById(R.id.send_num);
        send_time = (TextView) findViewById(R.id.send_time);
        taocan_lay = (LinearLayout) findViewById(R.id.taocan_lay);
        deals = (XtomListView) findViewById(R.id.deals);
        //套餐
        itemImgView = (ImageView) findViewById(R.id.goods_img);
        itemNameView = (TextView) findViewById(R.id.goods_name);
        itemPriceView = (TextView) findViewById(R.id.goods_price);
        itemSaleNumView = (TextView) findViewById(R.id.goods_num);
        itemSpecNameView = (TextView) findViewById(R.id.goods_spec);
        shop_name = (TextView) findViewById(R.id.shop_name);
        num = (TextView) findViewById(R.id.num);
        if ("2".equals(type)) {
            taocan_lay.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else{
            listView.setVisibility(View.VISIBLE);
            taocan_lay.setVisibility(View.GONE);
        }

        tv_liuyan = (TextView) findViewById(R.id.tv_liuyan);
        layout_scroe = (RelativeLayout) findViewById(R.id.layout_scroe);
        tv_exchange = (TextView) findViewById(R.id.tv_exchange);
    }

    public void initTaocan() {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(orderInfo.getPackageimgurl(), itemImgView, BaseUtil.displayImageOption());
        itemNameView.setText(orderInfo.getPackagename());
        itemPriceView.setText("¥" + orderInfo.getPackageprice());
        itemSaleNumView.setText("X" + orderInfo.getGoodsnum());
        send_num.setText("发货次数:" + orderInfo.getPackagefhimes());
        send_time.setText("发货间隔：" + orderInfo.getPackageshipdays());
        num.setText("共计" + orderInfo.getGoodsnum() + "商品/合计" + orderInfo.getGoodsprice());
        shop_name.setText(orderInfo.getShopname());

        //itemSpecNameView.setText(orderInfo.get);
    }

    @Override
    protected void getExras() {
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
    }

    @Override
    protected void setListener() {
        hmBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        layout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getOrderInfo();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {

            }
        });
        layout.setLoadmoreable(false);
        tv_check_express.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://www.kuaidi100.com/chaxun?com="
                        + orderInfo.getDeliveryname() +"@nu=" + orderInfo.getDeliverynum());
                intent.setData(content_url);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ORDER_GET:
            case REMOVE:
            case ORDER_OPE:
            case REMINDER:
                showProgressDialog(R.string.hm_hlxs_txt_59);
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ORDER_GET:
            case REMOVE:
            case ORDER_OPE:
            case REMINDER:
                cancelProgressDialog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        Intent intent = null;
        switch (information) {
            case ORDER_GET:
                layout.refreshSuccess();
                HemaArrayResult<OrderInfo> result = (HemaArrayResult<OrderInfo>) hemaBaseResult;
                orderInfo = result.getObjects().get(0);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                if ("2".equals(type)) {
                    getGoodList("5", orderInfo.getPackageid(), "");
                }
                freshData();
                break;
            case REMOVE://删除订单
                showTextDialog(getString(R.string.hm_hlxs_txt_60));
                intent = new Intent();
                intent.setAction(BaseConfig.BROADCAST_ORDER);
                orderInfo.setStatus("1".equals(hemaNetTask.getParams().get("keytype")) ? "7" : "6");
                intent.putExtra("order", orderInfo);
                sendBroadcast(intent);
                hmBarNameView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 500);
                break;
            case REMINDER:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            case ORDER_OPE://确认
                String type = hemaNetTask.getParams().get("keytype");
                switch (type) {
                    case "1":
                        //确认收货后刷新当前订单详情，并发送通知告知列表从待收货中移除该订单；以及修改噶该订单在全部订单中的状态
                        intent = new Intent();
                        intent.setAction(BaseConfig.BROADCAST_ORDER);
                        if("5".equals(orderInfo.getKeytype()))
                            orderInfo.setStatus("5");
                        else
                            orderInfo.setStatus("4");

                        intent.putExtra("order", orderInfo);
                        sendBroadcast(intent);
                        freshData();
                        if(!"5".equals(orderInfo.getKeytype())){
                            HemaArrayResult<ReplyId> voucherResult = (HemaArrayResult<ReplyId>) hemaBaseResult;
                            ArrayList<ReplyId> vourchers = voucherResult.getObjects();
                            if (dialog == null) {
                                dialog = new ButtonDialog(mContext);
                            }
                            dialog.setRes(R.mipmap.ic_launcher);
                            dialog.setValueText("已返积分,请查看列表");
                            dialog.setTextInVisivle();
                            dialog.setmTextValueSize();
                            dialog.setListener(new ButtonDialog.onConfrimListener() {
                                @Override
                                public void onConfirm() {
                                    dialog.cancel();
                                }
                            });
                            dialog.show();
                        }else{
                            showTextDialog("确认收货成功！");
                        }
                        break;
                    case "2":
                        showTextDialog(getString(R.string.hm_hlxs_txt_60));
                        intent = new Intent();
                        intent.setAction(BaseConfig.BROADCAST_ORDER);
                        orderInfo.setStatus("1".equals(hemaNetTask.getParams().get("keytype")) ? "7" : "6");
                        intent.putExtra("order", orderInfo);
                        sendBroadcast(intent);
                        hmBarNameView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 500);
                        break;

                    default:
                        showTextDialog(hemaBaseResult.getMsg());
                        break;
                }
                break;
            case GOODS_LIST:
                String keytype1 = hemaNetTask.getParams().get("keytype");
                HemaArrayResult<GoodsBriefIntroduction> likeResult1 = (HemaArrayResult<GoodsBriefIntroduction>) hemaBaseResult;
                meals = likeResult1.getObjects();
                getTaoCanList();
                break;
            case ALLCOUPON_REMOVE:
                showTextDialog("支付成功");
                intent = new Intent();
                intent.setAction(BaseConfig.BROADCAST_ORDER);
                orderInfo.setStatus("1".equals(hemaNetTask.getParams().get("keytype")) ? "7" : "6");
                intent.putExtra("order", orderInfo);
                sendBroadcast(intent);
                hmBarNameView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 500);
                break;
            default:
                break;
        }
    }

    //套餐列表
    public void getTaoCanList() {
        if (valueMealAdapter == null) {
            valueMealAdapter = new TaoCanListAdapter(ActivityOrderInfo.this, meals);
            valueMealAdapter.setEmptyString("暂无套餐");
            deals.setAdapter(valueMealAdapter);
        } else {
            valueMealAdapter.setData(meals);
            valueMealAdapter.setEmptyString("暂无套餐");
            valueMealAdapter.notifyDataSetChanged();
        }
        BaseUtil.setListViewHeightBasedOnChildren(deals);
    }

    /**
     * 1:待支付
     * <p>
     * 2:已支付(待发货)
     * <p>
     * 3:待收货
     * <p>
     * 4:待评价
     * <p>
     * 5:已完成
     * <p>
     * 6:已关闭
     */
    public void freshData() {
        if (orderInfo != null && !isNull(orderInfo.getStatus().toString())) {
            switch (Integer.parseInt(orderInfo.getStatus().toString().trim())) {
                case 1://待支付
                    statusTV.setVisibility(View.VISIBLE);
                    //statusTV.setText("等待买家付款");
                    statusTV.setText("待付款");
                    statusTV.setGravity(Gravity.CENTER);
                    firstBtn.setVisibility(View.VISIBLE);
                    firstBtn.setText("取消订单");
                    firstBtn.setOnClickListener(this);
                    secondBtn.setVisibility(View.VISIBLE);
                    secondBtn.setTag(R.id.type, "0");
                    secondBtn.setText("去 支 付");
                    secondBtn.setOnClickListener(this);
                    layout_express.setVisibility(View.GONE);
                    break;
                case 2://已支付(待发货)
                    statusTV.setVisibility(View.VISIBLE);
                    statusTV.setText("待发货");
                    if("5".equals(orderInfo.getKeytype()))
                        firstBtn.setVisibility(View.GONE);
                    else
                        firstBtn.setVisibility(View.VISIBLE);

                    firstBtn.setText("取消订单");
                    firstBtn.setTag(R.id.type, "2");
                    firstBtn.setOnClickListener(this);
                    secondBtn.setVisibility(View.VISIBLE);
                    secondBtn.setTag(R.id.type, "1");
                    secondBtn.setText("提醒发货");
                    secondBtn.setOnClickListener(this);
                    sendorderTime.setVisibility(View.GONE);
                    getTime.setVisibility(View.GONE);
                    layout_express.setVisibility(View.GONE);
                    break;
                case 3://待收货
                    statusTV.setVisibility(View.VISIBLE);
                    remainTimeLayout.setVisibility(View.GONE);
                    statusTV.setText("待收货");
                    firstBtn.setVisibility(View.GONE);
                    secondBtn.setVisibility(View.VISIBLE);
                    secondBtn.setTag(R.id.type, "2");
                    secondBtn.setText("确认收货");
                    secondBtn.setOnClickListener(this);
                    sendorderTime.setVisibility(View.VISIBLE);
                    getTime.setVisibility(View.GONE);
                    layout_express.setVisibility(View.VISIBLE);
                    sendorderTime.setText("发货时间：" + orderInfo.getFhtime());
                    expressNameView.setText("快递名称：" + orderInfo.getDeliveryname());
                    expressSNView.setText("快递单号：" + orderInfo.getDeliverynum());
                    break;
                case 4://待评价
                    if ("1".equals(this.type)) {
                        firstBtn.setVisibility(View.GONE);
                        secondBtn.setVisibility(View.VISIBLE);
                        secondBtn.setTag(R.id.type, "3");
                        secondBtn.setText("删除订单");
                        secondBtn.setOnClickListener(this);
                    } else if ("2".equals(this.type)) {
                        firstBtn.setVisibility(View.VISIBLE);
                        firstBtn.setTag(R.id.type, "4");
                        firstBtn.setText("删除订单");
                        firstBtn.setOnClickListener(this);
                        secondBtn.setVisibility(View.VISIBLE);
                        secondBtn.setTag(R.id.type, "4");
                        secondBtn.setText("去评价");
                        secondBtn.setOnClickListener(this);
                    }
                    statusTV.setVisibility(View.VISIBLE);
                    remainTimeLayout.setVisibility(View.GONE);
                    statusTV.setText("待评价");
                    sendorderTime.setVisibility(View.VISIBLE);
                    getTime.setVisibility(View.VISIBLE);
                    sendorderTime.setText("发货时间：" + orderInfo.getFhtime());
                    getTime.setText("收货时间" + orderInfo.getRegdate());
                    layout_express.setVisibility(View.VISIBLE);
                    expressNameView.setText("快递名称：" + orderInfo.getDeliveryname());
                    expressSNView.setText("快递单号：" + orderInfo.getDeliverynum());
                    break;
                case 5://已完成
                    statusTV.setVisibility(View.VISIBLE);
                    remainTimeLayout.setVisibility(View.GONE);
                    statusTV.setText("交易完成");
                    firstBtn.setVisibility(View.GONE);
                    secondBtn.setVisibility(View.VISIBLE);
                    secondBtn.setTag(R.id.type, "3");
                    secondBtn.setText("删除订单");
                    secondBtn.setOnClickListener(this);
                    sendorderTime.setVisibility(View.VISIBLE);
                    getTime.setVisibility(View.VISIBLE);
                    sendorderTime.setText("发货时间：" + orderInfo.getFhtime());
                    getTime.setText("收货时间" + orderInfo.getRegdate());
                    layout_express.setVisibility(View.VISIBLE);
                    expressNameView.setText("快递名称：" + orderInfo.getDeliveryname());
                    expressSNView.setText("快递单号：" + orderInfo.getDeliverynum());
                    break;
                case 6://已关闭
                    statusTV.setVisibility(View.VISIBLE);
                    remainTimeLayout.setVisibility(View.GONE);
                    statusTV.setText("交易关闭!");
                    firstBtn.setVisibility(View.GONE);
                    secondBtn.setVisibility(View.VISIBLE);
                    secondBtn.setTag(R.id.type, "3");
                    secondBtn.setText("删除订单");
                    secondBtn.setOnClickListener(this);
                    sendorderTime.setVisibility(View.VISIBLE);
                    getTime.setVisibility(View.VISIBLE);
                    sendorderTime.setText("发货时间：" + orderInfo.getFhtime());
                    getTime.setText("收货时间" + orderInfo.getRegdate());
                    layout_express.setVisibility(View.VISIBLE);
                    expressNameView.setText("快递名称：" + orderInfo.getDeliveryname());
                    expressSNView.setText("快递单号：" + orderInfo.getDeliverynum());
                    break;
                default:
                    break;
            }

            if("5".equals(orderInfo.getKeytype())) {
                tv_exchange.setVisibility(View.VISIBLE);
                layout_scroe.setVisibility(View.GONE);
            } else {
                tv_exchange.setVisibility(View.GONE);
                layout_scroe.setVisibility(View.VISIBLE);
            }

            nameView.setText("收件人: " + orderInfo.getClientname());
            telView.setText("电话: " + orderInfo.getTel());
            addressView.setText(orderInfo.getAddress());
            tv_liuyan.setText("买家留言："+orderInfo.getDemo());
            score_fee.setText("￥" + orderInfo.getScoremoney());
            if ("2".equals(type)) {
                initTaocan();
            } else {
                data = orderInfo.getGoods();
                freshOrderData();
            }
            deliverFeeView.setText("¥" + orderInfo.getShipment());
            int buycount = 0;
            //计算件数
            if (data == null || data.size() == 0) {
                buycount = 0;
            } else {
                buycount = data.size();
            }
            if("5".equals(orderInfo.getKeytype()))
                total_fee_tv.setText(orderInfo.getAllcoupon()+"点券");
            else
                total_fee_tv.setText("￥" + orderInfo.getMoney());
            orderSnView.setText("订单号：" + orderInfo.getOrdernum());
            orderTimeView.setText("付款时间: " + orderInfo.getOrdertime());
//            if (!isNull(orderInfo.getDeliverynum())) {
//                expressNameView.setVisibility(View.VISIBLE);
//                expressNameView.setText("快递公司：" + orderInfo.getDeliveryname());
//                expressSNView.setVisibility(View.VISIBLE);
//                expressSNView.setText("快递单号：" + orderInfo.getDeliverynum());
//            } else {
//                expressNameView.setVisibility(View.GONE);
//                expressSNView.setVisibility(View.GONE);
//            }
        }
    }

    //刷新订单商品列表适配器
    public void freshOrderData() {
        if (listGoodsAdapter == null) {
            listGoodsAdapter = new OrderInfoListAdapter(this, orderInfo);
            listGoodsAdapter.setIsResturn(orderInfo.getStatus().toString().trim());
            listGoodsAdapter.setOrder_id(orderInfo.getId());
            listGoodsAdapter.setStatu(orderInfo.getStatus());
            listGoodsAdapter.setFromtype(type);
            listView.setAdapter(listGoodsAdapter);
        } else {
            listGoodsAdapter.setIsResturn(orderInfo.getStatus().toString().trim());
            listGoodsAdapter.setOrder_id(orderInfo.getId());
            listGoodsAdapter.setData(data);
            listGoodsAdapter.setStatu(orderInfo.getStatus());
            listGoodsAdapter.setFromtype(type);
            listGoodsAdapter.notifyDataSetChanged();
        }
        BaseUtil.setListViewHeightBasedOnChildren(listView);
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ORDER_GET:
                layout.refreshFailed();
                showTextDialog(hemaBaseResult.getMsg());
                break;
            case REMOVE:
            case REMINDER:
            case ORDER_OPE:
            case ALLCOUPON_REMOVE:
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
            case ORDER_GET:
                layout.refreshFailed();
                break;
            case REMOVE:
            case REMINDER:
            case ORDER_OPE:
            case ALLCOUPON_REMOVE:
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.first:
                final String type1 = v.getTag(R.id.type).toString();
                if (delOrderDialog == null) {
                    delOrderDialog = new HmHelpDialog(ActivityOrderInfo.this, 0);
                }
                delOrderDialog.setLeftButtonText("取消");
                delOrderDialog.setRightButtonText("确定");
                delOrderDialog.setTitleName("确定取消订单?");
                delOrderDialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
                    @Override
                    public void onCancel(int type) {
                        delOrderDialog.cancel();
                    }

                    @Override
                    public void onConfirm(String msg) {
                        delOrderDialog.cancel();
                        if ("4".equals(type1)) {
                            getNetWorker().OrderOperation(user.getToken(), "2", orderInfo.getId());
                        } else {
                            if ("2".equals(orderInfo.getStatus()))
                                getNetWorker().OrderOperation(user.getToken(), "3", orderInfo.getId());
                            else
                                getNetWorker().OrderOperation(user.getToken(), "4", orderInfo.getId());
                        }

                    }
                });
                delOrderDialog.show();
                break;
            case R.id.second:
                String tagtype = v.getTag(R.id.type).toString();
                if ("0".equals(tagtype)) {
                    if(orderInfo.getKeytype().equals("5")){
                        showPwdPop();
                    }else{
                        intent = new Intent(ActivityOrderInfo.this, ActivityPay.class);
                        intent.putExtra("keytype", orderInfo.getKeytype());//购买商品
                        intent.putExtra("money", orderInfo.getMoney());//支付金额
                        intent.putExtra("order_id", orderInfo.getId());//订单id
                        intent.putExtra("from", "3");//订单详情
                        startActivityForResult(intent, 1002);
                    }
                } else if ("1".equals(tagtype)) {
                    if (delOrderDialog == null) {
                        delOrderDialog = new HmHelpDialog(mContext, 0);
                    }
                    delOrderDialog.setLeftButtonText("取消");
                    delOrderDialog.setRightButtonText("确定");
                    delOrderDialog.setTitleName("确定提醒卖家发货?");
                    delOrderDialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
                        @Override
                        public void onCancel(int type) {
                            delOrderDialog.cancel();
                        }

                        @Override
                        public void onConfirm(String msg) {
                            delOrderDialog.cancel();
                            reminderOrder();
                        }
                    });
                    delOrderDialog.show();
                } else if ("2".equals(tagtype)) {
                    confirmPacgageDialog = new HmHelpDialog(ActivityOrderInfo.this, 0);
                    confirmPacgageDialog.setLeftButtonText("取消");
                    confirmPacgageDialog.setRightButtonText("确定");
                    confirmPacgageDialog.setTitleName("确定收货?");
                    confirmPacgageDialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
                        @Override
                        public void onCancel(int type) {
                            confirmPacgageDialog.cancel();
                        }

                        @Override
                        public void onConfirm(String msg) {
                            confirmPacgageDialog.cancel();
                            if (user != null && orderInfo != null) {
                                getNetWorker().OrderOperation(user.getToken(), "1", orderInfo.getId());
                            }
                        }
                    });
                    confirmPacgageDialog.show();
                } else if ("3".equals(tagtype)) {
                    if (delOrderDialog == null) {
                        delOrderDialog = new HmHelpDialog(ActivityOrderInfo.this, 0);
                    }
                    delOrderDialog.setLeftButtonText("取消");
                    delOrderDialog.setRightButtonText("确定");
                    delOrderDialog.setTitleName("确定删除订单?");
                    delOrderDialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
                        @Override
                        public void onCancel(int type) {
                            delOrderDialog.cancel();
                        }

                        @Override
                        public void onConfirm(String msg) {
                            delOrderDialog.cancel();
                            if (user != null && orderInfo != null) {
                                getNetWorker().OrderOperation(user.getToken(), "2", orderInfo.getId());
                            }
                        }
                    });
                    delOrderDialog.show();
                } else if ("4".equals(tagtype)) { //去评论
                    Intent intent1 = new Intent(mContext, ActivityOrderComment.class);
                    CartGoodsInfo cartGoodsInfo = new CartGoodsInfo(orderInfo.getPackageid(), orderInfo.getPackagename(), orderInfo.getPackageimgurl(), orderInfo.getPackageprice(), "1");
                    intent1.putExtra("goods", cartGoodsInfo);
                    intent1.putExtra("orderid", orderInfo.getId());
                    intent1.putExtra("type", "2");
                    startActivity(intent1);
                }
                break;
            default:
                break;
        }
    }

    private HmHelpDialog dialog1 = null;//密码提示框
    private void showPwdPop(){
        if (dialog1 == null) {
            dialog1 = new HmHelpDialog(mContext, 2);
        }
        dialog1.setPayTxt("支付");
        dialog1.setTitleName("请输入支付密码");
        dialog1.setLeftButtonText("取消");
        dialog1.setRightButtonText("确定");
        dialog1.setListener(new HmHelpDialog.onPwdListener() {
            @Override
            public void onSetPwd() {//设置密码
                Intent intent = new Intent(mContext, ActivitySetPayPwd.class);
                mContext.startActivity(intent);
            }
            @Override
            public void onGetPwd() {
                Intent intent = new Intent(mContext, ActivitySetPayPwd.class);
                mContext.startActivity(intent);
            }
        });

        dialog1.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
            @Override
            public void onCancel(int type) {
                dialog1.cancel();
            }

            @Override
            public void onConfirm(String msg) {
                dialog1.cancel();
                String password = dialog1.getPassword();
                User user = BaseApplication.getInstance().getUser();
                dialog1.clearPwdET();
                if (user.getPaypassword() == null || "".equals(user.getPaypassword())) {
                    XtomToastUtil.showShortToast( mContext, "请先设置支付密码");
                    return;
                }
                if (isNull(password)) {
                    XtomToastUtil.showShortToast( mContext, "请输入支付密码");
                    return;
                }
                getNetWorker().allcouponRemove(user.getToken(), id, password);
            }
        });
        dialog1.show();
    }

    private void getDifference(long period, TextView timeView) {//根据毫秒差计算时间差
        String result = null;
        /*******计算出时间差中的时、分、秒*******/
        int hour = getHour(period);
        int minute = getMinute(period - hour * hourLevelValue);
        int second = getSecond(period - hour * hourLevelValue - minute * minuteLevelValue);
        result = formatTimeStr(hour) + ":" + formatTimeStr(minute) + ":" + formatTimeStr(second);

        SpannableStringBuilder style = new SpannableStringBuilder(result);
        //时分秒黑色块
        style.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.time_block_bg)), 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        style.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.time_block_bg)), 3, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        style.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.time_block_bg)), 6, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //时分秒前景色
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 3, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 6, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //设置冒号颜色
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.time_block_bg)), 2, 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.time_block_bg)), 5, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        timeView.setText(style);
        timeView.postInvalidate();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
