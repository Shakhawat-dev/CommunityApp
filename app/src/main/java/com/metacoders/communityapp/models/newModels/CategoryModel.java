package com.metacoders.communityapp.models.newModels;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public  class CategoryModel implements Serializable {
    @Expose
    @SerializedName("publication_status")
    private String publication_status;
    @Expose
    @SerializedName("slug")
    private String slug;
    @Expose
    @SerializedName("category_name")
    private String category_name;
    @Expose
    @SerializedName("id")
    private int id;

    public String getPublication_status() {
        return publication_status;
    }

    public void setPublication_status(String publication_status) {
        this.publication_status = publication_status;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return this.category_name + ""  ;
    }
}
