package com.eryue.search;

import android.text.TextUtils;
import android.util.Log;

import com.eryue.AccountUtil;

import net.InterfaceManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by enjoy on 2018/2/25.
 */

public class GoodsSearchPresenter {


    private KeyWordListener keyWordListener;

    private String baseIP = AccountUtil.getBaseIp();

    public void setKeyWordListener(KeyWordListener keyWordListener) {
        this.keyWordListener = keyWordListener;
    }

    // 搜索关键词获取
    public void getKeywords() {
        baseIP = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.KeywordsReq callFunc = retrofit.create(InterfaceManager.KeywordsReq.class);
        Call<InterfaceManager.KeywordsResponse> call = callFunc.get();
        call.enqueue(new Callback<InterfaceManager.KeywordsResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.KeywordsResponse> call, Response<InterfaceManager.KeywordsResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){
                    if (null!=keyWordListener){
                        keyWordListener.onKeyWordBack(response.body().result.keywords);
                    }
                }else{
                    if (null!=keyWordListener){
                        keyWordListener.onKeyWordError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.KeywordsResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=keyWordListener){
                    keyWordListener.onKeyWordError();
                }
            }
        });
    }



    public interface KeyWordListener{
        void onKeyWordBack(List<String> keywords);
        void onKeyWordError();
    }
}
