package com.eryue.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.eryue.BaseApplication;
import com.eryue.model.GoodsSearchModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bli.Jason on 2018/2/26.
 */

public class SharedPreferencesUtil {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static String KEY_SHARED = "eryue";

    private static SharedPreferencesUtil instatnce;

    public static SharedPreferencesUtil getInstance() {
        if (instatnce == null) {  //第一次判断是否为null

            synchronized (SharedPreferencesUtil.class) {  //在代码块中加同步锁 如果已经有线程访问 当前线程转为阻塞状态

                if (instatnce == null) { //当第二个线程访问时 已经不为null了 那么不再创建对象

                    instatnce = new SharedPreferencesUtil(BaseApplication.getInstance().getApplicationContext(), KEY_SHARED);

                }

            }

        }
        return instatnce;
    }

    private SharedPreferencesUtil(Context mContext, String preferenceName) {
        preferences = mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void clear(String tag){
        editor.putString(tag, "");
        editor.commit();
    }
    /**
     * 保存List
     *
     * @param tag
     * @param datalist
     */
    public <T> void setDataList(String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0){
            return;
        }

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.putString(tag, strJson);
        editor.commit();
    }

    /**
     * 获取List
     *
     * @param tag
     * @return
     */
    public <T> List<T> getDataList(String tag) {
        List<T> datalist = new ArrayList<T>();
        String strJson = preferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<GoodsSearchModel>>() {
        }.getType());
        return datalist;

    }

    /**
     * 保存字符串到数据库
     * @param tag
     * @param content
     */
    public void saveString(String tag,String content){
        if (TextUtils.isEmpty(tag)){
            return;
        }
        editor.putString(tag, content);
        editor.commit();
    }

    /**
     * 获取字符串
     * @param tag
     * @return
     */
    public String getString(String tag){
        try {
            if (TextUtils.isEmpty(tag)){
                return "";
            }
            return  preferences.getString(tag, "");
        }catch (Exception e){
        }
        return "";
    }

    public boolean getBoolean(String key, boolean defValue) {
        try {
            return preferences.getBoolean(key, defValue);
        }catch (Exception e){
        }
        return false;
    }

    public void saveBoolean(String key, boolean value){
        if (null!=editor){
            editor.putBoolean(key,value);
            editor.commit();
        }

    }

    public int getIntValue(String key,int defaultValue) {
        try {
            return preferences.getInt(key, defaultValue);
        }catch (Exception e){
        }
        return defaultValue;
    }

    public void saveIntValue(String key,int intValue){
        if (null!=editor){
            editor.putInt(key,intValue).apply();
            editor.commit();
        }

    }


}
