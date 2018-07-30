package com.eryue.goodsdetail;

import android.os.Bundle;

import com.directionalviewpager.DirectionalViewPager;
import com.eryue.R;
import com.eryue.activity.BaseFragment;

import net.InterfaceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enjoy on 2018/2/11.
 */

public class GoodsAbstractFragment extends BaseFragment{

    private DirectionalViewPager pager_goodsabstract;

    private GoodsDetailPagerAdapter pagerAdapter;

    private GoodsAbstractTopFragment topFragment;
    private GoodsDetailImageFragment imageFragment;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_goodsdetailtop);
        init();
        initData();
    }

    private void init(){
        pager_goodsabstract = (DirectionalViewPager) getView().findViewById(R.id.pager_goodsabstract);
        pager_goodsabstract.setOrientation(DirectionalViewPager.VERTICAL);
        pagerAdapter = new GoodsDetailPagerAdapter(getChildFragmentManager());
        pager_goodsabstract.setAdapter(pagerAdapter);
    }

    private void initData(){
        topFragment = new GoodsAbstractTopFragment();
        imageFragment = new GoodsDetailImageFragment();
        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(topFragment);
        fragmentList.add(imageFragment);
        pagerAdapter.setFragmentList(fragmentList);
        pagerAdapter.notifyDataSetChanged();
    }

    public void refreshData(InterfaceManager.SearchProductDetailInfoEx detailInfo){

        if (null!=topFragment){
            topFragment.refreshData(detailInfo);
        }
    }
}
