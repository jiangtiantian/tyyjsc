package com.hemaapp.tyyjsc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


public class CusImageView extends ImageView {

    private float ratio =1.78f;

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public CusImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CusImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CusImageView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
                - getPaddingRight();

        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingLeft()
                - getPaddingRight();

        if (widthMode == MeasureSpec.EXACTLY
                && heightMode != MeasureSpec.EXACTLY && ratio != 0.0f) {

            height = (int) (width / ratio + 0.5f);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                    MeasureSpec.EXACTLY);
        } else if (widthMode != MeasureSpec.EXACTLY
                && heightMode == MeasureSpec.EXACTLY && ratio != 0.0f) {

            width = (int) (height * ratio + 0.5f);

            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
                    MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
