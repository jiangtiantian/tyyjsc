package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivityMemeberGoodsInfo;
import com.hemaapp.tyyjsc.model.MembergoodsList;

import java.util.List;

/**
 * Created by zuozhongqian on 2017/8/3.
 */
public class MembergoodsListAdapter extends HemaAdapter {
    private List<MembergoodsList> datas;
    private Context context;
    private LayoutInflater inflater;

    public MembergoodsListAdapter(Context mContext, List<MembergoodsList> datas) {
        super(mContext);
        this.context = mContext;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
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
        ViewHolder holder=null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_member, parent, false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder= (ViewHolder) convertView.getTag();
        }
        Glide.with(context)
                .load(datas.get(position).getImgurl())
                .into( holder.iv_imageView);
        //ImageLoader.getInstance().displayImage(datas.get(position).getImgurl(), holder.iv_imageView);
        holder.tv_title.setText(datas.get(position).getName());
        holder.tv_shale.setText("已售"+datas.get(position).getDisplaysales()); //已售
        holder.tv_kucun.setText("库存"+datas.get(position).getStock());     //库存
        holder.tv_dianjuan.setText(datas.get(position).getCoupon()+"点券"); // 点券

        holder.btn_rightAway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ActivityMemeberGoodsInfo.class);
                intent .putExtra("type", "5");
                intent .putExtra("id",datas.get(position).getId());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public  class ViewHolder {
        public View rootView;
        public ImageView iv_imageView;
        public TextView tv_title;
        public TextView tv_shale;
        public TextView tv_kucun;
        public TextView tv_dianjuan;
        public Button btn_rightAway;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.iv_imageView = (ImageView) rootView.findViewById(R.id.iv_imageView);
            this.tv_title = (TextView) rootView.findViewById(R.id.tv_title);
            this.tv_shale = (TextView) rootView.findViewById(R.id.tv_shale);
            this.tv_kucun = (TextView) rootView.findViewById(R.id.tv_kucun);
            this.tv_dianjuan = (TextView) rootView.findViewById(R.id.tv_dianjuan);
            this.btn_rightAway = (Button) rootView.findViewById(R.id.btn_rightAway);
        }
    }
}
