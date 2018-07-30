package com.eryue.util;

import android.content.Context;

public class BaseNetHandler {

    public static boolean DEBUG = true;

    public static String SWITCH_LOGO = "SWITCH_LOGO";

    /**
     * ylzhang添加网络登录优化
     **/
    public final static String NETSTATUS_ACTION = "wind.android.net.NETSTATUS_CHANGE";
    private static BaseNetHandler instance;
    private Context context;

    private BaseNetHandler(Context context) {
        init(context);
    }

    public static BaseNetHandler getInstance(Context context) {
        if (instance == null) {
            instance = new BaseNetHandler(context);
        }
        return instance;
    }

    public void init(Context context) {
        try {
            if (this.context == null) {
                this.context = context;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //增加键盘和屏幕的监听

        DEBUG = SharedPreferencesUtil.getInstance().getBoolean(
                SWITCH_LOGO, false);
    }

}
