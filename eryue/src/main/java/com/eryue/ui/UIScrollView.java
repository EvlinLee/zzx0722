package com.eryue.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by bli.Jason on 2018/2/12.
 */

public class UIScrollView extends ScrollView {

    private boolean isScrolledToTop = true;// 初始化的时候设置一下值
    private boolean isScrolledToBottom = false;

    public UIScrollView(Context context) {
        super(context);
    }

    public UIScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    int lastX;
    int lastY;
    int lastScrollY;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = 0;
        int y = 0;
        int scrollX;
        int scrollY;
        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                lastScrollY = this.getScrollY();
                this.getParent().requestDisallowInterceptTouchEvent(true);

                break;
            case MotionEvent.ACTION_MOVE:

                x = (int) event.getRawX();
                y = (int) event.getRawY();
                int deltaY = y - lastY;
                int deltaX = x - lastX;
//                if ((Math.abs(scrollY - lastScrollY)<10)){
//                    //左右滑动
//                    this.getParent().requestDisallowInterceptTouchEvent(false);
//                }
                if (Math.abs(deltaX)>Math.abs(deltaY)){
                    //横向滑动
                    this.getParent().requestDisallowInterceptTouchEvent(false);
                }else if(Math.abs(deltaX)<Math.abs(deltaY)){
                    //纵向滑动
//                    this.getParent().requestDisallowInterceptTouchEvent(true);
                }
//                Log.d("libo","scrollY="+scrollY+"-----------lastScrollY="+lastScrollY+"-----------deltaX="+deltaX+"-----------deltaY="+deltaY);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onTouchEvent(event);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int x = 0;
//        int y = 0;
//        int scrollY;
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                lastX = (int) event.getRawX();
//                lastY = (int) event.getRawY();
//                lastScrollY = this.getScrollY();
//                this.getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                x = (int) event.getRawX();
//                y = (int) event.getRawY();
//                int deltaY = y - lastY;
//                int deltaX = x - lastX;
//
//                scrollY = this.getScrollY();
//                Log.d("libo","scrollY="+scrollY+"-----------lastScrollY="+lastScrollY+"-----------deltaX="+deltaX+"-----------deltaY="+deltaY);
//                if (scrollY == 0||(lastScrollY!=0&&(Math.abs(scrollY - lastScrollY)<5))) {
//                    //滑到顶后，继续滑动，就滑到上一个其他的view
//                    this.getParent().requestDisallowInterceptTouchEvent(false);
//                }else if (Math.abs(deltaX) > Math.abs(deltaY)) {
//                    this.getParent().requestDisallowInterceptTouchEvent(false);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                this.getParent().requestDisallowInterceptTouchEvent(false);
//            default:
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // 这个log可以研究ScrollView的上下padding对结果的影响
//        Log.d("libo","onScrollChanged getScrollY():" + getScrollY() + " t: " + t + " paddingTop: " + getPaddingTop());
        if (null!=scrollListener){
            scrollListener.onScrollChanged();
        }
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
                this.getParent().requestDisallowInterceptTouchEvent(true);
            }

            Log.d("libo","onOverScrolled isScrolledToBottom:" + isScrolledToBottom);
        }
    }


    private ScrollListener scrollListener;

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public interface ScrollListener{
        void onScrollChanged();
    }

}
