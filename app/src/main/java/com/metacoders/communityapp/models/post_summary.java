package com.metacoders.communityapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class post_summary {


    @SerializedName("video")
    @Expose
    private List<post_summary.Video> video = null;
    @SerializedName("audio")
    @Expose
    private List<post_summary.Audio> audio = null;
    @SerializedName("post")
    @Expose
    private List<post_summary.Post> post = null;

    public List<post_summary.Video> getVideo() {
        return video;
    }

    public void setVideo(List<post_summary.Video> video) {
        this.video = video;
    }

    public List<post_summary.Audio> getAudio() {
        return audio;
    }

    public void setAudio(List<post_summary.Audio> audio) {
        this.audio = audio;
    }

    public List<post_summary.Post> getPost() {
        return post;
    }

    public void setPost(List<post_summary.Post> post) {
        this.post = post;
    }

    public class Post {

        @SerializedName("ttl")
        @Expose
        private String ttl;

        public String getTtl() {
            return ttl;
        }

        public void setTtl(String ttl) {
            this.ttl = ttl;
        }

    }

    public class Video {


        @SerializedName("ttl")
        @Expose
        private String ttl;

        public String getTtl() {
            return ttl;
        }

        public void setTtl(String ttl) {
            this.ttl = ttl;
        }
    }
    public class Audio {

        @SerializedName("ttl")
        @Expose
        private String ttl;

        public String getTtl() {
            return ttl;
        }

        public void setTtl(String ttl) {
            this.ttl = ttl;
        }

    }

}


