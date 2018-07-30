package com.eryue.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eryue.R;
import com.eryue.activity.BaseFragment;
import com.eryue.listener.TouchEventListener;
import com.eryue.search.GoodsSearchActivity;
import com.eryue.widget.ScrollStockTabView;
import com.library.util.UIScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bli.Jason on 2018/2/7.
 */

public class HomeFragment extends BaseFragment implements TouchEventListener, View.OnClickListener, GoodsTabPopView.OnPopListener,
        AdapterView.OnItemClickListener, ViewPager.OnPageChangeListener {

    /**
     * 搜索
     */
    private LinearLayout search_contain;

    private ViewPager viewPager;

    //    private GoodsViewpagerAdapter adapter;
    private GoodsFragmentPagerAdapter adapter;

    /**
     * 可滑动tab
     */
    private ScrollStockTabView tabbar_home;
    private String[] titles = {"全部", "女装", "男装", "母婴", "鞋包", "数码", "家装", "食品", "美妆", "其他"};
    private ImageView iv_more;
    private Animation rotateUp;
    private Animation rotateDown;
    /**
     * 弹出框
     */
    private GoodsTabPopView popView;

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_home);
        initView();
        initData();
    }

    private void initView() {
        search_contain = (LinearLayout) getView().findViewById(R.id.search_contain);
        search_contain.setOnClickListener(this);
        //实例化
        viewPager = (ViewPager) getView().findViewById(R.id.viewpager);
        tabbar_home = (ScrollStockTabView) getView().findViewById(R.id.tabbar_home);
        tabbar_home.setNeedArrow(false);
        adapter = new GoodsFragmentPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                UIScreen.contentHeight = viewPager.getHeight();
            }
        });

        rotateUp = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_rotate_up);//创建动画
        rotateUp.setInterpolator(new LinearInterpolator());//设置为线性旋转
        rotateUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        if (null != popView) {
                            popView.showPopView(iv_more);
                        }
                    }
                });

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rotateDown = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_rotate_down);//创建动画
        rotateDown.setInterpolator(new LinearInterpolator());//设置为线性旋转

        iv_more = (ImageView) getView().findViewById(R.id.iv_more);
        iv_more.setOnClickListener(this);
        iv_more.setTag(true);
        //为imageView设置点击事件，并添加如下代码
//        rotate.setFillAfter(!rotate.getFillAfter());//每次都取相反值，使得可以不恢复原位的旋转imageView.startAnimation(rotate);
        rotateUp.setFillAfter(true);
    }

    List<GoodsTabModel> tabList;

    public void initData() {

        //页面，数据源
        List<String> tabStrList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            tabStrList.add(titles[i]);
        }
        tabbar_home.addItem(tabStrList);

        tabbar_home.setIndex(0);
        tabbar_home.refreshDrawableState();
        tabbar_home.post(new Runnable() {
            @Override
            public void run() {
                tabbar_home.setIndex(0, false);
            }
        });
        tabbar_home.setTouchListener(this);


        tabList = new ArrayList<>();
        GoodsTabModel tabModel;
        for (int i = 0; i < titles.length; i++) {
            tabModel = new GoodsTabModel();
            if (i == 0) {
                tabModel.setSelectTag(GoodsTabModel.TAG_SELECT);
            }
            tabModel.setName(titles[i]);
            tabList.add(tabModel);
        }

        //ViewPager的适配器
        adapter.setDataList(tabList);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void touchEvent(View sender, MotionEvent event) {
        final int selectIndex = tabbar_home.getSelectedIndex();
        tabbar_home.post(new Runnable() {
            @Override
            public void run() {
                tabbar_home.setIndex(selectIndex, false);
            }
        });
        viewPager.setCurrentItem(selectIndex);

    }

    @Override
    public void onClick(View v) {
        if (v == search_contain) {
            startActivity(new Intent(getContext(), GoodsSearchActivity.class));
        } else if (v == iv_more) {
            //刷新popview数据
            if (null == popView) {
                //刷新popview数据
                popView = new GoodsTabPopView(getContext());
                popView.setOnPopListener(this);
                popView.setOnItemClickListener(this);


                popView.refreshListData(tabList);
            }
            showMoreAnim();
        }


    }

    @Override
    public void onDismiss() {
        showMoreAnim();
    }

    private void showMoreAnim() {
        boolean isUp = (boolean) iv_more.getTag();

        if (isUp) {
            iv_more.startAnimation(rotateUp);
        } else {
            iv_more.startAnimation(rotateDown);
        }
        isUp = !isUp;
        iv_more.setTag(isUp);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (null != popView) {
            popView.dimiss();
        }
        setTabDataState(position);

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
        if (null != tabbar_home && position >= 0 && position < titles.length) {

            if (null != tabList && position < tabList.size()) {
                for (int i = 0; i < tabList.size(); i++) {
                    if (i == position) {
                        tabList.get(i).setSelectTag(GoodsTabModel.TAG_SELECT);
                    } else {
                        tabList.get(i).setSelectTag(GoodsTabModel.TAG_UNSELECT);
                    }
                }
            }
            tabbar_home.setIndex(position);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("libo", "onPageSelected---position=" + position);
        setTabDataState(position);
        Fragment fragment = adapter.getCurrentFragment();
        if (null != fragment) {
            if (fragment instanceof GoodsAllFragment) {
//                ((GoodsAllFragment) fragment).startRequest();

            } else if (fragment instanceof GoodsFragment) {
//                ((GoodsFragment) fragment).startRequest();
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void refreshCurrentFragment(){
        Fragment fragment = adapter.getCurrentFragment();
        if (null != fragment) {
            if (fragment instanceof GoodsAllFragment) {
                ((GoodsAllFragment) fragment).startRequest();

            } else if (fragment instanceof GoodsFragment) {
                ((GoodsFragment) fragment).startRequest();
            }
        }
    }
}
