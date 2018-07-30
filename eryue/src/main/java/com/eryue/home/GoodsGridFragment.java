package com.eryue.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eryue.R;
import com.eryue.activity.BaseFragment;
import com.library.ui.dragrefresh.DragRefreshListView;
import com.library.ui.dragrefresh.ListViewFooter;
import com.library.util.UIScreen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * Created by bli.Jason on 2017/4/11.
 */
public class GoodsGridFragment extends BaseFragment implements DragRefreshListView.DragRefreshListViewListener {

    private DragRefreshListView listview;

    private List<Goods> dataList;
    private GoodsGridAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UIScreen.resetMainScreen(getActivity());
        return View.inflate(getActivity(), R.layout.fragment_draggrid, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listview = (DragRefreshListView) getView().findViewById(R.id.listview_drag);
        listview.setDragRefreshListViewListener(this);
        listview.setFooterDividersEnabled(false);
//        listview.setFooterViewState(ListViewFooter.STATE_HIDE);
        listview.setFooterViewState(ListViewFooter.STATE_HIDE);
        listview.setAutoLoadMore(true);
        //headerview
//        final View headerView = LayoutInflater.from(this).inflate(R.layout.layout_header_adv,null);
//        listview.getRefreshableView().addHeaderView(headerView);

        adapter=new GoodsGridAdapter(getActivity());
        listview.setAdapter(adapter);

        //初始化数据
        initData();

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

    private void initData() {
        //图标
//        int icno[] = { R.drawable.bottom_tab01, R.drawable.bottom_tab02, R.drawable.bottom_tab03,
//                R.drawable.bottom_tab05, R.drawable.bottom_tab05, R.drawable.bottom_tab01, R.drawable.bottom_tab02, R.drawable.bottom_tab03,
//                R.drawable.bottom_tab05, R.drawable.bottom_tab05, R.drawable.bottom_tab01, R.drawable.bottom_tab02};
//        //图标下的文字
//        String name[]={"时钟","信号","宝箱","秒钟","大象","FF","记事本","书签","印象","商店","主题","迅雷"};
//        dataList = new ArrayList<Goods>();
//        Goods goods;
//        for (int i = 0; i <name.length; i++) {
//            goods = new Goods();
//            goods.setName(name[i]);
//            dataList.add(goods);
//        }
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
                showProgressMum();
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

                hideProgressMum();
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
}
