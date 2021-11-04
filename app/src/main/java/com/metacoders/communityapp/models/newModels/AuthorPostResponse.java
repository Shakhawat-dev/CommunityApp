package com.metacoders.communityapp.models.newModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AuthorPostResponse implements Serializable {
    @Expose
    @SerializedName("OwnVideos")
    public List<Post.PostModel> ownVideos;

    @Expose
    @SerializedName("OwnAudios")
    public List<Post.PostModel> ownAudios;

    @Expose
    @SerializedName("OwnArticles")
    public List<Post.PostModel> ownArticles;

    @Expose
    @SerializedName("totalPostCount")
    public int totalPostCount;

    @Expose
    @SerializedName("otherProfileFollowersCount")
    public int otherProfileFollowersCount;

    @Expose
    @SerializedName("otherUserInfo")
    public UserModel author;

    public int getTotalPostCount() {
        return totalPostCount;
    }

    public void setTotalPostCount(int totalPostCount) {
        this.totalPostCount = totalPostCount;
    }

    public int getOtherProfileFollowersCount() {
        return otherProfileFollowersCount;
    }

    public void setOtherProfileFollowersCount(int otherProfileFollowersCount) {
        this.otherProfileFollowersCount = otherProfileFollowersCount;
    }

    public List<Post.PostModel> getOwnVideos() {
        return ownVideos;
    }

    public void setOwnVideos(List<Post.PostModel> ownVideos) {
        this.ownVideos = ownVideos;
    }

    public List<Post.PostModel> getOwnAudios() {
        return ownAudios;
    }

    public void setOwnAudios(List<Post.PostModel> ownAudios) {
        this.ownAudios = ownAudios;
    }

    public List<Post.PostModel> getOwnArticles() {
        return ownArticles;
    }

    public void setOwnArticles(List<Post.PostModel> ownArticles) {
        this.ownArticles = ownArticles;
    }

    public UserModel getAuthor() {
        return author;
    }

    public void setAuthor(UserModel author) {
        this.author = author;
    }
}
