package com.eryue.friends;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author
 * Created by Administrator on 2018/8/11.
 */

public class MyPagerAdapter  extends FragmentPagerAdapter {


    /**
     *     创建FragmentManager
     */
    private FragmentManager fragmetnmanager;
    /**
     *     创建一个List<Fragment>
     */
    private List<Fragment> listfragment;

    /**
     *     定义构造带两个参数
     */
    public MyPagerAdapter(FragmentManager fm,List<Fragment> list) {
        super(fm);
        this.fragmetnmanager=fm;
        this.listfragment=list;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        //返回第几个fragment
        return listfragment.get(arg0);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //总共有多少个fragment
        return listfragment.size();
    }


}