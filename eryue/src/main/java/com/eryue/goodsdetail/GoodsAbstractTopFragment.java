package com.eryue.goodsdetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.GoodsContants;
import com.eryue.R;
import com.eryue.activity.BaseFragment;
import com.eryue.activity.CreateShareActivity;
import com.eryue.home.Goods;
import com.eryue.home.GoodsListAdapter;
import com.eryue.home.GoodsUtil;
import com.eryue.ui.NoScrollListView;
import com.eryue.ui.UIScrollView;
import com.eryue.util.counttime.CountDownTimerSupport;
import com.eryue.util.counttime.OnCountDownTimerListener;
import com.eryue.widget.AutoScrollViewPager.AutoScrollViewPager;
import com.library.util.CommonFunc;
import com.library.util.StringUtils;

import net.InterfaceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enjoy on 2018/2/11.
 */

public class GoodsAbstractTopFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, UIScrollView.ScrollListener, GoodsDetailPresenter.GoodsBuyOrderListener {

    /**
     * 商品广告图
     */
    private AutoScrollViewPager autoScrollViewPager;
    private ImageViewPagerAdapter pagerAdapter;

    /**
     * 商品渠道
     */
    private TextView iv_business;

    /**
     * 商品标题
     */
    private TextView tv_goodstitle;
    /**
     * 券后
     */
    private TextView tv_afterpaper;

    /**
     * 在售价
     */
    private TextView tv_nowprice;
    /**
     * 券价格
     */
    private TextView tv_paperprice;
    /**
     * 销量
     */
    private TextView tv_sellnum;

    /**
     * 积分
     */
    private TextView tv_jf;

    private ImageView iv_share;
    /**
     * 猜你喜欢
     */
    private NoScrollListView listview_like;
    private GoodsListAdapter likeListAdapter;

    private UIScrollView scrollview_root;

    private ImageView iv_rocket;


    private GoodsDetailPresenter presenter;
    //购买信息
    private TextView tv_buymessage;

    private CountDownTimerSupport timer;

    /**
     * 倒计时时间
     */
    private int CountSecond = 5000;
    /**
     * 步长
     */
    private final int CountUnit = 2000;

    int couponStatus;
    private ImageView iv_status;
    private String productType;

    //购买可得积分
    private LinearLayout layout_jf;
    private View view_line_jf;
    private boolean isLogin;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_top_goodsdetail);

        initView();
        init();
    }

    private void initView() {
        autoScrollViewPager = (AutoScrollViewPager) getView().findViewById(R.id.viewPager_goodsimg);
        listview_like = (NoScrollListView) getView().findViewById(R.id.listview_like);

        iv_business = (TextView) getView().findViewById(R.id.iv_business);
        tv_goodstitle = (TextView) getView().findViewById(R.id.tv_goodstitle);
        tv_afterpaper = (TextView) getView().findViewById(R.id.tv_afterpaper);
        tv_nowprice = (TextView) getView().findViewById(R.id.tv_nowprice);
        //中间横线
        tv_nowprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_paperprice = (TextView) getView().findViewById(R.id.tv_paperprice);
        tv_sellnum = (TextView) getView().findViewById(R.id.tv_sellnum);

        tv_jf = (TextView) getView().findViewById(R.id.tv_jf);
        iv_share = getView().findViewById(R.id.iv_share);
        iv_share.setOnClickListener(this);

        scrollview_root = getView().findViewById(R.id.scrollview_root);
        scrollview_root.setScrollListener(this);

        iv_rocket = getView().findViewById(R.id.iv_rocket);
        iv_rocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != scrollview_root) {
                    scrollview_root.smoothScrollTo(0, 0);
                }
            }
        });

        tv_buymessage = getView().findViewById(R.id.tv_buymessage);

        iv_status = getView().findViewById(R.id.iv_status);

        view_line_jf = getView().findViewById(R.id.view_line_jf);
        layout_jf = getView().findViewById(R.id.layout_jf);

    }

    private void init() {
//        List<String> dataList = new ArrayList<>();
//        dataList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1518336901046&di=3421d4583323f2fb3f7ce821b450947c&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Flarge%2F887790fagw1f4qhnk6bjsj20sg0lcad4.jpg");
//        dataList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1518336901046&di=3421d4583323f2fb3f7ce821b450947c&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Flarge%2F887790fagw1f4qhnk6bjsj20sg0lcad4.jpg");
        pagerAdapter = new ImageViewPagerAdapter<GoodsImageModel>(getContext());
        autoScrollViewPager.setAdapter(pagerAdapter);


        likeListAdapter = new GoodsListAdapter(getContext());
//        String[] horizontalArray = {"加厚高领修身羊毛衫","2018春季新款针织衫","2017新款韩版百搭衫","韩版2017秋冬新款百搭","2017秋冬新款加厚羊毛","半高领羊毛女秋冬毛衣",
//        "花愿秋冬新韩版毛衣","时尚宽松刺绣棒球外套"};
//        List<String> horizontalList = new ArrayList<>();
//        for (int i=0;i<horizontalArray.length;i++){
//            horizontalList.add(horizontalArray[i]);
//        }
//        likeListAdapter.setDataList(horizontalList);
        listview_like.setAdapter(likeListAdapter);
        listview_like.setOnItemClickListener(this);

        presenter = new GoodsDetailPresenter();
        presenter.setGoodsBuyOrderListener(this);

        couponStatus = getActivity().getIntent().getIntExtra("couponStatus",0);
        productType = getActivity().getIntent().getStringExtra("productType");

        isLogin = AccountUtil.isLogin();

        if (!isLogin){
            layout_jf.setVisibility(View.GONE);
            view_line_jf.setVisibility(View.GONE);
        }
    }


    InterfaceManager.SearchProductDetailInfoEx detailInfo;

    public void refreshData(final InterfaceManager.SearchProductDetailInfoEx detailInfo) {
        this.detailInfo = detailInfo;

        //刷新数据
        post(new Runnable() {
            @Override
            public void run() {
                pagerAdapter.setData(detailInfo.pics);
                if (null != likeListAdapter) {
                    likeListAdapter.setDataList(detailInfo.sameCatProducts);
                    likeListAdapter.notifyDataSetChanged();
                }
                //商品渠道
                tv_goodstitle.setText(detailInfo.itemTitle);
                tv_afterpaper.setText("¥" + CommonFunc.fixText(detailInfo.afterQuan, 2));
                tv_nowprice.setText("¥" + CommonFunc.fixText(detailInfo.discountPrice, 2));
                tv_paperprice.setText(CommonFunc.fixText(detailInfo.quanPrice, 0));
                tv_sellnum.setText("月销" + String.valueOf(detailInfo.soldQuantity));
                tv_jf.setText("购买可得" + CommonFunc.fixText(detailInfo.jf, 2) + "积分");

                //商品状态
                Glide.with(getContext()).load(GoodsUtil.getGoodsStatusRid(true,detailInfo.couponStatus)).asBitmap().into(iv_status);
                //设置商品渠道
                iv_business.setText(GoodsUtil.getGoodsBusinessRid(detailInfo.isMall, detailInfo.productType).toString());

            }
        });

        if (!TextUtils.isEmpty(detailInfo.itemId)) {
            if (null != presenter) {
                presenter.searchDetailsOrderReq(detailInfo.itemId);
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null == detailInfo || null == detailInfo.sameCatProducts || detailInfo.sameCatProducts.isEmpty()) {
            return;
        }
        InterfaceManager.SearchProductInfoEx sameCatProductInfo = null;
        if (position < detailInfo.sameCatProducts.size()) {
            sameCatProductInfo = detailInfo.sameCatProducts.get(position);
        }
        if (null != sameCatProductInfo && !TextUtils.isEmpty(sameCatProductInfo.itemId)) {
            Intent intent = new Intent(getContext(), GoodsDetailActivityEx.class);
            intent.putExtra("itemId", sameCatProductInfo.itemId);
            intent.putExtra("productType", sameCatProductInfo.productType);
            intent.putExtra("searchFlag", sameCatProductInfo.searchFlag);
            getContext().startActivity(intent);
        }

    }

    @Override
    public void onClick(View view) {
        //分享
        if (null != detailInfo && !TextUtils.isEmpty(detailInfo.itemId)) {
            Intent intent = getActivity().getIntent();
            intent.setClass(getContext(), CreateShareActivity.class);
//                intent.putExtra("itemId",productDetailInfo.itemId);
            startActivity(intent);
        }
    }

    boolean isRocketShow = false;

    @Override
    public void onScrollChanged() {
        int scrollY = scrollview_root.getScrollY();
        if (scrollY > StringUtils.dipToPx(70)) {

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
        Log.d("libo", "onScrollChanged scrollY:" + scrollview_root.getScrollY());
    }

    @Override
    public void onGoodsOrderBack(final List<InterfaceManager.SearchDetailOrderInfo> result) {
        if (null == result || result.isEmpty()) {
            return;
        }

        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                showTimeText(result);
            }
        }).sendEmptyMessage(0);

    }

    @Override
    public void onGoodsOrderError() {
//        final List<InterfaceManager.SearchDetailOrderInfo> result = new ArrayList<>();
//        InterfaceManager.SearchDetailOrderInfo orderInfo = new InterfaceManager.SearchDetailOrderInfo();
//        orderInfo.integralPrividor = "张三";
//        result.add(orderInfo);
//        orderInfo = new InterfaceManager.SearchDetailOrderInfo();
//        orderInfo.integralPrividor = "李四";
//        result.add(orderInfo);
//        orderInfo = new InterfaceManager.SearchDetailOrderInfo();
//        orderInfo.integralPrividor = "王五";
//        result.add(orderInfo);
//        orderInfo = new InterfaceManager.SearchDetailOrderInfo();
//        orderInfo.integralPrividor = "许三多";
//        result.add(orderInfo);
//        orderInfo = new InterfaceManager.SearchDetailOrderInfo();
//        orderInfo.integralPrividor = "王宝强";
//        result.add(orderInfo);
//
//
//        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
//            @Override
//            public void handleMessage(Message msg) {
//                showTimeText(result);
//            }
//        }).sendEmptyMessage(0);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isTimeEnd&&null != timer) {
            timer.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != timer) {
            timer.pause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != timer) {
            timer.stop();
        }
    }

    boolean isTimeEnd = false;

    private void showTimeText(final List<InterfaceManager.SearchDetailOrderInfo> result){
        if (null == timer) {
            CountSecond = result.size() * CountUnit;
            timer = new CountDownTimerSupport();
            timer.setMillisInFuture(CountSecond + 50);
            timer.setCountDownInterval(CountUnit);
            timer.setOnCountDownTimerListener(new OnCountDownTimerListener() {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (null != tv_buymessage) {
                        tv_buymessage.setVisibility(View.VISIBLE);

                        int index = (int) Math.round(millisUntilFinished / CountUnit);
                        index = result.size() - index;
                        if (index>=0&&index<result.size()){
                            String message = result.get(index).integralPrividor+"购买了该商品";
                            tv_buymessage.setText(message);
                        }
                    }
                }

                @Override
                public void onFinish() {
                    isTimeEnd = true;
                    if (null != tv_buymessage) {
                        tv_buymessage.setVisibility(View.GONE);
                    }
                }


            });

        }
        if (null!=timer){
            if (!timer.isStart()){
                timer.start();
            }
        }

    }



}
