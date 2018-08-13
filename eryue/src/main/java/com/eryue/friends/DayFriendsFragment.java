package com.eryue.friends;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eryue.R;
import com.eryue.activity.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/11.
 */

public class DayFriendsFragment  extends BaseFragment implements View.OnClickListener{
    /**
     *     UI Object
     */
    private TextView tv_recommend;
    private TextView tv_material;
    private TextView tv_newcomers;

    private ViewPager viewpager;


    /**
     * Fragment Object
     */
    private DayFriendsMaterialFragment fgMaterial;
    private DayFriendsNewcomersFragment fgNercomer;
    private DayFriendsRecommendFragment fgRrecommend;
    private android.support.v4.app.FragmentManager fManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setContentView(R.layout.fragment_dayfriends_vierpager);

            View mStateBarFixer =getView().findViewById(R.id.status_bar_fix_view);
            mStateBarFixer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(getActivity())));
            mStateBarFixer.setBackgroundColor(getResources().getColor(R.color.gray));

            setVp();
            initView();

    }

    public static int getStatusBarHeight(Activity a) {
        int result = 0;
        int resourceId = a.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = a.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     *     UI组件初始化与事件绑定

     */
    private void initView() {
        tv_recommend = (TextView) getView().findViewById(R.id.tv_recommend);
        tv_material = (TextView) getView().findViewById(R.id.tv_material);
        tv_newcomers = (TextView) getView().findViewById(R.id.tv_newcomers);

        tv_newcomers.setOnClickListener(this);
        tv_recommend.setOnClickListener(this);
        tv_material.setOnClickListener(this);
    }

    private void setVp() {

        viewpager=getView().findViewById(R.id.vp);
        //获取ViewPager
        viewpager=(ViewPager) getView().findViewById(R.id.vp);
        //new一个List<Fragment>
        List listfragment=new ArrayList<Fragment>();

        //添加三个fragment到集合
        DayFriendsMaterialFragment f1 = new DayFriendsMaterialFragment();
        DayFriendsNewcomersFragment f2 = new DayFriendsNewcomersFragment();
        DayFriendsRecommendFragment f3 = new DayFriendsRecommendFragment();
        listfragment.add(f1);
        listfragment.add(f2);
        listfragment.add(f3);

        FragmentManager fm=getFragmentManager();

        //new myFragmentPagerAdater记得带上两个参数
        MyPagerAdapter mfpa=new MyPagerAdapter(fm, listfragment);

        viewpager.setAdapter(mfpa);

        //设置当前页是第一页
        viewpager.setCurrentItem(0);

        //添加viewpager的滑动事件
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if (arg0 == 0) {
                    setSelected();
                    tv_recommend.setSelected(true);
                    tv_recommend.setTextColor(Color.parseColor("#333333"));
                    tv_recommend.setTextSize(17);
                } else if (arg0 == 1) {
                    setSelected();
                    tv_material.setSelected(true);
                    tv_material.setTextColor(Color.parseColor("#333333"));
                    tv_material.setTextSize(17);
                    viewpager.setCurrentItem(1);
                } else if (arg0 == 2) {
                    setSelected();
                    tv_newcomers.setSelected(true);
                    tv_newcomers.setTextColor(Color.parseColor("#333333"));
                    tv_newcomers.setTextSize(17);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_recommend:
                setSelected();
                tv_recommend.setSelected(true);
                tv_recommend.setTextColor(Color.parseColor("#333333"));
                tv_recommend.setTextSize(17);
                viewpager.setCurrentItem(0);
                break;
            case R.id.tv_material:
                setSelected();
                tv_material.setSelected(true);
                tv_material.setTextColor(Color.parseColor("#333333"));
                tv_material.setTextSize(17);
                viewpager.setCurrentItem(1);
                break;
            case R.id.tv_newcomers:
                setSelected();
                tv_newcomers.setSelected(true);
                tv_newcomers.setTextColor(Color.parseColor("#333333"));
                tv_newcomers.setTextSize(17);
                viewpager.setCurrentItem(2);
                break;
            default:
        }
    }

    /**
     *     重置所有文本的选中状态

     */
    private void setSelected(){
        tv_recommend.setSelected(false);
        tv_material.setSelected(false);
        tv_newcomers.setSelected(false);
        tv_recommend.setTextSize(14);
        tv_material.setTextSize(14);
        tv_newcomers.setTextSize(14);
        tv_recommend.setTextColor(Color.parseColor("#999999"));
        tv_material.setTextColor(Color.parseColor("#999999"));
        tv_newcomers.setTextColor(Color.parseColor("#999999"));
    }
}
