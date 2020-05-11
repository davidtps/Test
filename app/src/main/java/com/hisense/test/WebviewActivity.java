package com.hisense.test;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebviewActivity extends AppCompatActivity {
    private WebView mWebView;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mWebView = findViewById(R.id.webview);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");


        initData();


    }

    public void initData() {
//        Bundle bundle = getArguments();
//        String url = null;
//        boolean need_extra_parameter = true;//默认需要追加验签参数
//        if (bundle != null) {
//            url = bundle.getString(URL);
//            need_extra_parameter = bundle.getBoolean(NEED_EXTRA_PARAMETER, true);
//        }

        //获取WebView设置
        webSettings = mWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
//        webSettings.setUserAgentString(webSettings.getUserAgentString() + "" +
//                " android/wsmall/" + CommUtils.getVersionNameFromSys(_mActivity));
        //支持javascript
        webSettings.setJavaScriptEnabled(true);
//        // 设置可以支持缩放
//        webSettings.setSupportZoom(true);
//        // 设置出现缩放工具
//        webSettings.setBuiltInZoomControls(true);
        //扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        //禁用file
        webSettings.setAllowFileAccess(false);
        webSettings.setAllowFileAccessFromFileURLs(false);
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        //不适用缓存
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //在Android5.0 中WebView默认不允许加载Http、Https混合内容，解决办法如下：
        //MIXED_CONTENT_ALWAYS_ALLOW：允许从任何来源加载内容，即使起源是不安全的；
        //MIXED_CONTENT_NEVER_ALLOW：不允许Https加载Http的内容，即不允许从安全的起源去加载一个不安全的资源；
        //MIXED_CONTENT_COMPATIBILITY_MODE：当涉及到混合式内容时，WebView 会尝试去兼容最新Web浏览器的风格。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /**自适应屏幕
         * 用WebView显示图片，可使用这个参数 设置网页布局类型：
         * 1、LayoutAlgorithm.NARROW_COLUMNS ：适应内容大小
         * 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        //设置js调用的接口，命名在demo下，如果为this，可以调用 所有的public 方法
        //示例：onclick ="window.ws._ws_pay()"
//        mWebView.addJavascriptInterface(new WebviewFragment.InJavaScript(), "wssdkHook");
        //打开地址
//        if (StringUtil.isEmpty(url)) {
//            return;
//        }
//        String timesp = CommUtils.getTimeStemp();
        mWebView.loadUrl("file:///android_asset/index.html");
//        mWebView.loadUrl(url);
    }


}
