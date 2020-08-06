package com.metacoders.communityapp.api;

import android.content.Context;

import com.metacoders.communityapp.utils.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient mInstance;
    private Retrofit retrofit;
    private  Context context ;

    TokenInterceptor tokenInterceptor = new TokenInterceptor();

    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .build();

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public NewsRmeApi getApi() {
        return retrofit.create(NewsRmeApi.class);
    }
}
