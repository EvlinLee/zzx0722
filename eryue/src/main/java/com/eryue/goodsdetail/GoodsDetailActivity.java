package com.eryue.goodsdetail;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eryue.AccountUtil;
import com.eryue.JDManager;
import com.eryue.R;
import com.eryue.activity.CreateShareActivityEx;
import com.eryue.activity.MainActivity;
import com.eryue.mine.ContactServerActivity;
import com.eryue.util.SharedPreferencesUtil;
import com.library.util.CommonFunc;
import com.library.util.StringUtils;
import com.library.util.ToastTools;
import com.library.util.UIScreen;

import net.InterfaceManager;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;

/**
 * Created by enjoy on 2018/2/11.
 */

public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener,
        GoodsDetailPresenter.GoodsDetailListener,GoodsDetailPresenter.IGoodsStarListener,GoodsDetailPresenter.IGoodsDeleteListener,GoodsDetailPresenter.GoodsDetailListenerEx {


    private String[] titles = {"商品","详情"};


    /**
     * 商品摘要
     */
    private GoodsAbstractFragment abstractFragment;
//    private GoodsDetailWebFragment webFragment;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsdetail);
        hideNavigationBar(true);
        init();
        initData();

        showTips();
    }

    private void init(){
        abstractFragment = (GoodsAbstractFragment) getSupportFragmentManager().findFragmentById(R.id.abstractFragment);
        iv_detailback = (ImageView) findViewById(R.id.iv_detailback);
        iv_detailback.setOnClickListener(this);


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

        type = getIntent().getStringExtra("type");
        String productType = getIntent().getStringExtra("productType");

        String searchFlag = getIntent().getStringExtra("searchFlag");
        if (!TextUtils.isEmpty(type)){
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
            presenter.searchProductDetail(itemId,uid,productType,searchFlag);
        }

    }


    @Override
    public void onClick(View v) {

        if (v == iv_detailback){
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

                jumpToBuy();

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
            startActivity(intent);
        }else if (v == tv_mine){
            //我的
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("selectIndex",4);
            startActivity(intent);
        }else if (v == tv_serice){
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
        this.productDetailInfo = result;
        if (!TextUtils.isEmpty(type)&&type.equals("收藏夹")){
            //收藏夹的商品默认是收藏的
            productDetailInfo.isCollect = 1;
        }
        if (null!=abstractFragment){
            abstractFragment.refreshData(result);
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
                params.height = UIScreen.screenHeight-UIScreen.statusBarHeight;
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
        }else{
            Intent intent = new Intent(this,GoodsWebActivity.class);
            intent.putExtra("url",productDetailInfo.clickUrl);
            startActivity(intent);
        }


    }

    private void gotoPinDD(String itemId){


        boolean hasInstalled = CommonFunc.checkHasInstalledApp(this, "com.xunmeng.pinduoduo");

        if (hasInstalled){
            //https://a.toutiaonanren.com/api/d/vY9wqI
            String url = productDetailInfo.clickUrl;
            if (!TextUtils.isEmpty(url)&&url.contains("https://")){
                url = url.replace("https://","pinduoduo://");
            }
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }else{
            //跳转网页
            ToastTools.showShort(this,"跳转拼多多网页版");
            Intent intent = new Intent(this,GoodsWebActivity.class);
            intent.putExtra("url",productDetailInfo.clickUrl);
            startActivity(intent);
        }

    }
}
