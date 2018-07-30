package com.eryue.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import base.BaseActivity;
import com.eryue.R;

/**
 * 合伙人收益 页面
 */

public class HeHuoRenActivity extends BaseActivity implements View.OnClickListener{

    private HeHuoRenView2 today;
    private HeHuoRenView2 yestoday;
    private HeHuoRenView2 last7days;
    private HeHuoRenView2 curr_month;
    private HeHuoRenView2 last_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationBar.setTitle("合伙人收益报表");
        setContentView(R.layout.activity_hehuoren);

        setupViews();
        reqData();
    }

    private void setupViews() {
        findViewById(R.id.myteam).setOnClickListener(this);
        findViewById(R.id.mylive).setOnClickListener(this);
        findViewById(R.id.jihuoliang).setOnClickListener(this);
        findViewById(R.id.search).setOnClickListener(this);

        today = (HeHuoRenView2) findViewById(R.id.today);
        yestoday = (HeHuoRenView2) findViewById(R.id.yestoday);
        yestoday.setTitle("昨日统计");
        last7days = (HeHuoRenView2) findViewById(R.id.last7days);
        last7days.setUpdate(View.VISIBLE);
        last7days.setTitle("近7日统计");
        curr_month = (HeHuoRenView2) findViewById(R.id.curr_month);
        curr_month.setUpdate(View.VISIBLE);
        curr_month.setTitle("本月统计");
        last_month = (HeHuoRenView2) findViewById(R.id.last_month);
        last_month.setUpdate(View.VISIBLE);
        last_month.setTitle("上月统计");
    }

    private void reqData() {
        showProgressMum();
        today.reqData(1);
        yestoday.reqData(2);
        last7days.reqData(3);
        curr_month.reqData(4);
        last_month.reqData(5);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(findViewById(R.id.myteam))) {
            Intent intent = new Intent(this, MyTeamActivity.class);
            startActivity(intent);
        } else if (v.equals(findViewById(R.id.jihuoliang))) {
            Intent intent = new Intent(this, AppActivatorActivity.class);
            startActivity(intent);
        }  else if (v.equals(findViewById(R.id.search))) {
            Intent intent = new Intent(this, SearchOrderActivity.class);
            startActivity(intent);
        }  else if (v.equals(findViewById(R.id.mylive))) {
            Intent intent = new Intent(this, MyLiveActivity.class);
            startActivity(intent);
        }
    }
}
