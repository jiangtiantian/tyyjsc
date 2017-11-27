package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivityTaocanGoodsInfo;
import com.hemaapp.tyyjsc.model.GoodsBriefIntroduction;
import com.hemaapp.tyyjsc.view.CusImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by wangyuxia on 2017/9/1.
 * 首页 -- 套餐部分的商品列表
 */
public class TaoCanGoodsAdapter extends RecyclerView.Adapter<TaoCanGoodsAdapter.CusViewHolder> implements View.OnClickListener {

    private ArrayList<GoodsBriefIntroduction> data = null;
    private Context context = null;
    private LayoutInflater inflater = null;

    public TaoCanGoodsAdapter(Context context, ArrayList<GoodsBriefIntroduction> data) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    public void setData(ArrayList<GoodsBriefIntroduction> data) {
        this.data = data;
    }

    @Override
    public CusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CusViewHolder(inflater.inflate(R.layout.listitem_taocan, parent, false));
    }

    @Override
    public void onBindViewHolder(CusViewHolder holder, int position) {
        GoodsBriefIntroduction infors = data.get(position);
        holder.itemImgView.setRatio(1f);
        if (infors.getImgurl() == null
                || "".equals(infors.getImgurl())
                || "null".equals(infors.getImgurl())) {
            holder.itemImgView
                    .setBackgroundResource(R.mipmap.hm_icon_def);
        } else {
            ImageLoader.getInstance().displayImage(infors.getImgurl(), holder.itemImgView, BaseUtil.displayImageOption());
        }
        holder.itemNameView.setText(infors.getName());
        holder.itemPriceView.setText("¥" + infors.getPrice());
        BaseUtil.setThroughSpan(holder.itemOldPriceView, "¥" + infors.getOld_price());
        holder.itemSaleCountView.setText("已售" + (infors.getBuycount() == null ||
                "".equals(infors.getBuycount()) ? "0" : infors.getBuycount()));
        holder.getChild().setTag(infors);
        holder.getChild().setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (data != null) {
            if (data.size() > 3)
                return 3;
            else
                return data.size();
        } else
            return size;
    }

    @Override
    public void onClick(View v) {
        GoodsBriefIntroduction infors = (GoodsBriefIntroduction) v.getTag();
        Intent intent = new Intent(context, ActivityTaocanGoodsInfo.class);
        intent.putExtra("id", infors.getId());
        context.startActivity(intent);
    }

    public static class CusViewHolder extends RecyclerView.ViewHolder {
        private CusImageView itemImgView;
        private TextView itemNameView;
        private TextView itemPriceView;
        private TextView itemOldPriceView;
        private TextView itemSaleCountView;
        private View child = null;

        public CusViewHolder(View childView) {
            super(childView);
            this.child = childView;
            itemImgView = (CusImageView) childView.findViewById(R.id.item_img);
            itemImgView.setRatio(1.0f);
            itemNameView = (TextView) childView.findViewById(R.id.item_name);
            itemPriceView = (TextView) childView.findViewById(R.id.item_price);
            itemOldPriceView = (TextView) childView.findViewById(R.id.item_old_price);
            itemSaleCountView = (TextView) childView.findViewById(R.id.item_sale_count);
        }

        public View getChild() {
            return child;
        }
    }
}