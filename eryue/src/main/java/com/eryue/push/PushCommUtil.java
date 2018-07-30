package com.eryue.push;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.eryue.BaseApplication;
import com.eryue.util.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by cmwei on 2017/3/15.
 */
public class PushCommUtil {

    public static final String PUSH_LOG_DIR = "LOG_PUSH";    // PUSH 模块日志文件名
//    public static final int FLAG_PUSH_CHANNEL_DEBUG = -1;    // debug 状态下的 推送渠道配置值

    public static final int APP_ID_WINDINFO = 12;   // 机构版 应用ID
//    public static final int APP_ID_WINDSTOCK = 9;   // 个人版 应用ID

    public static final int FLAG_ONLY_BAIDU_PUSH = 0;   // 只开启百度推送
    public static final int FLAG_ONLY_PRIVATE_PUSH = 1; // [小米/华为机型] 只开启小米/华为推送
    public static final int FLAG_DOUBLE_PUSH = 2;       // [小米/华为机型] 开启百度+小米/华为推送

    public static final short PUSH_PLATFORM_ERROR = 0;
    public static final short PUSH_PLATFORM_BAIDU = 2;
    public static final short PUSH_PLATFORM_HUAWEI = 5;
    public static final short PUSH_PLATFORM_XIAOMI = 6;

    /**
     * 记录 日志
     *
     * @param logText
     */
    public static void writeDebugFileLog(String logText) {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Logger.getInstance(BaseApplication.getInstance()).writeLog_new(PUSH_LOG_DIR, today, logText);
    }

    /**
     * 获取 应用 VersionCode
     *
     * @return
     */
    private static int getAppVersionCode() {
        int vCode = 0;
        try {
            Context ctx = BaseApplication.getInstance();
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), 0);
            vCode = pi.versionCode;
        } catch (Exception ex) {
        }
        return vCode;
    }



    /**
     * 推送标记是否有效
     * 0   表示只启动百度推送（初始状态和紧急切换），
     * 1   表示单独启动小米推送（正式状态），
     * 2   同时启动小米和百度推送（积累用户阶段），
     * 负数表示错误
     *
     * @return
     */
    public static boolean isPushSwitchValid(int channelFlag) {
        return channelFlag >= 0 && channelFlag < 3;
    }

}
