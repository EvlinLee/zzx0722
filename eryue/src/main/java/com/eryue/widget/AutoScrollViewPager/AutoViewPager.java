package com.eryue.widget.AutoScrollViewPager;

/**
 * Created by dazhou on 2018/2/7.
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;


import java.util.Timer;
import java.util.TimerTask;


public class AutoViewPager extends ViewPager {
    private static final String TAG = "AutoViewPager";
    private int currentItem;
    private Timer mTimer;
    private AutoTask mTask;
    private Runnable runnable = new Runnable() {
        public void run() {
            currentItem = getCurrentItem();
            if(currentItem == getAdapter().getCount() - 1) {
                currentItem = 0;
            } else {
                currentItem++;
            }

            setCurrentItem(currentItem);
        }
    };
    private AutoHandler mHandler = new AutoHandler(null);

    public AutoViewPager(Context context) {
        super(context);
    }

    public AutoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(AutoViewPager viewPager, BaseViewPagerAdapter adapter) {
        adapter.init(viewPager, adapter);
    }

    public void start() {
        this.onStop();
        if(this.mTimer == null) {
            this.mTimer = new Timer();
        }

        this.mTimer.schedule(new AutoTask(), 3000L, 3000L);
    }

    public void updatePointView(int size) {
        if(this.getParent() instanceof AutoScrollViewPager) {
            AutoScrollViewPager pager = (AutoScrollViewPager)this.getParent();
            pager.initPointView(size);
        } else {
            Log.e("AutoViewPager", "parent view not be AutoScrollViewPager");
        }

    }

    public void onPageSelected(int position) {
        AutoScrollViewPager pager = (AutoScrollViewPager)this.getParent();
        pager.updatePointView(position);
    }

    public void onStop() {
        if(this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }

    }

    public void onDestroy() {
        this.onStop();
    }

    public void onResume() {
        this.start();
    }

    public boolean onTouchEvent(MotionEvent ev) {
        switch(ev.getAction()) {
            case 0:
                this.onStop();
                break;
            case 1:
                this.onResume();
        }

        return super.onTouchEvent(ev);
    }

    private static final class AutoHandler extends Handler {
        private AutoHandler(Object o) {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private class AutoTask extends TimerTask {
        private AutoTask() {
        }

        public void run() {
            mHandler.post(runnable);
        }
    }
}

