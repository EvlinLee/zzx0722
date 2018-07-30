package com.eryue.goodsdetail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.eryue.activity.BaseFragment;

import java.util.List;

/**
 * Created by enjoy on 2018/2/11.
 */

public class GoodsDetailPagerAdapter extends FragmentPagerAdapter{

    private List<BaseFragment> fragmentList;

    public GoodsDetailPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public List<BaseFragment> getFragmentList() {
        return fragmentList;
    }

    public void setFragmentList(List<BaseFragment> fragmentList) {
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        if (null!=fragmentList&&position>=0&&position<fragmentList.size()){
            return fragmentList.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        if (null!=fragmentList){
            return fragmentList.size();
        }
        return 0;
    }
}
