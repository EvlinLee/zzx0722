package com.eryue.search;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.eryue.R;
import com.eryue.activity.BaseFragment;
import com.eryue.listener.TouchEventListener;
import com.eryue.widget.ScrollStockTabView;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;

/**
 * Created by enjoy on 2018/2/13.
 */

public class GoodsSearchActivity extends BaseActivity implements View.OnClickListener,TouchEventListener{


    /**
     * 返回
     */
    private ImageView iv_back;
    private String[] titles = {"好券搜索","指定搜索"};
    /**
     *
     */
    private ScrollStockTabView tab_goodssearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_goodssearch);
        hideNavigationBar(true);

        mFragMgr = getSupportFragmentManager();
        initView();
        initData();
    }

    private void initView(){
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tab_goodssearch = (ScrollStockTabView) findViewById(R.id.tab_goodssearch);

        tab_goodssearch.setTouchListener(this);
        iv_back.setOnClickListener(this);

    }

    private void initData(){

        List<String> dataList = new ArrayList<>();
        for (int i=0;i<titles.length;i++){
            dataList.add(titles[i]);
        }
        tab_goodssearch.addItem(dataList);
        tab_goodssearch.setWrapContent(true);

        tab_goodssearch.setIndex(0);

    }


    @Override
    public void onClick(View v) {
        if (v == iv_back){
            this.finish();
        }
    }

    GoodPaperSearchFragment paperSearchFragment;
    GoodsKeywordFragent goodsKeywordFragent;

    @Override
    public void touchEvent(View sender, MotionEvent event) {

        int selectIndex = tab_goodssearch.getSelectedIndex();

        if (selectIndex == 0){

            if (null==paperSearchFragment){
                paperSearchFragment = new GoodPaperSearchFragment();
            }else{
                paperSearchFragment.refreshHistory();
            }
            showFragment(paperSearchFragment);


        }else if (selectIndex == 1){

            if (null==goodsKeywordFragent){
                goodsKeywordFragent = new GoodsKeywordFragent();
            }
            showFragment(goodsKeywordFragent);

        }

    }


}
