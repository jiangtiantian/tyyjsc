package com.hemaapp.tyyjsc;

import android.content.Intent;

import com.hemaapp.hm_FrameWork.HemaFragment;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.activity.Login;
import com.hemaapp.tyyjsc.view.BaseProgressDialog;
import com.hemaapp.tyyjsc.view.HemaImageToast;

import xtom.frame.XtomActivityManager;
import xtom.frame.net.XtomNetWorker;
public abstract class BaseFragment extends HemaFragment {
    private BaseProgressDialog progressDialog=null;
    // 提示框
    private HemaImageToast toast = null;
    @Override
    protected HemaNetWorker initNetWorker() {
        return new BaseNetWorker(getActivity());
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
                        Intent it = new Intent(getActivity(), Login.class);
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

    // ------------------------下面填充项目自定义方法---------------------------
    // 提示信息框
    public void showToast(int res, String msg) {
        if (toast == null) {
            toast = new HemaImageToast(getActivity());
        }
        toast.setImageRes(res);
        toast.setText(msg);
        toast.show();
    }

    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected abstract void lazyLoad();

    protected void onInvisible() {
    }
    public void showProgressDialog(String text) {
        if(this.progressDialog == null) {
            this.progressDialog = new BaseProgressDialog(getActivity(),R.style.dialog);
        }

        this.progressDialog.setText(text);
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
    }

    public void showProgressDialog(String text, boolean cancelable) {
        if(this.progressDialog == null) {
            this.progressDialog = new BaseProgressDialog(getActivity(),R.style.dialog);
        }

        this.progressDialog.setText(text);
        this.progressDialog.setCancelable(cancelable);
        this.progressDialog.show();
    }

    public void showProgressDialog(int text) {
        if(this.progressDialog == null) {
            this.progressDialog = new BaseProgressDialog(getActivity(),R.style.dialog);
        }

        this.progressDialog.setText(text);
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
    }

    public void showProgressDialog(int text, boolean cancelable) {
        if(this.progressDialog == null) {
            this.progressDialog = new BaseProgressDialog(getActivity(),R.style.dialog);
        }

        this.progressDialog.setText(text);
        this.progressDialog.setCancelable(cancelable);
        this.progressDialog.show();
    }

    public void cancelProgressDialog() {
        if(this.progressDialog != null) {
            this.progressDialog.setCancelable(false);
            this.progressDialog.cancel();
        }

    }
    }