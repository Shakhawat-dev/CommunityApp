package com.metacoders.communityapp.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class UploadResult {

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("error")
    private boolean error;

    public UploadResult(String message, boolean error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
