package com.eryue.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eryue.AccountUtil;
import com.eryue.R;
import com.library.util.StringUtils;
import com.library.util.ToastTools;

import net.DataCenterManager;
import net.InterfaceManager;
import net.KeyFlag;

import java.security.MessageDigest;

import base.BaseActivity;
import base.BaseActivityTransparent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static net.KeyFlag.INVITE_CODE_KEY;
import static net.KeyFlag.PHONE_KEY;

/**
 * 登录
 *
 * 测试账号：13912912962 密码：19930317ydx3610
 */

public class LoginActivity extends BaseActivityTransparent implements View.OnClickListener{
    private EditText phoneET;
    private EditText passwordET;

    private TextView loginTV;
    private TextView fpsTV;
    private TextView registerTV;

    private String baseIP = AccountUtil.getBaseIp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationBar.setTitle("立即登录");

        setContentView(R.layout.activity_login);

        initViews();
    }


    private void initViews() {

        phoneET = (EditText)findViewById(R.id.phone);
        String phone = DataCenterManager.Instance().get(this, PHONE_KEY);
        if (!TextUtils.isEmpty(phone)) {
            phoneET.setText(phone);
        }
        passwordET = (EditText)findViewById(R.id.password);

        loginTV = (TextView)findViewById(R.id.sure);
        fpsTV = (TextView)findViewById(R.id.forget_password);
        registerTV = (TextView)findViewById(R.id.register);

        loginTV.setOnClickListener(this);
        fpsTV.setOnClickListener(this);
        registerTV.setOnClickListener(this);
    }

    // 登录
    private void login() {
        final String userName = phoneET.getText().toString();
        final String password = passwordET.getText().toString();

        final Context context = this;

        if (TextUtils.isEmpty(userName)
                || TextUtils.isEmpty(password)) {
            // 提示信息为不完整
            return;
        }
        final String passwordEnc = getSha1(password + "s4lt");
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.LoginReq callFunc = retrofit.create(InterfaceManager.LoginReq.class);
        Call<InterfaceManager.LoginResponse> call = callFunc.get(userName, passwordEnc);
        call.enqueue(new Callback<InterfaceManager.LoginResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.LoginResponse> call, Response<InterfaceManager.LoginResponse> response) {
                if (null!=response.body()&&response.body().status == 1) {
                    DataCenterManager.Instance().save(context, PHONE_KEY, userName);
                    DataCenterManager.Instance().save(context, KeyFlag.PS_KEY, password);
                    DataCenterManager.Instance().save(context, KeyFlag.PSENC_KEY, passwordEnc);
                    if (response.body().result != null) {
                        //保存邀请码
                        String code = response.body().result.code;
                        if (!TextUtils.isEmpty(code)){
                            long uid = response.body().result.uid;
                            DataCenterManager.Instance().save(context, KeyFlag.REAL_UID_KEY, uid + "");
                            DataCenterManager.Instance().save(context, INVITE_CODE_KEY, code);

                            DataCenterManager.Instance().save(context, KeyFlag.YuGuJiFen, response.body().result.yg + "");
                            DataCenterManager.Instance().save(context, KeyFlag.LeiJiYouXiao, response.body().result.yx + "");
                            DataCenterManager.Instance().save(context, KeyFlag.LeiJiDuiHuan, response.body().result.dh + "");
                            DataCenterManager.Instance().save(context, KeyFlag.ZhangHuYuE, response.body().result.balance + "");

                            DataCenterManager.Instance().save(context, KeyFlag.JueSe, response.body().result.group_name);
                            DataCenterManager.Instance().save(context, KeyFlag.TouXiangUrl, response.body().result.pictUrl);
                            DataCenterManager.Instance().save(context, KeyFlag.PingTai, response.body().result.terrace);

                            Intent intent = new Intent("login_finish");
                            intent.putExtra("finish_type", "account_login");
                            getApplicationContext().sendBroadcast(intent);
                            DataCenterManager.Instance().save(context, KeyFlag.USER_NAME_KEY, response.body().result.userName);

                            DataCenterManager.Instance().save(context, KeyFlag.WECHAT_OPON_ID, response.body().result.appWxOpenId);

                            ToastTools.showShort(context, "登录成功");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 1000);


                        }
                    }

                } else if (null!=response.body()&&response.body().status == -1){
                    ToastTools.showShort(context, "账号被禁用，请联系客服");
                } else if (null!=response.body()&&response.body().status == 0){
                    ToastTools.showShort(context, "用户名或密码错误");
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.LoginResponse> call, Throwable t) {
                t.printStackTrace();
                ToastTools.showShort(context, "请求失败，稍后重试");
            }
        });
    }


    // sha1加密
    public static String getSha1(String str){
        if(str==null||str.length()==0) {
            return null;
        }
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
                'a','b','c','d','e','f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j*2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(loginTV)) {
            login();
        } else if (v.equals(fpsTV)) {
            Intent intent = new Intent(this, ForgetPasswordActivity.class);
            startActivity(intent);
        } else if (v.equals(registerTV)) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }
}
