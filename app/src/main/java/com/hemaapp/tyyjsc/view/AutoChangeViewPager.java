package com.hemaapp.tyyjsc.view;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 图片轮播
 */
public class AutoChangeViewPager extends ViewPager {
    private int time = 3000;

    private Runnable nextRunnable = new Runnable() {

        @Override
        public void run() {
            PagerAdapter adapter = getAdapter();
            if (adapter != null) {
                int count = adapter.getCount();
                Log.d("TAG", "count--->" + count);
                if (count > 0) {
                    int next = getCurrentItem() + 1;
                    Log.d("TAG", "next--->" + next);
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
    public AutoChangeViewPager(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public AutoChangeViewPager(Context context, AttributeSet attrs) {
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
}
