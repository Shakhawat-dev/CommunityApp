package com.metacoders.communityapp.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metacoders.communityapp.R;

import im.delight.android.webview.AdvancedWebView;

public class ShopFragment extends Fragment  {


    public ShopFragment() {
        // Required empty public constructor
    }
    View view ;
    AdvancedWebView mWebView ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view =  inflater.inflate(R.layout.fragment_shop, container, false);
        mWebView = (AdvancedWebView) view.findViewById(R.id.webview);

        mWebView.setListener(getActivity(), new AdvancedWebView.Listener() {
            @Override
            public void onPageStarted(String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(String url) {

            }

            @Override
            public void onPageError(int errorCode, String description, String failingUrl) {

            }

            @Override
            public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

            }

            @Override
            public void onExternalPageRequest(String url) {

            }
        });
        mWebView.setMixedContentAllowed(false);
        mWebView.setCookiesEnabled(true);
        mWebView.loadUrl("https://shoprme.com/");


        return view ;
    }




}