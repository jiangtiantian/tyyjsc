package com.hemaapp.tyyjsc.adapters;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.MyDealItem;

import java.util.ArrayList;

import static com.hemaapp.tyyjsc.R.id.reason;

/**
 * 账户余额适配器
 */
public class DealCountNoteAdapter extends HemaAdapter {

    private ArrayList<MyDealItem> data = null;
    private boolean isHidden = false;

    public DealCountNoteAdapter(Context mContext, ArrayList<MyDealItem> data) {
        super(mContext);
        this.data = data;
    }

    //是否隐藏第一项分割栏
    public void isHiddenFirstDivider(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public void setData(ArrayList<MyDealItem> data) {
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
        MyDealItem item = data.get(position);
        holder.reason.setText(data.get(position).getContent());
        holder.money.setText(data.get(position).getAmount());
        holder.date.setText(data.get(position).getRegdate());
        switch (item.getKeytype()) {
            case "1":
                holder.reason.setText("支付宝充值");
                holder.money.setTextColor(ContextCompat.getColor(mContext, R.color.my_red));
                break;
            case "2":
                holder.reason.setText("银联充值");
                holder.money.setTextColor(ContextCompat.getColor(mContext, R.color.my_red));
                break;
            case "3":
                holder.reason.setText("微信充值");
                holder.money.setTextColor(ContextCompat.getColor(mContext, R.color.my_red));
                break;
            case "4":
                holder.reason.setText("购物");
                holder.money.setTextColor(ContextCompat.getColor(mContext, R.color.gouwu));
                break;
            case "5":
                holder.reason.setText("退款");
                holder.money.setTextColor(ContextCompat.getColor(mContext, R.color.my_red));
                break;
            case "6":
                holder.reason.setText("后台充值");
                holder.money.setTextColor(ContextCompat.getColor(mContext, R.color.my_red));
                break;
            case "7":
                holder.reason.setText("积分兑换");
                holder.money.setTextColor(ContextCompat.getColor(mContext, R.color.my_red));
                break;
            case "8":
                holder.reason.setText("支付宝提现");
                holder.money.setTextColor(ContextCompat.getColor(mContext, R.color.gouwu));
                break;
            case "9":
                holder.reason.setText("银行卡提现");
                holder.money.setTextColor(ContextCompat.getColor(mContext, R.color.gouwu));
                break;
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