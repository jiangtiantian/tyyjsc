package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.BaseFragmentActivity;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivityMemeberGoodsInfo;
import com.hemaapp.tyyjsc.model.MembergoodsList;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.image.load.XtomImageTask;

/**
 *
 */

public class MemberGoodsListAdapters extends HemaAdapter {

    private ArrayList<MembergoodsList> data;
    private String type;
    private int width = 0;

    public MemberGoodsListAdapters(Context mContext, ArrayList<MembergoodsList> data) {
        super(mContext);
        this.data = data;
        DisplayMetrics dm = new DisplayMetrics();
        ((BaseFragmentActivity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
    }

    public void setData(ArrayList<MembergoodsList> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        int size=0;
        if(data!=null) {
            if(data.size()>3)
                return 3;
            else
                return data.size();
        }else
            return size;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.listitem_first_tuijian, null);
            holder = new ViewHolder();
            findview(holder, view);
            view.setTag(R.id.button, holder);
        }else{
            holder = (ViewHolder) view.getTag(R.id.button);
        }

        MembergoodsList good = data.get(i);
        try {
            URL url = new URL(good.getImgurl());
            ((BaseFragmentActivity)mContext).imageWorker.loadImage(new XtomImageTask(holder.imageView, url, mContext));
        } catch (MalformedURLException e) {
            holder.imageView.setImageResource(R.mipmap.hm_icon_def);
        }
        setImageSize(holder.imageView);
        holder.tv_name.setText(good.getName());
        holder.tv_price.setText(good.getCoupon()+"点券");

        view.setTag(R.id.buy, good);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MembergoodsList good = (MembergoodsList) view.getTag(R.id.buy);
                Intent it = new Intent(mContext, ActivityMemeberGoodsInfo.class);
                it.putExtra("id", good.getId());
                mContext.startActivity(it);
            }
        });
        return view;
    }

    private void setImageSize(ImageView imageView){
        int v1 = width - mContext.getResources().getDimensionPixelSize(R.dimen.margin_20)*3;
        int w = v1/3;
        double h = w / 4 * 3;
        imageView.setLayoutParams(new LinearLayout.LayoutParams(w, (int)h));
    }

    private void findview(ViewHolder holder, View view){
        holder.imageView = (ImageView) view.findViewById(R.id.img_good);
        holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
        holder.tv_price = (TextView) view.findViewById(R.id.tv_price);
    }

    private static class ViewHolder{
        ImageView imageView;
        TextView tv_name;
        TextView tv_price;
    }
}
