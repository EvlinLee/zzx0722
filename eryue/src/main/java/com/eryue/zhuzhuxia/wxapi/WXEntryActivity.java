package com.eryue.zhuzhuxia.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;


import com.eryue.WXShare;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import java.util.logging.Logger;

import base.BaseActivity;

/**
 * 微信分享回调
 * Created by bli.Jason on 2018/3/1.
 */

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WXShare share = new WXShare(this);
        api = share.getApi();

        Log.d("zdz", "oncreate");

        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            if (!api.handleIntent(getIntent(), this)) {
                Log.d("zdz", "finish");

                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("zdz", "Exception");

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("zdz", "onNewIntent");

        setIntent(intent);
        if (!api.handleIntent(intent, this)) {
            finish();
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.d("zdz", "onReq");

    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.d("zdz", "onResp");

        if (baseResp instanceof SendAuth.Resp) {
            Log.d("zdz", "SendAuth.Resp");
            Intent intent = new Intent("wechat_login_back");
            intent.putExtra("errCode", baseResp.errCode);
            intent.putExtra("code", ((SendAuth.Resp) baseResp).code);
            sendBroadcast(intent);
            finish();
            return;
        }
        Intent intent = new Intent(WXShare.ACTION_SHARE_RESPONSE);
        intent.putExtra(WXShare.EXTRA_RESULT, new WXShare.Response(baseResp));
        sendBroadcast(intent);
        finish();
    }

}