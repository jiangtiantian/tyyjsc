/*
 * Copyright (C) 2014 The Android Client Of Demo Project
 * 
 *     The BeiJing PingChuanJiaHeng Technology Co., Ltd.
 * 
 * Author:Yang ZiTian
 * You Can Contact QQ:646172820 Or Email:mail_yzt@163.com
 */
package com.hemaapp.tyyjsc.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.BaseApplication;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivityOrderInfo;
import com.hemaapp.tyyjsc.activity.ActivityPay;
import com.hemaapp.tyyjsc.activity.ActivitySetPayPwd;
import com.hemaapp.tyyjsc.fragments.FragmentOrderList;
import com.hemaapp.tyyjsc.model.OrderInfo;
import com.hemaapp.tyyjsc.model.User;
import com.hemaapp.tyyjsc.view.HmHelpDialog;
import com.hemaapp.tyyjsc.view.NoScrollListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;


/**
 * 订单列表； 退款订单列表适配器
 */
public class OrderAdapter extends HemaAdapter implements OnClickListener {

    private XtomListView listView = null;
    private ArrayList<OrderInfo> data = null;
    private ImageLoader loader = null;
    private FragmentOrderList context = null;
    private OnOrderPotListener listener = null;
    private String type = "1";//1：商品订单;2：套餐订单

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OrderAdapter(FragmentOrderList mContext, XtomListView listView, ArrayList<OrderInfo> data) {
        super(mContext);
        this.listView = listView;
        this.data = data;
        context = mContext;
        loader = ImageLoader.getInstance();
    }

    public void setData(ArrayList<OrderInfo> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null || data.size() == 0 ? 1 : data.size();
    }

    @Override
    public boolean isEmpty() {
        return data == null || data.size() == 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isEmpty())
            return getEmptyView(parent);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context.getActivity()).inflate(
                    R.layout.order_item, null);
            holder = new ViewHolder();
            holder.view = convertView.findViewById(R.id.father);
            holder.firstBtn = (TextView) convertView.findViewById(R.id.first);
            holder.secondBtn = (TextView) convertView.findViewById(R.id.second);
            holder.orderSnView = (TextView) convertView.findViewById(R.id.order_sn);
            holder.orderStatusView = (TextView) convertView.findViewById(R.id.order_state);
            holder.order_exchange = (TextView) convertView.findViewById(R.id.order_exchange);
            holder.orderNumView = (TextView) convertView.findViewById(R.id.num);
            holder.orderMoneyView = (TextView) convertView.findViewById(R.id.order_money);
            holder.list = (NoScrollListView) convertView.findViewById(R.id.list);
            holder.item_lay = (LinearLayout) convertView.findViewById(R.id.item_lay);
            holder.img = (ImageView) convertView.findViewById(R.id.item_img);
            holder.itemNameView = (TextView) convertView.findViewById(R.id.goods_name);
            holder.priceView = (TextView) convertView.findViewById(R.id.goods_price);
            holder.goods_num = (TextView) convertView.findViewById(R.id.goods_num);
            holder.specNameView = (TextView) convertView.findViewById(R.id.goods_spec);
            holder.send_num = (TextView) convertView.findViewById(R.id.send_num);
            holder.send_time = (TextView) convertView.findViewById(R.id.send_time);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }
        OrderInfo info = data.get(position);
        holder.orderSnView.setText("订单号: " + info.getOrdernum());
        if ("2".equals(type)) {
            holder.item_lay.setVisibility(View.VISIBLE);
            holder.list.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(info.getPackageimgurl(), holder.img, BaseUtil.displayImageOption());
            holder.img.setTag(info.getPackageimgurl());
            holder.itemNameView.setText(info.getPackagename());
            holder.goods_num.setText("×" + "1");
            holder.send_num.setText("发货次数：" + info.getPackagefhimes());
            holder.send_time.setText("发货间隔：" + info.getPackageshipdays());
        } else {
            holder.list.setAdapter(new OrderListAdapter(context.getActivity(), info.getGoods(), info.getStatus(), info.getKeytype()));
        }
        if ("5".equals(info.getKeytype())) {
            holder.priceView.setText(info.getAllcoupon() + "点券");
            holder.orderNumView.setText("共" + info.getGoodsnum() + "件商品/合计" + info.getAllcoupon() + "点券");
        } else {
            holder.priceView.setText("¥" + info.getPackageprice());
            holder.goods_num.setText("×" + info.getGoodsnum());
            holder.orderNumView.setText("共" + info.getGoodsnum() + "件商品/合计￥" + info.getAllprice());
        }
        holder.orderNumView.setVisibility(View.VISIBLE);

        /**
         *
         1:待支付
         2:已支付(待发货)
         3:待收货
         4:待评价
         5:已完成
         6:已关闭
         */
        String status = "";
        if ("5".equals(info.getKeytype())) {
            holder.order_exchange.setVisibility(View.VISIBLE);
        } else {
            holder.order_exchange.setVisibility(View.GONE);
        }
        switch (Integer.parseInt(info.getStatus().toString().trim())) {
            case 1:
                status = "待付款";
                holder.firstBtn.setVisibility(View.VISIBLE);
                holder.firstBtn.setText("取消订单");
                holder.firstBtn.setOnClickListener(this);
                holder.firstBtn.setTag(R.id.edit, "0");
                holder.orderMoneyView.setVisibility(View.VISIBLE);
                if ("2".equals(type))
                    holder.orderMoneyView.setText("七天未支付，该订单自动取消");
                else
                    holder.orderMoneyView.setVisibility(View.INVISIBLE);
                holder.secondBtn.setVisibility(View.VISIBLE);
                holder.secondBtn.setOnClickListener(this);
                holder.secondBtn.setTag(R.id.del, "0");
                holder.secondBtn.setText("去 付 款");
                break;
            case 2:
                status = "待发货";
                holder.firstBtn.setText("取消订单");
                holder.firstBtn.setTag(R.id.edit, "0");
                if ("5".equals(info.getKeytype())) {
                    holder.firstBtn.setVisibility(View.INVISIBLE);
                } else {
                    holder.firstBtn.setVisibility(View.VISIBLE);
                }

                holder.firstBtn.setOnClickListener(this);
                holder.orderMoneyView.setVisibility(View.INVISIBLE);
                holder.secondBtn.setVisibility(View.VISIBLE);
                holder.secondBtn.setOnClickListener(this);
                holder.secondBtn.setTag(R.id.del, "1");
                holder.secondBtn.setText("提醒发货");
                break;
            case 3:
                status = "待收货";
                holder.orderMoneyView.setVisibility(View.INVISIBLE);
                holder.firstBtn.setVisibility(View.GONE);
                holder.secondBtn.setVisibility(View.VISIBLE);
                holder.secondBtn.setText("确认收货");
                holder.secondBtn.setOnClickListener(this);
                holder.secondBtn.setTag(R.id.del, "2");
                break;
            case 4:
                status = "待评价";
                holder.firstBtn.setVisibility(View.VISIBLE);
                holder.firstBtn.setText("删除订单");
                holder.orderMoneyView.setVisibility(View.INVISIBLE);
                holder.firstBtn.setOnClickListener(this);
                holder.firstBtn.setTag(R.id.edit, "1");
                holder.secondBtn.setVisibility(View.VISIBLE);
                holder.secondBtn.setText("去 评 价");
                holder.secondBtn.setOnClickListener(this);
                holder.secondBtn.setTag(R.id.del, "3");
                break;
            case 5:
                status = "已完成";
                holder.orderMoneyView.setVisibility(View.INVISIBLE);
                holder.firstBtn.setVisibility(View.VISIBLE);
                holder.secondBtn.setVisibility(View.GONE);
                holder.firstBtn.setText("删除订单");
                holder.firstBtn.setOnClickListener(this);
                holder.firstBtn.setTag(R.id.edit, "1");
                break;
            case 6:
                status = "已关闭";
                holder.orderMoneyView.setVisibility(View.INVISIBLE);
                holder.firstBtn.setVisibility(View.VISIBLE);
                holder.secondBtn.setVisibility(View.GONE);
                holder.firstBtn.setText("删除订单");
                holder.firstBtn.setOnClickListener(this);
                holder.firstBtn.setTag(R.id.edit, "1");
                break;
            default:
                holder.orderMoneyView.setVisibility(View.INVISIBLE);
                break;
        }
        holder.firstBtn.setTag(R.id.type, info);
        holder.secondBtn.setTag(R.id.type, info);

        holder.orderStatusView.setText(status);
        holder.view.setOnClickListener(this);
        holder.view.setTag(R.id.type, info);
        holder.item_lay.setOnClickListener(this);
        holder.item_lay.setTag(R.id.type, info);
        return convertView;
    }

    private static class ViewHolder {
        private View view;
        private TextView firstBtn = null;
        private TextView secondBtn = null;
        private TextView orderSnView = null;
        private TextView orderStatusView = null;
        private NoScrollListView list = null;
        private TextView orderNumView = null;
        private TextView orderMoneyView = null;
        private LinearLayout item_lay;
        private ImageView img;
        private TextView priceView;
        private TextView itemNameView;
        private TextView send_num, send_time;
        private TextView specNameView;
        private TextView goods_num;
        private TextView order_exchange;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        OrderInfo orderInfo = null;
        switch (v.getId()) {
            case R.id.father:
            case R.id.item_lay:
                orderInfo = (OrderInfo) v.getTag(R.id.type);
                if (orderInfo == null) {
                    return;
                }
                intent = new Intent(context.getActivity(), ActivityOrderInfo.class);
                intent.putExtra("id", orderInfo.getId());
                intent.putExtra("type", this.type);
                context.startActivity(intent);
                break;
            case R.id.first:
                String type = v.getTag(R.id.edit).toString();
                orderInfo = (OrderInfo) v.getTag(R.id.type);
                if (orderInfo == null) {
                    return;
                }
                if ("0".equals(type)) {//取消订单
                    if (listener != null) {
                        listener.onCancelOrder(orderInfo);
                    }
                } else if ("1".equals(type)) {//删除订单
                    if (listener != null) {
                        listener.onDelOrder(orderInfo);
                    }
                }
                break;
            case R.id.second:
                orderInfo = (OrderInfo) v.getTag(R.id.type);
                if (orderInfo == null) {
                    return;
                }

                String mark = v.getTag(R.id.del).toString();
                if ("0".equals(mark)) {
                    if ("5".equals(orderInfo.getKeytype())) {
                        showPwdPop(orderInfo.getId());
                    } else {
                        intent = new Intent(context.getActivity(), ActivityPay.class);
//                        intent.putExtra("keytype","2");//购买商品
                        intent.putExtra("keytype", this.type);//购买商品
                        intent.putExtra("money", orderInfo.getMoney());//支付金额
                        intent.putExtra("order_id", orderInfo.getId());
                        intent.putExtra("order", orderInfo);
//                        intent.putExtra("from",this.type);//订单列表
                        intent.putExtra("from", "4");//订单列表
                        context.startActivity(intent);
                    }
                } else if ("1".equals(mark)) {
                    if (listener != null) {//提醒发货
                        listener.onRemindOrder(orderInfo);
                    }
                } else if ("2".equals(mark)) {//确认收货
                    if (listener != null) {
                        listener.onConfirmOrder(orderInfo);
                    }
                } else if ("3".equals(mark)) {
                    intent = new Intent(context.getActivity(), ActivityOrderInfo.class);
                    intent.putExtra("id", orderInfo.getId());
                    intent.putExtra("type", this.type);
                    context.startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private HmHelpDialog dialog = null;//密码提示框

    private void showPwdPop(final String id) {
        if (dialog == null) {
            dialog = new HmHelpDialog(mContext, 2);
        }
        dialog.setPayTxt("支付");
        dialog.setTitleName("请输入支付密码");
        dialog.setLeftButtonText("取消");
        dialog.setRightButtonText("确定");
        dialog.setListener(new HmHelpDialog.onPwdListener() {
            @Override
            public void onSetPwd() {//设置密码
                Intent intent = new Intent(mContext, ActivitySetPayPwd.class);
                mContext.startActivity(intent);
            }

            @Override
            public void onGetPwd() {
                Intent intent = new Intent(mContext, ActivitySetPayPwd.class);
                mContext.startActivity(intent);
            }
        });

        dialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
            @Override
            public void onCancel(int type) {
                dialog.cancel();
            }

            @Override
            public void onConfirm(String msg) {
                dialog.cancel();
                String password = dialog.getPassword();
                User user = BaseApplication.getInstance().getUser();
                dialog.clearPwdET();
                if (user.getPaypassword() == null || "".equals(user.getPaypassword())) {
                    XtomToastUtil.showShortToast(mContext, "请先设置支付密码");
                    return;
                }
                if (isNull(password)) {
                    XtomToastUtil.showShortToast(mContext, "请输入支付密码");
                    return;
                }
                context.getNetWorker().allcouponRemove(user.getToken(), id, password);
            }
        });
        dialog.show();
    }

    public void setListener(OnOrderPotListener listener) {
        this.listener = listener;
    }

    public interface OnOrderPotListener {
        void onDelOrder(OrderInfo orderInfo);

        void onCancelOrder(OrderInfo orderInfo);

        void onConfirmOrder(OrderInfo orderInfo);

        void onRemindOrder(OrderInfo orderInfo);
    }
}
