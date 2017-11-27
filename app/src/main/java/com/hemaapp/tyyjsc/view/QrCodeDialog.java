package com.hemaapp.tyyjsc.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by zuozhongqian on 2017/8/7.
 */

public class QrCodeDialog extends Dialog {
    public QrCodeDialog(@NonNull Context context) {
        super(context);
    }

    public QrCodeDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }
    public static class Builder {
        private Context context;
        private Bitmap image;

        public Builder(Context context) {
            this.context = context;
        }

        public Bitmap getImage() {
            return image;
        }

        public void setImage(Bitmap image) {
            this.image = image;
        }

        public QrCodeDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final QrCodeDialog dialog = new QrCodeDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.item_qrcode, null);
            dialog.addContentView(layout, new LinearLayout.LayoutParams(
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                    , android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setContentView(layout);
            ImageView img = (ImageView)layout.findViewById(R.id.iv_qrcode);
            ImageLoader.getInstance().displayImage(BaseApplication.getInstance().getUser().getQrcode(),img);

            return dialog;
        }

    }


}
