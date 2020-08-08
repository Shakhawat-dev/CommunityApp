package com.metacoders.communityapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("token_expire")
    @Expose
    private Integer tokenExpire;
    @SerializedName("user")
    @Expose
    private UserModel user;

    public LoginResponse() {
    }

    public LoginResponse(String status, Boolean error, String token, Integer tokenExpire, UserModel user) {
        this.status = status;
        this.error = error;
        this.token = token;
        this.tokenExpire = tokenExpire;
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getTokenExpire() {
        return tokenExpire;
    }

    public void setTokenExpire(Integer tokenExpire) {
        this.tokenExpire = tokenExpire;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "status='" + status + '\'' +
                ", error=" + error +
                ", token='" + token + '\'' +
                ", tokenExpire=" + tokenExpire +
                ", user=" + user +
                '}';
    }

    public class forgetPassResponse {

        @SerializedName("error")
        @Expose
        private Boolean error;
        @SerializedName("message")
        @Expose
        private String message;

        public forgetPassResponse(Boolean error, String message) {
            this.error = error;
            this.message = message;
        }

        public Boolean getError() {
            return error;
        }

        public void setError(Boolean error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}

