package com.eryue.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.R;
import com.library.util.StringUtils;
import com.library.util.ToastTools;

import net.InterfaceManager;
import net.MineInterface;

import base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by dazhou on 2018/5/6.
 * 订单提交
 */

public class OrderSubmissionActivity extends BaseActivity  implements View.OnClickListener {

    private EditText orderNumberET;

    private TextView valueTV0;
    private TextView valueTV1;
    private TextView valueTV2;

    private TextView normal_order;
    private TextView jingdong_order;
    private MineInterface.AccountInfoRsp accountInfoRsp;
    private MineInterface.AccountInfoRsp JDaccountInfoRsp;

    private int curType = 1;

    private String baseIP = AccountUtil.getBaseIp();

    private long uid = StringUtils.valueOfLong(AccountUtil.getUID());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_submission);

        navigationBar.setTitle("订单提交");

        initViews();

        reqNormalAccountInfo();
    }

    private void initViews() {
        normal_order = findViewById(R.id.normal_order);
        normal_order.setOnClickListener(this);
        jingdong_order = findViewById(R.id.jingdong_order);
        jingdong_order.setOnClickListener(this);

        valueTV0 = (TextView)findViewById(R.id.value0);
        valueTV1 = (TextView)findViewById(R.id.value1);
        valueTV2 = (TextView)findViewById(R.id.value2);

        orderNumberET = (EditText)findViewById(R.id.order_num);
        findViewById(R.id.sure).setOnClickListener(this);
    }

    // 普通账户 信息
    private void reqNormalAccountInfo() {
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.AccountInfoReq callFunc = retrofit.create(MineInterface.AccountInfoReq.class);
        Call<MineInterface.AccountInfoRsp> call = callFunc.get(AccountUtil.getUID());
        call.enqueue(new Callback<MineInterface.AccountInfoRsp>() {
            @Override
            public void onResponse(Call<MineInterface.AccountInfoRsp> call, Response<MineInterface.AccountInfoRsp> response) {
                if (response.body() == null) {
                    ToastTools.showShort(OrderSubmissionActivity.this, "暂无普通账户余额等数据");
                    return;
                }
                accountInfoRsp = response.body();
                refreshHeaderView(0);
            }

            @Override
            public void onFailure(Call<MineInterface.AccountInfoRsp> call, Throwable t) {
                t.printStackTrace();
                ToastTools.showShort(getBaseContext(), "请求失败");
            }
        });
    }

    // 京东免单账户 信息
    private void reqJingDongAccountInfo() {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.JDAccountInfoReq callFunc = retrofit.create(MineInterface.JDAccountInfoReq.class);
        Call<MineInterface.AccountInfoRsp> call = callFunc.get(AccountUtil.getUID());
        call.enqueue(new Callback<MineInterface.AccountInfoRsp>() {
            @Override
            public void onResponse(Call<MineInterface.AccountInfoRsp> call, Response<MineInterface.AccountInfoRsp> response) {
                if (response.body() == null) {
                    ToastTools.showShort(OrderSubmissionActivity.this, "暂无京东免单余额等数据");
                    return;
                }
                JDaccountInfoRsp = response.body();
                refreshHeaderView(1);
            }

            @Override
            public void onFailure(Call<MineInterface.AccountInfoRsp> call, Throwable t) {
                t.printStackTrace();
                ToastTools.showShort(getBaseContext(), "请求失败");
            }
        });
    }


    // 订单提交
    private void normalAccountTiXian() {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }

        String amount = orderNumberET.getText().toString();

        if (TextUtils.isEmpty(amount)) {
            ToastTools.showShort(this, "请输入订单号");
        }

        final Context context = this;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        MineInterface.OrderSubmitReq callFunc = retrofit.create(MineInterface.OrderSubmitReq.class);
        Call<String> call = callFunc.get(uid + "", amount, curType);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    ToastTools.showShort(context, response.body());
                } else {
                    ToastTools.showShort(context, "提交失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                ToastTools.showShort(context, "网络出错，稍后重试");
            }
        });
    }


    private void refreshHeaderView(final int type) {
        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                MineInterface.AccountInfoRsp tmp = accountInfoRsp;
                if (type == 1) {
                    tmp = JDaccountInfoRsp;
                }

                if (tmp == null) {
                    return;
                }
                valueTV0.setText(tmp.balance + "");
                valueTV1.setText(tmp.yg + "");
                valueTV2.setText(tmp.yx + "");
            }
        }).sendEmptyMessage(0);
    }
    @Override
    public void onClick(View v) {
        if (v.equals(findViewById(R.id.sure))) {
            normalAccountTiXian();
        } else if (v == normal_order) {
            curType = 1;
            if (accountInfoRsp == null) {
                reqNormalAccountInfo();
            } else {
                refreshHeaderView(0);
            }
            normal_order.setTextColor(Color.parseColor("#fd5b68"));
            jingdong_order.setTextColor(Color.parseColor("#666666"));
        } else if (v == jingdong_order) {
            curType = 0;
            if (JDaccountInfoRsp == null) {
                reqJingDongAccountInfo();
            } else {
                refreshHeaderView(1);
            }
            normal_order.setTextColor(Color.parseColor("#666666"));
            jingdong_order.setTextColor(Color.parseColor("#fd5b68"));
        }
    }
}
