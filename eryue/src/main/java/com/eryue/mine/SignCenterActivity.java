package com.eryue.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.R;
import com.library.util.CommonFunc;
import com.library.util.ToastTools;
import com.othershe.calendarview.weiget.CalendarView;

import net.DataCenterManager;
import net.KeyFlag;
import net.MineInterface;
import net.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

import base.BaseActivity;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static net.KeyFlag.KEY_DOMAIN;

/**
 * Created by dazhou on 2018/5/7.
 * 签到中心
 */

public class SignCenterActivity extends BaseActivity {

    private CalendarView calendarView;
    private TextView daysTV;
    private TextView hintTV;

    private ImageView image_view;

    private MineInterface.SearchContinuousSignDayRsp signDayRsp;

    // 连续签到天数
    private List<MineInterface.SignInfo> signInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_center);
        navigationBar.setTitle("签到中心");

        setupViews();

        reqSign();
        reqSignInfo();
        reqSignInfo1();
    }

    private void setupViews() {
        calendarView = (CalendarView) findViewById(R.id.calendar);
        daysTV = findViewById(R.id.num);
        hintTV = findViewById(R.id.hint);
        image_view = findViewById(R.id.image_view);


        String url = AccountUtil.getHeaderImageUl();
        if (! TextUtils.isEmpty(url)) {
            Glide.with(this).load(url)
                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                    .into(image_view);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("y年M月");
        String s = sdf.format(new Date());
        hintTV.setText(s);
    }


    // 签到
    private void reqSign() {
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        
        MineInterface.SignReq callFunc = retrofit.create(MineInterface.SignReq.class);
        Call<String> call = callFunc.get(AccountUtil.getUID());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, final Response<String> response) {
                {
                    if (response.body() != null) {
                        if ("signed".equals(response.body())) {
                            ToastTools.showShort(SignCenterActivity.this, "今日已签到");
                        } else if ("firstSign".equals(response.body()) || "sign_success".equals(response.body())) {
                            ToastTools.showShort(SignCenterActivity.this, "签到成功");
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                hideProgressMum();
                ToastTools.showShort(SignCenterActivity.this, "签到失败，请稍后重试");
            }
        });
    }


    // 请求连续签到天数与签到人
    private void reqSignInfo() {
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MineInterface.SearchContinuousSignDayReq callFunc = retrofit.create(MineInterface.SearchContinuousSignDayReq.class);
        Call<MineInterface.SearchContinuousSignDayRsp> call = callFunc.get(AccountUtil.getUID());
        call.enqueue(new Callback<MineInterface.SearchContinuousSignDayRsp>() {
            @Override
            public void onResponse(Call<MineInterface.SearchContinuousSignDayRsp> call, final Response<MineInterface.SearchContinuousSignDayRsp> response) {
                {
                    if (response.body() != null) {
                        signDayRsp = response.body();
                        refreshView();
                    }
                }
            }

            @Override
            public void onFailure(Call<MineInterface.SearchContinuousSignDayRsp> call, Throwable t) {
                t.printStackTrace();
                hideProgressMum();
                ToastTools.showShort(SignCenterActivity.this, "签到失败，请稍后重试");
            }
        });
    }

    // 具体签到天数查询接口
    private void reqSignInfo1() {
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MineInterface.SearchSignDayReq callFunc = retrofit.create(MineInterface.SearchSignDayReq.class);
        Call<List<MineInterface.SignInfo>> call = callFunc.get(AccountUtil.getUID());
        call.enqueue(new Callback<List<MineInterface.SignInfo>>() {
            @Override
            public void onResponse(Call<List<MineInterface.SignInfo>> call, final Response<List<MineInterface.SignInfo>> response) {
                {
                    if (response.body() != null) {
                        signInfoList = response.body();
                        refreshView1();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MineInterface.SignInfo>> call, Throwable t) {
                t.printStackTrace();
                hideProgressMum();
                ToastTools.showShort(SignCenterActivity.this, "数据请求失败，请稍后重试");
            }
        });
    }


    private void refreshView() {
        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                if (signDayRsp != null) {
                    daysTV.setText(signDayRsp.signCount + "天");
                }
            }
        }).sendEmptyMessage(0);
    }

    private void refreshView1() {

        if (signInfoList == null) {
            return;
        }



        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<String> list = new ArrayList<>();


                for (int i = 0; i < signInfoList.size(); i++) {
                    String date = TimeUtils.getStrTime(signInfoList.get(i).date + "", "yyyy.MM.dd");
                    list.add(date);
                }

                if (list.size() >= 0) {

                    Date bDate = CommonFunc.getFirstDateOfMonth(new Date());
                    Date eDate = CommonFunc.getLastDateOfMonth(new Date());
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
                    String bDateStr = formatter.format(bDate);
                    String eDateStr = formatter.format(eDate);

                    calendarView.setMultiDate(list);
                    calendarView
                            .setStartEndDate(bDateStr, eDateStr)
                            .setDisableStartEndDate(bDateStr, eDateStr)
                            .setInitDate(bDateStr)
                            .init();
                }
            }
        });
    }


}
