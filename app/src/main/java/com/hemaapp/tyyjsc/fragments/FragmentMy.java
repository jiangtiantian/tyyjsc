package com.hemaapp.tyyjsc.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseFragment;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivityAccountBalance;
import com.hemaapp.tyyjsc.activity.ActivityAddressList;
import com.hemaapp.tyyjsc.activity.ActivityCollection;
import com.hemaapp.tyyjsc.activity.ActivityEditInfo;
import com.hemaapp.tyyjsc.activity.ActivityMore;
import com.hemaapp.tyyjsc.activity.ActivityOrderList;
import com.hemaapp.tyyjsc.activity.ActivityScoreAccountBalance;
import com.hemaapp.tyyjsc.activity.ActivitySign;
import com.hemaapp.tyyjsc.activity.ActivityVoucherList;
import com.hemaapp.tyyjsc.activity.ChuZhiCard;
import com.hemaapp.tyyjsc.activity.CircleActivity;
import com.hemaapp.tyyjsc.activity.CircleUnMaxActivity;
import com.hemaapp.tyyjsc.activity.ContactCustomer;
import com.hemaapp.tyyjsc.activity.CouponActivity;
import com.hemaapp.tyyjsc.activity.Login;
import com.hemaapp.tyyjsc.model.CodeAdd;
import com.hemaapp.tyyjsc.model.SysInitInfo;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.HmHelpDialog;
import com.hemaapp.tyyjsc.view.ShareDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import xtom.frame.util.XtomToastUtil;

/**
 * 我的
 */
public class FragmentMy extends BaseFragment implements View.OnClickListener {
    /**
     * 用户已登录，展示用户信息
     */
    private LinearLayout myCenterRl = null;//个人信息
    private RoundedImageView logoView = null;//用户头像
    private TextView userNickNameView = null;//用户昵称
    private TextView sexImageView;
    private FrameLayout levelBtn = null;//会员等级
    private TextView levelLogoImg = null;//等级图片
    /**
     * 用户未登录，提示用户登录
     */
    private Button loginBtn = null;//
    //套餐订单
    private RelativeLayout orderAllRl = null;
    private TextView dfkBtn = null;
    private TextView dfhBtn = null;
    private TextView dshBtn = null;
    private TextView dpjBtn = null;
    //商品订单
    private RelativeLayout goodsorderAllRl = null;
    private TextView goodsdfkBtn = null;
    private TextView goodsdfhBtn = null;
    private TextView goodsdshBtn = null;
    private TextView goodsdpjBtn = null;
    private TextView moneyBtn = null;
    private TextView cardBtn = null;
    private TextView ticketBtn = null;
    private TextView scoreBtn = null;
    private TextView incomeBtn = null;
    private TextView colBtn = null;//收藏
    private TextView addressBtn = null;//收货地址
    private User user = null;//用户信息
    private TextView set;
    private TextView text_telphone;
    private LoginInfoReceiver loginInfoReceiver = null;//处理用户登录成功后发送的信息广播
    private SysInitInfo infor;
    private ImageView iv_sign;
    private TextView qr_code;//生成二维码
    private TextView tv_coupon;//点券

    private ShareDialog shareDialog = null;//分享
    private Dialog dialog;
    private ImageView img;
    private View layout;
    private String code_query;//二维码url 字符串

    private TextView tv_circle; //我的朋友圈


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_my);
        super.onCreate(savedInstanceState);
        init();
        infor = BaseApplication.getInstance().getSysInitInfo();
        loginInfoReceiver = new LoginInfoReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BaseConfig.BROADCAST_ACTION);
        intentFilter.addAction("com.hemaapp.edit.info");
        getActivity().registerReceiver(loginInfoReceiver, intentFilter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            init();
        }
        super.onHiddenChanged(hidden);
    }

    //初始化登个人中心用户信息
    public void init() {
        user = BaseApplication.getInstance().getUser();
        if (user != null) {//已登录
            myCenterRl.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.INVISIBLE);
            initUserInfor();
        } else {//未登录
            myCenterRl.setVisibility(View.INVISIBLE);
            loginBtn.setVisibility(View.VISIBLE);
            loginBtn.setOnClickListener(this);
        }
    }

    //用户信息
    private void initUserInfor() {
        String path = user.getAvatar();
        ImageLoader.getInstance().displayImage(path, logoView, BaseUtil.displayImageOption());
        //昵称
        userNickNameView.setText(user.getNickname());
        String sex = user.getSex();
        if ("男".equals(sex)) {
            sexImageView.setText("♂");
        } else {
            sexImageView.setText("♀");
        }
        levelLogoImg.setText(user.getLevel());
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog("加载中");
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                HemaArrayResult<User> infoResult = (HemaArrayResult<User>) hemaBaseResult;
                user = infoResult.getObjects().get(0);
                BaseApplication.getInstance().setUser(user);
                initUserInfor();
                break;
            case CODE_ADD:
                HemaArrayResult<CodeAdd> code_result= (HemaArrayResult<CodeAdd>) hemaBaseResult;
                code_query=   code_result.getObjects().get(0).getQrcode();
                createDailog();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        myCenterRl = (LinearLayout) findViewById(R.id.center_ln);
        logoView = (RoundedImageView) findViewById(R.id.logo);
        logoView.setCornerRadius(10000);
        userNickNameView = (TextView) findViewById(R.id.name);
        levelBtn = (FrameLayout) findViewById(R.id.level);//用户已登录下等级
        levelLogoImg = (TextView) findViewById(R.id.level_logo);//等级图片
        loginBtn = (Button) findViewById(R.id.login);//用户未登录
        sexImageView = (TextView) findViewById(R.id.sex);
        orderAllRl = (RelativeLayout) findViewById(R.id.order_all_rl);
        dfkBtn = (TextView) findViewById(R.id.dfk);
        dfhBtn = (TextView) findViewById(R.id.dfh);
        dshBtn = (TextView) findViewById(R.id.dsh);
        dpjBtn = (TextView) findViewById(R.id.dpj);
        goodsorderAllRl = (RelativeLayout) findViewById(R.id.goods_order_all_rl);
        goodsdfkBtn = (TextView) findViewById(R.id.goods_dfk);
        goodsdfhBtn = (TextView) findViewById(R.id.goods_dfh);
        goodsdshBtn = (TextView) findViewById(R.id.goods_dsh);
        goodsdpjBtn = (TextView) findViewById(R.id.goods_dpj);
        moneyBtn = (TextView) findViewById(R.id.money);
        ticketBtn = (TextView) findViewById(R.id.ticket);
        scoreBtn = (TextView) findViewById(R.id.score);
        cardBtn = (TextView) findViewById(R.id.card);
        colBtn = (TextView) findViewById(R.id.collection);
        addressBtn = (TextView) findViewById(R.id.address);
        set = (TextView) findViewById(R.id.set);
        text_telphone = (TextView) findViewById(R.id.link);
        iv_sign= (ImageView) findViewById(R.id.iv_sign);

        qr_code= (TextView) findViewById(R.id.qr_code);//二维码
        tv_coupon=(TextView) findViewById(R.id.tv_coupon);//点券
        tv_circle = (TextView) findViewById(R.id.circle);
    }

    @Override
    protected void setListener() {
        levelBtn.setOnClickListener(this);
        myCenterRl.setOnClickListener(this);
        orderAllRl.setOnClickListener(this);
        dfkBtn.setOnClickListener(this);
        dfhBtn.setOnClickListener(this);
        dshBtn.setOnClickListener(this);
        dpjBtn.setOnClickListener(this);
        goodsorderAllRl.setOnClickListener(this);
        goodsdfkBtn.setOnClickListener(this);
        goodsdfhBtn.setOnClickListener(this);
        goodsdshBtn.setOnClickListener(this);
        goodsdpjBtn.setOnClickListener(this);
        moneyBtn.setOnClickListener(this);
        ticketBtn.setOnClickListener(this);
        scoreBtn.setOnClickListener(this);
        cardBtn.setOnClickListener(this);
        logoView.setOnClickListener(this);
        colBtn.setOnClickListener(this);
        addressBtn.setOnClickListener(this);
        set.setOnClickListener(this);
        text_telphone.setOnClickListener(this);
        iv_sign.setOnClickListener(this);

        qr_code.setOnClickListener(this);
        tv_coupon.setOnClickListener(this);
        tv_circle.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        Intent intent;
        if (user == null) {
            intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }else{
            switch (v.getId()) {
                case R.id.circle:
                    if("2".equals(user.getIs_max())){ //非种子用户
                        intent = new Intent(getActivity(), CircleUnMaxActivity.class);
                        startActivity(intent);
                    }else{
                        intent = new Intent(getActivity(), CircleActivity.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.tv_coupon://点券页面
                    startActivity(new Intent(getActivity(), CouponActivity.class));
                    break;
                case R.id.iv_sign://签到
                    startActivity(new Intent(getActivity(), ActivitySign.class));
                    break;
                case R.id.login://去登陆
                    intent = new Intent(getActivity(), Login.class);
                    intent.putExtra("keytype", "0");// -1 由其他页面设置登录密码;0 由个人中心设置登录密码（设置密码界面） | 完善个人信息（个人信息界面） 1 设置支付密码 2 编辑用户信息
                    startActivity(intent);
                    break;
                case R.id.logo://编辑个人信息
                    intent = new Intent(getActivity(), ActivityEditInfo.class);
                    intent.putExtra("keytype", "2");//编辑个人信息
                    startActivity(intent);
                    break;
//            case R.id.level://会员等级
//                intent = new Intent(getActivity(), ActivityLevel.class);
//                startActivity(intent);
//                break;
                case R.id.order_all_rl://全部订单
                    intent = new Intent(getActivity(), ActivityOrderList.class);
                    intent.putExtra("from_what", "2");
                    intent.putExtra("keytype", "0");
                    startActivity(intent);
                    break;
                case R.id.dfk://待付款
                    intent = new Intent(getActivity(), ActivityOrderList.class);
                    intent.putExtra("keytype", "1");
                    intent.putExtra("from_what", "2");
                    startActivity(intent);
                    break;
                case R.id.dfh://待发货
                    intent = new Intent(getActivity(), ActivityOrderList.class);
                    intent.putExtra("from_what", "2");
                    intent.putExtra("keytype", "2");
                    startActivity(intent);
                    break;
                case R.id.dsh://待收货
                    intent = new Intent(getActivity(), ActivityOrderList.class);
                    intent.putExtra("from_what", "2");
                    intent.putExtra("keytype", "3");
                    startActivity(intent);
                    break;
                case R.id.dpj://待评价
                    intent = new Intent(getActivity(), ActivityOrderList.class);
                    intent.putExtra("from_what", "2");
                    intent.putExtra("keytype", "4");
                    startActivity(intent);
                    break;
                case R.id.goods_dsh://待收货
                    intent = new Intent(getActivity(), ActivityOrderList.class);
                    intent.putExtra("from_what", "1");
                    intent.putExtra("keytype", "3");
                    startActivity(intent);
                    break;
                case R.id.goods_dpj://待评价
                    intent = new Intent(getActivity(), ActivityOrderList.class);
                    intent.putExtra("from_what", "1");
                    intent.putExtra("keytype", "4");
                    startActivity(intent);
                    break;
                case R.id.goods_order_all_rl://全部订单
                    intent = new Intent(getActivity(), ActivityOrderList.class);
                    intent.putExtra("from_what", "1");
                    intent.putExtra("keytype", "0");
                    startActivity(intent);
                    break;
                case R.id.goods_dfk://待付款
                    intent = new Intent(getActivity(), ActivityOrderList.class);
                    intent.putExtra("keytype", "1");
                    intent.putExtra("from_what", "1");
                    startActivity(intent);
                    break;
                case R.id.goods_dfh://待发货
                    intent = new Intent(getActivity(), ActivityOrderList.class);
                    intent.putExtra("from_what", "1");
                    intent.putExtra("keytype", "2");
                    startActivity(intent);
                    break;
//            case R.id.tui://退款
//                intent = new Intent(getActivity(), ActivityOrderRefoundList.class);
//                startActivity(intent);
//                break;
//            case R.id.my_wallet://我的钱包
//                intent = new Intent(getActivity(), ActivityWallet.class);
//                startActivity(intent);
//                break;
                case R.id.money://账户余额
                    intent = new Intent(getActivity(), ActivityAccountBalance.class);
                    intent.putExtra("keytype", "0");
                    startActivity(intent);
                    break;
                case R.id.card://储值卡
                    intent = new Intent(getActivity(), ChuZhiCard.class);
                    startActivity(intent);
                    break;
                case R.id.score://积分
                    intent = new Intent(getActivity(), ActivityScoreAccountBalance.class);
                    intent.putExtra("keytype", "1");
                    startActivity(intent);
                    break;
                case R.id.ticket://代金券
                    intent = new Intent(getActivity(), ActivityVoucherList.class);
                    intent.putExtra("which", "1");
                    startActivity(intent);
                    break;
                case R.id.collection://我的收藏
                    intent = new Intent(getActivity(), ActivityCollection.class);
                    startActivity(intent);
                    break;
                case R.id.address://地址
                    intent = new Intent(getActivity(), ActivityAddressList.class);
                    intent.putExtra("which", "0");
                    startActivity(intent);
                    break;
                case R.id.link://客服
//                showPhoneDialog();
                    intent=new Intent(getActivity(),ContactCustomer.class);
                    startActivity(intent);
                    break;
                case R.id.set://更多
                    intent = new Intent(getActivity(), ActivityMore.class);
                    startActivity(intent);
                    break;
                case R.id.qr_code://生成二维码
                    if(isNull(code_query)){
                        initView();
                    }else{
                        createDailog();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void initView() {   //二维码请求与判断
        if (user!=null){
            getNetWorker().codeAdd(user.getToken());
        }else{
            startActivity(new Intent(getActivity(),Login.class));
        }
    }

    private void createDailog(){
        dialog=new Dialog(getActivity(), R.style.Dialog);
        layout = LayoutInflater.from(getActivity()).inflate(R.layout.item_qrcode, null);
        dialog.addContentView(layout, new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                , android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        //dialog.create();
        img = (ImageView)layout.findViewById(R.id.iv_qrcode);
        if (user!=null){
            Glide.with(getActivity()).load(code_query).into(img);
            dialog.setCanceledOnTouchOutside(true);// 点击外部区域关闭
        }else{
            startActivity(new Intent(getActivity(),Login.class));
        }
        TextView share= (TextView) layout.findViewById(R.id.tv_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("请稍等...");
                Config.dialog = dialog;
                if (shareDialog == null) {
                    shareDialog = new ShareDialog(getActivity());
                }
                shareDialog.setListener(new onClickPlatformShareListener());
                shareDialog.show();
            }
        });
        dialog.show();
    }

    private HmHelpDialog logoutDialog;
    private void showPhoneDialog(){
        if (logoutDialog == null) {
            logoutDialog = new HmHelpDialog(getActivity(), 0);
        }
        logoutDialog.setLeftButtonText("取消");
        logoutDialog.setRightButtonText("确定");
        logoutDialog.setTitleName("确定要联系客服\n"+infor.getSys_service_phone());
        logoutDialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
            @Override
            public void onCancel(int type) {
                logoutDialog.cancel();
            }

            @Override
            public void onConfirm(String msg) {
                logoutDialog.cancel();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + infor.getSys_service_phone()));
                startActivity(intent);
            }
        });
        logoutDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(loginInfoReceiver);
    }

    @Override
    protected void lazyLoad() {
        //nothing
    }

    //广播消息接收器
    class LoginInfoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.hemaapp.edit.info".equals(intent.getAction()))
                getNetWorker().clientGet(user.getToken(), user.getId());
            else
                init();

        }
    }



    //各平台下的分享
    class onClickPlatformShareListener implements ShareDialog.onShareListener {


        @Override
        public void onShare(int which) {
            switch (which) {
                case 0://QQ
                    new ShareAction(getActivity())
                            .setPlatform(SHARE_MEDIA.QQ)
                            .setCallback(new OnUmShareListener())
                            .withTitle(getResources().getString(R.string.app_name))
                           // .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withTargetUrl(infor.getSys_plugins()+"share/sdk.php?id="+user.getId()+"&keytype=5")
                            .withMedia(new UMImage(getActivity(), R.mipmap.ic_launcher))
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
                    new ShareAction(getActivity())
                            .setPlatform(SHARE_MEDIA.WEIXIN)
                            .setCallback(new OnUmShareListener())
                            .withTitle(getResources().getString(R.string.app_name))
                           // .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withTargetUrl(infor.getSys_plugins()+"share/sdk.php?id="+user.getId()+"&keytype=5")
                            .withMedia(new UMImage(getActivity(), R.mipmap.ic_launcher))
                            .share();
                    break;
                case 3://朋友圈
                    new ShareAction(getActivity())
                            .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                            .setCallback(new OnUmShareListener())
                            .withTitle(getResources().getString(R.string.app_name))
                            //.withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withTargetUrl(infor.getSys_plugins()+"share/sdk.php?id="+user.getId()+"&keytype=5")
                            .withMedia(new UMImage(getActivity(), R.mipmap.ic_launcher))
                            .share();
                    break;
                case 4://新浪
                    new ShareAction(getActivity())
                            .setPlatform(SHARE_MEDIA.QZONE)
                            .setCallback(new OnUmShareListener())
                            .withTitle(getResources().getString(R.string.app_name))
                          //  .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withTargetUrl(infor.getSys_plugins()+"share/sdk.php?id="+user.getId()+"&keytype=5")
                            .withMedia(new UMImage(getActivity(), R.mipmap.ic_launcher))
                            .share();
                    break;
            }
        }
    }

    //友盟分享回调
    class OnUmShareListener implements UMShareListener {

        @Override
        public void onResult(SHARE_MEDIA share_media) {

            XtomToastUtil.showShortToast(getActivity(), "分享成功");
           /* hmBarNameView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (user != null) {
                        getNetWorker().share(user.getToken(), "0");
                    }
                }
            }, 300);*/
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
         /*   hmBarNameView.post(new Runnable() {
                @Override
                public void run() {
                    XtomToastUtil.showShortToast(getActivity(), "分享失败");
                }
            });*/
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
          /*  hmBarNameView.post(new Runnable() {
                @Override
                public void run() {
                    XtomToastUtil.showShortToast(getActivity(), "分享取消");
                }
            });*/
        }
    }
    /* 分享相关end */
}
