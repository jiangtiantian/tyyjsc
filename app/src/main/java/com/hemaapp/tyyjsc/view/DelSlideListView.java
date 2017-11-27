package com.hemaapp.tyyjsc.view;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.image.load.XtomImageWorker;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * 侧滑删除
 */
public class DelSlideListView extends ListView implements GestureDetector.OnGestureListener, View.OnTouchListener {

    private SparseArray<SparseArray<XtomImageTask>> tasks;
    private XtomImageWorker imageWorker;
    private OnScrollListener onScrollListener;
    private DelSlideListView.XtomScrollListener xtomScrollListener;
    private DelSlideListView.XtomSizeChangedListener xtomSizeChangedListener;

    private onItemClickListener listener = null;//点击事件


    private GestureDetector mDetector;
    private String TAG = "DelSlideListView";

    private Context context;

    private int px = 0;

    public DelSlideListView(Context context) {
        this(context, null);
    }

    public DelSlideListView(Context context, AttributeSet att) {
        this(context, att, 0);
    }

    public DelSlideListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.tasks = new SparseArray();
        init(context);
    }

    private int standard_touch_target_size = 0;
    private float mLastMotionX;
    // 有item被拉出

    public boolean deleteView = false;
    // 当前拉出的view

    private ScrollLinerLayout mScrollLinerLayout = null;
    // 滑动着

    private boolean scroll = false;
    // 禁止拖动

    private boolean forbidScroll = false;
    // 禁止拖动

    private boolean clicksameone = false;
    // 当前拉出的位置

    private int position;
    // 消息冻结

    private boolean freeze = false;

    private void init(Context mContext) {
        if (this.isInEditMode()) {
            return;
        }
        this.context = mContext;
        mDetector = new GestureDetector(mContext, this);
        // mDetector.setIsLongpressEnabled(false);

        standard_touch_target_size = (int) getResources().getDimension(R.dimen.delete_action_len);
        this.setOnTouchListener(this);

        this.imageWorker = new XtomImageWorker(context.getApplicationContext());
        this.setOnScrollListener(new ScrollListener());
    }

    public void reset() {
        reset(false);
    }

    public void reset(boolean noaction) {
        position = -1;
        deleteView = false;
        if (mScrollLinerLayout != null) {
            if (!noaction) {
                mScrollLinerLayout.snapToScreen(0);
            } else {
                mScrollLinerLayout.scrollTo(0, 0);
            }
            mScrollLinerLayout = null;
        }
        scroll = false;
    }

    public boolean onDown(MotionEvent e) {
        // Log.i(TAG, "onDown");

        mLastMotionX = e.getX();
        int p = this.pointToPosition((int) e.getX(), (int) e.getY()) - this.getFirstVisiblePosition();
        if (deleteView) {
            if (p != position) {
                // 吃掉，不在有消息

                freeze = true;
                return true;
            } else {
                clicksameone = true;
            }
        }
        position = p;
        scroll = false;
        return false;
    }

    public void onLongPress(MotionEvent e) {
        // Log.i(TAG, "onLongPress");

    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // Log.i(TAG, "onScroll" + e1.getX() + ":" + distanceX);

        // 第二次

        if (scroll) {
            int deltaX = (int) (mLastMotionX - e2.getX());
            if (deleteView) {
                deltaX += standard_touch_target_size;
            }
            if (deltaX >= 0 && deltaX <= standard_touch_target_size) {
                mScrollLinerLayout.scrollBy(deltaX - mScrollLinerLayout.getScrollX(), 0);
            }
            return true;
        }
        if (!forbidScroll) {
            forbidScroll = true;
            // x方向滑动，才开始拉动

            if (Math.abs(distanceX) > Math.abs(distanceY)) {
                View v = this.getChildAt(position);
                boolean ischild = v instanceof ScrollLinerLayout;
                if (ischild) {
                    mScrollLinerLayout = (ScrollLinerLayout) v;
                    scroll = true;
                    int deltaX = (int) (mLastMotionX - e2.getX());
                    if (deleteView) {
                        // 再次点击的时候，要把deltax增加

                        deltaX += standard_touch_target_size;
                    }
                    if (deltaX >= 0 && deltaX <= standard_touch_target_size) {
                        mScrollLinerLayout.scrollBy((int) (e1.getX() - e2.getX()), 0);
                    }
                }
            }
        }
        return false;
    }

    public void onShowPress(MotionEvent e) {
        // Log.i(TAG, "onShowPress");

    }

    public boolean onSingleTapUp(MotionEvent e) {
        Log.i(TAG, "onSingleTapUp");
        int pos = this.pointToPosition((int) e.getX(), (int) e.getY());
        if (listener != null && pos > -1) {
            listener.goItemInfo(pos);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (scroll || deleteView) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            boolean isfreeze = freeze;
            boolean isclicksameone = clicksameone;
            forbidScroll = false;
            clicksameone = false;
            freeze = false;
            if (isfreeze) {
                // 上一个跟当前点击不一致 还原
                reset();
                return true;
            }
            int deltaX2 = (int) (mLastMotionX - event.getX());
            // 不存在

            // Log.i(TAG, "scroll:" + scroll + "deltaX2:" + deltaX2);

            if (scroll && deltaX2 >= standard_touch_target_size / 2) {
                mScrollLinerLayout.snapToScreen(standard_touch_target_size * 5 / 3);
                px = BaseUtil.dip2px(context, 120);
                if (XtomSharedPreferencesUtil.get(context, "w") != null) {
                    px = Integer.parseInt(XtomSharedPreferencesUtil.get(context, "w")) / 6;
                }
                mScrollLinerLayout.snapToScreen(px); //120
                deleteView = true;
                scroll = false;
                return true;
            }
            if (deleteView && scroll && deltaX2 >= -standard_touch_target_size / 2) {
                px = BaseUtil.dip2px(context, 120);
                if (XtomSharedPreferencesUtil.get(context, "w") != null) {
                    px = Integer.parseInt(XtomSharedPreferencesUtil.get(context, "w")) / 6;
                }
                mScrollLinerLayout.snapToScreen(px);
                deleteView = true;
                scroll = false;
                return true;
            }
            if (isclicksameone || scroll) {
                reset();
                return true;
            }
            reset();
        }
        if (freeze) {
            return true;
        }
        // Log.i(TAG, "onTouchEvent");

        return mDetector.onTouchEvent(event);

    }

    public void deleteItem() {
        Log.i(TAG, "deleteItem");
        reset(true);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.GestureDetector.OnGestureListener#onFling(android.view.
     * MotionEvent, android.view.MotionEvent, float, float)
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public void addTask(int position, int index, XtomImageTask task) {
        if (!this.imageWorker.isThreadControlable()) {
            this.imageWorker.loadImage(task);
        }

        SparseArray tasksInPosition = (SparseArray) this.tasks.get(position);
        if (this.imageWorker.loadImage(task)) {
            if (tasksInPosition == null) {
                tasksInPosition = new SparseArray();
                this.tasks.put(position, tasksInPosition);
            }

            tasksInPosition.put(index, task);
        } else if (tasksInPosition != null) {
            tasksInPosition.remove(index);
        }

    }

    private void excuteTasks(int first, int last) {
        int size;
        int key;
        for (size = first; size <= last; ++size) {
            SparseArray index = (SparseArray) this.tasks.get(size);
            if (index != null) {
                key = index.size();

                for (int index1 = 0; index1 < key; ++index1) {
                    int key1 = index.keyAt(index1);
                    XtomImageTask task = (XtomImageTask) index.get(key1);
                    this.imageWorker.loadImageByThread(task);
                }
            }
        }

        size = this.tasks.size();

        for (int var9 = 0; var9 < size; ++var9) {
            key = this.tasks.keyAt(var9);
            if (key < first || key > last) {
                this.tasks.remove(key);
            }
        }

    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.xtomSizeChangedListener != null) {
            this.xtomSizeChangedListener.onSizeChanged(this, w, h, oldw, oldh);
        }

    }

    public void setOnScrollListener(OnScrollListener l) {
        if (l instanceof DelSlideListView.ScrollListener) {
            this.onScrollListener = null;
            super.setOnScrollListener(l);
        } else {
            this.onScrollListener = l;
        }

    }

    public DelSlideListView.XtomScrollListener getXtomScrollListener() {
        return this.xtomScrollListener;
    }

    public void setXtomScrollListener(DelSlideListView.XtomScrollListener xtomScrollListener) {
        this.xtomScrollListener = xtomScrollListener;
    }

    public DelSlideListView.XtomSizeChangedListener getXtomSizeChangedListener() {
        return this.xtomSizeChangedListener;
    }

    public void setXtomSizeChangedListener(DelSlideListView.XtomSizeChangedListener xtomSizeChangedListener) {
        this.xtomSizeChangedListener = xtomSizeChangedListener;
    }

    private class ScrollListener implements OnScrollListener {
        private ScrollListener() {
        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (DelSlideListView.this.onScrollListener != null) {
                DelSlideListView.this.onScrollListener.onScrollStateChanged(view, scrollState);
            }

            switch (scrollState) {
                case 0:
                    int first = DelSlideListView.this.getFirstVisiblePosition();
                    int last = DelSlideListView.this.getLastVisiblePosition();
                    DelSlideListView.this.excuteTasks(first, last);
                    DelSlideListView.this.imageWorker.setThreadControlable(false);
                    if (DelSlideListView.this.xtomScrollListener != null) {
                        DelSlideListView.this.xtomScrollListener.onStop(DelSlideListView.this);
                    }
                    break;
                case 1:
                    DelSlideListView.this.imageWorker.clearTasks();
                    DelSlideListView.this.imageWorker.setThreadControlable(true);
                    if (DelSlideListView.this.xtomScrollListener != null) {
                        DelSlideListView.this.xtomScrollListener.onStart(DelSlideListView.this);
                    }
            }

        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (DelSlideListView.this.onScrollListener != null) {
                DelSlideListView.this.onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }

        }
    }

    public interface XtomScrollListener {
        void onStart(DelSlideListView var1);

        void onStop(DelSlideListView var1);
    }

    public interface XtomSizeChangedListener {
        void onSizeChanged(DelSlideListView var1, int var2, int var3, int var4, int var5);
    }
    public void setListener(onItemClickListener listener) {
        this.listener = listener;// 获取点击的位置监听
    }

    public interface onItemClickListener {// 进入列表详情
        void goItemInfo(int pos);
    }
}
