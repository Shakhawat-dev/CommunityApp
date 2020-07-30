package com.metacoders.communityapp.api;

import com.metacoders.communityapp.models.LoginResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;

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



//    @GET("checkListType")
//    Call<ChecklistTypeResponse> getCheckListTypes(
//            @Query("lang") String language
//    );


}
