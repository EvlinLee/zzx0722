package com.eryue.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eryue.R;

import base.BaseActivity;

/**
 * Created by enjoy on 2018/7/14.
 */

public class ImageBigActivity extends BaseActivity {
    public static final String KEY_URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigimage);

        setTitle(getResources().getString(R.string.app_name));


        initData();
    }


    private void  initData(){
        ImageView imageView = findViewById(R.id.imageview);

       String imageUrl =  getIntent().getStringExtra(KEY_URL);
       if(!TextUtils.isEmpty(imageUrl)){
           Glide.with(this).load(imageUrl).placeholder(R.drawable.img_default_contract).into(imageView);
       }

    }



}
