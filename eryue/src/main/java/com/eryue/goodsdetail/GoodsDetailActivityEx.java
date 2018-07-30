package com.eryue.goodsdetail;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.AlibcConstants;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.eryue.AccountUtil;
import com.eryue.BaseApplication;
import com.eryue.GoodsContants;
import com.eryue.JDManager;
import com.eryue.R;
import com.eryue.activity.CreateShareActivityEx;
import com.eryue.activity.MainActivity;
import com.eryue.jd.SearchJDFragment;
import com.eryue.jd.SearchJDPresenter;
import com.eryue.mine.ContactServerActivity;
import com.eryue.mine.login.LoginActivity1;
import com.eryue.util.Logger;
import com.eryue.util.SharedPreferencesUtil;
import com.eryue.util.StatusBarCompat;
import com.library.util.CommonFunc;
import com.library.util.ToastTools;

import net.InterfaceManager;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import base.BaseActivity;
import base.BaseActivityTransparent;

/**
 * Created by enjoy on 2018/2/11.
 */

public class GoodsDetailActivityEx extends BaseActivityTransparent implements View.OnClickListener,
        GoodsDetailPresenter.GoodsDetailListener,GoodsDetailPresenter.IGoodsStarListener,GoodsDetailPresenter.IGoodsDeleteListener,GoodsDetailPresenter.GoodsDetailListenerEx,GoodsDetailPresenter.IGoodsImageListener,SearchJDPresenter.SearchJDDetailListener {


    private String[] titles = {"商品","详情"};


    /**
     * 商品摘要
     */
    private GoodsDetailFragmentEx detailFragmentEx;
//    private GoodsDetailWebFragment webFragment;

    private RelativeLayout layout_back;
    private ImageView iv_detailback;

    /**
     * 领券购买
     */
    private LinearLayout layout_buy;

    /**
     * 收藏
     */
    private LinearLayout layout_start;
    private TextView tv_detail_star;
    /**
     * 分享
     */
    private LinearLayout layout_share;

    Drawable ic_star;
    Drawable ic_star_selected;

    private TextView tv_home;
    private TextView tv_mine;
    private TextView tv_serice;
    private TextView tv_detail_share;
    private TextView tv_buy;
    private String uid = AccountUtil.getUID();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(this,  Color.TRANSPARENT, Color.TRANSPARENT);
        super.onCreate(savedInstanceState);
        hideNavigationBar(true);
        setContentView(R.layout.activity_goodsdetail);
        init();
        initData();

        showTips();
    }

    private void init(){
        detailFragmentEx = (GoodsDetailFragmentEx) getSupportFragmentManager().findFragmentById(R.id.abstractFragment);
        iv_detailback = (ImageView) findViewById(R.id.iv_detailback);
        layout_back = findViewById(R.id.layout_back);
        layout_back.setOnClickListener(this);


        tv_home = findViewById(R.id.tv_home);
        tv_mine = findViewById(R.id.tv_mine);
        tv_serice = findViewById(R.id.tv_serice);
        tv_detail_share = findViewById(R.id.tv_detail_share);
        tv_buy = findViewById(R.id.tv_buy);

        tv_home.setOnClickListener(this);
        tv_mine.setOnClickListener(this);
        tv_serice.setOnClickListener(this);
        tv_detail_share.setOnClickListener(this);
        tv_buy.setOnClickListener(this);

    }


    private GoodsDetailPresenter presenter;

    private SearchJDPresenter jdPresenter;

    String type;
    private void initData(){
        //页面，数据源
        List<String> tabStrList = new ArrayList<>();
        for (int i=0;i<titles.length;i++){
            tabStrList.add(titles[i]);
        }

        String itemId = getIntent().getStringExtra("itemId");
        if (TextUtils.isEmpty(itemId)){
            return;
        }

        presenter = new GoodsDetailPresenter();
        presenter.setGoodsDetailListener(this);
        presenter.setGoodsDetailListenerEx(this);
        presenter.setImageListener(this);

        jdPresenter = new SearchJDPresenter();
        jdPresenter.setJdDetailListener(this);


        type = getIntent().getStringExtra("type");
        String productType = getIntent().getStringExtra("productType");

        String searchFlag = getIntent().getStringExtra("searchFlag");

        String jdType = getIntent().getStringExtra("jdtype");
        if (!TextUtils.isEmpty(type)){
            showProgressMum();
            if (type.equals("指定搜索")){
                //指定搜索
                presenter.superSearchProductDetail(itemId,uid,searchFlag);
            }else if (type.equals("好券优选")){
                presenter.searchProductYxDetail(itemId,uid);
            }else if (type.equals("人气宝贝")||type.equals("爆款单")||type.equals("好货疯抢")){
                //人气宝贝详情页面接口(人气宝贝、爆款单、好货疯抢)
                presenter.searchTbActivityProductDetail(itemId,uid);
            }else if (type.equals("品牌爆款")){
                //品牌爆款
                presenter.searchProductBrandDetail(itemId,uid);
            }else if (type.equals("聚划算")||type.equals("淘抢购")||type.equals("视频购物")){
                //聚划算详情页接口(聚划算、淘抢购、视频购物)
                presenter.searchTbActivityProductDetail(itemId,uid);
            }else if (type.equals("收藏夹")){
                //收藏夹
                presenter.searchProcutCollectionDetail(itemId,uid);
            }else{
                presenter.searchProductDetail(itemId,uid);
            }
        }else{
            showProgressMum();

            if (!TextUtils.isEmpty(jdType)&&jdType.equals(SearchJDFragment.JDFREE)){
                //京东免单
                jdPresenter.requestSearchJdDetail(itemId,uid);


            }else{
                presenter.searchProductDetail(itemId,uid,productType,searchFlag);

                //请求商品图片
                String url = GoodsContants.taobaoUrl.replace("itemId",itemId);
                presenter.requestGoodsImg(url);
            }

        }

    }


    @Override
    public void onClick(View v) {

        if (v == layout_back){
            this.finish();
        }else if (v == tv_buy){
            if (null!=productDetailInfo&& !TextUtils.isEmpty(productDetailInfo.clickUrl)){

//                if (CommonFunc.checkPackage("com.taobao.taobao",this)){
//                    String url = "https://item.taobao.com/item.htm?id=539789035577&ali_refid=a3_430406_1007:1124066525:N:485184283370953001_0_100:d45485b3013535b0cc4164b7cd5b7523&ali_trackid=1_d45485b3013535b0cc4164b7cd5b7523&spm=a21bo.50862.201874-sales.8.UYm99R";
////                    if(url.contains("https")){
////                        url = url.replace("https","taobao");
////                    }else if (url.contains("http")){
////                        url = url.replace("http","taobao");
////                    }
//                    Intent intent = new Intent();
//                    intent.setAction("android.intent.action.VIEW");
//                    Uri uri = Uri.parse(url);
//                    intent.setData(uri);
//                    intent.setClassName("com.taobao.taobao", "com.taobao.tao.detail.activity.DetailActivity");
//                    startActivity(intent);
//                }else{
//                    Intent intent = new Intent();
//                    intent.setAction("android.intent.action.VIEW");
//                    Uri uri = Uri.parse(productDetailInfo.clickUrl);
//                    intent.setData(uri);
//                intent.setClassName("com.taobao.taobao", "com.taobao.tao.detail.activity.DetailActivity");
//                    startActivity(intent);
//                }

//                Intent intent = new Intent(this,GoodsWebActivity.class);
//                intent.putExtra("url",productDetailInfo.clickUrl);
//                startActivity(intent);

//                String url1 = "file:///android_asset/test.html";
//                String url1 = "content://com.android.htmlfileprovider/sdcard/test.html";
//                        Intent intent = new Intent();
//        intent.setAction("android.intent.action.VIEW");
//        Uri content_url = Uri.parse(productDetailInfo.clickUrl);
//        intent.setData(content_url);
//        startActivity(intent);
//        intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");

                //如果没有登录就进入到登录页
                //点击消息、每日发圈、列表分享、详情分享、立即购买、联系客服,需要登录
                if (!AccountUtil.checkLogin(this)){
                    return;
                }

                jumpToBuy();

            }else{
                ToastTools.showShort(this,"该产品已下架");
            }

        }else if (v == layout_start){
            //收藏
            if (null!=presenter&&null!=productDetailInfo){
                if (productDetailInfo.isCollect == 0){
                    presenter.addProcutCollection(productDetailInfo);
                    presenter.setGoodsStarListener(this);
                }else{
                    List<InterfaceManager.SearchProductDetailInfoEx> dataList = new ArrayList<>();
                    dataList.add(productDetailInfo);
                    presenter.deleteProcutCollection(dataList);
                    presenter.setGoodsDeleteListener(this);
                }
            }
        }else if (v == tv_detail_share){

            //如果没有登录就进入到登录页
            //点击消息、每日发圈、列表分享、详情分享、立即购买、联系客服,需要登录
            if (!AccountUtil.checkLogin(this)){
                return;
            }
            //分享
            if (null!=productDetailInfo&&!TextUtils.isEmpty(productDetailInfo.itemId)){
                Intent intent = getIntent();
                intent.setClass(this,CreateShareActivityEx.class);
                intent.putExtra("itemId",productDetailInfo.itemId);
                startActivity(intent);
            }
        }else if (v == tv_home){
            //首页
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("selectIndex",MainActivity.TAB_HOME);
            startActivity(intent);
        }else if (v == tv_mine){
            //我的
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("selectIndex",MainActivity.TAB_MINE);
            startActivity(intent);
        }else if (v == tv_serice){
            //如果没有登录就进入到登录页
            //点击消息、每日发圈、列表分享、详情分享、立即购买、联系客服,需要登录
            if (!AccountUtil.checkLogin(this)){
                return;
            }
            //客服
            Intent intent = new Intent(this.getBaseContext(), ContactServerActivity.class);
            startActivity(intent);
        }

    }

    public String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        }
        catch (Exception localException) {
        }

        return "";
    }




    InterfaceManager.SearchProductDetailInfoEx productDetailInfo;
    @Override
    public void onGoodsDetailBack(InterfaceManager.SearchProductDetailInfo result) {
//        this.productDetailInfo = result;
//        if (!TextUtils.isEmpty(type)&&type.equals("收藏夹")){
//            //收藏夹的商品默认是收藏的
//            productDetailInfo.isCollect = 1;
//        }
//        if (null!=tv_quanPrice){
//            tv_quanPrice.setText("省¥"+CommonFunc.fixText(result.quanPrice));
//        }
//        if (null!=abstractFragment){
//            abstractFragment.refreshData(result);
//        }
//
//        Drawable ic_star = getResources().getDrawable(R.drawable.ic_star);
//        Drawable ic_star_selected = getResources().getDrawable(R.drawable.ic_star_selected);
//        ic_star.setBounds(0, 0, ic_star.getMinimumWidth(), ic_star.getMinimumHeight());//对图片进行压缩
//        ic_star_selected.setBounds(0, 0, ic_star_selected.getMinimumWidth(), ic_star_selected.getMinimumHeight());//对图片进行压缩
//        if (productDetailInfo.isCollect == 1){
//            if (null!=tv_detail_star){
//                tv_detail_star.setCompoundDrawables(ic_star_selected,null,null,null);
//            }
//        }else{
//            if (null!=tv_detail_star){
//                tv_detail_star.setCompoundDrawables(ic_star,null,null,null);
//            }
//        }




    }

    @Override
    public void onGoodsDetailError() {

    }

    @Override
    public void onGoodsStarBack(int status) {
        //收藏成功
        ToastTools.showShort(this,"收藏成功");
        productDetailInfo.isCollect = 1;
        if (null!=tv_detail_star){
            tv_detail_star.setCompoundDrawables(ic_star_selected,null,null,null);
        }

    }

    @Override
    public void onGoodsStarError() {
        //收藏失败
        ToastTools.showShort(this,"收藏失败");
        productDetailInfo.isCollect = 0;
        if (null!=tv_detail_star){
            tv_detail_star.setCompoundDrawables(ic_star,null,null,null);
        }

    }

    @Override
    public void onGoodsDeleteBack(int status) {
        //取消收藏成功
        ToastTools.showShort(this,"取消收藏成功");
        productDetailInfo.isCollect = 0;
        if (null!=tv_detail_star){
            tv_detail_star.setCompoundDrawables(ic_star,null,null,null);
        }

    }

    @Override
    public void onGoodsDeleteError() {
        //取消收藏失败
        ToastTools.showShort(this,"取消收藏失败");

    }

    @Override
    public void onGoodsDetailBackEx(InterfaceManager.SearchProductDetailInfoEx result) {
        hideProgressMum();
        this.productDetailInfo = result;
        if (!TextUtils.isEmpty(type)&&type.equals("收藏夹")){
            //收藏夹的商品默认是收藏的
            productDetailInfo.isCollect = 1;
        }
        if (null!=detailFragmentEx){
            detailFragmentEx.refreshData(result);
        }

        Drawable ic_star = getResources().getDrawable(R.drawable.ic_star);
        Drawable ic_star_selected = getResources().getDrawable(R.drawable.ic_star_selected);
        ic_star.setBounds(0, 0, ic_star.getMinimumWidth(), ic_star.getMinimumHeight());//对图片进行压缩
        ic_star_selected.setBounds(0, 0, ic_star_selected.getMinimumWidth(), ic_star_selected.getMinimumHeight());//对图片进行压缩
        if (productDetailInfo.isCollect == 1){
            if (null!=tv_detail_star){
                tv_detail_star.setCompoundDrawables(ic_star_selected,null,null,null);
            }
        }else{
            if (null!=tv_detail_star){
                tv_detail_star.setCompoundDrawables(ic_star,null,null,null);
            }
        }

    }

    @Override
    public void onGoodsDetailErrorEx() {
        hideProgressMum();

    }



    boolean isShowOptionalTip = false;

    private static final String KEY_OPTION_TIP = "KEY_TIP_GOODSDETAIL";

    public void showTips() {
        boolean hasShow = SharedPreferencesUtil.getInstance().getBoolean(KEY_OPTION_TIP, false);
//			boolean hasShow = false;
        try {
            if (!hasShow && !isShowOptionalTip) {
                final Dialog dialog;
                // 设置dialog窗口展示的位置
                dialog = new Dialog(this, R.style.MyDialogStyle);
                View view = LayoutInflater.from(this).inflate(R.layout.view_dialog_tips, null);
                view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(view);
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
                        SharedPreferencesUtil.getInstance().saveBoolean(KEY_OPTION_TIP, true);
                    }
                });
                dialog.show();
//                isShowOptionalTip = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private  void jumpToBuy(){
        String productType = getIntent().getStringExtra("productType");
        if (null == productDetailInfo||TextUtils.isEmpty(productType)){
            return;
        }
        //拼多多
        if(productType.equals("pdd")){
            gotoPinDD(productDetailInfo.itemId);
        }else if(productType.equals("jd")){
            JDManager.getInstance().openWebView(this,productDetailInfo.clickUrl);
        }else if (productType.equals("tb")||productType.equals("tbActivity")){
//            boolean hasInstalled = CommonFunc.checkHasInstalledApp(this, "com.taobao.taobao");
//            if (hasInstalled){
                gotoTaoBao();
//            }else{
//                Intent intent = new Intent(this,GoodsWebActivity.class);
//                intent.putExtra("url",productDetailInfo.clickUrl);
//                startActivity(intent);
//            }

        }else{
            Intent intent = new Intent(this,GoodsWebActivity.class);
            intent.putExtra("url",productDetailInfo.clickUrl);
            startActivity(intent);
        }


    }

    private void gotoPinDD(String itemId){


        boolean hasInstalled = CommonFunc.checkHasInstalledApp(this, "com.xunmeng.pinduoduo");

        if (hasInstalled){
//            https://a.toutiaonanren.com/api/d/vY9wqI


//            String url = productDetailInfo.clickUrl;
//            if (!TextUtils.isEmpty(url)){
//                if (url.startsWith("https://")){
//                    url = url.replace("https://","pinduoduo://");
//                }else if (url.startsWith("http://")){
//                    url = url.replace("http://","pinduoduo://");
//                }
//            }

            boolean isPddChecked = SharedPreferencesUtil.getInstance().getBoolean(
                    GoodsContants.SWITCH_PDD, false);
            String url;
            //https://mobile.yangkeduo.com/duo_coupon_landing.html?goods_id=976446256&pid=40001_175693&t=1528550270020&sign=EGGEDIZzRDHKMa4NpHQFSOZCeI8rzu-2OL91CMr923k%3D&duoduo_type=3
            url = productDetailInfo.clickUrl;
            if (!TextUtils.isEmpty(url)) {
                if (url.startsWith("https://mobile.yangkeduo.com")) {
                    url = url.replace("https://mobile.yangkeduo.com", "pinduoduo://com.xunmeng.pinduoduo");
                } else if (url.startsWith("http://mobile.yangkeduo.com")) {
                    url = url.replace("http://mobile.yangkeduo.com", "pinduoduo://com.xunmeng.pinduoduo");
                }
            }
            if (!TextUtils.isEmpty(url)){
                Logger.getInstance(BaseApplication.getInstance()).writeLog_new("pdd","pdd","gotoPinDD-----url="+url);
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        }else{
            //跳转网页
            Intent intent = new Intent(this,GoodsWebActivity.class);
            intent.putExtra("url",productDetailInfo.clickUrl);
            startActivity(intent);
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(productDetailInfo.clickUrl));
//            startActivity(intent);
        }

    }

    private void gotoTaoBao(){


        /**
         *  打开电商组件,支持使用外部webview
         * @param activity             必填
         * @param webView              外部 webView
         * @param webViewClient        webview的webViewClient
         * @param webChromeClient      webChromeClient客户端
         * @param tradePage            页面类型,必填，不可为null，详情见下面tradePage类型介绍
         * @param showParams           show参数
         * @param taokeParams          淘客参数
         * @param trackParam           yhhpass参数
         * @param tradeProcessCallback 交易流程的回调，必填，不允许为null；
         * @return 0标识跳转到手淘打开了,1标识用h5打开,-1标识出错
         */
        //商品详情page
        AlibcBasePage detailPage = new AlibcPage(productDetailInfo.clickUrl);
        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);
//        showParams.setClientType("taobao_scheme");

//        case TAOBAO:
//        alibcShowParams = new AlibcShowParams(OpenType.Native, false);
//        alibcShowParams.setClientType("taobao_scheme");
//        break;
//        case TMALL:
//        alibcShowParams = new AlibcShowParams(OpenType.Native, false);
//        alibcShowParams.setClientType("tmall_scheme");
//        break;

        HashMap<String, String> exParams = new HashMap<>();
        // 固定写法
        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");

        // 若非淘客taokeParams设置为null即可   mm_memberId_siteId_adzoneId
        // pid:广告位id
//        AlibcTaokeParams alibcTaokeParams = new AlibcTaokeParams();
//        // adzoneid 为mm_memberId_siteId_adzoneId最后一位
//        alibcTaokeParams.adzoneid = "33333";
//        alibcTaokeParams.pid = "mm_11111_22222_33333";
//        alibcTaokeParams.subPid = "mm_11111_22222_33333";
//        alibcTaokeParams.extraParams = new HashMap<>();
//        alibcTaokeParams.extraParams.put("taokeAppkey", "xxxxxxx");

        int result = AlibcTrade.show(this, detailPage, showParams, null, exParams, new AlibcTradeCallback() {

            @Override
            public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                Log.d("libo","onTradeSuccess");
                //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）

            }

            @Override
            public void onFailure(int code, String msg) {
                Log.d("libo","onFailure--code="+code+"---msg="+msg);
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
            }
        });
        Log.d("libo","result--"+result);

    }


//    public void showPage(View view) {
//        Map<String, String> exParams = new HashMap<String, String>();
//        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
//        Page page = new Page(url, exParams);
//        AlibabaSDK.getService(TradeService.class).show(page, null, this, null, new TradeProcessCallback(){
//            @Override
//            public void onPaySuccess(TradeResult tradeResult) {
//            }
//            @Override
//            public void onFailure(int code, String msg) {
//
//            }
//        });
//    }


    @Override
    public void onImageBack(List<String> images) {

        if (null!=detailFragmentEx){
            detailFragmentEx.refreshImageList(images);
        }

    }

    @Override
    public void onImageError() {
        if (null!=detailFragmentEx){
            detailFragmentEx.refreshImageList(null);
        }

    }

    @Override
    public void onSearchJDDetailBack(InterfaceManager.SearchProductDetailInfoEx result) {
        hideProgressMum();
        this.productDetailInfo = result;
        if (!TextUtils.isEmpty(type)&&type.equals("收藏夹")){
            //收藏夹的商品默认是收藏的
            productDetailInfo.isCollect = 1;
        }
        if (null!=detailFragmentEx){
            detailFragmentEx.refreshData(result);
        }

        Drawable ic_star = getResources().getDrawable(R.drawable.ic_star);
        Drawable ic_star_selected = getResources().getDrawable(R.drawable.ic_star_selected);
        ic_star.setBounds(0, 0, ic_star.getMinimumWidth(), ic_star.getMinimumHeight());//对图片进行压缩
        ic_star_selected.setBounds(0, 0, ic_star_selected.getMinimumWidth(), ic_star_selected.getMinimumHeight());//对图片进行压缩
        if (productDetailInfo.isCollect == 1){
            if (null!=tv_detail_star){
                tv_detail_star.setCompoundDrawables(ic_star_selected,null,null,null);
            }
        }else{
            if (null!=tv_detail_star){
                tv_detail_star.setCompoundDrawables(ic_star,null,null,null);
            }
        }
    }

    @Override
    public void onSearchJDDetailError() {
        hideProgressMum();

    }
}
