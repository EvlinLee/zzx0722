package com.eryue.home;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.eryue.ActivityHandler;
import com.eryue.R;
import com.eryue.activity.BaseFragment;
import com.library.ui.dragrefresh.DragRefreshListView;
import com.library.ui.dragrefresh.ListViewFooter;

import net.InterfaceManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bli.Jason on 2018/2/8.
 */

public class GoodsFragment extends BaseFragment implements DragRefreshListView.DragRefreshListViewListener,HomeRequestPresenter.HomeRequestGoodsListener {

    private GoodsTabModel tabModel;


    public static GoodsFragment newInstance(GoodsTabModel tabModel){

        GoodsFragment goodsFragment = new GoodsFragment();
        goodsFragment.tabModel = tabModel;
        return goodsFragment;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_draggrid);
        initView();
        initData();
    }

    private DragRefreshListView listview;
    private List<Goods> dataList;
    private GoodsGridAdapter goodsAdapter;


    private void initView() {
        listview = (DragRefreshListView) getView().findViewById(R.id.listview_drag);
        listview.setFooterDividersEnabled(false);
//        listview.setFooterViewState(ListViewFooter.STATE_HIDE);
        listview.setFooterViewState(ListViewFooter.STATE_HIDE);
        listview.setAutoLoadMore(true);
        listview.refreshComplete(true, new Date().getTime());
        //headerview
//        final View headerView = LayoutInflater.from(this).inflate(R.layout.layout_header_adv,null);
//        listview.getRefreshableView().addHeaderView(headerView);

        goodsAdapter=new GoodsGridAdapter(getContext());
        goodsAdapter.setTabModel(tabModel);
        listview.setAdapter(goodsAdapter);



    }

    private HomeRequestPresenter presenter;

    //类型：好券搜索、指定搜索
    String type;
    public void initData() {
        //图标
        //图标下的文字
//        String name[]={"时钟","信号","宝箱","秒钟","大象","FF","记事本","书签","印象","商店","主题","迅雷"};
//        dataList = new ArrayList<Goods>();
//        Goods goods;
//        for (int i = 0; i <name.length; i++) {
//            goods = new Goods();
//            goods.setName(name[i]);
//            if (null!=tabModel&&tabModel.getVideoFlag() == GoodsTabModel.VIDEO){
//                goods.setVideoFlag(Goods.VIDEO);
//            }
//            dataList.add(goods);
//        }
//        Log.d("libo","class="+this); 运动手环电子表男女孩学生韩版简约
//        adapter.setDataList(dataList);
//        adapter.notifyDataSetChanged();

        presenter = new HomeRequestPresenter();
        presenter.setGoodsListener(this);
        type = getActivity().getIntent().getStringExtra("type");


        listview.setDragRefreshListViewListener(this);
        startRequest();


    }

    public void startRequest(){
        Log.d("libo","GoodsFragment------startRequest");
        if (null!=listview){
            if (pageNo == 0){
                listview.setHeaderViewState();
            }
        }else{
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (pageNo == 0){
                        if (null!=listview){
                            listview.setHeaderViewState();
                        }
                    }

                }
            },200);
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

        if (isLastPage){
            return;
        }

        pageNo++;
        //获取下一页数据
        sendRequest();

    }
    String tabName;
    private void sendRequest(){
        if (null == tabModel){
            return;
        }
        tabName = tabModel.getName();
        if (TextUtils.isEmpty(tabName)){
            return;
        }

        if (null!=presenter){
            if(!TextUtils.isEmpty(type)&&type.equals("指定搜索")){
                presenter.superSearchProduct(pageNo,tabName,tabModel.getSidx(),tabModel.getIsQuan(),tabModel.getIsMall());

            }else if (tabName.equals("今日上新")){
                //获取数据
                //区分今日上新、9.9包邮、好券优选、人气宝贝、品牌爆款
                tabName = "";
                presenter.searchProduct(pageNo,tabName,tabModel.getSidx(),null);
            }else if (tabName.equals("9.9包邮")){
                presenter.searchProduct99Req(pageNo,tabName,tabModel.getSidx(),null);
            }else if (tabName.equals("好券优选")){
                presenter.searchProductYxReq(pageNo,tabModel.getSidx());
            }else if (tabName.equals("人气宝贝")||tabName.equals("好货疯抢")
                    ||tabName.equals("爆款单")){
                //好货疯抢、人气宝贝、爆款单
                presenter.searchProductTop(pageNo,tabModel.getSidx());
            }else if (tabName.equals("品牌爆款")){
                presenter.searchProductBrand(pageNo,tabModel.getSidx());
            }else if (tabName.equals("聚划算")){
                presenter.searchProductJhs(pageNo,tabModel.getSidx());
            }else if (tabName.equals("淘抢购")){
                presenter.searchProductTqg(pageNo,tabModel.getSidx());
            }else if (tabName.equals("上百券")){
                presenter.searchProduct100(pageNo,tabModel.getSidx());
            }else if (tabName.equals(getResources().getString(R.string.string_bottom_tab03))){
                //视频购物
                presenter.searchProductVideo(pageNo,tabModel.getSidx());

            }else if (tabName.equals("收藏夹")){
                //收藏夹
                presenter.searchProcutCollection();
                //收藏加没有分页
                isLastPage = true;
            }else{
                presenter.searchProduct(pageNo,tabName,tabModel.getSidx(),null);
            }


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null!=presenter&&!TextUtils.isEmpty(tabName)&&tabName.equals("收藏夹")){
            //收藏夹
            presenter.searchProcutCollection();
            //收藏加没有分页
            isLastPage = true;
        }
    }

    public void dispose(){

    }

    private int pageNo = 0;
    boolean isLastPage;
    private List<InterfaceManager.SearchProductInfo> allDataList = new ArrayList<>();
    @Override
    public void onDataBack(final List<InterfaceManager.SearchProductInfo> result) {
        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                // 显示最后更新的时间
                listview.refreshComplete(true, new Date().getTime());
                if (result.isEmpty()){
                    //无数据
                    isLastPage = true;
                }else{
                    if (pageNo == 1){
                        allDataList.clear();
                    }
                    allDataList.addAll(result);
                    if (null!=goodsAdapter){
                        goodsAdapter.setDataList(allDataList);
                        goodsAdapter.notifyDataSetChanged();
                    }
                }
            }
        }).sendEmptyMessage(0);

    }

    @Override
    public void onDataError() {
        listview.refreshComplete(true, new Date().getTime());
    }
}
