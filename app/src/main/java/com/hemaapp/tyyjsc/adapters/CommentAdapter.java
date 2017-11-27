package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivityComment;
import com.hemaapp.tyyjsc.model.Comment;
import com.hemaapp.tyyjsc.model.HM_ImgInfo;
import com.hemaapp.tyyjsc.showlargepic.ShowLargePicActivity;
import com.hemaapp.tyyjsc.view.CityGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 评论列表
 */
public class CommentAdapter extends HemaAdapter implements OnItemClickListener {

    public static int TYPE_MORE = 0;//查看更多
    public static int TYPE_NORMAL = 1;//评论列表

    private Context context = null;
    private ArrayList<Comment> data = new ArrayList<>();
    //mark == 1 商品详情中评论适配器  mark == 2 评论列表适配器
    private String mark = "";
    private String keyid = "";
    //布局填充器
    private LayoutInflater inflater = null;

    public CommentAdapter(Context context, ArrayList<Comment> data, String mark) {
        super(context);
        this.context = context;
        this.data = data;
        this.mark = mark;
        inflater = LayoutInflater.from(this.context);
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
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
        if ("1".equals(mark)) {//商品详情中评论适配器
            return data == null || data.size() == 0 ? 1 : data.size() > 1 ? 2 : data.size();
        }
        return data == null || data.size() == 0 ? 1 : data.size();
    }

    @Override
    public int getViewTypeCount() {
        if ("1".equals(mark)) {//商品详情中评论适配器
            return 3;
        }
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if ("1".equals(mark)) {
            if (position == 1) {
                return TYPE_MORE;//查看更多评论布局
            }
        }
        return TYPE_NORMAL;//评论列表
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
        int type = getItemViewType(position);
        if (convertView == null) {
            convertView = getView(type);
        }
        setData(type, convertView, position);
        return convertView;
    }

    public View getView(int type) {
        View convertView = null;
        switch (type) {
            case 1://评论列表布局
                inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.comment_item, null);
                ViewHolder holder = new ViewHolder();
                holder.cm_name = (TextView) convertView.findViewById(R.id.name);
                holder.cm_date = (TextView) convertView.findViewById(R.id.date);
                holder.cm_content = (TextView) convertView
                        .findViewById(R.id.content);
                holder.stars = (LinearLayout) convertView.findViewById(R.id.stars);

                holder.middle = convertView.findViewById(R.id.middle);
                holder.imgLayout = (LinearLayout) convertView
                        .findViewById(R.id.imgs);
                holder.gridView = (CityGridView) convertView
                        .findViewById(R.id.gridview);

                holder.cm_logo = (RoundedImageView) convertView
                        .findViewById(R.id.logo);
                holder.cm_logo.setCornerRadius(1000);
                holder.gridView.setOnItemClickListener(this);
                convertView.setTag(R.id.type, holder);
                break;
            case 0://查看更多
                inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.search_empty, null);
                TextView empty = (TextView) convertView.findViewById(R.id.empty);
                LinearLayout lookMoreLayout = (LinearLayout) convertView.findViewById(R.id.look);
                empty.setText("查看更多");
                //查看更多评论
                lookMoreLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ActivityComment.class);
                        intent.putExtra("keyid", keyid);
                        context.startActivity(intent);
                    }
                });
                break;
        }
        return convertView;
    }

    public void setData(int type, View convertView, int position) {
        switch (type) {
            case 1:
                ViewHolder holder = (ViewHolder) convertView.getTag(R.id.type);
                Comment comment = data.get(position);
                holder.cm_name.setText(comment.getNickname());
                holder.cm_date.setText(comment.getRegdate());
                if (isNull(comment.getContent())) {
                    holder.cm_content.setVisibility(View.GONE);
                } else {
                    holder.cm_content.setVisibility(View.VISIBLE);
                    holder.cm_content.setText(comment.getContent());
                }
                //处理评论
                if (!isNull(comment.getReplytype())) {
                    holder.stars.removeAllViews();
                    int starNum = Integer.parseInt(isNull(comment.getReplytype()) ? "0" : comment.getReplytype());
                    for (int i = 0; i < 5; i++) {
                        View imgView = LayoutInflater.from(mContext).inflate(
                                R.layout.star_item, null);
                        ImageView img = (ImageView) imgView.findViewById(R.id.img);
                        if (i < starNum) {
                            img.setImageResource(R.mipmap.ratebar_red);
                        } else
                            img.setImageResource(R.mipmap.ratebar_gray);
                        holder.stars.addView(imgView);
                    }
                } else {
                    for (int i = 0; i < 5; i++) {
                        View imgView = LayoutInflater.from(mContext).inflate(
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
                    ImgAdapter imgAdapter = new ImgAdapter(mContext, imgs);
                    holder.imgLayout.setVisibility(View.VISIBLE);
                    holder.gridView.setAdapter(imgAdapter);
                    holder.gridView.setTag(comment);
                } else {
                    holder.imgLayout.setVisibility(View.GONE);
                }
                break;
            case 0:
                break;
            default:
                break;
        }
    }

    static class ViewHolder {
        private TextView cm_name;
        private TextView cm_content;
        private TextView cm_date;
        private RoundedImageView cm_logo;
        private LinearLayout stars;
        private View middle;

        private LinearLayout imgLayout = null;
        private CityGridView gridView = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Comment comment = (Comment) parent.getTag();

        ArrayList<HM_ImgInfo> imgs = comment.getImgs();
        if (imgs == null || (imgs.size() == 1 && "".equals(imgs.get(0)))) {//默认图片，禁止查看大图
            return;
        }
        Intent it = new Intent(mContext, ShowLargePicActivity.class);
        it.putExtra("position", position);
        it.putExtra("comment", comment);
        mContext.startActivity(it);
    }
}
