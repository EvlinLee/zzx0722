package com.eryue.mine;

import android.os.Bundle;
import android.webkit.WebView;

import com.eryue.AccountUtil;
import com.eryue.R;
import com.library.util.StringUtils;

import base.BaseActivity;


public class MyLiveActivity extends BaseActivity {
    // 域名/live.do?userId=UID


    private WebView webView;
    private String url;

    private String inviteCode = AccountUtil.getInviteCode();
    private String serverURL = AccountUtil.getServerURL();
    private String baseIP = AccountUtil.getBaseIp();
    private long uid = StringUtils.valueOfLong(AccountUtil.getUID());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationBar.setTitle("我的直播");

        setContentView(R.layout.activity_my_live);
        url = serverURL + "live.do?userId=" + uid;

        initViews();
    }

    private void initViews() {
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl(url);
    }
}
