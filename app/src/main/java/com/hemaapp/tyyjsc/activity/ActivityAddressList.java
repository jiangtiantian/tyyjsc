package com.hemaapp.tyyjsc.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.adapters.AddressAdapter;
import com.hemaapp.tyyjsc.model.Address;
import com.hemaapp.tyyjsc.model.User;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 地址列表
 */
public class ActivityAddressList extends BaseActivity implements View.OnClickListener, AddressAdapter.onAddressListener {

    private ImageButton hmBackBtn = null;//返回
    private TextView hmBarNameView = null;//标题栏名字
    private ImageButton hmRightBtn = null;//标题栏右侧图标按钮
    private TextView hmRightTxtView = null;//标题栏右侧微文字按钮

    private XtomListView listView = null;
    private AddressAdapter addressAdapter = null;
    private ArrayList<Address> data = new ArrayList<>();
    private RefreshLoadmoreLayout layout = null;
    private int page = 0;
    private TextView addViewBtn = null;//新增收货地址
    private User user = null;//用户信息
    private Address address= null;//默认地址
    private AddressInfoReceiver addressInfoReceiver = null;//响应地址修改新增
    // 页面来源 1 提交订单页面; 0我的页面
    private String which = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_address_list);
        super.onCreate(savedInstanceState);
        setColor(mContext);
        user = BaseApplication.getInstance().getUser();
        addressInfoReceiver = new AddressInfoReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BaseConfig.BROADCAST_ADDRESS);
        registerReceiver(addressInfoReceiver, intentFilter);
        getAddressList();
    }
    //获取地址列表
    public void getAddressList(){
        getNetWorker().getAddressList(user == null ? "" : user.getToken());
    }
    @Override
    protected void findView() {
        //标题
        hmBackBtn = (ImageButton) findViewById(R.id.back_left);
        hmBackBtn.setVisibility(View.VISIBLE);

        hmBarNameView = (TextView) findViewById(R.id.bar_name);
        hmBarNameView.setText(R.string.hm_hlxs_txt_50);

        hmRightBtn = (ImageButton) findViewById(R.id.bar_right_img);
        hmRightBtn.setVisibility(View.GONE);

        hmRightTxtView = (TextView) findViewById(R.id.bar_right_txt);
        hmRightTxtView.setVisibility(View.INVISIBLE);
        hmRightTxtView.setText(R.string.hm_hlxs_txt_51);

        listView = (XtomListView)findViewById(R.id.addresslist);
        layout = (RefreshLoadmoreLayout)findViewById(R.id.refreshLoadmoreLayout);
        addViewBtn = (TextView)findViewById(R.id.add);
    }

    @Override
    protected void getExras() {
        which = getIntent().getStringExtra("which");
        which = isNull(which) ? "0" : which;
    }

    @Override
    protected void setListener() {
        if("0".equals(which)){
            hmBarNameView.setText(R.string.hm_hlxs_txt_52);
        }else
            hmBarNameView.setText(R.string.hm_hlxs_txt_53);

        hmBackBtn.setOnClickListener(this);
        layout.setLoadmoreable(false);
        layout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getAddressList();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                //暂无刷新的操作
                layout.loadmoreSuccess();

            }
        });
        addViewBtn.setOnClickListener(this);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDRESS_LIST:
            case ADDRESS_DEFAULT:
            case REMOVE:
                showProgressDialog(getString(R.string.hm_hlxs_txt_54));
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDRESS_LIST:
            case ADDRESS_DEFAULT:
            case REMOVE:
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
            case ADDRESS_LIST:
                int sysPagesize = BaseApplication.getInstance().getSysInitInfo()
                        .getSys_pagesize();
                HemaArrayResult<Address> nResult = (HemaArrayResult<Address>) hemaBaseResult;
                ArrayList<Address> list = nResult.getObjects();
                    layout.refreshSuccess();
                    data.clear();
                    data.addAll(list);
//                    if (list.size() < sysPagesize)
//                        layout.setLoadmoreable(false);
//                    else
//                        layout.setLoadmoreable(true);
//                else {// 更多
//                    layout.loadmoreSuccess();
//                    if (list.size() > 0) {
//                        data.addAll(list);
//                        if (list.size() < sysPagesize)
//                            layout.setLoadmoreable(false);
//                        else
//                            layout.setLoadmoreable(true);
//                    } else {
//                        layout.setLoadmoreable(false);
//                        XtomToastUtil.showShortToast(ActivityAddressList.this, getString(R.string.hm_hlxs_txt_55));
//                    }
//                }
                freshData();
                break;
            case ADDRESS_DEFAULT:
                String keytype = hemaNetTask.getParams().get("keytype");
                if("2".equals(keytype))
                {
                    if(address != null){
                        for(Address a : data){
                            if(address.getId().equals(a.getId())){
                                a.setRec("1");
                            }else{
                                a.setRec("2");
                            }
                        }
                        addressAdapter.notifyDataSetChanged();
                    }
                }else
                {
                    if(address != null){
                        for(Address a : data){
                            if(address.getId().equals(a.getId())){
                                data.remove(a);
                                break;
                            }
                        }
                        addressAdapter.notifyDataSetChanged();
                    }
                }

                break;
            default:
                break;
        }
    }
    public void freshData(){
        if(addressAdapter == null){
            addressAdapter = new AddressAdapter(ActivityAddressList.this, data, which);
            addressAdapter.setEmptyString(getString(R.string.hm_hlxs_txt_56));
            listView.setAdapter(addressAdapter);
            addressAdapter.setListener(this);
        }else{
            addressAdapter.setEmptyString(getString(R.string.hm_hlxs_txt_57));
            addressAdapter.setData(data);
            addressAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDRESS_LIST:
            case ADDRESS_DEFAULT:
            case REMOVE:
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
            case ADDRESS_LIST:
                    layout.loadmoreFailed();
                break;
            case ADDRESS_DEFAULT:
                break;
            case REMOVE:
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back_left){
            finish();
        }else if(v.getId() == R.id.add){
            Intent intent = new Intent(ActivityAddressList.this, ActivityAddressEdit.class);
            intent.putExtra("keytype", "1");//0编辑收货地址；1 新增收货地址
            startActivity(intent);
        }
    }

    @Override
    public void onDefClick(Address address) {
        if(user!= null && address != null){
            this.address = address;
            getNetWorker().setDefAddress(user.getToken(),"2",address.getId());
        }
    }

    @Override
    public void onDelClick(Address address) {
        if(user!= null && address != null){
            this.address = address;
            getNetWorker().setDefAddress(user.getToken(), "1", address.getId());
        }
    }

    @Override
    public void onSelect(Address address) {
        if(address != null){
            Intent intent = new Intent();
            intent.putExtra("key", address);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(addressInfoReceiver);
    }
    //更新地址列表
    public void updateAddressList(Address address) {
        boolean isNew = true;//是否为新增地址
        if (data != null) {
            for (Address a : data) {
                if (address.getId().equals(a.getId())) {//编辑地址，更新地址
                    a.setId(address.getId());
                    a.setName(address.getName());
                    a.setTel(address.getTel());
                    a.setProvince(address.getProvince());
                    a.setCity(address.getCity());
                    a.setProvince_id(address.getProvince_id());
                    a.setAddress(address.getAddress());
                    a.setCity_id(address.getCity_id());
                    a.setDistrict_id(address.getDistrict_id());
                    a.setDistrict(address.getDistrict());
                    a.setRec(address.getRec());
                    a.setZipcode(address.getZipcode());
                    isNew = false;
                    break;
                }
            }
            if(isNew){
                data.add(address);
            }
            if(addressAdapter != null){
                addressAdapter.notifyDataSetChanged();
            }
        }
    }
    //广播消息接收器
    class AddressInfoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            Address address = (Address)intent.getSerializableExtra("address");
//            updateAddressList(address);
            getAddressList();

        }
    }
}
