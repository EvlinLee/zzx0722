package com.eryue.mine;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.eryue.GoodsContants;
import com.eryue.R;
import com.eryue.home.GoodsListAdapter;


import net.DataCenterManager;
import net.InterfaceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import base.BaseActivity;


/**
 * @author zxc
 * Created by Administrator on 2018/8/13.
 */

public class MyFavorite extends BaseActivity implements AbsListView.OnScrollListener,View.OnClickListener {
    private ListView listView;
    private MyFavoriteListAdapter adapter;
    private List<InterfaceManager.SearchProductInfoEx> allDataList = new ArrayList<>();

    /**
     * 全选按钮
     *
     * @param savedInstanceState
     */
    private LinearLayout select_all;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfavorite);
        navigationBar.setTitle("我的收藏");

        setupViews();


        allDataList = DataCenterManager.Instance().getProductInfoList();
        Collections.reverse(allDataList);
        Log.d("","");

        adapter.setDataList(allDataList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v == select_all) {
            adapter.setAllSelect(true);
            adapter.notifyDataSetChanged();
        }
    }

    private void setupViews() {
        select_all = findViewById(R.id.select_all);
        select_all.setOnClickListener(this);

        listView = findViewById(R.id.listview);
        adapter = new MyFavoriteListAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnScrollListener(this);


        iv_rocket = findViewById(R.id.iv_rocket);
        iv_rocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击火箭滑动到顶部
                if (null != listView) {
                    listView.setSelection(0);
                }
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }


    int preItem = 0;
    boolean isRocketShow = false;
    private ImageView iv_rocket;
    @Override
    public void onScroll(AbsListView absListView, int firstItem, int i1, int i2) {
//商品详情，只要滑动超过头部就显示火箭
        int top = absListView.getTop();
        if (preItem!=firstItem){

            Log.d("libo","onScroll-----firstItem="+firstItem);
            preItem = firstItem;
            if (firstItem> GoodsContants.SHOWROCKET_NUM) {
                if (!isRocketShow && null != iv_rocket) {
                    iv_rocket.setVisibility(View.VISIBLE);
                    isRocketShow = true;
                }
            } else {

                if (isRocketShow && null != iv_rocket) {
                    iv_rocket.setVisibility(View.GONE);
                    isRocketShow = false;
                }

            }
        }


    }
}