package com.eryue.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bli.Jason on 2018/2/8.
 */

public class GoodsPagerAdapter extends FragmentStatePagerAdapter {
    private List<GoodsTabModel> dataList;
    private int childCount = 3;
    public GoodsFragment goodsFragment;
    private FragmentManager fm;

    public GoodsPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }


    public void setDataList(List<GoodsTabModel> dataList) {
        this.dataList = dataList;

    }


    @Override
    public Fragment getItem(int position) {
        GoodsFragment goodsFragment = GoodsFragment.newInstance(dataList.get(position));
        if (position == 0){
//            goodsFragment.startRequest();
        }
        return goodsFragment;
    }

    private GoodsFragment currentFragment;

    public GoodsFragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        this.currentFragment = (GoodsFragment) object;
    }


    @Override
    public int getCount() {
        if (null!=dataList){
            return dataList.size();
        }
        return 0;
    }
}
