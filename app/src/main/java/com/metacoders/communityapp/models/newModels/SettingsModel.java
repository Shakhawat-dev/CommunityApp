package com.metacoders.communityapp.models.newModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SettingsModel implements Serializable {
    @Expose
    @SerializedName("categories")
    private List<CategoryModel> categories;
    @Expose
    @SerializedName("countries")
    private List<CountryModel> countries;

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
    }

    public List<CountryModel> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryModel> countries) {
        this.countries = countries;
    }
}
