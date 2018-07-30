package com.eryue.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.eryue.ActivityHandler;
import com.eryue.R;

import net.InterfaceManager;

import base.BaseActivity;

/**
 * Created by enjoy on 2018/5/26.
 */

public class GoodsNoticeDetailActivity extends BaseActivity implements NoticePresenter.SearchInfoListener {


    /**
     * 公告详情
     */
    private TextView tv_notice;

    private NoticePresenter noticePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticedetail);
        setTitle("公告");
        tv_notice = findViewById(R.id.tv_notice);

        initData();
    }


    private void initData(){
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String databaseID = intent.getStringExtra("databaseID");

        if (!TextUtils.isEmpty(title)){
            navigationBar.getTitleView().setMaxLines(1);
            setTitle(title);
        }


        if (!TextUtils.isEmpty(databaseID)){
            noticePresenter = new NoticePresenter();
            noticePresenter.setSearchInfoListener(this);
            noticePresenter.requestSearchInfo(databaseID);
        }
    }

    @Override
    public void onSearchInfoBack(final InterfaceManager.SearchDetailInfo searchDetailInfo) {

        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {

                if (null!=searchDetailInfo&& !TextUtils.isEmpty(searchDetailInfo.info)){

                    if (null!=tv_notice){
                        tv_notice.setText(searchDetailInfo.info);
                    }
                }
            }
        }).sendEmptyMessage(0);

    }

    @Override
    public void onSearchInfoError() {

    }
}
