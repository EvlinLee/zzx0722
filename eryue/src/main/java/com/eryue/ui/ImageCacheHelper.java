package com.eryue.ui;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.eryue.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * @author hyu.Yale
 */
public class ImageCacheHelper {

    private static ImageCacheHelper imageCacheHelper = new ImageCacheHelper();

    public static ImageCacheHelper getInstance() {
        return imageCacheHelper;
    }

    private ImageCacheHelper() {
    }

    public void loadImage(final ImageView imageView, String imageUrl,
                          DisplayImageOptions options, ImageLoadingListener loadingListener) {
        ImageLoader.getInstance().displayImage(imageUrl, imageView, options,
                loadingListener);
    }

    public void loadImage(final ImageView imageView, String imageUrl,
                          DisplayImageOptions options) {

        if (null == options) {
            options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    // .showImageOnFail(R.drawable.pic_default_small)
                    .considerExifParams(true).build();
        }

        ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
    }

    public void loadProfileImage(final ImageView imageView, String imageUrl) {
        loadProfileImage(imageView, imageUrl, null, null);
    }

    public void loadProfileImage(final ImageView imageView, String imageUrl,
                                 DisplayImageOptions options, ImageLoadingListener listener) {

        if (null == options) {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.img_default_contract)
                    .showImageForEmptyUri(R.drawable.img_default_contract)
                    .showImageOnFail(R.drawable.img_default_contract)
                    .cacheInMemory(true).cacheOnDisk(true)
                    .considerExifParams(true).build();

        }

        ImageLoader.getInstance().displayImage(imageUrl, imageView, options,
                listener);
    }

    public void loadBitmaps(final ImageView imageView, String imageUrl) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .build();

        ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
    }

    public void loadBitmaps(final ImageView imageView, String imageUrl,
                            DisplayImageOptions options) {
        ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
    }

    private DisplayImageOptions optionsSmall = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .showImageOnLoading(R.drawable.img_default_contract)
            .showImageOnFail(R.drawable.img_default_contract)
            .considerExifParams(true).build();


    public DisplayImageOptions getOptionsSmall() {
        return optionsSmall;
    }

    //设置默认图
    public void setOptionsSmall(DisplayImageOptions optionsSmall) {
        this.optionsSmall = optionsSmall;
    }

    public void loadSmallImage(final ImageView imageView, String imageUrl) {
        ImageViewAware iva = new ImageViewAware(imageView, true);
        ImageLoader.getInstance().displayImage(imageUrl, iva, optionsSmall);
    }

    private DisplayImageOptions optionsBig = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .showImageOnLoading(R.drawable.img_default_contract)
            .showImageOnFail(R.drawable.img_default_contract)
            .considerExifParams(true).build();

    public void loadBigImage(final ImageView imageView, String imageUrl) {
        ImageCacheHelper.getInstance().loadImage(imageView, imageUrl,
                optionsBig);
    }

    public static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        public static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

}
