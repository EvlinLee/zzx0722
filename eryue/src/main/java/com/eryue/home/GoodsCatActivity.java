package com.eryue.home;

import android.os.Bundle;

import com.eryue.util.StatusBarCompat;

import base.BaseActivity;

/**
 * Created by enjoy on 2018/5/13.
 */

public class GoodsCatActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(this,0xFFFF1A40);
        super.onCreate(savedInstanceState);

        setTitle("分类");
        showFragment(new GoodsCatFragment());
    }
}
