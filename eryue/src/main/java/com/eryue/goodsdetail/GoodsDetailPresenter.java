package com.eryue.goodsdetail;

import android.text.TextUtils;
import android.util.Log;

import com.eryue.AccountUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.library.util.StringUtils;

import net.InterfaceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by bli.Jason on 2018/2/28.
 */

public class GoodsDetailPresenter {
    /**
     * 商品详情
     */
    GoodsDetailListener goodsDetailListener;

    private String baseIP = AccountUtil.getBaseIp();
    private long uid = StringUtils.valueOfLong(AccountUtil.getUID());

    public void setGoodsDetailListener(GoodsDetailListener goodsDetailListener) {
        this.goodsDetailListener = goodsDetailListener;
    }

    GoodsBuyOrderListener goodsBuyOrderListener;

    public void setGoodsBuyOrderListener(GoodsBuyOrderListener goodsBuyOrderListener) {
        this.goodsBuyOrderListener = goodsBuyOrderListener;
    }

    /**
     * 商品详情V2.0
     */
    GoodsDetailListenerEx goodsDetailListenerEx;

    public void setGoodsDetailListenerEx(GoodsDetailListenerEx goodsDetailListener) {
        this.goodsDetailListenerEx = goodsDetailListener;
    }

    private IGoodsImageListener imageListener;

    public void setImageListener(IGoodsImageListener imageListener) {
        this.imageListener = imageListener;
    }

    //商品详情页(好券搜索、首页分类、今日值得买、今日上新、9.9包邮、上百券)
    public void searchProductDetail(String itemId, String uid) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchProductDetailReq callFunc = retrofit.create(InterfaceManager.SearchProductDetailReq.class);
        Call<InterfaceManager.SearchProductDetailResponse> call = callFunc.get(itemId, uid);
        call.enqueue(new Callback<InterfaceManager.SearchProductDetailResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchProductDetailResponse> call, Response<InterfaceManager.SearchProductDetailResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1) {
                    if (null != goodsDetailListener) {
                        goodsDetailListener.onGoodsDetailBack(response.body().result);
                    }
                } else {
                    if (null != goodsDetailListener) {
                        goodsDetailListener.onGoodsDetailError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchProductDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != goodsDetailListener) {
                    goodsDetailListener.onGoodsDetailError();
                }
            }
        });
    }

    //商品详情页V2.0(好券搜索、首页分类、今日值得买、今日上新、9.9包邮、上百券)
    public void searchProductDetail(String itemId, String uid,String productType,String searchFlag) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        if (null == productType){
            productType = "";
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.WholeSearchDetailReq callFunc = retrofit.create(InterfaceManager.WholeSearchDetailReq.class);
        Call<InterfaceManager.WholeSearchDetailResponse> call = callFunc.get(itemId, uid,productType,searchFlag);
        call.enqueue(new Callback<InterfaceManager.WholeSearchDetailResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.WholeSearchDetailResponse> call, Response<InterfaceManager.WholeSearchDetailResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1) {
                    if (null != goodsDetailListenerEx) {
                        goodsDetailListenerEx.onGoodsDetailBackEx(response.body().result);
                    }
                } else {
                    if (null != goodsDetailListenerEx) {
                        goodsDetailListenerEx.onGoodsDetailErrorEx();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.WholeSearchDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != goodsDetailListenerEx) {
                    goodsDetailListenerEx.onGoodsDetailErrorEx();
                }
            }
        });
    }

    // 商品图片
    public void requestGoodsImg(String url) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        InterfaceManager.GoodsDetailImgReq callFunc = retrofit.create(InterfaceManager.GoodsDetailImgReq.class);
        Call<String> call = callFunc.getImg(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String detailStr = response.body();
                if (!TextUtils.isEmpty(detailStr)) {
                    String jsonStr = detailStr;
                    if (detailStr.contains("callback")) {
                        jsonStr = detailStr.replace("callback(", "").replace(")", "");
                    }
                    Gson gson = new Gson();
                    InterfaceManager.GoodsDetailImgResponse detailImgResponse = gson.fromJson(jsonStr, InterfaceManager.GoodsDetailImgResponse.class);
                    // 0：失败；1：正确
                    if (null != detailImgResponse && null != detailImgResponse.data.images) {
                        if (null != imageListener) {
                            imageListener.onImageBack(detailImgResponse.data.images);
                        }
                    } else {
                        if (null != imageListener) {
                            imageListener.onImageError();
                        }
                    }

                } else {
                    if (null != imageListener) {
                        imageListener.onImageError();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                if (null!=imageListener){
                    imageListener.onImageError();
                }
            }
        });
//        Call<InterfaceManager.GoodsDetailImgResponse> call = callFunc.getImg(url);
//        call.enqueue(new Callback<InterfaceManager.GoodsDetailImgResponse>() {
//            @Override
//            public void onResponse(Call<InterfaceManager.GoodsDetailImgResponse> call, Response<InterfaceManager.GoodsDetailImgResponse> response) {
//                // 0：失败；1：正确
//                if (null!=response&&null!=response.body().data.images&&!response.body().data.images.isEmpty()){
//                    if (null!=imageListener){
//                        imageListener.onImageBack(response.body().data.images);
//                    }
//                }else{
//                    if (null!=imageListener){
//                        imageListener.onImageError();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<InterfaceManager.GoodsDetailImgResponse> call, Throwable t) {
//                t.printStackTrace();
//                if (null!=imageListener){
//                    imageListener.onImageError();
//                }
//            }
//        });
    }


    private IGoodsStarListener goodsStarListener;

    public void setGoodsStarListener(IGoodsStarListener goodsStarListener) {
        this.goodsStarListener = goodsStarListener;
    }

    // 添加收藏
    public void addProcutCollection(InterfaceManager.SearchProductDetailInfoEx detailInfo) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.AddProcutCollectionReq callFunc = retrofit.create(InterfaceManager.AddProcutCollectionReq.class);
        Call<InterfaceManager.AddProcutCollectionResponse> call = callFunc.add(detailInfo.itemId, detailInfo.itemTitle, detailInfo.afterQuan,
                uid, detailInfo.clickUrl, detailInfo.quanPrice, detailInfo.discountPrice, detailInfo.shortTitle, detailInfo.pictUrl,detailInfo.jf,
                detailInfo.soldQuantity,detailInfo.isMall);
        call.enqueue(new Callback<InterfaceManager.AddProcutCollectionResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.AddProcutCollectionResponse> call, Response<InterfaceManager.AddProcutCollectionResponse> response) {
                // 0：失败；1：正确
                if (null != response && null != response.body()) {
                    if (null != goodsStarListener) {
                        goodsStarListener.onGoodsStarBack(response.body().status);
                    }
                } else {
                    if (null != goodsStarListener) {
                        goodsStarListener.onGoodsStarError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.AddProcutCollectionResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != goodsStarListener) {
                    goodsStarListener.onGoodsStarError();
                }
            }
        });
    }


    private IGoodsDeleteListener goodsDeleteListener;

    public void setGoodsDeleteListener(IGoodsDeleteListener goodsDeleteListener) {
        this.goodsDeleteListener = goodsDeleteListener;
    }

    // 删除收藏
    public void deleteProcutCollection(List<InterfaceManager.SearchProductDetailInfoEx> productList) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        List<InterfaceManager.DeleteProcutModel> deleteList = new ArrayList<>();
        InterfaceManager.DeleteProcutModel deleteProcutModel;
        for (int i = 0; i < productList.size(); i++) {
            if (null == productList.get(i)) {
                continue;
            }
            deleteProcutModel = new InterfaceManager.DeleteProcutModel();
            deleteProcutModel.itemId = productList.get(i).itemId;
            deleteProcutModel.uid = uid;
            deleteList.add(deleteProcutModel);
        }
        Gson gson = new Gson();
        String param = gson.toJson(deleteList);
        if (TextUtils.isEmpty(param)) {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.DeleteProcutCollectionReq callFunc = retrofit.create(InterfaceManager.DeleteProcutCollectionReq.class);
        Call<InterfaceManager.DeleteProcutCollectionResponse> call = callFunc.delete(param);
        call.enqueue(new Callback<InterfaceManager.DeleteProcutCollectionResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.DeleteProcutCollectionResponse> call, Response<InterfaceManager.DeleteProcutCollectionResponse> response) {
                // 0：失败；1：正确
                if (null != response && null != response.body()) {
                    if (null != goodsDeleteListener) {
                        goodsDeleteListener.onGoodsDeleteBack(response.body().status);
                    }
                } else {
                    if (null != goodsDeleteListener) {
                        goodsDeleteListener.onGoodsDeleteError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.DeleteProcutCollectionResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != goodsDeleteListener) {
                    goodsDeleteListener.onGoodsDeleteError();
                }
            }
        });
    }

    // 指定券详情页接口
    public void superSearchProductDetail(String itemId, String uid, String searchFlag) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SuperSearchProductDetailReq callFunc = retrofit.create(InterfaceManager.SuperSearchProductDetailReq.class);
        Call<InterfaceManager.SuperSearchProductDetailResponse> call = callFunc.get(itemId, uid, searchFlag);
        call.enqueue(new Callback<InterfaceManager.SuperSearchProductDetailResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SuperSearchProductDetailResponse> call, Response<InterfaceManager.SuperSearchProductDetailResponse> response) {
                // 0：失败；1：正确
                if (null != response.body() && response.body().status == 1) {
                    if (null != goodsDetailListener) {
                        goodsDetailListener.onGoodsDetailBack(response.body().result);
                    }
                } else {
                    if (null != goodsDetailListener) {
                        goodsDetailListener.onGoodsDetailError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SuperSearchProductDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != goodsDetailListener) {
                    goodsDetailListener.onGoodsDetailError();
                }
            }
        });
    }

    // 好券优选详情页接口
    public void searchProductYxDetail(String itemId, String uid) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchProductYxDetailReq callFunc = retrofit.create(InterfaceManager.SearchProductYxDetailReq.class);
        Call<InterfaceManager.SearchProductYxDetailResponse> call = callFunc.get(itemId, uid);
        call.enqueue(new Callback<InterfaceManager.SearchProductYxDetailResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchProductYxDetailResponse> call, Response<InterfaceManager.SearchProductYxDetailResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1) {
                    if (null != goodsDetailListener) {
                        goodsDetailListener.onGoodsDetailBack(response.body().result);
                    }
                } else {
                    if (null != goodsDetailListener) {
                        goodsDetailListener.onGoodsDetailError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchProductYxDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != goodsDetailListener) {
                    goodsDetailListener.onGoodsDetailError();
                }
            }
        });
    }

    // 人气宝贝详情页面接口(人气宝贝、爆款单、好货疯抢)
    public void searchProductTopDetail(String itemId, long uid) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchProductTopDetailReq callFunc = retrofit.create(InterfaceManager.SearchProductTopDetailReq.class);
        Call<InterfaceManager.SearchProductTopDetailResponse> call = callFunc.get(itemId, uid);
        call.enqueue(new Callback<InterfaceManager.SearchProductTopDetailResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchProductTopDetailResponse> call, Response<InterfaceManager.SearchProductTopDetailResponse> response) {
                // 0：失败；1：正确
                if (null != response.body() && response.body().status == 1) {
                    if (null != goodsDetailListener) {
                        goodsDetailListener.onGoodsDetailBack(response.body().result);
                    }
                } else {
                    if (null != goodsDetailListener) {
                        goodsDetailListener.onGoodsDetailError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchProductTopDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != goodsDetailListener) {
                    goodsDetailListener.onGoodsDetailError();
                }
            }
        });
    }

    // 品牌爆款详情页面接口
    public void searchProductBrandDetail(String itemId, String uid) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchProductBrandDetailReq callFunc = retrofit.create(InterfaceManager.SearchProductBrandDetailReq.class);
        Call<InterfaceManager.SearchProductDetailResponse> call = callFunc.get(itemId, uid);
        call.enqueue(new Callback<InterfaceManager.SearchProductDetailResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchProductDetailResponse> call, Response<InterfaceManager.SearchProductDetailResponse> response) {
                // 0：失败；1：正确
                if (null != response.body() && response.body().status == 1) {
                    if (null != goodsDetailListener) {
                        goodsDetailListener.onGoodsDetailBack(response.body().result);
                    }
                } else {
                    if (null != goodsDetailListener) {
                        goodsDetailListener.onGoodsDetailError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchProductDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != goodsDetailListener) {
                    goodsDetailListener.onGoodsDetailError();
                }
            }
        });
    }

    // 聚划算详情页接口(聚划算、淘抢购、视频购物)
    public void searchTbActivityProductDetail(String itemId, String uid) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchTbActivityProductDetailReq callFunc = retrofit.create(InterfaceManager.SearchTbActivityProductDetailReq.class);
        Call<InterfaceManager.SearchProductDetailResponse> call = callFunc.get(itemId, uid);
        call.enqueue(new Callback<InterfaceManager.SearchProductDetailResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchProductDetailResponse> call, Response<InterfaceManager.SearchProductDetailResponse> response) {
                // 0：失败；1：正确
                if (null != response.body() && response.body().status == 1) {
                    if (null != goodsDetailListener) {
                        goodsDetailListener.onGoodsDetailBack(response.body().result);
                    }
                } else {
                    if (null != goodsDetailListener) {
                        goodsDetailListener.onGoodsDetailError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchProductDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != goodsDetailListener) {
                    goodsDetailListener.onGoodsDetailError();
                }
            }
        });
    }

    // 收藏夹详情页接口
    public void searchProcutCollectionDetail(String itemId, String uid) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchProcutCollectionDetailReq callFunc = retrofit.create(InterfaceManager.SearchProcutCollectionDetailReq.class);
        Call<InterfaceManager.SearchProductDetailResponse> call = callFunc.get(itemId, uid);
        call.enqueue(new Callback<InterfaceManager.SearchProductDetailResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchProductDetailResponse> call, Response<InterfaceManager.SearchProductDetailResponse> response) {
                // 0：失败；1：正确
                if (null != response.body() && response.body().status == 1) {
                    if (null != goodsDetailListener) {
                        goodsDetailListener.onGoodsDetailBack(response.body().result);
                    }
                } else {
                    if (null != goodsDetailListener) {
                        goodsDetailListener.onGoodsDetailError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchProductDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != goodsDetailListener) {
                    goodsDetailListener.onGoodsDetailError();
                }
            }
        });
    }

    // 产品详情订单接口
    public void searchDetailsOrderReq(String itemId) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchDetailsOrderReq callFunc = retrofit.create(InterfaceManager.SearchDetailsOrderReq.class);
        Call<InterfaceManager.SearchDetailsOrderResponse> call = callFunc.get(itemId);
        call.enqueue(new Callback<InterfaceManager.SearchDetailsOrderResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchDetailsOrderResponse> call, Response<InterfaceManager.SearchDetailsOrderResponse> response) {
                // 0：失败；1：正确
                if (null != response.body()&&response.body().status == 1) {

                    if (null != goodsBuyOrderListener) {
                        goodsBuyOrderListener.onGoodsOrderBack(response.body().result);
                    }
                }else{
                    if (null != goodsBuyOrderListener) {
                        goodsBuyOrderListener.onGoodsOrderError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchDetailsOrderResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != goodsBuyOrderListener) {
                    goodsBuyOrderListener.onGoodsOrderError();
                }
            }
        });
    }

    /**
     * 商品详情回调V2.0
     */
    public interface GoodsDetailListenerEx {

        void onGoodsDetailBackEx(InterfaceManager.SearchProductDetailInfoEx result);

        void onGoodsDetailErrorEx();

    }


    /**
     * 商品详情回调
     */
    public interface GoodsDetailListener {

        void onGoodsDetailBack(InterfaceManager.SearchProductDetailInfo result);

        void onGoodsDetailError();

    }


    /**
     * 商品详情图片回调
     */
    public interface IGoodsImageListener {

        void onImageBack(List<String> images);

        void onImageError();

    }

    /**
     * 商品收藏回调
     */
    public interface IGoodsStarListener {

        void onGoodsStarBack(int status);

        void onGoodsStarError();

    }

    /**
     * 商品删除收藏回调
     */
    public interface IGoodsDeleteListener {

        void onGoodsDeleteBack(int status);

        void onGoodsDeleteError();

    }

    /**
     * 商品详情购买信息回调
     */
    public interface GoodsBuyOrderListener {

        void onGoodsOrderBack(List<InterfaceManager.SearchDetailOrderInfo> result);

        void onGoodsOrderError();

    }
}
