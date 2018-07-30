package com.eryue.home;

import android.text.TextUtils;
import android.util.Log;

import com.eryue.AccountUtil;
import com.eryue.BaseApplication;
import com.google.gson.Gson;
import com.library.util.StringUtils;
import com.library.util.ToastTools;

import net.DataCenterManager;
import net.InterfaceManager;
import net.NetManager;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static net.InterfaceManager.baseURL;
import static net.KeyFlag.BASE_IP_KEY_INPUT;
import static net.KeyFlag.BASE_IP_VALUE_KEY_INPUT;
import static net.KeyFlag.INVITE_CODE_KEY_INPUT;
import static net.KeyFlag.NORMAL_UID_KEY;

/**
 * Created by enjoy on 2018/2/25.
 */

public class MainPresenter {

    /**
     * 海报
     */
    SearchRedPacketListener redPacketListener;

    private String baseIP = AccountUtil.getBaseIp();
    private long uid = StringUtils.valueOfLong(AccountUtil.getUID());

    public void setRedPacketListener(SearchRedPacketListener redPacketListener) {
        this.redPacketListener = redPacketListener;
    }

    // 首页红包接口
    public void searchRedPacketReq() {
        baseIP = AccountUtil.getBaseIp();
        uid = StringUtils.valueOfLong(AccountUtil.getUID());
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
//        baseIP = "http://www.371d.cn/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchRedPacketReq callFunc = retrofit.create(InterfaceManager.SearchRedPacketReq.class);
        Call<InterfaceManager.SearchRedPacketResponse> call = callFunc.get(uid);
        call.enqueue(new Callback<InterfaceManager.SearchRedPacketResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchRedPacketResponse> call, Response<InterfaceManager.SearchRedPacketResponse> response) {
                // 0：失败；1：正确
                if (null!=response&&null!=response.body()&&response.body().status==1){

                    if (null!=redPacketListener){
                        redPacketListener.onPacketBack(response.body().result);
                    }


                }else{
                    if (null!=redPacketListener){
                        redPacketListener.onPacketError();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchRedPacketResponse> call, Throwable t) {
                t.printStackTrace();
                if (null!=redPacketListener){
                    redPacketListener.onPacketError();
                }
            }
        });
    }



    /**
     * 首页红包回调接口
     */
    public interface SearchRedPacketListener{

        void onPacketBack(InterfaceManager.SearchRedPacketInfo redPacketInfo);
        void onPacketError();

    }

    private HomeRequestPresenter homeRequestPresenter;
    InviteCodeListener inviteCodeListener;

    public void setInviteCodeListener(InviteCodeListener inviteCodeListener) {
        this.inviteCodeListener = inviteCodeListener;
    }

    public interface InviteCodeListener{
        void onError();

        void onSuccess();

    }


    // 邀请码获取IP接口
    public void getIpByCode(final String code) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.GetIpByInvitationCode getIpByInvitationCode = retrofit.create(InterfaceManager.GetIpByInvitationCode.class);
        Call<InterfaceManager.IpResponse> call = getIpByInvitationCode.get(code);
        call.enqueue(new Callback<InterfaceManager.IpResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.IpResponse> call, Response<InterfaceManager.IpResponse> response) {
                if (null!=response.body()&&response.body().status == 1) {
                    if (response.body().result != null && response.body().result.size() > 0) {
                        InterfaceManager.IpResponse.IpInfo ipInfo = response.body().result.get(0);
                        String baseIP = "http://" + ipInfo.ip + "/";
                        long uid = ipInfo.uid;
                        DataCenterManager.Instance().save(BaseApplication.getInstance(), NORMAL_UID_KEY, uid + "");
                        DataCenterManager.Instance().save(BaseApplication.getInstance(), BASE_IP_KEY_INPUT, baseIP);
                        DataCenterManager.Instance().save(BaseApplication.getInstance(), BASE_IP_VALUE_KEY_INPUT, ipInfo.ip);

                        if (!TextUtils.isEmpty(code)) {
                            //全局邀请码赋值
                            DataCenterManager.Instance().save(BaseApplication.getInstance(), INVITE_CODE_KEY_INPUT, code);
                        }


                        NetManager.getInstance().AddActivator();

                        //获取domain
                        if(null == homeRequestPresenter){
                            homeRequestPresenter = new HomeRequestPresenter();
                        }
                        homeRequestPresenter.getServerConfig(BaseApplication.getInstance());

                        if (null!=inviteCodeListener){
                            inviteCodeListener.onSuccess();
                        }

                    }
                } else if (null!=response.body()&&response.body().status == 0) {
//                    ToastTools.showShort(getContext(), "邀请码错误，请重新输入");
                } else {
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.IpResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }



}
