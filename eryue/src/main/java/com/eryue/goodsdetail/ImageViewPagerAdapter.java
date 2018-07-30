package com.eryue.goodsdetail;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.eryue.R;
import com.eryue.widget.AutoScrollViewPager.BaseViewPagerAdapter;
import com.library.util.UIScreen;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

/**
 * Created by bli.Jason on 2018/2/12.
 */

public class ImageViewPagerAdapter<T> extends BaseViewPagerAdapter {

    DisplayImageOptions optionsSmall = new DisplayImageOptions.Builder()
            .cacheInMemory(false)   //设置图片不缓存于内存中
            .bitmapConfig(Bitmap.Config.RGB_565)    //设置图片的质量
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)    //设置图片的缩放类型，该方法可以有效减少内存的占用
            .showImageOnLoading(R.drawable.img_default_contract)
            .showImageOnFail(R.drawable.img_default_contract)
            .considerExifParams(true).build();

    private Context context;
    public ImageViewPagerAdapter(Context context) {
        super(context);
        this.context = context;
    }

    public ImageViewPagerAdapter(Context context, List<String> data) {
        super(context,data);
        this.context = context;
    }

    private int maxHeight;

    @Override
    public void loadImage(final ImageView imgView, int var2, Object object) {
        if (null!=object&&(object instanceof String)&&null!=imgView){
            if (null==context||((context instanceof Activity)&&((Activity)context).isFinishing())){
                //处理activity已经destory的情况，直接return
                return;
            }
//            ImageLoader.getInstance().displayImage(((String) object),var1,optionsSmall);
//            Glide.with(context).load((String) object).placeholder(R.drawable.img_default_contract).into(var1);
            Glide.with(context).load((String) object).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    ViewGroup.LayoutParams lp = imgView.getLayoutParams();
                    lp.width = UIScreen.screenWidth;
                    lp.height = lp.width*2/5;
                    float tempHeight=height * ((float)lp.width / width);

                    if (maxHeight<tempHeight){
                        maxHeight =(int)tempHeight ;
                    }

                    if(maxHeight>UIScreen.screenWidth){
                        maxHeight = UIScreen.screenWidth;
                    }

                    if (lp.height < maxHeight){

                    lp.height =maxHeight ;
                    imgView.setLayoutParams(lp);
                    imgView.setImageBitmap(bitmap);

                    if (null!=imgView.getParent()&&(imgView.getParent() instanceof ViewGroup)){
                        ViewGroup viewGroup = (ViewGroup) imgView.getParent();
                        ViewGroup.LayoutParams groupLP = viewGroup.getLayoutParams();
                        groupLP.height = maxHeight;
                        viewGroup.setLayoutParams(groupLP);
                    }
                  }
                }
            });
//            DrawableTypeRequest drawableTypeRequest = Glide.with(context).load((String) object);
//            drawableTypeRequest.asBitmap().into(new SimpleTarget<Bitmap>() {
//
//                public void onLoadStarted(Drawable placeholder) {
//
//                    ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
//                        @Override
//                        public void handleMessage(Message msg) {
////                            imgView.setImageResource(R.drawable.img_default_contract);
//
//                            if (null!=imgView){
//                                ViewGroup.LayoutParams lp = imgView.getLayoutParams();
//                                if (lp.height<StringUtils.dipToPx(200)){
//                                    lp.height = StringUtils.dipToPx(200);
//                                    imgView.setLayoutParams(lp);
//                                }
//
//                                if (null!=imgView.getParent()&&(imgView.getParent() instanceof ViewGroup)){
//                                    ViewGroup viewGroup = (ViewGroup) imgView.getParent();
//                                    ViewGroup.LayoutParams groupLP = viewGroup.getLayoutParams();
//                                    if (groupLP.height<StringUtils.dipToPx(200)){
//                                        groupLP.height = StringUtils.dipToPx(200);
//                                        viewGroup.setLayoutParams(groupLP);
//                                    }
//                                }
//                            }
//                        }
//                    }).sendEmptyMessage(0);
//
//                }
//                @Override
//                public void onResourceReady(final Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
//
//                    ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
//                        @Override
//                        public void handleMessage(Message msg) {
//                            int width = bitmap.getWidth();
//                            int height = bitmap.getHeight();
//                            ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams)imgView.getLayoutParams();
//                            lp.width = UIScreen.screenWidth;
//                            float tempHeight=height * ((float)lp.width / width);
//                            maxHeight = (int) tempHeight;
//                            if (lp.height < maxHeight){
//
//                                lp.height =maxHeight ;
//                                imgView.setLayoutParams(lp);
//                                imgView.setImageBitmap(bitmap);
//
//                                if (null!=imgView.getParent()&&(imgView.getParent() instanceof ViewGroup)){
//                                    ViewGroup viewGroup = (ViewGroup) imgView.getParent();
//                                    ViewGroup.LayoutParams groupLP = viewGroup.getLayoutParams();
//                                    groupLP.height = maxHeight;
//                                    viewGroup.setLayoutParams(groupLP);
//                                }
//                            }
//
//                        }
//                    }).sendEmptyMessage(0);
//
//                }
//            });
        }


    }



}
