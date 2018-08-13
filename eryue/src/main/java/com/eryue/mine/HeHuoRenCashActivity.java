package com.eryue.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.R;
import com.eryue.WXShare;
import com.eryue.home.SharePopView;
import com.eryue.util.Logger;
import com.eryue.widget.ShareContentView;
import com.eryue.widget.StockDatePopModel;
import com.eryue.widget.StockDatePopView;
import com.library.util.StringUtils;
import com.library.util.ToastTools;

import net.DataCenterManager;
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
 * 合伙人提现
 */

public class HeHuoRenCashActivity extends BaseActivity implements View.OnClickListener {

    private final String[] hintStr0 = {"本月", "上月", "全部"};
    private final int[] hintValue0 = {0, 1, 2};
    private int hintIndex0 = 0;
    private StockDatePopView popView0;


    private EditText amountET; // 提现
    private TextView input_alipayTV;

    /**
     * 提现记录按钮
     */
    private TextView presentRecord;

    private TextView valueTV0;
    private TextView valueTV1;
    private TextView valueTV2;

    private TextView normal_account;
    private TextView jingdongmiandan;
    private MineInterface.AccountInfoRsp accountInfoRsp;
    private MineInterface.AccountInfoRsp JDaccountInfoRsp;
    private int curType;

    private String baseIP = AccountUtil.getBaseIp();
    private long uid = StringUtils.valueOfLong(AccountUtil.getUID());


    //分享
    SharePopView sharePopView;
    private View shareView;
    private WXShare wxShare;


    private TextView hint5;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hehuoren_cash);

        navigationBar.setTitle("账户提现");

        initViews();

        reqNormalAccountInfo();

        reqTixianRules();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!TextUtils.isEmpty(DataCenterManager.Instance().zfb)) {
            input_alipayTV.setText(DataCenterManager.Instance().zfb);
        }
    }

    private void initViews() {
        normal_account = findViewById(R.id.normal_account);
        normal_account.setOnClickListener(this);
        jingdongmiandan = findViewById(R.id.jingdongmiandan);
        jingdongmiandan.setOnClickListener(this);

        valueTV0 = (TextView)findViewById(R.id.value0);
        valueTV1 = (TextView)findViewById(R.id.value1);
        valueTV2 = (TextView)findViewById(R.id.value2);

        amountET = (EditText)findViewById(R.id.cash);
        //默认两位小数
        amountET.addTextChangedListener(new MoneyTextWatcher(amountET));

        input_alipayTV = (TextView)findViewById(R.id.input_alipay);
        input_alipayTV.setOnClickListener(this);
        findViewById(R.id.take_cash).setOnClickListener(this);

        presentRecord = (TextView) findViewById(R.id.present_record);
        presentRecord.setOnClickListener(this);

        shareView = findViewById(R.id.share_img);
        shareView.setOnClickListener(this);

        initPopView();

        hint5 = findViewById(R.id.hint5);
    }

    private void initPopView() {

        findViewById(R.id.hint0_con).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (curType != 1) {
                    ToastTools.showShort(HeHuoRenCashActivity.this, "只有京东免单支持帅选");
                    return;
                }

                if (null != popView0) {
                    popView0.showPopView(v);
                }
            }
        });
        popView0 = new StockDatePopView(this);
        popView0.setData(hintStr0, hintValue0);
        popView0.setOnDateClickListener(new StockDatePopView.OnDateClickListener() {
            @Override
            public void onDateItemClick(StockDatePopModel model) {
                TextView textView = (TextView) findViewById(R.id.hint0);
                textView.setText(model.getShowString());
                hintIndex0 = model.getIndex();
                // 等接口
            }
        });

        popView0.setSelectOption(hintIndex0);

//        amountET.addTextChangedListener(new TextWatcher()
    }

    // 提现规则
    private void reqTixianRules() {
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.WithdrwalRulesReq callFunc = retrofit.create(MineInterface.WithdrwalRulesReq.class);
        Call<MineInterface.WithdrwalRulesRsp> call = callFunc.get();
        call.enqueue(new Callback<MineInterface.WithdrwalRulesRsp>() {
            @Override
            public void onResponse(Call<MineInterface.WithdrwalRulesRsp> call, Response<MineInterface.WithdrwalRulesRsp> response) {
                if (response.body() == null) {
                    return;
                }
                if (!TextUtils.isEmpty(response.body().txRule)) {
                    hint5.setText(response.body().txRule);
                }
            }

            @Override
            public void onFailure(Call<MineInterface.WithdrwalRulesRsp> call, Throwable t) {
                t.printStackTrace();
                ToastTools.showShort(getBaseContext(), "请求失败");
            }
        });
    }

    // 普通账户 信息
    private void reqNormalAccountInfo() {
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }

        // just for test
//        baseip = "http://www.371d.cn/";

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
                    ToastTools.showShort(HeHuoRenCashActivity.this, "暂无普通订单余额等数据");
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
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }

        // just for test
//        baseip = "http://www.371d.cn/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.JDAccountInfoReq callFunc = retrofit.create(MineInterface.JDAccountInfoReq.class);
        Call<MineInterface.AccountInfoRsp> call = callFunc.get(AccountUtil.getUID());
        call.enqueue(new Callback<MineInterface.AccountInfoRsp>() {
            @Override
            public void onResponse(Call<MineInterface.AccountInfoRsp> call, Response<MineInterface.AccountInfoRsp> response) {
                if (response.body() == null) {
                    ToastTools.showShort(HeHuoRenCashActivity.this, "暂无京东免单余额等数据");
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


    // 普通账户 提现
    private void normalAccountTiXian() {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        // just for test
//        baseIP = "http://www.371d.cn/";
        int amount = 0;
        try {
            amount = Integer.valueOf(amountET.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            ToastTools.showShort(this, "提现金额书写错误");
            return;
        }
//        if (TextUtils.isEmpty(input_alipayTV.getText()) || "请添加支付宝账号".equalsIgnoreCase(input_alipayTV.getText().toString())) {
//            ToastTools.showShort(this, "请先绑定支付宝");
//            return;
//        }

        showProgressMum();
        final Context context = this;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.ExchangeReq callFunc = retrofit.create(InterfaceManager.ExchangeReq.class);
        Call<InterfaceManager.ExchangeResponse> call = callFunc.get(uid, amount);
        call.enqueue(new Callback<InterfaceManager.ExchangeResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.ExchangeResponse> call, final Response<InterfaceManager.ExchangeResponse> response) {
                second = System.currentTimeMillis()-second;
                Logger.getInstance(context).writeLog_new("cash","cash","提现时间="+second/1000+"s");
                hideProgressMum();
                ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (null!=response.body()&&response.body().status == 0) {
                            ToastTools.showShort(context, "余额不足");
                        } else if (null!=response.body()&&response.body().status == 1) {
                            ToastTools.showShort(context, "提现申请成功，请等待资金到账");
                            //提现成功后刷新当前界面
                            reqNormalAccountInfo();
                        } else if (null!=response.body()&&response.body().status == -1) {
                            ToastTools.showShort(context, "未绑定支付宝");
                        } else if (null!=response.body()&&response.body().status == -2) {
                            ToastTools.showShort(context, "发送过快");
                        } else if (null!=response.body()&&response.body().status == -3) {
                            ToastTools.showShort(context, "未到提现日期");
                        } else if (null!=response.body()&&response.body().status == -4) {
                            ToastTools.showShort(context, "不满足提现门槛");
                        }
                    }
                }).sendEmptyMessage(0);

            }

            @Override
            public void onFailure(Call<InterfaceManager.ExchangeResponse> call, Throwable t) {
                t.printStackTrace();
                hideProgressMum();
                ToastTools.showShort(context, "网络出错，稍后重试");
            }
        });
    }

    // 京东免单 提现
    private void JDAccountTiXian() {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        // just for test
//        baseIP = "http://www.371d.cn/";
        int amount = 0;
        try {
            amount = Integer.valueOf(amountET.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            ToastTools.showShort(this, "提现金额书写错误");
            return;
        }

        showProgressMum();

        final Context context = this;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        MineInterface.JDTiXianReq callFunc = retrofit.create(MineInterface.JDTiXianReq.class);
        Call<String> call = callFunc.get(uid + "", amount);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideProgressMum();
                Logger.getInstance(context).writeLog_new("cash","cash","jd提现时间="+second/1000+"s");
                if (response.body() != null) {
                    ToastTools.showShort(context, response.body());
                    //提现成功后刷新当前界面
                    reqJingDongAccountInfo();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                hideProgressMum();
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
                amountET.setText("");
            }
        }).sendEmptyMessage(0);
    }

    long second;
    @Override
    public void onClick(View v) {
        if (v.equals(input_alipayTV)) {
            Intent intent = new Intent(this, BindAlipayActivity.class);
            startActivity(intent);
        } else if (v.equals(findViewById(R.id.take_cash))) {
            second = System.currentTimeMillis();
            if (curType == 0) {
                normalAccountTiXian();
            }
            else if (curType == 1) {
                JDAccountTiXian();
            }
        } else if (v == presentRecord) {
            Intent intent = new Intent(this, TiXianRecordActivity.class);
            intent.putExtra("type", curType);
            startActivity(intent);

        } else if (v == normal_account) {
            curType = 0;
            if (accountInfoRsp == null) {
                reqNormalAccountInfo();
            } else {
                refreshHeaderView(0);
            }
            normal_account.setBackground(getResources().getDrawable(R.drawable.img_zhtx_ptzh));
            jingdongmiandan.setBackground(getResources().getDrawable(R.drawable.two_arc_conners_right));
            normal_account.setTextColor(Color.parseColor("#ffffffff"));
            jingdongmiandan.setTextColor(Color.parseColor("#ffff4c4c"));
        } else if (v == jingdongmiandan) {
            curType = 1;
            if (JDaccountInfoRsp == null) {
                reqJingDongAccountInfo();
            } else {
                refreshHeaderView(1);
            }
            jingdongmiandan.setBackground(getResources().getDrawable(R.drawable.img_zhtx_ptzh));
            normal_account.setBackground(getResources().getDrawable(R.drawable.two_arc_conners_left));
            normal_account.setTextColor(Color.parseColor("#ffff4c4c"));
            jingdongmiandan.setTextColor(Color.parseColor("#ffffffff"));
        }
        else if (v == shareView) {
            if (null == sharePopView){
                sharePopView = new SharePopView(HeHuoRenCashActivity.this);
                sharePopView.setOnShareClickListener(new ShareContentView.OnShareClickListener() {
                    @Override
                    public void onShareClick(int tag) {
                        shareImage(tag);
                    }
                });
                wxShare = new WXShare(HeHuoRenCashActivity.this);

            }

            sharePopView.showPopView();
        }
    }

    private void shareImage(int tag) {
        Bitmap bitmap = net.ImageUtils.takeScreenShot(this);

        if (bitmap == null) {
            ToastTools.showShort(HeHuoRenCashActivity.this, "图片有异常，稍后重试");
            return;
        }
        if (wxShare == null) {
            wxShare = new WXShare(HeHuoRenCashActivity.this);
        }
        wxShare.shareImage(bitmap, tag);
    }

    class MoneyTextWatcher implements TextWatcher {
        private EditText editText;
        private int digits = 2;

        public MoneyTextWatcher(EditText et) {
            editText = et;
        }
        public MoneyTextWatcher setDigits(int d) {
            digits = d;
            return this;
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //删除“.”后面超过2位后的数据
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > digits) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + digits+1);
                    editText.setText(s);
                    editText.setSelection(s.length()); //光标移到最后
                }
            }
            //如果"."在起始位置,则起始位置自动补0
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                editText.setText(s);
                editText.setSelection(2);
            }

            //如果起始位置为0,且第二位跟的不是".",则无法后续输入
            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    editText.setText(s.subSequence(0, 1));
                    editText.setSelection(1);
                    return;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
