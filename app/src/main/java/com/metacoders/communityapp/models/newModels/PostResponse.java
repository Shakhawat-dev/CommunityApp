package com.metacoders.communityapp.models.newModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        public Post followersPost = new Post();

        public Post getFollowersPost() {
            return followersPost;
        }

        public void setFollowersPost(Post followersPost) {
            this.followersPost = followersPost;
        }
    }

    public class OwnPostResponse implements Serializable {
        @Expose
        @SerializedName("OwnVideos")
        public List<Post.PostModel> ownPost = new ArrayList<>();

        @Expose
        @SerializedName("OwnAudios")
        public List<Post.PostModel> ownAudio = new ArrayList<>();
        @Expose
        @SerializedName("OwnArticles")
        public List<Post.PostModel> ownArticles = new ArrayList<>();

        public List<Post.PostModel> getOwnAudio() {
            return ownAudio;
        }

        public void setOwnAudio(List<Post.PostModel> ownAudio) {
            this.ownAudio = ownAudio;
        }

        public List<Post.PostModel> getOwnArticles() {
            return ownArticles;
        }

        public void setOwnArticles(List<Post.PostModel> ownArticles) {
            this.ownArticles = ownArticles;
        }

        public List<Post.PostModel> getOwnPost() {
            return ownPost;
        }

        public void setOwnPost(List<Post.PostModel> ownPost) {
            this.ownPost = ownPost;
        }
    }

    public class SerachResult implements Serializable {
        @Expose
        @SerializedName("searchResults")
        public Post searchResults = new Post();

        public Post getSearchResults() {
            return searchResults;
        }

        public void setSearchResults(Post searchResults) {
            this.searchResults = searchResults;
        }
    }





}
