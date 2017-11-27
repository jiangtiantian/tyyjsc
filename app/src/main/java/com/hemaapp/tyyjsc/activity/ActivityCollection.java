package com.hemaapp.tyyjsc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.CollectionAdapter;
import com.hemaapp.tyyjsc.model.GoodsBriefIntroduction;
import com.hemaapp.tyyjsc.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 我的收藏  我的足迹
 */
public class ActivityCollection extends BaseActivity implements View.OnClickListener, CollectionAdapter.onSelectListener {
    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧微文字按钮
    private CollectionAdapter collectionAdapter = null;
    private GridView gridView = null;
    private ArrayList<GoodsBriefIntroduction> data = new ArrayList<>();
    private boolean isEditable = false;//默认不可编辑
    private boolean isAllSelected = false;//是否全选中
    private HashMap<String, GoodsBriefIntroduction> selected = new HashMap<>();
    private RefreshLoadmoreLayout layout = null;
    private int page = 0;
    private User user = null;
    private FrameLayout topView = null;//滑动顶部按钮
    private FrameLayout goodsClearingLy = null;//底部删除布局
    private CheckBox allBox = null;
    private Button delBtn = null;//取消按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collection);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        user = BaseApplication.getInstance().getUser();
        getRecommendGoodList();

    }
    //获取收藏商品列表
    public void getRecommendGoodList() {
        getNetWorker().mycollect_list(user.getToken(),String.valueOf(page));
    }

    @Override
    protected void findView() {
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);

        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText(R.string.hm_hlxs_txt_110);

        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.GONE);
        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setTextColor(getResources().getColor(R.color.index_search_bg));
        hmRightTxtView.setVisibility(View.INVISIBLE);
        hmRightTxtView.setText(R.string.hm_hlxs_txt_112);
        //刷新控件
        layout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        gridView = (GridView) findViewById(R.id.grid);
        //浮动按钮
        topView = (FrameLayout) findViewById(R.id.top);
        goodsClearingLy = (FrameLayout) findViewById(R.id.goods_clearing_ly);
        allBox = (CheckBox) findViewById(R.id.cart_allchoose_cb);
        delBtn = (Button) findViewById(R.id.del);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        hmBackBtn.setOnClickListener(this);
        hmRightTxtView.setOnClickListener(this);
        topView.setOnClickListener(this);
        layout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        hmBackBtn.setOnClickListener(this);

        layout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getRecommendGoodList();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                getRecommendGoodList();
            }
        });
        allBox.setOnClickListener(this);
        delBtn.setOnClickListener(this);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MYCOLLECT_LIST:
            case LOVE_CANCEL:
                showProgressDialog(getString(R.string.hm_hlxs_txt_113));
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MYCOLLECT_LIST:
            case LOVE_CANCEL:
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
            case MYCOLLECT_LIST:
                String page = hemaNetTask.getParams().get("page");
                int sysPagesize = BaseApplication.getInstance().getSysInitInfo()
                        .getSys_pagesize();
                @SuppressWarnings("unchecked")
                HemaPageArrayResult<GoodsBriefIntroduction> nResult = (HemaPageArrayResult<GoodsBriefIntroduction>) hemaBaseResult;
                ArrayList<GoodsBriefIntroduction> list = nResult.getObjects();
                if ("0".equals(page)) {// 刷新
                    layout.refreshSuccess();
                    data.clear();
                    data.addAll(list);
                    if (list.size() < sysPagesize)
                        layout.setLoadmoreable(false);
                    else
                        layout.setLoadmoreable(true);
                } else {// 更多
                    layout.loadmoreSuccess();
                    if (list.size() > 0) {
                        data.addAll(list);
                        if (list.size() < sysPagesize)
                            layout.setLoadmoreable(false);
                        else
                            layout.setLoadmoreable(true);
                    } else {
                        layout.setLoadmoreable(false);
                        XtomToastUtil.showShortToast(ActivityCollection.this, getString(R.string.hm_hlxs_txt_114));
                    }
                }
                freshData();
                break;
            case LOVE_CANCEL:
                Iterator<Map.Entry<String, GoodsBriefIntroduction>> iterator = selected.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, GoodsBriefIntroduction> entry = iterator.next();
                    for (GoodsBriefIntroduction info : data) {
                        if (info.getId().equals(entry.getKey())) {
                            data.remove(info);
                            break;
                        }
                    }
                }
                selected.clear();
                freshData();
                updateBottomUI();
                break;
            default:
                break;
        }
    }

    public void freshData() {
        if (collectionAdapter == null) {
            collectionAdapter = new CollectionAdapter(this, data);
            collectionAdapter.setIsEditable(isEditable);
            collectionAdapter.setIsAllSelected(isAllSelected);
            gridView.setAdapter(collectionAdapter);
            collectionAdapter.setListener(this);
        } else {
            collectionAdapter.setData(data);
            collectionAdapter.setIsEditable(isEditable);
            collectionAdapter.setIsAllSelected(isAllSelected);
            collectionAdapter.notifyDataSetChanged();
        }
        if (data == null || data.size() == 0) {
            hmRightTxtView.setVisibility(View.INVISIBLE);
            topView.setVisibility(View.GONE);
        } else {
            hmRightTxtView.setVisibility(View.VISIBLE);
            topView.setVisibility(View.VISIBLE);
        }
    }

    public void updateBottomUI() {
        if (data == null || data.size() == 0) {
            goodsClearingLy.setVisibility(View.GONE);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layout.getLayoutParams();
            params.bottomMargin = BaseUtil.dip2px(ActivityCollection.this, 0);
            layout.setLayoutParams(params);

        } else {
            goodsClearingLy.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layout.getLayoutParams();
            params.bottomMargin = BaseUtil.dip2px(ActivityCollection.this, 58);
            layout.setLayoutParams(params);
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_LIST:
            case LOVE_CANCEL:
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
            case GOODS_LIST:
                String page = hemaNetTask.getParams().get("page");
                if (isNull(page) || "0".equals(page)) {
                    layout.refreshFailed();
                } else {
                    layout.loadmoreFailed();
                }
                break;
            case LOVE_CANCEL:
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_left:
                finish();
                break;
            case R.id.bar_right_txt://编辑 or 完成
                String txt = ((TextView) v).getText().toString().trim();
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layout.getLayoutParams();
                if ("编辑".equals(txt)) {
                    FrameLayout.LayoutParams pm = (FrameLayout.LayoutParams)topView.getLayoutParams();
                    pm.bottomMargin = BaseUtil.dip2px(mContext, 15) + BaseUtil.dip2px(mContext, 58);
                    ((TextView) v).setText(R.string.hm_hlxs_txt_115);
                    params.bottomMargin = BaseUtil.dip2px(ActivityCollection.this, 58);
                    goodsClearingLy.setVisibility(View.VISIBLE);
                    allBox.setChecked(false);
                    if (collectionAdapter != null) {
                        isEditable = true;
                        isAllSelected = false;
                        collectionAdapter.setIsEditable(isEditable);
                        collectionAdapter.setIsAllSelected(isAllSelected);
                        collectionAdapter.notifyDataSetChanged();
                    }
                } else {
                    FrameLayout.LayoutParams pm = (FrameLayout.LayoutParams)topView.getLayoutParams();
                    pm.bottomMargin = BaseUtil.dip2px(mContext, 15) + BaseUtil.dip2px(mContext, 0);
                    ((TextView) v).setText(R.string.hm_hlxs_txt_112);
                    params.bottomMargin = BaseUtil.dip2px(ActivityCollection.this, 0);
                    goodsClearingLy.setVisibility(View.INVISIBLE);
                    selected.clear();
                    if (collectionAdapter != null) {
                        isEditable = false;
                        isAllSelected = false;
                        collectionAdapter.setIsEditable(isEditable);
                        collectionAdapter.setIsAllSelected(isAllSelected);
                        collectionAdapter.notifyDataSetChanged();
                    }
                }
                layout.setLayoutParams(params);
                break;
            case R.id.top:
                gridView.smoothScrollToPosition(0);
                break;
            case R.id.del://删除功能
                if (selected != null && selected.size() > 0) {
                    String ids = "";
                    Iterator<Map.Entry<String, GoodsBriefIntroduction>> iterator = selected.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, GoodsBriefIntroduction> entry = iterator.next();
                        ids += entry.getKey() + ",";
                    }
                    ids = ids.substring(0, ids.lastIndexOf(","));
                    getNetWorker().loveCancel(user.getToken(),"1",ids);
                } else {
                    showTextDialog(getString(R.string.hm_hlxs_txt_103));
                }
                break;
            case R.id.cart_allchoose_cb://全选
                if (allBox.isChecked()) {
                    isAllSelected = true;
                    selected.clear();
                    for (GoodsBriefIntroduction info : data) {
                        selected.put(info.getId(), info);
                    }
                } else {
                    selected.clear();
                    isAllSelected = false;
                }
                if (collectionAdapter != null) {
                    collectionAdapter.setIsAllSelected(isAllSelected);
                    collectionAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSelect(HashMap<String, GoodsBriefIntroduction> selected) {
        this.selected = selected;
        if (selected.size() == data.size() && selected.size() != 0) {
            allBox.setChecked(true);
            isAllSelected = true;
        } else {
            allBox.setChecked(false);
            isAllSelected = false;
        }
    }
}
