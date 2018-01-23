package com.hemaapp.tyyjsc.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivityTuijianGoodInfo;
import com.hemaapp.tyyjsc.model.GoodsBriefIntroduction;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.XtomActivityManager;
import xtom.frame.image.load.XtomImageTask;

/**
 * 猜你喜欢GridView适配器
 * 只跳转到推荐/普通的商品详情
 */
public class GoodsGridViewAdapter extends HemaAdapter implements View.OnClickListener {

    private Context mActivity;
    private ArrayList<GoodsBriefIntroduction> data;

    public GoodsGridViewAdapter(Context mActivity, ArrayList<GoodsBriefIntroduction> data) {
        super(mActivity);
        this.mActivity = mActivity;
        this.data = data;
    }

    public void setData(ArrayList<GoodsBriefIntroduction> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        int size = data == null ? 0 : data.size();
        return size;
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
        ViewHolder viewHolderLimitGood ;
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
        } else {
            ImageLoader.getInstance().displayImage(infors.getImgurl(), viewHolderLimitGood.itemImgView, BaseUtil.displayImageOption());
//            try {
//                URL url = new URL(infors.getImgurl());
//                ((BaseActivity)mContext).imageWorker.loadImage(new XtomImageTask(viewHolderLimitGood.itemImgView, url, mContext));
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
        }
        viewHolderLimitGood.itemNameView.setText(infors.getName());
        viewHolderLimitGood.itemPriceView.setText("¥" + infors.getPrice());
        BaseUtil.setThroughSpan(viewHolderLimitGood.itemOldPriceView, "¥" + infors.getOld_price());
        viewHolderLimitGood.itemSaleNumView.setText("已售" + infors.getDisplaysales() + "件");
        /**
         * 库存：限时抢购、特价预订(一成购车)、超值套餐有库存；其他类型没有库存
         */
        if (("1".equals(infors.getKeytype()) &&  "2".equals(infors.getIs_display())) ||"2".equals(infors.getKeytype()) ||
                "3".equals(infors.getKeytype()) || ("4".equals(infors.getKeytype()) && "2".equals(infors.getIs_display()))) {
            viewHolderLimitGood.itemStockNum.setVisibility(View.VISIBLE);
            viewHolderLimitGood.itemStockNum.setText(mContext.getString(R.string.hm_hlxs_txt_239) + infors.getStock());
        } else {
            viewHolderLimitGood.itemStockNum.setVisibility(View.GONE);
        }
        if (("1".equals(infors.getKeytype()) &&  "2".equals(infors.getIs_display())) ||"2".equals(infors.getKeytype()) ||
                "3".equals(infors.getKeytype()) || ("4".equals(infors.getKeytype()) && "2".equals(infors.getIs_display()))) {
            int stock = Integer.parseInt(isNull(infors.getStock()) ? "0" : infors.getStock());
            if (stock <= 0) {//已售罄
                viewHolderLimitGood.endFl.setVisibility(View.VISIBLE);
                viewHolderLimitGood.status_tv.setText("已售罄");
            } else {
                viewHolderLimitGood.endFl.setVisibility(View.GONE);
            }
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
    }

    @Override
    public void onClick(View v) {
        GoodsBriefIntroduction infors = (GoodsBriefIntroduction) v.getTag(R.id.banner);
        if (infors != null) {
            Activity activityGoodsInfo = XtomActivityManager.getActivityByClass(ActivityTuijianGoodInfo.class);
            if (activityGoodsInfo != null) {
                activityGoodsInfo.finish();
            }
            Intent intent = new Intent(mContext, ActivityTuijianGoodInfo.class);
            intent.putExtra("id", infors.getId());
            mContext.startActivity(intent);
        }
    }
}