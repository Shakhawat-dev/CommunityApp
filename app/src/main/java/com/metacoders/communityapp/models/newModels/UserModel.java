package com.metacoders.communityapp.models.newModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public  class UserModel implements Serializable {

    @Expose
    @SerializedName("social_link")
    private String social_link;  //account_number
    @Expose
    @SerializedName("social_name")
    private String social_name;
    @Expose
    @SerializedName("email_verified_at")
    private String email_verified_at;
    @Expose
    @SerializedName("total_point")
    private String total_point;
    @Expose
    @SerializedName("report_point")
    private String report_point;
    @Expose
    @SerializedName("watch_point")
    private String watch_point;
    @Expose
    @SerializedName("company")
    private String company;
    @Expose
    @SerializedName("website")
    private String website;
    @Expose
    @SerializedName("bio")
    private String bio;
    @Expose
    @SerializedName("gender")
    private String gender;
    @Expose
    @SerializedName("zip_code")
    private String zip_code;
    @Expose
    @SerializedName("address")
    private String address;
    @Expose
    @SerializedName("phone")
    private String phone;
    @Expose
    @SerializedName("image")
    private String image;
    @Expose
    @SerializedName("country")
    private String country;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("account_number")
    private String account_number;

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getSocial_link() {
        return social_link;
    }

    public void setSocial_link(String social_link) {
        this.social_link = social_link;
    }

    public String getSocial_name() {
        return social_name;
    }

    public void setSocial_name(String social_name) {
        this.social_name = social_name;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(String email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public String getTotal_point() {
        return total_point;
    }

    public void setTotal_point(String total_point) {
        this.total_point = total_point;
    }

    public String getReport_point() {
        return report_point;
    }

    public void setReport_point(String report_point) {
        this.report_point = report_point;
    }

    public String getWatch_point() {
        return watch_point;
    }

    public void setWatch_point(String watch_point) {
        this.watch_point = watch_point;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
