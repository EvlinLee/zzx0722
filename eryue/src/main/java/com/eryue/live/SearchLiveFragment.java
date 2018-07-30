package com.eryue.live;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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

public class SearchLiveFragment extends BaseFragment implements UISortTabView.OnTabClickListener, SearchLivePresenter.SearchLiveListener, DragRefreshListView.DragRefreshListViewListener,
        View.OnClickListener, DragRefreshListView.ScrollStateChangeListener {


    private String[] tabStrs = {"综合", "价格", "销量", "券价"};
    private int[] tabTypes = {UISortTabView.TYPE_NORMAL, UISortTabView.TYPE_SORT, UISortTabView.TYPE_SORT, UISortTabView.TYPE_SORT};
    //updateTime：综合/afterQuan：价格/soldQuantity：销量/quanPrice：券价
    private String[] sidxArray = {"updateTime", "afterQuan", "soldQuantity", "quanPrice"};
    private UISortTabView tabView;

    private DragRefreshListView listview_goods;
    private GoodsListAdapter goodsListAdapter;

    private SearchLivePresenter presenter;

    private int pageNo = 1;
    private String sord = "desc";
    private String itemTitle = "";
    private String sidx = "updateTime";
    private String type = "";
    boolean isLastPage;
    private List<InterfaceManager.SearchProductInfoEx> allDataList = new ArrayList<>();

    private String title;

    public static SearchLiveFragment newInstatnce() {
        return new SearchLiveFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setContentView(R.layout.fragment_searchlive);
        initView();
        presenter = new SearchLivePresenter();
        presenter.setSearchLiveListener(this);

        Bundle bundle = getArguments();
        if (null != bundle) {
            title = bundle.getString("title");
            type = bundle.getString("type");
        }
        if (!TextUtils.isEmpty(title)) {

            if ("实时播".equalsIgnoreCase(title)) {
                showBack(false);
            } else {
                showBack(true);
            }

            setTitle(title);
        }

        startRequest();


    }

    private void initView() {
        iv_rocket = getView().findViewById(R.id.iv_rocket);
        iv_rocket.setOnClickListener(this);

        tabView = getView().findViewById(R.id.tabview);

        tabView.setDataArray(0, tabTypes, tabStrs);
        tabView.setOnTabClickListener(this);

        listview_goods = getView().findViewById(R.id.listview_goods);
        goodsListAdapter = new GoodsListAdapter(getContext());
        listview_goods.setAdapter(goodsListAdapter);

        listview_goods.setDragRefreshListViewListener(this);
        listview_goods.setFooterDividersEnabled(false);
        listview_goods.setFooterViewState(ListViewFooter.STATE_HIDE);
        listview_goods.setAutoLoadMore(true);
        listview_goods.setScrollStateChangeListener(this);
        listview_goods.refreshComplete(true, new Date().getTime());

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

        }

        if (flag == UISortTabView.FLAG_DOWN) {
            sord = "desc";
        } else if (flag == UISortTabView.FLAG_UP) {
            sord = "asc";
        }

        sidx = sidxArray[index];

        if (null!=goodsListAdapter){
            goodsListAdapter.clearData();
            goodsListAdapter.notifyDataSetChanged();
        }

        startRequest();


    }

    @Override
    public void onSearchLiveBack(final List<InterfaceManager.SearchProductInfoEx> result) {
        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                // 显示最后更新的时间
                listview_goods.refreshComplete(true, new Date().getTime());
                if (result.isEmpty()) {
                    //无数据
                    isLastPage = true;
                } else {
                    if (pageNo == 1) {
                        if (null!=iv_rocket){
                            iv_rocket.setVisibility(View.GONE);
                        }
                        allDataList.clear();
                    }
                    allDataList.addAll(result);
                    if (null != goodsListAdapter) {
                        goodsListAdapter.setDataList(allDataList);
                        goodsListAdapter.setLogin(AccountUtil.isLogin());
                        goodsListAdapter.notifyDataSetChanged();
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
                listview_goods.refreshComplete(true, new Date().getTime());
                if (pageNo == 1) {
                    if (null!=iv_rocket){
                        iv_rocket.setVisibility(View.GONE);
                    }
                    allDataList.clear();
                    if (null != goodsListAdapter) {
                        goodsListAdapter.setDataList(allDataList);
                        goodsListAdapter.setLogin(AccountUtil.isLogin());
                        goodsListAdapter.notifyDataSetChanged();
                    }
                }
            }
        }).sendEmptyMessage(0);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (null!=presenter){
            //切换ip，刷新数据
            if (!AccountUtil.getBaseIp().equals(presenter.getBaseIP())){
                refreshView();
            }
        }

    }

    @Override
    public void onRefresh() {
        isLastPage = false;
        pageNo = 1;
        sendRequest();
    }

    @Override
    public void onLoadMore() {
        if (isLastPage) {
            return;
        }

        pageNo++;
        //获取下一页数据
        sendRequest();
    }

    public void startRequest() {
        if (null != listview_goods) {
            listview_goods.setHeaderViewState();
        } else {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != listview_goods) {
                        listview_goods.setHeaderViewState();
                    }

                }
            }, 200);
        }

    }

    public void refreshView(){
        pageNo = 1;
        if (null != listview_goods) {
            listview_goods.setSelection(0);
            listview_goods.setHeaderViewState();
        }
    }

    private void sendRequest() {

        if (!TextUtils.isEmpty(title)) {

            if ("今日爆款".equalsIgnoreCase(title)) {
                if (null != presenter) {
                    presenter.requestSeachHot(pageNo, sord, itemTitle, sidx);
                }
            } else if ("百元券".equalsIgnoreCase(title)) {
                if (null != presenter) {
                    presenter.requestSearchCoupons(pageNo, sord, itemTitle, sidx);
                }
            } else if ("实时播".equalsIgnoreCase(title)) {
                if (null != presenter) {
                    presenter.requestSeachLive(pageNo, sord, itemTitle, sidx);
                }
            } else {
                // 渠道请求
                if (null != presenter) {
                    presenter.requestBusiness(pageNo, sord, type, itemTitle, "", sidx);
                }
            }

        } else {
            if (null != presenter) {
                presenter.requestSeachLive(pageNo, sord, itemTitle, sidx);
            }
        }

    }

    private ImageView iv_rocket;
    private boolean isRocketShow = false;

    @Override
    public void onItemsDisappear(int startPosition, int endPosition, boolean isUp) {

    }

    @Override
    public void onItemsVisible(int startPosition, int endPosition, boolean isUp) {
        if (startPosition > GoodsContants.SHOWROCKET_NUM) {
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

    @Override
    public void onClick(View view) {
        if (view == iv_rocket) {

            //滑动到顶部
            if (null != listview_goods) {
                listview_goods.setSelection(0);
            }

        }
    }
}
