package com.hemaapp.tyyjsc.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.Comment;
import com.hemaapp.tyyjsc.model.HM_ImgInfo;
import com.hemaapp.tyyjsc.showlargepic.ShowLargePicActivity;
import com.hemaapp.tyyjsc.view.CityGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 评论列表
 */
public class NewCommentAdapter extends HemaAdapter implements AdapterView.OnItemClickListener {
    private Context context = null;
    private ArrayList<Comment> data = new ArrayList<>();
    //布局填充器
    private LayoutInflater inflater = null;

    public NewCommentAdapter(Context context, ArrayList<Comment> data) {
        super(context);
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(this.context);
    }

    //设置数据
    public void setData(ArrayList<Comment> data) {
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

        return data.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isEmpty()) {
            return getEmptyView(parent);
        }
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.new_comment_item, null);
            holder.cm_name = (TextView) convertView.findViewById(R.id.name);
            holder.cm_date = (TextView) convertView.findViewById(R.id.date);
            holder.cm_content = (TextView) convertView
                    .findViewById(R.id.content);
            holder.stars = (LinearLayout) convertView.findViewById(R.id.stars);
            holder.middle = convertView.findViewById(R.id.middle);
            holder.cm_logo = (RoundedImageView) convertView
                    .findViewById(R.id.logo);
            holder.cm_logo.setCornerRadius(1000);

            holder.gridView = (CityGridView) convertView
                    .findViewById(R.id.gridview);
            holder.gridView.setOnItemClickListener(this);
            convertView.setTag(R.id.type, holder);
        }
        holder = (ViewHolder) convertView.getTag(R.id.type);
        Comment comment = data.get(position);
        holder.cm_name.setText(comment.getNickname());
        holder.cm_date.setText(comment.getRegdate());
        if (isNull(comment.getContent())) {
            holder.cm_content.setVisibility(View.GONE);
        } else {
            holder.cm_content.setVisibility(View.VISIBLE);
            holder.cm_content.setText(comment.getContent());
        }
        holder.cm_content.setText(comment.getContent());
        //处理评论
        if (!isNull(comment.getReplytype())) {
            holder.stars.removeAllViews();
            int starNum = Integer.parseInt(isNull(comment.getReplytype()) ? "0" : comment.getReplytype());
            for (int i = 0; i < 5; i++) {
                View imgView = inflater.inflate(
                        R.layout.star_item, null);
                ImageView img = (ImageView) imgView.findViewById(R.id.img);
                if (i < starNum) {
                    img.setImageResource(R.mipmap.ratebar_red);
                }else
                    img.setImageResource(R.mipmap.ratebar_gray);
                holder.stars.addView(imgView);
            }
        }else
        {
            for (int i = 0; i < 5; i++) {
                View imgView = inflater.inflate(
                        R.layout.star_item, null);
                ImageView img = (ImageView) imgView.findViewById(R.id.img);
                img.setImageResource(R.mipmap.ratebar_gray);
                holder.stars.addView(imgView);
            }
        }
        //评论列表
        if (comment.getAvatar() == null
                || "".equals(comment.getAvatar())
                || "null".equals(comment.getAvatar())) {
            holder.cm_logo
                    .setImageResource(R.mipmap.ic_launcher);
        } else {
            ImageLoader.getInstance().displayImage(comment.getAvatar(), holder.cm_logo, BaseUtil.displayImageOption());
        }
        if (getCount() == 1) {
            holder.middle.setVisibility(View.VISIBLE);
        } else {
            if (position == data.size() - 1) {
                holder.middle.setVisibility(View.INVISIBLE);
            } else {
                holder.middle.setVisibility(View.VISIBLE);
            }
        }
        //评价图片
        ArrayList<HM_ImgInfo> imgs = comment.getImgs();
        if (imgs != null && imgs.size() > 0) {
            holder.gridView.setVisibility(View.VISIBLE);
            ImgAdapter imgAdapter = new ImgAdapter(mContext, imgs);
            holder.gridView.setAdapter(imgAdapter);
            holder.gridView.setTag(comment);
        } else {
            holder.gridView.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        Comment comment = (Comment) parent.getTag();

        ArrayList<HM_ImgInfo> imgs = comment.getImgs();
        if(imgs == null || (imgs.size() == 1 && "".equals(imgs.get(0)))){//默认图片，禁止查看大图
            return;
        }
        Intent it = new Intent(mContext, ShowLargePicActivity.class);
        it.putExtra("position", position);
        it.putExtra("comment", comment);
        mContext.startActivity(it);
    }

    static class ViewHolder {
        private TextView cm_name;
        private TextView cm_content;
        private TextView cm_date;
        private RoundedImageView cm_logo;
        private LinearLayout stars;
        private View middle;

        private CityGridView gridView = null;
    }

}
