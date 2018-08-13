package com.eryue.mine;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eryue.AccountUtil;
import com.eryue.GoodsContants;
import com.eryue.R;
import com.eryue.WXShare;
import com.eryue.home.SharePopView;
import com.eryue.widget.ShareContentView;
import com.library.util.StringUtils;
import com.library.util.ToastTools;
import com.library.util.UIScreen;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import qrcode.QRCodeUtil;


public class ShareAppActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager pager;
    private LinearLayout indicatorView;
    private int count;

    private String erweimaUrl;

    private List<Bitmap> bitmapList;
    private int curIndex;

    private TextView yqmTV;

    private TextView share_weixin;
    private String inviteCode = AccountUtil.getInviteCode();
    private String serverURL = AccountUtil.getServerURL();


    //分享
    SharePopView sharePopView;
    private WXShare wxShare;

    //2.1改版，用户不输入邀请码可以进入程序，所以如果用户没有邀请码就不显示
    //我的邀请码
    private RelativeLayout layout_mycode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_app);

        navigationBar.setTitle("推广邀请");
        navigationBar.rightTV.setVisibility(View.INVISIBLE);

        indicatorView = (LinearLayout) findViewById(R.id.indicator_view);

        layout_mycode = findViewById(R.id.layout_mycode);
        yqmTV = (TextView)findViewById(R.id.yaoqingma);
        yqmTV.setText(inviteCode);

        share_weixin = findViewById(R.id.share_weixin);
        share_weixin.setOnClickListener(this);

        final Context context = this;
        findViewById(R.id.copy_yqm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 复制邀请码
                ClipboardManager myClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData myClip;
                myClip = ClipData.newPlainText("text", inviteCode);//text是内容
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
            }
        });

        //用户没有输入邀请码就不显示
        if (TextUtils.isEmpty(inviteCode)){
            layout_mycode.setVisibility(View.GONE);
            // http://域名//appDownAndriod.do?code=邀请码(安卓)
            erweimaUrl = serverURL + "appDownAndriod.do";
        }else{
            // http://域名//appDownAndriod.do?code=邀请码(安卓)
            erweimaUrl = serverURL + "appDownAndriod.do?code=" + inviteCode;
        }


        curIndex = 1;

        pager = (ViewPager) findViewById(R.id.view_pager);
        List<View> pages = getPages();
        if (pages == null) {
            return;
        }
        PagerAdapter adapter = new ViewAdapter(pages);
        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new SlidePageTransformer());
        pager.setCurrentItem(1);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("", "");
            }

            @Override
            public void onPageSelected(int position) {
                showIndictorView(position);
                curIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        showIndictorView(1);

    }

    private void shareImage(int tag) {
        Bitmap bitmap = bitmapList.get(curIndex);
        if (bitmap == null) {
            ToastTools.showShort(ShareAppActivity.this, "图片有异常，稍后重试");
            return;
        }
        wxShare.shareImage(bitmap, tag);
    }


    @SuppressLint("ResourceAsColor")
    private void showIndictorView(int index) {
        ArrayList<Integer> itemArr = new ArrayList<>();
        itemArr.add(R.id.item_0);
        itemArr.add(R.id.item_1);
        itemArr.add(R.id.item_2);
        for (int i = 0; i < itemArr.size(); i++) {
            View view = findViewById(itemArr.get(i));
            if (index == i) {
                view.setBackgroundColor(R.color.red_paper);
            } else {
                view.setBackgroundColor(Color.parseColor("#cccccc"));
            }
        }
    }



    private List<View> getPages() {
        List<View> pages = new ArrayList<>();

        bitmapList = new ArrayList<>();
        Bitmap erweimaBitmap = QRCodeUtil.createQRCodeBitmap(erweimaUrl, 50, 50);
        if (erweimaBitmap == null) {
            ToastTools.showShort(this, "二维码生成错误，请稍后重试");
            return null;
        }



        ImageView imageView0 = new ImageView(this);
        createMap0(imageView0, erweimaBitmap, R.drawable.shareapp0);

        ImageView imageView1 = new ImageView(this);
        createMap1(imageView1, erweimaBitmap, R.drawable.shareapp1);

        ImageView imageView2 = new ImageView(this);
        createMap2(imageView2, erweimaBitmap, R.drawable.shareapp2);


        pages.add(imageView0);
        pages.add(imageView1);
        pages.add(imageView2);

        return pages;
    }


    private void createMap0(ImageView imageView, Bitmap erweimaBitmap, int id) {
        Bitmap photo = BitmapFactory.decodeResource(getResources(), id);

        int width = StringUtils.dipToPx(375);
        int height = StringUtils.dipToPx(667);

        int width1 = StringUtils.dipToPx(120);
        int height1 = width1;

        //建立一个空的Bitmap
        Bitmap icon = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 初始化画布绘制的图像到icon上
        Canvas canvas = new Canvas(icon);
        // 建立画笔
        Paint photoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 获取更清晰的图像采样，防抖动
        photoPaint.setDither(true);
        // 过滤一下，抗剧齿
        photoPaint.setFilterBitmap(true);

        Rect dst = new Rect(0, 0, width, height);// 创建一个指定的新矩形的坐标
        canvas.drawBitmap(photo, null, dst, photoPaint);

        int left = (width - width1) / 2 + StringUtils.dipToPx(2);
        int top = StringUtils.dipToPx(216);
        Rect dst1 = new Rect(
                left,
                top,
                left + width1,
                top + height1);
        canvas.drawBitmap(erweimaBitmap, null, dst1, photoPaint);

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        drawInviteCode(canvas, Color.WHITE, 14, 181, 382);


        imageView.setImageBitmap(icon);
        bitmapList.add(icon);
    }


    private void createMap1(ImageView imageView, Bitmap erweimaBitmap, int id) {
        Bitmap photo = BitmapFactory.decodeResource(getResources(), id);

        int width = StringUtils.dipToPx(375);
        int height = StringUtils.dipToPx(667);

        int width1 = StringUtils.dipToPx(120);
        int height1 = width1;

        //建立一个空的Bitmap
        Bitmap icon = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 初始化画布绘制的图像到icon上
        Canvas canvas = new Canvas(icon);
        // 建立画笔
        Paint photoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 获取更清晰的图像采样，防抖动
        photoPaint.setDither(true);
        // 过滤一下，抗剧齿
        photoPaint.setFilterBitmap(true);

        Rect dst = new Rect(0, 0, width, height);// 创建一个指定的新矩形的坐标
        canvas.drawBitmap(photo, null, dst, photoPaint);

        int left = (width - width1) / 2;
        int top = StringUtils.dipToPx(490);
        Rect dst1 = new Rect(
                left,
                top,
                left + width1,
                top + height1);
        canvas.drawBitmap(erweimaBitmap, null, dst1, photoPaint);

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        drawInviteCode(canvas, Color.WHITE, 14, 181, 651);


        imageView.setImageBitmap(icon);
        bitmapList.add(icon);
    }

    private void createMap2(ImageView imageView, Bitmap erweimaBitmap, int id) {
        Bitmap photo = BitmapFactory.decodeResource(getResources(), id);

        int width = StringUtils.dipToPx(375);
        int height = StringUtils.dipToPx(667);

        int width1 = StringUtils.dipToPx(120);
        int height1 = width1;

        //建立一个空的Bitmap
        Bitmap icon = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 初始化画布绘制的图像到icon上
        Canvas canvas = new Canvas(icon);
        // 建立画笔
        Paint photoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 获取更清晰的图像采样，防抖动
        photoPaint.setDither(true);
        // 过滤一下，抗剧齿
        photoPaint.setFilterBitmap(true);

        Rect dst = new Rect(0, 0, width, height);// 创建一个指定的新矩形的坐标
        canvas.drawBitmap(photo, null, dst, photoPaint);

        int left = (width - width1) / 2;
        int top = StringUtils.dipToPx(210);
        Rect dst1 = new Rect(
                left,
                top,
                left + width1,
                top + height1);
        canvas.drawBitmap(erweimaBitmap, null, dst1, photoPaint);

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        drawInviteCode(canvas, Color.WHITE, 14, 181, 373);


        imageView.setImageBitmap(icon);
        bitmapList.add(icon);
    }

    private void createMap3(ImageView imageView, Bitmap erweimaBitmap, int id) {
        Bitmap photo = BitmapFactory.decodeResource(getResources(), id);


        int width = StringUtils.dipToPx(375);
        int height = StringUtils.dipToPx(667);

        int width1 = StringUtils.dipToPx(105);
        int height1 = width1;

        //建立一个空的Bitmap
        Bitmap icon = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 初始化画布绘制的图像到icon上
        Canvas canvas = new Canvas(icon);
        // 建立画笔
        Paint photoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 获取更清晰的图像采样，防抖动
        photoPaint.setDither(true);
        // 过滤一下，抗剧齿
        photoPaint.setFilterBitmap(true);

        Rect dst = new Rect(0, 0, width, height);// 创建一个指定的新矩形的坐标
        canvas.drawBitmap(photo, null, dst, photoPaint);

        int left = (width - width1) / 2 + StringUtils.dipToPx(2);
        int top = StringUtils.dipToPx(230);
        Rect dst1 = new Rect(
                left,
                top,
                left + width1,
                top + height1);
        canvas.drawBitmap(erweimaBitmap, null, dst1, photoPaint);

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        drawInviteCode(canvas, Color.WHITE, 14, 181, 375);


        imageView.setImageBitmap(icon);
        bitmapList.add(icon);
    }


    Paint textPaint;
    private void drawInviteCode(Canvas canvas, int color, int size, float x, float y) {
        if (! AccountUtil.isLogin()) {
            return;
        }

        if (textPaint == null) {
            textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        textPaint.setTextSize(StringUtils.dipToPx(size));
        textPaint.setColor(color);
        canvas.drawText(inviteCode, StringUtils.dipToPx(x), StringUtils.dipToPx(y), textPaint);
    }

    @Override
    public void onClick(View view) {
        if (view == share_weixin) {


            if (null == sharePopView){
                sharePopView = new SharePopView(ShareAppActivity.this);
                sharePopView.setOnShareClickListener(new ShareContentView.OnShareClickListener() {
                    @Override
                    public void onShareClick(int tag) {
                        shareImage(tag);
                    }
                });
                wxShare = new WXShare(ShareAppActivity.this);

            }

            sharePopView.showPopView();
        }
     }


    class ViewAdapter extends PagerAdapter {
        private List<View> datas;

        public ViewAdapter(List<View> list) {
            datas = list;
        }

        @Override
        public int getCount() {
            if (datas == null) {
                return 0;
            }
            return datas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = datas.get(position);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.setLayoutParams(params);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(datas.get(position));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != bitmapList) {
            for (int i = 0; i < bitmapList.size(); i++) {
                if (null != bitmapList.get(i) && !bitmapList.get(i).isRecycled()) {
                    bitmapList.get(i).recycle();
                    bitmapList.set(i, null);
                }
            }
        }
    }

}
