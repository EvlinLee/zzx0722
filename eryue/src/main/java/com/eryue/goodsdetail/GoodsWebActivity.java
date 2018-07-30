package com.eryue.goodsdetail;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.eryue.AccountUtil;
import com.eryue.R;

import java.net.URLEncoder;

import base.BaseActivity;

/**
 * Created by enjoy on 2018/3/7.
 */

public class GoodsWebActivity extends BaseActivity {

    private WebView webView;

    private ImageView iv_back;

    private String baseIP = AccountUtil.getBaseIp();

    private RelativeLayout layout_title;

    private String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsweb);
        hideNavigationBar(true);
//        navigationBar.setTitle("粉丝福利购");
        initView();
        initData();
    }

    private void initView() {
        layout_title = findViewById(R.id.layout_title);

        iv_back = (ImageView) findViewById(R.id.iv_back);

        webView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        webSettings.setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webSettings.setSupportZoom(true);//是否可以缩放，默认true
        webSettings.setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        webSettings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        webSettings.setAppCacheEnabled(true);//是否使用缓存
        webSettings.setDomStorageEnabled(true);//DOM Storage
//        webSettings.setDatabaseEnabled(true);
//        webSettings.setBlockNetworkImage(false);
//        webSettings.setBlockNetworkLoads(false);
//        webSettings.setAllowFileAccess(true);
//        webSettings.setAppCacheEnabled(true);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setDefaultTextEncodingName("utf-8");

//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setAppCacheEnabled(true);
//        //设置 缓存模式
//        // 开启 DOM storage API 功能
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setDatabaseEnabled(true);
//        webSettings.setBlockNetworkImage(false);
//        webSettings.setBlockNetworkLoads(false);
//        webSettings.setAllowFileAccess(true);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

//        webView.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
////                String pageTitle = view.getTitle();
////                if (null!=navigationBar&& !TextUtils.isEmpty(pageTitle)){
////                    navigationBar.setTitle(pageTitle);
////                }
//            }
//
//        });

        //如果不设置WebViewClient，请求会跳转系统浏览器
//        webView.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
//                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
//                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
//
//                if (url.toString().contains("taobao")){
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                }else{
//                    view.loadUrl(url);
//                    return true;
//                }
//
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
//            {
//                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
//                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//                    if (null!=request&&null!=request.getUrl()&&!TextUtils.isEmpty(request.getUrl().toString())){
//                        String url = request.getUrl().toString();
//                        if (url.toString().contains("taobao")){
//                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                            startActivity(intent);
//                            return true;
//                        }else{
//                            view.loadUrl(url);
//                            return true;
//                        }
//                    }
//
//                }
//
//                return false;
//            }
//
//        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsWebActivity.this.finish();
            }
        });
    }


    private void initData() {
        boolean hideTitle = getIntent().getBooleanExtra("hideTitle", false);
        layout_title.setVisibility(hideTitle ? View.GONE : View.VISIBLE);

        String url = getIntent().getStringExtra("url");

        if (!TextUtils.isEmpty(url)) {
            if (!url.startsWith("http")) {
                url = baseIP + "appGoTb.do?url=" + toURLEncoded(url);
            }
            webView.loadUrl(url);
        }
//        webView.loadUrl(url);
//        String url1 = baseIP + "appGoTb.do?url=" + toURLEncoded(url);

//        webView.loadUrl("file:///android_asset/test.html");
//        webView.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//            }
//        });
//        webView.setWebChromeClient(new WebChromeClient());


        webView.setWebViewClient(new CommentWebViewClient());


//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.VIEW");
//        Uri content_url = Uri.parse(url);
//        intent.setData(content_url);
//        intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
//        startActivity(intent);
    }

    public String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
        }

        return "";
    }


    public class CommentWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            boolean openSuccess = openApp(url);
//            if (!openSuccess){
//                String webUrl = getIntent().getStringExtra("url");
//                view.loadUrl(webUrl);
//                return true;
//            }
            if (!openSuccess){
                return super.shouldOverrideUrlLoading(view,url);
            }
            return openSuccess;
        }
    }

    //判断app是否安装
    private boolean isInstall(Intent intent) {
        return this.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    //打开app
    private boolean openApp(String url) {
        if (TextUtils.isEmpty(url)) return false;
        try {
            if (!(url.startsWith("http") || url.startsWith("https") || url.startsWith("ftp"))) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }




}
