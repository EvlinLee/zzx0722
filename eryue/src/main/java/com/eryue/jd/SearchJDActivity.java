package com.eryue.jd;

import android.os.Bundle;
import android.text.TextUtils;

import com.eryue.live.SearchLiveFragment;

import base.BaseActivity;

/**
 * 京东免单
 * Created by enjoy on 2018/5/14.
 */

public class SearchJDActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNavigationBar(true);
        String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title)){
            title = "京东免单";
        }
        //渠道type
        String type = getIntent().getStringExtra("type");
        Bundle bundle = new Bundle();
        bundle.putString("title",title);

        SearchJDFragment searchJDFragment = SearchJDFragment.newInstatnce();
        searchJDFragment.setArguments(bundle);

        showFragment(searchJDFragment);
    }


}
