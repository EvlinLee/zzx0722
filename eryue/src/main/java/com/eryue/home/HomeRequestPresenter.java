package com.eryue.home;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.eryue.AccountUtil;
import com.eryue.video.VideoRequestPresenter;
import com.library.util.StringUtils;

import net.DataCenterManager;
import net.InterfaceManager;
import net.KeyFlag;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static net.KeyFlag.KEY_DOMAIN;
import static net.KeyFlag.KEY_DOMAIN_INPUT;

/**
 * Created by enjoy on 2018/2/25.
 */

public class HomeRequestPresenter{

    /**
     * 海报
     */
    HomeRequestPosterListener posterListener;

    /**
     * 公告
     */
    NoticeListener noticeListener;
    /**
     * 商品列表
     */
    HomeRequestGoodsListener goodsListener;

    /**
     * 今日值得买
     */
    GoodsTJListener goodsTJListener;

    /**
     * 热门推荐
     */
    HomHotDataListener hotDataListener;

    /**
     * app消息数量
     */
    AppInfoCountListener appInfoCountListener;

    private String baseIP = AccountUtil.getBaseIp();
    private long uid = StringUtils.valueOfLong(AccountUtil.getUID());

    public void setPosterListener(HomeRequestPosterListener posterListener) {
        this.posterListener = posterListener;
    }

    public void setGoodsListener(HomeRequestGoodsListener goodsListener) {
        this.goodsListener = goodsListener;
    }

    public void setGoodsTJListener(GoodsTJListener goodsTJListener) {
        this.goodsTJListener = goodsTJListener;
    }


    public void setHotDataListener(HomHotDataListener hotDataListener) {
        this.hotDataListener = hotDataListener;
    }

    public void setNoticeListener(NoticeListener noticeListener) {
        this.noticeListener = noticeListener;
    }

    public void setAppInfoCountListener(AppInfoCountListener appInfoCountListener) {
        this.appInfoCountListener = appInfoCountListener;
    }

    public void onResume(){
        baseIP = AccountUtil.getBaseIp();
        uid = StringUtils.valueOfLong(AccountUtil.getUID());
    }

    public String getBaseIP() {
        return baseIP;
    }

    // 商品查询
    public void searchProduct(int page, String itemTitle) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        searchProduct(page,itemTitle,null,null);
    }

    // 好券搜索接口(好券搜索、首页分类、今日值得买、今日上新、9.9包邮、上百券)
    public void searchProduct(int page, String itemTitle, String Sidx, String cat) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchProductReq callFunc = retrofit.create(InterfaceManager.SearchProductReq.class);
        Call<InterfaceManager.SearchProductResponse> call = callFunc.get(page, itemTitle, Sidx, cat);
        call.enqueue(new Callback<InterfaceManager.SearchProductResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchProductResponse> call, Response<InterfaceManager.SearchProductResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=goodsListener){
                        goodsListener.onDataBack(response.body().result);
                    }
                }else{
                    if (null!=goodsListener){
                        goodsListener.onDataError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchProductResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=goodsListener){
                    goodsListener.onDataError();
                }
            }
        });
    }

    // 获取滚动海报
    public void getServerConfig(final Context context) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//                baseIP = "http://www.371d.cn/";
        InterfaceManager.GetServerConfig getServerConfig = retrofit.create(InterfaceManager.GetServerConfig.class);
        Call<InterfaceManager.ServerConfigResponse> call = getServerConfig.get();
        call.enqueue(new Callback<InterfaceManager.ServerConfigResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.ServerConfigResponse> call, Response<InterfaceManager.ServerConfigResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=posterListener){
                        posterListener.onPosterBack(response.body().result.banners);
                    }
                    InterfaceManager.ServerConfigResponse.ServerInfo serverInfo = response.body().result;
                    if (serverInfo != null) {
                        String serverURL = serverInfo.domain + "/";

                        InterfaceManager.info1 = serverInfo.regInfo;
                        InterfaceManager.info2 = serverInfo.regPlatformInfo;

                        //保存baseIp
                        //用户输入的邀请码
                        String inviteInputCode = DataCenterManager.Instance().get(context, KeyFlag.INVITE_CODE_KEY_INPUT);
                        //用户登录后返回的邀请码
                        String inviteCode = DataCenterManager.Instance().get(context, KeyFlag.INVITE_CODE_KEY);
                        if (!TextUtils.isEmpty(inviteCode)){
                            DataCenterManager.Instance().save(context, KEY_DOMAIN, serverURL);
                        }else if (!TextUtils.isEmpty(inviteInputCode)){
                            DataCenterManager.Instance().save(context, KEY_DOMAIN_INPUT, serverURL);
                        }
                    }
                }else{
                    if (null!=posterListener){
                        posterListener.onPosterError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.ServerConfigResponse> call, Throwable t) {
                if (null!=posterListener){
                    posterListener.onPosterError();
                }
                t.printStackTrace();
            }
        });
    }



    // 首页今日值得买查询接口
    public void searchProductTj() {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchProductTjReq callFunc = retrofit.create(InterfaceManager.SearchProductTjReq.class);
        Call<InterfaceManager.SearchProductTjResponse> call = callFunc.get();
        call.enqueue(new Callback<InterfaceManager.SearchProductTjResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchProductTjResponse> call, Response<InterfaceManager.SearchProductTjResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=goodsTJListener){
                        goodsTJListener.onGoodsTJBack(response.body().result);
                    }
                }else{
                    if (null!=goodsTJListener){
                        goodsTJListener.onGoodsTJError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchProductTjResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=goodsTJListener){
                    goodsTJListener.onGoodsTJError();
                }
            }
        });
    }


    // 9.9包邮接口
    public void searchProduct99Req(int page, String itemTitle, String Sidx, String cat) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchProduct99Req callFunc = retrofit.create(InterfaceManager.SearchProduct99Req.class);
        Call<InterfaceManager.SearchProduct99Response> call = callFunc.get(page, itemTitle, Sidx, cat);
        call.enqueue(new Callback<InterfaceManager.SearchProduct99Response>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchProduct99Response> call, Response<InterfaceManager.SearchProduct99Response> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=goodsListener){
                        goodsListener.onDataBack(response.body().result);
                    }
                }else{
                    if (null!=goodsListener){
                        goodsListener.onDataError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchProduct99Response> call, Throwable t) {
                t.printStackTrace();
                if (null!=goodsListener){
                    goodsListener.onDataError();
                }
            }
        });
    }

    // 好券优选查询接口
    public void searchProductYxReq(int page, String Sidx) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchProductYxReq callFunc = retrofit.create(InterfaceManager.SearchProductYxReq.class);
        Call<InterfaceManager.SearchProductYxResponse> call = callFunc.get(page, Sidx);
        call.enqueue(new Callback<InterfaceManager.SearchProductYxResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchProductYxResponse> call, Response<InterfaceManager.SearchProductYxResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=goodsListener){
                        goodsListener.onDataBack(response.body().result);
                    }
                }else{
                    if (null!=goodsListener){
                        goodsListener.onDataError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchProductYxResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=goodsListener){
                    goodsListener.onDataError();
                }
            }
        });
    }

    /**
     * 视频购物
     */
    VideoRequestPresenter.VideoListListener videoListListener;

    public void setVideoListListener(VideoRequestPresenter.VideoListListener videoListListener) {
        this.videoListListener = videoListListener;
    }

    // 视频购物列表
    public void searchProductVideo(int page,String sidx) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.searchProductVideoReq callFunc = retrofit.create(InterfaceManager.searchProductVideoReq.class);
        Call<InterfaceManager.searchProductVideoResponse> call = callFunc.get(page, sidx);
        call.enqueue(new Callback<InterfaceManager.searchProductVideoResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.searchProductVideoResponse> call, Response<InterfaceManager.searchProductVideoResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=goodsListener){
                        goodsListener.onDataBack(response.body().result);
                    }
                }else{
                    if (null!=goodsListener){
                        goodsListener.onDataError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.searchProductVideoResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=goodsListener){
                    goodsListener.onDataError();
                }
            }
        });
    }

    // 人气宝贝列表接口(人气宝贝、爆款单、好货疯抢)
    public void searchProductTop(int page,String sidx) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchProductTopReq callFunc = retrofit.create(InterfaceManager.SearchProductTopReq.class);
        Call<InterfaceManager.SearchProductTopResponse> call = callFunc.get(page, sidx,null);
        call.enqueue(new Callback<InterfaceManager.SearchProductTopResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchProductTopResponse> call, Response<InterfaceManager.SearchProductTopResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=goodsListener){
                        goodsListener.onDataBack(response.body().result);
                    }
                }else{
                    if (null!=goodsListener){
                        goodsListener.onDataError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchProductTopResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=goodsListener){
                    goodsListener.onDataError();
                }
            }
        });
    }

    // 品牌爆款
    public void searchProductBrand(int page,String sidx) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchProductTopBrandReq callFunc = retrofit.create(InterfaceManager.SearchProductTopBrandReq.class);
        Call<InterfaceManager.SearchProductTopBrandResponse> call = callFunc.get(page, sidx);
        call.enqueue(new Callback<InterfaceManager.SearchProductTopBrandResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchProductTopBrandResponse> call, Response<InterfaceManager.SearchProductTopBrandResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=goodsListener){
                        goodsListener.onDataBack(response.body().result);
                    }
                }else{
                    if (null!=goodsListener){
                        goodsListener.onDataError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchProductTopBrandResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=goodsListener){
                    goodsListener.onDataError();
                }
            }
        });
    }

    // 聚划算
    public void searchProductJhs(int page,String sidx) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchProductJhsReq callFunc = retrofit.create(InterfaceManager.SearchProductJhsReq.class);
        Call<InterfaceManager.SearchProductJhsResponse> call = callFunc.get(page, sidx);
        call.enqueue(new Callback<InterfaceManager.SearchProductJhsResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchProductJhsResponse> call, Response<InterfaceManager.SearchProductJhsResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=goodsListener){
                        goodsListener.onDataBack(response.body().result);
                    }
                }else{
                    if (null!=goodsListener){
                        goodsListener.onDataError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchProductJhsResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=goodsListener){
                    goodsListener.onDataError();
                }
            }
        });
    }

    // 淘抢购
    public void searchProductTqg(int page,String sidx) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchProductTqgReq callFunc = retrofit.create(InterfaceManager.SearchProductTqgReq.class);
        Call<InterfaceManager.SearchProductTqgResponse> call = callFunc.get(page, sidx);
        call.enqueue(new Callback<InterfaceManager.SearchProductTqgResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchProductTqgResponse> call, Response<InterfaceManager.SearchProductTqgResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=goodsListener){
                        goodsListener.onDataBack(response.body().result);
                    }
                }else{
                    if (null!=goodsListener){
                        goodsListener.onDataError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchProductTqgResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=goodsListener){
                    goodsListener.onDataError();
                }
            }
        });
    }

    // 上百券
    public void searchProduct100(int page,String sidx) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchProduct100Req callFunc = retrofit.create(InterfaceManager.SearchProduct100Req.class);
        Call<InterfaceManager.SearchProduct100Response> call = callFunc.get(page, sidx);
        call.enqueue(new Callback<InterfaceManager.SearchProduct100Response>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchProduct100Response> call, Response<InterfaceManager.SearchProduct100Response> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=goodsListener){
                        goodsListener.onDataBack(response.body().result);
                    }
                }else{
                    if (null!=goodsListener){
                        goodsListener.onDataError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchProduct100Response> call, Throwable t) {
                t.printStackTrace();
                if (null!=goodsListener){
                    goodsListener.onDataError();
                }
            }
        });
    }

    // 指定搜索接口
    public void superSearchProduct(int page, String itemTitle, String sidx,int isQuan, int isMall){
        superSearchProduct(uid,page,itemTitle,sidx,isQuan,isMall);
    }
    // 指定搜索接口
    private void superSearchProduct(long uid, int page, String itemTitle, String Sidx, int isQuan, int isMall) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SuperSearchProductReq callFunc = retrofit.create(InterfaceManager.SuperSearchProductReq.class);
        Call<InterfaceManager.SuperSearchProductResponse> call = callFunc.get(page, uid, itemTitle, Sidx, isQuan, isMall);
        call.enqueue(new Callback<InterfaceManager.SuperSearchProductResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SuperSearchProductResponse> call, Response<InterfaceManager.SuperSearchProductResponse> response) {
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=goodsListener){
                        goodsListener.onDataBack(response.body().result);
                    }
                }else{
                    if (null!=goodsListener){
                        goodsListener.onDataError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SuperSearchProductResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=goodsListener){
                    goodsListener.onDataError();
                }
            }
        });
    }

    // 查看收藏产品接口
    public void searchProcutCollection() {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchProcutCollectionReq callFunc = retrofit.create(InterfaceManager.SearchProcutCollectionReq.class);
        Call<InterfaceManager.SearchProcutCollectionResponse> call = callFunc.get(uid);
        call.enqueue(new Callback<InterfaceManager.SearchProcutCollectionResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchProcutCollectionResponse> call, Response<InterfaceManager.SearchProcutCollectionResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=goodsListener){
                        goodsListener.onDataBack(response.body().result);
                    }
                }else{
                    if (null!=goodsListener){
                        goodsListener.onDataError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchProcutCollectionResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=goodsListener){
                    goodsListener.onDataError();
                }
            }
        });
    }



    /**
     * 海报回调接口
     */
    public interface HomeRequestPosterListener{

        void onPosterBack(List<String> banners);
        void onPosterError();

        void onPicBack(List<InterfaceManager.ShufflingPictureInfo> banners);
        void onPicError();

    }

    /**
     * 公告回调
     */
    public interface NoticeListener{

        void onNoticeBack(List<InterfaceManager.SearchNoticeInfo> result);
        void onNoticeError();

    }

    /**
     * 商品查询回调
     */
    public interface HomeRequestGoodsListener{

        void onDataBack(List<InterfaceManager.SearchProductInfo> result);
        void onDataError();

    }

    /**
     * 今日值得买回调
     */
    public interface GoodsTJListener{

        void onGoodsTJBack(List<InterfaceManager.SearchProductInfo> result);
        void onGoodsTJError();

    }

    /**
     * 首页热门推荐
     */
    public interface HomHotDataListener{

        void onHotDataBack(List<InterfaceManager.SearchProductInfoEx> result);
        void onHotDataError();

    }

    /**
     * 首页热门推荐
     */
    public interface AppInfoCountListener{

        void onAppCountBack(int num);
        void onAppCountError();

    }

    // 热门推荐
    public void indexSearch(int page) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.IndexSearchReq callFunc = retrofit.create(InterfaceManager.IndexSearchReq.class);
        Call<InterfaceManager.IndexSearchResponse> call = callFunc.get(page);
        call.enqueue(new Callback<InterfaceManager.IndexSearchResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.IndexSearchResponse> call, Response<InterfaceManager.IndexSearchResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=hotDataListener){
                        hotDataListener.onHotDataBack(response.body().result);
                    }
                }else{
                    if (null!=hotDataListener){
                        hotDataListener.onHotDataError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.IndexSearchResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=hotDataListener){
                    hotDataListener.onHotDataError();
                }
            }
        });
    }

    // 获取首页公告
    public void searchNoticeReq(String uid) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        //测试ip:www.371d.cn
//        baseIP = "http://www.371d.cn/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//                baseIP = "http://www.371d.cn/";
        InterfaceManager.SearchNoticeReq searchNoticeReq = retrofit.create(InterfaceManager.SearchNoticeReq.class);
        Call<InterfaceManager.SearchNoticeResponse> call = searchNoticeReq.get(uid);
        call.enqueue(new Callback<InterfaceManager.SearchNoticeResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchNoticeResponse> call, Response<InterfaceManager.SearchNoticeResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=noticeListener){
                        noticeListener.onNoticeBack(response.body().result);
                    }
                }else{
                    if (null!=noticeListener){
                        noticeListener.onNoticeError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchNoticeResponse> call, Throwable t) {
                if (null!=noticeListener){
                    noticeListener.onNoticeError();
                }
                t.printStackTrace();
            }
        });
    }

    // 获取首页轮播图
    public void shufflingPictureReq(String uid) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        //测试ip:www.371d.cn
//                baseIP = "http://www.371d.cn/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.ShufflingPictureReq shufflingPictureReq = retrofit.create(InterfaceManager.ShufflingPictureReq.class);
        Call<InterfaceManager.ShufflingPictureResponse> call = shufflingPictureReq.get(uid);
        call.enqueue(new Callback<InterfaceManager.ShufflingPictureResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.ShufflingPictureResponse> call, Response<InterfaceManager.ShufflingPictureResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=posterListener){
                        posterListener.onPicBack(response.body().result);
                    }
                }else{
                    if (null!=posterListener){
                        posterListener.onPicError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.ShufflingPictureResponse> call, Throwable t) {
                if (null!=posterListener){
                    posterListener.onPicError();
                }
                t.printStackTrace();
            }
        });
    }


    // 获取消息数量
    public void getAppInfoCount(String uid) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//                baseIP = "http://www.371d.cn/";
        InterfaceManager.AppInfoCountReq appInfoCountReq = retrofit.create(InterfaceManager.AppInfoCountReq.class);
        Call<InterfaceManager.AppInfoCountResponse> call = appInfoCountReq.get(uid);
        call.enqueue(new Callback<InterfaceManager.AppInfoCountResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.AppInfoCountResponse> call, Response<InterfaceManager.AppInfoCountResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=appInfoCountListener){
                        appInfoCountListener.onAppCountBack(response.body().result);
                    }
                }else{
                    if (null!=appInfoCountListener){
                        appInfoCountListener.onAppCountError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.AppInfoCountResponse> call, Throwable t) {
                if (null!=appInfoCountListener){
                    appInfoCountListener.onAppCountError();
                }
                t.printStackTrace();
            }
        });
    }

}
