package com.hemaapp.tyyjsc.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.hemaapp.tyyjsc.fragments.FragmentOrderList;
import java.util.ArrayList;


/**
 * 订单适配器
 */
public class OrderViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<FragmentOrderList> frags = null;
    private FragmentManager fm = null;

    public OrderViewPagerAdapter(FragmentManager fm,
                                 ArrayList<FragmentOrderList> frags) {
        super(fm);
        this.fm = fm;
        this.frags = frags;
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return frags.size();
    }

    @Override
    public Fragment getItem(int position) {
        return frags.get(position % frags.size());
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((FragmentOrderList) object).getView();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}
