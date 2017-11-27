package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.BankInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by wangyuxia on 2017/8/11.
 * 银行列表的数据适配器
 */
public class BankListAdapter extends HemaAdapter{

    private ArrayList<BankInfo> infors;
    public BankListAdapter(Context mContext, ArrayList<BankInfo> infors) {
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
        if(view== null){
            view = LayoutInflater.from(mContext).inflate(R.layout.listitem_bank, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(R.id.TAG, holder);
        }else{
            holder = (ViewHolder) view.getTag(R.id.TAG);
        }

        holder.tv_name.setText(infors.get(i).getName());
        return view;
    }

    private static class ViewHolder{
        TextView tv_name;
    }
}
