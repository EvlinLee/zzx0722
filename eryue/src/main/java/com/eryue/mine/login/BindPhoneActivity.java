package com.eryue.mine.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eryue.AccountUtil;
import com.eryue.R;
import com.eryue.mine.BindAlipayActivity;
import com.library.util.ToastTools;

import net.DataCenterManager;
import net.KeyFlag;
import net.MineInterface;

import base.BaseActivity;
import base.BaseActivityTransparent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dazhou on 2018/7/7.
 * 绑定手机号
 */

public class BindPhoneActivity extends BaseActivityTransparent implements View.OnClickListener {

    private EditText phoneET;
    private TextView nextStepTV;
    private String phoneStr;
    private String codeStr;
    private EditText codeET;
    private TextView getCodeTV;
    private TextView tvDelete;
    private String baseIP = AccountUtil.getBaseIp();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bind_phone);

        setupViews();

        initFinishReceiver();
    }

    private void initFinishReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("login_finish");
        registerReceiver(mFinishReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mFinishReceiver);
    }

    public BroadcastReceiver mFinishReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("login_finish".equals(intent.getAction())) {
                DataCenterManager.Instance().save(context, KeyFlag.PHONE_KEY, phoneStr);

                finish();
            }
        }
    };

    private void setupViews() {
        tvDelete = findViewById(R.id.tv_delete);
        phoneET = findViewById(R.id.phone);
        nextStepTV = findViewById(R.id.next_step);
        nextStepTV.setOnClickListener(this);
        codeET = findViewById(R.id.code);
        getCodeTV = findViewById(R.id.second_hint);
        getCodeTV.setOnClickListener(this);

  /*      TextView hint1 = findViewById(R.id.hint1);
        hint1.setText(hint1.getText() + phoneStr);*/

        phoneET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInput(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        phoneStr = phoneET.getText().toString();
        codeStr = codeET.getText().toString();
        if (view == nextStepTV) {

            if (TextUtils.isEmpty(phoneStr) || phoneStr.length() != 11) {
                ToastTools.showShort(BindPhoneActivity.this, "请输入正确的手机号");
                return;
            }
            if (TextUtils.isEmpty(codeStr)) {
                ToastTools.showShort(BindPhoneActivity.this, "请输入验证码");
                return;
            }
            vertifyCode(phoneStr,codeStr);
        } else if(view == getCodeTV){
            if (TextUtils.isEmpty(phoneStr) || phoneStr.length() != 11) {
                ToastTools.showShort(BindPhoneActivity.this, "请输入正确的手机号");
                return;
            }
            getVerCode();
        }
    }


    private void checkInput(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence) || TextUtils.isEmpty(charSequence.toString())) {
            nextStepTV.setBackgroundResource(R.drawable.login_btn);
            nextStepTV.setEnabled(false);

            tvDelete.setVisibility(View.INVISIBLE);
            return;
        }

        nextStepTV.setBackgroundResource(R.drawable.login_btn_pre);
        tvDelete.setVisibility(View.VISIBLE);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneET.setText("");
            }
        });

    }

    // 判断邀请码是否正确
    private void vertifyCode(String phone,String code) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AccountUtil.getBaseIp())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String openid = DataCenterManager.Instance().get(this, KeyFlag.WECHAT_OPON_ID);
        String inviteCode = DataCenterManager.Instance().get(this, KeyFlag.INVITE_CODE_KEY_INPUT);


        MineInterface.JudgeReq req = retrofit.create(MineInterface.JudgeReq.class);
        String pictUrl = DataCenterManager.Instance().get(BindPhoneActivity.this, KeyFlag.IMAGE_RUL_WX);
        Call<MineInterface.JudgeRsp> call = req.get(openid, inviteCode, code, phone, pictUrl);
        call.enqueue(new Callback<MineInterface.JudgeRsp>() {
            @Override
            public void onResponse(Call<MineInterface.JudgeRsp> call, Response<MineInterface.JudgeRsp> response) {
                try {
                    if (response == null) {
                        Toast.makeText(BindPhoneActivity.this, "验证返回的数据为空", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (response.body().status == 1) {
                        MineInterface.JudgeInfo judgeInfo = response.body().result;

                        Log.d("zdz", "login success: ");
                        AccountUtil.saveUserInfo(judgeInfo);

                        ToastTools.showShort(BindPhoneActivity.this, "登录成功");

                        Intent intent = new Intent("login_finish");
                        intent.putExtra("finish_type", "wx_login");
                        getApplicationContext().sendBroadcast(intent);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);
                    }
                    else {
                        Log.e("JudgeRsp判断验证码", " "+response.body().status);
                        Toast.makeText(BindPhoneActivity.this, "验证失败，稍后重试", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(BindPhoneActivity.this, "解析验证返回的数据失败，稍后重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MineInterface.JudgeRsp> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(BindPhoneActivity.this, "获取根据openid判断有没有用户数据失败，稍后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int second;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (second == 1) {
                getCodeTV.setEnabled(true);
                getCodeTV.setText("获取验证码");
            } else {
                second --;
                getCodeTV.setText(second + "s");
                getCodeTV.setTextColor(Color.parseColor("#999999"));
                if (handler != null) {
                    handler.postDelayed(this, 1000);
                }
            }
        }
    };

    private void getVerCode() {
        getCodeTV.setEnabled(false);
        second = 60;
        getCodeTV.setText(second + "s");
        handler.postDelayed(runnable, 1000);
        getPhoneCode(phoneStr);
    }

    private void getPhoneCode(String phone) {
        if (TextUtils.isEmpty(baseIP)) {
            Toast.makeText(this, "baseIP 为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MineInterface.SendCodeForOpenIdReq callFunc = retrofit.create(MineInterface.SendCodeForOpenIdReq.class);
        Call<MineInterface.BaseRsp> call = callFunc.get(phone);
        call.enqueue(new Callback<MineInterface.BaseRsp>() {
            @Override
            public void onResponse(Call<MineInterface.BaseRsp> call, Response<MineInterface.BaseRsp> response) {
                if (response.body() == null) {
                    Toast.makeText(BindPhoneActivity.this, "系统错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (null!=response.body()&&response.body().status == 1) {
                    Toast.makeText(BindPhoneActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                } else if (null!=response.body()&&response.body().status == -1) {
                    Toast.makeText(BindPhoneActivity.this, "系统错误", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(BindPhoneActivity.this, "验证码发送失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MineInterface.BaseRsp> call, Throwable t) {
                Toast.makeText(BindPhoneActivity.this, "验证码发送失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}
