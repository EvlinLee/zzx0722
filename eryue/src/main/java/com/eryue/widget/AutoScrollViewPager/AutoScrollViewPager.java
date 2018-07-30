package com.eryue.widget.AutoScrollViewPager;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.eryue.R;
import com.eryue.ui.ViewPagerScroller;
import com.library.util.StringUtils;

/**
 * @author
 *
 */

public class AutoScrollViewPager extends RelativeLayout {
    private AutoViewPager mViewPager;
    private Context mContext;
    private LinearLayout layout;

    public AutoScrollViewPager(Context context) {
        super(context);
        this.init(context);
    }

    public AutoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mViewPager = new AutoViewPager(context);
        this.layout = new LinearLayout(this.mContext);
        this.addView(this.mViewPager);

        ViewPagerScroller mPagerScroller=new ViewPagerScroller(getContext());
        mPagerScroller.initViewPagerScroll(this.mViewPager);
    }

    public void setAdapter(BaseViewPagerAdapter adapter) {
        if(this.mViewPager != null) {
            this.mViewPager.init(this.mViewPager, adapter);
        }

    }

    public AutoViewPager getViewPager() {
        return this.mViewPager;
    }

    public void initPointView(int size) {
        this.layout = new LinearLayout(this.mContext);

        for(int i = 0; i < size; ++i) {
            ImageView imageView = new ImageView(this.mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(12, 12);
            params.setMargins(10,0,0,0);
//            params.leftMargin = 20;
//            params.gravity = 17;

            imageView.setLayoutParams(params);
            if(i == 0) {
                imageView.setBackgroundResource(R.drawable.point_checked);
            } else {
                imageView.setBackgroundResource(R.drawable.point_normal);
            }
            this.layout.addView(imageView);
        }

        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        layoutParams.setMargins(12, 20, 12, 20);
        this.layout.setPadding(0,0,0, StringUtils.dipToPx(6));
        this.layout.setLayoutParams(layoutParams);
        this.addView(this.layout);
    }

    public void updatePointView(int position) {
        int size = this.layout.getChildCount();

        for(int i = 0; i < size; ++i) {
            ImageView imageView = (ImageView)this.layout.getChildAt(i);
            if(i == position) {
                imageView.setBackgroundResource(R.drawable.point_checked);
            } else {
                imageView.setBackgroundResource(R.drawable.point_normal);
            }
        }

    }

    public void onDestroy() {
        if(this.mViewPager != null) {
            this.mViewPager.onDestroy();
        }

    }
}

