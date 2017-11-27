package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.RecordInfor;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;

/**
 * Created by wangyuxia on 2017/8/17.
 * 朋友圈数据适配器
 */
public class CircleAdapter extends HemaAdapter{

    private ArrayList<RecordInfor> infors;

    public CircleAdapter(Context mContext, ArrayList<RecordInfor> infors) {
        super(mContext);
        this.infors = infors;
    }

    @Override
    public boolean isEmpty() {
        if(infors == null || infors.size() == 0)
            return true;
        return false;
    }

    @Override
    public int getCount() {
        return infors == null || infors.size() == 0 ? 1 : infors.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(isEmpty())
            return getEmptyView(viewGroup);
        ViewHolder holder;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.listitem_circle, null);
            holder = new ViewHolder();
            findview(holder, view);
            view.setTag(R.id.button, holder);
        }else{
            holder = (ViewHolder) view.getTag(R.id.button);
        }

        RecordInfor infor = infors.get(i);
        if(i == 0)
            holder.layout_top.setVisibility(View.VISIBLE);
        else
            holder.layout_top.setVisibility(View.GONE);
        holder.tv_username.setText(infor.getUsername());
        holder.tv_nickname.setText(infor.getNickname());
        holder.tv_money.setText("￥"+infor.getMoney());
        holder.tv_convertmoney.setText("￥"+infor.getConvertmoney());
        return view;
    }

    private void findview(ViewHolder holder, View view){
        holder.layout_top = (LinearLayout) view.findViewById(R.id.layout_top);
        holder.tv_username = (TextView) view.findViewById(R.id.tv_username);
        holder.tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
        holder.tv_money = (TextView) view.findViewById(R.id.tv_money);
        holder.tv_convertmoney = (TextView) view.findViewById(R.id.tv_convertmoney);
    }

    private static class ViewHolder{
        LinearLayout layout_top;
        TextView tv_username;
        TextView tv_nickname;
        TextView tv_money;
        TextView tv_convertmoney;
    }
}
