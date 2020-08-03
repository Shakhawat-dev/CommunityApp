package com.metacoders.communityapp.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    private String accessToken;


    @Override
    public Response intercept(Chain chain) throws IOException {

        accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1OTYxNDQyMDcsImV4cCI6MTYwMTUwMTAwNywianRpIjoidmoxSnhQMFZsOFlZeVhyRTJXRWxEIiwidXNlciI6eyJuYW1lIjoic2hhYWtoYXdhdCIsImlkIjoiNCJ9fQ.vOfr6sByA1CpxvnxmGMeIovCRjZbH7F2AXjovp5EBdM";

        Request newRequest = chain.request().newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();

        return chain.proceed(newRequest);
    }
}
