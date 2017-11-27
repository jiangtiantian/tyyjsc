package com.hemaapp.tyyjsc.view;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hemaapp.tyyjsc.R;

import xtom.frame.XtomObject;

/**
 * 分享Dialog
 */

public class ShareDialog extends XtomObject implements View.OnClickListener {

    private Dialog mDialog = null;

    private Context context = null;
    private View convertView = null;
    private TextView qq_tv = null;
    private TextView zone_tv = null;
    private TextView wx_tv = null;
    private TextView wx_friend_tv = null;
    private TextView cancel = null;
    private onShareListener listener = null;

    public ShareDialog(Context context) {
        mDialog = new Dialog(context, R.style.dialog);
        convertView = LayoutInflater.from(context).inflate(
                R.layout.share_dialog, null);
        qq_tv = (TextView) convertView.findViewById(R.id.qq);
        zone_tv = (TextView) convertView.findViewById(R.id.qqzone);
        cancel = (TextView) convertView.findViewById(R.id.cancel);
        wx_tv = (TextView) convertView.findViewById(R.id.wx);
        wx_friend_tv = (TextView) convertView.findViewById(R.id.wxfriend);
        cancel.setOnClickListener(this);
        qq_tv.setOnClickListener(this);
        wx_tv.setOnClickListener(this);
        zone_tv.setOnClickListener(this);
        wx_friend_tv.setOnClickListener(this);
        //解决无法填充屏幕宽度问题并从底部向上弹出
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        Rect rect = new Rect();
        View view = window.getDecorView();
        view.getWindowVisibleDisplayFrame(rect);
        windowparams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(windowparams);

        //设置显示动画
        window.setWindowAnimations(R.style.anim);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setContentView(convertView);
    }

    public void show() {
        mDialog.show();
    }

    public void cancel() {
        mDialog.cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                cancel();
                break;
            case R.id.qq://QQ
                cancel();
                if (listener != null) {
                    listener.onShare(0);
                }
                break;
            case R.id.wb://新浪微博
                cancel();
                if (listener != null) {
                    listener.onShare(1);
                }
                break;
            case R.id.qqzone://qq空间
                cancel();
                if (listener != null) {
                    listener.onShare(4);
                }
                break;
            case R.id.wx://微信
                cancel();
                if (listener != null) {
                    listener.onShare(2);
                }
                break;
            case R.id.wxfriend://微信朋友圈
                cancel();
                if (listener != null) {
                    listener.onShare(3);
                }
                break;
            default:
                break;
        }
    }

    public interface onShareListener {
        void onShare(int which);
    }

    public void setListener(onShareListener listener) {
        this.listener = listener;
    }
}