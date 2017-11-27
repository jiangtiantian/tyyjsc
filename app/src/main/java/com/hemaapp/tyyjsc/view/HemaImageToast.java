package com.hemaapp.tyyjsc.view;


import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.tyyjsc.R;


/**
 * 提示框
 */
public class HemaImageToast {

    private Toast toast = null;

    private ImageView imgView = null;
    private TextView txtView = null;

    private Context context = null;

    public HemaImageToast(Context context) {

        this.context = context;

        View view = LayoutInflater.from(context).inflate(R.layout.toast_image, null);

        imgView = (ImageView) view.findViewById(R.id.image);
        txtView = (TextView) view.findViewById(R.id.textview);

        toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);

    }

    public void setImageRes(int resId) {
        imgView.setImageResource(resId);
    }

    public void setText(String text) {
        txtView.setText(text);
    }

    public void setText(int txtId) {
        txtView.setText(txtId);
    }

    public void show() {

        WindowManager wManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wManager.getDefaultDisplay().getMetrics(dm);
        toast.setGravity(Gravity.BOTTOM, 0, dm.heightPixels / 4);
        toast.show();
    }

    public void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
