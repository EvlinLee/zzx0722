package com.eryue.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.eryue.R;
import com.library.ui.img.uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by sgli.Android on 2016/9/6.
 */
public class ImageViewFragment extends Fragment {

    private static final String KEY_URL = "URL";

    private String url;
    private PhotoViewAttacher photoViewAttacher;
//    private DisplayImageOptions displayOption;
    private ProgressBar progressBar;
    private ImageView imageView;

    public ImageViewFragment() {
    }

    public static ImageViewFragment getInstance(@NonNull String url) {
        ImageViewFragment imageViewFragment = new ImageViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_URL, url);
        imageViewFragment.setArguments(bundle);
        return imageViewFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getArguments().getString(KEY_URL);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

//        displayOption = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.img_default_contract)
//                .showImageForEmptyUri(R.drawable.img_default_contract)
//                .showImageOnFail(R.drawable.img_default_contract)
//                .postProcessor(new BitmapProcessor() {
//                    @Override
//                    public Bitmap process(Bitmap bitmap) {
//                        try {
//                            if (!bitmap.hasAlpha())
//                                return bitmap;
//                            Bitmap myBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//                            Canvas canvas = new Canvas(myBitmap);
//                            canvas.drawColor(Color.WHITE);
//                            canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.ANTI_ALIAS_FLAG));
//                            bitmap.recycle();
//                            return myBitmap;
//                        } catch (Exception e) {
//                            return bitmap;
//                        }
//                    }
//                })
//                .decodingOptions(options)
//                .cacheInMemory(false)   //设置图片不缓存于内存中
//                .cacheOnDisk(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)    //设置图片的质量
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)    //设置图片的缩放类型，该方法可以有效减少内存的占用
//                .considerExifParams(false).build();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        imageView = new ImageView(getContext());
//        photoViewAttacher = new PhotoViewAttacher(imageView);
//        photoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
//            @Override
//            public void onPhotoTap(View view, float x, float y) {
//                getActivity().finish();
//            }
//        });

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        imageView.setLayoutParams(layoutParams);
        frameLayout.addView(imageView, layoutParams);

//        progressBar = new ProgressBar(getContext());
//        progressBar.setIndeterminate(true);
//        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
//        frameLayout.addView(progressBar, layoutParams2);

        return frameLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(getContext()).load(url).placeholder(R.drawable.img_default_contract).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

//        ImageLoader.getInstance().displayImage(url, imageView, displayOption, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//                progressBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                ToastTools.showShort(getContext(),"图片加载失败");
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                progressBar.setVisibility(View.GONE);
//                photoViewAttacher.update();
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//                progressBar.setVisibility(View.GONE);
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imageView != null)
            imageView.setImageDrawable(null);
        if (photoViewAttacher != null) {
            photoViewAttacher.setOnPhotoTapListener(null);
            if (photoViewAttacher.getScaleDragDetector() != null)
                photoViewAttacher.getScaleDragDetector().setOnGestureListener(null);
        }
//        displayOption = null;
    }
}
