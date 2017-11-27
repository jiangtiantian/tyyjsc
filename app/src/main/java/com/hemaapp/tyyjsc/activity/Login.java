package com.hemaapp.tyyjsc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetWorker;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.User;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import java.util.Map;
import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomToastUtil;
/**
 * 用户登录
 * 1、用户由个人中心页面登录
 * 2、用户从其他页面登录
 */
public class Login extends BaseActivity implements View.OnClickListener {
    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧文字按钮
    private Button loginBtn = null;//登录
    private ImageView forgetBtn = null;//忘记密码
    private TextView qqLoginBtn = null;//第三方QQ登录
    private TextView weixinLoginBtn = null;//第三方微信登录
    private EditText userNameEt = null;//手机号输入框
    private EditText userPasswordEt = null;//密码输入框
    private UMShareAPI mShareAPI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        getSystemService(Context.ACCESSIBILITY_SERVICE);
        mShareAPI = UMShareAPI.get(this);
    }

    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);
        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText("登录");
        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.INVISIBLE);
        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.VISIBLE);
        hmRightTxtView.setText("注册");
        userNameEt = (EditText) findViewById(R.id.username);
        userNameEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        userPasswordEt = (EditText) findViewById(R.id.password);
        userPasswordEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        loginBtn = (Button) findViewById(R.id.login);
        forgetBtn = (ImageView) findViewById(R.id.forgetpwd);
        qqLoginBtn = (TextView) findViewById(R.id.qq);
        weixinLoginBtn = (TextView) findViewById(R.id.weixin);
    }

    @Override
    protected void getExras() {
    }

    @Override
    protected void setListener() {
        hmBackBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        forgetBtn.setOnClickListener(this);
        qqLoginBtn.setOnClickListener(this);
        weixinLoginBtn.setOnClickListener(this);
        hmRightTxtView.setOnClickListener(this);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
                showProgressDialog("正在登录");
                break;
            case THIRD_SAVE:
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
            case CLIENT_LOGIN:
                cancelProgressDialog();
                break;
            case THIRD_SAVE:
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        Intent intent = null;
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
                @SuppressWarnings("unchecked")
                HemaArrayResult<User> result = (HemaArrayResult<User>) hemaBaseResult;
                User user = result.getObjects().get(0);
                BaseApplication.getInstance().setUser(user);



                String username = hemaNetTask.getParams().get("username");
                String userpwd = hemaNetTask.getParams().get("password");

                String userId = user.getId();
                XtomSharedPreferencesUtil.save(mContext, "userid", userId);
                XtomSharedPreferencesUtil.save(mContext, "username", username);
                XtomSharedPreferencesUtil.save(mContext, "password", userpwd);
                XtomSharedPreferencesUtil.save(mContext,"isShowed","true");
                // 自动登录
                XtomSharedPreferencesUtil.save(mContext, "isAutoLogin", "true");
                //更改与登录成功后显示相关的界面
                intent = new Intent();
                intent.setAction(BaseConfig.BROADCAST_ACTION);
                sendBroadcast(intent);
                //发送广播，保存设备信息并与cid绑定
                Intent clientIntent1 = new Intent();
                clientIntent1.setAction("com.hemaapp.push.connect");
                // 发送 一个无序广播
                sendBroadcast(clientIntent1);
                finish();
                break;
            case THIRD_SAVE:
                @SuppressWarnings("unchecked")
                HemaArrayResult<User> tResult = (HemaArrayResult<User>) hemaBaseResult;
                User tUser = tResult.getObjects().get(0);
                if(isNull(tUser.getUsername())){
//                    Intent intent1 = new Intent(mContext, ActivityMobileSave.class);
//                    intent1.putExtra("user", tUser);
//                    startActivity(intent1);
                }else{
                    BaseApplication.getInstance().setUser(tUser);
                    HemaUtil.setThirdSave(mContext, true);// 将第三方登录标记为true,注意在退出登录时将其置为false
                    XtomSharedPreferencesUtil.save(mContext, "isAutoLogin", "true");
                    intent = new Intent();
                    intent.setAction(BaseConfig.BROADCAST_ACTION);
                    sendBroadcast(intent);

                    //发送广播，保存设备信息并与cid绑定
                    Intent clientIntent = new Intent();
                    clientIntent.setAction("com.hemaapp.push.connect");
                    sendBroadcast(clientIntent);

                    finish();
                }
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
            case CLIENT_LOGIN:
            case THIRD_SAVE:
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
            case CLIENT_LOGIN:
            case THIRD_SAVE:
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_left://返回
                finish();
                break;
            case R.id.login:
                String userName = userNameEt.getText().toString().trim();
                String userPwd = userPasswordEt.getText().toString().trim();

                if (isNull(userName)) {
                    //showToast(R.mipmap.hm_icon_col_fail, "请输入用户名");
                    showTextDialog("请输入用户名");
                    return;
                }
                String mobile = "^[1][3-8]+\\d{9}";
                if (!userName.matches(mobile)) {
                    showTextDialog("您输入的手机号格式不正确");
                    return;
                }
                if (isNull(userPwd)) {
                    showTextDialog("请输入密码");
                    return;
                }
                BaseNetWorker netWorker = getNetWorker();
                netWorker.clientLogin(userName, userPwd);
                break;
            case R.id.bar_right_txt://注册
                intent = new Intent(mContext, ActivityReg.class);
                intent.putExtra("keytype", "0");
                startActivity(intent);
                break;
            case R.id.forgetpwd://找回密码
                intent = new Intent(mContext, ActivityGetPwd.class);
                intent.putExtra("keytype", "1");//密码类型 1 登录密码 2 支付密码
                startActivity(intent);
                break;
            case R.id.qq://QQ登录
                showToast(R.mipmap.ic_launcher, "暂未开通");
//                showProgressDialog("请稍后...");
//                if(mShareAPI.isInstall(this, SHARE_MEDIA.QQ)){
//                    SHARE_MEDIA platformQQ = SHARE_MEDIA.QQ;
//                    mShareAPI.doOauthVerify(this, platformQQ, umAuthListener);
//                }else{
//                    cancelProgressDialog();
//                    XtomToastUtil.showShortToast(mContext,"请先安装QQ客户端");
//                }
                break;
            case R.id.weixin://微信登录
                showProgressDialog("请稍后...");
                if(mShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN)){
                    SHARE_MEDIA platformWX = SHARE_MEDIA.WEIXIN;
                    mShareAPI.doOauthVerify(this, platformWX, umAuthListener);
                }else{
                    cancelProgressDialog();
                    XtomToastUtil.showShortToast(mContext,"请先安装微信客户端");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //授权成功回调
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            cancelProgressDialog();
            //获取用户信息在map中获取字段
            mShareAPI.getPlatformInfo(Login.this, platform, new UMAuthListener() {
                @Override
                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

                    //通过调试打印键值对找出需要的字段
                    String nickname = "";
                    String thirduid = "";
                    String avatar = "";
                    String thirdtype = "1";// 1：微信 2：QQ
                    String sex = "";
                    String age = "20";

                    if (share_media.compareTo(SHARE_MEDIA.WEIXIN) == 0) {//微信
                        nickname = map.get("nickname");
                        thirduid = map.get("openid");
                        avatar = map.get("headimgurl");
                        thirdtype = "1";
                        sex = "1".equals(map.get("sex")) ? "男" : "女";
                    } else {//QQ
                        nickname = map.get("screen_name");
                        thirduid = map.get("openid");
                        avatar = map.get("profile_image_url");
                        thirdtype = "2";
                        sex = map.get("gender");
                    }
                    // 将第三方登录信息保存在本地
                    XtomSharedPreferencesUtil.save(mContext, "thirdtype", thirdtype);
                    XtomSharedPreferencesUtil.save(mContext, "thirduid", thirduid);
                    XtomSharedPreferencesUtil.save(mContext, "avatar", avatar);
                    XtomSharedPreferencesUtil.save(mContext, "nickname", nickname);
                    XtomSharedPreferencesUtil.save(mContext, "sex", sex);
                    XtomSharedPreferencesUtil.save(mContext, "age", age);
                    getNetWorker().thirdSave(thirdtype, thirduid, avatar, nickname, sex,
                            age);
                }

                @Override
                public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                }

                @Override
                public void onCancel(SHARE_MEDIA share_media, int i) {

                }
            });
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            cancelProgressDialog();
            hmBarNameView.post(new Runnable() {
                @Override
                public void run() {
                    XtomToastUtil.showShortToast(mContext, "授权失败");
                }
            });
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            cancelProgressDialog();
            hmBarNameView.post(new Runnable() {
                @Override
                public void run() {
                    XtomToastUtil.showShortToast(mContext, "您取消了授权");
                }
            });
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }
}
