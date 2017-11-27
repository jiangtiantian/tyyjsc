package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.activity.ActivitySearchResult;
import com.hemaapp.tyyjsc.db.SearchDBClient;

import java.util.List;

import xtom.frame.XtomAdapter;

/**
 * 搜索历史记录适配器
 */

public class SearchHistoryAdapter extends XtomAdapter {

    private List<String> historys;

    private static final int VIEWTYPE_NORMAL = 0;
    private static final int VIEWTYPE_BOTTOM = 1;
    private static final int VIEWTYPE_EMPTY = 2;


    private SearchDBClient searchDBClient = null;


    public SearchHistoryAdapter(Context context, List<String> historys) {
        super(context);
        this.historys = historys;
        searchDBClient = SearchDBClient.get(mContext);
    }

    public void setListData(List<String> historys) {
        this.historys = historys;
    }

    @Override
    public boolean isEmpty() {
        return historys == null || historys.size() == 0;
    }

    @Override
    public int getCount() {
        if (historys == null || historys.size() == 0)
            return 1;
        else
            return (historys.size() + 1);
    }

    @Override
    public int getItemViewType(int position) {
        if(isEmpty()){
            return VIEWTYPE_EMPTY;
        }
        if (position < historys.size()) {
            return VIEWTYPE_NORMAL;
        }

        return VIEWTYPE_BOTTOM;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
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
        int type = getItemViewType(position);
        if (convertView == null) {
            convertView = get(type);
        }
        if (type == VIEWTYPE_NORMAL)
            setData(convertView, position);
        else
            setClearView(convertView);
        return convertView;
    }

    private void setData(View view, int position) {
        ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.TAG);

        String hd = historys.get(position);
        viewHolder.searchtxt.setText(hd);
        view.setOnClickListener(new OnSearchClickListener(position));
        if (position == historys.size() - 1) {
            viewHolder.bottom.setVisibility(View.VISIBLE);
            viewHolder.middle.setVisibility(View.GONE);
        } else {
            viewHolder.bottom.setVisibility(View.GONE);
            viewHolder.middle.setVisibility(View.VISIBLE);
        }
        view.setTag(hd);
    }

    private void setClearView(View view) {
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void notifyDataSetChanged() {
        searchDBClient.clear("1");
        historys = searchDBClient.select("1");
        super.notifyDataSetChanged();
    }

    private View get(int type) {
        View view = null;
        switch (type) {
            case VIEWTYPE_NORMAL:
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.listitem_searchhistory, null);
                ViewHolder viewholder = new ViewHolder();
                findView(viewholder, view);
                view.setTag(R.id.TAG, viewholder);
                break;
            case VIEWTYPE_BOTTOM:
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.foot_search_clear, null);
                break;
            case VIEWTYPE_EMPTY:
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.search_empty, null);
                break;
        }
        return view;
    }

    private void findView(ViewHolder holder, View view) {
        holder.searchtxt = (TextView) view.findViewById(R.id.searchtxt);

        holder.bottom = (ImageView) view.findViewById(R.id.bottom);
        holder.middle = view.findViewById(R.id.middle);
    }

    private class OnSearchClickListener implements OnClickListener {

        private int position;

        public OnSearchClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ActivitySearchResult.class);
            intent.putExtra("keytype","8");
            intent.putExtra("name", historys == null || historys.size() == 0 || historys.size() <= position ? "" : historys.get(position));
            mContext.startActivity(intent);
        }
    }

    private static class ViewHolder {
        TextView searchtxt;

        View middle;
        ImageView bottom;
    }
}
