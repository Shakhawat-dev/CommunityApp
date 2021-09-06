package com.metacoders.communityapp.models.newModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoryResponse implements Serializable {

    @Expose
    @SerializedName("cateooryPost")
    private Post categoryPost;

    public Post getCateooryPost() {
        return categoryPost;
    }

    public void setCateooryPost(Post categoryPost) {
        this.categoryPost = categoryPost;
    }


}
