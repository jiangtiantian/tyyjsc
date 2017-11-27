package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.Notice;

import java.util.ArrayList;

/**
 * 系统通知
 */
public class ChatAdapter extends HemaAdapter implements OnClickListener {

    private ArrayList<Notice> data = null;
    private onDelNoticeListener listener = null;

    public ChatAdapter(Context mContext, ArrayList<Notice> data) {
        super(mContext);
        this.data = data;
    }

    @Override
    public boolean isEmpty() {
        return data == null || data.size() == 0;
    }

    public void setData(ArrayList<Notice> data) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isEmpty()) {
            return getEmptyView(parent);
        }
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.chat_item, null);
            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.del = (TextView) convertView.findViewById(R.id.del_tv);
            holder.divider = convertView.findViewById(R.id.divider);
            holder.dot = (ImageView) convertView.findViewById(R.id.dot);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        Notice notice = data.get(position);
        holder.name.setText(notice.getContent());
        holder.date.setText(notice.getRegdate());
//        if(notice.getAvatar() == null
//                || "".equals(notice.getAvatar())
//                || "null".equals(notice.getAvatar()) || !notice.getAvatar().startsWith("http")){
//            holder.logo.setImageResource(R.mipmap.hm_icon_msg_def_img);
//        }else{;
//            ImageLoader.getInstance().displayImage(notice.getAvatar(), holder.logo, BaseUtil.displayImageOption());
//        }
        if ("1".equals(notice.getLooktype())) {//未读
            holder.dot.setVisibility(View.VISIBLE);
        } else {//已读
            holder.dot.setVisibility(View.INVISIBLE);
        }
        if (position == getCount() - 1) {
            holder.divider.setVisibility(View.GONE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
        }
        holder.del.setTag(R.id.type, notice);
        holder.del.setOnClickListener(this);
        return convertView;
    }

    static class ViewHolder {
        private TextView date = null;
        private TextView name = null;
        private ImageView dot = null;
        private TextView del;
        private View divider;
    }

    @Override
    public void onClick(View v) {
        Notice notice = (Notice) v.getTag(R.id.type);
        switch (v.getId()) {
            case R.id.del_tv:
                if (listener != null) {
                    listener.onDealNotice(notice);
                }
                break;
            default:
                break;
        }

    }

    public void setListener(onDelNoticeListener listener) {
        this.listener = listener;
    }

    public interface onDelNoticeListener {
        void onDealNotice(Notice notice);
    }
}
