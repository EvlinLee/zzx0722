package com.eryue;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eryue.activity.BaseFragment;
import com.eryue.home.GoodsFragment;
import com.eryue.home.GoodsFragmentPagerAdapter;
import com.eryue.home.GoodsPagerAdapter;
import com.eryue.home.GoodsTabModel;
import com.eryue.listener.TouchEventListener;
import com.eryue.widget.ScrollStockTabView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enjoy on 2018/2/16.
 */

public class GoodsListFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener, TouchEventListener {


    private LinearLayout layout_navigation;
    private TextView tv_navigation;
    /**
     * tab选项
     */
    private ScrollStockTabView tab_searchlist;

    private List<GoodsListTabModel> tabModelList;

    private String[] titles = {"综合", "券后价", "销量", "超优惠"};

    /**
     * 商品列表
     */
    private ViewPager viewpager_goodslist;
    private GoodsPagerAdapter adapter;

    private boolean isVideo;


    public static GoodsListFragment newInstance(boolean isVideo) {
        GoodsListFragment fragment = new GoodsListFragment();
        fragment.isVideo = isVideo;
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_goodslist);
        initView();
        initData();
    }

    private void initView() {
        layout_navigation = (LinearLayout) getView().findViewById(R.id.layout_navigation);
        tv_navigation = (TextView) getView().findViewById(R.id.tv_navigation);
        tab_searchlist = (ScrollStockTabView) getView().findViewById(R.id.tab_searchlist);
        tab_searchlist.setWrapContent(false);
        tab_searchlist.setTouchListener(this);

        viewpager_goodslist = (ViewPager) getView().findViewById(R.id.viewpager_goodslist);
        adapter = new GoodsPagerAdapter(getChildFragmentManager());
        viewpager_goodslist.setAdapter(adapter);
        viewpager_goodslist.addOnPageChangeListener(this);

    }

    private void initData() {
        String fragmentTitle = getArguments().getString("fragment_title");
        if (!TextUtils.isEmpty(fragmentTitle)) {
            layout_navigation.setVisibility(View.VISIBLE);
            tv_navigation.setText(fragmentTitle);
        }


        tabModelList = getArguments().getParcelableArrayList("tab");

        String type = getActivity().getIntent().getStringExtra("type");

        //如果没有传tab进入就直接写入
        if (null == tabModelList || tabModelList.isEmpty()) {
            if (!TextUtils.isEmpty(type) && type.equals("指定搜索")) {
                tabModelList = new ArrayList<>();
                //默认不传 券后价=afterQuan   只看有券:0=全部  1=只看有券 只看天猫:0=全部  1=天猫
                String[] resultTab = {"默认", "券后价", "只看有券", "只看天猫"};
                String[] tabValues = {"", "afterQuan", "", ""};
                GoodsListTabModel tabModel;
                for (int i = 0; i < resultTab.length; i++) {
                    tabModel = new GoodsListTabModel(resultTab[i], tabValues[i]);
                    tabModelList.add(tabModel);
                }
            } else {
                tabModelList = new ArrayList<>();
                String[] resultTab = {"综合", "券后价", "销量", "超优惠"};
                String[] tabValues = {"updateTime", "afterQuan", "soldQuantity", "quanPrice"};
                GoodsListTabModel tabModel;
                for (int i = 0; i < resultTab.length; i++) {
                    tabModel = new GoodsListTabModel(resultTab[i], tabValues[i]);
                    tabModelList.add(tabModel);
                }
            }

        }

        List<String> tabList = new ArrayList<>();
        for (int i = 0; i < tabModelList.size(); i++) {
            tabList.add(tabModelList.get(i).getName());
        }
        tab_searchlist.addItem(tabList);

        List<GoodsTabModel> goodsTabModelList = new ArrayList<>();
        GoodsTabModel tabModel;
        String title = getActivity().getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title) && title.equals("收藏夹")) {
            tab_searchlist.setVisibility(View.GONE);
            tabModel = new GoodsTabModel();
            tabModel.setName("收藏夹");
            goodsTabModelList.add(tabModel);
        } else {

            //初始化每个tab
            for (int i = 0; i < tabModelList.size(); i++) {
                tabModel = new GoodsTabModel();
                if (!TextUtils.isEmpty(title)) {
                    tabModel.setName(title);

                } else if (!TextUtils.isEmpty(fragmentTitle)) {
                    tabModel.setName(fragmentTitle);
                }
                tabModel.setSidx(tabModelList.get(i).getKey());

                if (!TextUtils.isEmpty(type) && type.equals("指定搜索")) {
                    tabModel.setType(type);
                }

                if (tabModelList.get(i).getName().equals("只看有券")) {
                    //只看有券
                    tabModel.setIsQuan(1);
                }
                if (tabModelList.get(i).getName().equals("只看天猫")) {
                    //只看天猫
                    tabModel.setIsMall(1);
                }

                goodsTabModelList.add(tabModel);
            }
        }


        //ViewPager的适配器
        adapter.setDataList(goodsTabModelList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 上一次选中的tab
     */
    private int lastPosition = -1;

    private void setTabDataState(int position) {
        if (lastPosition == position) {
            return;
        }
        lastPosition = position;
        if (null != tab_searchlist) {
            tab_searchlist.setIndex(position);
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        setTabDataState(position);
        GoodsFragment goodsFragment = adapter.getCurrentFragment();
        if (null != goodsFragment) {
//            goodsFragment.startRequest();
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void touchEvent(View sender, MotionEvent event) {
        final int selectIndex = tab_searchlist.getSelectedIndex();
        viewpager_goodslist.setCurrentItem(selectIndex);
//        GoodsFragment goodsFragment = adapter.getItemFragment(selectIndex);
//        if (null!=goodsFragment){
//            goodsFragment.startRequest();
//        }

    }

    public void refreshFragment() {
        GoodsFragment goodsFragment = adapter.getCurrentFragment();
        if (null != goodsFragment) {
            goodsFragment.startRequest();
        }
    }
}
