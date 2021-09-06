package com.metacoders.communityapp.models.newModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SignInResponse implements Serializable {
    @Expose
    @SerializedName("access_token")
    private String access_token;
    @Expose
    @SerializedName("user")
    private UserModel user;
    @Expose
    @SerializedName("user_id")
    private int user_id;
    @Expose
    @SerializedName("message")
    private String message;

    public String getAccess_token() {
        return access_token;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
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
