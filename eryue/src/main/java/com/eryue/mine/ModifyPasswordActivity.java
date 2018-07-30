package com.eryue.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eryue.AccountUtil;
import com.eryue.R;
import com.library.util.ToastTools;

import net.MineInterface;

import base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dazhou on 2018/5/13.
 * 密码设置
 */

public class ModifyPasswordActivity extends BaseActivity   implements View.OnClickListener{
    private EditText oldPSET;
    private EditText newPSET;
    private EditText againPSET;
    private TextView sureTV;

    private String oldPs;
    private String newPs;
    private String newPsAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        setupViews();
        navigationBar.setTitle("密码设置");
    }

    private void setupViews() {


        oldPSET = findViewById(R.id.input_old_ps);

        newPSET = findViewById(R.id.input_new_ps);

        againPSET = findViewById(R.id.input_ps_again);

        sureTV = findViewById(R.id.sure);
        sureTV.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == sureTV) {

            oldPs = oldPSET.getText().toString();
            newPs = newPSET.getText().toString();
            newPsAgain = againPSET.getText().toString();
            if (TextUtils.isEmpty(oldPs) || TextUtils.isEmpty(newPs) || TextUtils.isEmpty(newPsAgain)) {
                ToastTools.showShort(getBaseContext(), "请输入完整的信息进行修改");
                return;
            }
            if (newPs == null || newPs.length() < 6) {
                ToastTools.showShort(getBaseContext(), "新密码长度不少于6位");
                return;
            }
            if (!newPs.equals(newPsAgain)) {
                ToastTools.showShort(getBaseContext(), "两次新密码不一致，请进行检查");
                return;
            }

            reqModifyPassword();
        }
    }


    private void reqModifyPassword() {
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }
        showProgressMum();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.UpdatePwdReq callFunc = retrofit.create(MineInterface.UpdatePwdReq.class);
        Call<MineInterface.UpdatePwdRsp> call = callFunc.get(
                oldPs,
                newPs,
                AccountUtil.getUID());
        call.enqueue(new Callback<MineInterface.UpdatePwdRsp>() {
            @Override
            public void onResponse(Call<MineInterface.UpdatePwdRsp> call, final Response<MineInterface.UpdatePwdRsp> response) {
                {
                    hideProgressMum();
                    if (response.body() != null) {
                        int status = response.body().status;
                        if (0 == status) {
                            ToastTools.showShort(ModifyPasswordActivity.this, "原始密码不对");
                        } else if (1 == status) {
                            ToastTools.showShort(ModifyPasswordActivity.this, "修改成功");
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<MineInterface.UpdatePwdRsp> call, Throwable t) {
                t.printStackTrace();
                hideProgressMum();
                ToastTools.showShort(ModifyPasswordActivity.this, "请求失败");
            }
        });
    }

}
