package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.hemaapp.tyyjsc.model.CartGoodsInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 订单商品列表适配器
 */
public class OrderListAdapter extends HemaAdapter {
    private Context context = null;
    private ArrayList<CartGoodsInfo> data = new ArrayList<>();
    private int currentpostion;
    private String status;
    private String keytype;
    private HashMap<String, CartGoodsInfo> selected = new HashMap<>();
    public OrderListAdapter(Context context, ArrayList<CartGoodsInfo> data,String status, String keytype) {
        super(context);
        this.context = context;
        this.data = data;
        this.status=status;
        this.keytype = keytype;
    }
    @Override
    public boolean isEmpty() {
        return data == null || data.size() == 0;
    }
    public void setData(ArrayList<CartGoodsInfo> data) {
        this.data = data;
    }
    @Override
    public int getCount() {
        return data == null || data.size()<3 ? data.size() : 3;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_good_list, null);
            holder = new ViewHolder(convertView,position);
            convertView.setTag(R.id.type, holder);
        }
        holder = (ViewHolder) convertView.getTag(R.id.type);
        holder.setData(position);
        return convertView;
    }

    class ViewHolder {
        private LinearLayout box;
        private ImageView img;
        private TextView priceView;
        private TextView itemNameView;
        private TextView specNameView;
        private TextView del;
        private TextView goods_num;
        private View  root;
        private TextView statusTV = null;
        int position=0;
        public ViewHolder(final View convertView, final int position)
        {   this.position=position;
            box = (LinearLayout) convertView.findViewById(R.id.checkbox_layout);
            root=convertView.findViewById(R.id.root);
            img = (ImageView) convertView.findViewById(R.id.item_img);
            itemNameView = (TextView) convertView.findViewById(R.id.goods_name);
            priceView = (TextView) convertView.findViewById(R.id.goods_price);
            specNameView = (TextView) convertView.findViewById(R.id.goods_spec);
            statusTV = (TextView)convertView.findViewById(R.id.status_tv);
            goods_num= (TextView) convertView.findViewById(R.id.goods_num);
            del = (TextView) convertView.findViewById(R.id.del_tv);
            final CartGoodsInfo info = data.get(position);
            root.setTag(R.id.buy, info);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartGoodsInfo info = (CartGoodsInfo) v.getTag(R.id.buy);
                    //点击进入详情
                    Intent it;
                    switch (keytype){
                        case "1":
                            it = new Intent(mContext, ActivityTuijianGoodInfo.class);
                            it.putExtra("id", info.getGoodsid());
                            mContext.startActivity(it);
                            break;
                        case "2": //限时抢购
                            it = new Intent(mContext, ActivityLimitGoodsInfo.class);
                            it.putExtra("id", info.getGoodsid());
                            mContext.startActivity(it);
                            break;
                        case "3": //特价商品
                            it = new Intent(mContext, ActivityTejiaGoodsInfo.class);
                            it.putExtra("id", info.getGoodsid());
                            mContext.startActivity(it);
                            break;
                        case "4": //套餐专区
                            it = new Intent(mContext, ActivityTaocanGoodsInfo.class);
                            it.putExtra("id", info.getGoodsid());
                            mContext.startActivity(it);
                            break;
                        case "5":
                            it = new Intent(mContext, ActivityMemeberGoodsInfo.class);
                            it.putExtra("id", info.getGoodsid());
                            mContext.startActivity(it);
                            break;
                    }
                }
            });
        }
        public void setData(int position)
        {
            this.position=position;
            CartGoodsInfo info = data.get(position);
            if(info.getImgurl().equals(img.getTag()))
            {
            }else
            {
                ImageLoader.getInstance().displayImage(info.getImgurl(),img, BaseUtil.displayImageOption());
                img.setTag(info.getImgurl());
            }
            itemNameView.setText(info.getName());
            if("5".equals(keytype))
                priceView.setText(info.getCoupon()+"点券");
            else
                priceView.setText("¥" + info.getPrice());

            specNameView.setText(info.getPropertyname());
            goods_num.setText("×"+info.getBuycount());
//        if ("3".equals(info.getKeytype())) {//特价预订(一成购车) 限时抢购
//            int stock = Integer.parseInt(isNull(info.getStock()) ? "0" : info.getStock());
//            if (stock <= 0) {//已售罄
//                holder.endFL.setVisibility(View.VISIBLE);
//                holder.statusTV.setText("已售罄");
//            } else if(Integer.parseInt(info.getBuycount()) > stock){
//                holder.endFL.setVisibility(View.VISIBLE);
//                holder.statusTV.setText("库存不足");
//            }else {
//                holder.endFL.setVisibility(View.GONE);
//            }
//        } else {
//            holder.endFL.setVisibility(View.GONE);
//        }
//        if ("2".equals(info.getKeytype())) {//抢购商品
//            holder.limitTagView.setVisibility(View.VISIBLE);
//        } else {
//            holder.limitTagView.setVisibility(View.INVISIBLE);
//        }

        }
    }
}