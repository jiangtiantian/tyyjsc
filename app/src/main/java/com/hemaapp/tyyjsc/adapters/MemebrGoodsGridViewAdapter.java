package com.hemaapp.tyyjsc.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivityMemeberGoodsInfo;
import com.hemaapp.tyyjsc.activity.ActivityTuijianGoodInfo;
import com.hemaapp.tyyjsc.model.MembergoodsList;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import xtom.frame.XtomActivityManager;

/**
 * 猜你喜欢GridView适配器
 * 会员专区(全款购车)的商品详情，猜你喜欢
 */
public class MemebrGoodsGridViewAdapter extends HemaAdapter implements View.OnClickListener {

    private Context mActivity;
    private ArrayList<MembergoodsList> data;

    public MemebrGoodsGridViewAdapter(Context mActivity, ArrayList<MembergoodsList> data) {
        super(mActivity);
        this.mActivity = mActivity;
        this.data = data;
    }

    public void setData(ArrayList<MembergoodsList> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        int size = data == null ? 0 : data.size();
        return size;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolderLimitGood = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.guess_like_grid_item, null);
            viewHolderLimitGood = new ViewHolder();
            viewHolderLimitGood.itemImgView = (ImageView) convertView.findViewById(R.id.item_img);
            viewHolderLimitGood.itemNameView = (TextView) convertView.findViewById(R.id.item_name);
            viewHolderLimitGood.itemPriceView = (TextView) convertView.findViewById(R.id.item_price);
            viewHolderLimitGood.itemOldPriceView = (TextView) convertView.findViewById(R.id.item_old_price);
            viewHolderLimitGood.itemSaleNumView = (TextView) convertView.findViewById(R.id.item_salenum);
            viewHolderLimitGood.itemStockNum = (TextView) convertView.findViewById(R.id.item_stock);
            viewHolderLimitGood.endFl = (FrameLayout) convertView.findViewById(R.id.endFL);
            viewHolderLimitGood.status_tv = (TextView) convertView.findViewById(R.id.status_tv);
            viewHolderLimitGood.itemBuyBtn = (ImageButton) convertView.findViewById(R.id.item_buy);
            convertView.setTag(R.id.recommendgood, viewHolderLimitGood);
            convertView.setTag(R.id.TAG_VIEWHOLDER, viewHolderLimitGood);
        } else {
            viewHolderLimitGood = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }
        MembergoodsList infors = data.get(position);
        if (infors.getImgurl() == null
                || "".equals(infors.getImgurl())
                || "null".equals(infors.getImgurl())) {
            viewHolderLimitGood.itemImgView
                    .setImageResource(R.mipmap.hm_icon_def);
        } else {
            ImageLoader.getInstance().displayImage(infors.getImgurl(), viewHolderLimitGood.itemImgView, BaseUtil.displayImageOption());
        }

        viewHolderLimitGood.itemNameView.setText(infors.getName());
        viewHolderLimitGood.itemPriceView.setText(infors.getCoupon());
        viewHolderLimitGood.itemOldPriceView.setVisibility(View.GONE);
        viewHolderLimitGood.itemSaleNumView.setText("已售" + infors.getDisplaysales() + "件");
        viewHolderLimitGood.itemStockNum.setVisibility(View.VISIBLE);
        viewHolderLimitGood.itemStockNum.setText(mContext.getString(R.string.hm_hlxs_txt_239) + infors.getStock());
        int stock = Integer.parseInt(isNull(infors.getStock()) ? "0" : infors.getStock());
        if (stock <= 0) {//已售罄
            viewHolderLimitGood.endFl.setVisibility(View.VISIBLE);
            viewHolderLimitGood.status_tv.setText("已售罄");
        } else {
            viewHolderLimitGood.endFl.setVisibility(View.GONE);
        }
        convertView.setTag(R.id.banner, infors);
        convertView.setOnClickListener(this);
        return convertView;
    }

    class ViewHolder {
        private ImageView itemImgView = null;
        private TextView itemNameView = null;
        private TextView itemPriceView = null;
        private TextView itemOldPriceView = null;
        private TextView itemSaleNumView = null;
        private TextView itemStockNum = null;

        private FrameLayout endFl = null;
        private TextView status_tv = null;

        private ImageButton itemBuyBtn = null;
    }

    @Override
    public void onClick(View v) {
        MembergoodsList infors = (MembergoodsList) v.getTag(R.id.banner);
        if (infors != null) {
            int stock = Integer.parseInt(isNull(infors.getStock()) ? "0" : infors.getStock());
            if (stock <= 0) {//已售罄
                return;
            }
            Activity activityGoodsInfo = XtomActivityManager.getActivityByClass(ActivityTuijianGoodInfo.class);
            if (activityGoodsInfo != null) {
                activityGoodsInfo.finish();
            }
            Intent intent = new Intent(mContext, ActivityMemeberGoodsInfo.class);
            intent.putExtra("id", infors.getId());
            mContext.startActivity(intent);
        }
    }
}