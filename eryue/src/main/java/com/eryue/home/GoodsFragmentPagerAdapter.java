package com.eryue.home;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.eryue.activity.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bli.Jason on 2018/2/8.
 */

public class GoodsFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<GoodsTabModel> dataList;
    private int childCount = 3;
    public Fragment currentFragment;
    private FragmentManager fm;

    public GoodsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }


    public void setDataList(List<GoodsTabModel> dataList) {
        this.dataList = dataList;

    }

    @Override
    public Fragment getItem(int position) {
        Log.d("libo", "position=" + position);
        Fragment fragment;
        if (position == 0) {
            GoodsAllFragment allFragment = GoodsAllFragment.newInstance();
//            allFragment.startRequest();
            fragment = allFragment;
        } else {
            fragment = GoodsFragment.newInstance(dataList.get(position));
        }
        return fragment;
    }

    @Override
    public int getCount() {
        if (null != dataList) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        this.currentFragment = (Fragment) object;
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}
