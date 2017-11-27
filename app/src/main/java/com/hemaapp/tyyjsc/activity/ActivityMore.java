package com.hemaapp.tyyjsc.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.IdInfo;
import com.hemaapp.tyyjsc.model.SysInitInfo;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.ButtonDialog;
import com.hemaapp.tyyjsc.view.HmHelpDialog;
import com.hemaapp.tyyjsc.view.ShareDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import xtom.frame.image.cache.XtomImageCache;
import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomToastUtil;

/**
 * 更多
 */
public class ActivityMore extends BaseActivity implements View.OnClickListener {

    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧文字按钮

    private LinearLayout accountLy = null;//账户管理
    private LinearLayout aboutLy = null;//关于我们
    private LinearLayout adviceLy = null;//意见反馈
    private LinearLayout softwareShareLy = null;//软件分享
    private FrameLayout cacheLy = null;//清除缓存
    private TextView cacheSizeView = null;//显示缓存大小
    private HmHelpDialog exitDialog;
    private Button logoutBtn = null;//注销登录
    private HmHelpDialog logoutDialog = null;
    private User user = null;// 用户信息
    private LinearLayout contactLayout=null;
    private ShareDialog shareDialog = null;
    private String sharesharepath = "";
    private ButtonDialog dialog = null;//分享成功提示框
    private SysInitInfo sysInitInfo = null;
    private String invite = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_more);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        invite = getString(R.string.hm_hlxs_txt_132);
        user = BaseApplication.getInstance().getUser();
        sysInitInfo = BaseApplication.getInstance().getSysInitInfo();
        init();
        final String id = "0";
        SysInitInfo initInfo = BaseApplication.getInstance().getSysInitInfo();
        String sys_plugins = initInfo.getSys_plugins();
        sharesharepath = sys_plugins + "share/sdk.php?id=" + id;
        log_d("--->" + sharesharepath);
    }
    public void init() {
        long size = XtomImageCache.getInstance(ActivityMore.this).getCacheSize();
        cacheSizeView.setText(BaseUtil.transData(size / 1024.0 / 1024) + "M");
    }
    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);
        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText("设置");
        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.INVISIBLE);
        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.GONE);
        accountLy = (LinearLayout) findViewById(R.id.account_ly);
        aboutLy = (LinearLayout) findViewById(R.id.about_ly);
        adviceLy = (LinearLayout) findViewById(R.id.advice_ly);
        softwareShareLy = (LinearLayout) findViewById(R.id.software_share_ly);
        contactLayout= (LinearLayout) findViewById(R.id.contact);
        cacheLy = (FrameLayout) findViewById(R.id.cache_ly);
        logoutBtn = (Button) findViewById(R.id.logout);
        cacheSizeView = (TextView) findViewById(R.id.cache_num);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {

        hmBackBtn.setOnClickListener(this);

        accountLy.setOnClickListener(this);
        aboutLy.setOnClickListener(this);
        adviceLy.setOnClickListener(this);
        softwareShareLy.setOnClickListener(this);
        cacheLy.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        contactLayout.setOnClickListener(this);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_LOGINOUT:
                showProgressDialog(R.string.hm_hlxs_txt_34);
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
            case CLIENT_LOGINOUT:
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
            case CLIENT_LOGINOUT:
                XtomSharedPreferencesUtil.save(mContext, "username", "");
                XtomSharedPreferencesUtil.save(mContext, "password", "");
                BaseApplication.getInstance().delUser(user);
                HemaUtil.setThirdSave(mContext, false);// 将第三方登录标记为true,注意在退出登录时将其置为false
                Intent intent = new Intent();
                intent.setAction(BaseConfig.BROADCAST_ACTION);
                sendBroadcast(intent);
                finish();
                break;
            case SHARE:
                HemaArrayResult<IdInfo> valueResult = (HemaArrayResult<IdInfo>) hemaBaseResult;
                if (valueResult.getObjects() != null && valueResult.getObjects().size() > 0) {
                    IdInfo idInfo = valueResult.getObjects().get(0);
                    if (dialog == null) {
                        dialog = new ButtonDialog(ActivityMore.this);
                    }
                    dialog.setRes(R.mipmap.ic_launcher);
                    dialog.setValueText("+" + idInfo.getVoucher_value() + "代金券");
                    dialog.setText("分享成功");
                    dialog.setListener(new ButtonDialog.onConfrimListener() {
                        @Override
                        public void onConfirm() {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
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
            case CLIENT_LOGINOUT:
            case SHARE:
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
            case CLIENT_LOGINOUT:
            case SHARE:
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_left:
                finish();
                break;
            case R.id.account_ly://账户管理
                intent = new Intent(ActivityMore.this, ActivityAccountManager.class);
                startActivity(intent);
                break;
            case R.id.about_ly://关于我们
                intent = new Intent(ActivityMore.this, ActivityWebView.class);
                intent.putExtra("keytype", "1");
                intent.putExtra("keyid", "0");
                startActivity(intent);
                break;
            case R.id.advice_ly://意见反馈
                intent = new Intent(ActivityMore.this, ActivityAdvice.class);
                startActivity(intent);
                break;
            case R.id.software_share_ly://软件分享
                showShare();
                break;
            case R.id.cache_ly://清除缓存
                if (XtomImageCache.getInstance(ActivityMore.this).getCacheSize() == 0) {
                    XtomToastUtil.showShortToast(ActivityMore.this, "暂无缓存");
                    return;
                }
                new ClearTask().execute();
                break;
            case R.id.logout://注销登录
                if (logoutDialog == null) {
                    logoutDialog = new HmHelpDialog(ActivityMore.this, 0);
                }
                logoutDialog.setLeftButtonText("取消");
                logoutDialog.setRightButtonText("确定");
                logoutDialog.setTitleName("确定退出？");
                logoutDialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
                    @Override
                    public void onCancel(int type) {
                        logoutDialog.cancel();
                    }

                    @Override
                    public void onConfirm(String msg) {
                        logoutDialog.cancel();
                        getNetWorker().clientLoginout(user.getToken());
                    }
                });
                logoutDialog.show();
                break;
            case R.id.contact:
                intent=new Intent(ActivityMore.this,ContactCustomer.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

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
                    new ShareAction(ActivityMore.this)
                            .setPlatform(SHARE_MEDIA.QQ)
                            .setCallback(new OnUmShareListener())
                            .withTitle(getResources().getString(R.string.app_name))
                            .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withTargetUrl(sharesharepath)
                            .withMedia(new UMImage(ActivityMore.this, R.mipmap.ic_launcher))
                            .share();
                    break;
                case 4://qq空间
                    new ShareAction(ActivityMore.this)
                            .setPlatform(SHARE_MEDIA.QZONE)
                            .setCallback(new OnUmShareListener())
                            .withTitle(getResources().getString(R.string.app_name))
                            .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withTargetUrl(sharesharepath)
                            .withMedia(new UMImage(ActivityMore.this, R.mipmap.ic_launcher))
                            .share();
                    break;
                case 1://新浪
                    //showToast(R.mipmap.hm_icon_col_fail, "暂未开通");
//                    new ShareAction(ActivityMore.this)
//                            .setPlatform(SHARE_MEDIA.SINA)
//                            .setCallback(new OnUmShareListener())
//                            .withTitle(getResources().getString(R.string.app_name))
//                            .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
//                            .withTargetUrl(sharesharepath)
//                            .withMedia(new UMImage(ActivityMore.this, R.mipmap.hm_icon_app))
//                            .share();
                    break;
                case 2://微信
                    new ShareAction(ActivityMore.this)
                            .setPlatform(SHARE_MEDIA.WEIXIN)
                            .setCallback(new OnUmShareListener())
                            .withTitle(getResources().getString(R.string.app_name))
                            .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withTargetUrl(sharesharepath)
                            .withMedia(new UMImage(ActivityMore.this, R.mipmap.ic_launcher))
                            .share();
                    break;
                case 3://朋友圈
                    new ShareAction(ActivityMore.this)
                            .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                            .setCallback(new OnUmShareListener())
                            .withTitle(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withTargetUrl(sharesharepath)
                            .withMedia(new UMImage(ActivityMore.this, R.mipmap.ic_launcher))
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

    private class ClearTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // 删除图片缓存
            XtomImageCache.getInstance(mContext).deleteCache();
            ImageLoader.getInstance().clearDiskCache();
            ImageLoader.getInstance().clearMemoryCache();
            return null;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog("正在清除缓存");
        }

        @Override
        protected void onPostExecute(Void result) {
            cancelProgressDialog();
            showTextDialog("清除完成");
            cacheSizeView.setText(BaseUtil.transData(0) + "M");
        }

    }
}
