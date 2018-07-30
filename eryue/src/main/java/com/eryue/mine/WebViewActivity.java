package com.eryue.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eryue.AccountUtil;
import com.eryue.R;
import com.library.util.StringUtils;

import base.BaseActivity;

/**
 * Created by dazhou on 2018/5/5.
 *
 */

public class WebViewActivity extends BaseActivity {

    private WebView webView;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_agency_learn);
        int type = getIntent().getIntExtra("type", -1);
        if (type == 1) {
            navigationBar.setTitle("代理学习");
        } else if (type == 2) {
            navigationBar.setTitle("消息中心");
        } else {
            return;
        }

        url = (String) getIntent().getStringExtra("url");

        initViews();

        if (TextUtils.isEmpty(url)) {
            return;
        }

        webView.loadUrl(url);


        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;
//                return super.shouldOverrideUrlLoading(view, request);
            }
        });

    }

    private void initViews() {
        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        //设置 缓存模式
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.resumeTimers();
        webView.onResume();
    }


    @Override
    protected void onDestroy() {
        webView.destroy();
        webView = null;
        super.onDestroy();
    }
}
