package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.CartGoodsInfo;
import com.hemaapp.tyyjsc.model.SignValueInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

import xtom.frame.util.XtomBaseUtil;

/**
 * 商品列表适配器
 * ListView
 */
public class ListGoodsAdapter extends HemaAdapter {

    private ArrayList<CartGoodsInfo> data = null;
    private static Context mContext = null;
    private static String mark = null;
    private static String order_id = "";
    private HashMap<String, SignValueInfo> Exfee = new HashMap<>();//运费
    private boolean isCanClick = true;//默认可以点击
    public HashMap<String, String> goodfee = new HashMap<>();//运费
    public HashMap<String, EditText> dealDatas = new HashMap<>();
    private double total = 0.00;
    private static String keytype;

    public static String getKeytype() {
        return keytype;
    }

    public static void setKeytype(String keytype) {
        ListGoodsAdapter.keytype = keytype;
    }

    public ListGoodsAdapter(Context mContext, ArrayList<CartGoodsInfo> data, HashMap<String, SignValueInfo> exfee) {
        super(mContext);
        this.mContext = mContext;
        this.data = data;
        this.Exfee = exfee;
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
    public void setData(ArrayList<CartGoodsInfo> data, HashMap<String, SignValueInfo> exfee) {
        this.data = data;
        this.Exfee = exfee;
        notifyDataSetChanged();
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
        ViewHolderLimitGood holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_goods_item, null);
            holder = new ViewHolderLimitGood();
            findview(holder, convertView);
            convertView.setTag(R.id.recommendgood, holder);
        } else {
            holder = (ViewHolderLimitGood) convertView.getTag(R.id.recommendgood);
        }
        setdata(holder, position);
        dealDemo(holder, position);
        return convertView;
    }

    private int count = 0;
    private void dealDemo(ViewHolderLimitGood holder, int position){
        CartGoodsInfo info = data.get(position);
        dealDatas.put(info.getShopid(), holder.ticket);
        if(position == 0)
            dealDatas.put(info.getShopid(), holder.ticket);
        else{
            CartGoodsInfo before = data.get(position - 1);
            if(before.getShopid().equals(info.getShopid()))
                dealDatas.put(info.getShopid(), holder.ticket);
            else{
                count ++;
                dealDatas.put(before.getShopid(), holder.ticket);
            }
        }
    }

    private void setdata(ViewHolderLimitGood holder, int position) {
        CartGoodsInfo info = data.get(position);
        if(isNull(info.getDemo()))
            info.setDemo("");

        holder.layout.setTag(R.id.button, info);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartGoodsInfo info = (CartGoodsInfo) v.getTag(R.id.button);
                if (info != null && isCanClick) {
                }
            }
        });

        int count = 1;
        double d = 0.0;
        if(!"5".equals(keytype)){
            d = Double.parseDouble(info.getPrice());
        }else
            d = Double.parseDouble(info.getCoupon());

        int c = Integer.parseInt(info.getBuycount());
        double total_goodmoney = d * c;
        for (int i = 0; i < data.size(); i++) {
            if (i != position &&
                    data.get(i).getShopid().equals(info.getShopid())) {
                count++;
                int c1 = Integer.parseInt(data.get(i).getBuycount());
                if(!"5".equals(keytype)){
                    double d1 = Double.parseDouble(data.get(i).getPrice());
                    double value = d1 * c1;
                    total_goodmoney = total_goodmoney + value;
                }else{
                    double d1 = Double.parseDouble(data.get(i).getCoupon());
                    double value = d1 * c1;
                    total_goodmoney = total_goodmoney + value;
                }
            }
        }

        goodfee.put(data.get(position).getShopid(), String.valueOf(total_goodmoney));

        //头部的显示隐藏
        if (position == 0) {
            holder.shopname.setVisibility(View.VISIBLE);
            holder.shopname.setText(info.getShopname());
        } else {
            CartGoodsInfo before = data.get(position - 1);
            if (before.getShopid().equals(info.getShopid())) {
                holder.shopname.setVisibility(View.GONE);
            } else {
                holder.shopname.setVisibility(View.VISIBLE);
                holder.shopname.setText(info.getShopname());
            }
        }

        if (position != data.size() - 1) {
            CartGoodsInfo after = data.get(position + 1);
            if (after.getShopid().equals(info.getShopid())) {
                holder.bottom_ln.setVisibility(View.GONE);
            } else {
                holder.bottom_ln.setVisibility(View.VISIBLE);
                if (XtomBaseUtil.isNull(info.getByprice())) {//无满减规则
                    holder.deliver_fee_role.setVisibility(View.INVISIBLE);
                    holder.deliver_fee.setVisibility(View.INVISIBLE);
                    holder.deliver_fee.setText("￥0.00");
                    if(!"5".equals(keytype)){
                        holder.totaltxt.setText("共" + count + "件商品，合计￥" + (total_goodmoney));
                    }else{
                        holder.totaltxt.setText("共" + count + "件商品，合计" + Double.parseDouble(String.valueOf(total_goodmoney)) + "点券");
                    }
                } else {
                    holder.deliver_fee_role.setVisibility(View.VISIBLE);
                    holder.deliver_fee.setVisibility(View.VISIBLE);
                    double fee = Double.parseDouble(info.getByprice());
                    String getExfee = Exfee.get(info.getShopid()) == null ? "0.00" : Exfee.get(info.getShopid()).getShipment();
                    double expfee = Double.parseDouble(getExfee);
                    if (fee == 0.00) {//全场包邮
                        holder.deliver_fee_role.setText(R.string.hm_hlxs_txt_178);
                        holder.deliver_fee.setVisibility(View.INVISIBLE);
                        if(!"5".equals(keytype)){
                            holder.totaltxt.setText("共" + count + "件商品，合计￥" + (total_goodmoney));
                        }else{
                            holder.totaltxt.setText("共" + count + "件商品，合计" + Double.parseDouble(String.valueOf(total_goodmoney)) + "点券");
                        }

                    } else {
                        holder.deliver_fee_role.setText("满(￥" + info.getByprice() + ")包邮");
                        if (total_goodmoney < fee) {
                            if("5".equals(keytype)){
                                expfee = 0.0;
                                holder.totaltxt.setText("共" + count + "件商品，合计" + Double.parseDouble(String.valueOf(total_goodmoney)) + "点券");
                            }else{
                                holder.totaltxt.setText("共" + count + "件商品，合计￥" + (total_goodmoney + expfee));
                            }
                            holder.deliver_fee.setVisibility(View.VISIBLE);
                            holder.deliver_fee.setText("￥" + expfee);
                        } else {
                            if(!"5".equals(keytype)){
                                holder.totaltxt.setText("共" + count + "件商品，合计￥" + (total_goodmoney));
                            }else{
                                holder.totaltxt.setText("共" + count + "件商品，合计" + Double.parseDouble(String.valueOf(total_goodmoney)) + "点券");
                            }
                            holder.deliver_fee.setText("￥0");
                        }
                    }
                }
            }
        } else {
            holder.bottom_ln.setVisibility(View.VISIBLE);
            if (XtomBaseUtil.isNull(info.getByprice())) {//无满减规则
                holder.deliver_fee_role.setVisibility(View.INVISIBLE);
                holder.deliver_fee.setVisibility(View.VISIBLE);
                String send_fee = Exfee.get(info.getShopid()) == null ? "0.00" : Exfee.get(info.getShopid()).getShipment();
                if (send_fee.equals("0.00"))
                    holder.deliver_fee.setVisibility(View.INVISIBLE);
                else {
                    holder.deliver_fee.setVisibility(View.VISIBLE);
                    holder.deliver_fee.setText("￥" + send_fee);
                }

                if(!"5".equals(keytype)){
                    holder.totaltxt.setText("共" + count + "件商品，合计￥" + (total_goodmoney +
                            Double.parseDouble(isNull(send_fee)?"0.00" : send_fee)));
                }else{
                    holder.totaltxt.setText("共" + count + "件商品，合计" + (total_goodmoney +
                            Double.parseDouble(isNull(send_fee)?"0.00" : send_fee)) + "点券");
                }
            } else {
                holder.deliver_fee_role.setVisibility(View.VISIBLE);
                holder.deliver_fee.setVisibility(View.VISIBLE);
                double fee = Double.parseDouble(info.getByprice());
                String getExfee = Exfee.get(info.getShopid()) == null ? "0.00" : Exfee.get(info.getShopid()).getShipment();
                double expfee = Double.parseDouble(getExfee);
                if (fee == 0.00) {//全场包邮
                    holder.deliver_fee_role.setText(R.string.hm_hlxs_txt_178);
                    holder.deliver_fee.setVisibility(View.INVISIBLE);
                    if(!"5".equals(keytype)){
                        holder.totaltxt.setText("共" + count + "件商品，合计￥" + (total_goodmoney));
                    }else{
                        holder.totaltxt.setText("共" + count + "件商品，合计" + Double.parseDouble(String.valueOf(total_goodmoney)) + "点券");
                    }
                } else {
                    holder.deliver_fee_role.setText("满(￥" + info.getByprice() + ")包邮");
                    if (total_goodmoney < fee) {
                        if(!"5".equals(keytype)){
                            holder.totaltxt.setText("共" + count + "件商品，合计￥" + (total_goodmoney + expfee));
                        }else{
                            holder.totaltxt.setText("共" + count + "件商品，合计" + Double.parseDouble(String.valueOf(total_goodmoney + expfee)) + "点券");
                        }

                        holder.deliver_fee.setVisibility(View.VISIBLE);
                        holder.deliver_fee.setText("￥" + expfee);
                    } else {
                        if(!"5".equals(keytype)){
                            holder.totaltxt.setText("共" + count + "件商品，合计￥" + (total_goodmoney));
                        }else{
                            holder.totaltxt.setText("共" + count + "件商品，合计" + Double.parseDouble(String.valueOf(total_goodmoney)) + "点券");
                        }
                        holder.deliver_fee.setText("￥0");
                    }
                }
            }
        }

        holder.ticket.setText(info.getDemo());
        if (info.getImgurl() == null
                || "".equals(info.getImgurl())
                || "null".equals(info.getImgurl())) {
            holder.itemImgView
                    .setImageResource(R.mipmap.hm_icon_def);
        } else {
            ImageLoader.getInstance().displayImage(info.getImgurl(), holder.itemImgView, BaseUtil.displayImageOption());
        }
        holder.itemNameView.setText(info.getName());
        if("5".equals(keytype))
            holder.itemPriceView.setText(info.getCoupon() + "点券");
        else
            holder.itemPriceView.setText("¥" + info.getPrice());

        holder.itemSaleNumView.setText("X" + info.getBuycount());
        holder.itemSpecNameView.setText("规格："+info.getPropertyname());

    }

    private void findview(ViewHolderLimitGood holder, View convertView) {
        holder.itemImgView = (ImageView) convertView.findViewById(R.id.goods_img);
        holder.itemNameView = (TextView) convertView.findViewById(R.id.goods_name);
        holder.itemPriceView = (TextView) convertView.findViewById(R.id.goods_price);
        holder.itemSaleNumView = (TextView) convertView.findViewById(R.id.goods_num);
        holder.itemSpecNameView = (TextView) convertView.findViewById(R.id.goods_spec);
        holder.deliver_fee_role = (TextView) convertView.findViewById(R.id.deliver_fee_role);
        holder.itemTagView = (TextView) convertView.findViewById(R.id.limit_tag);
        holder.optBtn = (TextView) convertView.findViewById(R.id.opt);
        holder.ticket = (EditText) convertView.findViewById(R.id.ticket);
        holder.deliver_fee = (TextView) convertView.findViewById(R.id.deliver_fee);
        holder.totaltxt = (TextView) convertView.findViewById(R.id.total);
        holder.endFl = (FrameLayout) convertView.findViewById(R.id.endFL);
        holder.status_tv = (TextView) convertView.findViewById(R.id.status_tv);
        holder.layout = (RelativeLayout) convertView.findViewById(R.id.con);
        holder.shopname = (TextView) convertView.findViewById(R.id.shop_name);
        holder.bottom_ln = convertView.findViewById(R.id.bottom_ln);
    }

    private static class ViewHolderLimitGood {
        ImageView itemImgView = null;
        TextView itemNameView = null;
        TextView itemPriceView = null;
        TextView itemSaleNumView = null;
        TextView itemSpecNameView = null;
        TextView itemTagView = null;
        TextView shopname = null;
        View bottom_ln;
        TextView deliver_fee;
        TextView deliver_fee_role;
        TextView totaltxt;
        EditText ticket;
        FrameLayout endFl;//已售罄提示框
        TextView status_tv = null;//已售罄或已结束
        RelativeLayout layout;//点击进详情
        TextView optBtn = null;
    }

}
