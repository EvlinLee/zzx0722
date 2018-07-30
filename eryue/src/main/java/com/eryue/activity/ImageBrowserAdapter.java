package com.eryue.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sgli.Android on 2016/9/6.
 */
public class ImageBrowserAdapter extends FragmentPagerAdapter {

    private List<String> urls = new ArrayList<>();

    @Override
    public int getCount() {
        return urls == null ? 0 : urls.size();
    }

    public ImageBrowserAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return ImageViewFragment.getInstance(urls.get(position));
    }

    public void setUrls(List<String> urls) {
        this.urls.clear();
        if (urls != null)
            this.urls.addAll(urls);
        notifyDataSetChanged();
    }

}
