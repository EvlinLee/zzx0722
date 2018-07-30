package com.eryue.mine;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.eryue.AccountUtil;
import com.eryue.R;

import net.DataCenterManager;
import net.InterfaceManager;
import net.KeyFlag;

import base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static net.KeyFlag.KEY_DOMAIN;
import static net.KeyFlag.KEY_DOMAIN_INPUT;

/**
 * Created by dazhou on 2018/6/2.
 *  用户协议 平台用户协议
 */

public class XieYiActivity extends BaseActivity {

    private TextView textView;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xieyi);

        textView = findViewById(R.id.textview);

        type = getIntent().getIntExtra("type", -1);

        navigationBar.setTitle("协议内容");
        if (type == 0) {

            if (TextUtils.isEmpty(InterfaceManager.info1)) {
                getInfo();
            } else {
                textView.setText(InterfaceManager.info1);
            }
        } else if (type == 1) {
            if (TextUtils.isEmpty(InterfaceManager.info2)) {
                getInfo();
            } else {
                textView.setText(InterfaceManager.info2);
            }
        } else {
            return;
        }
    }

    private void refreshView() {
        if (type == 0) {
            textView.setText(InterfaceManager.info1);
        } else if (type == 1) {
            textView.setText(InterfaceManager.info2);
        }
    }

    public void getInfo() {
        String baseIP = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.GetServerConfig getServerConfig = retrofit.create(InterfaceManager.GetServerConfig.class);
        Call<InterfaceManager.ServerConfigResponse> call = getServerConfig.get();
        call.enqueue(new Callback<InterfaceManager.ServerConfigResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.ServerConfigResponse> call, Response<InterfaceManager.ServerConfigResponse> response) {
                // 0：失败；1：正确
                if (null!=response.body()&&response.body().status == 1){

                    InterfaceManager.ServerConfigResponse.ServerInfo serverInfo = response.body().result;
                    if (serverInfo != null) {
                        InterfaceManager.info1 = serverInfo.regInfo;
                        InterfaceManager.info2 = serverInfo.regPlatformInfo;
                        refreshView();
                    }
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.ServerConfigResponse> call, Throwable t) {

                t.printStackTrace();
            }
        });
    }
}
