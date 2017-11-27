package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.AllcouponRecord;

import java.util.List;

/**
 * Created by zuozhongqian on 2017/8/4.
 */

public class CouponAdapter extends HemaAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<AllcouponRecord> data;

    public CouponAdapter(Context mContext, List<AllcouponRecord> data) {
        super(mContext);
        this.context = mContext;

        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
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
            convertView = inflater.inflate(R.layout.item_coupon, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.tv_title.setText(data.get(position).getContent());
        holder.tv_time.setText(data.get(position).getRegdate());
        if (data.get(position).getAmount().contains("-")) {
            holder.tv_number.setTextColor(mContext.getResources().getColor(R.color.gouwu));
            holder.tv_number.setText(data.get(position).getAmount());
        } else {
            holder.tv_number.setTextColor(mContext.getResources().getColor(R.color.my_red));
            holder.tv_number.setText("+" + data.get(position).getAmount());
        }

        return convertView;
    }

    public class ViewHolder {
        public View rootView;
        public TextView tv_title;
        public TextView tv_time;
        public TextView tv_number;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tv_title = (TextView) rootView.findViewById(R.id.tv_title);
            this.tv_time = (TextView) rootView.findViewById(R.id.tv_time);
            this.tv_number = (TextView) rootView.findViewById(R.id.tv_number);
        }

    }
}
