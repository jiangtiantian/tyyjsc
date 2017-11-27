package com.hemaapp.tyyjsc.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自适应高度
 */
public class AutoHeightViewPager extends ViewPager {
    private int time = 3000;

    private Runnable nextRunnable = new Runnable() {

        @Override
        public void run() {
            PagerAdapter adapter = getAdapter();
            if (adapter != null) {
                int count = adapter.getCount();
                if (count > 0) {
                    int next = getCurrentItem() + 1;
                    if (next == count)
                        next = 0;
                    setCurrentItem(next, true);
                }
                startNext();
            }
        }
    };

    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        startNext();
    }

    public void startNext() {
        stopNext();
        postDelayed(nextRunnable, time);
    }

    public void stopNext() {
        removeCallbacks(nextRunnable);
    }

    public void setTime(int time) {
        this.time = time;
    }

    /**
     * @param context
     */
    public AutoHeightViewPager(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public AutoHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        stopNext();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                startNext();
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 自适应高度
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) height = h;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}