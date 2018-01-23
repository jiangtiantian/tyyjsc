package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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
import com.hemaapp.tyyjsc.view.CusRoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 我的收藏  我的足迹
 */
public class CollectionAdapter extends HemaAdapter implements View.OnClickListener {

    private ArrayList<GoodsBriefIntroduction> data = null;
    private Context mContext = null;
    private boolean isEditable = false;//是否可编辑
    private boolean isAllSelected = false;//是否全选中
    private HashMap<String, GoodsBriefIntroduction> selected = new HashMap<>();
    private onSelectListener listener = null;

    public CollectionAdapter(Context mContext, ArrayList<GoodsBriefIntroduction> data) {
        super(mContext);
        this.mContext = mContext;
        this.data = data;
    }

    public void setIsEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public void setIsAllSelected(boolean isAllSelected) {
        this.isAllSelected = isAllSelected;
    }

    //添加商品数据
    public void setData(ArrayList<GoodsBriefIntroduction> data) {
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
            View view
                    = LayoutInflater.from(mContext).inflate(R.layout.listitem_empty, null);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                    parent.getWidth(), parent.getHeight());
            view.setLayoutParams(layoutParams);
            return view;
        }
        ViewHolderLimitGood viewHolderLimitGood = null;
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.col_item, null);
            viewHolderLimitGood = new ViewHolderLimitGood();
            viewHolderLimitGood.itemImgView = (CusRoundedImageView) convertView.findViewById(R.id.item_img);
            viewHolderLimitGood.itemImgView.setRadio(1.05f);
            viewHolderLimitGood.itemNameView = (TextView) convertView.findViewById(R.id.item_name);
            viewHolderLimitGood.itemPriceView = (TextView) convertView.findViewById(R.id.item_price);
            viewHolderLimitGood.itemOldPriceView = (TextView) convertView.findViewById(R.id.item_old_price);
            viewHolderLimitGood.itemSaleNumView = (TextView) convertView.findViewById(R.id.item_salenum);
            viewHolderLimitGood.itemCheck = (CheckBox) convertView.findViewById(R.id.item_buy);
            viewHolderLimitGood.box = (LinearLayout) convertView.findViewById(R.id.checkbox_layout);
            convertView.setTag(R.id.recommendgood, viewHolderLimitGood);
        }
        viewHolderLimitGood = (ViewHolderLimitGood) convertView.getTag(R.id.recommendgood);
        GoodsBriefIntroduction info = data.get(position);

        if (info.getImgurl() == null
                || "".equals(info.getImgurl())
                || "null".equals(info.getImgurl())) {
            viewHolderLimitGood.itemImgView
                    .setImageResource(R.mipmap.hm_icon_def);
        } else {
            String path = (String) viewHolderLimitGood.itemImgView.getTag(R.id.kind2);
            if (!isNull(path) && path.equals(info.getImgurl())) {
                //nothing 避免重复加载图片
            } else {
                ImageLoader.getInstance().displayImage(info.getImgurl(), viewHolderLimitGood.itemImgView, BaseUtil.displayImageOption());
                viewHolderLimitGood.itemImgView.setTag(R.id.kind2, info.getImgurl());
            }
        }

        viewHolderLimitGood.itemNameView.setText(info.getName());
        if("3".equals(info.getKeytype())){
            viewHolderLimitGood.itemPriceView.setText(info.getPrice()+"点券");
            viewHolderLimitGood.itemOldPriceView.setVisibility(View.GONE);
        }else{
            viewHolderLimitGood.itemPriceView.setText("¥" + info.getPrice());
            BaseUtil.setThroughSpan(viewHolderLimitGood.itemOldPriceView, "¥" + info.getOld_price());
            viewHolderLimitGood.itemOldPriceView.setVisibility(View.VISIBLE);
        }

        viewHolderLimitGood.itemSaleNumView.setText("已售" + info.getDisplaysales() + "件");
        CheckBox box = (CheckBox) (viewHolderLimitGood.box).getChildAt(0);
        if (isEditable) {
            viewHolderLimitGood.box.setVisibility(View.VISIBLE);
            box.setChecked(false);
        } else {
            viewHolderLimitGood.box.setVisibility(View.INVISIBLE);
        }
        if (isAllSelected) {
            box.setChecked(true);
        } else {
            box.setChecked(false);
        }
        if (selected.containsKey(info.getId())) {
            viewHolderLimitGood.itemCheck.setChecked(true);
        } else {
            viewHolderLimitGood.itemCheck.setChecked(false);
        }
        viewHolderLimitGood.box.setTag(R.id.type, info);
        viewHolderLimitGood.box.setOnClickListener(this);

        convertView.setTag(R.id.button, info);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodsBriefIntroduction info = (GoodsBriefIntroduction) view.getTag(R.id.button);
                log_e("------------------ keytype1 = " +info.getKeytype());
                Intent it;
                switch (info.getKeytype()){
                    case "1": //推荐商品
                        it = new Intent(mContext, ActivityTuijianGoodInfo.class);
                        it.putExtra("id", info.getGoodsid());
                        mContext.startActivity(it);
                        break;
                    case "2": //套餐专区
                        it = new Intent(mContext, ActivityTaocanGoodsInfo.class);
                        it.putExtra("id", info.getGoodsid());
                        mContext.startActivity(it);
                        break;
                    case "3": //会员专区(全款购车)
                        it = new Intent(mContext, ActivityMemeberGoodsInfo.class);
                        it.putExtra("id", info.getGoodsid());
                        mContext.startActivity(it);
                        break;
                }
            }
        });
        return convertView;
    }

    @Override
    public void onClick(View v) {
        GoodsBriefIntroduction info = (GoodsBriefIntroduction) v.getTag(R.id.type);
        if (info != null) {
            CheckBox box = (CheckBox) ((LinearLayout) v).getChildAt(0);
            if (box.isChecked()) {
                box.setChecked(false);
                if (selected.containsKey(info.getId())) {
                    selected.remove(info.getId());
                }
            } else {
                box.setChecked(true);
                if (!selected.containsKey(info.getId())) {
                    selected.put(info.getId(), info);
                }
            }
            if (listener != null) {
                listener.onSelect(selected);
            }
        }
    }

    private static class ViewHolderLimitGood {
        private CusRoundedImageView itemImgView = null;
        private TextView itemNameView = null;
        private TextView itemPriceView = null;
        private TextView itemOldPriceView = null;
        private TextView itemSaleNumView = null;
        private CheckBox itemCheck = null;

        private LinearLayout box = null;
    }

    public void setListener(onSelectListener listener) {
        this.listener = listener;
    }

    public interface onSelectListener {
        void onSelect(HashMap<String, GoodsBriefIntroduction> selected);
    }
}

