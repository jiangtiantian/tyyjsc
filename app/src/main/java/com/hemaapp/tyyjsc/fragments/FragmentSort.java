package com.hemaapp.tyyjsc.fragments;

import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseFragment;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.CityAdapter;
import com.hemaapp.tyyjsc.adapters.GridAdapter;
import com.hemaapp.tyyjsc.adapters.SortAdapter;
import com.hemaapp.tyyjsc.model.SortItem;
import com.hemaapp.tyyjsc.model.SortItem1;
import com.hemaapp.tyyjsc.view.CityGridView;

import java.util.ArrayList;
import java.util.HashMap;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

import static com.hemaapp.tyyjsc.R.id.citys;
import static com.hemaapp.tyyjsc.R.id.special;

/**
 * 发现
 */
public class FragmentSort extends BaseFragment implements View.OnClickListener, SortAdapter.onItemClickListener {
    //经典分类
    private TextView classicalView = null;

    private LinearLayout sortContainer = null;
    private SortAdapter sortAdapter = null;// 左侧是否选中适配器
    private XtomListView leftSortListView = null;// 分类列表
    private ArrayList<SortItem> sorts = new ArrayList<SortItem>();
    private HashMap<Integer, Boolean> selected = null;// 是否被选中
    private RefreshLoadmoreLayout goodsLayout = null;// 二级分类
    private CityGridView rightGoodsistView = null;// 二级分类列表
    private GridAdapter gridAdapter = null;//二级分类适配器
    private ArrayList<SortItem1> seconds = new ArrayList<SortItem1>();
    private int sub_page = 0;//页数
    private String parent_id = "";//二级分类id
    private TextView subEmpty = null;
    private int Tabselected=1;

    //城市特色
    private TextView specialView = null;
    private RefreshLoadmoreLayout cityLayout = null;// 城市特色
    private CityGridView cityGridView = null;
    private CityAdapter cityAdapter = null;
    private int city_page = 0;//页数
    private TextView emptyView = null;
    //底部索引条
    private int offset = 0;
    private int curPos = 0;
    private ImageView view = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_find);
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        //offset = dm.widthPixels / 2- BaseUtil.dip2px(getActivity(),10);
        offset =BaseUtil.dip2px(getActivity(),240)/2;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                offset, LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        Matrix matrix = new Matrix();
        matrix.postTranslate(curPos * offset, 0);
        view.setImageMatrix(matrix);
        getTypeList();
    }

    //经典分类列表接口
    public void getTypeList() {
        getNetWorker().getTypeList(Tabselected+"");
    }
    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case TYPE_LIST_GET:
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
            case TYPE_LIST_GET:
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
            case TYPE_LIST_GET:
                String keytype=hemaNetTask.getParams().get("keytype");
                sorts.clear();
                HemaArrayResult<SortItem> nResult = (HemaArrayResult<SortItem>) hemaBaseResult;
                sorts = nResult.getObjects();
                switch (keytype)
                {
                    case "1"://特色分类
                        freshTypeList();
                        if (sorts != null && sorts.size() > 0) {
                            parent_id = sorts.get(0).getId();
                            seconds.clear();
                            if(sorts.get(0).getSubs()!=null)
                                seconds.addAll(sorts.get(0).getSubs());
                            freshSubTypeList();
                        }
                        goodsLayout.setLoadmoreable(false);
                        goodsLayout.setRefreshable(false);
                        break;
                    case "2":
                        cityLayout.setLoadmoreable(false);
                        cityLayout.setRefreshable(false);
                     freshCity();
                        break;
                }

                break;
            case CITY_TYPE_LIST:
                String city_page = hemaNetTask.getParams().get("page");
                int city_sysPagesize = BaseApplication.getInstance().getSysInitInfo()
                        .getSys_pagesize();
                @SuppressWarnings("unchecked")
                HemaPageArrayResult<SortItem> citynResult = (HemaPageArrayResult<SortItem>) hemaBaseResult;
                ArrayList<SortItem> citylist = citynResult.getObjects();
                if ("0".equals(city_page)) {// 刷新
                    cityLayout.refreshSuccess();
                    sorts.clear();
                    sorts.addAll(citylist);
                    if (citylist.size() < city_sysPagesize)
                        cityLayout.setLoadmoreable(false);
                    else
                        cityLayout.setLoadmoreable(true);
                } else {// 更多
                    cityLayout.loadmoreSuccess();
                    if (citylist.size() > 0) {
                        sorts.addAll(citylist);
                        if (citylist.size() < city_sysPagesize)
                            cityLayout.setLoadmoreable(false);
                        else
                            cityLayout.setLoadmoreable(true);
                    } else {
                        cityLayout.setLoadmoreable(false);
                        XtomToastUtil.showShortToast(getActivity(), getString(R.string.hm_hlxs_txt_89));
                    }
                }
                freshCity();
                break;
            default:
                break;
        }
    }

    public void initSortList() {
        selected = new HashMap<Integer, Boolean>();

        for (int i = 0; i < sorts.size(); i++) {
            if (i == 0) {
                selected.put(i, true);
            } else {
                selected.put(i, false);
            }
        }
    }

    //刷新以及分类
    public void freshTypeList() {

        if (sorts == null || sorts.size() == 0) {
            //nothing
        } else {
            initSortList();
            if (sortAdapter == null) {
                sortAdapter = new SortAdapter(getActivity(), selected, sorts);
                leftSortListView.setAdapter(sortAdapter);
                sortAdapter.setListener(this);
            } else {
                sortAdapter.setSelectedData(selected);
                sortAdapter.setTypeData(sorts);
                sortAdapter.notifyDataSetChanged();
            }
        }
    }

    //刷新二级分类
    public void freshSubTypeList() {
        if (seconds == null || seconds.size() == 0) {
            subEmpty.setVisibility(View.VISIBLE);
            rightGoodsistView.setVisibility(View.GONE);
        } else {
            subEmpty.setVisibility(View.GONE);
            rightGoodsistView.setVisibility(View.VISIBLE);

            if (gridAdapter == null) {
                gridAdapter = new GridAdapter(getActivity(), seconds);
                gridAdapter.setEmptyString("暂无数据");
                rightGoodsistView.setAdapter(gridAdapter);
            } else {
                gridAdapter.setData(seconds);
                gridAdapter.setEmptyString("暂无数据");
                gridAdapter.notifyDataSetChanged();
            }
        }
    }

    //刷新城市
    public void freshCity() {
        if (sorts == null || sorts.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            cityGridView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            cityGridView.setVisibility(View.VISIBLE);
            if (cityAdapter == null) {
                cityAdapter = new CityAdapter(getActivity(), sorts);
                cityGridView.setAdapter(cityAdapter);
            } else {
                cityAdapter.setData(sorts);
                cityAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case TYPE_LIST_GET:

                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case TYPE_LIST_GET:
                break;
            case TYPE_LIST_SUB_GET:
                String subpage = hemaNetTask.getParams().get("page");
                if (isNull(subpage) || "0".equals(subpage)) {
                    goodsLayout.refreshFailed();
                } else {
                    goodsLayout.loadmoreFailed();
                }
                break;
            case CITY_TYPE_LIST:
                String page = hemaNetTask.getParams().get("page");
                if (isNull(page) || "0".equals(page)) {
                    cityLayout.refreshFailed();
                } else {
                    cityLayout.loadmoreFailed();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {
//        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
//        hmBackBtn.setVisibility(View.INVISIBLE);
//
//        hmBarNameView = (TextView) findViewById(R.id.bar_name);
//        hmBarNameView.setText("分类");
//
//        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
//        hmRightBtn.setVisibility(View.GONE);
//
//        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
//        hmRightTxtView.setVisibility(View.INVISIBLE);

        //经典分类
        classicalView = (TextView) findViewById(R.id.classcial);
        sortContainer = (LinearLayout) findViewById(R.id.sort);
        leftSortListView = (XtomListView) findViewById(R.id.list);
        goodsLayout = (RefreshLoadmoreLayout) findViewById(R.id.goodsrefresh);
        rightGoodsistView = (CityGridView) findViewById(R.id.grid);
        subEmpty = (TextView) findViewById(R.id.empty_sub);
        //城市特色分类
        specialView = (TextView) findViewById(special);
        cityLayout = (RefreshLoadmoreLayout) findViewById(R.id.cityrefresh);
        cityGridView = (CityGridView) findViewById(citys);
        emptyView = (TextView) findViewById(R.id.empty);

        view = (ImageView) findViewById(R.id.tab);
    }
    public void moveTabBar(final int arg0) {
        Animation animation = new TranslateAnimation(curPos * offset,
                offset * arg0, 0, 0);
        animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
        animation.setDuration(200);//动画持续时间0.2秒
        view.startAnimation(animation);
        curPos = arg0;
    }

    @Override
    protected void setListener() {
        classicalView.setOnClickListener(this);
        specialView.setOnClickListener(this);
        cityLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                city_page = 0;
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                city_page++;
            }
        });
        goodsLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                sub_page = 0;
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                sub_page++;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.classcial:
                if (sortContainer.getVisibility() == View.GONE && cityLayout.getVisibility() == View.VISIBLE) {
                    sortContainer.setVisibility(View.VISIBLE);
                    cityLayout.setVisibility(View.GONE);
                    Tabselected=1;
                    classicalView.setTextColor(getResources().getColor(R.color.white));
                    specialView.setTextColor(getResources().getColor(R.color.black));
                    moveTabBar(0);
                    getTypeList();
                }
                break;
            case special:
                if (sortContainer.getVisibility() == View.VISIBLE && cityLayout.getVisibility() == View.GONE) {
                    sortContainer.setVisibility(View.GONE);
                    cityLayout.setVisibility(View.VISIBLE);
                    classicalView.setTextColor(getResources().getColor(R.color.black));
                    specialView.setTextColor(getResources().getColor(R.color.white));
                    moveTabBar(1);
                    Tabselected=2;
                    getTypeList();
                }
                break;
        }
    }

    @Override
    public void onItemClick(SortItem sortItem) {
        if (sortItem != null) {
            seconds.clear();
            if(sortItem.getSubs()!=null)
            seconds.addAll(sortItem.getSubs());
            freshSubTypeList();
        }
    }

    @Override
    protected void lazyLoad() {
        //nothing
    }
}
