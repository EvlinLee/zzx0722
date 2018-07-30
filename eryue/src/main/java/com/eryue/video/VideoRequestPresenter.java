package com.eryue.video;

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

public class VideoRequestPresenter {

    /**
     * 视频购物
     */
    VideoListListener videoListListener;

    private String baseIP = AccountUtil.getBaseIp();

    public void setVideoListListener(VideoListListener videoListListener) {
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
                    if (null!=videoListListener){
                        videoListListener.onVideoListBack(response.body().result);
                    }
                }else{
                    if (null!=videoListListener){
                        videoListListener.onVideoListError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.searchProductVideoResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=videoListListener){
                    videoListListener.onVideoListError();
                }
            }
        });
    }


    /**
     * 视频购物回调列表接口
     */
    public interface VideoListListener{

        void onVideoListBack(List<InterfaceManager.SearchProductInfo> result);
        void onVideoListError();

    }

}
