package com.metacoders.communityapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public  class Profile_Model  {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("email_status")
    @Expose
    private String emailStatus;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("google_id")
    @Expose
    private Object googleId;
    @SerializedName("facebook_id")
    @Expose
    private Object facebookId;
    @SerializedName("vk_id")
    @Expose
    private Object vkId;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("about_me")
    @Expose
    private Object aboutMe;
    @SerializedName("facebook_url")
    @Expose
    private Object facebookUrl;
    @SerializedName("twitter_url")
    @Expose
    private Object twitterUrl;
    @SerializedName("instagram_url")
    @Expose
    private Object instagramUrl;
    @SerializedName("pinterest_url")
    @Expose
    private Object pinterestUrl;
    @SerializedName("linkedin_url")
    @Expose
    private Object linkedinUrl;
    @SerializedName("vk_url")
    @Expose
    private Object vkUrl;
    @SerializedName("telegram_url")
    @Expose
    private Object telegramUrl;
    @SerializedName("youtube_url")
    @Expose
    private Object youtubeUrl;
    @SerializedName("last_seen")
    @Expose
    private Object lastSeen;
    @SerializedName("show_email_on_profile")
    @Expose
    private String showEmailOnProfile;
    @SerializedName("show_rss_feeds")
    @Expose
    private String showRssFeeds;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("token_expire")
    @Expose
    private String tokenExpire;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("rep_status")
    @Expose
    private String repStatus;
    @SerializedName("document")
    @Expose
    private String document;
    @SerializedName("profession")
    @Expose
    private String profession;
    @SerializedName("last_degree")
    @Expose
    private String lastDegree;

    public Profile_Model(String id, String username, String slug, String email, String emailStatus, String token, String password, String role, String userType, Object googleId, Object facebookId, Object vkId, String avatar, String status, Object aboutMe, Object facebookUrl, Object twitterUrl, Object instagramUrl, Object pinterestUrl, Object linkedinUrl, Object vkUrl, Object telegramUrl, Object youtubeUrl, Object lastSeen, String showEmailOnProfile, String showRssFeeds, String createdAt, String name, String tokenExpire, String address, String city, String country, String state, String details, String latitude, String longitude, String mobile, String repStatus, String document, String profession, String lastDegree) {
        this.id = id;
        this.username = username;
        this.slug = slug;
        this.email = email;
        this.emailStatus = emailStatus;
        this.token = token;
        this.password = password;
        this.role = role;
        this.userType = userType;
        this.googleId = googleId;
        this.facebookId = facebookId;
        this.vkId = vkId;
        this.avatar = avatar;
        this.status = status;
        this.aboutMe = aboutMe;
        this.facebookUrl = facebookUrl;
        this.twitterUrl = twitterUrl;
        this.instagramUrl = instagramUrl;
        this.pinterestUrl = pinterestUrl;
        this.linkedinUrl = linkedinUrl;
        this.vkUrl = vkUrl;
        this.telegramUrl = telegramUrl;
        this.youtubeUrl = youtubeUrl;
        this.lastSeen = lastSeen;
        this.showEmailOnProfile = showEmailOnProfile;
        this.showRssFeeds = showRssFeeds;
        this.createdAt = createdAt;
        this.name = name;
        this.tokenExpire = tokenExpire;
        this.address = address;
        this.city = city;
        this.country = country;
        this.state = state;
        this.details = details;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mobile = mobile;
        this.repStatus = repStatus;
        this.document = document;
        this.profession = profession;
        this.lastDegree = lastDegree;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Object getGoogleId() {
        return googleId;
    }

    public void setGoogleId(Object googleId) {
        this.googleId = googleId;
    }

    public Object getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(Object facebookId) {
        this.facebookId = facebookId;
    }

    public Object getVkId() {
        return vkId;
    }

    public void setVkId(Object vkId) {
        this.vkId = vkId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(Object aboutMe) {
        this.aboutMe = aboutMe;
    }

    public Object getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(Object facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public Object getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(Object twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public Object getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(Object instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public Object getPinterestUrl() {
        return pinterestUrl;
    }

    public void setPinterestUrl(Object pinterestUrl) {
        this.pinterestUrl = pinterestUrl;
    }

    public Object getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(Object linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public Object getVkUrl() {
        return vkUrl;
    }

    public void setVkUrl(Object vkUrl) {
        this.vkUrl = vkUrl;
    }

    public Object getTelegramUrl() {
        return telegramUrl;
    }

    public void setTelegramUrl(Object telegramUrl) {
        this.telegramUrl = telegramUrl;
    }

    public Object getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(Object youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public Object getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Object lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getShowEmailOnProfile() {
        return showEmailOnProfile;
    }

    public void setShowEmailOnProfile(String showEmailOnProfile) {
        this.showEmailOnProfile = showEmailOnProfile;
    }

    public String getShowRssFeeds() {
        return showRssFeeds;
    }

    public void setShowRssFeeds(String showRssFeeds) {
        this.showRssFeeds = showRssFeeds;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTokenExpire() {
        return tokenExpire;
    }

    public void setTokenExpire(String tokenExpire) {
        this.tokenExpire = tokenExpire;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRepStatus() {
        return repStatus;
    }

    public void setRepStatus(String repStatus) {
        this.repStatus = repStatus;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getLastDegree() {
        return lastDegree;
    }

    public void setLastDegree(String lastDegree) {
        this.lastDegree = lastDegree;
    }

    public  class  Profile_Response{

        @SerializedName("profileInfo")
        @Expose
        private Profile_Model profileInfo;

        public Profile_Response(Profile_Model profileInfo) {
            this.profileInfo = profileInfo;
        }

        public Profile_Model getProfileInfo() {
            return profileInfo;
        }

        public void setProfileInfo(Profile_Model profileInfo) {
            this.profileInfo = profileInfo;
        }
    }
}


