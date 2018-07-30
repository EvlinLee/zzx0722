package com.eryue.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.text.TextUtils;

import com.eryue.BaseApplication;
import com.library.util.StringUtils;
import com.library.util.UIScreen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;


/**
 * **************************************************************** 文件名称 :
 * SysUtil.java 作 者 : kyang.Kris 创建时间 : 2013-6-25 上午11:00:20 文件描述 : 一些工具方法 修改历史
 * : 2013-6-25 1.00 初始版本
 *****************************************************************/
public final class SysUtil {

    private SysUtil() {
        //
    }

    /**
     * 获取中文名字
     *
     * @param aPackageName 包名
     * @return 中文名字
     */
    public static String getPackageChinese(String aPackageName) {
//		if (Const.PROCESS_NAME.equals(aPackageName)) {
//			return Const.APP_NAME;
//		}
//		if(StoreData.appName != null && StoreData.appName.equals("")){
//			return StoreData.appName;
//		}

        Context context = BaseApplication.getInstance();
        return context.getString(context.getApplicationInfo().labelRes);
    }

    /**
     * 获取版本号
     *
     * @param aContext
     * @return
     */
    public static String getVersion(Context aContext) {
        String version = "";
        if (null != aContext) {
            try {
                version = aContext.getPackageManager().getPackageInfo(
                        aContext.getPackageName(), 0).versionName;
            } catch (NameNotFoundException e) {
                version = "";
            }
        }

        return version;
    }

    /**
     * 尝试获取小版本号
     *
     * @param aContext 上下文
     * @return 小版本号
     */
    public static String getSmallVersion(Context aContext) {
        String lastVersion = ".";
        try {
//			Context c = aContext.createPackageContext(Const.PROCESS_NAME,
//					Context.CONTEXT_INCLUDE_CODE
//							| Context.CONTEXT_IGNORE_SECURITY);

            // 以下方法从万得股票的读取版本号的地方拷贝来的
            InputStream is = aContext.getAssets().open("lastversion");

            byte[] b = new byte[is.available()];
            is.read(b);

            String str = new String(b);

            if (str != null && !str.equals("")) {
                lastVersion += str.substring(str.indexOf("#") + 1,
                        str.indexOf("\r"));
            }
        } catch (Exception e) {
            lastVersion = ".";
        }

        return lastVersion;
    }

    /**
     * 获取系统信息
     *
     * @return 系统信息
     */
    public static String getDeviceInfo() {
        return Build.BRAND + " " + Build.MODEL + " "
                + Build.DEVICE + " android"
                + Build.VERSION.RELEASE;
//		android.os.Build.VERSION.SDK
//		android.os.Build.VERSION.SDK_INT
    }


    /**
     * 邮件正文
     *
     * @return
     */
    public static String getMailBody(Context context, boolean isBody) {
        StringBuffer sb = new StringBuffer();
        sb.append("为了尽快解决您的问题，我们收集以下基本信息");
        sb.append("\n设备信息:");
        sb.append(getDeviceInfo());
        sb.append("\n分辨率:");
        sb.append(UIScreen.screenWidth + " * " + UIScreen.screenHeight);
        sb.append("\n内存:");
        sb.append(getTotalMemory(context));
        return sb.toString();
    }


    //获取总内存
    public static String getTotalMemory(Context context) {
        if (null == context) {
            return "";
        }
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        double resultMemory = 0;
        String resultStr = "";
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");

            // 获得系统总内存，单位是KB，乘以1024转换为Byte
            if (arrayOfString.length > 1) {
                initial_memory = StringUtils.valueOfLong(arrayOfString[1], 0);
            }
            //以M为单位
            resultMemory = initial_memory / 1024.0;
            //以G为单位
            if (resultMemory > 1000) {
                resultMemory = resultMemory / 1024.0;
                resultStr = String.format("%.2f ", resultMemory) + " GB";
            } else {
                resultStr = String.format("%.2f ", resultMemory) + " MB";
            }
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return resultStr;
//        return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }

    /**
     * Role:获取当前设置的电话号码
     * <BR>Date:2012-3-12
     * <BR>@author CODYY)peijiangping
     */
    //没使用，需要权限-》注释掉
//    public static String getNativePhoneNumber(Context context) {
//    	 TelephonyManager telephonyManager = (TelephonyManager) context
//			.getSystemService(Context.TELEPHONY_SERVICE);
//        String NativePhoneNumber=null;
//        NativePhoneNumber=telephonyManager.getLine1Number();
//        return NativePhoneNumber;
//    }


    /**
     * 根据ProcessName获取PID号，获取不到返回-1
     *
     * @param aContext     上下文
     * @param aProcessName 进程名字
     * @return PID号
     */
    public static int getPorcessPid(Context aContext, String aProcessName) {
        int pid = -1;

        if (null != aContext && TextUtils.isEmpty(aProcessName) ^ true) {
            ActivityManager manager = (ActivityManager) aContext
                    .getSystemService(Context.ACTIVITY_SERVICE);

            List<RunningAppProcessInfo> apps = manager.getRunningAppProcesses();

            for (RunningAppProcessInfo info : apps) {
                if (aProcessName.equals(info.processName)) {
                    pid = info.pid;
                    break;
                }
            }
        }

        return pid;
    }

    public static String getUniqueID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their device or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their device, there will be a duplicate entry
        String serial = null;
        try {
            serial = Build.SERIAL;
            if (TextUtils.isEmpty(serial))
                serial = Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
}
