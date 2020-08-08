package com.metacoders.communityapp.api;

import android.text.TextUtils;

import com.metacoders.communityapp.utils.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    public static final String API_BASE_URL = Constants.ROOT_URL;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit retrofit;


    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            tokenInterceptor2 interceptor =
                    new tokenInterceptor2(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);
                retrofit =
                        new Retrofit.Builder()
                                .baseUrl(API_BASE_URL)
                                .client(httpClient.build())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();


            }
        } else if ( authToken.equals("00")){
            retrofit =
                    new Retrofit.Builder()
                            .baseUrl(API_BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
        }

        return retrofit.create(serviceClass);
    }
}