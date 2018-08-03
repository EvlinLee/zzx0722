package com.eryue.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.R;
import com.library.util.ToastTools;

import net.MineInterface;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;

import base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dazhou on 2018/5/2.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private TextView cache_text;

    private View clear_con;
    private View modify0;
    private View modify1;

    private TextView phone;
    private TextView alipay;
    private TextView alipay_name;
    private TextView userName;

    /**
     * 用户ID
     */
    private TextView userId;
    /**
     * 退出登录
     */
    private TextView loginOut;

    private MineInterface.SearchUserInfoRsp.UserInfo userInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        navigationBar.setTitle("设置");

        setupViews();
//        getData();
    }

    private void setupViews() {
        cache_text = (TextView) findViewById(R.id.cache_text);
        cache_text.setText(getCacheSize(this));

        clear_con = findViewById(R.id.clear_con);
        clear_con.setOnClickListener(this);

        modify0 = findViewById(R.id.modify_0);
        modify0.setOnClickListener(this);

        modify1 = findViewById(R.id.modify_1);
        modify1.setOnClickListener(this);

        phone = findViewById(R.id.phone);
        alipay = findViewById(R.id.alipay);
        alipay_name = findViewById(R.id.alipay_name);
        userName = findViewById(R.id.nick_name);
        userId = findViewById(R.id.id_tv);

        loginOut = findViewById(R.id.login_out);
        loginOut.setOnClickListener(this);
    }

    public void getData() {
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }
        showProgressMum();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.SearchUserInfoReq callFunc = retrofit.create(MineInterface.SearchUserInfoReq.class);
        Call<MineInterface.SearchUserInfoRsp> call = callFunc.get(AccountUtil.getUID());
        call.enqueue(new Callback<MineInterface.SearchUserInfoRsp>() {
            @Override
            public void onResponse(Call<MineInterface.SearchUserInfoRsp> call, final Response<MineInterface.SearchUserInfoRsp> response) {
                {
                    hideProgressMum();
                    if (response.body() != null) {
                        userInfo = response.body().result;
                        refreshView();
                    }
                }

            }

            @Override
            public void onFailure(Call<MineInterface.SearchUserInfoRsp> call, Throwable t) {
                t.printStackTrace();
                hideProgressMum();
                ToastTools.showShort(SettingActivity.this, "请求失败");
            }
        });
    }

    private void refreshView() {
        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {

                modify0.setVisibility(View.GONE);

                if (userInfo == null) {
                    return;
                }
                phone.setText(userInfo.phone);
                alipay.setText(userInfo.zfb);
                alipay_name.setText(userInfo.realName);
                userName.setText(userInfo.userName);
                userId.setText(AccountUtil.getUID());

                if (!TextUtils.isEmpty(userInfo.userName) && userInfo.userName.startsWith("user")) {
                    modify0.setVisibility(View.VISIBLE);
                }

            }
        }).sendEmptyMessage(0);
    }


    @Override
    public void onClick(View view) {
        if (view == clear_con) {
            ToastTools.showShort(SettingActivity.this, "清理中");

            clearImageAllCache(SettingActivity.this);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    cache_text.setText(getCacheSize(SettingActivity.this));
                    ToastTools.showShort(SettingActivity.this, "已清理");
                }
            }, 1);
        } else if (view == modify0) {
            Intent intent = new Intent(SettingActivity.this, ModifyContactActivity.class);
            if (null != userInfo) {
                intent.putExtra("phone", userInfo.phone);
                intent.putExtra("wx", userInfo.wx);
                intent.putExtra("qq", userInfo.qq);
                intent.putExtra("userName", userInfo.userName);
            }
            startActivityForResult(intent, 1);
        } else if (view == modify1) {
            Intent intent = new Intent(SettingActivity.this, ModifyCashWayActivity.class);
            if (null != userInfo) {
                intent.putExtra("phone", userInfo.phone);
            }
            startActivity(intent);
        } else if (view == loginOut) {
            //退出登录
            AccountUtil.logout();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    modify0.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * 清除图片所有缓存
     */
    public void clearImageAllCache(Context context) {
        clearImageDiskCache(context);
        clearImageMemoryCache(context);
        String ImageExternalCatchDir = context.getExternalCacheDir() + ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;
        deleteFolderFile(ImageExternalCatchDir, true);
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath       filePath
     * @param deleteThisPath deleteThisPath
     */
    private void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清除图片磁盘缓存
     */
    public void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public String getCacheSize(Context context) {
        try {
            return getFormatSize(getFolderSize(new File(context.getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    private static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    private long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
}
