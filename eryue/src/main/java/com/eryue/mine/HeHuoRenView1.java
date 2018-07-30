package com.eryue.mine;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eryue.AccountUtil;
import com.eryue.R;
import com.library.util.StringUtils;
import com.library.util.ToastTools;

import net.DataCenterManager;
import net.InterfaceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 今日预估+今日总成交量+账户余额+上月结算预估收入+本月结算预估收入
 */

public class HeHuoRenView1 extends LinearLayout {
    private Context context;
    public InterfaceManager.AccountInfoResponse.AccountInfo accountInfo;

    private String baseIP = AccountUtil.getBaseIp();
    private long uid = StringUtils.valueOfLong(AccountUtil.getUID());

    public HeHuoRenView1(Context context) {
        super(context);
        initView(context);
    }

    public HeHuoRenView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HeHuoRenView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(final Context context) {
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_hehuoren_1, this);

        findViewById(R.id.tixian).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HeHuoRenCashActivity.class);
                context.startActivity(intent);
            }
        });

        getData();
    }

    private void getData() {

        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.AccountInfoReq callFunc = retrofit.create(InterfaceManager.AccountInfoReq.class);
        Call<InterfaceManager.AccountInfoResponse> call = callFunc.get(uid);
        call.enqueue(new Callback<InterfaceManager.AccountInfoResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.AccountInfoResponse> call, Response<InterfaceManager.AccountInfoResponse> response) {
                if (null!=response.body()&&response.body().status == 1) {
                    accountInfo = response.body().result;
                    DataCenterManager.Instance().balance = accountInfo.balance;

                    setViews();
                } else {
                    ToastTools.showShort(context, "请求出错，稍后重试");
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.AccountInfoResponse> call, Throwable t) {
                t.printStackTrace();
                ToastTools.showShort(context, "请求出错，稍后重试");
            }
        });
    }

    private void setViews() {
        TextView tv = (TextView) findViewById(R.id.jinriyujishouru);
        tv.setText("¥" + accountInfo.todayAmount);
        tv = (TextView) findViewById(R.id.jinrichengjiaozongliang);
        tv.setText(accountInfo.todayNum + "");
        tv = (TextView) findViewById(R.id.yue_value);
        tv.setText("¥" + accountInfo.balance);
        tv = (TextView) findViewById(R.id.benyue_value);
        tv.setText("¥" + accountInfo.currentMonthAmount);
        tv = (TextView) findViewById(R.id.shangyue_value);
        tv.setText("¥" + accountInfo.lastMonthAmount);

    }

}
