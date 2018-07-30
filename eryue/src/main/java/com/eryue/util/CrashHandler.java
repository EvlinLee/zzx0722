package com.eryue.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.eryue.BaseApplication;
import com.eryue.BuildConfig;
import com.eryue.R;
import com.library.util.FileUtil;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 * @author user
 */
public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";


    //系统默认的UncaughtException处理类   
    public static final String toastReport = "很抱歉,程序出现异常,即将退出";

    //��־��·��
    public String logPath;

    //系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    //�����Context����
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault());

    //打包crash日志时的进度提示框
//    private ProgressDialog mProgressDialog;

    private Handler mHandler;

    public static final int POP_DIALOG = 0;

    /**
     * 压缩结束
     */
    public static final int ZIP_CRASHFILE_FINISH = 1;

    public static final int ZIP_CRASHFILE = 2;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);

        logPath = FileUtil.getExternalCacheDirectory(context,null) + "/crash/";
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //注释，程序崩溃自动拉起的service
//        CheckAliveService.setExit(mContext, CheckAliveService.CRASH_EXIT_TYPE,
//                ex);
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        }
        //#ifndef CRASH
//@        else {
//@            try {
//@                Thread.sleep(3000);
//@            } catch (InterruptedException e) {
//@                Log.e(TAG, "error : ", e);
//@            }
//@          //退出程序
//@            onExit();
//@        }
        //#endif
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */

    File file;
    String zipFilePath;

    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        try {
            //#ifndef RELEASE
            if (BuildConfig.DEBUG) {
                Writer writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer);
                ex.printStackTrace(printWriter);
                Throwable cause = ex.getCause();
                while (cause != null) {
                    cause.printStackTrace(printWriter);
                    cause = cause.getCause();
                }
                printWriter.close();
                Log.e("error", writer.toString());
            }
            //#endif
            new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        Looper.prepare();
                        // 收集设备参数信息
                        collectDeviceInfo(mContext);
                        // 保存日志文件
                        saveCrashInfo2File(ex);
                        onExit();
                        Looper.loop();
                    } catch (Exception e) {
                        onExit();
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        } catch (Exception e) {
            onExit();
            e.printStackTrace();
        }
//      MainUIActivity.getMainController().onDestroy();
        return true;
    }


    /**
     * crash日志目录是否存在
     *
     * @return crash日志目录是否存在
     */
    public File crashFileExits() {
        File crashLogDir = new File(logPath);
        if (crashLogDir.exists()) {
            return crashLogDir;
        }
        return null;
    }

    public final static String MESSAGE_CRASH_LOG = "应用程序未正常关闭，是否发送日志来帮助我们改善此问题。";

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

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private void saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        try {
            // 手机的一些信息
            sb.append(SysUtil.getMailBody(mContext, false));
            sb.append("\n\n");
            int i = 0;
            for (Map.Entry<String, String> entry : infos.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key + "=" + value + "\n");
            }
            // 增加崩溃栈顶页面
            sb.append(BaseApplication.getInstance().getCurrentActivity().getClass().getName() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = logPath;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileUtil.writeFile(sb.toString().getBytes(), path, fileName);
            }
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
    }


    /**
     * 版本号
     *
     * @param context
     * @return
     */
    public String getMailTitlePre(Context context) {
        return context.getResources().getString(R.string.app_name)
                + SysUtil.getVersion(context) + SysUtil.getSmallVersion(context);
    }

    /**
     * 做一些UI处理
     *
     * @param text
     * @return
     */
    private boolean dealLogText(String text) {

        if (TextUtils.isEmpty(text)) {
            Toast.makeText(mContext, "没有找到日志", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void onExit() {
//		if(null!=DBBase.getInstance()){
//			DBBase.getInstance().close();
//		}
        try {

            BaseApplication.getInstance().clearActivityStack();

            android.os.Process.killProcess(android.os.Process.myPid());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}  