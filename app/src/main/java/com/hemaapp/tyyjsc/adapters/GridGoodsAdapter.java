package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
import com.hemaapp.tyyjsc.model.TimeInfo;
import com.hemaapp.tyyjsc.view.CusImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
/**
 * 抢购商品列表适配器
 */
public class GridGoodsAdapter extends HemaAdapter implements View.OnClickListener {
    private ArrayList<GoodsBriefIntroduction> data = null;
    private Context mContext = null;
    private int status =0;//标记状态 1：活动未开始 2：活动已结束 3：活动进行中
    private TimeInfo timeInfo = null;
    private Boolean  fromsort=false;
    private String keytype;
    public String getKeytype() {
        return keytype;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
        log_e("---------------- keytype = " + keytype);
    }

    public GridGoodsAdapter(Context mContext, ArrayList<GoodsBriefIntroduction> data, TimeInfo timeInfo, Boolean fromsort) {
        super(mContext);
        this.mContext = mContext;
        this.data = data;
        this.timeInfo = timeInfo;
        this.fromsort=fromsort;
        //当前抢购状态
        if (timeInfo != null) {
            status = timeInfo.getStatus();
        } else {
            status =0;
        }
    }

    public void setStatus(int status) {
        this.status = status;
    }

    //添加商品数据
    public void setData(ArrayList<GoodsBriefIntroduction> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null || data.size() == 0 ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderLimitGood viewHolderLimitGood = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.limit_good_item, null);
            viewHolderLimitGood = new ViewHolderLimitGood();
                viewHolderLimitGood.layout=convertView.findViewById(R.id.layout);
                viewHolderLimitGood.layout1=convertView.findViewById(R.id.layout1);
                viewHolderLimitGood.itemImgView = (CusImageView) convertView.findViewById(R.id.item_img);
                viewHolderLimitGood.itemImgView.setRatio(1.05f);
                viewHolderLimitGood.itemNameView = (TextView) convertView.findViewById(R.id.item_name);
                viewHolderLimitGood.itemPriceView = (TextView) convertView.findViewById(R.id.item_price);
                viewHolderLimitGood.itemOldPriceView = (TextView) convertView.findViewById(R.id.item_old_price);
                viewHolderLimitGood.itemSaleNumView = (TextView) convertView.findViewById(R.id.item_salenum);
                viewHolderLimitGood.itemPriceView1= (TextView) convertView.findViewById(R.id.item_price1);
                viewHolderLimitGood.itemOldPriceView1 = (TextView) convertView.findViewById(R.id.item_old_price1);
                viewHolderLimitGood.itemSaleNumView1= (TextView) convertView.findViewById(R.id.item_salenum1);
                viewHolderLimitGood.stock1= (TextView) convertView.findViewById(R.id.item_stock1);
                viewHolderLimitGood.itemBuyBtn = (ImageButton) convertView.findViewById(R.id.item_buy);
                viewHolderLimitGood.endFl = (FrameLayout) convertView.findViewById(R.id.endFL);
                viewHolderLimitGood.statusTV = (TextView) convertView.findViewById(R.id.status_tv);
                convertView.setTag(R.id.recommendgood, viewHolderLimitGood);
                convertView.setOnClickListener(this);
        }
        viewHolderLimitGood = (ViewHolderLimitGood) convertView.getTag(R.id.recommendgood);
        GoodsBriefIntroduction infors = data.get(position);
        if (infors.getImgurl() == null
                || "".equals(infors.getImgurl())
                || "null".equals(infors.getImgurl())) {
            viewHolderLimitGood.itemImgView
                    .setImageResource(R.mipmap.hm_icon_def);
        } else {
            ImageLoader.getInstance().displayImage(infors.getImgurl(), viewHolderLimitGood.itemImgView, BaseUtil.displayImageOption());
        }
        viewHolderLimitGood.itemNameView.setText(infors.getName());
        if(fromsort==false)
        {   viewHolderLimitGood.layout.setVisibility(View.VISIBLE);
            viewHolderLimitGood.layout1.setVisibility(View.GONE);
            viewHolderLimitGood.itemPriceView.setText("¥" + infors.getPrice());
            BaseUtil.setThroughSpan(viewHolderLimitGood.itemOldPriceView, "¥" + infors.getOld_price());
            viewHolderLimitGood.itemSaleNumView.setText("已售" + infors.getDisplaysales() + "件");
        }else
        {
            viewHolderLimitGood.layout1.setVisibility(View.VISIBLE);
            viewHolderLimitGood.layout.setVisibility(View.GONE);
            viewHolderLimitGood.itemPriceView1.setText("¥" + infors.getPrice());
            BaseUtil.setThroughSpan(viewHolderLimitGood.itemOldPriceView1, "¥" + infors.getOriginalprice());
            viewHolderLimitGood.itemSaleNumView1.setText("已售" + infors.getDisplaysales() + "件");
            viewHolderLimitGood.stock1.setText("库存"+infors.getStock());
        }
        if (-2 == status) {
            if (("1".equals(infors.getKeytype()) &&  "2".equals(infors.getIs_display())) ||"2".equals(infors.getKeytype()) || "3".equals(infors.getKeytype()) || ("4".equals(infors.getKeytype()) && "2".equals(infors.getIs_display()))) {//特价预订 超值套餐
                int stock = Integer.parseInt(isNull(infors.getStock()) ? "0" : infors.getStock());
                if (stock <= 0) {//已售罄
                    viewHolderLimitGood.endFl.setVisibility(View.VISIBLE);
                    viewHolderLimitGood.statusTV.setText("已售罄");
                } else {
                    viewHolderLimitGood.endFl.setVisibility(View.GONE);
                }
            }
        } else if (0 == status) {//抢购中
            int stock = Integer.parseInt(isNull(infors.getStock()) ? "0" : infors.getStock());
            if (stock <= 0) {//已售罄
                viewHolderLimitGood.endFl.setVisibility(View.VISIBLE);
                viewHolderLimitGood.statusTV.setText("已售罄");
            } else {
                viewHolderLimitGood.endFl.setVisibility(View.GONE);
            }
        } else if (-1 == status) {//即将开始
            viewHolderLimitGood.endFl.setVisibility(View.VISIBLE);
            viewHolderLimitGood.statusTV.setText("即将开始");

        } else if (1 == status) {//已结束
            viewHolderLimitGood.endFl.setVisibility(View.VISIBLE);
            viewHolderLimitGood.statusTV.setText("已结束");
        }
        convertView.setTag(R.id.banner, infors);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        GoodsBriefIntroduction infors = (GoodsBriefIntroduction) v.getTag(R.id.banner);
        if (infors != null) {
            Intent it;
            switch (keytype){
                case "1": //推荐商品
                case "6": //普通分类商品
                case "7"://特色分类商品 added by Torres
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
                case "5": //会员专区
                    it = new Intent(mContext, ActivityMemeberGoodsInfo.class);
                    it.putExtra("id", infors.getId());
                    mContext.startActivity(it);
                    break;
            }
        }
    }

    private static class ViewHolderLimitGood {
        private CusImageView itemImgView = null;
        private TextView itemNameView = null;
        private TextView itemPriceView = null;
        private TextView itemOldPriceView = null;
        private TextView itemSaleNumView = null;
        private TextView itemPriceView1 = null;
        private TextView itemOldPriceView1 = null;
        private TextView itemSaleNumView1 = null;
        private  TextView stock1=null;
        private View layout;
        private View layout1;
        private FrameLayout endFl = null;//已结束或者即将开始
        private TextView statusTV = null;
        private ImageButton itemBuyBtn = null;
    }
}
