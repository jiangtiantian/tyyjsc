package com.hemaapp.tyyjsc.view;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.tyyjsc.R;

import xtom.frame.XtomObject;

/**
 * 提示框
 */
public class ButtonDialog extends XtomObject {
    private Dialog mDialog = null;

    private ImageView mImageView = null;
    private TextView mTextView = null;
    private TextView mTextValue = null;

    private Button confirmBtn = null;
    private onConfrimListener listener;//点击确定回调

    public ButtonDialog(Context context) {
        mDialog = new Dialog(context, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_tip, null);

        mImageView = (ImageView) view.findViewById(R.id.img);
        mTextView = (TextView) view.findViewById(R.id.tip);
        mTextValue = (TextView) view.findViewById(R.id.value);

        confirmBtn = (Button) view.findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
               if(listener != null){
                   listener.onConfirm();
               }
            }
        });
        mDialog.setCancelable(false);
        mDialog.setContentView(view);
    }
    //设置文本
    public void setText(String text) {
        mTextView.setText(text);
    }
    public void setText(int textID) {
        mTextView.setText(textID);
    }
    //设置隐藏显示
    public void setTextVisivle(){
        mTextView.setVisibility(View.VISIBLE);
    }
    public void setTextInVisivle(){
        mTextView.setVisibility(View.GONE);
    }

    public void setValueText(String text) {
        mTextValue.setText(text);
    }
    public void setValueText(int textID) {
        mTextValue.setText(textID);
    }
    public void setValueTextVisible(int visible){
        mTextValue.setVisibility(visible);
    }
    public void setmTextValueSize(){
        mTextValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
    }

    public void setRes(int resId){
        mImageView.setImageResource(resId);
    }
    public void show() {
        mDialog.show();
    }

    public void cancel() {
        mDialog.cancel();
    }

    public void setListener(onConfrimListener listener){
        this.listener = listener;
    }
    public interface onConfrimListener{
        void onConfirm();
    }
}
