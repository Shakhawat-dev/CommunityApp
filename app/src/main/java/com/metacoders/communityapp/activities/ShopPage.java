package com.metacoders.communityapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.metacoders.communityapp.R;

import im.delight.android.webview.AdvancedWebView;

public class ShopPage extends AppCompatActivity  implements AdvancedWebView.Listener  {

    AdvancedWebView mWebView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_page);
        getSupportActionBar().hide();
        mWebView = (AdvancedWebView) findViewById(R.id.webview);
        mWebView.setListener(this, this);
        mWebView.setMixedContentAllowed(false);
        mWebView.setCookiesEnabled(true);
        mWebView.loadUrl("https://shoprme.com/");

        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (!mWebView.onBackPressed()) { return; }
        // ...
        mWebView.goBack();
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) { }


    @Override
    public void onPageFinished(String url) { }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) { }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) { }}