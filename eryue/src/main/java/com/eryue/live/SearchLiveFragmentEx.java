package com.eryue.live;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.GoodsContants;
import com.eryue.R;
import com.eryue.activity.BaseFragment;
import com.eryue.home.GoodsListAdapter;
import com.eryue.ui.UISortTabView;
import com.library.ui.dragrefresh.DragRefreshListView;
import com.library.ui.dragrefresh.ListViewFooter;

import net.InterfaceManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by enjoy on 2018/5/10.
 * 实时播
 */

public class SearchLiveFragmentEx extends BaseFragment implements SearchLivePresenter.SearchLiveListener {

    public static SearchLiveFragmentEx newInstatnce() {
        return new SearchLiveFragmentEx();
    }

    private RecyclerView recyclerView;

    private LiveGoodsRecylerAdapter adapter;

    private SearchLivePresenter presenter;

    private int pageNo = 1;
    private String sord = "desc";
    private String itemTitle = "";
    private String sidx = "updateTime";
    private String type = "";
    boolean isLastPage;
    private List<InterfaceManager.SearchProductInfoEx> allDataList = new ArrayList<>();


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setContentView(R.layout.fragment_searchliveex);


        presenter = new SearchLivePresenter();
        presenter.setSearchLiveListener(this);


        recyclerView = (RecyclerView) getView().findViewById(R.id.recycleview);

        adapter = new LiveGoodsRecylerAdapter(getContext());
        //线性布局
        /*LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);*/

        StaggeredGridLayoutManager recyclerViewLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setAdapter(adapter);


        sendRequest();

    }

    private void sendRequest() {
        presenter.requestSeachLive(pageNo, sord, itemTitle, sidx);
    }

    @Override
    public void onSearchLiveBack(final List<InterfaceManager.SearchProductInfoEx> result) {
        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                // 显示最后更新的时间
//                recyclerView.refreshComplete(true, new Date().getTime());
                if (result.isEmpty()) {
                    //无数据
                    isLastPage = true;
                } else {
                    if (pageNo == 1) {
                        /*if (null!=iv_rocket){
                            iv_rocket.setVisibility(View.GONE);
                        }*/
                        allDataList.clear();
                    }
                    allDataList.addAll(result);
                    if (null != adapter) {

                        adapter.setData(allDataList);
                        adapter.setLogin(AccountUtil.isLogin());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }).sendEmptyMessage(0);

    }

    @Override
    public void onSearchLiveError() {
        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
//                recyclerView.refreshComplete(true, new Date().getTime());
                if (pageNo == 1) {
                    /*if (null!=iv_rocket){
                        iv_rocket.setVisibility(View.GONE);
                    }*/
                    allDataList.clear();
                    if (null != adapter) {
                      /*  adapter.setDataList(allDataList);
                        adapter.setLogin(AccountUtil.isLogin());*/
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }).sendEmptyMessage(0);

    }
}