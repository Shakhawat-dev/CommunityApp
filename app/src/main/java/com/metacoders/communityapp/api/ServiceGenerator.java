package com.metacoders.communityapp.api;

import android.text.TextUtils;
import android.util.Log;

import com.metacoders.communityapp.utils.Constants;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;
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

        if (!authToken.equals("00")) {
            tokenInterceptor2 interceptor =
                    new tokenInterceptor2(authToken);
           //HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
          //  logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            if (!httpClient.interceptors().contains(interceptor)) {
               // Log.d("okhttp", "createService: " + authToken);

                httpClient.addInterceptor(interceptor);
                httpClient
                        .connectTimeout(5, TimeUnit.MINUTES)
                        .writeTimeout(5, TimeUnit.MINUTES)
                        .readTimeout(5 , TimeUnit.MINUTES)
                        //.protocols( Collections.singletonList(Protocol.HTTP_1_1));
                        ;

                
             //   .addInterceptor(logging);


                retrofit =
                        new Retrofit.Builder()
                                .baseUrl(API_BASE_URL)
                                .client(httpClient.build())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

//                Log.d("TAG", "createService: ME OffasdfN  "  );
            }
        } else {
          //  Log.d("TAG", "createService: ME ON  ");
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);



            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(500, TimeUnit.SECONDS)
                    .writeTimeout(500, TimeUnit.SECONDS)
                    .readTimeout(500 , TimeUnit.SECONDS)
                 //   .addInterceptor(logging)
                    .build();
            retrofit =
                    new Retrofit.Builder()
                            .baseUrl(API_BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client)

                            .build();
        }

        return retrofit.create(serviceClass);
    }
}