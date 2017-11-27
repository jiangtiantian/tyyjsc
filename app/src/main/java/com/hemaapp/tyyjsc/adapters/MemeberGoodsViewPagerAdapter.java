package com.hemaapp.tyyjsc.adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hemaapp.tyyjsc.R;
import com.hemaapp.tyyjsc.model.GoodsBriefIntroduction;
import com.hemaapp.tyyjsc.model.MembergoodsList;
import com.hemaapp.tyyjsc.view.CityGridView;

import java.util.ArrayList;


/**
 * 會員專區猜你喜欢ViewPager适配器
 */
public class MemeberGoodsViewPagerAdapter extends PagerAdapter {

    protected Context mContext;
    private View view;
    private RadioGroup mIndicator;
    private ArrayList<MembergoodsList> infors;

    private int size;

    public void setInfors(ArrayList<MembergoodsList> infors) {
        this.infors = infors;
    }

    public MemeberGoodsViewPagerAdapter(Context mContext, ArrayList<MembergoodsList> infors,
                                        View view) {
        this.mContext = mContext;
        this.infors = infors;
        this.view = view;
        init();
    }

    private void init() {
        size = ((BitmapDrawable) this.mContext.getResources().getDrawable(
                R.mipmap.dot_no)).getBitmap().getWidth();
        mIndicator = (RadioGroup) view.findViewById(R.id.radiogroup1);
        mIndicator.removeAllViews();
        if (getCount() > 1) {
            for (int i = 0; i < getCount(); i++) {
                RadioButton button = new RadioButton(mContext);
                button.setButtonDrawable(R.drawable.indicator_show);
                button.setId(i);
                button.setClickable(false);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                        (i == getCount() - 1) ? size : size * 2, size);
                button.setLayoutParams(params2);
                if (i == 0)
                    button.setChecked(true);
                mIndicator.addView(button);
            }
        } else {
            mIndicator.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getCount() {
        int count = 0;
        if (infors == null || infors.size() == 0)
            count = 0;
        else {
            int a = infors.size() / 2;
            int b = infors.size() % 2;
            if (a == 0) {
                if (b > 0)
                    count = 1;
            } else {
                if (b == 0)
                    count = a;
                else {
                    count = a + 1;
                }
            }
        }
        return count;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    //获取每页展示的数据
    public ArrayList<MembergoodsList> getEachData(int position) {
        ArrayList<MembergoodsList> data = new ArrayList<>();
        if (getCount() == 1) {
            data.addAll(infors);
        } else {
            ArrayList<GoodsBriefIntroduction> t = new ArrayList<GoodsBriefIntroduction>();
            int min = position * 2;
            int max = position * 2 + 1;
            if (max < infors.size()) {
                for (int i = min; i <= max; i++) {
                    data.add(infors.get(i));
                }
            } else {
                for (int i = min; i < infors.size(); i++) {
                    data.add(infors.get(i));
                }
            }
        }
        return data;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View mView = null;
        ViewHolder holder = null;
        if (container.getChildAt(position) == null) {
            mView = LayoutInflater.from(mContext).inflate(
                    R.layout.viewpager_gridview1, null);
            holder = new ViewHolder(mView);
            MemebrGoodsGridViewAdapter adapter = new MemebrGoodsGridViewAdapter(mContext, getEachData(position));
            holder.gridView.setAdapter(adapter);
            mView.setTag(R.id.type, holder);
            container.addView(mView);
        } else {
            mView = container.getChildAt(position);
        }
        holder = (ViewHolder) mView.getTag(R.id.type);
        MemebrGoodsGridViewAdapter adapter = (MemebrGoodsGridViewAdapter) holder.gridView.getAdapter();
        if (adapter == null) {
            adapter = new MemebrGoodsGridViewAdapter(mContext, getEachData(position));
            holder.gridView.setAdapter(adapter);
        } else {
            adapter.setData(getEachData(position));
            adapter.notifyDataSetChanged();
        }
        return mView;
    }

    class ViewHolder {
        public CityGridView gridView = null;

        public ViewHolder(View convertView) {
            gridView = (CityGridView) convertView.findViewById(R.id.gridview);
        }
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public ViewGroup getIndicator() {
        return mIndicator;
    }

}