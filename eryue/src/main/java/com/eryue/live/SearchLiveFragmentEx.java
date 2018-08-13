package com.eryue.live;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class SearchLiveFragmentEx extends BaseFragment implements UISortTabView.OnTabClickListener,SearchLivePresenter.SearchLiveListener {

    public static SearchLiveFragmentEx newInstatnce() {
        return new SearchLiveFragmentEx();
    }

    private RecyclerView recyclerView;

    private LiveGoodsRecylerAdapter adapter;

    /**
     * 筛选tab
     */
    private String[] tabStrs = {"综合", "价格", "销量", "券价"};
    private int[] tabTypes = {UISortTabView.TYPE_NORMAL, UISortTabView.TYPE_SORT, UISortTabView.TYPE_SORT, UISortTabView.TYPE_SORT};
    private UISortTabView tabView;

    private String[] sidxArray = {"updateTime", "afterQuan", "soldQuantity", "quanPrice"};

    private SearchLivePresenter presenter;

    private int pageNo = 1;
    private String sord = "desc";
    private String itemTitle = "";
    private String sidx = "updateTime";
    private String type = "";
    boolean isLastPage;
    private List<InterfaceManager.SearchProductInfoEx> allDataList = new ArrayList<>();

    /**
     * 下拉刷新
     */
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_searchliveex);

        View mStateBarFixer =getView().findViewById(R.id.status_bar_fix_view);
        mStateBarFixer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(getActivity())));
        mStateBarFixer.setBackgroundColor(getResources().getColor(R.color.gray));

        initView();

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


        recyclerView.setPadding(8,8,8,8);

        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setAdapter(adapter);


        sendRequest();

    }

    public static int getStatusBarHeight(Activity a) {
        int result = 0;
        int resourceId = a.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = a.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void initView(){
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                sendRequest();
//                allDataList.clear();
                //关闭下拉刷新显示
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        tabView = getView().findViewById(R.id.tabview);

        tabView.setDataArray(0, tabTypes, tabStrs);
        tabView.setOnTabClickListener(this);
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
    public void onTabClick(int index, int flag) {

        Log.d("libo", "index=" + index + "----flag=" + flag);
        switch (index) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default:

        }

        if (flag == UISortTabView.FLAG_DOWN) {
            sord = "desc";
        } else if (flag == UISortTabView.FLAG_UP) {
            sord = "asc";
        }

        sidx = sidxArray[index];

        if (null!=adapter){
            adapter.notifyDataSetChanged();
        }

        sendRequest();

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