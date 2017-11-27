package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.Comment;
import com.hemaapp.tyyjsc.model.HM_ImgInfo;
import com.hemaapp.tyyjsc.showlargepic.ShowLargePicActivity;
import com.hemaapp.tyyjsc.view.CusImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 商品详情的图片轮播
 */
public class ImgViewPagerAdapter extends PagerAdapter implements OnClickListener {
    protected Context mContext;
    private View view;
    private RadioGroup mIndicator;
    private ArrayList<HM_ImgInfo> infors;

    private int size;

    public ImgViewPagerAdapter(Context mContext,
                               ArrayList<HM_ImgInfo> infors, View view) {
        this.mContext = mContext;
        this.infors = infors;
        this.view = view;
        init();
    }

    public void setInfors(ArrayList<HM_ImgInfo> infors) {
        this.infors = infors;
        getCount();
        init();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private void init() {

        size = ((BitmapDrawable) mContext.getResources().getDrawable(
                R.mipmap.dot_no)).getBitmap().getWidth();
        mIndicator = (RadioGroup) view.findViewById(R.id.radiogroup);
        mIndicator.removeAllViews();
        if (infors.size() > 1) {
            mIndicator.setVisibility(View.VISIBLE);
            for (int i = 0; i < infors.size(); i++) {
                RadioButton button = new RadioButton(mContext);
                button.setButtonDrawable(R.drawable.indicator_show);
                button.setId(i);
                button.setClickable(false);
                LayoutParams params2 = new LayoutParams(
                        (i == infors.size() - 1) ? size : size * 2, size);
                button.setLayoutParams(params2);
                if (i == 0)
                    button.setChecked(true);
                mIndicator.addView(button);
            }
        } else {
            mIndicator.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        init();
        getCount();
        super.notifyDataSetChanged();
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View mView;
        mView = LayoutInflater.from(mContext).inflate(
                R.layout.viewpager_imageview, null);
        CusImageView imageView = (CusImageView) mView.findViewById(R.id.imageview);
        imageView.setRatio(1f);
        if (isEmpty()) {
            imageView.setImageResource(R.mipmap.hm_banner_def);
        } else {
            int index = position % infors.size();
            if (infors.get(index).getImgurl() == null
                    || "".equals(infors.get(index).getImgurl())
                    || "null".equals(infors.get(index).getImgurl())) {
                imageView.setImageResource(R.mipmap.hm_banner_def);
            } else{
                ImageLoader.getInstance().displayImage(infors.get(index).getImgurl(), imageView, BaseUtil.displayImageOption(R.mipmap.hm_banner_def));
            }
            mView.setTag(R.id.TAG, infors.get(index));
            mView.setOnClickListener(this);
            mView.setTag(R.id.button, index);
        }
        container.addView(mView);
        return mView;
    }

    @Override
    public int getCount() {
        return infors == null || infors.size() <= 1 ? infors.size() : Integer.MAX_VALUE;
    }
    public int getBannerSize(){
        return infors == null || infors.size() == 0 ? 1 : infors.size();
    }

    private boolean isEmpty() {
        return infors == null || infors.size() == 0;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public ViewGroup getIndicator() {
        return mIndicator;
    }

    @Override
    public void onClick(View v) {
        Comment comment = new Comment(infors);
        ArrayList<HM_ImgInfo> imgs = comment.getImgs();
        if(imgs == null || (imgs.size() == 1 && ("".equals(imgs.get(0).getImgurl()) || "null".equals(imgs.get(0).getImgurl())))){//默认图片，禁止查看大图
            return;
        }
        Intent it = new Intent(mContext, ShowLargePicActivity.class);
        it.putExtra("position", (int) v.getTag(R.id.button));
        it.putExtra("comment", comment);
        mContext.startActivity(it);
    }

}
