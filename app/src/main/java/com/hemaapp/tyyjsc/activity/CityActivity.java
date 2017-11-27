package com.hemaapp.tyyjsc.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.CharacterParser;
import com.hemaapp.tyyjsc.PinyinComparator;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.LocationAdapter;
import com.hemaapp.tyyjsc.db.VisitCitysDBHelper;
import com.hemaapp.tyyjsc.model.DistrictInfor;
import com.hemaapp.tyyjsc.view.ClearEditText;
import com.hemaapp.tyyjsc.view.LetterListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomToastUtil;

/**
 * 选择城市列表
 * 包括定位城市和最近访问城市
 * flag = 1:从广告页面进入的，在这种情况下,按返回键，直接进入首界面
 */
public class CityActivity extends BaseActivity {

    private TextView titleText; //标题栏的名称
    private ImageButton titleLeft; //左键
    private Button titleRight; //右键

    private ClearEditText etSearch; //自定义编辑框
    private LetterListView letterList; //右侧字母索引列表
    private ListView mCityList; //展示城市的listview
    private TextView overlay; //点击字母索引列表后，出现的额内容

    private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private Handler handler;
    private OverlayThread overlayThread;
    private WindowManager windowManager;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private LoacationReceiver loacationReceiver;

    private String locationCity;
    private ArrayList<DistrictInfor> allDistricts;
    private ArrayList<DistrictInfor> visitDistricts;
    private ArrayList<DistrictInfor> filterDateList;
    private LocationAdapter mLocationAdapter;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_city);
        super.onCreate(savedInstanceState);
        handler = new Handler();
        overlayThread = new OverlayThread();
        initOverlay();
        initViews();
        new VisitCityTask().execute();
        getLocationCity();
    }

    // 获取定位城市
    private void getLocationCity() {
        locationCity = XtomSharedPreferencesUtil.get(this, "city_name");
        if (isNull(locationCity)) {
            BaseApplication application = BaseApplication.getInstance();
            String action = application.getPackageName() + ".location";
            IntentFilter mFilter = new IntentFilter(action);
            mFilter.addAction(BaseConfig.BROADCAST_GETCITY);
            loacationReceiver = new LoacationReceiver();
            registerReceiver(loacationReceiver, mFilter);
        }
    }

    @Override
    protected void onDestroy() {
        windowManager.removeView(overlay);
        if (loacationReceiver != null)
            unregisterReceiver(loacationReceiver);
        super.onDestroy();
    }

    private void initViews() {
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        // 根据输入框输入值的改变来过滤搜索
        etSearch.addTextChangedListener(new TextChageListener());
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = allDistricts;
        } else {
            filterDateList.clear();
            for (DistrictInfor district : allDistricts) {
                String name = district.getName();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString().toLowerCase(
                                Locale.getDefault()))) {
                    filterDateList.add(district);
                }
            }
        }
        boolean isshow = etSearch.isVisibile();
        mLocationAdapter.setShow(!isshow);
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        mLocationAdapter.updateListView(filterDateList);
    }

    // 初始化汉语拼音首字母弹出提示框
    @SuppressLint("InflateParams")
    private void initOverlay() {
        LayoutInflater inflater = LayoutInflater.from(this);
        overlay = (TextView) inflater.inflate(R.layout.overlay, null);
        overlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlay, lp);
    }

    private void districtList() {
        getNetWorker().districtList("-1");
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
                showProgressDialog("正在获取城市列表");
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case DISTRICT_LIST1:
                cancelProgressDialog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask,
                                            HemaBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case DISTRICT_LIST1:
                @SuppressWarnings("unchecked")
                HemaPageArrayResult<DistrictInfor> dResult = (HemaPageArrayResult<DistrictInfor>) baseResult;
                allDistricts = dResult.getObjects();
                mLocationAdapter.setLastCties(visitDistricts);
                mLocationAdapter.setLocCity(locationCity);
                Collections.sort(allDistricts, pinyinComparator);
                mLocationAdapter.setList(allDistricts);
                setAdapter(allDistricts);
                mLocationAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    /**
     * 为ListView设置适配器
     *
     * @param list
     */
    private void setAdapter(List<DistrictInfor> list) {
        if (list != null) {
            mCityList.setAdapter(mLocationAdapter);
            alphaIndexer = mLocationAdapter.getAlphaIndexer();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case DISTRICT_LIST1:
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case DISTRICT_LIST1:
                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {
        titleText = (TextView) findViewById(R.id.bar_name);
        titleLeft = (ImageButton) findViewById(R.id.back_left);
        //titleRight = (Button) findViewById(R.id.title_btn_right);

        etSearch = (ClearEditText) findViewById(R.id.etsearch);
        letterList = (LetterListView) findViewById(R.id.letterListView);
        mCityList = (ListView) findViewById(R.id.city_list);
        mLocationAdapter = new LocationAdapter(this, true);
    }

    @Override
    protected void getExras() {
        flag = mIntent.getStringExtra("flag");
    }

    @Override
    protected void setListener() {
        titleText.setText("当前城市");
        titleLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //titleRight.setVisibility(View.GONE);
        letterList
                .setOnTouchingLetterChangedListener(new LetterListViewListener());
        mCityList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                DistrictInfor citysel = (DistrictInfor) view.getTag(R.id.TAG);
                itemclick(citysel);
            }
        });
    }

    /**
     * 城市列表点击事件
     */
    public void itemclick(DistrictInfor info) {
        if (info == null) {
            XtomToastUtil.showShortToast(mContext, "暂不支持此城市");
            return;
        }
        insertVisitcity(info);
        Intent intent = new Intent();
        intent.putExtra("city", info.getName());
        intent.setAction(BaseConfig.BROADCAST_GETCITY);
        sendBroadcast(intent);
        finish();

    }

    private void insertVisitcity(DistrictInfor city) {
        VisitCitysDBHelper dbHelper = new VisitCitysDBHelper(mContext);
        dbHelper.reInsert(city);
    }

    private class LetterListViewListener implements
            LetterListView.OnTouchingLetterChangedListener {
        @SuppressLint("NewApi")
        @Override
        public void onTouchingLetterChanged(final String s) {
            if (alphaIndexer != null && alphaIndexer.get(s) != null) {
                final int position = alphaIndexer.get(s);
                log_w("=s=" + s);
                mCityList.setSelection(position + 2);
            } else if ("↑".equals(s)) {
                mCityList.setSelection(0);
            }
            overlay.setText(s);
            overlay.setVisibility(View.VISIBLE);
            handler.removeCallbacks(overlayThread);
            // 延迟一秒后执行，让overlay为不可见
            handler.postDelayed(overlayThread, 1500);
        }
    }

    // 设置overlay不可见
    private class OverlayThread implements Runnable {

        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }
    }

    private class VisitCityTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            showProgressDialog("请稍后");
        }

        @Override
        protected Void doInBackground(Void... params) {
            VisitCitysDBHelper dbHelper = new VisitCitysDBHelper(mContext);
            visitDistricts = dbHelper.select();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            cancelProgressDialog();
            districtList();
        }
    }

    private class LoacationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BaseApplication application = BaseApplication.getInstance();
            locationCity = XtomSharedPreferencesUtil.get(CityActivity.this, "city_name");
            mLocationAdapter.setLocCity(locationCity);
            if (mCityList.getAdapter() != null)
                mLocationAdapter.notifyDataSetChanged();

        }
    }

    private class TextChageListener implements TextWatcher {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
            filterData(s.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
