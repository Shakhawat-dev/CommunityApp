package com.metacoders.communityapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.metacoders.communityapp.models.newModels.Post;

public class CountryPostRespose {
    @SerializedName("countryPosts")
    @Expose
    public Post countryPosts = null;

    public Post getCountryPosts() {
        return countryPosts;
    }

    public void setCountryPosts(Post countryPosts) {
        this.countryPosts = countryPosts;
    }
}
