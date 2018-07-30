package com.eryue.home;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.eryue.R;
import com.library.ui.dragrefresh.DragRefreshListView;
import com.library.ui.dragrefresh.ListViewFooter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * Created by bli.Jason on 2017/4/11.
 */
public class GoodsGridView extends LinearLayout implements DragRefreshListView.DragRefreshListViewListener {

    private DragRefreshListView listview;

    private List<Goods> dataList;
    private GoodsGridAdapter adapter;

    public GoodsGridView(Context context) {
        super(context);
        init();
    }

    public GoodsGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.fragment_draggrid,this);
        listview = (DragRefreshListView) findViewById(R.id.listview_drag);
        listview.setDragRefreshListViewListener(this);
        listview.setFooterDividersEnabled(false);
//        listview.setFooterViewState(ListViewFooter.STATE_HIDE);
        listview.setFooterViewState(ListViewFooter.STATE_HIDE);
        listview.setAutoLoadMore(true);
        //headerview
//        final View headerView = LayoutInflater.from(this).inflate(R.layout.layout_header_adv,null);
//        listview.getRefreshableView().addHeaderView(headerView);

        adapter=new GoodsGridAdapter(getContext());
        listview.setAdapter(adapter);


//        runOnUiThread(new Runnable(){
//            @Override
//            public void run() {
//                AdViewPager adViewPager = (AdViewPager) headerView.findViewById(R.id.viewpager_ad);
//                List<StockCommonEntity> imgList = new ArrayList<>();
//                StockCommonEntity entity = new StockCommonEntity();
//                entity.img = "bg_welcome";
//                imgList.add(entity);
//                imgList.add(entity);
//                imgList.add(entity);
//                imgList.add(entity);
//                adViewPager.setStockCommonList(imgList);
//                adViewPager.initData();
//            }
//        });



    }

    public void initData() {
        //图标
        //图标下的文字
//        String name[]={"时钟","信号","宝箱","秒钟","大象","FF","记事本","书签","印象","商店","主题","迅雷"};
//        dataList = new ArrayList<Goods>();
//        Goods goods;
//        for (int i = 0; i <name.length; i++) {
//            goods = new Goods();
//            goods.setName(name[i]);
//            dataList.add(goods);
//        }
//        Log.d("libo","class="+this);
//        adapter.setDataList(dataList);
//        adapter.notifyDataSetChanged();


    }

    @Override
    public void onRefresh() {
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                //模拟请求，舒眠2秒钟
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // 显示最后更新的时间
                listview.refreshComplete(true, new Date().getTime());

//                if (allDataList.isEmpty()) {
//                    //无数据
//                    listview.setFooterViewState(ListViewFooter.STATE_RESULT, NO_MSG, Color.GRAY);
//                } else {
//                    //无更多数据
//                    listview.setFooterViewState(ListViewFooter.STATE_RESULT, NO_MORE_MSG, Color.GRAY);
//                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onLoadMore() {

        Log.d("libo","onLoadMore");
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                //模拟请求，舒眠2秒钟
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // 显示最后更新的时间
                listview.refreshComplete(true, new Date().getTime());

//                if (allDataList.isEmpty()) {
//                    //无数据
//                    listview.setFooterViewState(ListViewFooter.STATE_RESULT, NO_MSG, Color.GRAY);
//                } else {
//                    //无更多数据
//                    listview.setFooterViewState(ListViewFooter.STATE_RESULT, NO_MORE_MSG, Color.GRAY);
//                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void dispose(){

    }
}
