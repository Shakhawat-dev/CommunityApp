package com.metacoders.communityapp.models.newModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PostResponse implements Serializable {
    @Expose
    @SerializedName("commonPosts")
    public Post commonPosts;

    public Post getCommonPosts() {
        return commonPosts;
    }

    public void setCommonPosts(Post commonPosts) {
        this.commonPosts = commonPosts;
    }

    public class FollowerPostResponse implements Serializable {
        @Expose
        @SerializedName("followersPost")
        public Post followersPost = new Post() ;

        public Post getFollowersPost() {
            return followersPost;
        }

        public void setFollowersPost(Post followersPost) {
            this.followersPost = followersPost;
        }
    }
}
