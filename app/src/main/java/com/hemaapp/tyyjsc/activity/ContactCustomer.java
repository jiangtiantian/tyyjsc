package com.hemaapp.tyyjsc.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.SignValueInfo;
import com.hemaapp.tyyjsc.model.SysInitInfo;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.HmHelpDialog;

/**
 * 签到
 */
public class ContactCustomer extends BaseActivity implements View.OnClickListener {
    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private TextView hmRightTxtView = null;//标题栏右侧文字按钮
    private LinearLayout phoneln, qqln;
    private User user = null;//用户个人信息
    private SignValueInfo signValueInfo = null;
    private HmHelpDialog exitDialog;
    private SysInitInfo sysInitInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.customer_ln);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        sysInitInfo = BaseApplication.getInstance().getSysInitInfo();
    }

    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);
        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText("联系客服");
        phoneln = (LinearLayout) findViewById(R.id.contact_phone);
        qqln = (LinearLayout) findViewById(R.id.contact_qq);
        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.GONE);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        hmBackBtn.setOnClickListener(this);
        phoneln.setOnClickListener(this);
        qqln.setOnClickListener(this);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
            case USER_SIGN:
            case SIGN_VALUE_GET:
                showProgressDialog(R.string.hm_hlxs_txt_11);
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
            case CLIENT_GET:
            case USER_SIGN:
            case SIGN_VALUE_GET:
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
            case CLIENT_GET:
                HemaArrayResult<User> infoResult = (HemaArrayResult<User>) hemaBaseResult;
                user = infoResult.getObjects().get(0);
                break;
            case SIGN_VALUE_GET:
                showTextDialog("签到成功");
                HemaArrayResult<SignValueInfo> signValueResult = (HemaArrayResult<SignValueInfo>) hemaBaseResult;
                signValueInfo = signValueResult.getObjects().get(0);
//                init();
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
            case CLIENT_GET:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            case SIGN_VALUE_GET:
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
            case CLIENT_GET:
                break;
            case USER_SIGN:
                break;
            case SIGN_VALUE_GET:
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_left:
                finish();
                break;
            case R.id.contact_phone:
                if (exitDialog == null) {
                    exitDialog = new HmHelpDialog(ContactCustomer.this, 0);
                }
                exitDialog.setLeftButtonText("取消");
                exitDialog.setRightButtonText("确定");
                exitDialog.setTitleName(sysInitInfo == null ? "未知号码" : sysInitInfo.getSys_service_phone());
                exitDialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
                    @Override
                    public void onCancel(int type) {
                        exitDialog.cancel();
                    }

                    @Override
                    public void onConfirm(String msg) {
                        if (sysInitInfo != null) {
                            exitDialog.cancel();
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + sysInitInfo.getSys_service_phone()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    }
                });
                exitDialog.show();
                break;
            case R.id.contact_qq:
                String qqNum = sysInitInfo.getSys_service_qq();
                if (checkApkExist(this, "com.tencent.mobileqq")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum + "&version=1")));
                } else {
                    Toast.makeText(this, "本机未安装QQ应用", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
}


