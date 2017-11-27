package com.hemaapp.tyyjsc.showlargepic;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaActivity;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.photoview.HackyViewPager;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.Comment;

import java.util.ArrayList;

/**
 * 查看大图(本地图片请传递imagelist,网络图片传递images或者imagelist,
 * titleAndContentVisible标题和图片内容是否显示默认为true)
 */
public class ShowLargePicActivity extends HemaActivity {
    private TextView orderby;

    private HackyViewPager mViewPager;
    private ArrayList<String> urllist;
    private int position;
    private Comment comment = null;

    // 头像
    private String avar = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_showlargepic);
        super.onCreate(savedInstanceState);
        if (urllist != null && urllist.size() > 0) {
            mViewPager.setAdapter(new PhotoPagerAdapter(this, urllist));
            mViewPager.setCurrentItem(position);
        }
        setImgInfo();
    }

    protected void setImgInfo() {
        int sp = position + 1;
        orderby.setText(sp + "/" + urllist.size());
    }

    @Override
    protected void findView() {
        mViewPager = (HackyViewPager) findViewById(R.id.gallery);

        orderby = (TextView) findViewById(R.id.orderby);

    }

    /**
     * 显示或隐藏图片信息
     */
    public void toogleInfo() {
        finish();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void getExras() {
        comment = (Comment) getIntent().getSerializableExtra("comment");
        position = mIntent.getIntExtra("position", 0);

        if (comment == null) {// 如果用户没有传递comment参数 查看用户头像
            avar = getIntent().getStringExtra("avar");
            if (urllist == null || urllist.size() == 0) {
                urllist = new ArrayList<String>();
                urllist.add(avar);
            }
        } else {// 查看评论大图
            if (comment.getImgs() != null
                    && comment.getImgs().size() > 0) {

                if (urllist == null || urllist.size() == 0) {
                    urllist = new ArrayList<String>();
                    for (int i = 0; i < comment.getImgs().size(); i++) {
                        String url = comment.getImgs().get(i)
                                .getImgurlbig();
                        urllist.add(url);
                    }
                }
            }
        }
    }

    @Override
    protected void setListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                ShowLargePicActivity.this.position = position;
                setImgInfo();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        mViewPager.setCurrentItem(position);
        orderby.setText(position + "/" + urllist.size());
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask,
                                            HemaBaseResult baseResult) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        // TODO Auto-generated method stub

    }

    @Override
    protected HemaNetWorker initNetWorker() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean onAutoLoginFailed(HemaNetWorker netWorker,
                                     HemaNetTask netTask, int failedType, HemaBaseResult baseResult) {

        return false;
    }

}
