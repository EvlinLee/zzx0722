package com.eryue;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import com.ali.auth.third.core.MemberSDK;
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.eryue.util.CrashHandler;
import com.eryue.util.Logger;
import com.kepler.jd.Listener.AsyncInitListener;
import com.kepler.jd.login.KeplerApiManager;
import com.library.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
/*
 * 需要在主的配置文档里面
 * <application      android:name=".CrashApplication">
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 * */

/**
 * 主Application,负责百度云推送和报错日志打印
 *
 * @author afwang
 */
public class BaseApplication extends Application {
    public final static String MESSAGE_ANR_LOG = "应用程序在上一次使用时，未正常关闭，是否发送日志来帮助我们改善此问题。";

    private String anrLogText;

    public static final int HAVE_ANR_LOG = 0;

    public static final String TAG = "CrashHandler";

    public static Context applicationContext;

    private static Handler mainHandler;

    private static BaseApplication baseApplication;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();
    // 支持APPCLASS的动态切换存储map
    public Map<Integer, Integer> appClsMap;

    private static int count = 0;

    private static boolean bRunningBackground = false;


    /**
     * 京东cps联盟
     */
    private static final String jd_appKey = "eb629e520e114bdc9f6addfb46eb512a";

    /**
     * 京东cps联盟
     */
    private static final String jd_keySecret = "cc4f4ee0d357455380a8d195ebbea346";

    @Override
    public void onCreate() {
        super.onCreate();
        //过滤baidu进程初始化,防止自定义初始化走两遍
        if (!isDefaultProcess(getApplicationContext())) {
            return;
        }
        applicationContext = getApplicationContext();
        //#ifdef LOG
//@        CmdInfo2XslHelper.initActMap();
        //#endif
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(applicationContext);
        baseApplication = this;

        //创建默认的ImageLoader配置参数
        //Initialize ImageLoader with configuration.
        if(!ImageLoader.getInstance().isInited()){
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        }
//        UncaughtExceptionHandlerImpl.getInstance().init(this,BuildConfig.DEBUG);
        initTopActivityListener();

        KeplerApiManager.asyncInitSdk(this, jd_appKey, jd_keySecret,
                new AsyncInitListener() {
                    @Override
                    public void onSuccess() {
// TODO Auto-generated method stub
//                        Log.e("Kepler", "Kepler asyncInitSdk onSuccess ");
                    }
                    @Override
                    public void onFailure() {
// TODO Auto-generated method stub
//                        Log.e("Kepler",
//                                "Kepler asyncInitSdk 授权失败，请检查lib 工程资源引用；包名,签名证书是否和注册一致");

                    }
                });

        //电商SDK初始化
        AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
//                Toast.makeText(BaseApplication.this, "初始化成功", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int code, String msg) {
//                Toast.makeText(BaseApplication.this, "初始化失败,错误码="+code+" / 错误消息="+msg, Toast.LENGTH_SHORT).show();
            }
        });
        MemberSDK.turnOnDebug();



    }


    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.d("libo", "onTerminate");
        super.onTerminate();
    }
    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        Log.d("libo", "onLowMemory");
        super.onLowMemory();
    }
    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        Log.d("libo", "onTrimMemory");
        super.onTrimMemory(level);
    }





    protected boolean isDefaultProcess(Context context) {
        if (null == context)
            return false;
        String curProcessName = null;
        String packageName = context.getPackageName();
        boolean isDefaultProcess = false;

        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                curProcessName = appProcess.processName;
            }
        }
        if (null != packageName && packageName.equals(curProcessName)) {
            isDefaultProcess = true;
        }
//	    Log.e("BaseApplication", "packageName:" + packageName + "	,curProcessName:" + curProcessName);
        return isDefaultProcess;
    }




    public static BaseApplication getInstance() {
        return baseApplication;
    }



    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    //获取手机内存大小
    private String getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "/t");
            }
            if (arrayOfString != null && arrayOfString.length > 1) {
                initial_memory = StringUtils.valueOfInteger(arrayOfString[1], 0) * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            }
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return Formatter.formatFileSize(getBaseContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }


    public final static String ANONYMOUS_LOGIN_NAME = "_anonymousLoginName";



    private static String getHardwareAddress() {
        try {
            Enumeration<NetworkInterface> e = NetworkInterface
                    .getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface ni = e.nextElement();
                if ("wlan0".equals(ni.getName())) {
                    byte[] mac = ni.getHardwareAddress();
                    if (mac != null) {
                        return displayMac(mac);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String displayMac(byte[] mac) {
        String macStr = "";
        for (int i = 0; i < mac.length; i++) {
            byte b = mac[i];
            int intValue = 0;
            if (b >= 0)
                intValue = b;
            else
                intValue = 256 + b;
            String hexStr = Integer.toHexString(intValue);
            if (hexStr.length() == 1) {
                macStr += "0";
            }
            macStr += hexStr;
        }
        return macStr;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initTopActivityListener() {
        if (Build.VERSION.SDK_INT >= 14) {
            activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    actStackMap.put(getActKey(activity), new WeakReference<Activity>(activity));
                }

                @Override
                public void onActivityStarted(Activity activity) {
                    if (count == 0) {
                        bRunningBackground = false;
                        subMitUserAction();
                    }
                    count++;
                }

                @Override
                public void onActivityResumed(Activity activity) {
                    setCurrentActivity(activity);
                    sortActivityStack(activity);
                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {
                    count--;
                    if (count == 0) {
                        bRunningBackground = true;
                    }
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    try {
                        actStackMap.remove(getActKey(activity));
                    } catch (Exception ex) {
                    }
                }
            };
            registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        }
    }


    private String getActKey(Activity activity) {
        return activity.hashCode() + "#" + activity.getClass().getName();
    }


    //提交用户登录或者后台切前台功能点
    public void subMitUserAction() {


    }


    /**
     * 监听所有activity中的生命周期
     */
    ActivityLifecycleCallbacks activityLifecycleCallbacks;
    private WeakReference<Context> wfCurrentActivity;
    private LinkedHashMap<String, WeakReference<Activity>> actStackMap = new LinkedHashMap<>();

    /**
     * 清理堆栈 (崩溃后 退出程序操作)
     */
    public void clearActivityStack() {
        Iterator<String> keyIter = actStackMap.keySet().iterator();
        while (keyIter.hasNext()) {
            String key = keyIter.next();
            WeakReference<Activity> wr = actStackMap.get(key);
            if (wr != null && wr.get() != null) {
                wr.get().finish();
            }
        }
    }

    /**
     *
     * @return 返回最底部的activity
     */
    public Activity getRootActivity() {
        if (null == actStackMap || actStackMap.size() <= 0) {
            return null;
        }
        WeakReference<Activity> wr = actStackMap.entrySet().iterator().next().getValue();
        if (wr == null || wr.get() == null) {
            return null;
        }
        return wr.get();
    }


    /**
     * @return 重新排序activity，寻找到LinkedHashMap中存储的activity
     */
    private void sortActivityStack(Activity activity) {
        if (null == actStackMap || actStackMap.size() <= 0) {
            return;
        }
        //重新将当前的activity放到链表尾部
        Iterator<Map.Entry<String, WeakReference<Activity>>> iterator = actStackMap.entrySet().iterator();
        int index = 0;
        int stackSize = actStackMap.size();
        boolean needReAdd = false;
        while (iterator.hasNext()) {
            Object str = iterator.next().getKey();
            if ((index < stackSize - 1) && str instanceof String && str.equals(getActKey(activity))) {
                iterator.remove();
                needReAdd = true;
            }
            index++;
        }
        if (needReAdd) {
            actStackMap.put(getActKey(activity), new WeakReference<Activity>(activity));
        }
    }



    /**
     * @return 获取Activity的数量
     */
    public int getNumActivities() {
        if (null == actStackMap) {
            return 0;
        }
        return actStackMap.size();
    }

    public void setCurrentActivity(Activity Context) {
        wfCurrentActivity = new WeakReference<android.content.Context>(Context);
    }

    public Context getCurrentActivity() {
        if (wfCurrentActivity != null) {
            return wfCurrentActivity.get();
        }
        return null;
    }

    /**
     * 注意使用时机，在UI线程使用安全
     * @return
     */
    public boolean getAppIsRunningBackgroud() {
        return bRunningBackground;
    }

    /**
     *
     * @param context 发起跳转处的上下文
     */
    public void popToRootAct(Context context) {
        if (null == context) {
            return;
        }
        Activity rootAct = getRootActivity();
        if (null == rootAct) {
            return;
        }
        if (getNumActivities() <= 1) {
            return;
        }
        try {
            Intent intent = new Intent(context, rootAct.getClass());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
