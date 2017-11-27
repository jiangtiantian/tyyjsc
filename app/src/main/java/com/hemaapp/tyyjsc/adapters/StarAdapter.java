package com.hemaapp.tyyjsc.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hemaapp.tyyjsc.R;

/**
 * 星级评论适配器
 */
public class StarAdapter extends BaseAdapter {
	
	public Context mContext = null;

	private int pos = -1;
	
	public StarAdapter(Context context, int pos){
		this.mContext = context;
		this.pos = pos;
	}

	public void setPos(int pos){
		this.pos = pos;		 
	}

	@Override
	public int getCount() {		 
		return 5;
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
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.star_item, null);
			holder = new ViewHolder();
			holder.img = (ImageView)convertView.findViewById(R.id.img);
			convertView.setTag(holder);
		}
		holder = (ViewHolder)convertView.getTag();
		if(pos > -1 && position < pos){
			holder.img = (ImageView)convertView.findViewById(R.id.img);
			holder.img.setImageResource(R.mipmap.ratebar_red);
		}else{
			holder.img.setImageResource(R.mipmap.ratebar_gray);
		}
		return convertView;
	}
	
	static class ViewHolder{
		private ImageView img = null;
	}

}
