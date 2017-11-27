package com.hemaapp.tyyjsc.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.hm_FrameWork.view.ShowLargeImageView;
import com.hemaapp.tyyjsc.BaseActivity;
import com.hemaapp.tyyjsc.BaseUtil;
import com.hemaapp.tyyjsc.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;

import xtom.frame.image.load.XtomImageTask;

/**
 * 评论图片适配器
 */
public class SendImageAdapter extends HemaAdapter implements OnClickListener {
    private static final int TYPE_ADD = 0;
    private static final int TYPE_IMAGE = 1;
    private View rootView;
    private ArrayList<String> images;

    private int PicCount = 4;// 最大图片数量

    public SendImageAdapter(Context mContext, View rootView,
                            ArrayList<String> images, int count) {
        super(mContext);
        this.rootView = rootView;
        this.images = images;
        this.PicCount = count;

    }
    public void setData(ArrayList<String> images){
        this.images = images;
    }

    @Override
    public int getCount() {
        int count = 0;
        int size = images == null ? 1 : images.size();
        if (size < PicCount)
            count = size + 1;
        else
            count = PicCount;
        return count;
    }

    @Override
    public boolean isEmpty() {
        int size = images == null ? 0 : images.size();
        return size == 0;
    }

    @Override
    public int getItemViewType(int position) {
        int size = images == null ? 0 : images.size();
        int count = getCount();
        if (size < PicCount) {
            if (position == count - 1)
                return TYPE_ADD;
            else
                return TYPE_IMAGE;
        } else {
            return TYPE_IMAGE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder holder = null;
        if (convertView == null) {
            switch (type) {
                case TYPE_ADD:
                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.addpic_item, null);
                    holder = new ViewHolder();
                    findView(convertView, holder);
                    convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
                    break;
                case TYPE_IMAGE:
                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.addpic_item, null);
                    holder = new ViewHolder();
                    findView(convertView, holder);
                    convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
                    break;
            }
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }

        switch (type) {
            case TYPE_ADD:
                setDataAdd(position, holder);
                break;
            case TYPE_IMAGE:
                setDataImage(position, holder);
                break;
        }

        return convertView;
    }

    private void setDataAdd(int position, ViewHolder holder) {
        holder.add.setOnClickListener(this);
    }

    private void setDataImage(int position, ViewHolder holder) {
        String path = images.get(position);
        holder.add.setVisibility(View.GONE);
        holder.imageView.setVisibility(View.VISIBLE);
        holder.deleteButton.setVisibility(View.VISIBLE);
        holder.deleteButton.setTag(path);
        holder.deleteButton.setOnClickListener(this);
        holder.imageView.setCornerRadius(0);
        log_e("----------- path = " + path);
//        ImageLoader.getInstance().displayImage(path, holder.imageView, BaseUtil.displayImageOption());
        XtomImageTask imageTask = new XtomImageTask(holder.imageView, path,
                mContext);
        ((BaseActivity)mContext).imageWorker.loadImage(imageTask);
        holder.imageView.setTag(R.id.TAG, path.replace("file://", ""));
        holder.imageView.setOnClickListener(this);
    }

    private void findView(View view, ViewHolder holder) {
        holder.add = (ImageButton) view.findViewById(R.id.share_addimg3);
        holder.imageView = (RoundedImageView) view
                .findViewById(R.id.share_addimg4);
        holder.deleteButton = (ImageButton) view.findViewById(R.id.pic_delete);
    }

    private static class ViewHolder {
        ImageView add;
        RoundedImageView imageView;
        ImageButton deleteButton;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pic_delete:
                String dPath = (String) v.getTag();
                File file = new File(dPath);
                file.delete();
                images.remove(dPath);
                notifyDataSetChanged();
                break;
            case R.id.share_addimg4:
                String iPath = (String) v.getTag(R.id.TAG);
                mView = new ShowLargeImageView((Activity) mContext, rootView);
                mView.show();
                mView.setImagePath(iPath);
                break;
            default:
                ((BaseActivity)mContext).openAlbum();
                break;
        }
    }

    private ShowLargeImageView mView;
}
