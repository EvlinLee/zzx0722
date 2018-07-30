package com.eryue.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import base.BaseActivity;

/**
 * 沉浸式 状态栏 （Android 4.4+）
 * Created by cmwei on 2016/8/17.
 */
public class StatusBarCompat {


    public static void setStatusBarColor(BaseActivity activity, int statusColor, int navgationColor) {
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);

        Logger.getInstance(activity).writeLog_new("statusbar","statusbar","setStatusBarColor");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (checkDeviceHasNavigationBar(activity)) { // 屏幕虚拟键会有底部弹框遮盖问题
//                return;
//            }
            //First translucent status bar.
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = getStatusBarHeight(activity);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                //After LOLLIPOP not translucent status bar
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //Then call setStatusBarColor.
//                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                window.setNavigationBarColor(Color.TRANSPARENT);
                if (mContentView.getChildAt(0) != null) {
                    ViewCompat.setFitsSystemWindows(mContentView.getChildAt(0), true);
                }
            } else {
                //Before LOLLIPOP create a fake status bar View.
                View mTopView = mContentView.getChildAt(0);
                if (mTopView != null && mTopView.getLayoutParams() != null && mTopView.getLayoutParams().height == statusBarHeight) {
                    //if fake status bar view exist, we can setBackgroundColor and return.
                    mTopView.setBackgroundColor(Color.TRANSPARENT);
                    return;
                }
                //now topView is layout content
                if (mTopView != null) {
                    ViewCompat.setFitsSystemWindows(mTopView, true);
                }
                mTopView = new View(activity);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
                mTopView.setBackgroundColor(Color.TRANSPARENT);
                mContentView.addView(mTopView, 0, lp);
            }
            try {
                if (activity.getMainBodyView() != null) {
                    ViewCompat.setFitsSystemWindows(activity.getMainBodyView(), true);
                }
            } catch (Exception ex) {
            }
        }
    }

    public static void setStatusBarColor(Activity activity,int statusColor){
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(statusColor);
        }
    }


    private static void setStatusTransParent(Window window) {

        try {
            View decordView = window.getDecorView();     //获取DecorView实例
            Field field = View.class.getDeclaredField("mSemiTransparentStatusBarColor");  //获取特定的成员变量
            field.setAccessible(true);   //设置对此属性的可访问性
            field.setInt(decordView, Color.TRANSPARENT);  //修改属性值
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void translucentStatusBar(Activity activity) {
        Window window = activity.getWindow();
        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //First translucent status bar.
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //After LOLLIPOP just set LayoutParams.
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (mContentView.getChildAt(0) != null) {
                    ViewCompat.setFitsSystemWindows(mContentView.getChildAt(0), false);
                }
            } else {
                //如果添加了StatusBar, 需要移除这个View
                View statusBarView = mContentView.getChildAt(0);
                if (statusBarView != null && statusBarView.getLayoutParams() != null && statusBarView.getLayoutParams().height == getStatusBarHeight(activity)) {
                    mContentView.removeView(statusBarView);
                }
                if (mContentView.getChildAt(0) != null) {
                    ViewCompat.setFitsSystemWindows(mContentView.getChildAt(0), false);
                }
            }
        }
    }

    //获取状态栏的高度
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }

    private static boolean checkDeviceHasNavigationBar(Context context) {
        // 判断 系统home屏幕内是为了规避 沉浸状态栏的 popWindow显示问题, 获取失败则不显示
        boolean hasNavigationBar = true;
        try {
            Resources rs = context.getResources();
            int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
            if (id > 0) {
                hasNavigationBar = rs.getBoolean(id);
            }
        } catch (Exception ex) {
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            hasNavigationBar = true;
        }
        return hasNavigationBar;
    }


}
