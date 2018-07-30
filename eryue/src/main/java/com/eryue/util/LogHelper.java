package com.eryue.util;

import android.util.Log;

import com.eryue.BuildConfig;


/**
 * ****************************************************************
 * 文件名称	: LogHelper.java
 * 作    者	: gzhao
 * 创建时间	: 2012-2-2 下午07:45:37
 * 文件描述	: 日志工具类
 * 修改历史	: 2012-2-2 1.00 初始版本
 * ****************************************************************
 */
public class LogHelper {

    public static final int VERBOSE = 0;
    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int WARN = 3;
    public static final int ERROR = 4;

    /**
     * 在控制台打印输出
     *
     * @param msg 日志信息
     */
    public static void print(String msg) {
        Log.v("log", msg);
    }

    /**
     * 在控制台打印输出
     *
     * @param tag 日志tag
     * @param msg 日志信息
     */
    public static void print(String tag, String msg) {
        Log.v(tag, msg);
    }

    /**
     * 在控制台打印输出
     *
     * @param tag     日志tag
     * @param msg     日志信息
     * @param logType 日志类型
     */
    public static void print(String tag, String msg, int logType) {
        if (logType == Log.VERBOSE) {
            Log.v(tag, msg);
        } else if (logType == Log.DEBUG) {
            Log.d(tag, msg);
        } else if (logType == Log.INFO) {
            Log.i(tag, msg);
        } else if (logType == Log.WARN) {
            Log.w(tag, msg);
        } else if (logType == Log.ERROR) {
            Log.e(tag, msg);
        } else {
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, Object... messages) {
        // Only log VERBOSE if build type is DEBUG
        log(tag, Log.VERBOSE, null, messages);
    }

    public static void d(String tag, Object... messages) {
        // Only log DEBUG if build type is DEBUG
        log(tag, Log.DEBUG, null, messages);
    }

    public static void i(String tag, Object... messages) {
        log(tag, Log.INFO, null, messages);
    }

    public static void w(String tag, Object... messages) {
        log(tag, Log.WARN, null, messages);
    }

    public static void w(String tag, Throwable t, Object... messages) {
        log(tag, Log.WARN, t, messages);
    }

    public static void e(String tag, Object... messages) {
        log(tag, Log.ERROR, null, messages);
    }

    public static void e(String tag, Throwable t, Object... messages) {
        log(tag, Log.ERROR, t, messages);
    }

    public static void log(String tag, int level, Throwable t, Object... messages) {
        if (BuildConfig.DEBUG) {
            String message;
            if (t == null && messages != null && messages.length == 1) {
                // handle this common case without the extra cost of creating a stringbuffer:
                message = messages[0].toString();
            } else {
                StringBuilder sb = new StringBuilder();
                if (messages != null) for (Object m : messages) {
                    sb.append(m);
                }
                if (t != null) {
                    sb.append("\n").append(Log.getStackTraceString(t));
                }
                message = sb.toString();
            }
            Log.println(level, tag, message);
        }
    }
}
