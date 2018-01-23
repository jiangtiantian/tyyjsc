package com.hemaapp.tyyjsc.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseFragment;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivityMemeberGoodsInfo;
import com.hemaapp.tyyjsc.adapters.MembergoodsListAdapter;
import com.hemaapp.tyyjsc.model.MembergoodsList;

import java.util.ArrayList;
import java.util.List;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;


/**
 * 会员专区(全款购车)
 */

public class FragmentMember extends BaseFragment  {
    private TextView bar_name;
    private ImageButton back_left;
    private XtomListView listview;
    private RefreshLoadmoreLayout refresh_layout;
    private List<MembergoodsList> datas=new ArrayList<MembergoodsList>();
    private MembergoodsListAdapter adapter;
    private int page=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_member);
        super.onCreate(savedInstanceState);
        getNetWorker().membergoodsList("1","",String.valueOf(page));
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog(R.string.hm_hlxs_txt_34);
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEMBERGOODS_LIST:
                HemaPageArrayResult<MembergoodsList> result= (HemaPageArrayResult<MembergoodsList>) hemaBaseResult;

               // datas.addAll(result.getObjects()); //添加到集合里面去

                String page1 = hemaNetTask.getParams().get("page");
                int sysPagesize = BaseApplication.getInstance().getSysInitInfo()
                        .getSys_pagesize();
                List<MembergoodsList> list1= result.getObjects();
                if ("0".equals(page1)) {// 刷新
                    refresh_layout.refreshSuccess();
                    datas.clear();
                    datas.addAll(list1);
                    adapter.notifyDataSetChanged();
                    if (list1.size() < sysPagesize)
                        refresh_layout.setLoadmoreable(false);
                    else
                        refresh_layout.setLoadmoreable(true);
                } else {
                    refresh_layout.loadmoreSuccess();
                    if (list1.size() > 0) {
                        datas.addAll(list1);
                        if (list1.size() < sysPagesize)
                            refresh_layout.setLoadmoreable(false);
                        else
                            refresh_layout.setLoadmoreable(true);
                    } else {
                        refresh_layout.setLoadmoreable(false);
                        XtomToastUtil.showShortToast(getActivity(), getString(R.string.hm_hlxs_txt_89));
                    }
                    adapter.notifyDataSetChanged();
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {

        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        back_left= (ImageButton) findViewById(R.id.back_left);
        bar_name= (TextView) findViewById(R.id.bar_name);
        listview= (XtomListView) findViewById(R.id.listview);

        bar_name.setText("全款购车");
        back_left.setVisibility(View.GONE);
        refresh_layout= (RefreshLoadmoreLayout) findViewById(R.id.refresh_layout);

        adapter=new MembergoodsListAdapter(getActivity(),datas);
        listview.setAdapter(adapter);

    }

    @Override
    protected void setListener() {
        refresh_layout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page=0;
                getNetWorker().membergoodsList("1","",String.valueOf(page));

            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                getNetWorker().membergoodsList("1","",String.valueOf(page));

            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), ActivityMemeberGoodsInfo.class).
                        putExtra("id",datas.get(position).getId()).putExtra("type","5"));
            }
        });
    }
}
