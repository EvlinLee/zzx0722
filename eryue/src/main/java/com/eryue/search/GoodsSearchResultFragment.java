package com.eryue.search;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.eryue.ActivityHandler;
import com.eryue.GoodsContants;
import com.eryue.R;
import com.eryue.activity.BaseFragment;
import com.eryue.home.GoodsListAdapter;
import com.eryue.live.GoodsBusinessModel;
import com.eryue.live.GoodsBusinessPopView;
import com.eryue.ui.UISortTabView;
import com.library.ui.dragrefresh.DragRefreshListView;
import com.library.ui.dragrefresh.ListViewFooter;
import com.library.util.ToastTools;

import net.InterfaceManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by enjoy on 2018/5/10.
 * 实时播
 */

public class GoodsSearchResultFragment extends BaseFragment implements UISortTabView.OnTabClickListener,GoodsSearchResultPresenter.WholeSearchListener,DragRefreshListView.DragRefreshListViewListener,
        GoodsBusinessPopView.OnBusinessPopListener,View.OnClickListener, DragRefreshListView.ScrollStateChangeListener{


    private static final int TAB_ALL = 0;
    private static final int TAB_PRICE = 1;
    private static final int TAB_SOLD = 2;
    private static final int TAB_QUANPRICE = 3;
    private static final int TAB_BUSINESS = 4;

    //all：全网/tb：淘宝/jd：京东/mgj：蘑菇街/pdd：拼多多/sn：苏宁/tbActivity：淘宝活动


    private List<GoodsBusinessModel> tabList = new ArrayList<>();

    private String[] tabStrs = {"综合","价格","销量","券价","全部"};
    private int[] tabTypes = {UISortTabView.TYPE_NORMAL,UISortTabView.TYPE_SORT,UISortTabView.TYPE_SORT,UISortTabView.TYPE_SORT,UISortTabView.TYPE_DOWN};
    //updateTime：综合/afterQuan：价格/soldQuantity：销量/quanPrice：券价
    private String[] sidxArray = {"updateTime","afterQuan","soldQuantity","quanPrice","all"};
    private UISortTabView tabView;

    private DragRefreshListView listview_goods;
    private GoodsListAdapter goodsListAdapter;

    private GoodsSearchResultPresenter presenter;

    private int pageNo = 1;
    private String sord = "desc";
    private String type = "all";
    private String itemTitle = "";
    private String cat = "";
    private String sidx = "updateTime";
    boolean isLastPage;
    private List<InterfaceManager.SearchProductInfoEx> allDataList = new ArrayList<>();

    private String[] titles = {"全部", "女装", "男装", "母婴", "鞋包", "数码", "家装", "食品", "美妆", "其他"};

    private View view_line;

    public static GoodsSearchResultFragment newInstance() {
        GoodsSearchResultFragment fragment = new GoodsSearchResultFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setContentView(R.layout.fragment_searchresult);
        initView();
        initData();
        presenter = new GoodsSearchResultPresenter();
        presenter.setWholeSearchListener(this);

        itemTitle = getActivity().getIntent().getStringExtra("title");
        cat = getActivity().getIntent().getStringExtra("cat");

        startRequest();


    }

    private void initData(){
//        tabTestList = new ArrayList<>();
//        GoodsTabModel tabModel = null;
//        for (int i = 0; i < titles.length; i++) {
//            tabModel = new GoodsTabModel();
//            if (i == 0) {
//                tabModel.setSelectTag(GoodsTabModel.TAG_SELECT);
//            }
//            tabModel.setName(titles[i]);
//            tabTestList.add(tabModel);
//        }

    }

    private void initView(){

        tabView = getView().findViewById(R.id.tabview);

        tabView.setDataArray(0,tabTypes,tabStrs);
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

        view_line = getView().findViewById(R.id.view_line);
        iv_rocket = getView().findViewById(R.id.iv_rocket);
        iv_rocket.setOnClickListener(this);

    }


    /**
     * 弹出框
     */
    private GoodsBusinessPopView popView;

    /**
     * 弹出框
     */
//    private GoodsTabPopView tabPopView;
//
//    List<GoodsTabModel> tabTestList;
    @Override
    public void onTabClick(int index, int flag) {

        Log.d("libo","index="+index+"----flag="+flag);
        //"综合","价格","销量","券价","全部"
        switch (index){
            case TAB_ALL:
                //综合
                break;
            case TAB_PRICE:
                //价格
                break;
            case TAB_SOLD:
                //销量
                break;
            case TAB_QUANPRICE:
                //券价
                break;
            case TAB_BUSINESS:
                //all：全网/tb：淘宝/jd：京东/mgj：蘑菇街/pdd：拼多多/sn：苏宁/tbActivity：淘宝活动
                //刷新popview数据
                if (null == popView) {
                    //刷新popview数据
                    popView = new GoodsBusinessPopView(getContext());
                    popView.setOnPopListener(this);

                    GoodsBusinessModel businessModel;
                    for (int i = 0;i< GoodsContants.tabs.length;i++){
                        businessModel = new GoodsBusinessModel(GoodsContants.tabs[i],GoodsContants.tabValues[i]);
                        if (i == 0){
                            businessModel.setSelectTag(GoodsBusinessModel.TAG_SELECT);
                        }
                        tabList.add(businessModel);
                    }
                    popView.refreshListData(tabList);

                }

                if (null != popView) {
                    popView.showPopView(view_line);
                }
                //刷新popview数据
//                if (null == tabPopView) {
//                    //刷新popview数据
//                    tabPopView = new GoodsTabPopView(getContext());
////                    tabPopView.setOnPopListener(this);
//                    tabPopView.setOnItemClickListener(this);
//
//
//                    tabPopView.refreshListData(tabTestList);
//                }
//
//
//                if (null != tabPopView) {
//                    tabPopView.showPopView(getView());
//                }
                return;

        }

        if (flag == UISortTabView.FLAG_DOWN){
            sord = "desc";
        }else if (flag == UISortTabView.FLAG_UP){
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
    public void onRefresh() {
        isLastPage = false;
        pageNo = 1;
        sendRequest();
    }

    @Override
    public void onLoadMore() {
        if (isLastPage){
            return;
        }

        pageNo++;
        //获取下一页数据
        sendRequest();
    }

    public void startRequest(){
        if (null!=listview_goods){
                listview_goods.setHeaderViewState();
        }else{
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null!=listview_goods){
                        listview_goods.setHeaderViewState();
                    }

                }
            },200);
        }

    }

    private void sendRequest(){
        if (pageNo > 1){
            showProgressMum();
        }
        if (null!=presenter){
            presenter.requestWholeSearch(pageNo,sord,type,itemTitle,cat,sidx);
        }
    }

    @Override
    public void onWholeSearcheBack(final List<InterfaceManager.SearchProductInfoEx> result) {

        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                hideProgressMum();
                // 显示最后更新的时间
                listview_goods.refreshComplete(true, new Date().getTime());
                if (result.isEmpty()){
                    //无数据
                    isLastPage = true;
                    ToastTools.showShort(getActivity(),"无更多商品");
                }else{
                    if (pageNo == 1){
                        allDataList.clear();

                        if (null!=iv_rocket){
                            iv_rocket.setVisibility(View.GONE);
                        }
                    }
                    allDataList.addAll(result);
                    if (null!=goodsListAdapter){
                        goodsListAdapter.setDataList(allDataList);
                        goodsListAdapter.notifyDataSetChanged();
                    }
                }
            }
        }).sendEmptyMessage(0);

    }

    @Override
    public void onWholeSearchError() {
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
                        goodsListAdapter.notifyDataSetChanged();
                    }
                }
                ToastTools.showShort(getActivity(),"无更多商品");
            }
        }).sendEmptyMessage(0);


    }


    @Override
    public void onBusinessClick(GoodsBusinessModel businessModel) {
        pageNo = 1;
        type = businessModel.getValue();

        if (null!=tabView){
            tabView.setTabText(TAB_BUSINESS,businessModel.getName());
        }

        if (null != goodsListAdapter) {
            goodsListAdapter.clearData();
            goodsListAdapter.notifyDataSetChanged();
        }
        startRequest();
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
