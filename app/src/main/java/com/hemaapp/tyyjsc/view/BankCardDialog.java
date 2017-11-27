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
import com.hemaapp.tyyjsc.view.singlewheel.LoopView;

import java.util.ArrayList;

import xtom.frame.XtomObject;

/**
 * 银行卡选择框
 */
public class BankCardDialog  extends XtomObject {

    private Dialog mDialog = null;

    private Context context = null;
    private View convertView = null;

    private LoopView loopView = null;//滚轮控件
    private ArrayList<String> data = null;

    private TextView cancel = null;
    private TextView confirm = null;
    private onSelectedItemListener listener = null;

    public BankCardDialog(Context context, final ArrayList<String> data){
        mDialog = new Dialog(context, R.style.dialog);
        convertView = LayoutInflater.from(context).inflate(
                R.layout.bank_card_dialog, null);


        loopView = (LoopView)convertView.findViewById(R.id.loop);
        cancel = (TextView)convertView.findViewById(R.id.tv_cancel);
        confirm = (TextView)convertView.findViewById(R.id.tv_sure);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                if(listener != null){
                    listener.onSelected(loopView.getSelectedItem());
                }
            }
        });
        //设置是否循环播放
        loopView.setNotLoop();
        //设置原始数据
        loopView.setItems(data);
        //设置初始位置
        loopView.setInitPosition(data.size() / 2);
        //设置字体大小
        loopView.setTextSize(20);

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
    public void setListener(onSelectedItemListener listener){
        this.listener = listener;
    }
    public interface onSelectedItemListener{
        void onSelected(int index);
    }
}
