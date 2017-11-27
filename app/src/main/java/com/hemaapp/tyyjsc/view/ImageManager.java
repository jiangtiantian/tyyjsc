package com.hemaapp.tyyjsc.view;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 16-5-22.
 */
public class ImageManager {

    private Context mContext;
    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";
    public ImageManager(Context context) {
        this.mContext = context;
    }

    // 将资源ID转为Uri
    public Uri resourceIdToUri(int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + mContext.getPackageName() + FOREWARD_SLASH + resourceId);
    }

    // 加载网络图片
    public void loadUrlImage(String url, ImageView imageView) {
        Glide.with(mContext)
                .load(url)
                .into(imageView);
    }

    // 加载drawable图片
    public void loadResImage(int resId, ImageView imageView) {
        Glide.with(mContext)
                .load(resourceIdToUri(resId))

                .into(imageView);
    }

    // 加载本地图片
    public void loadLocalImage(String path, ImageView imageView) {
        Glide.with(mContext)
                .load("file://" + path)

                .into(imageView);
    }

    // 加载网络圆型图片
    public void loadCircleImage(String url, ImageView imageView) {
        Glide.with(mContext)
                .load(url)
        /*        .transform(new GlideCircleTransform(mContext))*/
                .into(imageView);
    }

/*    // 加载drawable圆型图片
    public void loadCircleResImage(int resId, ImageView imageView) {
        Glide.with(mContext)
                .load(resourceIdToUri(resId))

                .transform(new GlideCircleTransform(mContext))
                .into(imageView);
    }

    // 加载本地圆型图片
    public void loadCircleLocalImage(String path, ImageView imageView) {
        Glide.with(mContext)
                .load("file://" + path)
                .transform(new GlideCircleTransform(mContext))
                .into(imageView);
    }*/

}
