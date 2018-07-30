package com.eryue.jd;

import android.text.TextUtils;

import com.eryue.AccountUtil;
import com.library.util.StringUtils;

import net.InterfaceManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by enjoy on 2018/5/12.
 */

public class SearchJDPresenter {

    private String baseIP = AccountUtil.getBaseIp();

    private SearchJDListener searchJDListener;

    private SearchJDDetailListener jdDetailListener;

    private SearchJDShareListener jdShareListener;

    private SearchJDActivityListener jdActivityListener;


    public SearchJDPresenter() {

    }

    public void setSearchJDListener(SearchJDListener searchJDListener) {
        this.searchJDListener = searchJDListener;
    }

    public void setJdDetailListener(SearchJDDetailListener jdDetailListener) {
        this.jdDetailListener = jdDetailListener;
    }

    public void setJdShareListener(SearchJDShareListener jdShareListener) {
        this.jdShareListener = jdShareListener;
    }

    public void setJdActivityListener(SearchJDActivityListener jdActivityListener) {
        this.jdActivityListener = jdActivityListener;
    }

    // 京东免单接口
    public void requestSearchJd(int itemId, String sidx, String sord) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchJdFreeReq callFunc = retrofit.create(InterfaceManager.SearchJdFreeReq.class);
        Call<InterfaceManager.SearchJdFreeResponse> call = callFunc.get(itemId, sidx, sord);
        call.enqueue(new Callback<InterfaceManager.SearchJdFreeResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchJdFreeResponse> call, Response<InterfaceManager.SearchJdFreeResponse> response) {
                // 0：失败；1：正确
                if (null != response.body() && response.body().status == 1) {
                    if (null != searchJDListener) {
                        searchJDListener.onSearchJDBack(response.body().result);
                    }
                } else {
                    if (null != searchJDListener) {
                        searchJDListener.onSearchJDError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchJdFreeResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != searchJDListener) {
                    searchJDListener.onSearchJDError();
                }
            }
        });
    }

    /**
     * 商品查询回调
     */
    public interface SearchJDListener {

        void onSearchJDBack(List<InterfaceManager.SearchProductInfoEx> result);

        void onSearchJDError();

    }

    // 京东免单详情接口
    public void requestSearchJdDetail(String itemId, String uid) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchJdDetailReq callFunc = retrofit.create(InterfaceManager.SearchJdDetailReq.class);
        Call<InterfaceManager.SearchJdDetailResponse> call = callFunc.get(itemId, uid);
        call.enqueue(new Callback<InterfaceManager.SearchJdDetailResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchJdDetailResponse> call, Response<InterfaceManager.SearchJdDetailResponse> response) {
                // 0：失败；1：正确
                if (null != response.body() && response.body().status == 1) {
                    if (null != jdDetailListener) {
                        jdDetailListener.onSearchJDDetailBack(response.body().result);
                    }
                } else {
                    if (null != jdDetailListener) {
                        jdDetailListener.onSearchJDDetailError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchJdDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (null != jdDetailListener) {
                    jdDetailListener.onSearchJDDetailError();
                }
            }
        });
    }

    /**
     * 京东免单详情回调
     */
    public interface SearchJDDetailListener {

        void onSearchJDDetailBack(InterfaceManager.SearchProductDetailInfoEx result);

        void onSearchJDDetailError();

    }

    //分享v2.0
    public void requesJDShareDetail(String itemId, String uid) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchJdShareReq callFunc = retrofit.create(InterfaceManager.SearchJdShareReq.class);
        Call<InterfaceManager.SearchJdShareResponse> call = callFunc.get(itemId, uid);
        call.enqueue(new Callback<InterfaceManager.SearchJdShareResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchJdShareResponse> call, Response<InterfaceManager.SearchJdShareResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=jdShareListener){
                        jdShareListener.onSearchJDShareBack(response.body().result);
                    }
                }else{
                    if (null!=jdShareListener){
                        jdShareListener.onSearchJDShareError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchJdShareResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=jdShareListener){
                    jdShareListener.onSearchJDShareError();
                }
            }
        });
    }

    public void onResume(){
        baseIP = AccountUtil.getBaseIp();
    }



    /**
     * 京东免单详情回调
     */
    public interface SearchJDShareListener {

        void onSearchJDShareBack(InterfaceManager.WholeAppShareDetailInfo shareDetailInfo);

        void onSearchJDShareError();

    }


    //首页京东广告
    public void getActivityJd(String uid) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.GetActivityJdReq callFunc = retrofit.create(InterfaceManager.GetActivityJdReq.class);
        Call<InterfaceManager.GetActivityJdResponse> call = callFunc.get(uid);
        call.enqueue(new Callback<InterfaceManager.GetActivityJdResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.GetActivityJdResponse> call, Response<InterfaceManager.GetActivityJdResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=jdActivityListener){
                        jdActivityListener.onJDActivityBack(response.body().result);
                    }
                }else{
                    if (null!=jdActivityListener){
                        jdActivityListener.onJDActivityError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.GetActivityJdResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=jdActivityListener){
                    jdActivityListener.onJDActivityError();
                }
            }
        });
    }


    /**
     * 京东免单详情回调
     */
    public interface SearchJDActivityListener {

        void onJDActivityBack(List<InterfaceManager.GetActivityJdInfo> result);

        void onJDActivityError();

    }


}
