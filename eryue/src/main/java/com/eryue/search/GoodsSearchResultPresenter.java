package com.eryue.search;

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

public class GoodsSearchResultPresenter {

    private String baseIP = AccountUtil.getBaseIp();

    private WholeSearchListener wholeSearchListener;


    public GoodsSearchResultPresenter(){

    }


    public void setWholeSearchListener(WholeSearchListener wholeSearchListener) {
        this.wholeSearchListener = wholeSearchListener;
    }

    // 实时播接口
    public void requestWholeSearch(int page, String sord,String type, String itemTitle, String cat,String sidx) {
//        baseIP = "http://www.371d.cn/";
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
                    if (null!=wholeSearchListener){
                        wholeSearchListener.onWholeSearcheBack(response.body().result);
                    }
                }else{
                    if (null!=wholeSearchListener){
                        wholeSearchListener.onWholeSearchError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.WholeSearchResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=wholeSearchListener){
                    wholeSearchListener.onWholeSearchError();
                }
            }
        });
    }

    /**
     * 商品查询回调
     */
    public interface WholeSearchListener{

        void onWholeSearcheBack(List<InterfaceManager.SearchProductInfoEx> result);
        void onWholeSearchError();

    }


}
