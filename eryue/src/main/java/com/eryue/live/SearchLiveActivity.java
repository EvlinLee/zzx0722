package com.eryue.live;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.eryue.util.StatusBarCompat;

import base.BaseActivity;

/**
 * 今日爆款、上百券
 * Created by enjoy on 2018/5/14.
 */

public class SearchLiveActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(this,0xFFFF1A40);
        super.onCreate(savedInstanceState);
        hideNavigationBar(true);
        String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title)){

            title = "实时播";
        }
        //渠道type
        String type = getIntent().getStringExtra("type");
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("type",type);

        SearchLiveFragmentEx searchLiveFragment = SearchLiveFragmentEx.newInstatnce();
        searchLiveFragment.setArguments(bundle);

        showFragment(searchLiveFragment);
    }


}
