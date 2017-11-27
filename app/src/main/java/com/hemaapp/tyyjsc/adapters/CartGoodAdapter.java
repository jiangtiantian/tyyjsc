package com.hemaapp.tyyjsc.adapters;


import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivityLimitGoodsInfo;
import com.hemaapp.tyyjsc.activity.ActivityTaocanGoodsInfo;
import com.hemaapp.tyyjsc.activity.ActivityTejiaGoodsInfo;
import com.hemaapp.tyyjsc.activity.ActivityTuijianGoodInfo;
import com.hemaapp.tyyjsc.model.CartGoodsInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 购物车商品适配器
 */
public class CartGoodAdapter extends HemaAdapter {

    private Context context = null;
    private ArrayList<CartGoodsInfo> data = new ArrayList<>();
    private ListView listView = null;
    private onDealChildClickListener listener = null;
    private int currentpostion;
    private HashMap<String, CartGoodsInfo> selected = new HashMap<>();

    public CartGoodAdapter(Context context, ArrayList<CartGoodsInfo> data, ListView listView) {
        super(context);
        this.context = context;
        this.data = data;
        this.listView = listView;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (isEmpty()) {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(dm);
            int h = dm.heightPixels - BaseUtil.dip2px(mContext, 100) - BaseUtil.getStatusBarHeight(mContext);
            View view
                    = LayoutInflater.from(mContext).inflate(R.layout.listitem_empty, null);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                    parent.getWidth(), h);
            view.setLayoutParams(layoutParams);
            return view;
        }
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.type, holder);
        }
        holder = (ViewHolder) convertView.getTag(R.id.type);
        holder.setData(position);
        return convertView;
    }

    class ViewHolder {
        private LinearLayout box;
        private ImageView img;
        private TextView tv_tag;
        private CheckBox cart_allchoose_cb;
        private TextView priceView;
        private ImageView addView;
        private TextView itemNameView;
        private TextView numView;
        private ImageView minusView;
        private TextView specNameView;
        private View topname_view;
        private CheckBox shopname;
        private TextView del;
        private View root;
        private TextView statusTV = null;
        int position = 0;

        public ViewHolder(View convertView) {
            box = (LinearLayout) convertView.findViewById(R.id.checkbox_layout);
            root = convertView.findViewById(R.id.root);
            img = (ImageView) convertView.findViewById(R.id.item_img);
            tv_tag = (TextView) convertView.findViewById(R.id.tv_tag);
            cart_allchoose_cb = (CheckBox) convertView.findViewById(R.id.cart_allchoose_cb);
            shopname = (CheckBox) convertView.findViewById(R.id.shopname);
            itemNameView = (TextView) convertView.findViewById(R.id.goods_name);
            topname_view = convertView.findViewById(R.id.topname_view);
            priceView = (TextView) convertView.findViewById(R.id.goods_price);
            addView = (ImageView) convertView.findViewById(R.id.num_m);
            numView = (TextView) convertView.findViewById(R.id.num);
            specNameView = (TextView) convertView.findViewById(R.id.goods_spec);
            minusView = (ImageView) convertView.findViewById(R.id.num_p);
            statusTV = (TextView) convertView.findViewById(R.id.status_tv);
            del = (TextView) convertView.findViewById(R.id.del_tv);
        }

        public void setData(int position) {
            this.position = position;
            CartGoodsInfo info = data.get(position);

            if ("3".equals(info.getKeytype())) {
                tv_tag.setText("特价商品");
                tv_tag.setBackgroundColor(mContext.getResources().getColor(R.color.cl_3ea600));
                tv_tag.setVisibility(View.VISIBLE);
            } else if ("4".equals(info.getKeytype())) {
                tv_tag.setText("套餐商品");
                tv_tag.setBackgroundColor(0xffc92b2a);
                tv_tag.setVisibility(View.VISIBLE);
            } else {
                tv_tag.setVisibility(View.INVISIBLE);
            }

            cart_allchoose_cb.setTag(R.id.button, position);
            cart_allchoose_cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean bool;
                    bool = ((CheckBox) v).isChecked();
                    int pos = (Integer) v.getTag(R.id.button);
                    if (listener != null) {
                        listener.onDealChildClick(data.get(pos), bool);
                    }
                }
            });

            root.setTag(R.id.buy, info);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartGoodsInfo info = (CartGoodsInfo) v.getTag(R.id.buy);
                    Intent it;
                    switch (info.getKeytype()) {
                        case "1": //推荐商品
                            it = new Intent(context, ActivityTuijianGoodInfo.class);
                            it.putExtra("id", info.getKeyid());
                            context.startActivity(it);
                            break;
                        case "2": //限时抢购
                            it = new Intent(context, ActivityLimitGoodsInfo.class);
                            it.putExtra("id", info.getKeyid());
                            context.startActivity(it);
                            break;
                        case "3": //特价商品
                            it = new Intent(context, ActivityTejiaGoodsInfo.class);
                            it.putExtra("id", info.getKeyid());
                            context.startActivity(it);
                            break;
                        case "4": //套餐专区
                            it = new Intent(context, ActivityTaocanGoodsInfo.class);
                            it.putExtra("id", info.getKeyid());
                            context.startActivity(it);
                            break;
                    }
                }
            });

            root.setTag(R.id.cache_ly, position);
            root.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = (Integer) v.getTag(R.id.cache_ly);
                    listener.onLongClick(data.get(pos));
                    return true;
                }
            });

            shopname.setTag(R.id.date, position);
            shopname.setOnClickListener(new View.OnClickListener() {
                boolean bool;

                @Override
                public void onClick(View v) {
                    bool = ((CheckBox) v).isChecked();
                    int pos = (Integer) v.getTag(R.id.date);
                    listener.onheaderListener(data.get(pos).getShopid(), bool);
                }
            });
            //数量加一
            addView.setTag(R.id.face_iv, position);
            addView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer) v.getTag(R.id.face_iv);
                    int buycount = Integer.parseInt(data.get(pos).getBuycount());
                    buycount++;
                    if (listener != null) {
                        listener.onChangeNum("1", data.get(pos), buycount);
                    }
                }
            });
            //数量减一
            minusView.setTag(R.id.deal_note, position);
            minusView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer) v.getTag(R.id.deal_note);
                    int buycount = Integer.parseInt(data.get(pos).getBuycount());
                    if (buycount >= 2) {
                        buycount--;
                        if (listener != null) {
                            listener.onChangeNum("1", data.get(pos), buycount);
                        }
                    }
                }
            });
            //删除商品
            del.setTag(R.id.gallery, position);
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer) v.getTag(R.id.gallery);
                    if (listener != null) {
                        listener.onChangeNum("1", data.get(pos), 0);
                    }
                }
            });

            if (!info.getImgurl().equals(img.getTag())) {
                ImageLoader.getInstance().displayImage(info.getImgurl(), img, BaseUtil.displayImageOption());
                img.setTag(info.getImgurl());
            }
            if (position == 0) {
                topname_view.setVisibility(View.VISIBLE);
            } else {
                CartGoodsInfo before = data.get(position - 1);
                if (before.getShopid().equals(info.getShopid())) {
                    topname_view.setVisibility(View.GONE);
                } else
                    topname_view.setVisibility(View.VISIBLE);
            }//头部选中操作
            shopname.setText(info.getShopname());
            itemNameView.setText(info.getName());
            priceView.setText("¥" + info.getPrice());
            numView.setText(info.getBuycount());
            specNameView.setText(info.getPropertyname());

            if (info.isChecked()) {
                cart_allchoose_cb.setChecked(true);
            } else {
                cart_allchoose_cb.setChecked(false);
            }
            //店铺点击
            if (info.isSellerChecked()) {
                shopname.setChecked(true);
            } else
                shopname.setChecked(false);

        }
    }

    //设置子布局选中按钮监听
    public void setListener(onDealChildClickListener listener) {
        this.listener = listener;
    }

    public interface onDealChildClickListener {
        void onDealChildClick(CartGoodsInfo info, boolean bool);

        void onheaderListener(String id, boolean bool);

        void onChangeNum(String keytype, CartGoodsInfo info, int buycount);

        void onLongClick(CartGoodsInfo info);
    }
}