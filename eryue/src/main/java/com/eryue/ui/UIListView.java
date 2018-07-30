package com.eryue.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by bli.Jason on 2018/2/27.
 */

public class UIListView extends ListView implements AbsListView.OnScrollListener {

    private boolean isScrolledToTop = true;// 初始化的时候设置一下值
    private boolean isScrolledToBottom = false;

    public UIListView(Context context) {
        super(context);
    }

    public UIListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnScrollListener(this);
    }


    int lastX;
    int lastY;
    int lastScrollY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = 0;
        int y = 0;
        int scrollY;
        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                lastScrollY = this.getScrollY();
                this.getParent().requestDisallowInterceptTouchEvent(true);

                break;
            case MotionEvent.ACTION_MOVE:
                scrollY = this.getScrollY();
                x = (int) event.getRawX();
                y = (int) event.getRawY();
                int deltaY = y - lastY;
                int deltaX = x - lastX;
//                Log.d("libo","lastScrollY="+lastScrollY+"------scrollY="+scrollY+"--------deltaX="+deltaX+"--------deltaY="+deltaY);
                if (Math.abs(deltaX)>Math.abs(deltaY)){
                    //横向滑动
                    this.getParent().requestDisallowInterceptTouchEvent(false);
                }else if(Math.abs(deltaX)<Math.abs(deltaY)){
                    //纵向滑动
//                    this.getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null!=scrollYListener){
            scrollYListener.onScrollYChanged(t);
        }
        // 这个log可以研究ScrollView的上下padding对结果的影响
        Log.d("libo","onScrollChanged l:" + l + " t: " + t + " oldl: " + oldl+ " oldt: " + oldt);
        Log.d("libo","onScrollChanged getScrollY():" + getScrollY() + " t: " + t + " paddingTop: " + getPaddingTop());
//        if (getScrollY() == 0) {
//            isScrolledToTop = true;
//            isScrolledToBottom = false;
//            this.getParent().requestDisallowInterceptTouchEvent(false);
//            Log.d("libo","onScrollChanged isScrolledToTop:" + isScrolledToTop);
//        } else if (getScrollY() + getHeight() - getPaddingTop() - getPaddingBottom() == getChildAt(0).getHeight()) {
//            isScrolledToBottom = true;
//            isScrolledToTop = false;
//            Log.d("libo","onScrollChanged isScrolledToBottom:" + isScrolledToBottom);
//            this.getParent().requestDisallowInterceptTouchEvent(false);
//        } else {
//            this.getParent().requestDisallowInterceptTouchEvent(true);
//            isScrolledToTop = false;
//            isScrolledToBottom = false;
//
//        }

    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (scrollY == 0) {
            isScrolledToTop = clampedY;
            isScrolledToBottom = false;
            this.getParent().requestDisallowInterceptTouchEvent(false);
            Log.d("libo","onOverScrolled isScrolledToTop:" + isScrolledToTop);
        } else {
            isScrolledToTop = false;
            isScrolledToBottom = clampedY;
            if (isScrolledToBottom){
                this.getParent().requestDisallowInterceptTouchEvent(false);
            }else{
                if (isFlingState != 1){
                    this.getParent().requestDisallowInterceptTouchEvent(true);
//                    Log.d("libo","onOverScrolled isScrolledToBottom:" + isScrolledToBottom+"---isFlingState="+isFlingState);
                }
            }

        }

//        Log.d("libo","onOverScrolled isScrolledToBottom:" + isScrolledToBottom+"------isFlingState="+isFlingState);
    }


    /**
     * 0.开始滚动 1.isFling 2.SCROLL_STATE_IDLE
     */
    public int isFlingState = 0;
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            // 开始滚动监听
            if (isFlingState != 1) {
                isFlingState = 0;
            }


        } else if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            if (isFlingState != 2) {
                isFlingState = 2;
            }
        } else if (scrollState == OnScrollListener.SCROLL_STATE_FLING) {
            isFlingState = 1;
            if (view.getFirstVisiblePosition() == 0
                    || view.getLastVisiblePosition() == view.getCount() - 1) {
                isFlingState = 2;
            }
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    private ScrollYListener scrollYListener;

    public void setScrollYListener(ScrollYListener scrollYListener) {
        this.scrollYListener = scrollYListener;
    }

    public interface ScrollYListener{
        void onScrollYChanged(int top);
    }
}
