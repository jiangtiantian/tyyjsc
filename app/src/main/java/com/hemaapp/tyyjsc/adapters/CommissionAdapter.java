package com.hemaapp.tyyjsc.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.DealItem;

import java.util.ArrayList;

/**
 * 储值卡列表
 */
public class CommissionAdapter extends HemaAdapter {

    private ArrayList<DealItem> data = null;

    public CommissionAdapter(Context  context, ArrayList<DealItem> data){
        super(context);
        this.data = data;
    }
    public void setData(ArrayList<DealItem> data){
        this.data = data;
    }
    private AddCardListener addCardListener;

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
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(isEmpty()){
            return getEmptyView(parent);
        }
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.commission_item, null);
            holder = new ViewHolder(convertView,position);
            convertView.setTag(R.id.type, holder);

        }
        holder = (ViewHolder)convertView.getTag(R.id.type);
        holder.setData(position);
        return convertView;
    }
    class ViewHolder{
        private TextView price;
        private TextView final_price;
        private TextView buy;
        private TextView cardname;
        private int postion;
        public ViewHolder(View convertView, final int position)
        {
            this.postion=position;
            price = (TextView) convertView.findViewById(R.id.price);
            final_price = (TextView)convertView.findViewById(R.id.final_price);
            buy = (TextView)convertView.findViewById(R.id.buy);
            cardname= (TextView) convertView.findViewById(R.id.cardname);
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addCardListener.addCart(data.get(position).getId(),data.get(position).getPrice());
                }
            });
        }
        public void setData(int position)
        {
            price.setText("￥"+data.get(position).getMoney());
            final_price.setText("￥"+data.get(position).getPrice());
            cardname.setText("["+data.get(position).getName()+"]");
        }
    }
    public void setListener(AddCardListener addCardListener)
    {
        this.addCardListener=addCardListener;
    }
    public interface AddCardListener
    {
        public void addCart(String cardid,String money);
    }

}
