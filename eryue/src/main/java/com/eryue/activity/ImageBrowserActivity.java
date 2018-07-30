package com.eryue.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.eryue.GoodsContants;
import com.eryue.R;
import com.library.util.ImageUtils;
import com.library.util.ToastTools;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


/**
 * 图片浏览器
 * Created by sgli.Android on 2016/9/6.
 */
public class ImageBrowserActivity extends FragmentActivity {
    private ImageBrowserAdapter mImageBrowserAdapter;
    private TextView tvCurIndex, tvTotalIndex;
    private View ivDownload;
    private ViewPager viewPager;

    private List<String> urls;
    private int curIndex;

    public static final String KEY_URL = "URL";
    public static final String KEY_INDEX = "INDEX";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);

        initData();
        initView();

        tvCurIndex.setText(curIndex + 1 + "");
        tvTotalIndex.setText(urls.size() + "");
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            urls = intent.getStringArrayListExtra(KEY_URL);
            curIndex = intent.getIntExtra(KEY_INDEX, 0);
        }
    }

    private void initView() {
        tvCurIndex = (TextView) findViewById(R.id.curIndex);
        tvTotalIndex = (TextView) findViewById(R.id.totalIndex);
        ivDownload = findViewById(R.id.download);
        ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageLoader.getInstance().loadImage(urls.get(curIndex), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        Toast.makeText(ImageBrowserActivity.this, "图片下载失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        try {
                            //压缩图片
                            ImageUtils.getInstance(ImageBrowserActivity.this).setScaleSize(2048);
                            byte[] compressByte = ImageUtils.getInstance(ImageBrowserActivity.this).compressedimgToBase64(loadedImage);
                            File file_path = StorageUtils.getOwnCacheDirectory(getApplicationContext(), GoodsContants.PATH_DOWNIMG);
                            File file = new File(file_path, System.currentTimeMillis() + ".png");
                            FileOutputStream fOut = new FileOutputStream(file);

                            fOut.write(compressByte);
                            fOut.flush();
                            fOut.close();
                            ToastTools.showShort(ImageBrowserActivity.this,"图片已经下载到/sdcard"+GoodsContants.PATH_DOWNIMG+"/");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mImageBrowserAdapter = new ImageBrowserAdapter(getSupportFragmentManager());
        mImageBrowserAdapter.setUrls(urls);
        viewPager.setAdapter(mImageBrowserAdapter);
        //设置进入界面时显示的是第几张图片，setCurrentItem方法必须在setAdapter调用之后才有效
        viewPager.setCurrentItem(curIndex);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curIndex = position;
                tvCurIndex.setText((position + 1) + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewPager.clearOnPageChangeListeners();
    }
}
