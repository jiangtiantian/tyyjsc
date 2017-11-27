package com.hemaapp.tyyjsc.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivityVoucherList;
import com.hemaapp.tyyjsc.model.VourcherInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * 代金券
 */
public class VoucherAdapter extends HemaAdapter implements View.OnClickListener {
    private Context context = null;
    private ArrayList<VourcherInfo> data = new ArrayList<>();
    private boolean hasUseed = false;//是否存在已过期代金券
    private String which = "";
    private String money = null;
    private boolean bool;//是否已经使用

    public VoucherAdapter(Context context, ArrayList<VourcherInfo> data, String which, boolean bool) {
        super(context);
        this.bool = bool;
        this.context = context;
        this.data = data;
        this.which = which;
    }

    public void setVourcherInfo(String money) {
        this.money = money;
    }

    public void setData(ArrayList<VourcherInfo> data) {
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
        ViewHolder viewholder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.voucher_item, null);
            viewholder = new ViewHolder(convertView);
            convertView.setTag(R.id.type, viewholder);
        }
        viewholder = (ViewHolder) convertView.getTag(R.id.type);
        VourcherInfo info = data.get(position);
        if (bool) {//过期
            viewholder.sel.setVisibility(View.VISIBLE);
        } else {
            viewholder.sel.setVisibility(View.INVISIBLE);
        }
        viewholder.imgView.setText("¥" + info.getMoney());
        viewholder.nameView.setText(info.getName());
        viewholder.infoView.setText(info.getDemo());
        if (isNull(info.getEndstate()) || isNull(info.getRegdate())) {
            viewholder.dateView.setText("起始时间");
        } else {
            try {
                viewholder.dateView.setText(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(info.getRegdate())) + "~" + new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(info.getEndstate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        viewholder.cn.setOnClickListener(this);
        viewholder.cn.setTag(info);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (!"1".equals(which)) {
            VourcherInfo info = (VourcherInfo) v.getTag();
            Double money1 = Double.parseDouble(money);
            Double money2 = Double.parseDouble(isNull(info.getMaxmoney()) ? "0.00" : info.getMaxmoney());
            if (money1 >= money2) {
                ((ActivityVoucherList) context).finishActivity(info);
            } else
                Toast.makeText(context, "不满足金额", Toast.LENGTH_SHORT).show();
        }
    }

    class ViewHolder {
        public TextView imgView;
        public TextView nameView;
        public TextView infoView;
        public TextView dateView;
        public ImageView sel;
        private RelativeLayout cn;

        public ViewHolder(View convertView) {
            imgView = (TextView) convertView.findViewById(R.id.logo);
            nameView = (TextView) convertView.findViewById(R.id.name);
            infoView = (TextView) convertView.findViewById(R.id.info);
            dateView = (TextView) convertView.findViewById(R.id.date);
            sel = (ImageView) convertView.findViewById(R.id.sel);
            cn = (RelativeLayout) convertView.findViewById(R.id.cn);

        }
    }
}
