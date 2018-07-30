package com.eryue.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.eryue.GoodsContants;
import com.library.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author
 * @date 2011-5-5 下午07:50:43
 */

public final class Logger {
    private String PATH;
    private String FILENAME = "/log";
    private static Logger _instance;
    private boolean isClose = true;
    private final String emotyString = "";

    private Context context;

    private Logger(Context context){
        BaseNetHandler.getInstance(context);
        PATH = FileUtil.getExternalCacheDirectory(context,null).getAbsolutePath();
    }

    public static Logger getInstance(Context context) {
        if (null == _instance&&null!=context){
            _instance = new Logger(context);
        }
        return _instance;
    }

    /**
     * 写日志
     * @param folderName
     * @param fileName
     * @param content
     */
    public void writeLog_new(String folderName, String fileName, String content) {
        if (BaseNetHandler.DEBUG) {
            LogHelper.print(fileName, content);
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
            String date = sDateFormat.format(new java.util.Date());
            String text = date + " " + content + "\n";
            String path = PATH + "/" + folderName + "/";
            String filePath = path + fileName + ".log";
            if (!TextUtils.isEmpty(PATH)){
                onWrite(text, path, filePath);
            }
        }
    }

    private void onWrite(String content, String filePath, String fileName) {
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {

                File path = new File(PATH);
                File f = new File(PATH + FILENAME);

                // 目录
                if ((TextUtils.isEmpty(filePath) || TextUtils.isEmpty(fileName))) {
                    filePath = Environment.getExternalStorageDirectory().getPath()
                            + GoodsContants.PATH_LOG;
                    fileName = filePath + FILENAME;
                }

                path = new File(filePath);
                f = new File(fileName);
                if (!path.exists()) {
                    path.mkdirs();
                }
                if (!f.exists()) {
                    f.createNewFile();

                }
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f, true));
                osw.write(content);
                osw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                FileOutputStream fos = context.openFileOutput(
                        fileName,
                        Context.MODE_WORLD_WRITEABLE
                                | Context.MODE_WORLD_READABLE
                                | Context.MODE_APPEND);
                if (fos != null) {
                    fos.write(content.getBytes());
                    fos.flush();
                    fos.close();
                }
            } catch (Exception ee) {
                e.printStackTrace();
            }
        }
    }

}
