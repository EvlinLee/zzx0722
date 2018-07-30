package com.eryue.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static final String TAG = "PushDemoActivity";

    public static String logStringCache = "";

    private static final String KEY_PUSH_CHANNEL = "key_push_channel";
    private static final String KEY_PRIVATE_PUSH_CHANNEL = "key_private_push_channel";
    private static final String KEY_PRIVATE_PUSH_TOKEN_ID = "key_private_push_token_id";

    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {
        }
        return apiKey;
    }

    // 用share preference来实现是否绑定的开关。在ionBind且成功时设置true，unBind且成功时设置false
    public static String hasBind(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("bind_flag", "");
    }

    public static void setBaiduInfo(Context context, String baiduId, String channelId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("baiduId", baiduId);
        editor.putString("channelId", channelId);
        editor.commit();
    }

    public static String[] getBaiduInfo(Context context) {
        String[] str = new String[2];
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        str[0] = sp.getString("baiduId", "");
        str[1] = sp.getString("channelId", "");
        return str;
    }

    public static void setBind(Context context, String flag) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("bind_flag", flag);
        editor.commit();
    }

    // 用share preference来实现是否绑定的开关。在ionBind且成功时设置true，unBind且成功时设置false
    public static boolean hasPushFun(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String flag = sp.getString("push_flag", "");
        if (flag == null || flag.trim().length() == 0) { // 默认开启
            setPushFun(context, "1");
            return true;
        }
        return "1".equalsIgnoreCase(flag);
    }

    public static void setPushFun(Context context, String flag) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("push_flag", flag);
        editor.commit();
    }

    /**
     * 存储 推送通道 标记
     * 0   表示只启动百度推送（初始状态和紧急切换），
     * 1   表示单独启动小米推送（正式状态），
     * 2   同时启动小米和百度推送（积累用户阶段），
     * 负数表示错误
     *
     * @param context
     * @param channelFlag
     */
    public static void savePushChannelInfo(Context context, int channelFlag) {
        if (!PushCommUtil.isPushSwitchValid(channelFlag)) {   // 处理异常参数
            return;
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putInt(KEY_PUSH_CHANNEL, channelFlag);
        editor.commit();
    }

    /**
     * 获取 存储的 推送通道 标记
     * [主要 用于 用户登录后 开启推送的操作]
     *
     * @param context
     * @return
     */
    public static int getSavedPushChannelInfo(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(KEY_PUSH_CHANNEL, PushCommUtil.FLAG_ONLY_BAIDU_PUSH); // 默认只开百度
    }


    public static List<String> getTagsList(String originalText) {
        if (originalText == null || originalText.equals("")) {
            return null;
        }
        List<String> tags = new ArrayList<String>();
        int indexOfComma = originalText.indexOf(',');
        String tag;
        while (indexOfComma != -1) {
            tag = originalText.substring(0, indexOfComma);
            tags.add(tag);

            originalText = originalText.substring(indexOfComma + 1);
            indexOfComma = originalText.indexOf(',');
        }

        tags.add(originalText);
        return tags;
    }

    public static String getLogText(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("log_text", "");
    }

    public static void setLogText(Context context, String text) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("log_text", text);
        editor.commit();
    }

    /**
     * 获取系统属性
     *
     * @param propName
     * @return
     */
    public static String getSysProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }


    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isChinaPhoneLegal(String str){
        try{
            String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(str);
            return m.matches();
        }catch (Exception e){

        }
        return  false;

    }

}
