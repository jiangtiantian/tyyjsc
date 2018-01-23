package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivityLimitGoodsInfo;
import com.hemaapp.tyyjsc.activity.ActivityMemeberGoodsInfo;
import com.hemaapp.tyyjsc.activity.ActivityTaocanGoodsInfo;
import com.hemaapp.tyyjsc.activity.ActivityTejiaGoodsInfo;
import com.hemaapp.tyyjsc.activity.ActivityTuijianGoodInfo;
import com.hemaapp.tyyjsc.model.GoodsBriefIntroduction;
import com.hemaapp.tyyjsc.view.CusImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 商品详情：特价预定
 */
public class ValueMealAdapter extends HemaAdapter implements View.OnClickListener {

    private Context mActivity;
    private ArrayList<GoodsBriefIntroduction> data;
    private String keytype;
    public String getKeytype() {
        return keytype;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    public ValueMealAdapter(Context mActivity, ArrayList<GoodsBriefIntroduction> data) {
        super(mActivity);
        this.mActivity = mActivity;
        this.data = data;
    }
    public void setData(ArrayList<GoodsBriefIntroduction> data){
        this.data = data;
    }

    @Override
    public boolean isEmpty() {
        return data == null || data.size() == 0;
    }

    @Override
    public int getCount() {
        int size = data == null ? 1 : data.size();
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
        if(isEmpty()){
            return getEmptyView(parent);
        }
        ViewHolder viewHolderLimitGood = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.listitem_goods_item, null);
            viewHolderLimitGood = new ViewHolder();
            viewHolderLimitGood.itemImgView = (CusImageView) convertView.findViewById(R.id.item_img);
            viewHolderLimitGood.itemImgView.setRatio(1.05f);
            viewHolderLimitGood.itemNameView = (TextView) convertView.findViewById(R.id.item_name);
            viewHolderLimitGood.itemPriceView = (TextView) convertView.findViewById(R.id.item_price);
            viewHolderLimitGood.itemSaleNumView = (TextView) convertView.findViewById(R.id.item_salenum);
            viewHolderLimitGood.goForwardSale = (TextView)convertView.findViewById(R.id.go_forward_sale);
            viewHolderLimitGood.itemStockView = (TextView)convertView.findViewById(R.id.item_stock);
            convertView.setTag(R.id.recommendgood, viewHolderLimitGood);
            convertView.setTag(R.id.TAG_VIEWHOLDER, viewHolderLimitGood);
        } else {
            viewHolderLimitGood = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }
        GoodsBriefIntroduction infors = data.get(position);
        if (infors.getImgurl() == null
                || "".equals(infors.getImgurl())
                || "null".equals(infors.getImgurl())) {
            viewHolderLimitGood.itemImgView
                    .setImageResource(R.mipmap.hm_icon_def);
        }else{
            ImageLoader.getInstance().displayImage(infors.getImgurl(), viewHolderLimitGood.itemImgView, BaseUtil.displayImageOption());
        }
        viewHolderLimitGood.itemNameView.setText(infors.getName());
        viewHolderLimitGood.itemPriceView.setText("¥" + infors.getPrice());
        viewHolderLimitGood.itemSaleNumView.setText("已售" + infors.getDisplaysales() + "件");
        viewHolderLimitGood.itemStockView.setText(mContext.getString(R.string.hm_hlxs_txt_239) + infors.getStock());
        convertView.setTag(R.id.banner, infors);
        convertView.setOnClickListener(this);
        return convertView;
    }

    class ViewHolder {
        private CusImageView itemImgView = null;
        private TextView itemNameView = null;
        private TextView itemPriceView = null;
        private TextView itemSaleNumView = null;
        private TextView goForwardSale = null;
        private TextView itemStockView = null;
    }

    @Override
    public void onClick(View v) {
        GoodsBriefIntroduction infors = (GoodsBriefIntroduction) v.getTag(R.id.banner);
        if (infors != null) {
            if (("1".equals(infors.getKeytype()) &&  "2".equals(infors.getIs_display())) ||"2".equals(infors.getKeytype())
                    || "3".equals(infors.getKeytype()) || ("4".equals(infors.getKeytype()) && "2".equals(infors.getIs_display()))) {//限时抢购和特价预订(一成购车) 已售罄时，禁止进去详情页
                int stock = Integer.parseInt(("".equals(infors.getStock()) || ("null".equals(infors.getStock()))) ? "0" : infors.getStock());
                if (stock <= 0) {
                    return;
                }
            }
            Intent it;
            switch (keytype){
                case "1":
                    it = new Intent(mContext, ActivityTuijianGoodInfo.class);
                    it.putExtra("id", infors.getId());
                    mContext.startActivity(it);
                    break;
                case "2": //限时抢购
                    it = new Intent(mContext, ActivityLimitGoodsInfo.class);
                    it.putExtra("id", infors.getId());
                    mContext.startActivity(it);
                    break;
                case "3": //特价商品
                    it = new Intent(mContext, ActivityTejiaGoodsInfo.class);
                    it.putExtra("id", infors.getId());
                    mContext.startActivity(it);
                    break;
                case "4": //套餐专区
                    it = new Intent(mContext, ActivityTaocanGoodsInfo.class);
                    it.putExtra("id", infors.getId());
                    mContext.startActivity(it);
                    break;
                case "5":
                    it = new Intent(mContext, ActivityMemeberGoodsInfo.class);
                    it.putExtra("id", infors.getId());
                    mContext.startActivity(it);
                    break;
            }
        }
    }
}