package com.metacoders.communityapp.api;

import android.content.Context;
import android.util.Log;

import com.metacoders.communityapp.utils.SharedPrefManager;
import com.metacoders.communityapp.utils.StringGen;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class tokenInterceptor2 implements Interceptor {

    private String accessToken;
    private SharedPrefManager sharedPrefManager;
    private Context mtx  ;
    String token = ""   ;

    public tokenInterceptor2(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {



        //TODO: Grave this token from SharedprefManager -> RAHAT

       // accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1OTYxNDQyMDcsImV4cCI6MTYwMTUwMTAwNywianRpIjoidmoxSnhQMFZsOFlZeVhyRTJXRWxEIiwidXNlciI6eyJuYW1lIjoic2hhYWtoYXdhdCIsImlkIjoiNCJ9fQ.vOfr6sByA1CpxvnxmGMeIovCRjZbH7F2AXjovp5EBdM";
        //  String   accessTokens = sharedPrefManager.getUserToken();
        Log.d("TAG", "intercept: " + StringGen.token);
        Request newRequest = chain.request().newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();

        return chain.proceed(newRequest);
    }
}
