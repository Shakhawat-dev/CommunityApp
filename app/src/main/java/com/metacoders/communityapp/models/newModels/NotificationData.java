package com.metacoders.communityapp.models.newModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotificationData implements Serializable {
    @Expose
    @SerializedName("updated_at")
    private String updated_at;
    @Expose
    @SerializedName("created_at")
    private String created_at;
    @Expose
    @SerializedName("read_at")
    private String read_at;
    @Expose
    @SerializedName("data")
    private Data data;
    @Expose
    @SerializedName("notifiable_id")
    private int notifiable_id;
    @Expose
    @SerializedName("notifiable_type")
    private String notifiable_type;
    @Expose
    @SerializedName("type")
    private String type;
    @Expose
    @SerializedName("id")
    private String id;

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getRead_at() {
        return read_at;
    }

    public void setRead_at(String read_at) {
        this.read_at = read_at;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getNotifiable_id() {
        return notifiable_id;
    }

    public void setNotifiable_id(int notifiable_id) {
        this.notifiable_id = notifiable_id;
    }

    public String getNotifiable_type() {
        return notifiable_type;
    }

    public void setNotifiable_type(String notifiable_type) {
        this.notifiable_type = notifiable_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public class Data implements Serializable {
        @Expose
        @SerializedName("type")
        private String type;
        @Expose
        @SerializedName("url")
        private String url;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("image")
        private String image;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
