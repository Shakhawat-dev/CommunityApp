package com.metacoders.communityapp.api;

import com.metacoders.communityapp.models.Audio_List_Model;
import com.metacoders.communityapp.models.CommentModel;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.News_List_Model;
import com.metacoders.communityapp.models.OwnListModel;
import com.metacoders.communityapp.models.Post_Model;
import com.metacoders.communityapp.models.Profile_Model;
import com.metacoders.communityapp.models.RegistrationResponse;
import com.metacoders.communityapp.models.SinglePostDetails;
import com.metacoders.communityapp.models.Video_List_Model;
import com.metacoders.communityapp.models.allDataResponse;
import com.metacoders.communityapp.models.newModels.CategoryResponse;
import com.metacoders.communityapp.models.newModels.PostResponse;
import com.metacoders.communityapp.models.newModels.SettingsModel;
import com.metacoders.communityapp.models.newModels.SignInResponse;
import com.metacoders.communityapp.models.post_summary;

import org.json.JSONObject;

import java.util.List;

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
            @Path("category") String path
    );


    //post

    @FormUrlEncoded
    @POST("login")
    Call<SignInResponse> login(
            @Field("email") String userName,
            @Field("password") String password
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


    @FormUrlEncoded
    @POST("registration/user-registration")
    Call<RegistrationResponse> registration(
            @Field("name") String name,
            @Field("user_name") String username,
            @Field("email") String email,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("forgot-password")
    Call<JSONObject> forget_password(
            @Field("email") String email
    );


    @FormUrlEncoded
    @POST("profile/change-password")
    Call<LoginResponse.forgetPassResponse> changePassword(
            @Field("old_password") String old_password,
            @Field("new_password") String new_password
    );

    @FormUrlEncoded
    @POST("common/post-search-result")
    Call<List<Post_Model>> getSearchResult(
            @Field("search") String search,
            @Field("category_id") String category_id,
            @Field("subcategory_id") String subcategory_id,
            @Field("lang_id") String lang_id
    );


    @GET("dashboard/get-own-news-summary")
    Call<post_summary> get_post_summary();

    @GET("dashboard/get-own-news-list")
    Call<OwnListModel> get_post_list();

    @GET("common/get-news-lists")
    Call<News_List_Model> getNewsList();

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

    @GET("dashboard/get-comments/{id}")
    Call<CommentModel> getCommentsList(@Path("id") String id);

    @Multipart
    @POST("profile/store-photo")
    Call<UploadResult> uploadImage(@Part("image\"; filename=\"myfile.jpg\" ") RequestBody file);

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


    @FormUrlEncoded
    @POST("profile/update-profile-info")
    Call<RegistrationResponse> update_profile(
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("profession") String profession,
            @Field("last_degree") String last_degree,
            @Field("city") String city,
            @Field("country") String country,
            @Field("address") String address

    );

    // create a comment via
    @FormUrlEncoded
    @POST("dashboard/post-comments")
    Call<RegistrationResponse> post_comments(
            @Field("post_id") String post_id,
            @Field("comment") String comment,
            @Field("email") String email,
            @Field("name") String name,
            @Field("user_id") String user_id,
            @Field("parent_id") String parent_id,
            @Field("like_count") String like_count,
            @Field("ip_address") String ip_address

    );
}
