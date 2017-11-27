package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivityBookGoods;
import com.hemaapp.tyyjsc.model.SortItem1;
import com.hemaapp.tyyjsc.view.CusImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 经典分类：二级分类
 */
public class GridAdapter extends HemaAdapter implements OnClickListener {

    private Context context = null;

    private ArrayList<SortItem1> data = null;

    public GridAdapter(Context context, ArrayList<SortItem1> data) {
        super(context);
        this.context = context;
        this.data = data;
    }

    public void setData(ArrayList<SortItem1> data) {
        this.data = data;
    }

    @Override
    public boolean isEmpty() {
        return data == null || data.size() == 0;
    }

    @Override
    public int getCount() {
        return data == null || data.size() == 0 ? 1 : data.size();
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
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.grid_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.item_name);
            holder.img = (CusImageView) convertView.findViewById(R.id.item_img);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.img.setRatio(1);
        SortItem1 infors = data.get(position);
        if (infors.getImgurl() == null
                || "".equals(infors.getImgurl())
                || "null".equals(infors.getImgurl())) {
            holder.img
                    .setImageResource(R.mipmap.hm_icon_def);
        } else {
            ImageLoader.getInstance().displayImage(infors.getImgurl(), holder.img, BaseUtil.displayImageOption());
        }
        holder.name.setText(infors.getName());
        convertView.setTag(R.id.button, infors);
        convertView.setOnClickListener(this);
        return convertView;
    }

    static class ViewHolder {
        private TextView name;
        private CusImageView img;
    }

    @Override
    public void onClick(View v) {
        SortItem1 item = (SortItem1) v.getTag(R.id.button);
        if (item != null) {
            //分类进入列表
            Intent intent = new Intent(mContext, ActivityBookGoods.class);
            intent.putExtra("keytype","6");
            intent.putExtra("id", item.getId());
            intent.putExtra("name", item.getName());
            mContext.startActivity(intent);
        }
    }


}
