package com.metacoders.communityapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public  class CommentModel implements Serializable {
    @Expose
    @SerializedName("comments")
    private List<comments> comments = null;

    public CommentModel(List<CommentModel.comments> comments) {
        this.comments = comments;
    }

    public List<CommentModel.comments> getComments() {
        return comments;
    }

    public void setComments(List<CommentModel.comments> comments) {
        this.comments = comments;
    }

    public  class  comments implements Serializable {
       @Expose
       @SerializedName("created_at")
       private String created_at;
       @Expose
       @SerializedName("like_count")
       private String like_count;
       @Expose
       @SerializedName("ip_address")
       private String ip_address;
       @Expose
       @SerializedName("comment")
       private String comment;
       @Expose
       @SerializedName("name")
       private String name;
       @Expose
       @SerializedName("email")
       private String email;
       @Expose
       @SerializedName("user_id")
       private String user_id;
       @Expose
       @SerializedName("post_id")
       private String post_id;
       @Expose
       @SerializedName("parent_id")
       private String parent_id;
       @Expose
       @SerializedName("id")
       private String id;

       public String getCreated_at() {
           return created_at;
       }

       public void setCreated_at(String created_at) {
           this.created_at = created_at;
       }

       public String getLike_count() {
           return like_count;
       }

       public void setLike_count(String like_count) {
           this.like_count = like_count;
       }

       public String getIp_address() {
           return ip_address;
       }

       public void setIp_address(String ip_address) {
           this.ip_address = ip_address;
       }

       public String getComment() {
           return comment;
       }

       public void setComment(String comment) {
           this.comment = comment;
       }

       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }

       public String getEmail() {
           return email;
       }

       public void setEmail(String email) {
           this.email = email;
       }

       public String getUser_id() {
           return user_id;
       }

       public void setUser_id(String user_id) {
           this.user_id = user_id;
       }

       public String getPost_id() {
           return post_id;
       }

       public void setPost_id(String post_id) {
           this.post_id = post_id;
       }

       public String getParent_id() {
           return parent_id;
       }

       public void setParent_id(String parent_id) {
           this.parent_id = parent_id;
       }

       public String getId() {
           return id;
       }

       public void setId(String id) {
           this.id = id;
       }
   }
}
