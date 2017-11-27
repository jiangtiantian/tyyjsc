package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.HM_ImgInfo;
import com.hemaapp.tyyjsc.view.CusImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 评价图片适配器
 */
public class ImgAdapter extends HemaAdapter {

    private ArrayList<HM_ImgInfo> data = null;

    public ImgAdapter(Context mContext, ArrayList<HM_ImgInfo> data) {
        super(mContext);

        this.data = data;
    }
    public void setData(ArrayList<HM_ImgInfo> data){
        this.data = data;
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.img_item, null);
            holder = new ViewHolder();
            holder.img = (CusImageView) convertView.findViewById(R.id.img);
            holder.img.setRatio(1);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        HM_ImgInfo imgInfo = data.get(position);
        if (imgInfo.getImgurl() == null
                || "".equals(imgInfo.getImgurl())
                || "null".equals(imgInfo.getImgurl())) {
            holder.img
                    .setImageResource(R.mipmap.hm_icon_def);
        } else {
            ImageLoader.getInstance().displayImage(imgInfo.getImgurl(), holder.img, BaseUtil.displayImageOption());
        }
        return convertView;
    }

    static class ViewHolder {
        private CusImageView img;
    }
}
