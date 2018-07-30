package com.eryue.mine;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eryue.AccountUtil;
import com.eryue.R;
import com.library.util.StringUtils;

import net.InterfaceManager;

import base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 显示 今日统计、昨日统计、近7日统计、本月统计、上月统计
 */

public class HeHuoRenView2 extends LinearLayout implements View.OnClickListener{
    private Context context;

    public int type; // 1=今日 2=昨日 3=近7日 4=本月 5=上月

    private String baseIP = AccountUtil.getBaseIp();
    private long uid = StringUtils.valueOfLong(AccountUtil.getUID());

    public InterfaceManager.AccountStatisticsResponse.AccountStatisticsInfo accountStatisticsInfo;

    public HeHuoRenView2(Context context) {
        super(context);
        initView(context);
    }

    public HeHuoRenView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HeHuoRenView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(final Context context) {
        this.context = context;

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_hehuoren_2, this);

        findViewById(R.id.self_contain_1).setOnClickListener(this);
        findViewById(R.id.self_contain_2).setOnClickListener(this);
        findViewById(R.id.proxy_contain_1).setOnClickListener(this);
        findViewById(R.id.proxy_contain_2).setOnClickListener(this);


        findViewById(R.id.update).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reqData(type);
            }
        });
    }



    public void setUpdate(int visi) {
//        findViewById(R.id.update).setVisibility(visi);
    }

    public void setTitle(String title) {
        ((TextView) findViewById(R.id.title)).setText(title);
    }


    public void reqData(int type) {
        this.type = type;

        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.AccountStatisticsReq callFunc = retrofit.create(InterfaceManager.AccountStatisticsReq.class);
        Call<InterfaceManager.AccountStatisticsResponse> call = callFunc.get(uid, type);
        call.enqueue(new Callback<InterfaceManager.AccountStatisticsResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.AccountStatisticsResponse> call, Response<InterfaceManager.AccountStatisticsResponse> response) {

                if (null!=response.body()&&response.body().status == 0) {
                    return;
                }
                if (null!=getContext()&&(getContext() instanceof BaseActivity)){
                    ((BaseActivity)getContext()).hideProgressMum();
                }

                accountStatisticsInfo = response.body().result;
                setViews();
            }

            @Override
            public void onFailure(Call<InterfaceManager.AccountStatisticsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setViews() {
        ((TextView) findViewById(R.id.value1)).setText("¥" + accountStatisticsInfo.selftAmount);
        ((TextView) findViewById(R.id.value2)).setText("¥" + accountStatisticsInfo.proxyAmount);
        ((TextView) findViewById(R.id.value3)).setText("" + accountStatisticsInfo.selftNum);
        ((TextView) findViewById(R.id.value4)).setText("" + accountStatisticsInfo.proxyNum);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(findViewById(R.id.self_contain_1)) ||
                v.equals(findViewById(R.id.self_contain_2)) ) {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("selfOrProxy", 1);
            context.startActivity(intent);
        } else if (v.equals(findViewById(R.id.proxy_contain_1)) ||
                v.equals(findViewById(R.id.proxy_contain_2)) ) {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("selfOrProxy", 2);
            context.startActivity(intent);
        }
    }
}
