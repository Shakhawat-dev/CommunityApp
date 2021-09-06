package com.metacoders.communityapp.models.newModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationResp {

    @SerializedName("accessToken")
    @Expose
    private String accessToken;

    @SerializedName("user")
    @Expose
    private UserModel user;

    @SerializedName("message")
    @Expose
    private String message;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
