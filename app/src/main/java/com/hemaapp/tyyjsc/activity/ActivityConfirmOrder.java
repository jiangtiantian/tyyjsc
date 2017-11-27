package com.hemaapp.tyyjsc.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.ListGoodsAdapter;
import com.hemaapp.tyyjsc.model.Address;
import com.hemaapp.tyyjsc.model.CartGoodsInfo;
import com.hemaapp.tyyjsc.model.IdInfo;
import com.hemaapp.tyyjsc.model.MemberorderAdd;
import com.hemaapp.tyyjsc.model.ReplyId;
import com.hemaapp.tyyjsc.model.SignValueInfo;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.model.VourcherInfo;
import com.hemaapp.tyyjsc.view.HmHelpDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;

import static com.hemaapp.tyyjsc.R.id.address;
import static com.hemaapp.tyyjsc.R.id.all;
import static com.hemaapp.tyyjsc.R.id.checkbox;
import static com.hemaapp.tyyjsc.R.id.score;
import static com.hemaapp.tyyjsc.R.id.sel;
import static com.hemaapp.tyyjsc.R.id.tip;
import static com.hemaapp.tyyjsc.R.id.tv_coupon;

/**
 * 确认订单
 */
public class ActivityConfirmOrder extends BaseActivity implements View.OnClickListener {
    // 修改地址
    public static int ADDRESS = 1001;
    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧微文字按钮
    private ArrayList<CartGoodsInfo> data = null;
    private XtomListView listView = null;
    private ListGoodsAdapter listGoodsAdapter = null;
    private Boolean justbuy = false;
    private View priceLine = null;//价格和商品间的分割线
    private RotateAnimation up = null;// 顺时针旋转180
    private RotateAnimation down = null; // 逆时针旋转180
    private RelativeLayout selectAddressLayout = null;//选择收货地址
    private TextView addressNameView = null;
    private TextView addressTelView = null;
    private TextView addressView = null;
    private TextView tipView = null;//提示有无设置收货地址
    private Address addressInfo = null;//收货信息
    private ScrollView myScrollView = null;
    //价格统计布局
    private TextView total_feeView = null;//总价
    private CheckBox deliverFeeView = null;//运费
    private int buycount = 0;//商品总数量
    private double money = 0.00;//商品总金额
    private String actual_pay_money = "";//实际支付金额
    private double profit = 0.00;//优惠金额
    private double expressfee = 0.00;//运费
    private int goods_score = 0;//商品所需积分
    private int goods_score_ex_money = 0;//商品总金额 / radio最大可兑换积分
    private String scoreString;//用户拥有积分
    private int pay_score = 0;//支付积分
    private String specs_id;//商品组合属性
    private HashMap<String, SignValueInfo> hashExpressFee = new HashMap<>();//各个店铺距离收货地址的运费
    private TextView goodsScoreView = null;//实际积分
    private TextView scoreView = null;//当前可用积分
    private boolean isScorePay = false;
    private double radio = 0.001; //1000积分抵现一元
    private RelativeLayout voucherLayout = null;//代金券
    private VourcherInfo vourcherInfo = null;//代金券信息
    private TextView ticketView = null;//代金券金额
    private TextView payBtn = null;//立即支付
    private User user = null;//个人信息
    private String cart_id = "";//购物车id拼接串
    private StringBuffer cartIdBuffer = new StringBuffer();
    private String goods_id = "";//商品id拼接
    private String address_id;//地址id
    private String keytype = "";//1：普通商品 2：抢购商品
    private String keyid = "";//当keytype == 0 || 3
    private String order_id = "";//订单号
    private int cout = 0;//判断获取运费的是否请求完
    private int getcount = 0;//获取请求完成后的运费个数
//    private StringBuffer memo = new StringBuffer();//备注
    private String memo;

    private RelativeLayout float_middle;
    private RelativeLayout relativeLayout_jifen;
    private Button btn_zhifu;
    private TextView tv_convert_number;
    private CheckBox checkbox_dianquan;
    private RelativeLayout linerLayout_daijinquan;
    private boolean statue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_confirm_order);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        user = BaseApplication.getInstance().getUser();
        // 加载动画
        up = (RotateAnimation) AnimationUtils.loadAnimation(mContext,
                R.anim.charge_rotate_up);
        down = (RotateAnimation) AnimationUtils.loadAnimation(mContext,
                R.anim.charge_rotate_down);
        //获取积分
        if (data.size() != 1)//后台不修改，一件商品直接抓取详情的积分
            getNetWorker().score_get(user.getToken(), cart_id);
        else {
            scoreString = data.get(0).getScore();
            goodsScoreView.setText(getString(R.string.hm_hlxs_txt_182) + scoreString + ")");
        }
        getAddressDef();
        freshData();
    }

    // 获取默认收货地址
    public void getAddressDef() {
        getNetWorker().getAddressList(user.getToken());
    }

    //获取运费
    public void getExpressFee(String address, String shopid, String allweight) {

        getNetWorker().getExpressFee(user.getToken(), address, shopid, allweight);
    }

    //获取所有店铺的运费
    public void getAllExptessFee(String address) {
        hashExpressFee.clear();//运费的初始化
        getcount = 0;//计数器初始化
        String shop_id = data.get(0).getShopid();
        double allweight = 0.0;
        for(int i = 0; i < data.size(); i++){
            if(shop_id.equals(data.get(i).getShopid()))
                allweight = allweight + Double.parseDouble(isNull(data.get(i).getAllweight())?"0.0":data.get(i).getAllweight());
        }
        getExpressFee(address, data.get(0).getShopid(), BaseUtil.get2double(allweight));
        for (int i = 0; i < data.size(); i++) {
            if (i < data.size() - 1) {
                String id_0 = data.get(i).getShopid();
                String id_1 = data.get(i + 1).getShopid();
                if (!id_0.equals(id_1)) {
                    double allweight1 = 0.0;
                    for(int j = 0; j < data.size(); j++){
                        if(data.get(j).getShopid().equals(id_1))
                            allweight1 = allweight1 + Double.parseDouble(isNull(data.get(i).getAllweight())?"0.0":data.get(i).getAllweight());
                    }
                    getExpressFee(address, data.get(i + 1).getShopid(), BaseUtil.get2double(allweight1));
                }
            }
        }
    }

    //用户信息
    public void getClientInfo() {
        getNetWorker().clientGet(user.getToken(), user.getId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1001:// 修改地址
                addressInfo = (Address) data.getSerializableExtra("key");
                if (addressNameView.getVisibility() == View.INVISIBLE) {
                    showAddressView();
                }
                addressNameView.setText("收件人: " + addressInfo.getName());
                addressTelView.setText("电话: " + addressInfo.getTel());
                addressView.setText(addressInfo.getAddress());
                address_id = addressInfo.getId();
                getAllExptessFee(addressInfo.getProvince());
                break;
            case 1000:// 选择优惠券
                vourcherInfo = (VourcherInfo) data.getSerializableExtra("key");
                if (vourcherInfo != null) {
                    ticketView.setText("-" + vourcherInfo.getMoney() + "元");
                    profit = Double.parseDouble(vourcherInfo.getMoney());
                } else {//取消优惠券
                    ticketView.setText("请选择");
                    profit = 0;
                }
                if (!isNull(address_id))
                    chargeNumAndTotalMoney();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void freshData() {
        if (listGoodsAdapter == null) {
            listGoodsAdapter = new ListGoodsAdapter(mContext, data, hashExpressFee);
            listGoodsAdapter.setCanClick(true);
            listGoodsAdapter.setKeytype(keytype);
            listView.setAdapter(listGoodsAdapter);
        } else {
            listGoodsAdapter.setData(data, hashExpressFee);
            listGoodsAdapter.setKeytype(keytype);
            listGoodsAdapter.notifyDataSetChanged();
        }
        BaseUtil.setListViewHeightBasedOnChildren(listView);
        myScrollView.smoothScrollTo(0, 0);//强制ScrollView滑动到顶部
    }

    //商品总金额
    public void chargeNumAndTotalMoney() {
        buycount = 0;
        goods_score = 0;
        money = 0.00;
        double tempMoney = 0.00;
        double byprice;

        Iterator iter = listGoodsAdapter.goodfee.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            tempMoney = Double.parseDouble((String) entry.getValue());
            double sendfee = Double.parseDouble(isNull(hashExpressFee.get(key).getShipment()) ? "0.00" : hashExpressFee.get(key).getShipment());
            double start_price = 0.00;
            int position = 0;
            if (data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getShopid().equals(key)) {
                        position = i;
                        start_price = Double.parseDouble(isNull(data.get(i).getByprice()) ? "0.00" : data.get(i).getByprice());
                        break;
                    }
                }
            }

            if (XtomBaseUtil.isNull(data.get(position).getByprice())) {
                money = money + tempMoney + sendfee;
            } else {
                if (tempMoney < start_price)
                    money = money + tempMoney + sendfee;
                else
                    money = money + tempMoney;
            }
        }

        if (deliverFeeView.isChecked()) {
            Double scorefee = Double.parseDouble(isNull(scoreString) ?
                    "0.00" : scoreString) * Double.parseDouble(isNull(user.getDxmoney()) ? "0.00" : user.getDxmoney());
            actual_pay_money = BaseUtil.transData((Double.parseDouble(BaseUtil.transData(money - profit - scorefee)) < 0 ?
                    0 : Double.parseDouble(BaseUtil.transData(money - profit - scorefee))));
        } else
            actual_pay_money = BaseUtil.transData((Double.parseDouble(BaseUtil.transData(money - profit)) < 0 ?
                    0 : Double.parseDouble(BaseUtil.transData(money - profit))));
        total_feeView.setText(getString(R.string.hm_hlxs_txt_189) + actual_pay_money);

    }

    @Override
    protected void findView() {
        //标题
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);
        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText(R.string.hm_hlxs_txt_190);
        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.GONE);
        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.INVISIBLE);
        //商品列表
        listView = (XtomListView) findViewById(R.id.goods);
        priceLine = findViewById(R.id.price_line);
        //地址
        selectAddressLayout = (RelativeLayout) findViewById(R.id.selectaddress);
        addressNameView = (TextView) findViewById(R.id.name);
        addressTelView = (TextView) findViewById(R.id.tel);
        addressView = (TextView) findViewById(address);
        tipView = (TextView) findViewById(tip);
        //统计价格
        myScrollView = (ScrollView) findViewById(R.id.sv);
        total_feeView = (TextView) findViewById(R.id.total_fee);
        //运费
        deliverFeeView = (CheckBox) findViewById(R.id.deliver_fee);
        //代金券
        voucherLayout = (RelativeLayout) findViewById(R.id.voucher);
        ticketView = (TextView) findViewById(R.id.ticket);
        //积分
        goodsScoreView = (TextView) findViewById(score);
        payBtn = (TextView) findViewById(R.id.pay);

        float_middle = (RelativeLayout) findViewById(R.id.float_middle);
        relativeLayout_jifen = (RelativeLayout) findViewById(R.id.relativeLayout_jifen);
        btn_zhifu = (Button) findViewById(R.id.btn_zhifu);//点券支付按钮
        tv_convert_number = (TextView) findViewById(R.id.tv_convert_number);  //可用总的代金券
        checkbox_dianquan = (CheckBox) findViewById(R.id.checkbox_dianquan);  //点券支付的checkbox
        linerLayout_daijinquan = (RelativeLayout) findViewById(R.id.linerLayout_daijinquan);

        if ("5".equals(keytype)) {   //监听
            float_middle.setVisibility(View.GONE);
            relativeLayout_jifen.setVisibility(View.GONE);
            voucherLayout.setVisibility(View.GONE);
        } else {
            float_middle.setVisibility(View.VISIBLE);
            relativeLayout_jifen.setVisibility(View.VISIBLE);
            voucherLayout.setVisibility(View.VISIBLE);
            btn_zhifu.setVisibility(View.GONE);
            linerLayout_daijinquan.setVisibility(View.GONE);
        }
    }

    @Override
    protected void getExras() {
        keytype = getIntent().getStringExtra("keytype");

        data = (ArrayList<CartGoodsInfo>) getIntent().getSerializableExtra("data");
        justbuy = getIntent().getBooleanExtra("justbuy", false);
        keytype = isNull(keytype) ? "1" : keytype;
        int key = Integer.parseInt(keytype);
        if (key > 4) {
            keytype = "5";
        }
        cout = getIntent().getIntExtra("count", 1);
        goods_id = "";
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                cartIdBuffer.append(data.get(i).getId() + ",");
                goods_id += data.get(i).getKeyid() + ",";
            }
        }
        cart_id = cartIdBuffer.substring(0, cartIdBuffer.lastIndexOf(","));//开始处的索引（包括）； 结束处的索引（不包括）
        //goods_id获取运费时用到
        goods_id = goods_id.substring(0, goods_id.lastIndexOf(","));//开始处的索引（包括）； 结束处的索引（不包括）

        if ("0".equals(keytype) || "3".equals(keytype)) {//限时抢购立即购买；普通商品立即购买
            keyid = cart_id;
            specs_id = data.get(0).getPropertyid();
            log_e(specs_id);
        } else if ("5".equals(keytype))
            specs_id = data.get(0).getPropertyid();
    }


    @Override
    protected void setListener() {
        hmBackBtn.setOnClickListener(this);
        selectAddressLayout.setOnClickListener(this);
        relativeLayout_jifen.setOnClickListener(this);

        voucherLayout.setOnClickListener(this);
        goodsScoreView.setOnClickListener(this);
        payBtn.setOnClickListener(this);

        btn_zhifu.setOnClickListener(this);
        checkbox_dianquan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    statue = true;
                } else {
                    statue = false;
                }
            }
        });

        deliverFeeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vourcherInfo == null) {
                    if (deliverFeeView.isChecked()) {
                        statue = true;
                        isScorePay = true;
                    }else{
                        statue = false;
                        isScorePay = false;
                        chargeNumAndTotalMoney();
                    }
                    if (!isNull(address_id))
                        chargeNumAndTotalMoney();
                } else {
                    deliverFeeView.setChecked(false);
                    XtomToastUtil.showShortToast(mContext, getString(R.string.hm_hlxs_txt_185));
                    return;
                }
            }
        });
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case ADDRESS_LIST:
                showProgressDialog(R.string.hm_hlxs_txt_59);
            case GOODS_EXPRESSFEE_GET:
            case CLIENT_GET:
            case ORDER_ADD:
            case PACKAGEORDER_ADD:
            case PROPORTION_GET:
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
            case ADDRESS_LIST:
                cancelProgressDialog();
            case GOODS_EXPRESSFEE_GET:
            case CLIENT_GET:
            case ORDER_ADD:
            case PACKAGEORDER_ADD:
            case PROPORTION_GET:
                break;
            default:
                break;
        }
    }

    //初始化积分
    public void initScoreExPanel() {
        chargeNumAndTotalMoney();
        goodsScoreView.setText(getString(R.string.hm_hlxs_txt_182) + scoreString + ")");
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        Intent intent = null;
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case ADDRESS_LIST:
                @SuppressWarnings("unchecked")
                HemaArrayResult<Address> resultAddress = (HemaArrayResult<Address>) hemaBaseResult;
                if (resultAddress.getObjects() != null
                        && resultAddress.getObjects().size() > 0) {
                    ArrayList<Address> addresses = resultAddress.getObjects();
                    // 遍历查找默认收货地址
                    for (Address address : addresses) {
                        if ("1".equals(address.getRec())) {// 是默认收货地址
                            addressInfo = address;
                            break;
                        }
                    }
                } else {
                    addressInfo = null;
                }
                getClientInfo();
                hiddenAddressView();
                break;
            case SCORE_GET:
                HemaArrayResult<ReplyId> Scoreresult = (HemaArrayResult<ReplyId>) hemaBaseResult;
                ReplyId ids = Scoreresult.getObjects().get(0);
                scoreString = isNull(ids.getScore()) ? "0" : ids.getScore();
                break;
            case GOODS_EXPRESSFEE_GET:
                getcount++;
                String shopid = hemaNetTask.getParams().get("shopid");
                HemaArrayResult<SignValueInfo> result = (HemaArrayResult<SignValueInfo>) hemaBaseResult;

                SignValueInfo fees = result.getObjects().get(0);
                if ("5".equals(keytype)) {
                    fees.setShipment("0");
                }
                hashExpressFee.put(shopid, fees);
                if (cout == getcount) {
                    freshData();
                    initScoreExPanel();
                    chargeNumAndTotalMoney();
                }
                break;
            case CLIENT_GET:
                HemaArrayResult<User> infoResult = (HemaArrayResult<User>) hemaBaseResult;
                user = infoResult.getObjects().get(0);
                tv_convert_number.setText("(可用点券"+user.getPointcoupon()+")");
                BaseApplication.getInstance().setUser(user);
                // getProportion();
                break;
            case PROPORTION_GET:
                HemaArrayResult<SignValueInfo> resultPro = (HemaArrayResult<SignValueInfo>) hemaBaseResult;
                ArrayList<SignValueInfo> proration = resultPro.getObjects();
                radio = Double.parseDouble(isNull(proration.get(0).getProportion()) ? "0.001" : proration.get(0).getProportion());
                break;
            case ORDER_ADD:  //购物车列表
                showTextDialog(getString(R.string.hm_hlxs_txt_183));
                intent = new Intent();
                intent.setAction(BaseConfig.BROADCAST_CART);
                sendBroadcast(intent);
                HemaArrayResult<IdInfo> orderAddResult = (HemaArrayResult<IdInfo>) hemaBaseResult;
                if (orderAddResult.getObjects() != null) {
                    IdInfo idInfo = orderAddResult.getObjects().get(0);
                    order_id = idInfo.getOrderid();
                    hmBarNameView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent it = new Intent(mContext, ActivityPay.class);
                            it.putExtra("keytype", "2");
                            it.putExtra("money", actual_pay_money);//剩余支付金额
                            it.putExtra("order_id", order_id);
                            it.putExtra("from", "4");
                            startActivity(it);
                            finish();
                        }
                    }, 500);
                }
                break;
            case PACKAGEORDER_ADD://套餐清单列表
                showTextDialog(getString(R.string.hm_hlxs_txt_183));
                HemaArrayResult<IdInfo> detailedAddResult = (HemaArrayResult<IdInfo>) hemaBaseResult;
                if (detailedAddResult.getObjects() != null) {
                    IdInfo idInfo = detailedAddResult.getObjects().get(0);
                    order_id = idInfo.getOrderid();
                    hmBarNameView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent it = new Intent(mContext, ActivityPay.class);
                            it.putExtra("keytype", "2");
                            it.putExtra("money", actual_pay_money);//剩余支付金额
                            it.putExtra("order_id", order_id);
                            it.putExtra("from", "4");
                            startActivity(it);
                            finish();
                        }
                    }, 500);
                }
                break;
            case FIRSTBUY:
                HemaArrayResult<IdInfo> directAddResult = (HemaArrayResult<IdInfo>) hemaBaseResult;
                if (directAddResult.getObjects() != null) {
                    IdInfo idInfo = directAddResult.getObjects().get(0);
                    order_id = idInfo.getOrderid();
                    hmBarNameView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent it = new Intent(mContext, ActivityPay.class);
                            it.putExtra("keytype", "2");
                            it.putExtra("money", actual_pay_money);//剩余支付金额
                            it.putExtra("order_id", order_id);
                            it.putExtra("from", "4");
                            startActivity(it);
                            finish();
                        }
                    }, 500);
                }
                break;
            case MEMBERORDER_ADD:
                HemaArrayResult<MemberorderAdd> couponResult = (HemaArrayResult<MemberorderAdd>) hemaBaseResult;
                user_order = couponResult.getObjects().get(0).getOrderid();
                showPwdPop();
                break;
            case ALLCOUPON_REMOVE:
                showTextDialog("支付成功");
                ticketView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
            default:
                break;
        }
    }

    private String user_order;

    private void showPwdPop() {
        if (TextUtils.isEmpty(addressView.getText().toString().trim()) == true) {
            XtomToastUtil.showShortToast(mContext, "选择收货地址");
            return;
        }
        
        if (statue == false) {
            if("5".equals(keytype))
                XtomToastUtil.showShortToast(mContext, "请勾选点券支付");
            else
                XtomToastUtil.showShortToast(mContext, "请勾选代金券支付");
            
            return;
        }

        if (dialog == null) {
            dialog = new HmHelpDialog(mContext, 2);
        }
        dialog.setPayTxt("支付");
        dialog.setTitleName("请输入支付密码");
        dialog.setLeftButtonText("取消");
        dialog.setRightButtonText("确定");
        dialog.setListener(new HmHelpDialog.onPwdListener() {
            @Override
            public void onSetPwd() {//设置密码
                Intent intent = new Intent(mContext, ActivitySetPayPwd.class);
                startActivity(intent);
            }

            @Override
            public void onGetPwd() {
                Intent intent = new Intent(mContext, ActivitySetPayPwd.class);
                startActivity(intent);
            }
        });

        dialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
            @Override
            public void onCancel(int type) {
                dialog.cancel();
            }

            @Override
            public void onConfirm(String msg) {
                dialog.cancel();
                String password = dialog.getPassword();
                dialog.clearPwdET();
                if (user.getPaypassword() == null || "".equals(user.getPaypassword())) {
                    XtomToastUtil.showShortToast(mContext, "请先设置支付密码");
                    return;
                }
                if (isNull(password)) {
                    XtomToastUtil.showShortToast(mContext, "请输入支付密码");
                    return;
                }
                getNetWorker().allcouponRemove(user.getToken(), user_order, password);
            }
        });
        dialog.show();
    }

    // 隐藏收货人地址布局
    public void hiddenAddressView() {
        if (addressInfo == null) {
            addressNameView.setVisibility(View.INVISIBLE);
            addressTelView.setVisibility(View.INVISIBLE);
            addressView.setVisibility(View.INVISIBLE);
            tipView.setVisibility(View.VISIBLE);
            freshData();
        } else {
            showAddressView();
            addressNameView.setText(addressInfo.getName());
            addressTelView.setText(addressInfo.getTel());
            addressView.setText(addressInfo.getAddress());
            tipView.setVisibility(View.INVISIBLE);
            address_id = addressInfo.getId();
            getAllExptessFee(addressInfo.getProvince());
        }

    }

    // 显示收货人地址布局
    public void showAddressView() {
        addressNameView.setVisibility(View.VISIBLE);
        addressTelView.setVisibility(View.VISIBLE);
        addressView.setVisibility(View.VISIBLE);
        tipView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case ADDRESS_LIST:
            case GOODS_EXPRESSFEE_GET:
            case CLIENT_GET:
            case ORDER_ADD:
            case FIRSTBUY:
            case PACKAGEORDER_ADD:
            case PROPORTION_GET:
            case ORDER_DIRECTADD:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            case MEMBERORDER_ADD:
                HemaArrayResult<MemberorderAdd> couponResult = (HemaArrayResult<MemberorderAdd>) hemaBaseResult;
                showTextDialog(couponResult.getMsg());
                break;
            case ALLCOUPON_REMOVE:
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
            case ADDRESS_LIST:
            case GOODS_EXPRESSFEE_GET:
            case CLIENT_GET:
            case ORDER_ADD:
            case FIRSTBUY:
            case PACKAGEORDER_ADD:
            case PROPORTION_GET:
            case ORDER_DIRECTADD:
            case ALLCOUPON_REMOVE:
                showTextDialog("操作失败，请稍后重试");
                break;
            default:
                break;
        }
    }

    private HmHelpDialog dialog = null;//密码提示框

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_zhifu:   //确认支付
                String number = data.get(0).getBuycount();
                getNetWorker().memberorderAdd(user.getToken(), goods_id, specs_id, address_id,
                        listGoodsAdapter.dealDatas.get(data.get(0).getShopid()).getText().toString(), number);
                break;
            case R.id.back_left:
                finish();
                break;
            case R.id.selectaddress:
                intent = new Intent(mContext, ActivityAddressList.class);
                intent.putExtra("which", "1");// 修改收货地址
                startActivityForResult(intent, ADDRESS);
                break;
            case R.id.relativeLayout_jifen:
                if (addressInfo == null) {
                    XtomToastUtil.showShortToast(mContext, getString(R.string.hm_hlxs_txt_184));
                    return;
                }
                if (vourcherInfo != null) {
                    XtomToastUtil.showShortToast(mContext, getString(R.string.hm_hlxs_txt_185));
                    return;
                }
                if (pay_score > score) {
                    XtomToastUtil.showShortToast(mContext, getString(R.string.hm_hlxs_txt_186));
                    return;
                }
                if (!isScorePay) {
                    isScorePay = true;
                    profit = Double.parseDouble(BaseUtil.transData(pay_score * radio));
                    chargeNumAndTotalMoney();
                } else {
                    isScorePay = false;
                    profit = 0;
                    chargeNumAndTotalMoney();
                }
                if(!deliverFeeView.isChecked())
                    deliverFeeView.setChecked(true);
                else
                    deliverFeeView.setChecked(false);
                break;
            case R.id.voucher:
                if (addressInfo == null) {
                    XtomToastUtil.showShortToast(mContext, getString(R.string.hm_hlxs_txt_187));
                    return;
                }
                if (isScorePay) {
                    XtomToastUtil.showShortToast(mContext, getString(R.string.hm_hlxs_txt_188));
                    return;
                }
                intent = new Intent(mContext, ActivityVoucherList.class);
                intent.putExtra("which", "0");//0 代表由确认订单页面进入代金券页面；1 代表由个人中心进入
                intent.putExtra("money", actual_pay_money);
                intent.putExtra("totalMoney", BaseUtil.transData(money));
                startActivityForResult(intent, 1000);
                break;
            case R.id.pay:
                if (addressInfo == null) {
                    XtomToastUtil.showShortToast(mContext, getString(R.string.hm_hlxs_txt_187));
                    return;
                }

                memo = getDemo();
                if (memo.length() != 0)
                    memo = memo.substring(0, memo.length() - 1);

                if (justbuy) {
                    if (deliverFeeView.isChecked())
                        getNetWorker().firstbuy(user.getToken(), keytype, goods_id, data.get(0).getPropertyid(), data.get(0).getBuycount(),
                                addressInfo.getId(), vourcherInfo == null ? "" : vourcherInfo.getId(), "1", memo);
                    else
                        getNetWorker().firstbuy(user.getToken(), keytype, goods_id, data.get(0).getPropertyid(), data.get(0).getBuycount(), addressInfo.getId(),
                                vourcherInfo == null ? "" : vourcherInfo.getId(), isScorePay ? String.valueOf(pay_score) : vourcherInfo == null ? "3" : "2",
                                memo);
                } else {
                    if ("4".equals(keytype)) {
                        if (deliverFeeView.isChecked())
                            getNetWorker().packageorderAdd(user.getToken(), cart_id, addressInfo.getId(), "1", memo,
                                    vourcherInfo == null ? "" : vourcherInfo.getId());
                        else
                            getNetWorker().packageorderAdd(user.getToken(), cart_id, addressInfo.getId(), vourcherInfo == null ? "3" : "2",
                                    memo.toString(), vourcherInfo == null ? "" : vourcherInfo.getId());
                    } else if ("3".equals(keytype)) {
                        if (deliverFeeView.isChecked())
                            getNetWorker().firstbuy(user.getToken(), keytype, cart_id, data.get(0).getPropertyid(), data.get(0).getBuycount(),
                                    addressInfo.getId(), vourcherInfo == null ? "" : vourcherInfo.getId(), "1", memo);
                        else
                            getNetWorker().firstbuy(user.getToken(), keytype, cart_id, data.get(0).getPropertyid(), data.get(0).getBuycount(),
                                    addressInfo.getId(), vourcherInfo == null ? "" : vourcherInfo.getId(), isScorePay ? String.valueOf(pay_score) : vourcherInfo == null ? "3" : "2",
                                    memo);
                        break;
                    } else {
                        if (deliverFeeView.isChecked())
                            getNetWorker().orderAdd(user.getToken(), keytype, cart_id, addressInfo.getId(), vourcherInfo == null ? "" : vourcherInfo.getId(),
                                    money + "", isScorePay ? String.valueOf(pay_score) : "1", memo);
                        else
                            getNetWorker().orderAdd(user.getToken(), keytype, cart_id, addressInfo.getId(), vourcherInfo == null ? "" : vourcherInfo.getId(),
                                    money + "", isScorePay ? String.valueOf(pay_score) : vourcherInfo == null ? "3" : "2", memo);
                    }
                }
                break;
            default:
                break;
        }
    }

    private String getDemo(){
        String memo;
        String shop_id = data.get(0).getShopid();
        String value = listGoodsAdapter.dealDatas.get(shop_id).getText().toString();
        if(isNull(value))
            memo = "";
        else{
            memo = data.get(0).getShopid() + "-" + listGoodsAdapter.dealDatas.get(shop_id).getText().toString() + ",";
        }
        for (int i = 0; i < data.size(); i++) {
            if (i < data.size() - 1) {
                String id_0 = data.get(i).getShopid();
                String id_1 = data.get(i + 1).getShopid();
                if (!id_0.equals(id_1)) {
                    String value1= listGoodsAdapter.dealDatas.get(id_1).getText().toString();
                    if(!isNull(value1)){
                        memo = memo +  id_1 + "-" + value + ",";
                    }
                }
            }
        }
        return memo;
    }
}
