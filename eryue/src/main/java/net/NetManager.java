package net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.eryue.AccountUtil;
import com.eryue.home.HomeRequestPresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dzzhao.summer on 2018/2/12.
 */

public class NetManager {

    /**
     * 没有网络
     */
    public static final int NETWORKTYPE_INVALID = 0;
    /**
     * wap网络
     */
    public static final int NETWORKTYPE_WAP = 1;
    /**
     * 2G网络
     */
    public static final int NETWORKTYPE_2G = 2;
    public static final int NETWORKTYPE_3G = 3;
    public static final int NETWORKTYPE_WIFI = 4;
    private static NetManager shareObject = null;
    private final static Object object = new Object();

    public static NetManager getInstance() {
        if (shareObject == null) {
            synchronized (object) {
                if (shareObject == null) {
                    shareObject = new NetManager();
                }
            }
        }
        return shareObject;
    }


    //用于获取domain
    private HomeRequestPresenter presenter;


    public static int getNetWorkType(Context context) {
        int mNetWorkType = 0;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();

            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost = android.net.Proxy.getDefaultHost();

                mNetWorkType = TextUtils.isEmpty(proxyHost)
                        ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G : NETWORKTYPE_2G)
                        : NETWORKTYPE_WAP;
            }
        } else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }

        return mNetWorkType;
    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    // 激活接口(在获取ip后调用)
    public void AddActivator() {
        String baseIP = AccountUtil.getBaseIp();
        String inviteCode = AccountUtil.getInviteCode();
        if (TextUtils.isEmpty(baseIP)||TextUtils.isEmpty(inviteCode)){
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.AddActivatorReq callFunc = retrofit.create(InterfaceManager.AddActivatorReq.class);
        Call<InterfaceManager.AddActivatorResponse> call = callFunc.get(inviteCode,1);
        call.enqueue(new Callback<InterfaceManager.AddActivatorResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.AddActivatorResponse> call, Response<InterfaceManager.AddActivatorResponse> response) {
                // zdz 总是返回1，待联调确认是否服务是否正常
                if (null!=response.body()&&response.body().status == 0) { //成功

                }
            }
            @Override
            public void onFailure(Call<InterfaceManager.AddActivatorResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
