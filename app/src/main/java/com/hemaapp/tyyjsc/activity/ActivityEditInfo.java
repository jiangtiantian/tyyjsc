package com.hemaapp.tyyjsc.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseNetWorker;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.HmHelpDialog;
import com.hemaapp.tyyjsc.view.ImageWay;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;

import xtom.frame.XtomActivityManager;
import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomImageUtil;
import xtom.frame.util.XtomSharedPreferencesUtil;
/**
 * 编辑个人信息
 */
public class ActivityEditInfo extends BaseActivity implements View.OnClickListener {
    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧文字按钮
    private RelativeLayout logoRl = null;//头像选择
    private RoundedImageView logo = null;
    private LinearLayout nickNameLl = null;//昵称选择
    private EditText nickNameEt = null;
    private LinearLayout sexLl = null;//性别
    private TextView sexView = null;
    private HmHelpDialog sexDialog = null;
    public ImageWay imageWay;// 图片路径
    private String imagePathCamera;
    private String tempPath;
    private String compressPath;//压缩之后的路径
    private String username = null;// 用户名
    private String temToken = null;//临时令牌
    private String password = null;//密码
    private String mobile = null;//邀请人手机号
    private String userSex = null;//性别
    private String keytype = "";//0 设置登录密码（设置密码界面） | 完善个人信息（个人信息界面） 1 设置支付密码 2 编辑用户信息
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_info);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        user = BaseApplication.getInstance().getUser();
        if (savedInstanceState == null) {
            imageWay = new ImageWay(mContext, 1, 2);
        } else {
            imagePathCamera = savedInstanceState.getString("imagePathCamera");
            imageWay = new ImageWay(mContext, 1, 2);
        }
        if("2".equals(keytype))
        {
            initUserInfor();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case 1:// 相册选择图片
                album(data);
                break;
            case 2:// 拍照
                camera();
                break;
            case 3:// 裁剪
                new CompressPicTask().execute(tempPath);
                break;
        }
    }

    private void camera() {

        if (imagePathCamera == null) {
            imagePathCamera = imageWay.getCameraImage();
        }
        editImage(imagePathCamera, 3);
    }

    private void editImage(String path, int requestCode) {
        File file = new File(path);
        startPhotoZoom(Uri.fromFile(file), requestCode);
    }

    private void album(Intent data) {
        if (data == null)
            return;
        Uri selectedImageUri = data.getData();
        startPhotoZoom(selectedImageUri, 3);
    }

    private void startPhotoZoom(Uri uri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", BaseConfig.IMAGE_WIDTH);
        intent.putExtra("aspectY", BaseConfig.IMAGE_WIDTH);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", BaseConfig.IMAGE_WIDTH);
        intent.putExtra("outputY", BaseConfig.IMAGE_WIDTH);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, requestCode);
    }

    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }

    private File getTempFile() {
        String savedir = XtomFileUtil.getTempFileDir(ActivityEditInfo.this);
        File dir = new File(savedir);
        if (!dir.exists())
            dir.mkdirs();
        // 保存入sdCard
        tempPath = savedir + XtomBaseUtil.getFileName() + ".jpg";// 保存路径
        File file = new File(tempPath);
        try {
            file.createNewFile();
        } catch (IOException e) {
        }
        return file;
    }

    //显示个人信息
    private void initUserInfor() {
        // 头像
        String path = user.getAvatar();
        ImageLoader.getInstance().displayImage(path, logo, BaseUtil.displayImageOption());
        // 昵称
        nickNameEt.setText(user.getNickname());
        nickNameEt.setSelection(nickNameEt.getText().length());
        // 性别
        if (!isNull(user.getSex())) {
            userSex = user.getSex();
            sexView.setText(userSex);
        }
    }

    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);

        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText(R.string.hm_hlxs_txt_205);

        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.INVISIBLE);

        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.VISIBLE);
        hmRightTxtView.setText(R.string.hm_hlxs_txt_31);

        logoRl = (RelativeLayout) findViewById(R.id.logo_rl);
        logo = (RoundedImageView) findViewById(R.id.logo);
        logo.setCornerRadius(1000);
        nickNameLl = (LinearLayout) findViewById(R.id.nick_ll);
        nickNameEt = (EditText) findViewById(R.id.nickname);
        nickNameEt.setSelection(nickNameEt.getText().length());

        sexLl = (LinearLayout) findViewById(R.id.sex_ll);
        sexView = (TextView) findViewById(R.id.sex);

    }

    @Override
    protected void getExras() {
        username = getIntent().getStringExtra("username");
        temToken = getIntent().getStringExtra("temptoken");
        password = getIntent().getStringExtra("password");
        keytype = getIntent().getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        if ("0".equals(keytype)) {
            hmBarNameView.setText(R.string.hm_hlxs_txt_204);
        } else if ("2".equals(keytype)) {
            hmBarNameView.setText(R.string.hm_hlxs_txt_205);
        }
        hmBackBtn.setOnClickListener(this);
        sexLl.setOnClickListener(this);
        logoRl.setOnClickListener(this);
        hmRightTxtView.setOnClickListener(this);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_ADD:
            case FILE_UPLOAD:
            case CLIENT_LOGIN:
            case CLIENT_GET:
            case THIRD_SAVE:
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
            case CLIENT_ADD:
            case FILE_UPLOAD:
            case CLIENT_LOGIN:
            case CLIENT_GET:
            case THIRD_SAVE:
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
            case CLIENT_ADD:
                @SuppressWarnings("unchecked")
                HemaArrayResult<String> result = (HemaArrayResult<String>) hemaBaseResult;
                String token = result.getObjects().get(0);
                if (!isNull(tempPath)) {
                    getNetWorker().fileUpload(token, "1", "0", "0", "0", "无",
                            tempPath);
                } else {
                  showTextDialog(getString(R.string.hm_hlxs_txt_196));
                    hmBarNameView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //注册成功后，由于本地尚存储用户名和密码需要调用带参数的登录方法，否则提示username不能为空
                            BaseNetWorker netWorker = getNetWorker();
                            netWorker.clientLogin(username, password);
                        }
                    }, 500);
                }
                break;
            case CLIENT_SAVE:
                if (!isNull(compressPath) && user != null) {
                    getNetWorker().fileUpload(user.getToken(), "1", "0", "0", "0", "无",
                            compressPath);
                } else {
//                    BaseNetWorker netWorker = getNetWorker();
//                    if (HemaUtil.isThirdSave(mContext)) {
//                        netWorker.thirdSave();
//                    } else {
//                        netWorker.clientLogin();
//                    }
                    Intent clientIntent = new Intent();
                    clientIntent.setAction("com.hemaapp.edit.info");
                    sendBroadcast(clientIntent);
                    showTextDialog(getString(R.string.hm_hlxs_txt_197));
                    hmBarNameView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },500);
                }
                break;
            case CLIENT_GET:
                HemaArrayResult<User> infoResult = (HemaArrayResult<User>) hemaBaseResult;
                user = infoResult.getObjects().get(0);
                BaseApplication.getInstance().setUser(user);
                initUserInfor();
                break;
            case FILE_UPLOAD:
                if ("0".equals(keytype)) {
                    showTextDialog(getString(R.string.hm_hlxs_txt_196));
                    hmBarNameView.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // 用户登录
                            BaseNetWorker netWorker = getNetWorker();
                            netWorker.clientLogin(username, password);
                        }
                    }, 500);
                } else if ("2".equals(keytype)) {
                    BaseNetWorker netWorker = getNetWorker();
                    if (HemaUtil.isThirdSave(mContext)) {
                        netWorker.thirdSave();
                    } else {
                        netWorker.clientLogin();
                    }
                }
                break;
            case THIRD_SAVE:
            case CLIENT_LOGIN:
                @SuppressWarnings("unchecked")
                HemaArrayResult<User> uResult = (HemaArrayResult<User>) hemaBaseResult;
                User u = uResult.getObjects().get(0);
                BaseApplication.getInstance().setUser(u);
                XtomSharedPreferencesUtil.save(mContext, "username", hemaNetTask
                        .getParams().get("username"));
                XtomSharedPreferencesUtil.save(mContext, "password", hemaNetTask
                        .getParams().get("password"));
                //自动登录
                XtomSharedPreferencesUtil.save(mContext, "isAutoLogin", "true");
                Activity activityLogin = XtomActivityManager.getActivityByClass(Login.class);
                Activity activityReg = XtomActivityManager.getActivityByClass(ActivityReg.class);
                Activity activityRegNext = XtomActivityManager.getActivityByClass(ActivityRegNext.class);
                if (activityRegNext != null) {
                    activityRegNext.finish();
                }
                if (activityReg != null) {
                    activityReg.finish();
                }
                if (activityLogin != null) {
                    activityLogin.finish();
                }
                Intent intent = new Intent();
                intent.setAction(BaseConfig.BROADCAST_ACTION);
                sendBroadcast(intent);

                //发送广播，保存设备信息并与cid绑定
                Intent clientIntent = new Intent();
                clientIntent.setAction("com.hemaapp.push.connect");
                sendBroadcast(clientIntent);
                if ("0".equals(keytype)) {
                    finish();
                } else if ("2".equals(keytype)) {
                    showTextDialog(getString(R.string.hm_hlxs_txt_197));
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
            case CLIENT_ADD:
            case FILE_UPLOAD:
            case CLIENT_LOGIN:
            case CLIENT_GET:
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
            case CLIENT_ADD:
            case FILE_UPLOAD:
            case CLIENT_LOGIN:
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
            case R.id.sex_ll://选择性别
                if (sexDialog == null) {
                    sexDialog = new HmHelpDialog(ActivityEditInfo.this, 3);
                }
                sexDialog.setLeftButtonText(getString(R.string.hm_hlxs_txt_101));
                sexDialog.setRightButtonText(getString(R.string.hm_hlxs_txt_102));
                sexDialog.setCurSex(isNull(userSex) ? getString(R.string.hm_hlxs_txt_198) : userSex);
                sexDialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
                    @Override
                    public void onCancel(int type) {
                        sexDialog.cancel();
                    }

                    @Override
                    public void onConfirm(String msg) {
                        sexDialog.cancel();
                        userSex = msg;
                        sexView.setText(userSex);
                    }
                });
                sexDialog.show();
                break;
            case R.id.logo_rl://上传头像
                imageWay.show();
                break;
            case R.id.bar_right_txt:
                String nickName = nickNameEt.getText().toString().trim();
                if (isNull(nickName)) {
                    showTextDialog(getString(R.string.hm_hlxs_txt_199));
                    return;
                }
                userSex = sexView.getText().toString().trim();
                if (isNull(userSex)) {
                    showTextDialog(getString(R.string.hm_hlxs_txt_200));
                    return;
                }
                if ("0".equals(keytype)) {//完善个人信息
                    getNetWorker().clientAdd(temToken, username, password, nickName,
                            userSex);
                } else if ("2".equals(keytype)) {//编辑个人信息
                    if (user != null) {
                        getNetWorker().clientSave(user.getToken(), nickName, userSex);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 压缩图片
     */
    private class CompressPicTask extends AsyncTask<String, Void, Integer> {


        @Override
        protected Integer doInBackground(String... params) {
            try {
                deleteTempFile();
                String path = params[0];
                String savedir = XtomFileUtil.getTempFileDir(mContext);
                compressPath = XtomImageUtil.compressPictureWithSaveDir(path,
                        BaseConfig.IMAGE_HEIGHT, BaseConfig.IMAGE_WIDTH,
                        BaseConfig.IMAGE_QUALITY, savedir, mContext);
                return 0;
            } catch (IOException e) {
                return 1;
            }
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog(getString(R.string.hm_hlxs_txt_201));
        }

        @Override
        protected void onPostExecute(Integer result) {
            cancelProgressDialog();
            switch (result) {
                case 0:
                    cancelProgressDialog();
                    ImageLoader.getInstance().displayImage("file://" + compressPath, logo, BaseUtil.displayImageOption());
                    break;
                case 1:
                    showTextDialog(getString(R.string.hm_hlxs_txt_202));
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteTempFile();
    }

    public void deleteTempFile() {
        //删除压缩的文件
        if (!isNull(compressPath)) {
            File file = new File(compressPath);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
