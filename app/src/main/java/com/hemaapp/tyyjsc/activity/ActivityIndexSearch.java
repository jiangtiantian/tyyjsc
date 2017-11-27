package com.hemaapp.tyyjsc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.SearchHistoryAdapter;
import com.hemaapp.tyyjsc.db.SearchDBClient;
import com.hemaapp.tyyjsc.model.LabelInfo;
import com.hemaapp.tyyjsc.view.FlowLayout;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
/**
 * 搜索页面
 */
public class ActivityIndexSearch extends BaseActivity implements View.OnClickListener{
    private ImageButton hmBackBtn = null;//返回
    private EditText keywordET = null;//关键字输入框
    private TextView doSearchBtn = null;//取消或搜索
    private View headerView = null;//历史搜索记录头布局
    private FlowLayout flowLayout = null;//标签流布局
    private ArrayList<LabelInfo> labels = null;
    private ListView listView = null;
    private SearchHistoryAdapter searchHistoryAdapter = null; // 搜索记录适配器
    private SearchDBClient searchDBClient = null;// 搜索数据
    private ArrayList<String> historyList = null;// 历史记录


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_index_search);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        // 实例化搜索数据操作对象
        searchDBClient = SearchDBClient.get(mContext);
        getLabelList();
    }
    //获取热门标签列表
    public void getLabelList() {
        getNetWorker().getLabelList();
    }

    // 初始化收索列表
    public void initHistoryList() {
        historyList = searchDBClient.select("1");
        searchHistoryAdapter = new SearchHistoryAdapter(this, historyList);
        listView.setAdapter(searchHistoryAdapter);
    }

    @Override
    protected void findView() {
        keywordET = (EditText) findViewById(R.id.index_search);
        doSearchBtn = (TextView) findViewById(R.id.search);
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);
        headerView = LayoutInflater.from(this).inflate(R.layout.header_history_view, null);
        flowLayout = (FlowLayout) headerView.findViewById(R.id.labels);
        listView = (ListView) findViewById(R.id.history);
        listView.addHeaderView(headerView);
    }

    @Override
    protected void getExras() {

    }
    @Override
    protected void setListener() {
        hmBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        keywordET.addTextChangedListener(new onTxTChange());
        // 处理搜索 或取消按钮
        doSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handelSearch();
            }
        });
        // 监听软键盘搜索按钮
        keywordET
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH
                                || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            handelSearch();
                            return true;
                        }
                        return false;
                    }
                });
    }

    public void handelSearch() {
        String keyWord = keywordET.getText().toString().trim();
        String curTxt = doSearchBtn.getText().toString().trim();
        if ("取消".equals(curTxt)) {
            closeKeyboard();
            finish();
        } else {
            if(isNull(keyWord)){
                XtomToastUtil.showShortToast(mContext, "输入不能为空");
                return;
            }
            keywordET.setText("");
            // 保存搜索项
            searchDBClient.insert(keyWord, "1");
            Intent intent = new Intent(ActivityIndexSearch.this, ActivitySearchResult.class);
            intent.putExtra("keytype","8");
            intent.putExtra("name", keyWord);
            startActivity(intent);
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case LABEL_LIST:
                showProgressDialog(R.string.hm_hlxs_txt_34);
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case LABEL_LIST:
                cancelProgressDialog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case LABEL_LIST:
                HemaArrayResult<LabelInfo> labelResult = (HemaArrayResult<LabelInfo>) hemaBaseResult;
                labels = labelResult.getObjects();
                initLabel();
                break;
            default:
                break;
        }
    }
    public void initLabel(){
        flowLayout.removeAllViews();
        if(labels != null && labels.size() > 0){
            for (int i = 0; i < labels.size(); i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.label_item, null);
                TextView labelNameView = (TextView) view.findViewById(R.id.labelname);
                labelNameView.setText(labels.get(i).getKeyword());
                labelNameView.setBackgroundResource(R.drawable.label_search_selector);
                labelNameView.setTextColor(getResources().getColor(R.color.black));
                labelNameView.setOnClickListener(this);
                labelNameView.setTag(labels.get(i));
                flowLayout.addView(view);
            }
        }else{
            View view = LayoutInflater.from(this).inflate(R.layout.label_item, null);
            TextView labelNameView = (TextView) view.findViewById(R.id.labelname);
            labelNameView.setTextColor(getResources().getColor(R.color.black));
            labelNameView.setText("暂无");
            labelNameView.setBackgroundResource(R.drawable.label_search_selector);
            flowLayout.addView(view);
        }

    }
    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case LABEL_LIST:
               showTextDialog(hemaBaseResult.getMsg());
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case LABEL_LIST:
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {//标签跳转
        LabelInfo labelInfo = (LabelInfo)v.getTag();
        if(labelInfo != null){
            Intent intent = new Intent(ActivityIndexSearch.this, ActivitySearchResult.class);
            intent.putExtra("id", labelInfo.getId());
            intent.putExtra("keytype","8");
            intent.putExtra("name", labelInfo.getKeyword());
            startActivity(intent);
        }
    }

    // 输入框文字变化监听
    private class onTxTChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            int len = s.length();
            if (len == 0) {
                doSearchBtn.setText("取消");
            } else {
                doSearchBtn.setText("搜索");
            }
        }
    }

    @Override
    protected void onResume() {
        initHistoryList();
        super.onResume();
    }
}
