package com.metacoders.communityapp.models.newModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SinglePostResponse {
    @Expose
    @SerializedName("post")
    public Post.PostModel data ;

    @Expose
    @SerializedName("relatedPost")
    public List<Post.PostModel> relatedPosts = new ArrayList<>();

    @Expose
    @SerializedName("postLikesCount")
    public String postLikesCount ;
    @Expose
    @SerializedName("postLikesCheck")
    public JSONObject postLikesCheck = null ;
    @Expose
    @SerializedName("followerCount")
    public String followerCount ;
    @Expose
    @SerializedName("followerCheck")
    public JSONObject followerCheck ;


    public Post.PostModel getData() {
        return data;
    }

    public void setData(Post.PostModel data) {
        this.data = data;
    }

    public List<Post.PostModel> getRelatedPosts() {
        return relatedPosts;
    }

    public void setRelatedPosts(List<Post.PostModel> relatedPosts) {
        this.relatedPosts = relatedPosts;
    }

    public String getPostLikesCount() {
        return postLikesCount;
    }

    public void setPostLikesCount(String postLikesCount) {
        this.postLikesCount = postLikesCount;
    }

    public JSONObject getPostLikesCheck() {
        return postLikesCheck;
    }

    public void setPostLikesCheck(JSONObject postLikesCheck) {
        this.postLikesCheck = postLikesCheck;
    }

    public String getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(String followerCount) {
        this.followerCount = followerCount;
    }

    public JSONObject getFollowerCheck() {
        return followerCheck;
    }

    public void setFollowerCheck(JSONObject followerCheck) {
        this.followerCheck = followerCheck;
    }
}
