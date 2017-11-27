package com.hemaapp.tyyjsc.view;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.hemaapp.tyyjsc.R;


/**
 * 提示文本框
 */
public class CheckCardDialog implements View.OnClickListener {

    private Dialog mDiglog = null;
    private Context context = null;//上下文

    private View viewConvert = null;//对话框布局

    private EditText cardNoET = null;
    private EditText cardCodeET = null;

    //公用部分
    private Button cancelBtn = null;
    private Button confirmBtn = null;//左右侧按钮
    private OnCancelOrConfirmListener listener = null;


    public CheckCardDialog(Context context) {
        this.context = context;

        viewConvert = LayoutInflater.from(context).inflate(R.layout.check_card_dialog, null);
        mDiglog = new Dialog(context, R.style.dialog);


        cardNoET = (EditText) viewConvert.findViewById(R.id.card_no);
        cardCodeET = (EditText) viewConvert.findViewById(R.id.card_code);

        cancelBtn = (Button) viewConvert.findViewById(R.id.cancle);
        confirmBtn = (Button) viewConvert.findViewById(R.id.confirm);

        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

        mDiglog.setCanceledOnTouchOutside(true);
        mDiglog.setCancelable(true);
        Window window = mDiglog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        Rect rect = new Rect();
        View view = window.getDecorView();
        view.getWindowVisibleDisplayFrame(rect);
        windowparams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(windowparams);

        mDiglog.setContentView(viewConvert);
    }

    //左侧按钮提示文字
    public void setLeftButtonText(String txt) {
        cancelBtn.setText(txt);
    }

    public void setLeftButtonText(int resId) {
        cancelBtn.setText(resId);
    }

    //右侧按钮显示文字
    public void setRightButtonText(String txt) {
        confirmBtn.setText(txt);
    }

    public void setRightButtonText(int resId) {
        confirmBtn.setText(resId);
    }

    //重置输入框
    public void clearET() {
        cardNoET.setText("");
        cardCodeET.setText("");
    }

    public void show() {
        mDiglog.show();
    }

    public void cancel() {
        mDiglog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle:
                clearET();
                cancel();
                break;
            case R.id.confirm:
                if (listener != null) {
                    listener.onConfirm(getCardNo(), getCardCode());
                }
                break;
            default:
                break;
        }
    }

    public String getCardNo() {
        return cardNoET.getText().toString().trim();
    }

    public String getCardCode() {
        return cardCodeET.getText().toString().trim();
    }

    public void setListener(OnCancelOrConfirmListener listener) {
        this.listener = listener;
    }

    public interface OnCancelOrConfirmListener {
        void onConfirm(String no, String code);
    }
}
