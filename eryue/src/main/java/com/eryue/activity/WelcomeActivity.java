package com.eryue.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eryue.AccountUtil;
import com.eryue.R;
import com.eryue.util.BaseNetHandler;
import com.eryue.util.SharedPreferencesUtil;
import com.eryue.util.counttime.CountDownTimerSupport;
import com.eryue.util.counttime.OnCountDownTimerListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.security.MessageDigest;

import base.BaseActivity;
import base.BaseActivityTransparent;

public class WelcomeActivity extends BaseActivityTransparent {
    DisplayImageOptions optionsSmall = new DisplayImageOptions.Builder()
            .cacheInMemory(false)   //设置图片不缓存于内存中
            .bitmapConfig(Bitmap.Config.RGB_565)    //设置图片的质量
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)    //设置图片的缩放类型，该方法可以有效减少内存的占用
            .showImageOnLoading(R.drawable.img_default_contract)
            .showImageOnFail(R.drawable.img_default_contract)
            .considerExifParams(true).build();

    private ImageView iv_welcome;

    private TextView tv_second;
    private LinearLayout layout_second;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    };

    private CountDownTimerSupport timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        hideNavigationBar(true);
        setContentView(R.layout.activity_welcome);
        iv_welcome = (ImageView) findViewById(R.id.iv_wecome);
//		ImageLoader.getInstance().displayImage("drawable://" + R.drawable.bg_welcome,iv_welcome,optionsSmall);
        Glide.with(this).load(R.drawable.bg_welcome).asBitmap().into(iv_welcome);

//		handler.sendEmptyMessageDelayed(0, 2 * 1000);

        tv_second = (TextView) findViewById(R.id.tv_second);
        layout_second = (LinearLayout) findViewById(R.id.layout_second);

        layout_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != timer) {
                    timer.stop();
                }

                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        });

        timer = new CountDownTimerSupport();
        timer.setMillisInFuture(CountSecond + 50);
        timer.setCountDownInterval(CountUnit);
        timer.setOnCountDownTimerListener(new OnCountDownTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                if (null != tv_second) {
                    Log.d("libo", "millisUntilFinished=" + millisUntilFinished);
                    int second = (int) Math.round(millisUntilFinished / 1000.0);
                    tv_second.setText(String.valueOf(second));
                }
            }

            @Override
            public void onFinish() {
                if (null != WelcomeActivity.this && !WelcomeActivity.this.isFinishing()) {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    finish();
                }
            }


        });

        //默认打开日志
        BaseNetHandler.DEBUG = true;
        SharedPreferencesUtil.getInstance().saveBoolean(
                BaseNetHandler.SWITCH_LOGO, true);
    }

    // sha1加密
    public static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
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
    protected void onResume() {
        super.onResume();
        if (null != timer) {
            timer.resume();
        }

//        boolean isLogin = AccountUtil.checkStatus();
//        if (!isLogin){
//            AccountUtil.autoLogin();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != timer) {
            timer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != timer) {
            timer.stop();
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        tv_second.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (null != timer) {
                    timer.start();
                }
            }
        }, 500);

    }

    /**
     * 倒计时时间
     */
    private final int CountSecond = 2000;
    /**
     * 步长
     */
    private final int CountUnit = 1000;


}
