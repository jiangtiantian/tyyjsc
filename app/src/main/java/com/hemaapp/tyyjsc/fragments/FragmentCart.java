package com.hemaapp.tyyjsc.fragments;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseFragment;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivityConfirmOrder;
import com.hemaapp.tyyjsc.activity.Login;
import com.hemaapp.tyyjsc.adapters.CartGoodAdapter;
import com.hemaapp.tyyjsc.model.CartGoodsInfo;
import com.hemaapp.tyyjsc.model.ShopCart;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.HmHelpDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 购物车
 */
public class FragmentCart extends BaseFragment implements OnClickListener, CartGoodAdapter.onDealChildClickListener {
    private final static long hourLevelValue = 60 * 60 * 1000;
    private final static long minuteLevelValue = 60 * 1000;
    private final static long secondLevelValue = 1000;
    private TextView hmRightTxtView = null;//标题栏右侧微文字按钮
    private RelativeLayout cartListLayout = null;
    private ListView listView = null;
    private ArrayList<CartGoodsInfo> showDatas = new ArrayList<>();//获取所有的商品
    private ArrayList<CartGoodsInfo> selecedArr = new ArrayList<>();//
    private ArrayList<CartGoodsInfo> tempSelecedArr = new ArrayList<>();//
    private HashMap<Integer, ArrayList<CartGoodsInfo>> conformlist = new HashMap<>();
    private CartGoodAdapter cartgoodAdapter = null;
    private RefreshLoadmoreLayout layout = null;
    private RelativeLayout emptyLayout = null;
    //尚未登录
    private TextView goLoginBtn = null;
    private Button cart_clearing_btn = null;//计算
    private TextView cart_clearing_text = null;//总额
    private FrameLayout bottomLayout = null;//底部全选、总额、计算布局
    private LinearLayout allCheckLayout = null;
    private CheckBox allBox = null;//全选
    private HmHelpDialog hmHelpDialog = null;
    private HmHelpDialog hmHelpDialog1 = null;
    private TextView clickCart, limitCart;
    private User user = null;//个人信息
    private LoginInfoReceiver loginInfoReceiver = null;//处理用户登录成功后发送的信息广播
    private FrameLayout remainTimeLayout = null;//剩余有效时间
    private TextView timeTV = null;//倒计时
    private long period = 0;//时间戳
    private int offset = 0;
    private int curPos = 0;
    private TextView timeViewH = null;//时
    private TextView timeViewM = null;//分
    private TextView timeViewS = null;//秒
    private View selector;
    private String type = "1";//1：购物车 2：购物清单
    private ImageView Tabview = null;
    private int getleft = 0;//tab距离父布局的距离，便于计算动画距离
    private CountDownTimer countDownTimer = null;//计时器


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_cart);
        super.onCreate(savedInstanceState);
        loginInfoReceiver = new LoginInfoReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BaseConfig.BROADCAST_ACTION);
        getActivity().registerReceiver(loginInfoReceiver, intentFilter);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        offset = BaseUtil.dip2px(getActivity(), 210) / 2;//初始化滑动动画的大小
        getleft = (dm.widthPixels - BaseUtil.dip2px(getActivity(), 210)) / 2;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                offset, LinearLayout.LayoutParams.MATCH_PARENT);
        Tabview.setLayoutParams(params);
        Matrix matrix = new Matrix();
        matrix.postTranslate(0, 0);
        Tabview.setImageMatrix(matrix);
        init();
    }

    public void init() {
        user = BaseApplication.getInstance().getUser();
        if (user == null) {// 如果用户尚未登录，则显示登录提示
            emptyLayout.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
            //去登录
            goLoginBtn.setVisibility(View.VISIBLE);
            hmRightTxtView.setVisibility(View.INVISIBLE);
            cartListLayout.setVisibility(View.GONE);
        } else {// 获取购物车商品
            goLoginBtn.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.GONE);
            cartListLayout.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
            getCartList(type);
        }
    }

    /**
     * 只在Fragmetn切换时调用onHiddenChanged
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
//        if (!hidden) {
//            init();
//        }
//        super.onHiddenChanged(hidden);
    }

    //获取购物车商品
    public void getCartList(String type) {
        user = BaseApplication.getInstance().getUser();
        if (user == null) {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
            return;
        }
        getNetWorker().getCartGoodsList(user.getToken(), type);
    }

    //购物车操作
    public void cartOperate(String keytype, String cart_id, String buycount) {
        user = BaseApplication.getInstance().getUser();
        if (user == null) {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
            return;
        }
        getNetWorker().cartOperate(user.getToken(), keytype, cart_id, buycount);
    }

    public void freshData() {
        freshCartData();
    }

    //倒计时
    public void countTimer(ShopCart info) {
        String endTime = "";//结束时间
        Date endDate = null;//结束时间
        Date serviceDate = null;//服务器时间
        endTime = info.getCleantime();
        // endDate = BaseUtil.formatDate(endTime, info.getNowtime());
        try {
            endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(info.getCleantime());
            serviceDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(info.getNowtime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (endDate != null) {
            period = (endDate.getTime() - serviceDate.getTime()) <= 0 ? 0 : (endDate.getTime() - serviceDate.getTime());
            countDownTimer = new CountDownTimer(period, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    period -= 1000;
                    getDifference(period, timeViewH, timeViewM, timeViewS);
                }

                @Override
                public void onFinish() {
                    // remainTimeLayout.setVisibility(View.GONE);
//                    showDatas.clear();
                    // cartgoodAdapter.notifyDataSetChanged();
                }
            }.start();
        }

    }

    //刷新购物车数据
    public void freshCartData() {
        if (cartgoodAdapter == null) {
            cartgoodAdapter = new CartGoodAdapter(getActivity(), showDatas, listView);
            cartgoodAdapter.setListener(this);
            cartgoodAdapter.setEmptyString("马上去购物");
            listView.setAdapter(cartgoodAdapter);
        } else {
            cartgoodAdapter.setEmptyString("马上去购物");
            cartgoodAdapter.setData(showDatas);
            cartgoodAdapter.notifyDataSetChanged();
        }
        if (showDatas == null || showDatas.size() == 0) {
            bottomLayout.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layout.getLayoutParams();
            params.bottomMargin = BaseUtil.dip2px(getActivity(), 0);
            layout.setLayoutParams(params);
            hmRightTxtView.setVisibility(View.INVISIBLE);
        } else {
            bottomLayout.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layout.getLayoutParams();
            params.bottomMargin = BaseUtil.dip2px(getActivity(), 58);
            layout.setLayoutParams(params);
            hmRightTxtView.setVisibility(View.VISIBLE);
            chargeTotalFee();
        }
        //清空全选状态
        allBox.setChecked(false);
    }

    //计算总价
    public void chargeTotalFee() {
        double total_fee = 0.00;
        for (int i = 0; i < showDatas.size(); i++) {//最外层商家
            CartGoodsInfo cartItemInfo = showDatas.get(i);
            if (cartItemInfo.isChecked()) {
                total_fee += Integer.parseInt(isNull(cartItemInfo.getBuycount()) ? "0" : cartItemInfo.getBuycount()) * Double.parseDouble(isNull(cartItemInfo.getPrice()) ? "0" : cartItemInfo.getPrice());
            }
        }
        cart_clearing_text.setText("¥" + BaseUtil.transData(total_fee));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CART_LIST:
            case CART_OPERATE:
                showProgressDialog(R.string.hm_hlxs_txt_59);
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
            case CART_LIST:
            case CART_OPERATE:
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
            case CART_LIST:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                layout.refreshSuccess();
                showDatas.clear();
                HemaArrayResult<ShopCart> nResult = (HemaArrayResult<ShopCart>) hemaBaseResult;
                for (ShopCart cart : nResult.getObjects()) {
                    showDatas.addAll(cart.getGoods());
                }
                if ("2".equals(type)) {
                    if (nResult.getObjects().size() != 0) {
                        remainTimeLayout.setVisibility(View.VISIBLE);
                        countTimer(nResult.getObjects().get(0));
                    }
                }
                freshData();
                break;
            case CART_OPERATE:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                getCartList(type);
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
            case CART_LIST:
            case CART_OPERATE:
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
            case CART_LIST:
                layout.refreshFailed();
                break;
            case CART_OPERATE:
                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {
        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.VISIBLE);
        hmRightTxtView.setText("清空");
        selector = findViewById(R.id.selector);
        cartListLayout = (RelativeLayout) findViewById(R.id.cart_list_rl);
        listView = (ListView) findViewById(R.id.cart_list);
        clickCart = (TextView) findViewById(R.id.cart);
        limitCart = (TextView) findViewById(R.id.limit_cart);
        Tabview = (ImageView) findViewById(R.id.tab);
        cart_clearing_btn = (Button) findViewById(R.id.cart_clearing_btn);
        cart_clearing_text = (TextView) findViewById(R.id.cart_clearing_text);
        goLoginBtn = (TextView) findViewById(R.id.go_login);
        allCheckLayout = (LinearLayout) findViewById(R.id.alllayout);
        allBox = (CheckBox) findViewById(R.id.cart_allchoose_cb);
        layout = (RefreshLoadmoreLayout) findViewById(R.id.cart_have_goods_ly);
        emptyLayout = (RelativeLayout) findViewById(R.id.cart_no_goods_ly);
        bottomLayout = (FrameLayout) findViewById(R.id.cart_clearing_ly);
        remainTimeLayout = (FrameLayout) findViewById(R.id.remain_time_ll);
        timeViewH = (TextView) findViewById(R.id.time_h);
        timeViewM = (TextView) findViewById(R.id.time_m);
        timeViewS = (TextView) findViewById(R.id.time_s);
    }

    @Override
    protected void setListener() {
        clickCart.setOnClickListener(this);
        limitCart.setOnClickListener(this);
        layout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getCartList(type);
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {

            }
        });
        layout.setLoadmoreable(false);
        hmRightTxtView.setOnClickListener(this);
        emptyLayout.setOnClickListener(this);
        allCheckLayout.setOnClickListener(this);
        cart_clearing_btn.setOnClickListener(this);
        goLoginBtn.setOnClickListener(this);
        //实现点击购物车商品进详情时，取消注释
        //listView.setListener(this);
    }

    @Override
    protected void lazyLoad() {
        //nothing
    }

    public void moveTabBar(final int arg0) {
        Animation animation = new TranslateAnimation(curPos * offset,
                offset * arg0, 0, 0);
        animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
        animation.setDuration(200);//动画持续时间0.2秒
        Tabview.startAnimation(animation);
        curPos = arg0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cart:
                moveTabBar(0);
                clickCart.setTextColor(getResources().getColor(R.color.white));
                limitCart.setTextColor(getResources().getColor(R.color.black));
                type = "1";
                remainTimeLayout.setVisibility(View.GONE);
                getCartList(type);
                break;
            case R.id.limit_cart:
                clickCart.setTextColor(getResources().getColor(R.color.black));
                limitCart.setTextColor(getResources().getColor(R.color.white));
                remainTimeLayout.setVisibility(View.VISIBLE);
                moveTabBar(1);
                type = "2";
                getCartList(type);
                break;
            case R.id.bar_right_txt://清空
                if (hmHelpDialog == null) {
                    hmHelpDialog = new HmHelpDialog(getActivity(), 0);
                }
                hmHelpDialog.setTitleName("确定清空购物车");
                hmHelpDialog.setLeftButtonText("取消");
                hmHelpDialog.setRightButtonText("确定");
                hmHelpDialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
                    @Override
                    public void onCancel(int type) {
                        hmHelpDialog.cancel();
                    }

                    @Override
                    public void onConfirm(String msg) {
                        hmHelpDialog.cancel();
                        String ids = "";
                        for (CartGoodsInfo info : showDatas) {
                            ids += info.getId() + ",";
                        }
                        ids = ids.substring(0, ids.lastIndexOf(","));
                        if ("2".equals(type))
                            cartOperate("4", ids, "");//清空购物车
                        else
                            cartOperate("3", ids, "");//清空购物车
                    }
                });
                hmHelpDialog.show();
                break;
            case R.id.go_login://去登陆
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                break;
            case R.id.alllayout://全选
                CheckBox box = (CheckBox) allCheckLayout.getChildAt(0);
                boolean isChecked = box.isChecked();
                box.setChecked(!isChecked);
                isAllCheck(!isChecked);
                break;
            case R.id.cart_clearing_btn:
                boolean isExsit = false;
                selecedArr = new ArrayList<>();
                for (CartGoodsInfo info : showDatas) {
                    if (info.isChecked()) { //存在被选中商品
                        isExsit = true;
                        selecedArr.add(info);
                    }
                }
                if (!isExsit) {
                    XtomToastUtil.showShortToast(getActivity(), "请选择商品");
                    return;
                }
                //判断特价预订(一成购车)是否存在库存不足商品
                boolean isNoStockEnough = false;
                for (CartGoodsInfo info : showDatas) {
//                    if ("3".equals(info.getKeytype())) {//特价预订(一成购车)
//                        int stock = Integer.parseInt(isNull(info.getBuycount()) ? "0" : info.getBuycount());
//                        if (stock <= 0) {//已售罄
//                            isNoStockEnough = true;
//                            break;
//                        } else if (Integer.parseInt(info.getBuycount()) > stock) {
//                            isNoStockEnough = true;
//                            break;
//                        }
//                    }
                }
                if (isNoStockEnough) {
                    XtomToastUtil.showShortToast(getActivity(), "请移除库存不足商品");
                    return;
                }
                boolean ishasReserved = false;
                for (CartGoodsInfo info : showDatas) {
//                    if (info.isChecked() && "1".equals(info.getIs_reserved())) { //是否特价预订(一成购车)商品 	0否1是
//                        ishasReserved = true;
//                        break;
//                    }
                }
                if (ishasReserved) {
                    if (hmHelpDialog == null) {
                        hmHelpDialog = new HmHelpDialog(getActivity(), 0);
                    }
                    hmHelpDialog.setTitleName("购物车内存在一成购车商品\n如继续支付，则发货时间按一成购车时间");
                    hmHelpDialog.setLeftButtonText("取消");
                    hmHelpDialog.setRightButtonText("继续");
                    hmHelpDialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
                        @Override
                        public void onCancel(int type) {
                            hmHelpDialog.cancel();
                        }

                        @Override
                        public void onConfirm(String msg) {
                            hmHelpDialog.cancel();
                            Intent it = new Intent(getActivity(), ActivityConfirmOrder.class);
                            it.putExtra("data", selecedArr);
                            it.putExtra("keytype", "2");//购物车提交订单
                            startActivityForResult(it, R.id.layout_bank);
                        }
                    });
                    hmHelpDialog.show();
                } else {
                    int cout = 1;//计数有几个商家
                    for (int i = 0; i < selecedArr.size(); i++) {
                        if (i < selecedArr.size() - 1) {
                            if (!selecedArr.get(i).getShopid().equals(selecedArr.get(i + 1).getShopid())) {
                                cout++;
                            }
                        }

                    }
                    Intent it = new Intent(getActivity(), ActivityConfirmOrder.class);
                    it.putExtra("data", selecedArr);
                    it.putExtra("count", cout);
                    it.putExtra("keytype", type);//购物车提交订单
                    startActivityForResult(it, 1);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != (-1))
            return;
        switch (requestCode){
            case 1:
                getCartList(type);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //处理全选
    public void isAllCheck(Boolean bool) {
        for (CartGoodsInfo info : showDatas) {
            info.setChecked(bool);
            info.setSellerChecked(bool);
        }
        allBox.setChecked(bool);
        cartgoodAdapter.notifyDataSetChanged();
        chargeTotalFee();
    }

    @Override
    public void onDealChildClick(CartGoodsInfo item, boolean bool) {
        item.setChecked(bool);
        if (bool) {
            selecedArr.add(item);
        } else {
            selecedArr.remove(item);
        }
        checkStore(item.getShopid());
        cartgoodAdapter.notifyDataSetChanged();
    }

    /**
     * 判断该仓库下的商品是否已经全选
     */
    private void checkStore(String id) {
        boolean isAll = true;
        for (CartGoodsInfo item : showDatas) {
            if (id.equals(item.getShopid()) && !item.isChecked()) {//如果该商家下，有商品未选中
                isAll = false;
                break;
            }
        }
        //设置该仓库下的商品属性
        for (CartGoodsInfo item : showDatas) {
            if (id.equals(item.getShopid()))
                item.setSellerChecked(isAll);
        }
        if (isAll)//如果该仓库已经全选，再去判断是否所有仓库全选
            checkAll();
        else
            allBox.setChecked(false);
        chargeTotalFee();
    }

    /**
     * 判断是否购物车中全部商品已经勾选
     */
    public void checkAll() {
        boolean isAll = true;
        for (CartGoodsInfo item : showDatas) {
            if (!item.isSellerChecked()) {
                isAll = false;
                break;
            }
        }
        if (isAll) {//如果已经全选，就把全选按钮勾上
            allBox.setChecked(true);
        }
        chargeTotalFee();
    }

    //处理头部全选未选中的操作
    @Override
    public void onheaderListener(String id, boolean bool) {
        for (CartGoodsInfo item : showDatas) {
            if (id.equals(item.getShopid())) {
                item.setChecked(bool);
                item.setSellerChecked(bool);
                if (bool) {//让子类全选
                    if (!selecedArr.contains(item))
                        selecedArr.add(item);
                    if (selecedArr.size() >= showDatas.size())
                        allBox.setChecked(true);
                } else {
                    selecedArr.remove(item);
                    allBox.setChecked(false);
                }
            }
        }
        cartgoodAdapter.notifyDataSetChanged();
        chargeTotalFee();
    }

    @Override
    public void onChangeNum(String keytype, CartGoodsInfo info, int buycount) {
        cartOperate(keytype, info.getId(), String.valueOf(buycount));
    }

    @Override
    public void onLongClick(final CartGoodsInfo info) {
        //处理长按删除的操作
        if (hmHelpDialog1 == null) {
            hmHelpDialog1 = new HmHelpDialog(getActivity(), 0);
        }
        hmHelpDialog1.setTitleName("确定删除该商品");
        hmHelpDialog1.setLeftButtonText("取消");
        hmHelpDialog1.setRightButtonText("确定");
        hmHelpDialog1.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
            @Override
            public void onCancel(int type) {
                hmHelpDialog1.cancel();
            }

            @Override
            public void onConfirm(String msg) {
                hmHelpDialog1.cancel();
                cartOperate("2", info.getId(), "");//清空购物车
            }
        });
        hmHelpDialog1.show();
    }

    //广播消息接收器
    class LoginInfoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            init();
        }
    }

    public void remove() {
//        if (data != null) {
//            for (int i = 0; i < data.size(); i++) {
//                if (data.get(i).isChecked()) {
//                    data.remove(data.get(i));
//                    i--;
//                }
//            }
//        }
//        if (data.size() > 0) {
//            if (cartGoodAdapter != null) {
//                cartGoodAdapter.setData(data);
//                cartGoodAdapter.notifyDataSetChanged();
//            }
//            isAllCheck();
//        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        getActivity().unregisterReceiver(loginInfoReceiver);
    }
}
