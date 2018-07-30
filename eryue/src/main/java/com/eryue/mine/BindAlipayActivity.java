package com.eryue.mine;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.eryue.AccountUtil;
import com.eryue.R;
import com.library.util.StringUtils;
import com.library.util.ToastTools;

import net.DataCenterManager;
import net.InterfaceManager;
import net.KeyFlag;

import base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 绑定支付宝页面
 */

public class BindAlipayActivity extends BaseActivity {
    private EditText alipayET;
    private EditText nameET;

    private String realName;
    private String zfb;

    private String baseIP = AccountUtil.getBaseIp();
    private long uid = StringUtils.valueOfLong(AccountUtil.getUID());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_alipay);
        navigationBar.setTitle("绑定支付宝");
        initViews();
    }

    private void initViews() {
        alipayET = (EditText) findViewById(R.id.input_alipay);
        nameET = (EditText) findViewById(R.id.name);
        findViewById(R.id.take_cash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindAliPay();
            }
        });
    }

    // 绑定支付宝
    private void bindAliPay() {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }

//        zfb = alipayET.getText().toString();
//        realName = nameET.getText().toString();
//        if (TextUtils.isEmpty(zfb) || TextUtils.isEmpty(realName)) {
//
//            ToastTools.showShort(this, "请输入完整信息");
//            return;
//        }
//
//        final Context context = this;
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseIP)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        InterfaceManager.BindAlipayReq callFunc = retrofit.create(InterfaceManager.BindAlipayReq.class);
//        Call<InterfaceManager.BindAlipayResponse> call = callFunc.get(uid, realName, zfb);
//        call.enqueue(new Callback<InterfaceManager.BindAlipayResponse>() {
//            @Override
//            public void onResponse(Call<InterfaceManager.BindAlipayResponse> call, Response<InterfaceManager.BindAlipayResponse> response) {
//                if (response.body().status == 0) {
//                    ToastTools.showShort(context, "绑定失败，稍后重试");
//                } else if (response.body().status == 1) {
//                    ToastTools.showShort(context, "绑定成功");
//                    DataCenterManager.Instance().zfb = zfb;
//                    DataCenterManager.Instance().save(context, KeyFlag.ZFB_KEY, zfb);
//                    DataCenterManager.Instance().save(context, KeyFlag.REAL_NAME_KEY, realName);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<InterfaceManager.BindAlipayResponse> call, Throwable t) {
//                t.printStackTrace();
//                ToastTools.showShort(context, "网络出错，稍后重试");
//            }
//        });
    }
}
