package com.metacoders.communityapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class News_List_Model {
    @SerializedName("getNewsList")
    @Expose
    private List<Post_Model> getNewsList = null;

    public List<Post_Model> getGetNewsList() {
        return getNewsList;
    }

    public void setGetNewsList(List<Post_Model> getNewsList) {
        this.getNewsList = getNewsList;
    }
}
