package com.metacoders.communityapp.models.newModels;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public  class CategoryResponse implements Serializable {

    @Expose
    @SerializedName("cateooryPost")
    private List<Post.PostModel> categoryPost;

    public List<Post.PostModel> getCateooryPost() {
        return categoryPost;
    }

    public void setCateooryPost(List<Post.PostModel> categoryPost) {
        this.categoryPost = categoryPost;
    }


}
