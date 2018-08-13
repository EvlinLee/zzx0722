package com.eryue.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.eryue.R;

import java.util.List;

import base.BaseActivity;

/**
 * 新手教程
 */

public class NewUserActivity extends BaseActivity implements View.OnClickListener{


    int[] ids = {R.id.hint11, R.id.hint12, R.id.hint13, R.id.hint14, R.id.hint15};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        navigationBar.setTitle("使用帮助");


        for (int i = 0; i < ids.length; i++) {
            findViewById(ids[i]).setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();

        Intent intent = new Intent(this, NewUserDetailActivity.class);
        intent.putExtra("tag", tag);
        startActivity(intent);
    }
}
