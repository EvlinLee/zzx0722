package com.eryue.mine;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eryue.AccountUtil;
import com.eryue.R;
import com.library.util.StringUtils;

import net.DataCenterManager;
import net.InterfaceManager;
import net.KeyFlag;

import base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener{

    private EditText phoneET;
    private EditText invoteCodeET;
    private EditText codeET;
    private EditText passwordET;
    private EditText passwordAgainET;

    private TextView getCodeTV;

    private int second;

    private String inviteCode = AccountUtil.getInviteCode();
    private String serverURL = AccountUtil.getServerURL();
    private String baseIP = AccountUtil.getBaseIp();
    private long uid = StringUtils.valueOfLong(AccountUtil.getUID());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        navigationBar.setTitle("忘记密码");

        initViews();
    }

    private void initViews() {
        phoneET = (EditText) findViewById(R.id.phone);
        invoteCodeET = (EditText) findViewById(R.id.invite_code);
        codeET = (EditText) findViewById(R.id.code);
        passwordET = (EditText) findViewById(R.id.new_password);
        passwordAgainET = (EditText) findViewById(R.id.password_again);

        getCodeTV = (TextView) findViewById(R.id.get_code);
        getCodeTV.setOnClickListener(this);

        findViewById(R.id.sure).setOnClickListener(this);

//        String inviteCode = DataCenterManager.Instance().get(this, KeyFlag.INVITE_CODE_KEY);
        if (!TextUtils.isEmpty(inviteCode)) {
            invoteCodeET.setText(inviteCode);
        }
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

    @Override
    public void onClick(View v) {

        if (v.equals(getCodeTV)) {
            String phone = phoneET.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                return;
            }

            getCodeTV.setEnabled(false);
            second = 60;
            getCodeTV.setText(second + "秒");
            handler.postDelayed(runnable, 1000);
            getPhoneCode(phoneET.getText().toString());
        } else if (v.equals(findViewById(R.id.sure))) {
            updatePassword();
        }
    }

    private void updatePassword() {
        String newPS = passwordET.getText().toString();
        if (newPS != null && newPS.length() < 6) {
            Toast.makeText(this, "密码不能少于6位", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!passwordET.getText().toString().equalsIgnoreCase(passwordAgainET.getText().toString())) {
            Toast.makeText(this, "两次密码不相同", Toast.LENGTH_SHORT).show();
            return;
        }
        final String phone = phoneET.getText().toString();
        final String password = passwordET.getText().toString();
        String code = codeET.getText().toString();
//        String inviteCode = invoteCodeET.getText().toString();
        if (TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(code)||
                TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请填写完整内容", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(baseIP)) {
            Toast.makeText(this, "baseIP 为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final Context context = this;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.UpdatePasswordReq callFunc = retrofit.create(InterfaceManager.UpdatePasswordReq.class);
        Call<InterfaceManager.UpdatePasswordResponse> call = callFunc.get(phone, password, code);
        call.enqueue(new Callback<InterfaceManager.UpdatePasswordResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.UpdatePasswordResponse> call, Response<InterfaceManager.UpdatePasswordResponse> response) {
                if (null!=response.body()&&response.body().status == 1) {
                    DataCenterManager.Instance().save(context, KeyFlag.PHONE_KEY, phone);

                    Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                } else if (null!=response.body()&&response.body().status == 0){
                    Toast.makeText(context, "验证码不正确！", Toast.LENGTH_SHORT).show();
                } else if (null!=response.body()&&response.body().status == -1){
                    Toast.makeText(context, "系统错误！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "修改失败，稍后再试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.UpdatePasswordResponse> call, Throwable t) {
                Toast.makeText(context, "验证码发送失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    private void getPhoneCode(String phone) {
        if (TextUtils.isEmpty(baseIP)) {
            Toast.makeText(this, "baseIP 为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final Context context = this;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.GetPhoneCode2 callFunc = retrofit.create(InterfaceManager.GetPhoneCode2.class);
        Call<InterfaceManager.PhoneCode2Response> call = callFunc.get(phone);
        call.enqueue(new Callback<InterfaceManager.PhoneCode2Response>() {
            @Override
            public void onResponse(Call<InterfaceManager.PhoneCode2Response> call, Response<InterfaceManager.PhoneCode2Response> response) {
                if (null!=response.body()&&response.body().status == 1) {
                    Toast.makeText(context, "验证码已发送", Toast.LENGTH_SHORT).show();
                } else if (null!=response.body()&&response.body().status == 0){
                    Toast.makeText(context, "手机号不存在！", Toast.LENGTH_SHORT).show();
                } else if (null!=response.body()&&response.body().status == -1){
                    Toast.makeText(context, "系统错误！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "验证码发送失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.PhoneCode2Response> call, Throwable t) {
                Toast.makeText(context, "验证码发送失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}
