package com.metacoders.communityapp.api;

import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.News_List_Model;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NewsRmeApi {

    @FormUrlEncoded
    @POST("auth/user-login")
    Call<LoginResponse> login (
            @Field("user_name") String userName,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("profile/change-password")
    Call<ResponseBody> changePassword(
            @Field("old_password") String old_password,
            @Field("new_password") String new_password
    );



    @GET("common/get-news-lists")
    Call<News_List_Model> getNewsList();


}
