package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.GoodsBriefIntroduction;
import com.hemaapp.tyyjsc.view.CusImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 商品详情：特价预定
 * keytype = 1；套餐详情中
 * keytype = 2: 订单中
 */
public class TaoCanListAdapter extends HemaAdapter implements View.OnClickListener {

    private Context mActivity;
    private ArrayList<GoodsBriefIntroduction> data;
    private String keytype;

    public String getKeytype() {
        return keytype;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    public TaoCanListAdapter(Context mActivity, ArrayList<GoodsBriefIntroduction> data) {
        super(mActivity);
        this.mActivity = mActivity;
        this.data = data;
    }

    public void setData(ArrayList<GoodsBriefIntroduction> data) {
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
        ViewHolder viewHolderLimitGood = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.index_taocan_list_item, null);
            viewHolderLimitGood = new ViewHolder();
            viewHolderLimitGood.itemImgView = (CusImageView) convertView.findViewById(R.id.item_img);
            viewHolderLimitGood.itemImgView.setRatio(1.05f);
            viewHolderLimitGood.itemNameView = (TextView) convertView.findViewById(R.id.item_name);
            viewHolderLimitGood.itemSaleNumView = (TextView) convertView.findViewById(R.id.item_salenum);
            viewHolderLimitGood.itemStockView = (TextView) convertView.findViewById(R.id.item_stock);
            viewHolderLimitGood.itemAttriName = (TextView) convertView.findViewById(R.id.attr_name);
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
        }
        viewHolderLimitGood.itemNameView.setText(infors.getName());
        viewHolderLimitGood.itemSaleNumView.setText("￥"+infors.getPrice());
        viewHolderLimitGood.itemStockView.setText("×" + infors.getNum());
        viewHolderLimitGood.itemAttriName.setText("规格:"+infors.getProperty());
        convertView.setTag(R.id.banner, infors);
        convertView.setOnClickListener(this);
        return convertView;
    }

    class ViewHolder {
        private CusImageView itemImgView = null;
        private TextView itemNameView = null;
        private TextView itemAttriName = null;
        private TextView itemSaleNumView = null;
        private TextView itemStockView = null;
    }

    @Override
    public void onClick(View v) {
        GoodsBriefIntroduction infors = (GoodsBriefIntroduction) v.getTag(R.id.banner);
        if (infors != null) {
            if (("1".equals(infors.getKeytype()) && "2".equals(infors.getIs_display())) || "2".equals(infors.getKeytype())
                    || "3".equals(infors.getKeytype()) || ("4".equals(infors.getKeytype()) && "2".equals(infors.getIs_display()))) {//限时抢购和特价预订(一成购车) 已售罄时，禁止进去详情页
                int stock = Integer.parseInt(("".equals(infors.getStock()) || ("null".equals(infors.getStock()))) ? "0" : infors.getStock());
                if (stock <= 0) {
                    return;
                }
            }
//            Intent intent = new Intent(mContext, ActivityTuijianGoodInfo.class);
//            intent.putExtra("id", infors.getId());
//            mContext.startActivity(intent);
        }
    }
}
