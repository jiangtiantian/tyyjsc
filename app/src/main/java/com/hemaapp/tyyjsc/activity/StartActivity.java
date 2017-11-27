package com.hemaapp.tyyjsc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetWorker;
import com.hemaapp.tyyjsc.BaseUpGrade;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.AdList;
import com.hemaapp.tyyjsc.model.SysInitInfo;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.CusImageView;

import java.util.ArrayList;

import xtom.frame.util.XtomSharedPreferencesUtil;


/**
 * 启动页
 */
public class StartActivity extends BaseActivity implements BaseUpGrade.onCancelListener, AMapLocationListener {
    // private CusImageView imageView;
    private CusImageView imageView;
    private SysInitInfo sysInitInfo;
    // 标记是否展示过引导页
    private boolean isShowed = false;
    // 检测升级
    private BaseUpGrade upGrade = null;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;

    private ArrayList<AdList> infors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_start);
        super.onCreate(savedInstanceState);


 /*  sysInitInfo =  BaseApplication.getInstance().getSysInitInfo();*/

        // 软件升级
        upGrade = new BaseUpGrade(mContext);
        upGrade.setListener(this);

        startLocation();
        init();

    }

    private void startLocation() {
        if (locationClient == null) {
            locationClient = new AMapLocationClient(mContext);
            locationOption = new AMapLocationClientOption();
            //设置定位监听
            locationClient.setLocationListener(this);
            //设置为高精度定位模式
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            locationOption.setOnceLocation(true);
            //设置定位参数
            locationClient.setLocationOption(locationOption);
            locationClient.startLocation();
        }
    }


    //初始化启动动画
    private void init() {
//        setStartImage();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo);
        animation.setAnimationListener(new StartAnimationListener());
        imageView.startAnimation(animation);
    }

    //动态展示启动页图片
    private void setStartImage() {
        imageView.setImageResource(R.mipmap.first01);
//        if (sysInitInfo != null)
//            startImage = sysInitInfo.getStart_img();
//        if (!isNull(startImage)) {
//            URL url;
//            try {
//                url = new URL(startImage);
//                ImageLoader.getInstance().displayImage(startImage, imageView, BaseUtil.displayImageOption(R.mipmap.first01));
//            } catch (MalformedURLException e) {
//                // 设置默认图片
//                imageView.setImageResource(R.mipmap.first01);
//            }
//        }
    }

    private void toGuide() {
        // 判断信息引导页是否展示过了
        if (!isShow()) {
            Intent it = new Intent(mContext, ActivityGuide.class);
            startActivity(it);
            finish();
        } else {
            // 判断是否自动登录
            if (isAutoLogin()) {
                if (HemaUtil.isThirdSave(mContext)) {
                    BaseNetWorker netWorker = getNetWorker();
                    netWorker.thirdSave();
                    return;
                }
                String username = XtomSharedPreferencesUtil.get(this,
                        "username");
                String password = XtomSharedPreferencesUtil.get(this,
                        "password");
                if (!isNull(username) && !isNull(password)) {
                    BaseNetWorker netWorker = getNetWorker();
                    netWorker.clientLogin();
                } else {
                    toMain();
                }
            } else {
                toMain();
            }
        }
    }

    // 检查是否已经展示过引导页界面了
    private boolean isShow() {
        isShowed = "true".equals(XtomSharedPreferencesUtil.get(mContext,
                "isShowed"));
        return isShowed;
    }

    // 检查是否自动登录
    private boolean isAutoLogin() {
        String autoLogin = XtomSharedPreferencesUtil.get(mContext,
                "isAutoLogin");
        boolean no = "false".equals(autoLogin);
        return !no;
    }

    // 程序主界面
    private void toMain() {
        if(infors == null || infors.size() == 0){
            Intent it = new Intent(mContext, ActivityIndex.class);
            startActivity(it);
            finish();
        }else{
            Intent it = new Intent(mContext, OpenAdAcitivity.class);
            it.putExtra("imgurl", infors.get(0).getImgurl());
            startActivity(it);
            finish();
        }
    }

    @Override
    public void cancel() {
        toGuide();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) { //定位成功
            Double lat = aMapLocation.getLatitude();
            Double lng = aMapLocation.getLongitude();
            BaseApplication application = BaseApplication.getInstance();
            log_i("定位成功：lng=" + lng.toString() + " lat=" + lat.toString());
            XtomSharedPreferencesUtil.save(application, "lat", lat.toString());
            XtomSharedPreferencesUtil.save(application, "lng", lng.toString());
            XtomSharedPreferencesUtil.save(application, "dizhi", aMapLocation.getAddress());
            XtomSharedPreferencesUtil.save(application, "city_name", aMapLocation.getCity());
            XtomSharedPreferencesUtil.save(application, "city", aMapLocation.getCity());
            XtomSharedPreferencesUtil.save(application, "district", aMapLocation.getProvince() + aMapLocation.getCity());
            XtomSharedPreferencesUtil.save(application, "position", aMapLocation.getProvince() + "," + aMapLocation.getCity() + "," + aMapLocation.getDistrict());
            //发送广播通知定位成功
            Intent intent = new Intent();
            intent.setAction(BaseConfig.BROADCAST_GETCITY);
            sendBroadcast(intent);
        } else {
            startLocation();
        }
    }

    private class StartAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            // 计算手机的分辨率
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int w = metrics.widthPixels;
            XtomSharedPreferencesUtil.save(mContext, "w", String.valueOf(w));
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            BaseNetWorker netWorker = getNetWorker();
            netWorker.init();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    @Override
    protected void findView() {
        imageView = (CusImageView) findViewById(R.id.start);
        imageView.setRatio(0.635f);
    }

    @Override
    protected void getExras() {
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case INIT:
            case CLIENT_LOGIN:
            case THIRD_SAVE:
                showProgressDialog("请稍后");
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case INIT:
            case CLIENT_LOGIN:
            case THIRD_SAVE:
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
            case INIT:
                HemaArrayResult<SysInitInfo> sResult = (HemaArrayResult<SysInitInfo>) hemaBaseResult;
                sysInitInfo = sResult.getObjects().get(0);
                setStartImage();
                BaseApplication.getInstance().setSysInitInfo(sysInitInfo);
                getNetWorker().adList("4");
                String sysVersion = sysInitInfo.getAndroid_last_version();
                String version = HemaUtil.getAppVersionForSever(mContext);
                if (HemaUtil.isNeedUpDate(version, sysVersion)) {
                    upGrade.check();
                }
                break;
            case CLIENT_LOGIN:
                @SuppressWarnings("unchecked")
                HemaArrayResult<User> uResult = (HemaArrayResult<User>) hemaBaseResult;
                User user = uResult.getObjects().get(0);
                BaseApplication.getInstance().setUser(user);
                toMain();
                break;
            case THIRD_SAVE:
                HemaArrayResult<User> tResult = (HemaArrayResult<User>) hemaBaseResult;
                User tUser = tResult.getObjects().get(0);
                BaseApplication.getInstance().setUser(tUser);
                HemaUtil.setThirdSave(mContext, true);// 将第三方登录标记为true,注意在退出登录时将其置为false
                XtomSharedPreferencesUtil.save(mContext, "isAutoLogin", "true");
                Intent itthird = new Intent(this, ActivityIndex.class);
                startActivity(itthird);
                finish();
                break;
            case AD_LIST:
                HemaArrayResult<AdList> a_result = (HemaArrayResult<AdList>) hemaBaseResult;
                infors = a_result.getObjects();
                toGuide();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case INIT:
            case THIRD_SAVE:
                //showToast(R.mipmap.ic_launcher, hemaBaseResult.getMsg());
                break;
            case CLIENT_LOGIN:
                XtomSharedPreferencesUtil.save(mContext, "username", "");
                XtomSharedPreferencesUtil.save(mContext, "password", "");
                Intent intent = new Intent(mContext, ActivityIndex.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case INIT:
            case THIRD_SAVE:
                break;
            case CLIENT_LOGIN:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.with(getApplicationContext()).pauseRequests();
    }
}
