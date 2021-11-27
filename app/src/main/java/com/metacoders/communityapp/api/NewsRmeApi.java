package com.metacoders.communityapp.api;

import com.metacoders.communityapp.models.Audio_List_Model;
import com.metacoders.communityapp.models.CommentModel;
import com.metacoders.communityapp.models.CountryPostRespose;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.Profile_Model;
import com.metacoders.communityapp.models.RegistrationResponse;
import com.metacoders.communityapp.models.ShortUrlResponse;
import com.metacoders.communityapp.models.SinglePostDetails;
import com.metacoders.communityapp.models.Video_List_Model;
import com.metacoders.communityapp.models.allDataResponse;
import com.metacoders.communityapp.models.newModels.AuthorPostResponse;
import com.metacoders.communityapp.models.newModels.CategoryResponse;
import com.metacoders.communityapp.models.newModels.PostResponse;
import com.metacoders.communityapp.models.newModels.RegistrationResp;
import com.metacoders.communityapp.models.newModels.SettingsModel;
import com.metacoders.communityapp.models.newModels.SignInResponse;
import com.metacoders.communityapp.models.newModels.SinglePostResponse;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NewsRmeApi {
    //Short URL API
    @FormUrlEncoded
    @POST("shorturlapi")
    Call<ShortUrlResponse> createShortUrl(
            @Field("url") String longurl
    );

    //send point
    @FormUrlEncoded
    @POST("gift-point")
    Call<LoginResponse.forgetPassResponse> sharePoint(
            @Field("account_number") String account_number,
            @Field("point_amount") String point_amount
    );

    //withdraw to bank
    @FormUrlEncoded
    @POST("withdraw-to-bank")
    Call<LoginResponse.forgetPassResponse> withdrawToBank(
            @Field("bank_type") String bank_type,
            @Field("bank_name") String bank_name,
            @Field("branch_name") String branch_name,
            @Field("account_number") String account_number,
            @Field("account_name") String account_name,
            @Field("transfer_amount") String transfer_amount
    );

    //withdraw to card
    @FormUrlEncoded
    @POST("withdraw-to-card")
    Call<LoginResponse.forgetPassResponse> withdrawToBank(
            @Field("card_type") String card_type,
            @Field("card_number") String card_number,
            @Field("card_on_name") String card_on_name,
            @Field("card_transfer_amount") String card_transfer_amount
    );


    //search country list
    @GET("country-wise-post/{code}")
    Call<CountryPostRespose> getCountry(
            @Path("code") String code,
            @Query("page") int p
    );


    @FormUrlEncoded
    @POST("user-watch-time")
    Call<LoginResponse.forgetPassResponse> givePoint(
            @Field("reporter_id") String reporter_id,
            @Field("post_id") int post_id,
            @Field("watch_time") int watch_time
    );

    // single p/ost details
    @GET("single-post/{slug}")
    Call<SinglePostResponse> getSinglePost(
            @Path("slug") String slug
    );

    // name, email, phone,
    @FormUrlEncoded
    @POST("contact-message")
    Call<LoginResponse.forgetPassResponse> createContactMessage(
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("message") String message
    );

    @FormUrlEncoded
    @POST("like-post/{id}")
    Call<LoginResponse.forgetPassResponse> likePost(
            @Path("id") Integer id,
            @Field("user_id") Integer user_id,
            @Field("post_id") Integer post_id

    );


    @FormUrlEncoded
    @POST("follow-reporter")
    Call<LoginResponse.forgetPassResponse> followAuthor(
            @Field("reporter_id") String reporter_id,
            @Field("follower_id") String follower_id
    );

    @GET("other-profile-post/{id}")
    Call<AuthorPostResponse> getAuthorPost(
            @Path("id") String id
    );

    @GET("own-post")
    Call<PostResponse.OwnPostResponse> getOwnPost(
    );

    // new api start
    @GET("common-post")
    Call<PostResponse> getCommonNewsList(
            @Query("page") int page
    );

    @GET("follower-post")
    Call<PostResponse.FollowerPostResponse> getFollowerPostList(
            @Query("page") int page
    );

    //category-country
    @GET("category-country")
    Call<SettingsModel> getCategories_Countries(
    );


    //categorical post

    @GET("category-wise-post/{category}")
    Call<CategoryResponse> getCategoricalPost(
            @Path("category") String path,
            @Query("page") int page
    );


    //post

    @FormUrlEncoded
    @POST("login")
    Call<SignInResponse> login(
            @Field("email") String userName,
            @Field("password") String password
    );

    // lang, category, country, title, image, description

    @FormUrlEncoded
    @POST("update-article/{post_id}")
    Call<JSONObject> updateArticle(@Path("post_id") String id,
                                   @Field("lang") String lang,
                                   @Field("category") String category,
                                   @Field("country") String country,
                                   @Field("title") String title,
                                   @Field("description") String description
    );

    //  https://newsrme.com/v1/remove-post/{id}

    @FormUrlEncoded
    @POST("remove-post/{post_id}")
    Call<JSONObject> ChangeStatus(
            @Path("post_id") String id,
            @Field("publication_status") String publication_status
    );

    @FormUrlEncoded
    @POST("update-audio/{post_id}")
    Call<JSONObject> updateAudioPost(@Path("post_id") String id,
                                     @Field("lang") String lang,
                                     @Field("category") String category,
                                     @Field("country") String country,
                                     @Field("title") String title,
                                     @Field("description") String description
    );

    @Multipart
    @POST("store-article")
    Call<LoginResponse.forgetPassResponse> uploadPost(@Part("title") RequestBody title,
                                                      @Part("description") RequestBody content,
                                                      @Part("lang") RequestBody lang_id,
                                                      @Part("country") RequestBody county,
                                                      @Part("category") RequestBody category,
                                                      @Part MultipartBody.Part image);
    // new api ends

    //lang, category, country, title, thumb_image, video, description
    @FormUrlEncoded
    @POST("update-video/{post_id}")
    Call<JSONObject> updateVideo(
            @Path("post_id") String id,
            @Field("lang") String lang,
            @Field("category") String category,
            @Field("country") String country,
            @Field("title") String title,
            @Field("description") String description
    );


    @FormUrlEncoded
    @POST("register")
    Call<RegistrationResp> registration(
            @Field("name") String name,
            @Field("email") String email,
            @Field("gender") String gender,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation,
            @Field("country") String country

    );


    @FormUrlEncoded
    @POST("invite-friend")
    Call<JSONObject> invite_friend(
            @Field("referral_user_id") String referral_user_id,
            @Field("invited_user_id") String invited_user_id,
            @Field("mac") String mac_address
    );

    @FormUrlEncoded
    @POST("forgot-password")
    Call<JSONObject> forget_password(
            @Field("email") String email
    );


    @FormUrlEncoded
    @POST("reset-password")
    Call<LoginResponse.forgetPassResponse> changePassword(
            @Field("password_confirmation") String old_password,
            @Field("password") String new_password
    );

    @GET("search")
    Call<PostResponse.SerachResult> getSearchResult(
            @Query("page") int page,
            @Query("search") String search
    );


    @GET("common/get-all-data-list")
    Call<allDataResponse> getCategoryList();

    @GET("common/get-news-audio-lists")
    Call<Audio_List_Model> getAudioList();

    @GET("common/get-news-video-lists")
    Call<Video_List_Model> getVideoList();

    @GET("profile/get-profile-info")
    Call<Profile_Model.Profile_Response> getProfileInfo();


    @GET("common/get-news-details/{id}")
    Call<SinglePostDetails> getPostDetails(@Path("id") String id);

    // get comment

    @GET("get-comment-with-reply/{slug}")
    Call<CommentModel> getCommentsList(@Path("slug") String slug);

    @Multipart
    @POST("update-user-profile/{user_id}")
    Call<LoginResponse.forgetPassResponse> uploadImage(
            @Path("user_id") String user_id,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part image);

    // @FormUrlEncoded
    @Multipart
    @POST("store-audio")
    //("video\"; filename=\"myfile.mp4\" ")  ("image\"; filename=\"myfifle.image\" ")
    Call<LoginResponse.forgetPassResponse> uploadAudioFilePost(@Part MultipartBody.Part audio,
                                                               @Part("title") RequestBody title,
                                                               @Part("description") RequestBody content,
                                                               @Part("lang") RequestBody lang_id,
                                                               @Part("category") RequestBody category_id,
                                                               @Part("country") RequestBody sub_category_id,
                                                               @Part MultipartBody.Part image);


    @Multipart
    @POST("store-video")
        // lang, category, country, title, thumb_image, video, description
    Call<LoginResponse.forgetPassResponse> uploadVideoFilePost(@Part MultipartBody.Part file,
                                                               @Part("title") RequestBody title,
                                                               @Part("description") RequestBody description,
                                                               @Part("lang") RequestBody lang_id,
                                                               @Part("category") RequestBody category_id,
                                                               @Part("country") RequestBody sub_category_id,
                                                               @Part MultipartBody.Part image);
    // socila login

    @FormUrlEncoded
    @POST("auth/social-login")
    Call<LoginResponse> socialReg(
            @Field("user_name") String userName,
            @Field("email") String email,
            @Field("name") String name,
            @Field("google_id") String google_id,  //facebook_id
            @Field("facebook_id") String facebook_id,
            @Field("type") String type
    );

    @Multipart
    @POST("profile/store-document")
    Call<RegistrationResponse> sendDOc(
            @Part("image\"; filename=\"myfile.jpg\" ") RequestBody file
    );
/*
                        full_name,
                        phone,
                        bio ,
                        company,
                        address
 */

    @FormUrlEncoded
    @POST("update-user-profile/{user_id}")
    Call<RegistrationResponse> update_profile(
            @Path("user_id") String user_id,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("bio") String bio,
            @Field("company") String company,
            @Field("address") String address,
            @Field("gender") String gender,
            @Field("website") String social_link,
            @Field("zip_code") String zip_code
    );

    // create a comment via
    @FormUrlEncoded
    @POST("comment")
    Call<LoginResponse.forgetPassResponse> post_comments(
            @Field("post_id") String post_id,
            @Field("user_id") String user_id,
            @Field("comment") String comment
    );

    @FormUrlEncoded
    @POST("reply")
    Call<LoginResponse.forgetPassResponse> post_reply(
            @Field("post_id") String post_id,
            @Field("user_id") String user_id,
            @Field("comment_id") String comment_id,
            @Field("reply") String comment
    );
}

