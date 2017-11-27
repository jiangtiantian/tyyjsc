package com.hemaapp.tyyjsc;

import android.view.View;
import android.widget.ImageView;

import java.net.URL;

import xtom.frame.image.load.XtomImageTask;

/**
 * 获取头像任务
 */
public class BaseAvatarTask extends XtomImageTask {

    public BaseAvatarTask(ImageView imageView, Object context) {
        super(imageView, context);
        // TODO Auto-generated constructor stub
    }

    public BaseAvatarTask(ImageView imageView, URL url, Object context) {
        super(imageView, url, context);
        // TODO Auto-generated constructor stub
    }

    public BaseAvatarTask(ImageView imageView, String path, Object context) {
        super(imageView, path, context);
        // TODO Auto-generated constructor stub
    }

    public BaseAvatarTask(ImageView imageView, String path, Object context,
                          View fatherView) {
        super(imageView, path, context, fatherView);
        // TODO Auto-generated constructor stub
    }

    public BaseAvatarTask(ImageView imageView, URL url, Object context,
                          View fatherView) {
        super(imageView, url, context, fatherView);
        // TODO Auto-generated constructor stub
    }

    public BaseAvatarTask(ImageView imageView, String path, Object context,
                          Size size) {
        super(imageView, path, context, size);
        // TODO Auto-generated constructor stub
    }

    public BaseAvatarTask(ImageView imageView, URL url, Object context,
                          Size size) {
        super(imageView, url, context, size);
        // TODO Auto-generated constructor stub
    }

    public BaseAvatarTask(ImageView imageView, String path, Object context,
                          View fatherView, Size size) {
        super(imageView, path, context, fatherView, size);
        // TODO Auto-generated constructor stub
    }

    public BaseAvatarTask(ImageView imageView, URL url, Object context,
                          View fatherView, Size size) {
        super(imageView, url, context, fatherView, size);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void failed() {
        //imageView.setImageResource();
    }

    @Override
    public void beforeload() {
        //imageView.setImageResource(R.mipmap.ic_launcher);
    }

}
