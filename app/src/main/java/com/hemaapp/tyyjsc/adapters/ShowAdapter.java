package com.hemaapp.tyyjsc.adapters;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivityGuide;

import xtom.frame.XtomActivity;

/**
 * 引导页适配器
 */
public class ShowAdapter extends PagerAdapter implements View.OnClickListener{
    private ActivityGuide mContext;
    private int[] imgs;
    private View view;
    private RadioGroup mIndicator;
    private int size;


    public ShowAdapter(Context mContext, int[] imgs, View view) {
        this.mContext = (ActivityGuide) mContext;
        this.imgs = imgs;
        this.view = view;
        init();
    }

    private void init() {
        size = ((BitmapDrawable) mContext.getResources().getDrawable(
                R.mipmap.hm_lsw_icon_indicator_p)).getBitmap().getWidth();
        mIndicator = (RadioGroup) view.findViewById(R.id.radiogroup);
        mIndicator.removeAllViews();
        if (getCount() > 1) {
            for (int i = 0; i < getCount(); i++) {
                RadioButton button = new RadioButton(mContext);
                button.setButtonDrawable(R.drawable.indicator_show);
                button.setId(i);
                button.setClickable(false);
                LayoutParams params2 = new LayoutParams(
                        (i == getCount() - 1) ? size : size * 2, size);
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
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @SuppressWarnings("deprecation")
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View mView;
        if (container.getChildAt(position) == null) {
            mView = new ImageView(mContext);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            mView.setLayoutParams(params);
            mView.setBackgroundResource(imgs[position]);
            container.addView(mView, position);
        } else
            mView = container.getChildAt(position);
        mView.setTag(position);
        mView.setOnClickListener(this);
        return mView;
    }

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public RadioGroup getmIndicator() {
        return mIndicator;
    }

    @Override
    public void onClick(View v) {
        int position = (int)v.getTag();
        if(imgs.length - 1== position){
            mContext.checkLogin();
        }
    }
}
