package com.eryue.search;

import android.os.Bundle;

import com.eryue.R;

import base.BaseActivity;

/**
 * Created by enjoy on 2018/5/13.
 */

public class GoodsSearchActivityEx extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideNavigationBar(true);

        setTitle("超级搜索");

        GoodsSuperSearchFragment goodsSuperSearchFragment = new GoodsSuperSearchFragment();
        //是否显示返回按钮
        Bundle bundle = new Bundle();
        bundle.putBoolean("showBack",true);
        goodsSuperSearchFragment.setArguments(bundle);
        showFragment(goodsSuperSearchFragment);

    }
}
