package com.eryue.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.eryue.R;
import com.eryue.chartutils.ChartUtils;
import com.eryue.util.StatusBarCompat;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;

/**
 * 主页
 * Created by Administrator on 2018/8/9.
 *
 */

public class MyOrderChartsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvDate;
    private LineChart chart;
    private ImageView navigation_back;

    private TextView check_detail;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorder_charts_line_activity);

        initView();
    }

    private void initView() {
        tvDate = (TextView) findViewById(R.id.tv_date);
        chart = (LineChart) findViewById(R.id.chart);
        check_detail = (TextView) findViewById(R.id.check_detail);
        check_detail.setOnClickListener(this);

        navigation_back = (ImageView) findViewById(R.id.navigation_back);
        navigation_back.setOnClickListener(this);

        tvDate.setOnClickListener(this);

        ChartUtils.initChart(chart);
        ChartUtils.notifyDataSetChanged(chart, getData(), ChartUtils.dayValue);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date:
                //打开订单详情页
                intent = new Intent(MyOrderChartsActivity.this, OrderDetailActivity1.class);
                startActivity(intent);
                break;
            case R.id.check_detail:
                //打开订单详情页
                intent = new Intent(MyOrderChartsActivity.this, OrderDetailActivity1.class);
                startActivity(intent);
                break;
            case R.id.navigation_back:
                finish();
                break;
            default:
                break;
        }
    }

    private List<Entry> getData() {
        List<Entry> values = new ArrayList<>();
        values.add(new Entry(0, 50));
        values.add(new Entry(1, 55));
        values.add(new Entry(2, 41));
        values.add(new Entry(3, 20));
        values.add(new Entry(4, 25));
        values.add(new Entry(5, 20));
        values.add(new Entry(6, 20));
        return values;
    }
}
