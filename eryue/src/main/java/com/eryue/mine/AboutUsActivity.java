package com.eryue.mine;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.eryue.AccountUtil;
import com.eryue.R;
import com.eryue.Update;
import com.eryue.activity.HideSettingActivity;
import com.library.util.CommonFunc;
import com.library.util.ToastTools;

import java.io.File;
import java.math.BigDecimal;

import base.BaseActivity;


public class AboutUsActivity extends BaseActivity implements Update.UpdateListener {


    public static final String checkUpdateUrl = "http://www.yifengdongli.com/app/checkZzxAndriodVersion.do";

    public static final String smallversion = "136";

    private ImageView header_iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        navigationBar.setTitle("关于我们");


        String version = getLocalVersion();
        TextView curVersionTV = (TextView) findViewById(R.id.curr_version);
        curVersionTV.setText("当前版本：V" + version);


        Update.getInstance(this).setUpdateListener(this);

        findViewById(R.id.layout_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPinDD();
            }
        });

        findViewById(R.id.check_version).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检测升级
                Update.getInstance(AboutUsActivity.this).checkUpdate(checkUpdateUrl);
            }
        });

        header_iv = findViewById(R.id.header_iv);

        header_iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(AboutUsActivity.this, HideSettingActivity.class);
                startActivity(intent);
                return false;
            }
        });
        String imgUrl = AccountUtil.getBaseIp() + "cms/aboutUs.jpg";
        Glide.with(this).load(imgUrl).error(R.drawable.img_default_contract).into(header_iv);

    }


    private void gotoPinDD(){

//        boolean hasInstalled = CommonFunc.checkHasInstalledApp(this, "com.xunmeng.pinduoduo");
//
//        if (hasInstalled){
//            String content = "pinduoduo://com.xunmeng.pinduoduo/duo_coupon_landing.html?goods_id=580820933&pid=10001_20999&t=JDj7m0HqSXQbTTWKnb0jjHkWGN3zVjAa9Hs5ZUD0O0s=";
//            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(content));
//            startActivity(intent);
//        }else{
//            //跳转网页
//            ToastTools.showShort(this,"跳转拼多多网页版");
//        }

    }


    private String getLocalVersion() {
        String localVersion = "";
        try {
            PackageInfo packageInfo = getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localVersion;
    }


    @Override
    public void onUpdateBack(final boolean needUpdate) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 检查版本号
                if (needUpdate){
//                    ToastTools.showShort(AboutUsActivity.this,"检测到新版本，下载中...");
                    Update.getInstance(AboutUsActivity.this).downLoadFile(true);

                } else{
                    ToastTools.showShort(AboutUsActivity.this,"当前已经是最新版本");
                }

            }
        });

    }

    @Override
    public void onUpdateError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastTools.showShort(AboutUsActivity.this,"检测失败，请稍后重试");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Update.getInstance(this).setUpdateListener(null);
    }
}
