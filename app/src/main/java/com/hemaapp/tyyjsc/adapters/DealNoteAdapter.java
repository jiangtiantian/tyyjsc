package com.hemaapp.tyyjsc.adapters;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.DealItem;

import java.util.ArrayList;

import static com.hemaapp.tyyjsc.R.id.reason;

/**
 * 交易明细适配器
 */
public class DealNoteAdapter extends HemaAdapter {

    private ArrayList<DealItem> data = null;
    private String keytype = "0";//账户余额 0 、1.积分 2.储值卡明细
    private boolean isHidden = false;

    public DealNoteAdapter(Context mContext, ArrayList<DealItem> data) {
        super(mContext);
        this.data = data;
        log_e("-------" + data.toString());
    }
    //是否隐藏第一项分割栏
    public void isHiddenFirstDivider(boolean isHidden){
        this.isHidden = isHidden;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    public void setData(ArrayList<DealItem> data) {
        this.data = data;
    }

    @Override
    public boolean isEmpty() {
        return data == null || data.size() == 0;
    }

    @Override
    public int getCount() {
        return (data == null || data.size() == 0) ? 1 : data.size();
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
        if (isEmpty()) {
            return getEmptyView(parent);
        }
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.deal_note_item, null);
            holder = new ViewHolder();
            holder.reason = (TextView) convertView.findViewById(reason);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.money = (TextView) convertView.findViewById(R.id.money);
            holder.top = convertView.findViewById(R.id.top);
            holder.middle = convertView.findViewById(R.id.middle);
            holder.bottom = convertView.findViewById(R.id.bottom);
            holder.divider = convertView.findViewById(R.id.divider);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        DealItem item = data.get(position);
        if ("1".equals(keytype)) {//积分明细
            //交易类型 	1：消费得积分 2：每日签到 3：积分购买
            switch (item.getKeytype())
            {
                case"1":
                    holder.reason.setText("消费积分");
                    break;
                case "2":
                    holder.reason.setText("每日签到");
                    break;
                case "3":
                    holder.reason.setText("积分购买");
                    break;
                case "4":
                    holder.reason.setText("积分兑换金额");

                    break;
                case "5":
                    holder.reason.setText("退款 ");
                    break;
                case "6":
                    holder.reason.setText("后台充值 ");
                    break;
                case "7":
                    holder.reason.setText("积分兑换");
                    break;
            }
            String str = "";
            holder.money.setText(str + item.getScore_num());
            String content=str + item.getScore_num();
            if (content.contains("+")){
                holder.money.setTextColor(ContextCompat.getColor(mContext,R.color.my_red));
            }else{
                holder.money.setTextColor(ContextCompat.getColor(mContext,R.color.gouwu));
            }
        } else if("0".equals(keytype)) {
             //余额交易明细
            //1:支付宝充值 2：银联充值 3：微信充值 4：购物 5：退款 6.后台充值
            String str = "";
            String reason = "";
            switch (item.getKeytype()) {
                case "1":
                    holder.reason.setText("支付宝充值");
                    break;
                case "2":
                    holder.reason.setText("银联充值");
                    break;
                case "3":
                    holder.reason.setText("微信充值");
                    break;
                case "4":
                    holder.reason.setText("购物");
                    break;
                case "5":
                    holder.reason.setText("退款");
                    break;
                case "6":
                    holder.reason.setText("后台充值");
                    break;
            }
                holder.money.setText(str + item.getAmount());
            }else
        {
            holder.reason.setText("￥"+item.getMoney());
            holder.money.setText("￥"+item.getAmount());
        }
        holder.date.setText(item.getRegdate());
        //如果只有一条数据 显示 top bottom, 隐藏middle
        if (getCount() == 1) {
            holder.top.setVisibility(View.VISIBLE);
            holder.bottom.setVisibility(View.VISIBLE);
            holder.middle.setVisibility(View.INVISIBLE);
            if(isHidden){
                holder.divider.setVisibility(View.GONE);
            }else{
                holder.divider.setVisibility(View.VISIBLE);
            }
        } else {
            if (position == 0) {
                holder.top.setVisibility(View.VISIBLE);
                holder.bottom.setVisibility(View.INVISIBLE);
                holder.middle.setVisibility(View.VISIBLE);
                if(isHidden){
                    holder.divider.setVisibility(View.GONE);
                }else{
                    holder.divider.setVisibility(View.VISIBLE);
                }
            } else if (position == getCount() - 1) {
                holder.top.setVisibility(View.INVISIBLE);
                holder.bottom.setVisibility(View.VISIBLE);
                holder.middle.setVisibility(View.INVISIBLE);
                holder.divider.setVisibility(View.GONE);
            } else {
                holder.top.setVisibility(View.INVISIBLE);
                holder.bottom.setVisibility(View.INVISIBLE);
                holder.middle.setVisibility(View.VISIBLE);
                holder.divider.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView reason;
        private TextView date;
        private TextView money;

        private View top;
        private View bottom;
        private View middle;

        private View divider = null;
    }
}