package com.eryue.search;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eryue.GoodsListFragment;
import com.eryue.R;
import com.eryue.activity.BaseFragment;

import base.BaseActivity;

/**
 * Created by enjoy on 2018/2/16.
 */

public class GoodsSearchResultActivity extends BaseActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView(){

        GoodsSearchResultFragment searchResultFragment = GoodsSearchResultFragment.newInstance();
        showFragment(searchResultFragment);
    }

    private void initData(){
        String title = getIntent().getStringExtra("title");
        String cat = getIntent().getStringExtra("cat");
        if (!TextUtils.isEmpty(title)){
            navigationBar.getTitleView().setMaxLines(1);
            setTitle(title);
        }else{
            setTitle(cat);
        }

    }


}
