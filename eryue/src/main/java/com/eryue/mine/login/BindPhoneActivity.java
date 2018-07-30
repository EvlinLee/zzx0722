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

import com.eryue.R;
import com.eryue.mine.BindAlipayActivity;
import com.library.util.ToastTools;

import net.DataCenterManager;
import net.KeyFlag;

import base.BaseActivity;
import base.BaseActivityTransparent;

/**
 * Created by dazhou on 2018/7/7.
 * 绑定手机号
 */

public class BindPhoneActivity extends BaseActivityTransparent implements View.OnClickListener {

    private EditText phoneET;
    private TextView nextStepTV;
    private String phoneStr;

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
        phoneET = findViewById(R.id.phone);
        nextStepTV = findViewById(R.id.next_step);
        nextStepTV.setOnClickListener(this);

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
        if (view == nextStepTV) {
            phoneStr = phoneET.getText().toString();
            if (TextUtils.isEmpty(phoneStr) || phoneStr.length() != 11) {
                ToastTools.showShort(BindPhoneActivity.this, "请输入正确的手机号");
                return;
            }

            Intent intent = new Intent(BindPhoneActivity.this, VertifyCodeActivity.class);
            intent.putExtra("phone", phoneStr);
            startActivity(intent);
        }
    }


    private void checkInput(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence) || TextUtils.isEmpty(charSequence.toString())) {
            nextStepTV.setBackgroundResource(R.drawable.bg_invite_code_0);
            return;
        }

        nextStepTV.setBackgroundResource(R.drawable.bg_invite_code_1);
    }
}
