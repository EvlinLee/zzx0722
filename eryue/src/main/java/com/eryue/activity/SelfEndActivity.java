package com.eryue.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eryue.R;
import com.eryue.util.SharedPreferencesUtil;
import com.library.util.ToastTools;

import base.BaseActivity;

/**
 * Created by enjoy on 2018/3/3.
 */

public class SelfEndActivity extends BaseActivity implements View.OnClickListener{

    /**
     * 自定义小尾巴内容
     */
    private EditText et_selfcontent;

    /**
     * 清空
     */
    private TextView tv_clear;

    /**
     * 保存
     */
    private TextView tv_save;

    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selftend);
        hideNavigationBar(true);

        initView();
        initData();
    }

    private void initView(){
        iv_back = (ImageView) findViewById(R.id.iv_back);

        et_selfcontent = (EditText) findViewById(R.id.et_selfcontent);
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        tv_save = (TextView) findViewById(R.id.tv_save);

        tv_clear.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        iv_back.setOnClickListener(this);

    }

    private void initData(){
        String content = SharedPreferencesUtil.getInstance().getString(key_selfend);
        et_selfcontent.setText(content);

    }

    public static final String key_selfend = "key_selfend";
    @Override
    public void onClick(View v) {
        if (v == tv_clear){
            if (null!=et_selfcontent){
                et_selfcontent.setText("");
            }
        }else if (v == tv_save){
            if (null!=et_selfcontent){
                String selfConent = et_selfcontent.getText().toString();
                SharedPreferencesUtil.getInstance().saveString(key_selfend, selfConent);

                ToastTools.showShort(this,"保存成功");
            }
        }else if (v == iv_back){
            this.finish();
        }
    }
}
