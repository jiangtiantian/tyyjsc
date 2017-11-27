
package com.hemaapp.tyyjsc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.DigitalClock;

import com.hemaapp.tyyjsc.R;

import java.util.Calendar;

/**
 * 自定义倒计时控件
 */
@SuppressWarnings("deprecation")
public class CustomDigitalClock extends DigitalClock {

    Calendar mCalendar;
    private final static String m12 = "h:mm aa";
    private final static String m24 = "k:mm";
    private FormatChangeObserver mFormatChangeObserver;

    private Runnable mTicker;
    private Handler mHandler;
    private long endTime;
    private ClockListener mClockListener;

    private boolean mTickerStopped = false;

    private Context context = null;//上下文

    //自定义颜色
    private int txtBgColor = 0;//背景颜色
    private int txtForeColor = 0;//时分秒前景颜色
    private int dotForeColor = 0;//冒号颜色



    @SuppressWarnings("unused")
    private String mFormat;

    public CustomDigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context, attrs);
    }
    private void initClock(Context context,AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.dialog);//得到属性数组
        txtBgColor = a.getColor(R.styleable.dialog_timeBgColor, Color.WHITE);//得到属性值，可以指定默认值，防止空值，注意引用名。
        txtForeColor = a.getColor(R.styleable.dialog_timeColor, Color.GRAY);
        dotForeColor = a.getColor(R.styleable.dialog_timeDotColor, Color.GRAY);
        a.recycle();

        this.context = context;
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }

        mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI, true, mFormatChangeObserver);

        setFormat();
    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new Handler();

        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
            public void run() {
                if (mTickerStopped)
                    return;
                long currentTime = System.currentTimeMillis();
                if (currentTime / 1000 == endTime / 1000 - 5 * 60) {
                    mClockListener.remainFiveMinutes();
                }
                long distanceTime = endTime - currentTime;
                distanceTime /= 1000;
                if (distanceTime == 0) {
                    setCusTextViewStyle("00:00:00");
                    onDetachedFromWindow();
                    mClockListener.timeEnd();
                } else if (distanceTime < 0) {
                    setCusTextViewStyle("00:00:00");
                } else {
                    setCusTextViewStyle(dealTime(distanceTime));
                }
                invalidate();
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();
    }
    //自定义TextView显示格式
    public void setCusTextViewStyle(String format){

        format = format.trim();

        SpannableStringBuilder style=new SpannableStringBuilder(format);
        //时分秒黑色块
        style.setSpan(new BackgroundColorSpan(txtBgColor),0,2,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        style.setSpan(new BackgroundColorSpan(txtBgColor),3,5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        style.setSpan(new BackgroundColorSpan(txtBgColor),6,8,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //时分秒前景色
        style.setSpan(new ForegroundColorSpan(txtForeColor),0,2,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(txtForeColor),3,5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(txtForeColor),6,8,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //设置冒号颜色
        style.setSpan(new ForegroundColorSpan(dotForeColor),2,3,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(dotForeColor),5,6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);


        setText(style);
    }

    /**
     * deal time string
     *
     * @param time
     * @return
     */
    public static String dealTime(long time) {
        StringBuffer returnString = new StringBuffer();
        long day = time / (24 * 60 * 60);
        long hours = (time % (24 * 60 * 60)) / (60 * 60);
        long minutes = ((time % (24 * 60 * 60)) % (60 * 60)) / 60;
        long second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;

        String hoursStr = timeStrFormat(String.valueOf(hours));
        String minutesStr = timeStrFormat(String.valueOf(minutes));
        String secondStr = timeStrFormat(String.valueOf(second));

        returnString.append(hoursStr).append(":").append(minutesStr)
                .append(":").append(secondStr);
        return returnString.toString();
    }

    /**
     * format time
     *
     * @param timeStr
     * @return
     */
    private static String timeStrFormat(String timeStr) {
        switch (timeStr.length()) {
            case 1:
                timeStr = "0" + timeStr;
                break;
        }
        return timeStr;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }

    /**
     * Clock end time from now on.
     *
     * @param endTime
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    /**
     * Pulls 12/24 mode from system settings
     */
    private boolean get24HourMode() {
        return android.text.format.DateFormat.is24HourFormat(getContext());
    }

    private void setFormat() {
        if (get24HourMode()) {
            mFormat = m24;
        } else {
            mFormat = m12;
        }
    }

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            setFormat();
        }
    }

    public void setClockListener(ClockListener clockListener) {
        this.mClockListener = clockListener;
    }

    public interface ClockListener {
        void timeEnd();

        void remainFiveMinutes();
    }

}