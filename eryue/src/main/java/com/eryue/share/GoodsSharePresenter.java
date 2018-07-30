package com.eryue.share;

import android.text.TextUtils;
import android.util.Log;

import com.eryue.AccountUtil;

import net.InterfaceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 商品分享
 * Created by bli.Jason on 2018/3/1.
 */

public class GoodsSharePresenter {

    private IGoodsShareDetailListener shareDetailListener;

    private IGoodsShareDetailExListener shareDetailExListener;

    private String baseIP = AccountUtil.getBaseIp();


    public void setShareDetailListener(IGoodsShareDetailListener shareDetailListener) {
        this.shareDetailListener = shareDetailListener;
    }

    public void setShareDetailExListener(IGoodsShareDetailExListener shareDetailExListener) {
        this.shareDetailExListener = shareDetailExListener;
    }

    // 好券搜索分享页接口(好券搜索、首页分类、今日值得买、今日上新、9.9包邮、上百券)
    public void shareProductDetail(String itemId, long uid) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.ShareProductDetailReq callFunc = retrofit.create(InterfaceManager.ShareProductDetailReq.class);
        Call<InterfaceManager.ShareProductDetailResponse> call = callFunc.get(itemId, uid);
        call.enqueue(new Callback<InterfaceManager.ShareProductDetailResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.ShareProductDetailResponse> call, Response<InterfaceManager.ShareProductDetailResponse> response) {
                // 0：失败；1：正确
                if (null != response.body() && response.body().status == 1) {
                    if (null != shareDetailListener) {
                        shareDetailListener.onShareDetailBack(response.body().result);
                    }
                } else {
                    if (null != shareDetailListener) {
                        shareDetailListener.onShareDetailError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.ShareProductDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != shareDetailListener) {
                    shareDetailListener.onShareDetailError();
                }
            }
        });
    }

    // 指定券分享页接口
    public void superShareProductDetail(String itemId, long uid, String searchFlag) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SuperShareProductDetailReq callFunc = retrofit.create(InterfaceManager.SuperShareProductDetailReq.class);
        Call<InterfaceManager.SuperShareProductDetailResponse> call = callFunc.get(itemId, uid, searchFlag);
        call.enqueue(new Callback<InterfaceManager.SuperShareProductDetailResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SuperShareProductDetailResponse> call, Response<InterfaceManager.SuperShareProductDetailResponse> response) {
                // 0：失败；1：正确
                if (null != response.body() && response.body().status == 1) {
                    if (null != shareDetailListener) {
                        shareDetailListener.onShareDetailBack(response.body().result);
                    }
                } else {
                    if (null != shareDetailListener) {
                        shareDetailListener.onShareDetailError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SuperShareProductDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != shareDetailListener) {
                    shareDetailListener.onShareDetailError();
                }
            }
        });
    }

    // 好券优选接口
    public void shareProductYxDetail(String itemId, long uid) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.ShareProductYxDetailReq callFunc = retrofit.create(InterfaceManager.ShareProductYxDetailReq.class);
        Call<InterfaceManager.ShareProductDetailResponse> call = callFunc.get(itemId, uid);
        call.enqueue(new Callback<InterfaceManager.ShareProductDetailResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.ShareProductDetailResponse> call, Response<InterfaceManager.ShareProductDetailResponse> response) {
                // 0：失败；1：正确
                if (null != response.body() && response.body().status == 1) {
                    if (null != shareDetailListener) {
                        shareDetailListener.onShareDetailBack(response.body().result);
                    }
                } else {
                    if (null != shareDetailListener) {
                        shareDetailListener.onShareDetailError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.ShareProductDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != shareDetailListener) {
                    shareDetailListener.onShareDetailError();
                }
            }
        });
    }

    // 人气宝贝接口(人气宝贝、爆款单、好货疯抢)
    public void shareProductTopDetail(String itemId, long uid) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.ShareProductTopDetailReq callFunc = retrofit.create(InterfaceManager.ShareProductTopDetailReq.class);
        Call<InterfaceManager.ShareProductDetailResponse> call = callFunc.get(itemId, uid);
        call.enqueue(new Callback<InterfaceManager.ShareProductDetailResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.ShareProductDetailResponse> call, Response<InterfaceManager.ShareProductDetailResponse> response) {
                // 0：失败；1：正确
                if (null != response.body() && response.body().status == 1) {
                    if (null != shareDetailListener) {
                        shareDetailListener.onShareDetailBack(response.body().result);
                    }
                } else {
                    if (null != shareDetailListener) {
                        shareDetailListener.onShareDetailError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.ShareProductDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != shareDetailListener) {
                    shareDetailListener.onShareDetailError();
                }
            }
        });
    }

    // 品牌爆款分享页接口
    public void shareProductBrandDetail(String itemId, long uid) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.ShareProductBrandDetailReq callFunc = retrofit.create(InterfaceManager.ShareProductBrandDetailReq.class);
        Call<InterfaceManager.ShareProductDetailResponse> call = callFunc.get(itemId, uid);
        call.enqueue(new Callback<InterfaceManager.ShareProductDetailResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.ShareProductDetailResponse> call, Response<InterfaceManager.ShareProductDetailResponse> response) {
                // 0：失败；1：正确
                if (null != response.body() && response.body().status == 1) {
                    if (null != shareDetailListener) {
                        shareDetailListener.onShareDetailBack(response.body().result);
                    }
                } else {
                    if (null != shareDetailListener) {
                        shareDetailListener.onShareDetailError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.ShareProductDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != shareDetailListener) {
                    shareDetailListener.onShareDetailError();
                }
            }
        });
    }

    // 聚划算分享页接口(聚划算、淘抢购、视频购物)
    public void shareTbActivityProductDetail(String itemId, long uid) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.ShareTbActivityProductDetailReq callFunc = retrofit.create(InterfaceManager.ShareTbActivityProductDetailReq.class);
        Call<InterfaceManager.ShareProductDetailResponse> call = callFunc.get(itemId, uid);
        call.enqueue(new Callback<InterfaceManager.ShareProductDetailResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.ShareProductDetailResponse> call, Response<InterfaceManager.ShareProductDetailResponse> response) {
                // 0：失败；1：正确
                if (null != response.body() && response.body().status == 1) {
                    if (null != shareDetailListener) {
                        shareDetailListener.onShareDetailBack(response.body().result);
                    }
                } else {
                    if (null != shareDetailListener) {
                        shareDetailListener.onShareDetailError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.ShareProductDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != shareDetailListener) {
                    shareDetailListener.onShareDetailError();
                }
            }
        });
    }

    //收藏夹
    public void shareProductCollectionDetail(String itemId, long uid) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.ShareProductCollectionDetailReq callFunc = retrofit.create(InterfaceManager.ShareProductCollectionDetailReq.class);
        Call<InterfaceManager.ShareProductDetailResponse> call = callFunc.get(itemId, uid);
        call.enqueue(new Callback<InterfaceManager.ShareProductDetailResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.ShareProductDetailResponse> call, Response<InterfaceManager.ShareProductDetailResponse> response) {
                // 0：失败；1：正确
                if (null != response.body() && response.body().status == 1) {
                    if (null != shareDetailListener) {
                        shareDetailListener.onShareDetailBack(response.body().result);
                    }
                } else {
                    if (null != shareDetailListener) {
                        shareDetailListener.onShareDetailError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.ShareProductDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != shareDetailListener) {
                    shareDetailListener.onShareDetailError();
                }
            }
        });
    }

    //分享v2.0
    public void requestWholeAppShareDetail(String itemId, String uid, String productType, String searchFlag) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.WholeAppShareDetailReq callFunc = retrofit.create(InterfaceManager.WholeAppShareDetailReq.class);
        Call<InterfaceManager.WholeAppShareDetailResponse> call = callFunc.get(itemId, uid, productType, searchFlag);
        call.enqueue(new Callback<InterfaceManager.WholeAppShareDetailResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.WholeAppShareDetailResponse> call, Response<InterfaceManager.WholeAppShareDetailResponse> response) {
                // 0：失败；1：正确
                if (null != response.body() && response.body().status == 1) {
                    if (null != shareDetailExListener) {
                        shareDetailExListener.onShareDetailExBack(response.body().result);
                    }
                } else {
                    if (null != shareDetailExListener) {
                        shareDetailExListener.onShareDetailExError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.WholeAppShareDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != shareDetailExListener) {
                    shareDetailExListener.onShareDetailExError();
                }
            }
        });
    }


    /**
     * 商品收藏回调
     */
    public interface IGoodsShareDetailListener {

        void onShareDetailBack(InterfaceManager.ShareProductDetailInfo shareProductDetailInfo);

        void onShareDetailError();

    }

    /**
     * 商品分享回调
     */
    public interface IGoodsShareDetailExListener {

        void onShareDetailExBack(InterfaceManager.WholeAppShareDetailInfo shareDetailInfo);

        void onShareDetailExError();

    }
}
