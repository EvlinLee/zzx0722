package com.eryue.home;

import android.text.TextUtils;

import com.eryue.AccountUtil;
import com.library.util.StringUtils;

import net.InterfaceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by enjoy on 2018/5/26.
 */

public class NoticePresenter {


    private String baseIP = AccountUtil.getBaseIp();
    private long uid = StringUtils.valueOfLong(AccountUtil.getUID());

    private SearchInfoListener searchInfoListener;

    public void setSearchInfoListener(SearchInfoListener searchInfoListener) {
        this.searchInfoListener = searchInfoListener;
    }

    // 首页公告详情接口
    public void requestSearchInfo(String dataBaseId) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
//        baseIP = "http://www.371d.cn/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchInfoReq callFunc = retrofit.create(InterfaceManager.SearchInfoReq.class);
        Call<InterfaceManager.SearchInfoResponse> call = callFunc.get(uid,dataBaseId);
        call.enqueue(new Callback<InterfaceManager.SearchInfoResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchInfoResponse> call, Response<InterfaceManager.SearchInfoResponse> response) {
                // 0：失败；1：正确
                if (null!=response&&null!=response.body()&&response.body().status==1){

                    if (null!=searchInfoListener){
                        if (null!=response.body().result){
                            searchInfoListener.onSearchInfoBack(response.body().result);
                        }else{
                            searchInfoListener.onSearchInfoError();
                        }
                    }


                }else{
                    if (null!=searchInfoListener){
                        searchInfoListener.onSearchInfoError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchInfoResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=searchInfoListener){
                    searchInfoListener.onSearchInfoError();
                }
            }
        });
    }



    /**
     * 首页红包回调接口
     */
    public interface SearchInfoListener{

        void onSearchInfoBack(InterfaceManager.SearchDetailInfo searchDetailInfo);
        void onSearchInfoError();

    }



}
