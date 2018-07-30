package com.eryue;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Message;
import android.os.StatFs;
import android.text.TextUtils;

import com.eryue.mine.AboutUsActivity;
import com.eryue.util.WindHttpClient_;
import com.library.util.StringUtils;

import net.InterfaceManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;

import base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by enjoy on 2018/3/3.
 */

public class Update implements ActivityHandler.ActivityHandlerListener {

    private Context context;

    private static Update instance;

    private String baseIP = AccountUtil.getBaseIp();

    private Update(Context context){
        this.context = context;
    }

    public static Update getInstance(Context context){

        if (null==instance){

            synchronized (Update.class){
                if (null==instance){
                    instance = new Update(context);
                }
            }

        }
        instance.context = context;
        return instance;

    }


    public synchronized void checkUpdate(String url) {
        if (TextUtils.isEmpty(baseIP)||!baseIP.startsWith("http")) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SendUpdateReq callFunc = retrofit.create(InterfaceManager.SendUpdateReq.class);
        Call<InterfaceManager.SendUpdateResponse> call = callFunc.get(url);
        call.enqueue(new Callback<InterfaceManager.SendUpdateResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SendUpdateResponse> call, Response<InterfaceManager.SendUpdateResponse> response) {
                // 0：失败；1：正确
                if (null != response && null != response.body() && response.body().status == 1) {
                    dealUpdateData(response.body().result);

                } else {
                    if (null!=updateListener){
                        updateListener.onUpdateError();
                    }

                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SendUpdateResponse> call, Throwable throwable) {
                if (null!=updateListener){
                    updateListener.onUpdateError();
                }
            }
        });

    }
    private String getLocalVersion() {
        String localVersion = "";
        try {
            PackageInfo packageInfo = context
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    InterfaceManager.UpdateModel updateModel;
    //是否需要更新版本
    private void dealUpdateData(InterfaceManager.UpdateModel updateModel){
        //是否需要升级
        if (!TextUtils.isEmpty(updateModel.version)){

            String newVersion = updateModel.version.replaceAll("\\.","");
            String version = getLocalVersion();
            if(StringUtils.valueOfInt(newVersion)>StringUtils.valueOfInt(version.replaceAll("\\.",""))){
                this.updateModel = updateModel;
                updateUrl = updateModel.cgi;
                if (null != updateListener){
                    updateListener.onUpdateBack(true);
                }
                //后台下载安装包
//                downLoadFile(false);
            } else {
                if (null != updateListener){
                    updateListener.onUpdateBack(false);
                }
            }
        }

    }

    UpdateListener updateListener;

    public void setUpdateListener(UpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public interface UpdateListener{
        void onUpdateBack(boolean needUpdate);
        void onUpdateError();
    }

    // 升级相关
    private String updateUrl;
    private String updataTip;



    private String getOutputName(boolean temp) {
        if (null == updateUrl) {
            updateUrl = String.valueOf(new Random().nextInt());
        }
        if (null == updataTip) {
            updataTip = "软件有新的版本 是否立即升级";
        }
        String path = "/huizhuan/download/zhuzhuxia"+updateUrl.hashCode()+(temp ? ".temp" : ".apk");
        return path;
    }


    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case 104:

                downLoadFile(isNeedOpen);

                break;

            default:
                break;
        }

    }

    boolean isNeedOpen = false;
    /**
     * 用户点击版本后，打开下载好的版本，
     * 如果没有下载，就立即下载安装包
     */
    public synchronized void downLoadFile(boolean isNeedOpen){
        this.isNeedOpen = isNeedOpen;
        String name = getOutputName(false);
        final File file = new File(Environment.getExternalStorageDirectory(), name);
        //下载完成后，文件还不存在就不继续下载
        if (isLoadEnd&&!file.exists()){
            return;
        }
        if (!file.exists()) {
            update();
        }else{
            //提示需要升级
//            if (null!=updateListener){
//                updateListener.onUpdateBack(true);
//            }
            if (!isNeedOpen) {
                return;
            }
            if (context instanceof Activity){
                if (((Activity)context).isFinishing()){
                    return;
                }

            }
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setNegativeButton("稍后再说",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            dialog.setPositiveButton("立即安装",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            openFile(file);
                        }
                    });
            if (updataTip == null || updataTip.equals("")) {
                updataTip = "软件有新的版本 是否立即升级";
            }
            if (null!=updateModel&&!TextUtils.isEmpty(updateModel.version)){
                dialog.setTitle("发现新版本 "+updateModel.version);
            }else{
                dialog.setTitle("发现新版本");
            }
            dialog.setMessage(updataTip);
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    private void openFile(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private boolean readSDCard(int size) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long blockSize = sf.getBlockSize();
            long availCount = sf.getAvailableBlocks();
            long blockCount = sf.getBlockCount();
            if (blockSize * availCount > size * 2)
                return true;
        }
        return false;
    }

    boolean isDownLoading = false;
    boolean isLoadEnd = false;
    /**
     * 下载升级包
     */
    private void update() {
        if (TextUtils.isEmpty(updateUrl)||isDownLoading){
            return;
        }

        new AsyncTask<Void,Void,Void>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (isNeedOpen&&(context instanceof AboutUsActivity)){
                    ((AboutUsActivity)context).showProgressMum();
                }
                isDownLoading = true;
                isLoadEnd = false;
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    HttpGet httpget = new HttpGet(updateUrl);
                    File file = new File(Environment
                            .getExternalStorageDirectory(),
                            getOutputName(true));
                    int downloadedLength = 0;
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    } else {
                        file.delete();
                        file.createNewFile();
                        downloadedLength = (int) file.length();
                    }

                    if (downloadedLength > 0) {
                        httpget.addHeader("range", "bytes="
                                + downloadedLength + "-");
                    }
                    HttpResponse response = WindHttpClient_.getInstance()
                            .execute(httpget);
                    HttpEntity entity = response.getEntity();
                    int code = response.getStatusLine().getStatusCode();
                    if (!(code == HttpStatus.SC_OK || code == HttpStatus.SC_PARTIAL_CONTENT)
                            || entity == null){

                        return null;
                    }
                    int length = (int) entity.getContentLength();
                    InputStream is = entity.getContent();
                    FileOutputStream fos = new FileOutputStream(file, true);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    if (!readSDCard(length)) {
//							ActivityHandler.getInstance(MainLaunchActivity.this).sendEmptyMessage(103);
                        return null;
                    }
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    int tempint;
                    while (len < length) {
                        if (length - len >= 1024) {
                            tempint = is.read(buffer);
                            len += tempint;
                            fos.write(buffer, 0, tempint);
                            Thread.sleep(1);
                        } else {
                            byte[] temp = new byte[length - len];
                            tempint = is.read(temp);
                            if (tempint != -1) {
                                len += tempint;
                                fos.write(temp, 0, tempint);
                            } else {
                                len = length;
                            }
                        }
                    }

                    fos.close();
                    bis.close();
                    is.close();
                    String name = getOutputName(false);
                    file.renameTo(new File(Environment
                            .getExternalStorageDirectory(), name));

//						openFile(file);
                } catch (Exception e) {
//						ActivityHandler.getInstance(MainLaunchActivity.this).sendEmptyMessage(102);
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (isNeedOpen&&(context instanceof AboutUsActivity)){
                    ((AboutUsActivity)context).hideProgressMum();
                }
                isDownLoading = false;
                isLoadEnd = true;
                ActivityHandler.getInstance(Update.this).sendEmptyMessage(104);
                // 显示最后更新的时间

//                if (allDataList.isEmpty()) {
//                    //无数据
//                    listview.setFooterViewState(ListViewFooter.STATE_RESULT, NO_MSG, Color.GRAY);
//                } else {
//                    //无更多数据
//                    listview.setFooterViewState(ListViewFooter.STATE_RESULT, NO_MORE_MSG, Color.GRAY);
//                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
