package com.hemaapp.tyyjsc.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivityAddressEdit;
import com.hemaapp.tyyjsc.model.Address;
import com.hemaapp.tyyjsc.view.HmHelpDialog;

import java.util.ArrayList;

/**
 * 地址列表适配器
 */
public class AddressAdapter extends HemaAdapter implements View.OnClickListener{

    public Context context = null;
    private ArrayList<Address> data = null;
    private onAddressListener listener = null;
    private HmHelpDialog dialog = null;

    private Address address = null;
    private String which;//页面来源 1 提交订单页面; 0我的页面

    public AddressAdapter(Context context, ArrayList<Address> data, String which){
        super(context);

        this.context = context;
        this.data = data;
        this.which = which;
    }
    public void setData(ArrayList<Address> data){
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
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(isEmpty()){
            return  getEmptyView(parent);
        }
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.address_item, null);
            holder = new ViewHolder();
            holder.editView = (TextView)convertView.findViewById(R.id.edit);
            holder.delView = (TextView)convertView.findViewById(R.id.remove);
            holder.defView = (TextView)convertView.findViewById(R.id.def);
            holder.opt = (ImageView)convertView.findViewById(R.id.opt);
            holder.nameView = (TextView)convertView.findViewById(R.id.name);
            holder.telView = (TextView)convertView.findViewById(R.id.tel);
            holder.addressView = (TextView)convertView.findViewById(R.id.address);

            convertView.setTag(R.id.banner, holder);
        }
        holder = (ViewHolder)convertView.getTag(R.id.banner);
        Address address = data.get(position);
        holder.nameView.setText("收货人: " + address.getName());
        holder.telView.setText("电话: " + address.getTel());
        holder.addressView.setText(address.getTotaladdress());
        if("0".equals(which)){
            holder.defView.setVisibility(View.VISIBLE);
            holder.opt.setVisibility(View.INVISIBLE);
            String tip = "";
            int res;
            if("1".equals(address.getRec())){//设为默认地址
                tip = context.getString(R.string.hm_hlxs_txt_117);
                res = R.mipmap.address_checked;
            }else{//默认地址
                tip = context.getString(R.string.hm_hlxs_txt_118);
                res = R.mipmap.address_uncheck;
            }
            Drawable drawable = mContext.getResources().getDrawable(res);
            drawable.setBounds(0,0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.defView.setText(tip);
            holder.defView.setCompoundDrawables(drawable, null, null, null);
            holder.defView.setTag(R.id.def, address);//设置默认
            holder.defView.setOnClickListener(this);
        }else{
            holder.defView.setVisibility(View.INVISIBLE);
            holder.opt.setVisibility(View.VISIBLE);
            holder.opt.setTag(R.id.select, address);
            holder.opt.setOnClickListener(this);
        }
        holder.editView.setOnClickListener(this);
        holder.editView.setTag(R.id.edit, address);//编辑

        holder.delView.setOnClickListener(this);
        holder.delView.setTag(R.id.del, address);//删除
        return convertView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.edit:
                address = (Address)v.getTag(R.id.edit);
                if(address != null){
                    Intent intent = new Intent(context, ActivityAddressEdit.class);
                    intent.putExtra("keytype", "0");//0编辑收货地址；1 新增收货地址
                    intent.putExtra("address", address);
                    context.startActivity(intent);
                }
                break;
            case R.id.remove:
                address = (Address)v.getTag(R.id.del);
                if(dialog == null){
                    dialog = new HmHelpDialog(context, 0);
                }
                dialog.setTitleName(context.getString(R.string.hm_hlxs_txt_119));
                dialog.setListener(new HmHelpDialog.OnCancelOrConfirmListener() {
                    @Override
                    public void onCancel(int type) {
                        dialog.cancel();
                    }

                    @Override
                    public void onConfirm(String msg) {
                        dialog.cancel();
                        if(address != null && listener != null){
                            listener.onDelClick(address);
                        }
                    }
                });
                dialog.show();
                break;
            case R.id.def:
                address = (Address)v.getTag(R.id.def);
                if(address != null){
                    if("1".equals(address.getRec())){//当前地址为默认地址
                        //nothing
                    }else{
                        if(listener != null){
                            listener.onDefClick(address);
                        }
                    }
                }
                break;
            case R.id.opt:
                address = (Address)v.getTag(R.id.select);
                ImageView opt = (ImageView)v;
                opt.setImageResource(R.mipmap.address_checked);
                if(listener != null){
                    listener.onSelect(address);
                }
                break;
            default:
                break;
        }
    }

    class ViewHolder{
        private TextView nameView = null;
        private TextView telView = null;
        private TextView addressView = null;

        private ImageView opt = null;

        private TextView defView = null;
        private TextView editView = null;
        private TextView delView = null;
    }
    public void setListener(onAddressListener listener){
        this.listener = listener;
    }
    public interface onAddressListener{
        void onDefClick(Address address);
        void onDelClick(Address address);
        void onSelect(Address address);
    }
}
