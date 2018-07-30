package com.eryue.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eryue.R;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by bli.Jason on 2018/2/11.
 */

public class VideoActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout layout_close;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_video);
        initData();
    }

    private void initData() {
        layout_close = (LinearLayout)findViewById(R.id.layout_close);
        JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.playervideo_list);

        String url = getIntent().getStringExtra("url");
        jzVideoPlayerStandard.setUp(url
                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
//        jzVideoPlayerStandard.thumbImageView.setImageURI(Uri.parse("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640"));
        jzVideoPlayerStandard.startButton.performClick();


        layout_close.setOnClickListener(this);

    }


    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    public void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }
}
