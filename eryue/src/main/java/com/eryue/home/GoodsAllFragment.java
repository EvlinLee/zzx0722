package com.eryue.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.GoodsListTabModel;
import com.eryue.R;
import com.eryue.activity.BaseFragment;
import com.eryue.goodsdetail.GoodsImageModel;
import com.eryue.goodsdetail.GridViewImageAdapter;
import com.eryue.goodsdetail.ImageViewPagerAdapter;
import com.eryue.search.GoodsSearchListActivity;
import com.eryue.ui.NoScrollGridView;
import com.eryue.widget.AutoScrollViewPager.AutoScrollViewPager;
import com.library.ui.dragrefresh.DragRefreshListView;
import com.library.ui.dragrefresh.ListViewFooter;
import com.library.util.StringUtils;
import com.viewpagerindicator.CirclePageIndicator;

import net.InterfaceManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bli.Jason on 2018/2/8.
 */

public class GoodsAllFragment extends BaseFragment implements DragRefreshListView.DragRefreshListViewListener,
        HomeRequestPresenter.HomeRequestPosterListener, HomeRequestPresenter.HomeRequestGoodsListener, HomeRequestPresenter.GoodsTJListener {

    private GoodsTabModel tabModel;

    public static GoodsAllFragment newInstance() {
        GoodsAllFragment goodsFragment = new GoodsAllFragment();
        return goodsFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_draggrid);
        init();
        initData();
    }

    private DragRefreshListView listview;
    private List<Goods> dataList;
    private GoodsGridAdapter goodsAdapter;

    /**
     * 广告
     */
    private AutoScrollViewPager viewPager_adv;
    private ImageViewPagerAdapter pagerAdapter;

    //今日值得买
    private LinearLayout layout_daily;
    ViewPager viewpager_daily;
    CirclePageIndicator indicator_daily;
    GoodsDailyPagerAdapter dailyPagerAdapter;

    private String baseIP = AccountUtil.getBaseIp();


    private void init() {
        listview = (DragRefreshListView) getView().findViewById(R.id.listview_drag);
        listview.setDragRefreshListViewListener(this);
        listview.setFooterDividersEnabled(false);
//        listview.setFooterViewState(ListViewFooter.STATE_HIDE);
        listview.setFooterViewState(ListViewFooter.STATE_HIDE);
        listview.setAutoLoadMore(true);
        listview.refreshComplete(true, new Date().getTime());
        //headerview
        final View headerView = LayoutInflater.from(getContext()).inflate(R.layout.header_fragment_goods, null);
        listview.addHeaderView(headerView);


        goodsAdapter = new GoodsGridAdapter(getContext());
        listview.setAdapter(goodsAdapter);

        //广告
        viewPager_adv = (AutoScrollViewPager) headerView.findViewById(R.id.viewPager_adv);
//        List<String> dataList = new ArrayList<>();
//        dataList.add("http://122.152.200.24/cms/banner.jpg");
//        dataList.add("http://122.152.200.24/cms/banner.jpg");
//        dataList.add("http://122.152.200.24/cms/banner.jpg");
//        dataList.add("http://122.152.200.24/cms/banner.jpg");
//        dataList.add("http://122.152.200.24/cms/banner.jpg");
//        pagerAdapter = new ImageViewPagerAdapter<GoodsImageModel>(getContext(),dataList);
//        viewPager_adv.setAdapter(pagerAdapter);
        pagerAdapter = new ImageViewPagerAdapter<GoodsImageModel>(getContext());
        viewPager_adv.setAdapter(pagerAdapter);

        GridView gridview_home = (GridView) headerView.findViewById(R.id.gridview_home);
        HomeHorizontalListAdapter horiAdapter = new HomeHorizontalListAdapter(getContext());

        String[] horizontalArray = {"今日上新", "9.9包邮", "好券优选", "人气宝贝", "品牌爆款"};
        final List<String> horizontalList = new ArrayList<>();
        for (int i = 0; i < horizontalArray.length; i++) {
            horizontalList.add(horizontalArray[i]);
        }
        List<Integer> horizontalImgList = new ArrayList<>();
        horizontalImgList.add(R.drawable.ic_daily);
        horizontalImgList.add(R.drawable.ic_jj);
        horizontalImgList.add(R.drawable.ic_yx);
        horizontalImgList.add(R.drawable.ic_top);
        horizontalImgList.add(R.drawable.ic_bk);


        horiAdapter.setImgList(horizontalImgList);
        horiAdapter.setDataList(horizontalList);
        gridview_home.setAdapter(horiAdapter);

        String[] resultTab = {"综合", "券后价", "销量", "超优惠"};
        String[] tabValues = {"updateTime", "afterQuan", "soldQuantity", "quanPrice"};
        final ArrayList<GoodsListTabModel> tabList = new ArrayList<>();
        GoodsListTabModel tabModel;
        for (int i = 0; i < resultTab.length; i++) {
            tabModel = new GoodsListTabModel(resultTab[i], tabValues[i]);
            tabList.add(tabModel);
        }
        gridview_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = parent.getAdapter().getItem(position);
                if (null != object && object instanceof String) {
                    Intent intent = new Intent(getContext(), GoodsSearchListActivity.class);
                    intent.putExtra("title", (String) object);
                    startActivity(intent);
                }
            }
        });

        //今日值得买
        layout_daily = (LinearLayout) headerView.findViewById(R.id.layout_daily);
        viewpager_daily = (ViewPager) headerView.findViewById(R.id.viewpager_daily);
        indicator_daily = (CirclePageIndicator) headerView.findViewById(R.id.indicator_daily);
        dailyPagerAdapter = new GoodsDailyPagerAdapter(getContext());
//        String[] dailyArray = {"全套基础视频教程","宽松百搭羊毛衫","P美工视频教程","[饱腹代餐]绿瘦","清肠排毒大麦若","清肠排毒绿瘦荷",
//                "后期制作视频教程","燕肌旗舰店正品","减肥瘦身绿瘦左","燕肌药业去细纹","美味饱腹绿瘦香","控食饱腹绿瘦香"};
//        List<String> dailyList = new ArrayList<>();
//        for (int i=0;i<dailyArray.length;i++){
//            dailyList.add(dailyArray[i]);
//        }
//        dailyPagerAdapter.setDataList(dailyList);
        viewpager_daily.setAdapter(dailyPagerAdapter);
        indicator_daily.setViewPager(viewpager_daily);
//        postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                viewpager_daily.requestLayout();
//            }
//        }, 100);

        NoScrollGridView gridview_adv = (NoScrollGridView) headerView.findViewById(R.id.gridview_adv);
        GridViewImageAdapter adapter = new GridViewImageAdapter(getContext());
        List<String> advList = new ArrayList<>();
        advList.add(baseIP + "newTheme/img/nian-01.jpg");
        advList.add(baseIP + "newTheme/img/nian-02.jpg");
        advList.add(baseIP + "newTheme/img/nian-03.jpg");
        advList.add(baseIP + "newTheme/img/nian-04.jpg");
        adapter.setDataList(advList);
        gridview_adv.setAdapter(adapter);

        String[] advTitles = {"爆款单", "上百券", "聚划算", "淘抢购"};

        final ArrayList<GoodsListTabModel> advTitleList = new ArrayList<>();
        for (int i = 0; i < advTitles.length; i++) {
            tabModel = new GoodsListTabModel(advTitles[i], advTitles[i]);
            advTitleList.add(tabModel);
        }

        gridview_adv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), GoodsSearchListActivity.class);
                intent.putExtra("title", advTitleList.get(position).getName());
                startActivity(intent);
            }
        });


    }


    private HomeRequestPresenter presenter;

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
//        goodsAdapter.setDataList(dataList);
//        goodsAdapter.notifyDataSetChanged();

        presenter = new HomeRequestPresenter();
        presenter.setGoodsListener(this);
        presenter.setPosterListener(this);
        presenter.setGoodsTJListener(this);

        startRequest();

    }

    public void startRequest(){
        if(pageNo ==1&&null!=listview){
            Log.d("libo","GoodsAllFragment------startRequest");
            listview.setHeaderViewState();
        }
    }

    @Override
    public void onRefresh() {
        if (null != presenter) {
            isLastPage = false;
            pageNo = 1;
            //获取海报
            presenter.getServerConfig(getContext());
            //获取今日值得买
            presenter.searchProductTj();
            //获取数据
            presenter.searchProduct(pageNo, "");
        }

    }

    @Override
    public void onLoadMore() {
        if (isLastPage) {
            return;
        }

        pageNo++;
        //获取数据
        presenter.searchProduct(pageNo, "");

        Log.d("libo", "onLoadMore");
    }

    public void dispose() {

    }


    @Override
    public void onPosterBack(final List<String> banners) {

//        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
//            @Override
//            public void handleMessage(Message msg) {
//                List<String> dataList = new ArrayList<>();
//                dataList.add("http://122.152.200.24/cms/banner.jpg");
//                dataList.add("http://122.152.200.24/cms/banner.jpg");
//                dataList.add("http://122.152.200.24/cms/banner.jpg");
//                dataList.add("http://122.152.200.24/cms/banner.jpg");
//                dataList.add("http://122.152.200.24/cms/banner.jpg");
//                pagerAdapter = new ImageViewPagerAdapter<GoodsImageModel>(getContext(),banners);
//                viewPager_adv.setAdapter(pagerAdapter);
//                pagerAdapter.add(banners);
//
//            }
//        }).sendEmptyMessage(0);

        if (null != banners) {

            String url;
            for (int i = 0; i < banners.size(); i++) {
                url = banners.get(i);
                if (!url.startsWith("http")) {
                    url = baseIP + url;
                    banners.set(i, url);
                }
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (null != pagerAdapter) {
                        pagerAdapter.setData(banners);
                    }
                }
            });

        }else{
            if (null!=viewPager_adv){
                viewPager_adv.setVisibility(View.GONE);
            }
        }




    }

    @Override
    public void onPosterError() {
        if (null!=viewPager_adv){
            viewPager_adv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPicBack(List<InterfaceManager.ShufflingPictureInfo> banners) {

    }

    @Override
    public void onPicError() {

    }


    private int pageNo = 1;
    boolean isLastPage;
    private List<InterfaceManager.SearchProductInfo> allDataList = new ArrayList<>();

    @Override
    public void onDataBack(final List<InterfaceManager.SearchProductInfo> result) {

        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                if (result.isEmpty()) {
                    //无数据
                    isLastPage = true;
                } else {
                    if (pageNo == 1){
                        allDataList.clear();
                    }
                    allDataList.addAll(result);
                    // 显示最后更新的时间
                    listview.refreshComplete(true, new Date().getTime());
                    if (null != goodsAdapter) {
                        goodsAdapter.setDataList(allDataList);
                        goodsAdapter.notifyDataSetChanged();
                    }
                }
            }
        }).sendEmptyMessage(0);

    }

    @Override
    public void onDataError() {

    }

    @Override
    public void onGoodsTJBack(final List<InterfaceManager.SearchProductInfo> result) {

        post(new Runnable() {
            @Override
            public void run() {
                if (null != layout_daily) {
                    layout_daily.setVisibility(View.VISIBLE);
                }
                if (null != dailyPagerAdapter) {
                    dailyPagerAdapter.setDataList(result);
                    dailyPagerAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void onGoodsTJError() {
        post(new Runnable() {
            @Override
            public void run() {
                if (null != layout_daily) {
                    layout_daily.setVisibility(View.GONE);
                }
            }
        });
    }
}
