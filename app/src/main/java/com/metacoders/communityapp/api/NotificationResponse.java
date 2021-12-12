package com.metacoders.communityapp.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.metacoders.communityapp.models.newModels.NotificationData;

import java.io.Serializable;
import java.util.List;

public class NotificationResponse implements Serializable {

    @Expose
    @SerializedName("notification")
    private List<NotificationData> notificationList;

    public NotificationResponse(List<NotificationData> notificationList) {
        this.notificationList = notificationList;
    }

    public List<NotificationData> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<NotificationData> notificationList) {
        this.notificationList = notificationList;
    }
}
