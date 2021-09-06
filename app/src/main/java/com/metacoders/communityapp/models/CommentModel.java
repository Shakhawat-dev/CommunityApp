package com.metacoders.communityapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.metacoders.communityapp.models.newModels.UserModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommentModel implements Serializable {
    @Expose
    @SerializedName("comments")
    private List<comments> comments = new ArrayList<>();

    public CommentModel(List<CommentModel.comments> comments) {
        this.comments = comments;
    }

    public List<CommentModel.comments> getComments() {
        return comments;
    }

    public void setComments(List<CommentModel.comments> comments) {
        this.comments = comments;
    }

    public class comments implements Serializable {
        @Expose
        @SerializedName("updated_at")
        private String updated_at;
        @Expose
        @SerializedName("created_at")
        private String created_at;
        @Expose
        @SerializedName("comment")
        private String comment;
        @Expose
        @SerializedName("post_id")
        private int post_id;
        @Expose
        @SerializedName("user_id")
        private int user_id;
        @Expose
        @SerializedName("id")
        private int id;
        @Expose
        @SerializedName("user")
        private UserModel user;

        @Expose
        @SerializedName("reply")
        private List<Reply> reply;


        public List<Reply> getReply() {
            return reply;
        }

        public void setReply(List<Reply> reply) {
            this.reply = reply;
        }

        public UserModel getUser() {
            return user;
        }

        public void setUser(UserModel user) {
            this.user = user;
        }

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

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public int getPost_id() {
            return post_id;
        }

        public void setPost_id(int post_id) {
            this.post_id = post_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public class Reply implements Serializable {
        @Expose
        @SerializedName("updated_at")
        private String updated_at;
        @Expose
        @SerializedName("created_at")
        private String created_at;
        @Expose
        @SerializedName("reply")
        private String reply;
        @Expose
        @SerializedName("comment_id")
        private int comment_id;
        @Expose
        @SerializedName("post_id")
        private int post_id;
        @Expose
        @SerializedName("user_id")
        private int user_id;
        @Expose
        @SerializedName("id")
        private int id;
        @Expose
        @SerializedName("user")
        private UserModel user;

        public UserModel getUser() {
            return user;
        }

        public void setUser(UserModel user) {
            this.user = user;
        }

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

        public String getReply() {
            return reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public int getComment_id() {
            return comment_id;
        }

        public void setComment_id(int comment_id) {
            this.comment_id = comment_id;
        }

        public int getPost_id() {
            return post_id;
        }

        public void setPost_id(int post_id) {
            this.post_id = post_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

}
