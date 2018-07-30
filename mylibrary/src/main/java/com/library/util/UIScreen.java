package com.library.util;

import android.app.Activity;
import android.content.res.Resources;
import android.view.Display;

public class UIScreen {
    private static UIScreen _mainScreen;
    public static int screenWidth = 0;
    public static int screenHeight = 0;
    public static int statusBarHeight = 0;
    public static int statusBarHeightEx = 0;
    public static float density;
    public static float scaleDensity;
    public static final int PORTRAIT = 0;//竖屏
    public static final int LANDSCAPE = 1;//横屏

    /**
     * 工具栏高度
     */
    public static int toolBarHeight = 0;
    public static int contentHeight = 0;

    /**
     * 构造一个和设备屏幕一样大的UIScreen实例
     */
    private UIScreen(Activity baseActivity) {
        Display display = baseActivity.getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        statusBarHeight = getStatusBarHeight(baseActivity.getResources());
        density = baseActivity.getApplicationContext().getResources().getDisplayMetrics().density;
        display = null;
        scaleDensity = baseActivity.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * 构造一个和设备屏幕一样大的UIScreen实例
     *
     * @param orientation 横屏或者竖屏
     */
    private UIScreen(Activity baseActivity, int orientation) {
        Display display = baseActivity.getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        if (orientation == PORTRAIT) {
            if (screenWidth > screenHeight) {
                int temp = screenWidth;
                screenWidth = screenHeight;
                screenHeight = temp;
            }
        } else if (orientation == LANDSCAPE) {
            if (screenWidth < screenHeight) {
                int temp = screenWidth;
                screenWidth = screenHeight;
                screenHeight = temp;
            }
        }
        density = baseActivity.getApplicationContext().getResources().getDisplayMetrics().density;
        display = null;
        scaleDensity = baseActivity.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
    }

    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";

    public static int getStatusBarHeight(Resources res) {
        int result = 0;
        int resourceId = res.getIdentifier(STATUS_BAR_HEIGHT_RES_NAME, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 获取主屏幕的单根实例
     *
     * @return 返回主屏幕的单根实例
     */
    public static final UIScreen getMainScreen(Activity baseActivity) {
        if (_mainScreen == null) {
            _mainScreen = new UIScreen(baseActivity);
        }
        return _mainScreen;
    }

    /**
     * 重置主屏幕实例
     *
     * @return 主屏幕的单根实例
     */
    public static final UIScreen resetMainScreen(Activity baseActivity) {
        _mainScreen = new UIScreen(baseActivity);
        return _mainScreen;
    }

    public static final int getWidthByDensity() {
        return (int) (screenWidth / density);
    }

    /**
     * 重置主屏幕实例
     *
     * @return 主屏幕的单根实例
     */
    public static final UIScreen resetMainScreen(Activity baseActivity, int orientation) {
        _mainScreen = new UIScreen(baseActivity, orientation);
        return _mainScreen;
    }
}
