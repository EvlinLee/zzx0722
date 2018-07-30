package com.library.ui.img.uk.co.senab.photoview.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.library.R;
import com.library.ui.UINavigationBar;
import com.library.util.ImageUtils;
import com.library.util.UIScreen;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * User: qii
 * Date: 14-3-21
 */
public class GalleryAnimationActivity extends FragmentActivity implements Callback {

    //private static final int STATUS_BAR_HEIGHT_DP_UNIT = 25;

    private static final int ADV_MSG = 110;

    private static final int ADV_TIME = 3000;

    private ArrayList<String> urls = new ArrayList<String>();

    private ViewPager pager;

    private TextView position;

    private int initPosition;

    private Handler mHandler;

    private RelativeLayout titleLayout;

    private String adv = null;

   /* private View background;

    private ColorDrawable backgroundColor;*/

    public static Intent newIntent(Context context, String[] path,
                                   int initPosition) {
        Intent intent = new Intent(context, GalleryAnimationActivity.class);
        intent.putExtra("msg", path);
        intent.putExtra("position", initPosition);
        return intent;
    }

    public static Intent newIntent(Context context, String[] path, String adv) {
        Intent intent = new Intent(context, GalleryAnimationActivity.class);
        intent.putExtra("msg", path);
        intent.putExtra("adv", adv);
        return intent;
    }

    Button backButton;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galleryactivity_animation_layout);
        boolean disableHardwareLayerType = false;
        String path[] = getIntent().getStringArrayExtra("msg");
        adv = getIntent().getStringExtra("adv");
        boolean scaleBg = false;
        titleLayout = (RelativeLayout) findViewById(R.id.pre_view_navigationBar);
        titleLayout.getLayoutParams().height = UINavigationBar.DEF_BAR_HEIGHT;
        backButton = (Button) findViewById(R.id.pre_view_navigationBar_leftView);
        saveButton = (Button) findViewById(R.id.pre_view_navigationBar_rightView);

        // 调整页面 样式
        titleLayout.setBackgroundResource(UINavigationBar.DEF_BAR_BG_RES_ID);
        TextView titleV = (TextView) findViewById(R.id.pre_view_navigationBar_titleView);
        float scraleSize = 2.3f * UIScreen.density;
        titleV.setTextSize(UINavigationBar.DEF_BAR_HEIGHT / scraleSize);

        saveButton.setBackgroundColor(Color.TRANSPARENT);

        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                saveBitamp();
            }
        });
        if (adv != null) {
            scaleBg = true;
            titleLayout.setVisibility(View.GONE);
            closeAdv();
        } else {
            titleLayout.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < path.length; i++) {
            urls.add(path[i]);
        }
        position = (TextView) findViewById(R.id.position);
        initPosition = getIntent().getIntExtra("position", 0);

        pager = (ViewPager) findViewById(R.id.pager);
        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(getSupportFragmentManager());
        imagePagerAdapter.setScaleBg(scaleBg);
        pager.setAdapter(imagePagerAdapter);
        final boolean finalDisableHardwareLayerType = disableHardwareLayerType;
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                GalleryAnimationActivity.this.position.setText(String.valueOf(position + 1));
            }

            @SuppressLint("NewApi")
            @Override
            public void onPageScrollStateChanged(int scrollState) {
                if (scrollState != ViewPager.SCROLL_STATE_IDLE && finalDisableHardwareLayerType) {
                    final int childCount = pager.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = pager.getChildAt(i);
                        if (VERSION.SDK_INT >= 11) {
                            if (child.getLayerType() != View.LAYER_TYPE_NONE) {
                                child.setLayerType(View.LAYER_TYPE_NONE, null);
                            }
                        }
                    }
                }
            }
        });
        pager.setCurrentItem(getIntent().getIntExtra("position", 0));
        pager.setOffscreenPageLimit(1);
        pager.setPageTransformer(true, new ZoomOutPageTransformer());

        TextView sum = (TextView) findViewById(R.id.sum);
        sum.setText(String.valueOf(urls.size()));

        //background = AnimationUtility.getAppContentView(this);
    }

    /**
     * 3秒关闭广告
     */
    private void closeAdv() {
        mHandler = new Handler(this);
        new Thread(new Runnable() {

            @Override
            public void run() {
                mHandler.sendEmptyMessageDelayed(ADV_MSG, ADV_TIME);
            }
        }).start();
    }

    private HashMap<Integer, ContainerFragment> fragmentMap
            = new HashMap<Integer, ContainerFragment>();

    private boolean alreadyAnimateIn = false;

    private class ImagePagerAdapter extends FragmentPagerAdapter {

        private boolean mScaleBg = false;

        public ImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            ContainerFragment fragment = fragmentMap.get(position);
            if (fragment == null) {

                boolean animateIn = (initPosition == position) && !alreadyAnimateIn;
                fragment = ContainerFragment
                        .newInstance(urls.get(position), animateIn,
                                initPosition == position, mScaleBg);
                alreadyAnimateIn = true;
                fragmentMap.put(position, fragment);
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return urls.size();
        }

        public void setScaleBg(boolean scaleBg) {
            this.mScaleBg = scaleBg;
        }
    }

    @Override
    public void onBackPressed() {
        if (adv != null) {
            super.onBackPressed();
        } else {
            setTitleVisible();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 控制titleBar显示
     */
    private void setTitleVisible() {
        if (titleLayout != null) {
            if (titleLayout.isShown()) {
                titleLayout.setVisibility(View.GONE);
            } else {
                titleLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case ADV_MSG:
                finish();
                break;
            case IMAGE_SAVE_SUCC:
                String path = (String) msg.obj;
                Toast.makeText(this, "图片已保存到 " + path, Toast.LENGTH_SHORT).show();
                break;
            case IMAGE_SAVE_FAIL:
                Toast.makeText(this, "保存失败！", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 保存图片
     */
    public void saveBitamp() {
        String url = null;
        if (urls != null && urls.size() > 0) {
            url = urls.get(0);
        }

        if (mHandler == null) {
            mHandler = new Handler(this);
        }

        File file = ImageLoader.getInstance().getDiskCache().get(url);
        if (file != null && file.exists()) {
            savePicTOSDcard(file);
        } else {
            final String imagUrl = url;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    File file = ImageLoader.getInstance().getDiskCache().get(imagUrl);
                    if (file != null) {
                        savePicTOSDcard(file);
                    }
                }
            }).start();


            //Toast.makeText(this,"保存失败！",Toast.LENGTH_SHORT).show();
        }
    }

    private static final int IMAGE_SAVE_SUCC = 0x11;
    private static final int IMAGE_SAVE_FAIL = 0x12;


    private void savePicTOSDcard(File file) {
        String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/DCIM/Wind/";
        String filepath = savePath + "windexport_" + file.getName() + ".jpg";
        File copyfile = new File(filepath);
        if (copyfile.exists()) {
            Message msg = Message.obtain();
            msg.what = IMAGE_SAVE_SUCC;
            msg.obj = filepath;
            mHandler.sendMessage(msg);
        } else {
            try {
                Bitmap bitmap = ImageUtils.getBitmapByFile(file);
                ImageUtils.saveImageToSD(getApplicationContext(), filepath, bitmap, 100);
                Message msg = Message.obtain();
                msg.what = IMAGE_SAVE_SUCC;
                msg.obj = filepath;
                mHandler.sendMessage(msg);
            } catch (IOException e) {
                mHandler.sendEmptyMessage(IMAGE_SAVE_FAIL);
            }
        }
    }


}
