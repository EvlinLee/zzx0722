package com.eryue.mine;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eryue.AccountUtil;
import com.eryue.R;
import com.library.util.ToastTools;


import net.DataCenterManager;
import net.KeyFlag;
import net.MineInterface;

import base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dazhou on 2018/5/13.
 * 修改联系方式
 */

public class ModifyContactActivity extends BaseActivity implements View.OnClickListener{

    private TextView phoneTV;
    private EditText wechatET;
    private EditText qqET;
    private EditText userNameET;
    private TextView sureTV;

    private String phone;
    private String wechat;
    private String qq;
    private String userName;
    private String originaUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_contact);
        navigationBar.setTitle("修改用户名");

        phone = getIntent().getStringExtra("phone");
        originaUserName = getIntent().getStringExtra("userName");

        setupViews();
    }

    private void setupViews() {


        phoneTV = findViewById(R.id.phone);
        if (!TextUtils.isEmpty(phone)) {
            phoneTV.setText(phone);
        }

        wechatET = findViewById(R.id.input_wechat);

        qqET = findViewById(R.id.input_qq);
        userNameET = findViewById(R.id.input_username);

        sureTV = findViewById(R.id.sure);
        sureTV.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == sureTV) {
            wechat = wechatET.getText().toString();
            qq = qqET.getText().toString();
            wechat = getIntent().getStringExtra("wx");
            qq = getIntent().getStringExtra("qq");

            userName = userNameET.getText().toString();

            if (TextUtils.isEmpty(userName)) {
                ToastTools.showShort(getBaseContext(), "请输入用户名");
                return;
            }

//            if ( ! TextUtils.isEmpty(originaUserName) && originaUserName.startsWith("user") &&
//                ! userName.startsWith("user")) {
//                ToastTools.showShort(getBaseContext(), "请输入以字母 user 为开头的用户名");
//                return;
//            } else if (! TextUtils.isEmpty(originaUserName) && ! originaUserName.startsWith("user") &&
//                     userName.startsWith("user")) {
//                ToastTools.showShort(getBaseContext(), "请输入不是以字母 user 为开头的用户名");
//                return;
//            }
            if (userName.startsWith("user")) {
                ToastTools.showShort(getBaseContext(), "请输入不是以字母 user 为开头的用户名");
                return;
            }

            else {
                reqModifyContact();
            }
        }
    }

    private void reqModifyContact() {
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }
        showProgressMum();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.UpdateUserInfoReq callFunc = retrofit.create(MineInterface.UpdateUserInfoReq.class);
        Call<MineInterface.UpdateUserInfoRsp> call = callFunc.get(
                phone,
                wechat,
                qq,
                AccountUtil.getUID(),
                userName);
        call.enqueue(new Callback<MineInterface.UpdateUserInfoRsp>() {
            @Override
            public void onResponse(Call<MineInterface.UpdateUserInfoRsp> call, final Response<MineInterface.UpdateUserInfoRsp> response) {
                {
                    hideProgressMum();
                    if (response.body() != null) {
                        int status = response.body().status;
                        if (-2 == status) {
                            ToastTools.showShort(ModifyContactActivity.this, "QQ号已经存在");
                        } else if (-1 == status) {
                            ToastTools.showShort(ModifyContactActivity.this, "微信号已经存在");
                        } else if (0 == status) {
                            ToastTools.showShort(ModifyContactActivity.this, "手机号已经存在");
                        } else if (1 == status) {
                            ToastTools.showShort(ModifyContactActivity.this, "修改成功");
                            DataCenterManager.Instance().save(ModifyContactActivity.this, KeyFlag.USER_NAME_KEY, userName);


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 100);
                        } else if (-3 == status) {
                            ToastTools.showShort(ModifyContactActivity.this, "用户名存在");
                        } else {
                            ToastTools.showShort(ModifyContactActivity.this, "修改失败");
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<MineInterface.UpdateUserInfoRsp> call, Throwable t) {
                t.printStackTrace();
                hideProgressMum();
                ToastTools.showShort(ModifyContactActivity.this, "请求失败");
            }
        });
    }
}
