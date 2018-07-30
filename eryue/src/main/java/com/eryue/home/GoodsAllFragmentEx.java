package com.eryue.home;

import android.app.Dialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.GoodsContants;
import com.eryue.R;
import com.eryue.RedRocketPopView;
import com.eryue.activity.BaseFragment;
import com.eryue.activity.ImageBigActivity;
import com.eryue.activity.ImageBrowserActivity;
import com.eryue.goodsdetail.GoodsDetailActivityEx;
import com.eryue.goodsdetail.GoodsImageModel;
import com.eryue.goodsdetail.GoodsWebActivity;
import com.eryue.goodsdetail.HomeViewPagerAdapter;
import com.eryue.jd.SearchJDActivity;
import com.eryue.jd.SearchJDPresenter;
import com.eryue.live.SearchLiveActivity;
import com.eryue.mine.MessageCenterActivity;
import com.eryue.search.GoodsSearchActivityEx;
import com.eryue.ui.HorizontalListView;
import com.eryue.util.Logger;
import com.eryue.util.SharedPreferencesUtil;
import com.eryue.widget.AutoScrollViewPager.AutoScrollViewPager;
import com.eryue.widget.AutoScrollViewPager.BaseViewPagerAdapter;
import com.library.ui.dragrefresh.DragRefreshListView;
import com.library.ui.dragrefresh.ListViewFooter;
import com.library.util.StringUtils;
import com.library.util.UIScreen;
import com.sunfusheng.marqueeview.MarqueeView;

import net.InterfaceManager;
import net.KeyFlag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bli.Jason on 2018/2/8.
 */

public class GoodsAllFragmentEx extends BaseFragment implements DragRefreshListView.DragRefreshListViewListener,
        HomeRequestPresenter.HomeRequestPosterListener, HomeRequestPresenter.HomeRequestGoodsListener,
        HomeRequestPresenter.GoodsTJListener, HomeRequestPresenter.HomHotDataListener, View.OnClickListener, DragRefreshListView.ScrollStateChangeListener, HomeRequestPresenter.NoticeListener,
        MainPresenter.SearchRedPacketListener, HomeRequestPresenter.AppInfoCountListener, SearchJDPresenter.SearchJDActivityListener {

    private GoodsTabModel tabModel;

    //搜索框
    private LinearLayout search_contain;

    private String baseIP = AccountUtil.getBaseIp();


    private ImageView iv_rocket;

    private ImageView iv_message;

    GridView gridview_business;

    public static GoodsAllFragmentEx newInstance() {
        GoodsAllFragmentEx goodsFragment = new GoodsAllFragmentEx();
        return goodsFragment;
    }

    private DragRefreshListView listview;

    /**
     * 置顶预览
     */
    private RecyclerView rv_goodsTop;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_goodsall);
        init();
        initData();
    }

    private List<Goods> dataList;
//    private GoodsGridAdapter goodsAdapter;

    private GoodsListAdapter goodsListAdapter;
    /**
     * 广告
     */
    private AutoScrollViewPager viewPager_adv;
    private HomeViewPagerAdapter pagerAdapter;

    //今日值得买
//    private LinearLayout layout_daily;
//    ViewPager viewpager_daily;
//    CirclePageIndicator indicator_daily;
//    GoodsDailyPagerAdapter dailyPagerAdapter;

    //跑马灯展示公告
    private MarqueeView marqueeView;

    private LinearLayout layout_notice;

    //京东免单广告
    private LinearLayout layout_jd;
    private String jdImgUrl = baseIP + "cms/jdFreePic.jpg";

    private View view_space;


    private void init() {
        iv_rocket = getView().findViewById(R.id.iv_rocket);
        iv_rocket.setOnClickListener(this);

        search_contain = getView().findViewById(R.id.search_contain);
        search_contain.setOnClickListener(this);

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
        listview.setScrollStateChangeListener(this);


        goodsListAdapter = new GoodsListAdapter(getContext());
        listview.setAdapter(goodsListAdapter);

        //公告
        layout_notice = headerView.findViewById(R.id.layout_notice);
        marqueeView = headerView.findViewById(R.id.marqueeView);
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {

                if (null != noticeInfoList && position < noticeInfoList.size()) {

                    InterfaceManager.SearchNoticeInfo searchNoticeInfo = noticeInfoList.get(position);

                    if (null != searchNoticeInfo) {

                        Intent intent = new Intent(getContext(), GoodsNoticeDetailActivity.class);
                        intent.putExtra("title", searchNoticeInfo.title);
                        intent.putExtra("databaseID", searchNoticeInfo.databaseID);
                        getActivity().startActivity(intent);
                    }

                }

            }
        });

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
        pagerAdapter = new HomeViewPagerAdapter<GoodsImageModel>(getContext());
        viewPager_adv.setAdapter(pagerAdapter);
        pagerAdapter.setListener(new BaseViewPagerAdapter.OnAutoViewPagerItemClickListener() {

            @Override
            public void onItemClick(int position, Object var2) {
                if (null != banners && position < banners.size()) {

                    InterfaceManager.ShufflingPictureInfo shufflingPictureInfo = banners.get(position);

                    if (null == shufflingPictureInfo){
                        return;
                    }
                    //lbType;//轮播图类型 1：普通 2：专场
                    String imageUrl = shufflingPictureInfo.zcPicture;
                    if (shufflingPictureInfo.lbType == 2&&!TextUtils.isEmpty(imageUrl)){
                        if (!imageUrl.startsWith("http")) {
                            imageUrl = baseIP + imageUrl;
                        }
                        //跳转到大图
                        Intent intent = new Intent(getContext(), ImageBigActivity.class);
                        intent.putExtra(ImageBigActivity.KEY_URL, imageUrl);
                        startActivity(intent);

                    }else{
                        if (TextUtils.isEmpty(shufflingPictureInfo.itemID) || !StringUtils.isNum(shufflingPictureInfo.itemID)) {
                            return;
                        }
                        Intent intent = new Intent(getContext(), GoodsDetailActivityEx.class);
                        intent.putExtra("itemId", shufflingPictureInfo.itemID);
                        if (!TextUtils.isEmpty(shufflingPictureInfo.type)) {
                            intent.putExtra("productType", shufflingPictureInfo.type);
                        } else {
                            intent.putExtra("productType", "tb");
                        }
                        getContext().startActivity(intent);
                    }

                }

            }
        });

        ViewGroup.LayoutParams lp = viewPager_adv.getLayoutParams();
        lp.width = UIScreen.screenWidth;
        lp.height = lp.width * 2 / 5;
        viewPager_adv.setLayoutParams(lp);

        layout_jd = headerView.findViewById(R.id.layout_jd);
        view_space = headerView.findViewById(R.id.view_space);


//        GridView gridview_home = (GridView) headerView.findViewById(R.id.gridview_home);
//        HomeHorizontalListAdapter horiAdapter = new HomeHorizontalListAdapter(getContext());
//
//        String[] horizontalArray = {"今日上新", "9.9包邮", "好券优选", "人气宝贝", "品牌爆款"};
//        final List<String> horizontalList = new ArrayList<>();
//        for (int i = 0; i < horizontalArray.length; i++) {
//            horizontalList.add(horizontalArray[i]);
//        }
//        List<Integer> horizontalImgList = new ArrayList<>();
//        horizontalImgList.add(R.drawable.ic_daily);
//        horizontalImgList.add(R.drawable.ic_jj);
//        horizontalImgList.add(R.drawable.ic_yx);
//        horizontalImgList.add(R.drawable.ic_top);
//        horizontalImgList.add(R.drawable.ic_bk);
//
//
//        horiAdapter.setImgList(horizontalImgList);
//        horiAdapter.setDataList(horizontalList);
//        gridview_home.setAdapter(horiAdapter);
//        gridview_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Object object = parent.getAdapter().getItem(position);
//                if (null != object && object instanceof String) {
//                    Intent intent = new Intent(getContext(), GoodsSearchListActivity.class);
//                    intent.putExtra("title", (String) object);
//                    startActivity(intent);
//                }
//            }
//        });

        //修改今日上新这一行的控件为左右滑动(2期修改)
        HorizontalGoodAllAdapter businessAdapter = new HorizontalGoodAllAdapter(getContext());

        final String[] businessType = {"tb","", "", "", "jd", "pdd", "mgj", "sn"};
        final String[] businessArray = {"淘宝","今日爆款", "百元券", "分类", "京东", "拼多多", "蘑菇街", "苏宁"};
        final List<String> businessList = new ArrayList<>();
        for (int i = 0; i < businessArray.length; i++) {
            businessList.add(businessArray[i]);
        }
        List<Integer> businessImgList = new ArrayList<>();
        businessImgList.add(R.drawable.ic_taobao);
        businessImgList.add(R.drawable.ic_dailyhot);
        businessImgList.add(R.drawable.ic_hundred);
        businessImgList.add(R.drawable.ic_fl);
        businessImgList.add(R.drawable.ic_jd);
        businessImgList.add(R.drawable.ic_pdd);
        businessImgList.add(R.drawable.ic_mgj);
        businessImgList.add(R.drawable.ic_sn);


        businessAdapter.setImgList(businessImgList);
        businessAdapter.setDataList(businessList);


        gridview_business = getView().findViewById(R.id.gridview_business);
        gridview_business.setAdapter(businessAdapter);



//        String[] resultTab = {"综合", "券后价", "销量", "超优惠"};
//        String[] tabValues = {"updateTime", "afterQuan", "soldQuantity", "quanPrice"};
//        final ArrayList<GoodsListTabModel> tabList = new ArrayList<>();
//        GoodsListTabModel tabModel;
//        for (int i = 0; i < resultTab.length; i++) {
//            tabModel = new GoodsListTabModel(resultTab[i], tabValues[i]);
//            tabList.add(tabModel);
//        }


        //今日值得买
//        layout_daily = (LinearLayout) headerView.findViewById(R.id.layout_daily);
//        viewpager_daily = (ViewPager) headerView.findViewById(R.id.viewpager_daily);
//        indicator_daily = (CirclePageIndicator) headerView.findViewById(R.id.indicator_daily);
//        dailyPagerAdapter = new GoodsDailyPagerAdapter(getContext());
//        String[] dailyArray = {"全套基础视频教程","宽松百搭羊毛衫","P美工视频教程","[饱腹代餐]绿瘦","清肠排毒大麦若","清肠排毒绿瘦荷",
//                "后期制作视频教程","燕肌旗舰店正品","减肥瘦身绿瘦左","燕肌药业去细纹","美味饱腹绿瘦香","控食饱腹绿瘦香"};
//        List<String> dailyList = new ArrayList<>();
//        for (int i=0;i<dailyArray.length;i++){
//            dailyList.add(dailyArray[i]);
//        }
//        dailyPagerAdapter.setDataList(dailyList);
//        viewpager_daily.setAdapter(dailyPagerAdapter);
//        indicator_daily.setViewPager(viewpager_daily);
//        postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                viewpager_daily.requestLayout();
//            }
//        }, 100);

//        NoScrollGridView gridview_adv = (NoScrollGridView) headerView.findViewById(R.id.gridview_adv);
//        GridViewImageAdapter adapter = new GridViewImageAdapter(getContext());
//        List<String> advList = new ArrayList<>();
//        advList.add(baseIP + "newTheme/img/nian-01.jpg");
//        advList.add(baseIP + "newTheme/img/nian-02.jpg");
//        advList.add(baseIP + "newTheme/img/nian-03.jpg");
//        advList.add(baseIP + "newTheme/img/nian-04.jpg");
//        adapter.setDataList(advList);
//        gridview_adv.setAdapter(adapter);
//
//        String[] advTitles = {"爆款单", "上百券", "聚划算", "淘抢购"};
//
//        final ArrayList<GoodsListTabModel> advTitleList = new ArrayList<>();
//        for (int i = 0; i < advTitles.length; i++) {
//            tabModel = new GoodsListTabModel(advTitles[i], advTitles[i]);
//            advTitleList.add(tabModel);
//        }
//
//        gridview_adv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getContext(), GoodsSearchListActivity.class);
//                intent.putExtra("title", advTitleList.get(position).getName());
//                startActivity(intent);
//            }
//        });

        iv_message = getView().findViewById(R.id.iv_message);
        iv_message.setOnClickListener(this);

        rv_goodsTop = (RecyclerView) getView().findViewById(R.id.rv_top);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),3);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rv_goodsTop.setLayoutManager(layoutManager);
        rv_goodsTop.setAdapter(new GoodsTopCardViewAdapter(getActivity(),inintList()));

    }

    public ArrayList inintList() {
        ArrayList<ArrayList> list = new ArrayList<ArrayList>();
        list.add(inintmLis("空调纯棉被子","￥220",R.drawable.img_like));
        list.add(inintmLis("空调纯棉被子","￥220",R.drawable.img_like));
        list.add(inintmLis("空调纯棉被子","￥220",R.drawable.img_like));
        list.add(inintmLis("空调纯棉被子","￥220",R.drawable.img_like));
        return list;
    }
    private ArrayList inintmLis(String str1,String str2,int s) {
        ArrayList list1 = new ArrayList();
        list1.add(str1);
        list1.add(str2);
        list1.add(s);
        return list1;

    }

    private HomeRequestPresenter presenter;
    private SearchJDPresenter jdPresenter;

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
//        presenter.setGoodsListener(this);
//        presenter.setGoodsTJListener(this);
        presenter.setPosterListener(this);
        presenter.setHotDataListener(this);
        presenter.setNoticeListener(this);
        presenter.setAppInfoCountListener(this);

        jdPresenter = new SearchJDPresenter();
        jdPresenter.setJdActivityListener(this);


        startRequest();

    }

    public void startRequest() {
        if (pageNo == 1 && null != listview) {
            listview.setHeaderViewState();
        }
    }

    public void refreshView(){
        pageNo = 1;
        if (null != listview) {
            listview.setSelection(0);
            listview.setHeaderViewState();
        }
    }


    boolean isBaiduRegister = false;

    @Override
    public void onRefresh() {
        if (null != presenter) {
            String uid = AccountUtil.getUID();
            baseIP = AccountUtil.getBaseIp();
            isLastPage = false;
            pageNo = 1;

            presenter.onResume();
            //获取海报
            presenter.getServerConfig(getContext());
            //获取海报
            presenter.shufflingPictureReq(uid);
            //获取公告
            presenter.searchNoticeReq(uid);
            //获取消息数量
            presenter.getAppInfoCount(uid);
            //获取今日值得买
//            presenter.searchProductTj();
            //获取数据
//            presenter.searchProduct(pageNo, "");

            //获取热门推荐
            presenter.indexSearch(pageNo);

            //获取京东首页广告
            jdPresenter.onResume();
            jdPresenter.getActivityJd(uid);


            if (AccountUtil.isLogin()) {
                if (null == mainPresenter) {
                    mainPresenter = new MainPresenter();
                }
                mainPresenter.setRedPacketListener(this);
                mainPresenter.searchRedPacketReq();
            }
        }


    }

    @Override
    public void onLoadMore() {
        if (isLastPage) {
            return;
        }

        pageNo++;
        //获取数据
//        presenter.searchProduct(pageNo, "");
        presenter.indexSearch(pageNo);

        Log.d("libo", "onLoadMore");
    }

    public void dispose() {
        if (null != presenter) {
            presenter.setPosterListener(null);
            presenter.setHotDataListener(null);
            presenter.setNoticeListener(null);
            presenter.setAppInfoCountListener(null);
        }
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

//        if (null != banners) {
//
//            String url;
//            for (int i = 0; i < banners.size(); i++) {
//                url = banners.get(i);
//                if (!url.startsWith("http")) {
//                    url = baseIP + url;
//                    banners.set(i, url);
//                }
//            }
//
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                    if (null != pagerAdapter) {
//                        pagerAdapter.setData(banners);
//                    }
//                }
//            });
//
//        } else {
//            if (null != viewPager_adv) {
//                viewPager_adv.setVisibility(View.GONE);
//            }
//        }


    }

    @Override
    public void onPosterError() {
//        if (null != viewPager_adv) {
//            viewPager_adv.setVisibility(View.GONE);
//        }
    }


    MainPresenter mainPresenter;

    @Override
    public void onResume() {
        super.onResume();

        showTips();

        showMessageRed(netInfoNum);

        if (null != presenter) {
            presenter.setPosterListener(this);
            presenter.setHotDataListener(this);
            presenter.setNoticeListener(this);
            presenter.setAppInfoCountListener(this);

            if (!AccountUtil.getBaseIp().equals(presenter.getBaseIP())){
                //切换ip，刷新数据
                refreshView();
            }
        }

    }


    //轮播图数据
    List<InterfaceManager.ShufflingPictureInfo> banners;

    @Override
    public void onPicBack(final List<InterfaceManager.ShufflingPictureInfo> banners) {

        if (null != banners) {
            this.banners = banners;

            final ArrayList<String> picList = new ArrayList<>();
//            baseIP = "http://www.371d.cn/";
            String url;
            for (int i = 0; i < banners.size(); i++) {
                url = banners.get(i).appShufflingPicture;
                if (!url.startsWith("http")) {
                    url = baseIP + url;
                }
                picList.add(url);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (null != viewPager_adv) {
                        viewPager_adv.setVisibility(View.VISIBLE);
                    }
                    if (null != pagerAdapter) {
                        pagerAdapter.setData(picList);
                    }
                }
            });

        } else {
            if (null != viewPager_adv) {
                viewPager_adv.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onPicError() {
        if (null != viewPager_adv) {
            viewPager_adv.setVisibility(View.GONE);
        }
    }


    private int pageNo = 1;
    boolean isLastPage;
    private List<InterfaceManager.SearchProductInfoEx> allDataList = new ArrayList<>();

    @Override
    public void onDataBack(final List<InterfaceManager.SearchProductInfo> result) {

//        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
//            @Override
//            public void handleMessage(Message msg) {
//                if (result.isEmpty()) {
//                    //无数据
//                    isLastPage = true;
//                } else {
//                    if (pageNo == 1){
//                        allDataList.clear();
//                    }
//                    allDataList.addAll(result);
//                    // 显示最后更新的时间
//                    listview.refreshComplete(true, new Date().getTime());
//                    if (null != goodsAdapter) {
//                        goodsAdapter.setDataList(allDataList);
//                        goodsAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//        }).sendEmptyMessage(0);

    }

    @Override
    public void onDataError() {

    }

    @Override
    public void onGoodsTJBack(final List<InterfaceManager.SearchProductInfo> result) {

//        post(new Runnable() {
//            @Override
//            public void run() {
//                if (null != layout_daily) {
//                    layout_daily.setVisibility(View.VISIBLE);
//                }
//                if (null != dailyPagerAdapter) {
//                    dailyPagerAdapter.setDataList(result);
//                    dailyPagerAdapter.notifyDataSetChanged();
//                }
//            }
//        });

    }

    @Override
    public void onGoodsTJError() {
//        post(new Runnable() {
//            @Override
//            public void run() {
//                if (null != layout_daily) {
//                    layout_daily.setVisibility(View.GONE);
//                }
//            }
//        });
    }

    @Override
    public void onHotDataBack(final List<InterfaceManager.SearchProductInfoEx> result) {

        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                if (result.isEmpty()) {
                    //无数据
                    isLastPage = true;
                } else {
                    if (pageNo == 1) {
                        allDataList.clear();
                    }
                    allDataList.addAll(result);
                    // 显示最后更新的时间
                    listview.refreshComplete(true, new Date().getTime());
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
    public void onHotDataError() {

    }

    @Override
    public void onClick(View view) {

        if (view == search_contain) {
            startActivity(new Intent(getContext(), GoodsSearchActivityEx.class));
        } else if (view == iv_rocket) {

            //滑动到顶部
            if (null != listview) {
                listview.setSelection(0);
            }
            if (null!=iv_rocket){
                iv_rocket.setVisibility(View.GONE);
            }
        } else if (view == iv_message) {

            //如果没有登录就进入到登录页
            //点击消息、每日发圈、列表分享、详情分享、立即购买、联系客服,需要登录
            if (!AccountUtil.checkLogin(getActivity())){
                return;
            }

            if (netInfoNum != 0) {
                SharedPreferencesUtil.getInstance().saveIntValue(KeyFlag.KEY_INFONUM, netInfoNum);
            }
            Intent intent = new Intent(getContext(), MessageCenterActivity.class);
            getContext().startActivity(intent);
        }

    }


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


    //公告信息列表
    List<InterfaceManager.SearchNoticeInfo> noticeInfoList;

    @Override
    public void onNoticeBack(final List<InterfaceManager.SearchNoticeInfo> result) {

        Log.d("libo", "onNoticeBack----result");

        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                if (null == result || result.isEmpty()) {
                    if (null != layout_notice) {
                        layout_notice.setVisibility(View.GONE);
                    }
                    return;
                }

                noticeInfoList = result;

                ArrayList<String> infoList = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    if (null != result.get(i) && !TextUtils.isEmpty(result.get(i).title)) {
                        infoList.add(result.get(i).title);
                    }
                }
//                List<String> info = new ArrayList<>();
//                info.add("1. 大家好，我是孙福生。");
//                info.add("2. 欢迎大家关注我哦！");
//                info.add("3. GitHub帐号：sfsheng0322");
//                info.add("4. 新浪微博：孙福生微博");
//                info.add("5. 个人博客：sunfusheng.com");
//                info.add("6. 微信公众号：孙福生");
                if (null != layout_notice) {
                    layout_notice.setVisibility(View.VISIBLE);
                }
                marqueeView.startWithList(infoList);

            }
        }).sendEmptyMessage(0);

    }

    @Override
    public void onNoticeError() {
        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                if (null != layout_notice) {
                    layout_notice.setVisibility(View.GONE);
                }
            }
        }).sendEmptyMessage(0);

    }

    RedRocketPopView redRocketPopView;

    @Override
    public void onPacketBack(final InterfaceManager.SearchRedPacketInfo redPacketInfo) {
        Log.d("libo", "onPacketBack");
        String redPacketId = SharedPreferencesUtil.getInstance().getString(GoodsContants.REDPACKET_ID);
        if (TextUtils.isEmpty(redPacketId) || (null != redPacketInfo && !redPacketId.equals(redPacketInfo.productID))) {
            //显示红包
            ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
                @Override
                public void handleMessage(Message msg) {
                    if (null == redRocketPopView) {
                        redRocketPopView = new RedRocketPopView(getContext());
                    }
                    redRocketPopView.showPopView(getActivity().getWindow().getDecorView());
                    redRocketPopView.setSearchRedPacketInfo(redPacketInfo);
                }
            }).sendEmptyMessage(0);
        }


    }

    @Override
    public void onPacketError() {
//        String redPacketId = SharedPreferencesUtil.getInstance().getString(GoodsContants.REDPACKET_ID);
//        if (TextUtils.isEmpty(redPacketId)) {
//            //显示红包
//            ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
//                @Override
//                public void handleMessage(Message msg) {
//                    if (null == redRocketPopView) {
//                        redRocketPopView = new RedRocketPopView(getContext());
//                    }
//                    redRocketPopView.showPopView(getActivity().getWindow().getDecorView());
//                    //保存红包id
//                    SharedPreferencesUtil.getInstance().saveString(GoodsContants.REDPACKET_ID,"1123456");
//                }
//            }).sendEmptyMessage(0);
//
//
//
//        }

    }

    boolean isShowOptionalTip = false;

    private static final String KEY_TIP_GOODSLIST = "KEY_TIP_GOODSLIST";

    public void showTips() {
        boolean hasShow = SharedPreferencesUtil.getInstance().getBoolean(KEY_TIP_GOODSLIST, false);
//			boolean hasShow = false;
        try {
            if (!hasShow && !isShowOptionalTip && !(getActivity().isFinishing())) {
                final Dialog dialog;
                // 设置dialog窗口展示的位置
                dialog = new Dialog(getContext(), R.style.MyDialogStyle);
                View view = LayoutInflater.from(getContext()).inflate(R.layout.view_dialog_tips_goodslist, null);
                view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(view);

//                ImageView iv_tips = view.findViewById(R.id.iv_tips);
//                iv_tips.setImageResource(R.drawable.icon_tip_goodlist);

                // 获取dialog的窗口
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.x = 0;
                params.y = 0;
                params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(params);
                dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isShowOptionalTip = false;
                        SharedPreferencesUtil.getInstance().saveBoolean(KEY_TIP_GOODSLIST, true);
                    }
                });
                dialog.show();
                isShowOptionalTip = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *     接口返回的消息数量
     */
    int netInfoNum = 0;

    @Override
    public void onAppCountBack(final int num) {
        netInfoNum = num;

        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                showMessageRed(num);

            }
        }).sendEmptyMessage(0);
    }

    @Override
    public void onAppCountError() {

    }

    private void showMessageRed(int num) {
        int preNum = SharedPreferencesUtil.getInstance().getIntValue(KeyFlag.KEY_INFONUM, 0);
        if (preNum != num) {
            //显示消息红点
            if (null != iv_message) {
                iv_message.setImageResource(R.drawable.icon_message_red);
            }

        } else {
            if (null != iv_message) {
                iv_message.setImageResource(R.drawable.icon_message);
            }
        }
    }

    @Override
    public void onJDActivityBack(final List<InterfaceManager.GetActivityJdInfo> result) {


        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                refreshJDImage(result);
            }
        }).sendEmptyMessage(0);

    }

    @Override
    public void onJDActivityError() {
        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                if (null != layout_jd) {
                    layout_jd.setVisibility(View.GONE);
                }
                if (null != view_space) {
                    view_space.setVisibility(View.VISIBLE);
                }
            }
        }).sendEmptyMessage(0);
    }

    private void refreshJDImage(List<InterfaceManager.GetActivityJdInfo> result) {

        if (null == result || result.isEmpty()) {
            if (null != layout_jd) {
                layout_jd.setVisibility(View.GONE);
            }
            if (null != view_space) {
                view_space.setVisibility(View.VISIBLE);
            }
            return;
        }

        if (null != view_space) {
            view_space.setVisibility(View.GONE);
        }
        if (null != layout_jd) {
            layout_jd.removeAllViews();
        }

        InterfaceManager.GetActivityJdInfo jdInfo = null;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.width = UIScreen.screenWidth;
        lp.height = lp.width * 2 / 5;

        Logger.getInstance(getContext()).writeLog_new("goods", "goods", "refreshJDImage---result=" + result.size());
        for (int i = 0; i < result.size(); i++) {
            jdInfo = result.get(i);

            if (null != jdInfo && jdInfo.isOpen == 1) {

                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(lp);

                Glide.with(getContext()).load(baseIP + jdInfo.pictUrl).placeholder(R.drawable.img_default_contract)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

                final InterfaceManager.GetActivityJdInfo finalJdInfo = jdInfo;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //跳转类型0=网页 1=源生
                        if (finalJdInfo.type == 0 && !TextUtils.isEmpty(finalJdInfo.url)) {
                            Intent intent = new Intent(getContext(), GoodsWebActivity.class);
                            intent.putExtra("hideTitle", false);
                            intent.putExtra("url", baseIP + finalJdInfo.url);
                            startActivity(intent);
                        } else if (finalJdInfo.type == 1) {
                            Intent intent = new Intent(getContext(), SearchJDActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                layout_jd.addView(imageView, lp);
            }

        }

    }

}
