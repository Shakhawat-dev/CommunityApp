package com.metacoders.communityapp.models.newModels;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public  class RelatedPostResponse {

    @Expose
    @SerializedName("current_page")
    private int current_page;
    @Expose
    @SerializedName("last_page")
    private int last_page;
    @Expose
    @SerializedName("from")
    private int from;
    @Expose
    @SerializedName("data")
    public List<Post.PostModel> relatedPosts = new ArrayList<>();

    public List<Post.PostModel> getRelatedPosts() {
        return relatedPosts;
    }

    public void setRelatedPosts(List<Post.PostModel> relatedPosts) {
        this.relatedPosts = relatedPosts;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    @NonNull
    @Override
    public String toString() {
        return getCurrent_page()+"";
    }
}
