package com.eryue.widget.AutoScrollViewPager;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eryue.R;
import com.eryue.R.layout;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseViewPagerAdapter<T> extends PagerAdapter implements OnPageChangeListener {
    private List<T> data = new ArrayList();
    private Context mContext;
    private AutoViewPager mView;
    private OnAutoViewPagerItemClickListener listener;

    public BaseViewPagerAdapter(List<T> t) {
        this.data = t;
    }

    public BaseViewPagerAdapter(Context context) {
        this.mContext = context;
    }

    public BaseViewPagerAdapter(Context context, OnAutoViewPagerItemClickListener listener) {
        this.mContext = context;
        this.listener = listener;
    }

    public BaseViewPagerAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.data = data;
    }

    public BaseViewPagerAdapter(Context context, List<T> data, OnAutoViewPagerItemClickListener listener) {
        this.mContext = context;
        this.data = data;
        this.listener = listener;
    }

    public void init(AutoViewPager viewPager, BaseViewPagerAdapter adapter) {
        this.mView = viewPager;
        this.mView.setAdapter(this);
        this.mView.addOnPageChangeListener(this);
        if (null != this.data && this.data.size() != 0) {
//            int position = 1073741823 - 1073741823 % this.getRealCount();
//            this.mView.setCurrentItem(position);
            this.mView.start();
            this.mView.updatePointView(this.getRealCount());
        }
    }

    public void setListener(OnAutoViewPagerItemClickListener listener) {
        this.listener = listener;
    }

    public void add(T t) {
        this.data.add(t);
        this.notifyDataSetChanged();
        this.mView.updatePointView(this.getRealCount());
    }

    public void setData(List<T> dataList) {
        if (null != dataList && !dataList.isEmpty()) {
            this.data = dataList;
            this.notifyDataSetChanged();
            this.mView.start();
            this.mView.updatePointView(this.getRealCount());
        }
    }

    public List<T> getData() {
        return data;
    }

    public int getCount() {
        if (null == this.data || this.data.isEmpty()) {
            return 0;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public int getRealCount() {
        return this.data == null ? 0 : this.data.size();
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView view = (ImageView) LayoutInflater.from(this.mContext).inflate(R.layout.imageview, container, false);
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (BaseViewPagerAdapter.this.listener != null) {
                    BaseViewPagerAdapter.this.listener.onItemClick(position % BaseViewPagerAdapter.this.getRealCount(), BaseViewPagerAdapter.this.data.get(position % BaseViewPagerAdapter.this.getRealCount()));
                }

            }
        });
        this.loadImage(view, position, this.data.get(position % this.getRealCount()));
        container.addView(view);
        return view;
    }

    public abstract void loadImage(ImageView var1, int var2, T var3);

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void onPageSelected(int position) {
        this.mView.onPageSelected(position % this.getRealCount());
    }

    public void onPageScrollStateChanged(int state) {
    }

    public interface OnAutoViewPagerItemClickListener<T> {
        void onItemClick(int var1, T var2);
    }
}

