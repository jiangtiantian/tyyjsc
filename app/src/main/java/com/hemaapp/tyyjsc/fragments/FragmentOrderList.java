package com.hemaapp.tyyjsc.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseConfig;
import com.hemaapp.tyyjsc.BaseFragment;
import com.hemaapp.tyyjsc.BaseHttpInformation;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.Login;
import com.hemaapp.tyyjsc.adapters.OrderAdapter;
import com.hemaapp.tyyjsc.model.OrderInfo;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.ButtonDialog;
import com.hemaapp.tyyjsc.view.HmHelpDialog;
import java.util.ArrayList;
import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 订单
 */
public class FragmentOrderList extends BaseFragment implements OrderAdapter.OnOrderPotListener {
    private String status = "";// 	0:全部    1:待支付    2:已支付(待发货)    3:待收货    4:待评价    5:已完成    6:已关闭 7：删除
    private OrderAdapter orderAdapter = null;
    private ArrayList<OrderInfo> data = new ArrayList<>();
    private XtomListView listView = null;
    private RefreshLoadmoreLayout layout = null;
    private boolean isPrepared = false; // 标志位，标志已经初始化完成。
    private User user = null;
    private int page = 0;
    private String keytype="1";//1：商品订单;2：套餐订单
    private OrderInfo info = null;//广播发送来的订单信息或者接口回调传的订单
    private OrderStatusChangeReceiver orderStatusChangeReceiver = null;
    private HmHelpDialog delOrderDialog = null;
    private ButtonDialog dialog = null;//确认收货提示代金券

    public static FragmentOrderList newInstance(String status,String keytype) {
        FragmentOrderList newFragment = new FragmentOrderList();
        Bundle bundle = new Bundle();
        bundle.putString("status", status);
        bundle.putString("keytype", keytype);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_order_list);
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        status = args != null ? args.getString("status") : "0";
        keytype=args!=null? args.getString("keytype"):"1";
        user = BaseApplication.getInstance().getUser();
        isPrepared = true;
        orderStatusChangeReceiver = new OrderStatusChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BaseConfig.BROADCAST_ORDER);
        getActivity().registerReceiver(orderStatusChangeReceiver, intentFilter);
        lazyLoad();
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ORDER_LIST:
                showProgressDialog(R.string.hm_hlxs_txt_59);
                break;
            case REMOVE:
            case ORDER_OPE:
            case REMINDER:
            case ALLCOUPON_REMOVE:
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ORDER_LIST:
                cancelProgressDialog();
                break;
            case REMOVE:
            case ORDER_OPE:
            case REMINDER:
            case ALLCOUPON_REMOVE:
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        Intent intent = null;
        switch (information) {
            case ORDER_LIST:
                String page = hemaNetTask.getParams().get("page");
                int sysPagesize = BaseApplication.getInstance().getSysInitInfo()
                        .getSys_pagesize();
                @SuppressWarnings("unchecked")
                HemaPageArrayResult<OrderInfo> nResult = (HemaPageArrayResult<OrderInfo>) hemaBaseResult;
                ArrayList<OrderInfo> list = nResult.getObjects();
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
                        XtomToastUtil.showShortToast(getActivity(), getString(R.string.hm_hlxs_txt_89));
                    }
                }
                freshData();
                break;
            case REMINDER:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            case ORDER_OPE:
                String type=hemaNetTask.getParams().get("keytype");
                switch (type) {
                    case "1":
                        //确认收货
                        intent = new Intent();
                        intent.setAction(BaseConfig.BROADCAST_ORDER);
                        info.setStatus("4");
                        intent.putExtra("order", info);
                        getActivity().sendBroadcast(intent);
                        showTextDialog("亲，您已确认收货");
                        break;
                    case "2"://删除订单
                        showTextDialog(hemaBaseResult.getMsg());
                        intent = new Intent();
                        intent.setAction(BaseConfig.BROADCAST_ORDER);
                        info.setStatus("1".equals(hemaNetTask.getParams().get("keytype")) ? "7" : "6");
                        intent.putExtra("order", info);
                        getActivity().sendBroadcast(intent);
                        break;
                    default:
                        showTextDialog(hemaBaseResult.getMsg());
                        break;
                }
                break;
            case ALLCOUPON_REMOVE:
                showTextDialog("支付成功");
                showTextDialog(hemaBaseResult.getMsg());
                intent = new Intent();
                intent.setAction(BaseConfig.BROADCAST_ORDER);
//                info.setStatus("1".equals(hemaNetTask.getParams().get("keytype")) ? "7" : "6");
                intent.putExtra("order", info);
                getActivity().sendBroadcast(intent);
                break;
            default:
                break;
        }
    }
    //刷新订单
    public void freshData() {
        if (orderAdapter == null) {
            orderAdapter = new OrderAdapter(FragmentOrderList.this, listView, data);
            orderAdapter.setEmptyString("暂无订单");
            orderAdapter.setListener(this);
            orderAdapter.setType(keytype);
            listView.setAdapter(orderAdapter);
        } else {
            orderAdapter.setType(keytype);
            orderAdapter.setData(data);
            orderAdapter.setEmptyString("暂无订单");
            orderAdapter.setListener(this);
            orderAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ORDER_LIST:
            case REMOVE:
            case ORDER_OPE:
            case REMINDER:
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
            case ORDER_LIST:
                String page = hemaNetTask.getParams().get("page");
                if (isNull(page) || "0".equals(page)) {
                    layout.refreshFailed();
                } else {
                    layout.loadmoreFailed();
                }
                break;
            case REMOVE:
            case REMINDER:
            case ORDER_OPE:
                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {
        listView = (XtomListView) findViewById(R.id.orders);
        layout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
    }

    @Override
    protected void setListener() {
        layout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getNetWorker().getOrderList(user.getToken(),keytype,status, String.valueOf(page));
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                getNetWorker().getOrderList(user.getToken(),keytype, status, String.valueOf(page));
            }
        });
    }

    //处理订单变化
    public void onStatusChange() {
        if (info != null && data != null) {
            if ("7".equals(info.getStatus())) {//从存在该订单的列表中移除该订单
                for (OrderInfo item : data) {
                    if (info.getId().equals(item.getId())) {
                        data.remove(item);
                        break;
                    }
                }
            } else if ("0".equals(status)) {//更新全部订单列表中的订单状态
                for (OrderInfo item : data) {
                    if (info.getId().equals(item.getId())) {
                        item.setStatus(info.getStatus());
                        break;
                    }
                }
            } else {//以及删除该订单目前所处的列表
                for (OrderInfo item : data) {
                    if (info.getId().equals(item.getId())) {
                        data.remove(item);
                        break;
                    }
                }
            }
            freshData();
            //支付成功后将订单更改为待发货状态
            //将评价完成的订单置为已完成
        }
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {//每次都都加载
            return;
        }
        user = BaseApplication.getInstance().getUser();
        if(user == null){
            Intent intent = new Intent(getActivity(),Login.class);
            startActivity(intent);
        }else{
            getNetWorker().getOrderList(user.getToken(), keytype,status, String.valueOf(page));
        }
    }

    @Override
    public void onDelOrder(OrderInfo orderInfo) {
        info = orderInfo;
        if (delOrderDialog == null) {
            delOrderDialog = new HmHelpDialog(getActivity(), 0);
        }
        delOrderDialog.setLeftButtonText("取消");
        delOrderDialog.setRightButtonText("确定");
        delOrderDialog.setTitleName("确定删除订单?");
        delOrderDialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
            @Override
            public void onCancel(int type) {
                delOrderDialog.cancel();
            }

            @Override
            public void onConfirm(String msg) {
                delOrderDialog.cancel();
                getNetWorker().OrderOperation(user.getToken(),"2",info.getId());
            }
        });
        delOrderDialog.show();
    }

    @Override
    public void onCancelOrder(OrderInfo orderInfo) {
        info = orderInfo;
        if (delOrderDialog == null) {
            delOrderDialog = new HmHelpDialog(getActivity(), 0);
        }
        delOrderDialog.setLeftButtonText("取消");
        delOrderDialog.setRightButtonText("确定");
        delOrderDialog.setTitleName("确定取消订单?");
        delOrderDialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
            @Override
            public void onCancel(int type) {
                delOrderDialog.cancel();
            }

            @Override
            public void onConfirm(String msg) {
                delOrderDialog.cancel();
                if("2".equals(info.getStatus()))
                   getNetWorker().OrderOperation(user.getToken(),"3",info.getId());
                else
                    getNetWorker().OrderOperation(user.getToken(),"4",info.getId());
            }
        });
        delOrderDialog.show();
    }

    @Override
    public void onConfirmOrder(OrderInfo orderInfo) {
        info = orderInfo;
        if (delOrderDialog == null) {
            delOrderDialog = new HmHelpDialog(getActivity(), 0);
        }
        delOrderDialog.setLeftButtonText("取消");
        delOrderDialog.setRightButtonText("确定");
        delOrderDialog.setTitleName("确定收货?");
        delOrderDialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
            @Override
            public void onCancel(int type) {
                delOrderDialog.cancel();
            }

            @Override
            public void onConfirm(String msg) {
                delOrderDialog.cancel();
                getNetWorker().OrderOperation(user.getToken(),"1",info.getId());
            }
        });
        delOrderDialog.show();
    }

    @Override
    public void onRemindOrder(OrderInfo orderInfo) {
        info = orderInfo;
        if (delOrderDialog == null) {
            delOrderDialog = new HmHelpDialog(getActivity(), 0);
        }
        delOrderDialog.setLeftButtonText("取消");
        delOrderDialog.setRightButtonText("确定");
        delOrderDialog.setTitleName("确定提醒卖家发货?");
        delOrderDialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
            @Override
            public void onCancel(int type) {
                delOrderDialog.cancel();
            }

            @Override
            public void onConfirm(String msg) {
                delOrderDialog.cancel();
                reminderOrder();
            }
        });
        delOrderDialog.show();
    }

    //提醒发货
    public void reminderOrder() {
        if (user != null && info != null) {
            getNetWorker().OrderOperation(user.getToken(),"5",info.getId());
        }
    }

    class OrderStatusChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            info = (OrderInfo) intent.getSerializableExtra("order");
            onStatusChange();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(orderStatusChangeReceiver);
    }
}
