package com.hemaapp.tyyjsc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.R;

import java.net.MalformedURLException;
import java.net.URL;

import xtom.frame.image.load.XtomImageTask;

/**
 * Created by wangyuxia on 2017/8/15.
 * 开机广告
 */
public class OpenAdAcitivity extends BaseActivity{

    private ImageView imageView;
    private TextView textView;

    private String imgurl;
    private TimeThread timeThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_open_ad);
        super.onCreate(savedInstanceState);
        timeThread = new TimeThread(new TimeHandler(this));
        timeThread.start();
        loadImage();
    }

    private void loadImage(){
        try {
            URL url = new URL(imgurl);
            imageWorker.loadImage(new XtomImageTask(imageView, url, mContext));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        imageView = (ImageView) findViewById(R.id.img);
        textView = (TextView) findViewById(R.id.textview);
    }

    @Override
    protected void getExras() {
        imgurl = mIntent.getStringExtra("imgurl");
    }

    @Override
    protected void setListener() {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMain();
            }
        });
    }

    private void toMain(){
        Intent it = new Intent(mContext, ActivityIndex.class);
        startActivity(it);
        finish();
    }

    @Override
    protected boolean onKeyBack() {
        toMain();
        return true;
    }

    private class TimeThread extends Thread {
        private int curr;

        private TimeHandler timeHandler;

        public TimeThread(TimeHandler timeHandler) {
            this.timeHandler = timeHandler;
        }

        void cancel() {
            curr = 0;
        }

        @Override
        public void run() {
            curr = 10;
            while (curr > 0) {
                timeHandler.sendEmptyMessage(curr);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // ignore
                }
                curr--;
            }
            timeHandler.sendEmptyMessage(-1);
        }
    }

    private static class TimeHandler extends Handler {
        OpenAdAcitivity activity;

        public TimeHandler(OpenAdAcitivity activity) {
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    Intent it = new Intent(activity, ActivityIndex.class);
                    activity.startActivity(it);
                    activity.finish();
                    break;
                default:
                    activity.textView.setText("跳过" + msg.what);
                    break;
            }
        }
    }
}
