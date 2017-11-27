package com.hemaapp.tyyjsc.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.AttrInfo;
import com.hemaapp.tyyjsc.model.HM_GoodsInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.HashMap;

import xtom.frame.XtomObject;
import xtom.frame.util.XtomToastUtil;

/**
 * 属性选择框
 */
public class AttrSelectorDialog extends XtomObject implements View.OnClickListener {
    private Dialog mDialog = null;
    private Context context = null;
    private View convertView = null;//Dialog布局
    //    private LinearLayout container = null;//属性容器
    private RoundedImageView img = null;
    private TextView priceView = null;//价格
    private TextView nameView = null;//商品名字
    private TextView itemStockTv = null;//显示“库存”
    private TextView itemStockView = null;//库存数量
    private ImageView addBtn = null;//增1
    private ImageView minusBtn = null;//减1
    private TextView numView = null;//显示购物车数量
    private ImageView close = null;//关闭弹框
    private Button add = null;//加入购物车
    private ArrayList<AttrInfo> attrs = null;
    private HashMap<Integer, String> map = new HashMap<>();//保存用户选中的属性id
    private String specs_id = "", specs_value = "", specs_price;//组合属性
    private String specsName = "", specs_weight;//混合属性名字
    private String price = "0";//组合属性下的商品价格
    private String weight = "0";
    private FlowLayout flowLayout;
    private onAddCardListener listener = null;
    private int buycount = 1;//购买数量

    private HM_GoodsInfo info = null;
    private LinearLayout layout_attri;

    public AttrSelectorDialog(Context context, HM_GoodsInfo info) {
        this.context = context;
        this.info = info;
        if (info != null)
            this.attrs = this.info.getAttrs();
        mDialog = new Dialog(context, R.style.dialog);
        convertView = LayoutInflater.from(context).inflate(
                R.layout.pop_attr_layout, null);
        priceView = (TextView) convertView.findViewById(R.id.price);
        nameView = (TextView) convertView.findViewById(R.id.name);
        layout_attri = (LinearLayout) convertView.findViewById(R.id.layout_attri);
        flowLayout = (FlowLayout) convertView.findViewById(R.id.labels);
        itemStockTv = (TextView) convertView.findViewById(R.id.item_stock_tv);
        itemStockView = (TextView) convertView.findViewById(R.id.item_stock);
        img = (RoundedImageView) convertView.findViewById(R.id.img);
        img.setCornerRadius(3);
        numView = (TextView) convertView.findViewById(R.id.num);
        addBtn = (ImageView) convertView.findViewById(R.id.num_m);
        minusBtn = (ImageView) convertView.findViewById(R.id.num_p);
        close = (ImageView) convertView.findViewById(R.id.close);
        add = (Button) convertView.findViewById(R.id.add);
        close.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        minusBtn.setOnClickListener(this);
        add.setOnClickListener(this);
        init();
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        Rect rect = new Rect();
        View view = window.getDecorView();
        view.getWindowVisibleDisplayFrame(rect);
        windowparams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(windowparams);
        //设置显示动画
        window.setWindowAnimations(R.style.anim);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setContentView(convertView);
    }

    public void setGoodInfo(HM_GoodsInfo info) {
        this.info = info;
        init();
    }

    public void hideAttri(){
        layout_attri.setVisibility(View.GONE);
    }

    public String getCoupon() {
        return specs_value;
    }

    public void init() {
        specsName = "";
        //价格
        if (info.getCoupon() != null) {
            priceView.setText(info.getCoupon() + "点券");
        } else {
            priceView.setText("¥" + info.getPrice());
        }

        //商品名字
        nameView.setText(info.getName());
        //商品总库存
        /**
         * 库存：限时抢购和特价预订有库存；推荐商品和超值套餐没有库存
         */
        if (("1".equals(info.getKeytype()) && "2".equals(info.getIs_display())) || "2".equals(info.getKeytype()) || "3".equals(info.getKeytype()) || "4".equals(info.getKeytype())) {
            itemStockTv.setVisibility(View.VISIBLE);
            itemStockView.setVisibility(View.VISIBLE);
            itemStockView.setText(info.getStock());
        } else {
            itemStockView.setVisibility(View.GONE);
            itemStockTv.setVisibility(View.GONE);
        }
        //商品图片
        if (!isNull(info.getImgurl())) {
            ImageLoader.getInstance().displayImage(info.getImgurl(), img);
        } else {
            img.setBackgroundResource(R.mipmap.hm_icon_def);
        }
        //清空上一次操作缓存的数据
        specs_id = "";
        specs_value = "";
        specs_price = "";
        specs_weight = "";
        map.clear();
        numView.setText("1");
        buycount = 1;
        flowLayout.removeAllViews();
        for (int i = 0; i < attrs.size(); i++) {
            View child = LayoutInflater.from(context).inflate(R.layout.label_item, null);
            TextView labelNameView = (TextView) child.findViewById(R.id.labelname);
            labelNameView.setText(attrs.get(i).getPath());
            if (i == 0) {//规格下的第一个属性默认选中
                specs_value = attrs.get(0).getCoupon();
                specs_price = attrs.get(0).getPrice();
                specs_weight = attrs.get(0).getWeight();
                if ("5".equals(info.getKeytype()))
                    price = attrs.get(0).getCoupon();
                else
                    price = attrs.get(0).getPrice();
                labelNameView.setBackgroundResource(R.drawable.label_filter_shape_p);
                labelNameView.setTextColor(context.getResources().getColor(R.color.black));
            } else {
                labelNameView.setBackgroundResource(R.drawable.label_filter_shape_n);
                labelNameView.setTextColor(context.getResources().getColor(R.color.black));
            }
            labelNameView.setTag(R.id.type, attrs.get(i).getId());
            labelNameView.setOnClickListener(new onDealLabelListener(flowLayout, i));
            map.put(i, attrs.get(i).getId());
            flowLayout.addView(child);
        }
        if (attrs.size() != 0)
            curAttrPriceShow(0);
    }


    public void setAddTxt(String txt) {
        add.setText(txt);
    }

    public void show() {
        mDialog.show();
    }

    public void cancel() {
        mDialog.cancel();
    }

    @Override
    public void onClick(View v) {
        buycount = Integer.parseInt(numView.getText().toString().trim());
        switch (v.getId()) {
            case R.id.num_m://执行数量加1
                //检测商品库存是否充足
                if (attrs.size() != 0) {
                    if (isNull(specs_id)) {
                        XtomToastUtil.showShortToast(context, "请选择属性");
                        return;
                    }
                }
                //特价预订和限时抢购需要判断库存和限购数量
                int limit_num = 0;//限购数量
                int stock_num = Integer.parseInt(isNull(itemStockView.getText().toString().trim()) ? "0" : itemStockView.getText().toString().trim());
                //获取限购数量，处理非法输入
                try {
                    limit_num = Integer.parseInt(isNull(info.getLimit_num()) ? "0" : info.getLimit_num());
                } catch (Exception e) {
                    limit_num = 0;
                    log_d(e.toString());
                }
                if (limit_num != 0 && buycount >= limit_num) {
                    XtomToastUtil.showShortToast(context, "已超限购数量");
                    return;
                }
                if (buycount >= stock_num) {
                    XtomToastUtil.showShortToast(context, "库存不足");
                    return;
                }
                price = specs_value;
                weight = specs_weight;
                buycount++;
                numView.setText(String.valueOf(buycount));
                break;
            case R.id.num_p:
                if (buycount - 1 <= 0) {
                    return;
                }
                price = specs_value;
                weight = specs_weight;
                buycount--;
                numView.setText(String.valueOf(buycount));
                break;
            case R.id.close:
                cancel();
                break;
            case R.id.add:
                if (attrs.size() == 0) {
                    if (listener != null) {
                        if (info.getCoupon() != null) {
                            listener.onAddCart(specsName, specs_id, String.valueOf(buycount), specs_value, weight);
                        } else {
                            listener.onAddCart(specsName, specs_id, String.valueOf(buycount), specs_value, weight);
                        }

                    }
                } else {
                    if (isNull(specs_id)) {
                        XtomToastUtil.showShortToast(context, "请选择属性");
                        return;
                    }
                    if (listener != null) {
                        if (info.getCoupon() != null) {
                            listener.onAddCart(specsName, isNull(specs_id) ? "0" : specs_id, String.valueOf(buycount), specs_value, weight);
                        } else {
                            listener.onAddCart(specsName, isNull(specs_id) ? "0" : specs_id, String.valueOf(buycount), specs_price, weight);
                        }
                    }
                }
                break;
            default:
        }
    }

    //处理流布局事件
    class onDealLabelListener implements View.OnClickListener {
        private FlowLayout flowLayout = null;
        private int x = 0;

        public onDealLabelListener(FlowLayout flowLayout, int x) {
            this.flowLayout = flowLayout;
            this.x = x;
        }

        @Override
        public void onClick(View v) {
            for (int i = 0; i < flowLayout.getChildCount(); i++) {
                View child = flowLayout.getChildAt(i);
                TextView labelNameView = (TextView) child.findViewById(R.id.labelname);
                if (i == x) {
                    map.put(x, attrs.get(x).getId());
                    labelNameView.setBackgroundResource(R.drawable.label_filter_shape_p);
                    buycount = 1;
                    numView.setText("1");
                    if (info.getCoupon() != null) {
                        priceView.setText(attrs.get(i).getCoupon() + "点券");
                        price = attrs.get(i).getCoupon();
                        specs_value = attrs.get(i).getCoupon();
                        specs_price = attrs.get(i).getPrice();
                        specs_weight = attrs.get(i).getWeight();
                    } else {
                        priceView.setText("￥" + attrs.get(i).getPrice());
                        price = attrs.get(i).getPrice();
                        specs_value = attrs.get(i).getCoupon();
                        specs_price = attrs.get(i).getPrice();
                        specs_weight = attrs.get(i).getWeight();
                    }

                } else {
                    labelNameView.setBackgroundResource(R.drawable.label_filter_shape_n);
                }
            }
            curAttrPriceShow(x);
        }
    }

    //当前所选商品属性价格
    public void curAttrPriceShow(int i) {
        if (map.size() == attrs.size()) {  //编辑确定组合属性
            AttrInfo maxInfo = attrs.get(i);
            specs_id = maxInfo.getId();
            specsName = maxInfo.getPath();
            specs_weight = maxInfo.getWeight();
           /* priceView.setText("¥" + maxInfo.getPrice());*/
            itemStockView.setText(maxInfo.getStock());
        }

    }

    public void setListener(onAddCardListener listener) {
        this.listener = listener;
    }

    public interface onAddCardListener {
        void onAddCart(String goods_specName, String spec_id, String num, String price, String weight);//price字段在立即购物时用到
    }
}
