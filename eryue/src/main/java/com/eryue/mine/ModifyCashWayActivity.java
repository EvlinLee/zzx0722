package com.eryue.mine;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.R;
import com.library.util.ToastTools;

import net.InterfaceManager;
import net.MineInterface;

import base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by dazhou on 2018/5/13.
 * 修改提现信息
 */

public class ModifyCashWayActivity extends BaseActivity  implements View.OnClickListener{
    private TextView phoneET;
    private EditText codeET;
    private EditText alipayET;
    private EditText alipayNameET;
    private TextView sureTV;

    private String password;
    private String alipay;
    private String alipayName;
    private String phoneStr;

    private TextView getCodeTV;
    private int second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_cash_way);
        navigationBar.setTitle("修改提现信息");

        phoneStr = getIntent().getStringExtra("phone");

        setupViews();
    }

    private void setupViews() {


        phoneET = findViewById(R.id.phone);
        codeET = findViewById(R.id.code);
        alipayET = findViewById(R.id.input_alipay);

        alipayNameET = findViewById(R.id.input_alipay_name);

        sureTV = findViewById(R.id.sure);
        sureTV.setOnClickListener(this);

        getCodeTV = (TextView) findViewById(R.id.get_code);
        getCodeTV.setOnClickListener(this);

        phoneET.setText(phoneStr);
    }

    @Override
    public void onClick(View view) {
        if (view == sureTV) {
            password = phoneET.getText().toString();
            alipay = alipayET.getText().toString();
            alipayName = alipayNameET.getText().toString();

            if (TextUtils.isEmpty(password) || TextUtils.isEmpty(alipay) || TextUtils.isEmpty(alipayName)) {
                ToastTools.showShort(getBaseContext(), "请输入完整的信息进行修改");
                return;
            }

            reqModifyCashWay();
        } else if (view == getCodeTV) {
            getVerCode();
        }
    }

    private void getVerCode() {
        getCodeTV.setEnabled(false);
        second = 60;
        getCodeTV.setText(second + "秒后重新获取");
        handler.postDelayed(runnable, 1000);
        getPhoneCode(phoneStr);
    }

    // 注册短信验证码接口
    private void getPhoneCode(String phone) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AccountUtil.getBaseIp())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MineInterface.SendCodeForOpenIdReq callFunc = retrofit.create(MineInterface.SendCodeForOpenIdReq.class);
        Call<MineInterface.BaseRsp> call = callFunc.get(phone);
        call.enqueue(new Callback<MineInterface.BaseRsp>() {
            @Override
            public void onResponse(Call<MineInterface.BaseRsp> call, Response<MineInterface.BaseRsp> response) {
                if (response.body() == null) {
                    Toast.makeText(ModifyCashWayActivity.this, "系统错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (null!=response.body()&&response.body().status == 1) {
                    Toast.makeText(ModifyCashWayActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                } else if (null!=response.body()&&response.body().status == -1) {
                    Toast.makeText(ModifyCashWayActivity.this, "系统错误", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ModifyCashWayActivity.this, "验证码发送失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MineInterface.BaseRsp> call, Throwable t) {
                Toast.makeText(ModifyCashWayActivity.this, "验证码发送失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (second == 1) {
                getCodeTV.setEnabled(true);
                getCodeTV.setText("获取验证码");
            } else {
                second --;
                getCodeTV.setText(second + "秒");
                if (handler != null) {
                    handler.postDelayed(this, 1000);
                }
            }
        }
    };


    private void reqModifyCashWay() {
        String baseip = AccountUtil.getBaseIp();

        showProgressMum();

        String code = codeET.getText().toString();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(ModifyCashWayActivity.this, "请输入短信验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phoneStr)) {
            Toast.makeText(ModifyCashWayActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        long tUid = Long.valueOf(AccountUtil.getUID());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.BindAlipayByCodeReq callFunc = retrofit.create(InterfaceManager.BindAlipayByCodeReq.class);
        Call<InterfaceManager.BindAlipayByCodeRsp> call = callFunc.get(
                tUid,
                alipayName,
                alipay,
                code,
                phoneStr);
        call.enqueue(new Callback<InterfaceManager.BindAlipayByCodeRsp>() {
            @Override
            public void onResponse(Call<InterfaceManager.BindAlipayByCodeRsp> call, final Response<InterfaceManager.BindAlipayByCodeRsp> response) {
                {
                    hideProgressMum();
                    if (response.body() != null) {
                        int status = response.body().status;
                        if (-1 == status) {
                            ToastTools.showShort(ModifyCashWayActivity.this, "验证码错误");
                        } else if (0 == status) {
                            ToastTools.showShort(ModifyCashWayActivity.this, "修改失败");
                        } else if (1 == status) {
                            ToastTools.showShort(ModifyCashWayActivity.this, "修改成功");

                            ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
                                @Override
                                public void handleMessage(Message msg) {
                                    ModifyCashWayActivity.this.finish();
                                }
                            }).sendEmptyMessage(0);

                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<InterfaceManager.BindAlipayByCodeRsp> call, Throwable t) {
                t.printStackTrace();
                hideProgressMum();
                ToastTools.showShort(ModifyCashWayActivity.this, "请求失败");
            }
        });
    }
}
