package com.eryue.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.eryue.GoodsContants;
import com.eryue.R;
import com.eryue.util.BaseNetHandler;
import com.eryue.util.SharedPreferencesUtil;
import com.library.util.FileUtil;
import com.library.util.ToastTools;

import java.io.File;

import base.BaseActivity;

/**
 * Created by enjoy on 2018/5/8.
 */

public class HideSettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener{

    private CheckBox cb_log;
    private CheckBox cb_pdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationBar.setTitle("隐藏设置");
        setContentView(R.layout.activity_hidesetting);
        initView();
    }

    private void initView(){
        cb_log = findViewById(R.id.cb_log);
        cb_log.setOnCheckedChangeListener(this);

        boolean isChecked = SharedPreferencesUtil.getInstance().getBoolean(
                BaseNetHandler.SWITCH_LOGO, false);
        cb_log.setChecked(isChecked);


        cb_pdd = findViewById(R.id.cb_pdd);
        cb_pdd.setOnCheckedChangeListener(this);

        boolean isPddChecked = SharedPreferencesUtil.getInstance().getBoolean(
                GoodsContants.SWITCH_PDD, false);
        cb_pdd.setChecked(isPddChecked);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        if (null!=cb_log&&cb_log == compoundButton){
            BaseNetHandler.DEBUG = isChecked;
            SharedPreferencesUtil.getInstance().saveBoolean(
                    BaseNetHandler.SWITCH_LOGO, isChecked);
            if (isChecked) {
                ToastTools.showShort(this,"显示日志");
            } else {
                ToastTools.showShort(this,"隐藏日志");
            }

            if(!isChecked){
                //删除日志文件
                deleteFilePath(FileUtil.getExternalCacheDirectory(this,null));

            }
        }else if (null!=cb_pdd&&cb_pdd == compoundButton){
            SharedPreferencesUtil.getInstance().saveBoolean(
                    GoodsContants.SWITCH_PDD, isChecked);
            if (isChecked) {
                ToastTools.showShort(this,"mobile.yangkeduo.com");
            } else {
                ToastTools.showShort(this,"com.xunmeng.pinduoduo");
            }

        }

    }

    private void deleteFilePath(File file){
        if (null!=file){
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for (int i=0;i<files.length;i++){

                    if (null!=files[i]){
                        deleteFilePath(files[i]);
                    }
                }
            }else{
                file.delete();
            }
        }
    }
}
