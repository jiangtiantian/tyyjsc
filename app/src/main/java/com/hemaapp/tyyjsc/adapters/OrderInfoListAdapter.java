package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivityLimitGoodsInfo;
import com.hemaapp.tyyjsc.activity.ActivityMemeberGoodsInfo;
import com.hemaapp.tyyjsc.activity.ActivityOrderComment;
import com.hemaapp.tyyjsc.activity.ActivityTaocanGoodsInfo;
import com.hemaapp.tyyjsc.activity.ActivityTejiaGoodsInfo;
import com.hemaapp.tyyjsc.activity.ActivityTuijianGoodInfo;
import com.hemaapp.tyyjsc.model.CartGoodsInfo;
import com.hemaapp.tyyjsc.model.OrderInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import static com.hemaapp.tyyjsc.R.id.limit_tag;

/**
 * ListView 订单详情适配器
 */
public class OrderInfoListAdapter extends HemaAdapter implements View.OnClickListener {
    private ArrayList<CartGoodsInfo> data = null;
    private Context mContext = null;
    private OrderInfo orderinfo;
    private String mark = null;
    private String order_id = "";
    private String fromtype;

    public String getFromtype() {
        return fromtype;
    }

    public void setFromtype(String fromtype) {
        this.fromtype = fromtype;
    }

    private boolean isCanClick = true;//默认可以点击
    private String statu;

    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }

    public OrderInfoListAdapter(Context mContext, OrderInfo orderInfo) {
        super(mContext);
        this.mContext = mContext;
        this.orderinfo = orderInfo;
        this.data = orderInfo.getGoods();
    }

    public void setCanClick(boolean isCanCLick) {
        this.isCanClick = isCanCLick;
    }

    public void setIsResturn(String mark) {
        this.mark = mark;
    }

    //设置该商品所属的订单id
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    //添加商品数据
    public void setData(ArrayList<CartGoodsInfo> data) {
        this.data = data;
    }

    @Override
    public boolean isEmpty() {
        return data == null || data.size() == 0;
    }

    @Override
    public int getCount() {
        return data == null || data.size() == 0 ? 0 : data.size();
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
            getEmptyView(parent);
        }
        ViewHolderLimitGood viewHolderLimitGood = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_goods_item, null);
            viewHolderLimitGood = new ViewHolderLimitGood();
            viewHolderLimitGood.itemImgView = (ImageView) convertView.findViewById(R.id.goods_img);
            viewHolderLimitGood.itemNameView = (TextView) convertView.findViewById(R.id.goods_name);
            viewHolderLimitGood.itemPriceView = (TextView) convertView.findViewById(R.id.goods_price);
            viewHolderLimitGood.itemSaleNumView = (TextView) convertView.findViewById(R.id.goods_num);
            viewHolderLimitGood.itemSpecNameView = (TextView) convertView.findViewById(R.id.goods_spec);
            viewHolderLimitGood.itemTagView = (TextView) convertView.findViewById(limit_tag);
            viewHolderLimitGood.bottom_ln = convertView.findViewById(R.id.bottom_ln);
            viewHolderLimitGood.bottom_visible_score = convertView.findViewById(R.id.bottom_visible_score);
            viewHolderLimitGood.optBtn = (TextView) convertView.findViewById(R.id.opt);
            viewHolderLimitGood.endFl = (FrameLayout) convertView.findViewById(R.id.endFL);
            viewHolderLimitGood.status_tv = (TextView) convertView.findViewById(R.id.status_tv);
            viewHolderLimitGood.shop_name = (TextView) convertView.findViewById(R.id.shop_name);
            viewHolderLimitGood.layout = (RelativeLayout) convertView.findViewById(R.id.con);
            viewHolderLimitGood.total = (TextView) convertView.findViewById(R.id.total);
            viewHolderLimitGood.layout.setOnClickListener(this);
            convertView.setTag(R.id.recommendgood, viewHolderLimitGood);
        }
        final CartGoodsInfo info = data.get(position);
        viewHolderLimitGood = (ViewHolderLimitGood) convertView.getTag(R.id.recommendgood);
        viewHolderLimitGood.bottom_visible_score.setVisibility(View.GONE);
        if (position == 0) {
            viewHolderLimitGood.shop_name.setVisibility(View.VISIBLE);
            viewHolderLimitGood.shop_name.setText(orderinfo.getShopname());
        } else {
            CartGoodsInfo before = data.get(position - 1);
            if ((isNull(before.getShopid()) ? "" : before.getShopid()).equals(isNull(info.getShopid()) ? "" : info.getShopid())) {
                viewHolderLimitGood.shop_name.setVisibility(View.GONE);
            } else {
                viewHolderLimitGood.shop_name.setVisibility(View.VISIBLE);
                viewHolderLimitGood.shop_name.setText(orderinfo.getShopname());
            }
        }
        if (position < data.size() - 1) {
            int tempPosition = position + 1;
            CartGoodsInfo after = data.get(tempPosition);
            if ((isNull(info.getShopid()) ? "" : info.getShopid()).equals(isNull(after.getShopid()) ? "" : after.getShopid())) {
                viewHolderLimitGood.bottom_ln.setVisibility(View.GONE);
            } else {
                viewHolderLimitGood.bottom_ln.setVisibility(View.VISIBLE);
                if (orderinfo.getKeytype().equals("5")) {
                    viewHolderLimitGood.total.setText("共" + data.size() + "件商品/合计：" + orderinfo.getAllcoupon() + "点券");
                } else
                    viewHolderLimitGood.total.setText("共" + data.size() + "件商品/合计：" + orderinfo.getGoodsprice());

            }
        } else {
            viewHolderLimitGood.bottom_ln.setVisibility(View.VISIBLE);
            if (orderinfo.getKeytype().equals("5")) {
                viewHolderLimitGood.total.setText("共" + data.size() + "件商品/合计：" + orderinfo.getAllcoupon() + "点券");
            } else
                viewHolderLimitGood.total.setText("共" + data.size() + "件商品/合计：" + orderinfo.getGoodsprice());

        }
        if (info.getImgurl() == null
                || "".equals(info.getImgurl())
                || "null".equals(info.getImgurl())) {
            viewHolderLimitGood.itemImgView
                    .setImageResource(R.mipmap.hm_icon_def);
        } else {
            ImageLoader.getInstance().displayImage(info.getImgurl(), viewHolderLimitGood.itemImgView, BaseUtil.displayImageOption());
        }
        viewHolderLimitGood.itemNameView.setText(info.getName());
        if ("5".equals(orderinfo.getKeytype()))
            viewHolderLimitGood.itemPriceView.setText(info.getCoupon() + "点券");
        else
            viewHolderLimitGood.itemPriceView.setText("¥" + info.getPrice());

        viewHolderLimitGood.itemSaleNumView.setText("X" + info.getBuycount());
        viewHolderLimitGood.itemSpecNameView.setText(info.getPropertyname());
        if ("4".equals(statu)) {//待评价状态
            viewHolderLimitGood.optBtn.setVisibility(View.VISIBLE);
            viewHolderLimitGood.optBtn.setTag(R.id.type, info);
            viewHolderLimitGood.optBtn.setText("去 评 价");
            viewHolderLimitGood.optBtn.setOnClickListener(this);
        } else {
            viewHolderLimitGood.optBtn.setVisibility(View.INVISIBLE);
        }
        viewHolderLimitGood.layout.setTag(R.id.type, info);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        CartGoodsInfo info = (CartGoodsInfo) v.getTag(R.id.type);
        Intent it;
        switch (v.getId()) {
            case R.id.opt:
                it = new Intent(mContext, ActivityOrderComment.class);
                it.putExtra("goods", info);
                it.putExtra("orderid", orderinfo.getId());
                it.putExtra("type", "1");
                ((BaseActivity) mContext).startActivityForResult(it, 1001);
                break;
            case R.id.con:
                if (info != null && isCanClick) {
                    switch (info.getKeytype()) {
                        case "1": //推荐商品
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
                    }
                }
                break;
            default:
                break;
        }
    }

    private static class ViewHolderLimitGood {
        private ImageView itemImgView = null;
        private TextView itemNameView = null;
        private TextView itemPriceView = null;
        private TextView itemSaleNumView = null;
        private TextView itemSpecNameView = null;
        private TextView itemTagView = null;
        private TextView shop_name = null;
        private FrameLayout endFl;//已售罄提示框
        private TextView total;
        private TextView status_tv = null;//已售罄或已结束
        private RelativeLayout layout;//点击进详情
        private View bottom_ln;
        private TextView optBtn = null;
        private View bottom_visible_score = null;
    }
}
