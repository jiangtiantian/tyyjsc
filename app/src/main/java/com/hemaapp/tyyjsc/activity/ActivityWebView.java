package com.hemaapp.tyyjsc.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.SysInitInfo;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.MyWebView;
import com.hemaapp.tyyjsc.view.ShareDialog;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import xtom.frame.util.XtomToastUtil;

import static com.hemaapp.tyyjsc.R.id.webview;

/**
 * 所有WebView显示界面
 */
public class ActivityWebView extends BaseActivity implements View.OnClickListener {

    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧文字按钮

    private MyWebView webView = null;

    private LinearLayout bottomLayout = null;
    private Button invitateBtn = null;
    /**
     * keytype=1 	 protocal注册协议
     * keytype=2 	instructions使用说明
     * keytype=3 	 vouchers代金券使用规则
     * keytype=4 	 signway签到攻略
     * keytype=5 	ad广告的图文详情webview/parm/ad/id/广告id
     *
     */
    private String keytype = "";
    private String keyid = "";
    private String sharepath = "";
    private String sharesharepath = "";

    private ShareDialog shareDialog = null;
    private SysInitInfo sysInitInfo = null;
    private String invite = "";

    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_web_view);
        super.onCreate(savedInstanceState);
        invite = getString(R.string.hm_hlxs_txt_132);
        setColor(mContext);
        sysInitInfo = BaseApplication.getInstance().getSysInitInfo();
        user = BaseApplication.getInstance().getUser();
        switch (keytype)
        {
            case "1":
                sharepath = BaseApplication.getInstance().getSysInitInfo().getSys_web_service() + "webview/parm/protocal";
                break;
            case "2":
                sharepath = BaseApplication.getInstance().getSysInitInfo().getSys_web_service() + "webview/parm/instructions";
                break;
            case "3":
                sharepath = BaseApplication.getInstance().getSysInitInfo().getSys_web_service() + "webview/parm/vouchers";
                break;
            case "4":
                sharepath = BaseApplication.getInstance().getSysInitInfo().getSys_web_service() + "webview/parm/signway";
                break;
            case "5":
                sharepath = BaseApplication.getInstance().getSysInitInfo().getSys_web_service() + "webview/parm/ad/id/" + keyid;
                break;
        }

        log_d("---" + sharepath);
        webView.loadUrl(sharepath);
        final String id = "0";
        SysInitInfo initInfo = BaseApplication.getInstance().getSysInitInfo();
        String sys_plugins = initInfo.getSys_plugins();
        sharesharepath = sys_plugins + "share/sdk.php?id=" + id;
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
        dialog.setMessage(getString(R.string.hm_hlxs_txt_121));
        Config.dialog = dialog;

        if (shareDialog == null) {
            shareDialog = new ShareDialog(mContext);
        }
        shareDialog.setListener(new onClickPlatformShareListener());
        shareDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (user == null) {
            return;
        }
        showShare();
    }

    //各平台下的分享
    class onClickPlatformShareListener implements ShareDialog.onShareListener {

        @Override
        public void onShare(int which) {
            switch (which) {
                case 0://QQ
//                    showToast(R.mipmap.hm_icon_col_fail, getString(R.string.hm_hlxs_txt_122));
//                    new ShareAction(ActivityWebView.this)
//                            .setPlatform(SHARE_MEDIA.QQ)
//                            .setCallback(new OnUmShareListener())
//                            .withTitle(getResources().getString(R.string.app_name))
//                            .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
//                           .withTargetUrl(isNull(user.getDownload()) ? sharepath : user.getDownload())
//                            .withMedia(new UMImage(ActivityWebView.this, R.mipmap.hm_icon_app))
//                            .share();
                    break;
                case 1://新浪
//                    showToast(R.mipmap.hm_icon_col_fail, getString(R.string.hm_hlxs_txt_122));
//                new ShareAction(ActivityWebView.this)
//                            .setPlatform(SHARE_MEDIA.SINA)
//                            .setCallback(new OnUmShareListener())
//                            .withTitle(getResources().getString(R.string.app_name))
//                            .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
//                            .withTargetUrl(isNull(user.getDownload()) ? sharepath : user.getDownload())
//                            .withMedia(new UMImage(ActivityWebView.this, R.mipmap.hm_icon_app))
//                            .share();
                    break;
                case 2://微信
                    new ShareAction(ActivityWebView.this)
                            .setPlatform(SHARE_MEDIA.WEIXIN)
                            .setCallback(new OnUmShareListener())
                            .withTitle(getResources().getString(R.string.app_name))
                            .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withTargetUrl(isNull(user.getDownload()) ? sharepath : user.getDownload())
                            .withMedia(new UMImage(ActivityWebView.this, R.mipmap.ic_launcher))
                            .share();
                    break;
                case 3://朋友圈
                    new ShareAction(ActivityWebView.this)
                            .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                            .setCallback(new OnUmShareListener())
                            .withTitle(getResources().getString(R.string.app_name))
                            .withText(sysInitInfo == null ? invite : (isNull(sysInitInfo.getMsg_invite()) ? invite : sysInitInfo.getMsg_invite()))
                            .withTargetUrl(isNull(user.getDownload()) ? sharepath : user.getDownload())
                            .withMedia(new UMImage(ActivityWebView.this, R.mipmap.ic_launcher))
                            .share();
                    break;
            }
        }
    }

    //友盟分享回调
    class OnUmShareListener implements UMShareListener {

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            hmBarNameView.post(new Runnable() {
                @Override
                public void run() {
                    XtomToastUtil.showShortToast(mContext, getString(R.string.hm_hlxs_txt_123));
                }
            });
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            hmBarNameView.post(new Runnable() {
                @Override
                public void run() {
                    XtomToastUtil.showShortToast(mContext, getString(R.string.hm_hlxs_txt_124));
                }
            });
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            hmBarNameView.post(new Runnable() {
                @Override
                public void run() {
                    XtomToastUtil.showShortToast(mContext, getString(R.string.hm_hlxs_txt_125));
                }
            });
        }
    }
    /* 分享相关end */

    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);

        hmBarNameView = (TextView) findViewById(R.id.bar_name);

        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.INVISIBLE);
        hmRightBtn.setImageResource(R.mipmap.ic_launcher);
        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.GONE);

        webView = (MyWebView) findViewById(webview);
        bottomLayout = (LinearLayout) findViewById(R.id.bottomlayout);
        invitateBtn = (Button) findViewById(R.id.invite);
    }

    @Override
    protected void getExras() {
        keytype = getIntent().getStringExtra("keytype");
        keyid = getIntent().getStringExtra("keyid");
    }

    @Override
    protected void setListener() {
        hmBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if ("1".equals(keytype)) {
            hmBarNameView.setText("注册协议");
        } else if ("2".equals(keytype)) {
            hmBarNameView.setText("使用说明");
        } else if ("3".equals(keytype)) {
            hmBarNameView.setText("代金券使用规则");
        } else if ("4".equals(keytype)) {
            hmBarNameView.setText("签到攻略");
        } else if ("5".equals(keytype)) {
            hmBarNameView.setText("图文详情");
        } else if ("6".equals(keytype)) {
            hmBarNameView.setText(R.string.hm_hlxs_txt_131);
        }
        if ("6".equals(keytype)) {//邀请好友
            bottomLayout.setVisibility(View.VISIBLE);
        } else {
            bottomLayout.setVisibility(View.GONE);
        }
        invitateBtn.setOnClickListener(this);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }
}
