package com.hemaapp.tyyjsc.activity;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseFragmentActivity;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.fragments.FragmentCart;
import com.hemaapp.tyyjsc.fragments.FragmentIndex;
import com.hemaapp.tyyjsc.fragments.FragmentMember;
import com.hemaapp.tyyjsc.fragments.FragmentMy;
import com.hemaapp.tyyjsc.fragments.FragmentSort;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.push.DemoPushService;
import com.hemaapp.tyyjsc.push.PushUtils;
import com.igexin.sdk.PushManager;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import xtom.frame.XtomActivity;
import xtom.frame.XtomActivityManager;
import xtom.frame.util.XtomToastUtil;


/**
 * 软件首页
 */
public class ActivityIndex extends BaseFragmentActivity {


    private RadioGroup group = null; // 单选按钮组
    private static boolean isExit = false;
    private Timer timer = null; // 计时器
    private String which = "";//3 购物车
    private ImageView top1,top2,top3,top4,top5,lastview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_index);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        toogleFragment(FragmentIndex.class);
        // 个推开始
        startGeTuiPush();
    }

    public void change(){
        toogleFragment(FragmentMember.class);
        group.check(R.id.member);
    }

    /**
     * 个推开始
     */
    private PushReceiver pushReceiver;

    private void startGeTuiPush() {
        PushManager.getInstance().initialize(mContext, DemoPushService.class);
        registerPushReceiver();
    }

    private void registerPushReceiver() {
        if (pushReceiver == null) {
            pushReceiver = new PushReceiver();
            IntentFilter mFilter = new IntentFilter("com.hemaapp.push.connect");
            mFilter.addAction("com.hemaapp.push.msg");
            mFilter.addAction("com.hemaapp.tyyjsc.shopcart");
            registerReceiver(pushReceiver, mFilter);
        }
    }

    private void stopGeTuiPush() {
        unregisterPushReceiver();
    }

    private void unregisterPushReceiver() {
        if (pushReceiver != null)
            unregisterReceiver(pushReceiver);
    }

    private class PushReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            handleEvent(intent);
        }

        private void handleEvent(Intent intent) {
            String action = intent.getAction();

            if ("com.hemaapp.push.connect".equals(action)) {
                saveDevice();

            } else if ("com.hemaapp.push.msg".equals(action)) {
                boolean unread = PushUtils.getmsgreadflag(
                        getApplicationContext(), "2");
                if (unread) {
                    log_i("有未读推送");
                } else {
                    log_i("无未读推送");
                }
            } else if("com.hemaapp.tyyjsc.shopcart".equals(action)){
                toogleFragment(FragmentCart.class);
                group.check(R.id.cart);
            }
        }
    }

    public void saveDevice() {
        User user = getApplicationContext().getUser();
        if (user != null) {
            getNetWorker().deviceSave(user.getToken(),
                    PushUtils.getUserId(mContext), "2",
                    PushUtils.getChannelId(mContext));
        }
    }

    /**
     * 个推结束
     */
    public Fragment toogleFragment(Class<? extends Fragment> c) {
        FragmentManager manager = getSupportFragmentManager();
        String tag = c.getName();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag(tag);

        if (fragment == null) {
            try {
                fragment = c.newInstance();
                // 替换时保留Fragment,以便复用
                transaction.add(R.id.con, fragment, tag);
            } catch (Exception e) {
                // ignore
            }
        } else {
            // nothing
        }
        // 遍历存在的Fragment,隐藏其他Fragment
        List<Fragment> fragments = manager.getFragments();
        if (fragments != null)
            for (Fragment fm : fragments)
                if (!fm.equals(fragment))
                    transaction.hide(fm);

        transaction.show(fragment);
        transaction.commitAllowingStateLoss();//commit
        return fragment;
    }
    @Override
    protected void findView() {
        group = (RadioGroup) findViewById(R.id.rg);
        top1= (ImageView) findViewById(R.id.top1);
        top2= (ImageView) findViewById(R.id.top2);
        top3= (ImageView) findViewById(R.id.top3);
        top4= (ImageView) findViewById(R.id.top4);
        top5= (ImageView) findViewById(R.id.top5);
        lastview=top1;
    }

    @Override
    protected void getExras() {
        which = getIntent().getStringExtra("which");
    }

    @Override
    protected void setListener() {
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.hom://首页
                        lastview.setVisibility(View.INVISIBLE);
                        top1.setVisibility(View.VISIBLE);
                        lastview=top1;
                        toogleFragment(FragmentIndex.class);
                        break;
                    case R.id.sort://分类
                        lastview.setVisibility(View.INVISIBLE);
                        top2.setVisibility(View.VISIBLE);
                        lastview=top2;
                        toogleFragment(FragmentSort.class);
                        break;
                    case R.id.cart://购物车
                        lastview.setVisibility(View.INVISIBLE);
                        top4.setVisibility(View.VISIBLE);
                        lastview=top4;
                        toogleFragment(FragmentCart.class);
                        break;
                    case R.id.my://我的
                        lastview.setVisibility(View.INVISIBLE);
                        top5.setVisibility(View.VISIBLE);
                        lastview=top5;
                        toogleFragment(FragmentMy.class);
                        break;
                    case R.id.member://会员
                        lastview.setVisibility(View.INVISIBLE);
                        top3.setVisibility(View.VISIBLE);
                        lastview=top3;
                        toogleFragment(FragmentMember.class);
                        break;
                    default:
                        break;
                }
            }
        });
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
    protected boolean onKeyBack() {
        // 实现点击两次退出程序
        if (!isExit) {
            isExit = true;
            XtomToastUtil.showShortToast(mContext, "再按一次返回键退出程序");
            timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
            NotificationManager nm = (NotificationManager) mContext
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancelAll();
        }
        return true;
    }

    @Override
    public void onDestroy() {
        //个推
        stopGeTuiPush();
        //个推
        super.onDestroy();
    }
}
