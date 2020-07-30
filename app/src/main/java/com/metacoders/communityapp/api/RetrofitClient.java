package com.metacoders.communityapp.api;

import com.metacoders.communityapp.utils.Utils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    TokenInterceptor tokenInterceptor = new TokenInterceptor();

    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .build();

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Utils.ROOT_URL)
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
