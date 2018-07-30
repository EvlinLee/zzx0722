package com.eryue.friends;

import android.text.TextUtils;

import com.eryue.AccountUtil;
import com.library.util.StringUtils;

import net.InterfaceManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bli.Jason on 2018/2/28.
 */

public class DayFriendPresenter {

    private static final OkHttpClient client = new OkHttpClient.Builder().

            connectTimeout(30, TimeUnit.SECONDS).

            readTimeout(30, TimeUnit.SECONDS).

            writeTimeout(30, TimeUnit.SECONDS).build();

    private IFriendsListener friendsListener;
    private IFriendsExListener friendsExListener;

    private String baseIP = AccountUtil.getBaseIp();
    private long uid = StringUtils.valueOfLong(AccountUtil.getUID());

    public void setFriendsListener(IFriendsListener friendsListener) {
        this.friendsListener = friendsListener;
    }

    public void setFriendsExListener(IFriendsExListener friendsExListener) {
        this.friendsExListener = friendsExListener;
    }

    public String getBaseIP() {
        return baseIP;
    }

    // 每日发圈
    public void requestTimeLine() {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchTimeLineReq callFunc = retrofit.create(InterfaceManager.SearchTimeLineReq.class);

        TimeZone tz = TimeZone.getTimeZone("GMT");

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        format.setTimeZone(tz);
        Date myDate = new Date();
        //从1970年来的秒数
        long second = myDate.getTime()/1000;

        Call<InterfaceManager.SearchTimeLineResponse> call = callFunc.get(uid,second);
        call.enqueue(new Callback<InterfaceManager.SearchTimeLineResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchTimeLineResponse> call, Response<InterfaceManager.SearchTimeLineResponse> response) {
                // 0：失败；1：正确
                if (null!=response&&null!=response.body().result&&!response.body().result.isEmpty()){
                    if (null!=friendsListener){
                        friendsListener.onFriendsDataBack(response.body().result);
                    }
                }else{
                    if (null!=friendsListener){
                        friendsListener.onFriendsDataError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchTimeLineResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=friendsListener){
                    friendsListener.onFriendsDataError();
                }
            }
        });
    }

    // 每日发圈
    public void requestTimeLineEx(int pageNo) {
        baseIP = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        //测试ip:www.371d.cn
//        baseIP = "http://www.371d.cn/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchTimeLineExReq callFunc = retrofit.create(InterfaceManager.SearchTimeLineExReq.class);

        TimeZone tz = TimeZone.getTimeZone("GMT");

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        format.setTimeZone(tz);
        Date myDate = new Date();
        //从1970年来的秒数
        long second = myDate.getTime()/1000;
//        second = 1526547600;

        Call<InterfaceManager.SearchTimeLineExResponse> call = callFunc.get(uid,second,pageNo);
        call.enqueue(new Callback<InterfaceManager.SearchTimeLineExResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchTimeLineExResponse> call, Response<InterfaceManager.SearchTimeLineExResponse> response) {
                // 0：失败；1：正确
                if (null!=response&&null!=response.body()&&null!=response.body().result&&!response.body().result.isEmpty()){
                    if (null!=friendsExListener){
                        friendsExListener.onFriendsDataExBack(response.body().result);
                    }
                }else{
                    if (null!=friendsExListener){
                        friendsExListener.onFriendsDataExError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchTimeLineExResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=friendsExListener){
                    friendsExListener.onFriendsDataExError();
                }
            }
        });
    }

    /**
     * 商品详情图片回调
     */
    public interface IFriendsListener{

        void onFriendsDataBack(List<InterfaceManager.TimeLine> dataList);
        void onFriendsDataError();

    }

    /**
     * 商品详情图片回调
     */
    public interface IFriendsExListener{

        void onFriendsDataExBack(List<InterfaceManager.TimeLineEx> dataList);
        void onFriendsDataExError();

    }
}
