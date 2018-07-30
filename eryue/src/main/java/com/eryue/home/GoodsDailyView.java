package com.eryue.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.eryue.R;
import com.eryue.goodsdetail.GoodsDetailActivityEx;

import net.InterfaceManager;

import java.util.List;

/**
 * Created by enjoy on 2018/2/21.
 */

public class GoodsDailyView extends LinearLayout{

    private GridView gridview_daily;
    private  GoodsDailyAdapter adapter;

    public GoodsDailyView(Context context) {
        super(context);
        init();
    }

    public GoodsDailyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_goodsdaily,this);
        gridview_daily = (GridView) findViewById(R.id.gridview_daily);
        adapter = new GoodsDailyAdapter(getContext());
        gridview_daily.setAdapter(adapter);

        gridview_daily.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<InterfaceManager.SearchProductInfo> dailyList = adapter.getDataList();
                InterfaceManager.SearchProductInfo searchProductInfo = dailyList.get(position);
                Intent intent = new Intent(getContext(), GoodsDetailActivityEx.class);
                intent.putExtra("itemId",searchProductInfo.itemId);
                intent.putExtra("searchFlag", searchProductInfo.searchFlag);
                getContext().startActivity(intent);
            }
        });

    }

    public void setData(List<InterfaceManager.SearchProductInfo> dailyList){

        adapter.setDataList(dailyList);
        adapter.notifyDataSetChanged();
    }
}
