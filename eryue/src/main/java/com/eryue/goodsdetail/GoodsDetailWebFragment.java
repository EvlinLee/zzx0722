package com.eryue.goodsdetail;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eryue.R;
import com.eryue.activity.BaseFragment;

/**
 * Created by enjoy on 2018/2/11.
 */

public class GoodsDetailWebFragment extends BaseFragment{

    private WebView webView;

    public static final String taobaoUrl = "https://hws.m.taobao.com/cache/mtop.wdetail.getItemDescx/4.1/?data={item_num_id%3A%22itemId%22}&type=jsonp&dataType=jsonp";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webView = new WebView(getContext());
        setContentView(webView);
        initData();
    }


    int lastX = 0;
    int lastY = 0;

    private void initData(){

        String itemId = getActivity().getIntent().getStringExtra("itemId");
        if (TextUtils.isEmpty(itemId)){
            return;
        }
        String url = taobaoUrl.replace("itemId",itemId);
        reloadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            //覆盖shouldOverrideUrlLoading 方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

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
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);


        /**
         * webView与ViewPager所带来的滑动冲突问题解决方法
         */
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = 0;
                int y = 0;
                int scrollY;
                int scrollX;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        webView.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x = (int) event.getRawX();
                        y = (int) event.getRawY();
                        int deltaY = y - lastY;
                        int deltaX = x - lastX;

                        scrollY = webView.getScrollY();
                        if (deltaY>0&&scrollY==0){
                            //滑到顶后，继续滑动，就滑到上一个fragment
                            webView.getParent().requestDisallowInterceptTouchEvent(false);
                        }else if (Math.abs(deltaX) > Math.abs(deltaY)){
                            webView.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        webView.getParent().requestDisallowInterceptTouchEvent(false);
                    default:
                        break;
                }
                return false;
            }
        });
//        webView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK ) {  //表示按返回键
//                        if (webView.canGoBack()){//网页可以后退
//                            webView.goBack();   //后退
//                            return true;
//                        }else {
//                            getActivity().finish();
//                        }
//                    }
//                }
//                return false;
//            }
//        });




        //声明WebSettings子类
//        WebSettings webSettings = webView.getSettings();
//        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
//        webSettings.setJavaScriptEnabled(true);
//        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
//        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
//        //设置自适应屏幕，两者合用
//        //将图片调整到适合webview的大小
//        webSettings.setUseWideViewPort(true);
//        // 缩放至屏幕的大小
//        webSettings.setLoadWithOverviewMode(true);
//        //缩放操作
//        //支持缩放，默认为true。是下面那个的前提。
//        webSettings.setSupportZoom(true);
//        //设置内置的缩放控件。若为false，则该WebView不可缩放
//        webSettings.setBuiltInZoomControls(true);
//        //隐藏原生的缩放控件
//        webSettings.setDisplayZoomControls(false);
//        //其他细节操作
//        //关闭webview中缓存
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        //设置可以访问文件
//        webSettings.setAllowFileAccess(true);
//        //支持通过JS打开新窗口
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        //支持自动加载图片
//        webSettings.setLoadsImagesAutomatically(true);
//        //设置编码格式
//        webSettings.setDefaultTextEncodingName("utf-8");
    }

    public void reloadUrl(String url){
        if (TextUtils.isEmpty(url)){
            return;
        }

        webView.loadUrl(url);

    }
}
