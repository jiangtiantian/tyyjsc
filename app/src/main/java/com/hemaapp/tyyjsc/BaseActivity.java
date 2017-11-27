package com.hemaapp.tyyjsc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.hemaapp.hm_FrameWork.HemaActivity;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.activity.Login;
import com.hemaapp.tyyjsc.view.BaseProgressDialog;
import com.hemaapp.tyyjsc.view.HemaImageToast;

import xtom.frame.XtomActivityManager;
import xtom.frame.net.XtomNetWorker;
/**
 */
public abstract class BaseActivity extends HemaActivity {
    private HemaImageToast toast = null;
    private BaseProgressDialog progressDialog=null;
    @Override
    protected HemaNetWorker initNetWorker() {
        return new BaseNetWorker(mContext);
    }

    @Override
    public BaseNetWorker getNetWorker() {
        return (BaseNetWorker) super.getNetWorker();
    }

    @Override
    public boolean onAutoLoginFailed(HemaNetWorker netWorker,
                                     HemaNetTask netTask, int failedType, HemaBaseResult baseResult) {
        switch (failedType) {
            case 0:// 服务器处理失败
                int error_code = baseResult.getError_code();
                switch (error_code) {
                    case 102:// 密码错误
                        XtomActivityManager.finishAll();
                        Intent it = new Intent(mContext, Login.class);
                        startActivity(it);
                        return true;
                    default:
                        break;
                }
            case XtomNetWorker.FAILED_HTTP:// 网络异常
            case XtomNetWorker.FAILED_DATAPARSE:// 数据异常
            case XtomNetWorker.FAILED_NONETWORK:// 无网络
                break;
        }
        return false;
    }

    // ------------------------下面填充项目自定义方法--------------------------

    /**
     * 设置状态栏颜色 * * @param activity 需要设置的activity * @param color 状态栏颜色值
     */
    public void setColor(Activity activity) {
          int color=getResources().getColor(R.color.statusbar_bg);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 5.0的全透明设置
            Window window = activity.getWindow();
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            ViewGroup mContentView = (ViewGroup) activity
                    .findViewById(Window.ID_ANDROID_CONTENT);

            // First translucent status bar.
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = getStatusBarHeight();

            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView
                        .getLayoutParams();
                // 如果已经为 ChildView 设置过了 marginTop, 再次调用时直接跳过
                if (lp != null && lp.topMargin < statusBarHeight
                        && lp.height != statusBarHeight) {
                    // 不预留系统空间
                    mChildView.setFitsSystemWindows(false);
//                    ViewCompat.setFitsSystemWindows(mChildView, false);
                    lp.topMargin += statusBarHeight;
                    mChildView.setLayoutParams(lp);
                }
            }

            View statusBarView = mContentView.getChildAt(0);
            if (statusBarView != null
                    && statusBarView.getLayoutParams() != null
                    && statusBarView.getLayoutParams().height == statusBarHeight) {
                // 避免重复调用时多次添加 View
                statusBarView.setBackgroundColor(color);
                return;
            }
            statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            statusBarView.setBackgroundColor(color);
            // 向 ContentView 中添加假 View
            mContentView.addView(statusBarView, 0, lp);
        }
    }

    public int getStatusBarHeight() {
        // 获得状态栏高度
        int resourceId = mContext.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        int statusBarHeight = mContext.getResources().getDimensionPixelSize(
                resourceId);
        return statusBarHeight;

    }

    // 关闭软键盘
    public void closeKeyboard() {

        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void openAlbum() {
    }

    // 提示信息框
    public void showToast(int res, String msg) {
        if (toast == null) {
            toast = new HemaImageToast(mContext);
        }
        toast.setImageRes(res);
        toast.setText(msg);
        toast.show();
    }
    public void showProgressDialog(String text) {
        if(this.progressDialog == null) {
            this.progressDialog = new BaseProgressDialog(this,R.style.dialog);
        }
        this.progressDialog.setText(text);
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
    }

    public void showProgressDialog(String text, boolean cancelable) {
        if(this.progressDialog == null) {
            this.progressDialog = new BaseProgressDialog(this,R.style.dialog);
        }

        this.progressDialog.setText(text);
        this.progressDialog.setCancelable(cancelable);
        this.progressDialog.show();
    }

    public void showProgressDialog(int text) {
        if(this.progressDialog == null) {
            this.progressDialog = new BaseProgressDialog(this,R.style.dialog);
        }
        this.progressDialog.setText(text);
        this.progressDialog.show();
    }


    public void cancelProgressDialog() {
        if(this.progressDialog != null) {
            this.progressDialog.dismiss();
        }

    }

}
