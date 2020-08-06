package com.metacoders.communityapp.api;

import com.google.gson.JsonObject;
import com.metacoders.communityapp.models.Audio_List_Model;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.News_List_Model;
import com.metacoders.communityapp.models.Profile_Model;
import com.metacoders.communityapp.models.RegistrationResponse;
import com.metacoders.communityapp.models.Video_List_Model;

import org.json.JSONObject;

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
    @POST("registration/user-registration")
    Call<RegistrationResponse> registration (
            @Field("name") String name,
            @Field("username") String username,
            @Field("email") String email,
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

    @GET("common/get-news-audio-lists")
    Call<Audio_List_Model> getAudioList();

    @GET("common/get-news-video-lists")
    Call<Video_List_Model> getVideoList();

    @GET("profile/get-profile-info")
    Call<Profile_Model.Profile_Response> getProfileInfo();

}
