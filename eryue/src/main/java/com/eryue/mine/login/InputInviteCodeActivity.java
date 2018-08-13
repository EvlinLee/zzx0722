package com.eryue.mine.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eryue.BaseApplication;
import com.eryue.R;
import com.eryue.home.HomeRequestPresenter;
import com.eryue.mine.LoginActivity;
import com.eryue.util.Logger;
import com.library.util.StringUtils;
import com.library.util.ToastTools;

import net.DataCenterManager;
import net.InterfaceManager;
import net.KeyFlag;
import net.MineInterface;
import net.NetManager;

import base.BaseActivity;
import base.BaseActivityTransparent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static net.InterfaceManager.baseURL;
import static net.KeyFlag.BASE_IP_KEY_INPUT;
import static net.KeyFlag.BASE_IP_VALUE_KEY_INPUT;
import static net.KeyFlag.INVITE_CODE_KEY_INPUT;
import static net.KeyFlag.NORMAL_UID_KEY;


/**
 * Created by dazhou on 2018/7/7.
 * 输入邀请码
 */

public class InputInviteCodeActivity extends BaseActivityTransparent implements View.OnClickListener {
    private EditText inviteCodeET;
    private TextView nextStepTV;
    private String inviteCodeStr;

    private String loginType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input_invite_code);

        loginType = getIntent().getStringExtra("loginType");

        setupViews();
        initFinishReceiver();
    }

    private boolean isAccountLogin() {
        if (!TextUtils.isEmpty(loginType) && "account_login".equals(loginType)) {
            return true;
        }

        return false;
    }

    private void setupViews() {
        inviteCodeET = findViewById(R.id.invite_code);
        nextStepTV = findViewById(R.id.next_step);
        nextStepTV.setOnClickListener(this);

        inviteCodeET.addTextChangedListener(new TextWatcher() {
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
                finish();
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view == nextStepTV) {
            inviteCodeStr = inviteCodeET.getText().toString();
            if (TextUtils.isEmpty(inviteCodeStr)) {
                return;
            }

            if (isAccountLogin()) {
                getIpByCode(inviteCodeStr);
            } else {
                getIp();
            }
        }
    }

//    调用 openid 绑定 ip 接口
//    传入 邀请码，openid，
//    获取ip
    private void getIp() {
        String openid = DataCenterManager.Instance().get(InputInviteCodeActivity.this, KeyFlag.WECHAT_OPON_ID);
        if (TextUtils.isEmpty(openid)) {
            ToastTools.showShort(InputInviteCodeActivity.this, "openid为空");
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.yifengdongli.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.AddOpenIdBindReq req = retrofit.create(MineInterface.AddOpenIdBindReq.class);
        Call<MineInterface.AddOpenIdBindRsp> call = req.get(inviteCodeStr, openid);
        call.enqueue(new Callback<MineInterface.AddOpenIdBindRsp>() {
            @Override
            public void onResponse(Call<MineInterface.AddOpenIdBindRsp> call, Response<MineInterface.AddOpenIdBindRsp> response) {
                try {
                    if (response == null) {
                        Toast.makeText(InputInviteCodeActivity.this, "获取数据为空", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Logger.getInstance(InputInviteCodeActivity.this).writeLog_new("login", "login", "调用 openid 绑定 ip 接口 status: " + response.body().status);

                    if (response.body().status == 1) {
                        DataCenterManager.Instance().save(InputInviteCodeActivity.this, KeyFlag.IP_BIND_OPEN_ID, "http://" + response.body().ip + "/");
                        DataCenterManager.Instance().save(InputInviteCodeActivity.this, KeyFlag.INVITE_CODE_KEY_INPUT, inviteCodeStr);

                        gotoBindPhone();
                    }
                    else {
                        Toast.makeText(InputInviteCodeActivity.this, "邀请码无效", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(InputInviteCodeActivity.this, "解析数据失败，稍后重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MineInterface.AddOpenIdBindRsp> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(InputInviteCodeActivity.this, "调用 openid 绑定 ip 接口，稍后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 邀请码获取IP接口
    public void getIpByCode(final String code) {
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(InputInviteCodeActivity.this, "请输入邀请码", Toast.LENGTH_SHORT).show();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.GetIpByInvitationCode getIpByInvitationCode = retrofit.create(InterfaceManager.GetIpByInvitationCode.class);
        Call<InterfaceManager.IpResponse> call = getIpByInvitationCode.get(code);
        call.enqueue(new Callback<InterfaceManager.IpResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.IpResponse> call, Response<InterfaceManager.IpResponse> response) {
                if (null!=response.body()&&response.body().status == 1) {
                    if (response.body().result != null && response.body().result.size() > 0) {
                        InterfaceManager.IpResponse.IpInfo ipInfo = response.body().result.get(0);
                        String baseIP = "http://" + ipInfo.ip + "/";
                        long uid = ipInfo.uid;
                        DataCenterManager.Instance().save(BaseApplication.getInstance(), NORMAL_UID_KEY, uid + "");
                        DataCenterManager.Instance().save(BaseApplication.getInstance(), BASE_IP_KEY_INPUT, baseIP);
                        DataCenterManager.Instance().save(BaseApplication.getInstance(), BASE_IP_VALUE_KEY_INPUT, ipInfo.ip);

                        if (!TextUtils.isEmpty(code)) {
                            //全局邀请码赋值
                            DataCenterManager.Instance().save(BaseApplication.getInstance(), INVITE_CODE_KEY_INPUT, code);
                        }

                        Intent intent = new Intent(InputInviteCodeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(InputInviteCodeActivity.this, "邀请码错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.IpResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    // 去绑定手机号
    private void gotoBindPhone() {
        Intent intent = new Intent(InputInviteCodeActivity.this, BindPhoneActivity.class);
        startActivity(intent);
    }


    private void checkInput(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence) || TextUtils.isEmpty(charSequence.toString())) {
            nextStepTV.setBackgroundResource(R.drawable.login_btn);
            return;
        }

        nextStepTV.setBackgroundResource(R.drawable.login_btn_pre);
    }
}
