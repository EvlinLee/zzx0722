package com.eryue.mine;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.eryue.AccountUtil;
import com.eryue.GoodsContants;
import com.eryue.R;
import com.library.util.ImageUtils;
import com.library.util.ToastTools;

import net.InterfaceManager;
import net.MineInterface;

import java.io.File;
import java.io.FileOutputStream;

import base.BaseActivity;
import permission.PermissionUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * 联系客服
 */

public class ContactServerActivity extends BaseActivity {
    private ImageView imageView;

    private String baseIP = AccountUtil.getBaseIp();

    private TextView wechat;

    private MineInterface.WechatServerInfo wechatServerInfo;

    private boolean canCopy = false;
    private boolean canSave = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_server);

        navigationBar.setTitle("联系客服");
        imageView = (ImageView)findViewById(R.id.image);
        wechat = findViewById(R.id.wechat);


//        String imageUrl = baseIP + "cms/weixin02.jpg";
//        Glide.with(this).load(imageUrl).error(R.drawable.img_default_contract).into(imageView);

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });
        findViewById(R.id.copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (false == canCopy || wechatServerInfo == null) {
                    Toast.makeText(ContactServerActivity.this,"正在请求中，请稍后重试",Toast.LENGTH_SHORT).show();
                    return;
                }

                // 复制邀请码
                ClipboardManager myClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData myClip;
                myClip = ClipData.newPlainText("text", wechatServerInfo.wxName);//text是内容
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(ContactServerActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
            }
        });

        reqInfo();
    }

    private void reqInfo() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.ServerInfoReq callFunc = retrofit.create(MineInterface.ServerInfoReq.class);
        Call<MineInterface.ServerInfoRsp> call = callFunc.get(AccountUtil.getUID());
        call.enqueue(new Callback<MineInterface.ServerInfoRsp>() {
            @Override
            public void onResponse(Call<MineInterface.ServerInfoRsp> call, Response<MineInterface.ServerInfoRsp> response) {
                if (response.body() == null || response.body().status != 1) {
                    ToastTools.showShort(ContactServerActivity.this, "暂无数据");
                    return;
                }

                wechatServerInfo = response.body().result;
                refreshView();
            }

            @Override
            public void onFailure(Call<MineInterface.ServerInfoRsp> call, Throwable t) {
                t.printStackTrace();
                ToastTools.showShort(getBaseContext(), "请求失败");
            }
        });
    }

    private void refreshView() {
        if (wechatServerInfo == null) {
            return;
        }

        canCopy = true;
         wechat.setText(wechatServerInfo.wxName);
//         Glide.with(this).load(wechatServerInfo.url).error(R.drawable.img_default_contract).into(imageView);

        Glide.with(this).load(wechatServerInfo.url).error(R.drawable.img_default_contract).into(new GlideDrawableImageViewTarget(imageView) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                canSave = true;
            }
        });
    }

    private void saveImage() {
        final Context context = this;

        if (false == canSave) {
            Toast.makeText(context,"图片加载中，请稍后重试",Toast.LENGTH_SHORT).show();
            return;
        }

        imageView.setDrawingCacheEnabled(true);
        final Bitmap mBitmap = imageView.getDrawingCache();
        if (mBitmap == null) {
            return;
        }
        // 保存图片
        PermissionUtil.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtil.PermissionListener() {
            @Override
            public void onPermissionGot() {
                MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "猪猪虾客服二维码", "二维码");
                savePicture(mBitmap);
            }
            @Override
            public void onPermissionDenied(String permission) {
                Toast.makeText(context,"保存失败",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPermissionDeniedAndNeverAskAgain() {
                Toast.makeText(context,"保存失败",Toast.LENGTH_SHORT).show();
            }
        }, "请允许保存图片");
    }

    private void savePicture(Bitmap image) {
        try {
            //压缩图片
            ImageUtils.getInstance(this).setScaleSize(900);
            byte[] compressByte = ImageUtils.getInstance(this).compressedimgToBase64(image);
            File file_path = StorageUtils.getOwnCacheDirectory(getApplicationContext(), GoodsContants.PATH_DOWNIMG);
            File file = new File(file_path, "erWeiMa.png");
            FileOutputStream fOut = new FileOutputStream(file);

            fOut.write(compressByte);
            fOut.flush();
            fOut.close();

            Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
            scanPhotos(file, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scanPhotos(File file, Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
