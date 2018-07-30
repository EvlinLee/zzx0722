package com.eryue.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.eryue.BaseApplication;
import com.eryue.R;
import com.eryue.util.SharedPreferencesUtil;

import net.DataCenterManager;

import base.BaseActivity;

import static net.KeyFlag.FIRST_OPEN;

/**
 * Created by enjoy on 2018/4/17.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        String isFirstOpen = SharedPreferencesUtil.getInstance().getString(FIRST_OPEN);
        // 如果是第一次启动，则先进入功能引导页
        if (TextUtils.isEmpty(isFirstOpen)) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }else{
            enterHomeActivity();
        }

        // 如果不是第一次启动app，则正常显示启动屏
//        setContentView(R.layout.activity_splash);

//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                enterHomeActivity();
//            }
//        }, 2000);
    }

    private void enterHomeActivity() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}
