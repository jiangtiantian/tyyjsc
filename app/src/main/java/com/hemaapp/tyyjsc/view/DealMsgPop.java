package com.hemaapp.tyyjsc.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hemaapp.tyyjsc.R;


public class DealMsgPop {

    private Context context = null;

    private PopupWindow pop = null;
    private LayoutInflater inflater = null;
    private View contentView = null;

    // 清空按钮
    private TextView clear = null;
    // 全部设为已读
    private TextView readed = null;
    // 取消按钮
    private TextView cancelBtn = null;
    // 确定监听
    private OnClickConfirmListener listener = null;

    public DealMsgPop(Context context) {
        this.context = context;
        init();
    }

    // 初始化
    public void init() {
        pop = new PopupWindow(context);
        pop.setWidth(LayoutParams.MATCH_PARENT);
        pop.setHeight(LayoutParams.MATCH_PARENT);

        inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(R.layout.pop_msg, null);

        cancelBtn = (TextView) contentView.findViewById(R.id.cancel);
        clear = (TextView) contentView.findViewById(R.id.clear_msg);
        readed = (TextView) contentView.findViewById(R.id.set_readed);

        // 清空监听
        clear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    dismiss();
                    listener.onConfirmListener();
                }
            }
        });
        // 取消监听
        cancelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        // 设置已读监听
        readed.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    dismiss();
                    listener.onRead();
                }
            }
        });
        pop.setFocusable(true);
        pop.setTouchable(true);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setAnimationStyle(R.style.anim);
        pop.setContentView(contentView);
    }

    //设置当前用户性别
    public void setDefSex(String sex) {
        if ("男".equals(sex)) {
            clear.setTextColor(context.getResources().getColor(R.color.sort_txt_p));
            readed.setTextColor(context.getResources().getColor(R.color.sex));
        } else {
            clear.setTextColor(context.getResources().getColor(R.color.sex));
            readed.setTextColor(context.getResources().getColor(R.color.sort_txt_p));
        }
    }

    // 关闭
    public void dismiss() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
        }
    }

    public void show() {
        new Handler() {
        }.postDelayed(new Runnable() {
            @Override
            public void run() {
                pop.showAtLocation(contentView, Gravity.CENTER, 0, 0);
            }
        }, 10);

    }

    // 显示
    public void showAsDropDown(View anchor) {
        pop.showAsDropDown(anchor);
    }

    // 设置第一个按钮文本
    public void setConfirmBtnText(String text) {
        clear.setText(text);
    }

    public void setConfirmBtnText(int text) {
        clear.setText(text);
    }


    // 设置取消文本
    public void setCancelBtnText(String text) {
        cancelBtn.setText(text);
    }

    public void setCancelBtnText(int text) {
        cancelBtn.setText(text);
    }

    // 设置已读文本
    public void setReadBtnText(String text) {
        readed.setText(text);
    }

    public void setReadBtnText(int text) {
        readed.setText(text);
    }

    // 当单击确定按钮时，执行的回调函数
    public interface OnClickConfirmListener {
        void onConfirmListener();

        void onRead();
    }

    // 设置监听
    public void setOnConfirmListener(OnClickConfirmListener listener) {
        this.listener = listener;
    }
}
