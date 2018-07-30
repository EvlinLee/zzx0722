package com.library.ui.img.uk.co.senab.photoview.gallery;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.library.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;


/**
 * User: qii
 * Date: 14-4-30
 */
public class ContainerFragment extends Fragment {

    DisplayImageOptions options = null;


    public static ContainerFragment newInstance(String url,
                                                boolean animationIn, boolean firstOpenPage, boolean scaleBg) {
        ContainerFragment fragment = new ContainerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        //bundle.putParcelable("rect", rect);
        bundle.putBoolean("scaleBg", scaleBg);
        bundle.putBoolean("animationIn", animationIn);
        bundle.putBoolean("firstOpenPage", firstOpenPage);
        fragment.setArguments(bundle);
        return fragment;
    }

    TextView wait;

    TextView error;

    CircleProgressView progressView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_container_layout, container, false);
        progressView = (CircleProgressView) view.findViewById(R.id.loading);
        wait = (TextView) view.findViewById(R.id.wait);
        error = (TextView) view.findViewById(R.id.error);
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).considerExifParams(true).build();
        Bundle bundle = getArguments();
        String url = bundle.getString("url");
        boolean animateIn = bundle.getBoolean("animationIn");
        bundle.putBoolean("animationIn", false);
        File file = null;
        if (url.startsWith("http")) {
            file = ImageLoader.getInstance().getDiskCache().get(url);
        } else {
            file = new File(url);
        }
        String path = file.getAbsolutePath();
        Log.e("ContainerFragment", "file:" + file.exists());
        if (ImageUtility.isThisBitmapCanRead(path) && file.exists()
                /*&& TaskCache.isThisUrlTaskFinished(url)*/) {
            displayPicture(path, animateIn);
        } else {
            progressView.setVisibility(View.VISIBLE);
            wait.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().loadImage(url, options, new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    error.setVisibility(View.INVISIBLE);
                    wait.setVisibility(View.VISIBLE);

                }

                @Override
                public void onLoadingFailed(String imageUri, View view,
                                            FailReason failReason) {
                    wait.setVisibility(View.INVISIBLE);
                    error.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    error.setVisibility(View.INVISIBLE);
                    progressView.setVisibility(View.INVISIBLE);
                    wait.setVisibility(View.INVISIBLE);
                    File file = ImageLoader.getInstance().getDiskCache().get(imageUri);
                    Log.e("ContainerFragment", "imageUri:" + imageUri + ":file.exists:" + file.exists());
                    if (file.exists() && !isDetached() && isVisible()) {
                        displayPicture(file.getAbsolutePath(), false);
                    }

                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    // TODO Auto-generated method stub

                }
            });
        }

        return view;
    }


    private void displayPicture(String path, boolean animateIn) {
        if (!ImageUtility.isThisBitmapTooLargeToRead(path)) {
            Log.e("Container", "Container--GeneralPictureFragment");
            Bundle bundle = getArguments();
            boolean scaleBg = bundle.getBoolean("scaleBg", false);
            Fragment fragment = GeneralPictureFragment.newInstance(path, animateIn, scaleBg);
            getChildFragmentManager().beginTransaction().replace(R.id.child, fragment)
                    .commitAllowingStateLoss();

        } else {
            Log.e("Container", "Container--LargePictureFragment");
            LargePictureFragment fragment = LargePictureFragment.newInstance(path, animateIn);
            getChildFragmentManager().beginTransaction().replace(R.id.child, fragment)
                    .commitAllowingStateLoss();
        }

    }


    public void animationExit(ObjectAnimator backgroundAnimator) {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.child);
        if (fragment instanceof GeneralPictureFragment) {
            GeneralPictureFragment child = (GeneralPictureFragment) fragment;
            child.animationExit(backgroundAnimator);
        }
    }


    public boolean canAnimateCloseActivity() {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.child);
        if (fragment instanceof GeneralPictureFragment) {
            return true;
        } else {
            return false;
        }
    }
}
