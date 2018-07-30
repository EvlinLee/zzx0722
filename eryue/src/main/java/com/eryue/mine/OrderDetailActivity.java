package com.eryue.mine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eryue.R;
import com.eryue.activity.BaseFragment;

import java.util.ArrayList;

import base.BaseActivity;

/**
 * 订单明细
 */

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {


    private int reqType; // 1=今日 2=昨日 3=近7日 4=本月 5=上月
    private int pageType = 0;// 1=我的订单  2=团队贡献订单


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        reqType = intent.getIntExtra("type", -1);
        if (reqType == -1) {
            return;
        }


        if (reqType == 1) {
            navigationBar.setTitle("今日订单明细");
        } else if (reqType == 2) {
            navigationBar.setTitle("昨日订单明细");
        } else if (reqType == 3) {
            navigationBar.setTitle("近7日订单明细");
        } else if (reqType == 4) {
            navigationBar.setTitle("本月订单明细");
        } else if (reqType == 5) {
            navigationBar.setTitle("上月订单明细");
        }

        setupViews();
        int selfOrProxy = intent.getIntExtra("selfOrProxy", 1);
        changePage(selfOrProxy);
    }

    private void setupViews() {
        findViewById(R.id.myorder_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePage(1);
            }
        });
        findViewById(R.id.teamorder_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePage(2);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.equals(findViewById(R.id.myorder_title))) {
            changePage(1);
        } else if (v.equals(findViewById(R.id.teamorder_title))) {
            changePage(2);
        }
    }


    OrderDetailMyFragment orderDetailMyFragment;
    OrderDetailTeamFragment orderDetailTeamFragment;
    private void changePage(int pageType) {
        if (this.pageType == pageType) {
            return;
        }

        this.pageType = pageType;
        int color = Color.parseColor("#ff4c4c");
        int color1 = Color.parseColor("#333333");

        if (pageType == 1) {
            TextView tv = (TextView) findViewById(R.id.myorder_title);
            tv.setTextColor(color);

            tv = (TextView) findViewById(R.id.teamorder_title);
            tv.setTextColor(color1);

            if (orderDetailMyFragment == null) {
                orderDetailMyFragment = new OrderDetailMyFragment();
                orderDetailMyFragment.setReqType(reqType);
            }
            showFragment(orderDetailMyFragment);
        }  else if (pageType == 2) {
            TextView tv = (TextView) findViewById(R.id.myorder_title);
            tv.setTextColor(color1);

            tv = (TextView) findViewById(R.id.teamorder_title);
            tv.setTextColor(color);

            if (orderDetailTeamFragment == null) {
                orderDetailTeamFragment = new OrderDetailTeamFragment();
                orderDetailTeamFragment.setReqType(reqType);
            }
            showFragment(orderDetailTeamFragment);
        }
    }

    protected void showFragment(BaseFragment fragment) {
        if (getFragment() != fragment&&null!=fragment) {
            FragmentTransaction transaction = mFragMgr.beginTransaction();
            if (null!=getFragment()){
                transaction.hide(getFragment());
            }
            setFragment(fragment);
            if (!fragment.isAdded()) {
                transaction.add(R.id.main_body1, fragment).show(fragment).commit();
            } else {
                transaction.show(fragment).commit();
            }
        }
    }

    private BaseFragment fragment;

    public BaseFragment getFragment() {
        return fragment;
    }

    public void setFragment(BaseFragment f) {
        this.fragment = f;
    }


}
