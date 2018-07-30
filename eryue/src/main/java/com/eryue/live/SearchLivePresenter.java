package com.eryue.live;

import android.text.TextUtils;

import com.eryue.AccountUtil;

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

public class SearchLivePresenter {

    private String baseIP = AccountUtil.getBaseIp();

    private SearchLiveListener searchLiveListener;


    public SearchLivePresenter(){

    }


    public String getBaseIP() {
        return baseIP;
    }

    public void setSearchLiveListener(SearchLiveListener searchLiveListener) {
        this.searchLiveListener = searchLiveListener;
    }

    // 实时播接口
    public void requestSeachLive(int page, String sord, String itemTitle, String sidx) {
        baseIP = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchLiveReq callFunc = retrofit.create(InterfaceManager.SearchLiveReq.class);
        Call<InterfaceManager.SearchLiveResponse> call = callFunc.get(page, sord, itemTitle, sidx);
        call.enqueue(new Callback<InterfaceManager.SearchLiveResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchLiveResponse> call, Response<InterfaceManager.SearchLiveResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=searchLiveListener){
                        searchLiveListener.onSearchLiveBack(response.body().result);
                    }
                }else{
                    if (null!=searchLiveListener){
                        searchLiveListener.onSearchLiveError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchLiveResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=searchLiveListener){
                    searchLiveListener.onSearchLiveError();
                }
            }
        });
    }

    /**
     * 商品查询回调
     */
    public interface SearchLiveListener{

        void onSearchLiveBack(List<InterfaceManager.SearchProductInfoEx> result);
        void onSearchLiveError();

    }


    // 今日爆款接口
    public void requestSeachHot(int page, String sord, String itemTitle, String sidx) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchtHotReq callFunc = retrofit.create(InterfaceManager.SearchtHotReq.class);
        Call<InterfaceManager.SearchtHotResponse> call = callFunc.get(page, sord, itemTitle, sidx);
        call.enqueue(new Callback<InterfaceManager.SearchtHotResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchtHotResponse> call, Response<InterfaceManager.SearchtHotResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=searchLiveListener){
                        searchLiveListener.onSearchLiveBack(response.body().result);
                    }
                }else{
                    if (null!=searchLiveListener){
                        searchLiveListener.onSearchLiveError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchtHotResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=searchLiveListener){
                    searchLiveListener.onSearchLiveError();
                }
            }
        });
    }

    // 上百券搜索接口
    public void requestSearchCoupons(int page, String sord, String itemTitle, String sidx) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchCouponsReq callFunc = retrofit.create(InterfaceManager.SearchCouponsReq.class);
        Call<InterfaceManager.SearchCouponsResponse> call = callFunc.get(page, sord, itemTitle, sidx);
        call.enqueue(new Callback<InterfaceManager.SearchCouponsResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchCouponsResponse> call, Response<InterfaceManager.SearchCouponsResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=searchLiveListener){
                        searchLiveListener.onSearchLiveBack(response.body().result);
                    }
                }else{
                    if (null!=searchLiveListener){
                        searchLiveListener.onSearchLiveError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchCouponsResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=searchLiveListener){
                    searchLiveListener.onSearchLiveError();
                }
            }
        });
    }

    //all：全网/tb：淘宝/jd：京东/mgj：蘑菇街/pdd：拼多多/sn：苏宁/tbActivity：淘宝活动
    public void requestBusiness(int page, String sord,String type, String itemTitle,String cat, String sidx) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.WholeSearchReq callFunc = retrofit.create(InterfaceManager.WholeSearchReq.class);
        Call<InterfaceManager.WholeSearchResponse> call = callFunc.get(page, sord,type, itemTitle,cat, sidx);
        call.enqueue(new Callback<InterfaceManager.WholeSearchResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.WholeSearchResponse> call, Response<InterfaceManager.WholeSearchResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status==1){
                    if (null!=searchLiveListener){
                        searchLiveListener.onSearchLiveBack(response.body().result);
                    }
                }else{
                    if (null!=searchLiveListener){
                        searchLiveListener.onSearchLiveError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.WholeSearchResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=searchLiveListener){
                    searchLiveListener.onSearchLiveError();
                }
            }
        });
    }


}
