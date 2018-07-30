package com.eryue.mine;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.R;
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

import static net.KeyFlag.INVITE_CODE_KEY;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    private EditText phoneET;
    private EditText codeET;
    private EditText passwordET;
    private EditText we_chat;
    private EditText nicheng;
    private TextView getCodeTV;

    private int second;

    private String inviteCode = AccountUtil.getInviteCode();
    private String baseIP = AccountUtil.getBaseIp();

    private View aggre;
    private View hint2;
    private View hint3;
    private ImageView agree_iv;

    private int aggreType = 0; // 0:同意， 1 不同意

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        navigationBar.setTitle("注册");

        setupViews();
    }

    private void setupViews() {
        phoneET = (EditText) findViewById(R.id.phone);
        codeET = (EditText) findViewById(R.id.code);
        passwordET = (EditText) findViewById(R.id.login_password);
        we_chat =  findViewById(R.id.we_chat);
        nicheng =  findViewById(R.id.nicheng);

        getCodeTV = (TextView) findViewById(R.id.get_code);
        getCodeTV.setOnClickListener(this);

        aggre = findViewById(R.id.aggre);
        aggre.setOnClickListener(this);

        hint2 = findViewById(R.id.hint2);
        hint2.setOnClickListener(this);

        hint3 = findViewById(R.id.hint3);
        hint3.setOnClickListener(this);

        we_chat = findViewById(R.id.we_chat);
        nicheng = findViewById(R.id.nicheng);
        agree_iv = findViewById(R.id.agree_iv);

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(phoneET.getText().toString(), passwordET.getText().toString(),
                        codeET.getText().toString(), inviteCode, we_chat.getText().toString(),
                        nicheng.getText().toString());
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

    // 注册接口
    private void register(final String phone, final String password, String code,
                          String inviteCode, String wx, String nickName) {
        if (TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(code)
                || TextUtils.isEmpty(inviteCode)
                || TextUtils.isEmpty(wx)
                || TextUtils.isEmpty(nickName)) {
            // 提示信息为不完整
            ToastTools.showShort(this, "请填写完整信息");
            return;
        }
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        if (password == null || password.length() < 6) {
            ToastTools.showShort(this, "密码请不要小于6位");
            return;
        }
        if (aggreType == 1) {
            ToastTools.showShort(this, "注册必须同意勾选同意");
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.RegisterReq callFunc = retrofit.create(InterfaceManager.RegisterReq.class);
        Call<InterfaceManager.RegisterResponse> call = callFunc.get(phone, password, code,
                inviteCode, nickName, wx);
        call.enqueue(new Callback<InterfaceManager.RegisterResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.RegisterResponse> call, Response<InterfaceManager.RegisterResponse> response) {
                // 成功
                if (null!=response.body()&&response.body().status == 1) {
                    DataCenterManager.Instance().save(RegisterActivity.this, KeyFlag.PHONE_KEY, phone);
                    DataCenterManager.Instance().save(RegisterActivity.this, KeyFlag.PS_KEY, password);
                    if (response.body().result != null) {
                        //保存邀请码
                        String code = response.body().result.code;
                        if (!TextUtils.isEmpty(code)){
                            long uid = response.body().result.uid;
                            DataCenterManager.Instance().save(RegisterActivity.this, KeyFlag.REAL_UID_KEY, uid + "");
                            DataCenterManager.Instance().save(RegisterActivity.this, INVITE_CODE_KEY, code);
                            ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
                                @Override
                                public void handleMessage(Message msg) {
                                    finish();
                                }
                            }).sendEmptyMessage(0);

                        }else{
                            ToastTools.showShort(RegisterActivity.this, "注册失败，请稍后重试");
                        }
                    }

                } else if (null!=response.body()&&response.body().status == 0){
                    ToastTools.showShort(RegisterActivity.this, "注册失败，请稍后重试");
                } else if (null!=response.body()&&response.body().status == -1){
                    ToastTools.showShort(RegisterActivity.this, "手机验证码错误");
                } else if (null!=response.body()&&response.body().status == -2){
                    ToastTools.showShort(RegisterActivity.this, "手机号码存在");
                } else if (null!=response.body()&&response.body().status == -3){
                    ToastTools.showShort(RegisterActivity.this, "用户存在");
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.RegisterResponse> call, Throwable t) {
                t.printStackTrace();
                ToastTools.showShort(RegisterActivity.this, "注册失败，请稍后重试");
            }
        });
    }

    // 注册短信验证码接口
    private void getPhoneCode(String phone) {
        if (TextUtils.isEmpty(baseIP)) {
            Toast.makeText(this, "baseIP 为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.GetPhoneCode callFunc = retrofit.create(InterfaceManager.GetPhoneCode.class);
        Call<InterfaceManager.PhoneCodeResponse> call = callFunc.get(phone);
        call.enqueue(new Callback<InterfaceManager.PhoneCodeResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.PhoneCodeResponse> call, Response<InterfaceManager.PhoneCodeResponse> response) {
                if (response.body() == null) {
                    Toast.makeText(RegisterActivity.this, "系统错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (null!=response.body()&&response.body().status == 1) {
                    Toast.makeText(RegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                } else if (null!=response.body()&&response.body().status == 0) {
                    Toast.makeText(RegisterActivity.this, "手机号已注册", Toast.LENGTH_SHORT).show();
                } else if (null!=response.body()&&response.body().status == -1) {
                    Toast.makeText(RegisterActivity.this, "系统错误", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "验证码发送失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.PhoneCodeResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "验证码发送失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler = null;
    }

    @Override
    public void onClick(View view) {
        if (view == getCodeTV) {
            getVerCode();
        } else if (view == aggre) {
            if (aggreType == 0) {
                aggreType = 1;
                agree_iv.setBackgroundResource(R.drawable.bg_aggre_no);
            } else if (aggreType == 1) {
                aggreType = 0;
                agree_iv.setBackgroundResource(R.drawable.icon_agreed);
            }
        }  else if (view == hint2) {
            Intent intent = new Intent(RegisterActivity.this, XieYiActivity.class);
            intent.putExtra("type", 0);
            startActivity(intent);
        } else if (view == hint3) {
            Intent intent = new Intent(RegisterActivity.this, XieYiActivity.class);
            intent.putExtra("type", 1);
            startActivity(intent);
        }
    }

    private void getVerCode() {
        String phone = phoneET.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(RegisterActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        getCodeTV.setEnabled(false);
        second = 60;
        getCodeTV.setText(second + "秒");
        handler.postDelayed(runnable, 1000);
        getPhoneCode(phone);
    }

}
