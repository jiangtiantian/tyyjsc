package com.hemaapp.tyyjsc.activity;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseFragmentActivity;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.OrderViewPagerAdapter;
import com.hemaapp.tyyjsc.fragments.FragmentOrderList;

import java.util.ArrayList;


/**
 * 我的订单
 */
public class ActivityOrderList extends BaseFragmentActivity {
    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧微文字按钮
    private ViewPager viewPager = null;
    private OrderViewPagerAdapter orderViewPagerAdapter = null;
    private ArrayList<FragmentOrderList> frags = null;
    private RadioGroup group = null;
    private String from_what;//1：商品订单;2：套餐订单
    private FragmentOrderList fragmentOrderAll = null;//全部
    private FragmentOrderList fragmentDfk = null;//待付款
    private FragmentOrderList fragmentDfh = null;//待发货
    private FragmentOrderList fragmentDsh = null;//待收货
    private FragmentOrderList fragmentDpj = null;//待评价

    //底部索引条
    private int offset = 0;
    private int curPos = 0;
    private ImageView view = null;

    private String keytype = "";
    private int curTabPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_list);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        offset = dm.widthPixels / 5;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                dm.widthPixels / 5, LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        Matrix matrix = new Matrix();
        matrix.postTranslate(curPos * offset, 0);
        view.setImageMatrix(matrix);
        initViewPager();
    }

    //初始化适配器
    public void initViewPager() {
        fragmentOrderAll = FragmentOrderList.newInstance("1",from_what);
        fragmentDfk = FragmentOrderList.newInstance("2",from_what);
        fragmentDfh = FragmentOrderList.newInstance("3",from_what);
        fragmentDsh = FragmentOrderList.newInstance("4",from_what);
        fragmentDpj = FragmentOrderList.newInstance("5",from_what);
        frags = new ArrayList<>();
        frags.add(0, fragmentOrderAll);
        frags.add(1, fragmentDfk);
        frags.add(2, fragmentDfh);
        frags.add(3, fragmentDsh);
        frags.add(4, fragmentDpj);
        orderViewPagerAdapter = new OrderViewPagerAdapter(getSupportFragmentManager(), frags);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(orderViewPagerAdapter);
        viewPager.setOnPageChangeListener(new TabPageChangeListener());
        viewPager.setCurrentItem(curTabPos);
    }
    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);
        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText("我的订单");
        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.GONE);
        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.INVISIBLE);
        viewPager = (ViewPager) findViewById(R.id.vp);
        group = (RadioGroup) findViewById(R.id.group);

        //索引条
        view = (ImageView) findViewById(R.id.tab);
    }
    public void moveTabBar(int arg0){
        Animation animation = new TranslateAnimation(curPos * offset,
                offset * arg0, 0, 0);
        animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
        animation.setDuration(100);//动画持续时间0.2秒
        view.startAnimation(animation);
        curPos = arg0;
    }
    @Override
    protected void getExras() {
        from_what=getIntent().getStringExtra("from_what");
        keytype = getIntent().getStringExtra("keytype");
        if(isNull(keytype)){
            curTabPos = 0;
        }else{
            curTabPos = Integer.parseInt(keytype);
        }
    }

    @Override
    protected void setListener() {
        hmBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.order_all_tv:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.order_dfk_tv:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.order_dfh_tv:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.order_dsh_tv:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.order_dpj_tv:
                        viewPager.setCurrentItem(4);
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

    //滑动监听
    class TabPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) group.getChildAt(position);
            radioButton.setChecked(true);
            moveTabBar(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
