package com.hemaapp.tyyjsc.view;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

public class PopLinearLayout extends LinearLayout {
    private static final String TAG = "CustomShareBoard";
    private Context context;

    public PopLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    public PopLinearLayout(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isOffset()) {
            if (this.getPaddingBottom() != 0) {
                this.setPadding(0, 0, 0, 0);
            }
        } else {
            this.setPadding(0, 0, 0, getNavigationBarHeight());
            invalidate();
        }
        super.onLayout(true, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    //计算华为手机底部导航栏高度
    private int getNavigationBarHeight() {
        Resources resources = this.context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public boolean isOffset() {
        return getDecorViewHeight() > getScreenHeight();
    }


    public int getDecorViewHeight() {
        return ((Activity) this.context).getWindow().getDecorView().getHeight();
    }

    public int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) this.context).getWindowManager().getDefaultDisplay().getMetrics(dm);//当前activity
        return dm.heightPixels;
    }
}