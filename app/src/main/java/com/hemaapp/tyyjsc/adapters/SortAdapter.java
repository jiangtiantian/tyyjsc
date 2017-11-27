package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.SortItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 一级分类适配器
 */
public class SortAdapter extends HemaAdapter implements View.OnClickListener{

    private HashMap<Integer, Boolean> selectedData = new HashMap<>();
    private ArrayList<SortItem> data = new ArrayList<>();
    private onItemClickListener listener = null;

    public SortAdapter(Context context, HashMap<Integer, Boolean> selectedData, ArrayList<SortItem> data) {
        super(context);

        this.selectedData = selectedData;
        this.data = data;
    }

    public void setSelectedData(HashMap<Integer, Boolean> selectedData) {
        this.selectedData = selectedData;
        getCount();
        notifyDataSetChanged();
    }
    public void setTypeData(ArrayList<SortItem> data){
        this.data = data;
    }

    @Override
    public int getCount() {

        return data == null ? 0 : data.size();
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sort_item, null);
            holder.sort = (TextView) convertView.findViewById(R.id.item_name);
            holder.layout = (FrameLayout) convertView.findViewById(R.id.container);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        SortItem item = data.get(position);
        holder.sort.setText(item.getName());
        if (selectedData.get(position)) {
            holder.sort.setTextColor(mContext.getResources().getColor(R.color.index_search_bg));
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.sort_bg_black));
        } else {
            holder.sort.setTextColor(mContext.getResources().getColor(R.color.sort_txt_n));
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.sort_bg_white));
        }
        convertView.setOnClickListener(this);
        convertView.setTag(R.id.banner,item);
        convertView.setTag(R.id.label, String.valueOf(position));
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int pos = Integer.parseInt((String)v.getTag(R.id.label));
        selectedData.clear();
        for (int i = 0; i < data.size(); i++) {
            if (i == pos) {
                selectedData.put(i, true);
            } else {
                selectedData.put(i, false);
            }
        }
        notifyDataSetChanged();
        SortItem sortItem = (SortItem)v.getTag(R.id.banner);
        if(listener != null && sortItem != null){
            listener.onItemClick(sortItem);
        }
    }

    static class ViewHolder {
        private TextView sort;
        private FrameLayout layout;
    }

    public void setListener(onItemClickListener listener){
        this.listener = listener;
    }
    public interface onItemClickListener{
        void onItemClick(SortItem sortItem);
    }
}