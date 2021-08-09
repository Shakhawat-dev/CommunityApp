package com.metacoders.communityapp.models.newModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Post implements Serializable {
    @Expose
    @SerializedName("data")
    public List<PostModel> data = new ArrayList<>();
    @SerializedName("current_page")
    @Expose
    private Integer currentPage;
    @SerializedName("first_page_url")
    @Expose
    private String firstPageUrl;
    @SerializedName("from")
    @Expose
    private Integer from;
    @SerializedName("last_page")
    @Expose
    private Integer lastPage;
    @SerializedName("last_page_url")
    @Expose
    private String lastPageUrl;
    @SerializedName("next_page_url")
    @Expose
    private String nextPageUrl;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("per_page")
    @Expose
    private Integer perPage;
    @SerializedName("prev_page_url")
    @Expose
    private String prevPageUrl;
    @SerializedName("to")
    @Expose
    private Integer to;
    @SerializedName("total")
    @Expose
    private Integer total;

    public List<PostModel> getData() {
        return data;
    }

    public void setData(List<PostModel> data) {
        this.data = data;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public String getFirstPageUrl() {
        return firstPageUrl;
    }

    public void setFirstPageUrl(String firstPageUrl) {
        this.firstPageUrl = firstPageUrl;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

    public String getLastPageUrl() {
        return lastPageUrl;
    }

    public void setLastPageUrl(String lastPageUrl) {
        this.lastPageUrl = lastPageUrl;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public String getPrevPageUrl() {
        return prevPageUrl;
    }

    public void setPrevPageUrl(String prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public  class PostModel implements Serializable {

        @Expose
        @SerializedName("updated_at")
        private String updated_at;
        @Expose
        @SerializedName("created_at")
        private String created_at;
        @Expose
        @SerializedName("description")
        private String description;
        @Expose
        @SerializedName("hit")
        private int hit;
        @Expose
        @SerializedName("admin_role")
        private int admin_role;
        @Expose
        @SerializedName("publication_status")
        private int publication_status;
        @Expose
        @SerializedName("image")
        private String image;
        @Expose
        @SerializedName("type")
        private String type;
        @Expose
        @SerializedName("thumb")
        private String thumb;
        @Expose
        @SerializedName("path")
        private String path;
        @Expose
        @SerializedName("metaphone")
        private String metaphone;
        @Expose
        @SerializedName("slug")
        private String slug;
        @Expose
        @SerializedName("title")
        private String title;
        @Expose
        @SerializedName("country")
        private String country;
        @Expose
        @SerializedName("category_id")
        private String category_id;
        @Expose
        @SerializedName("lang")
        private String lang;
        @Expose
        @SerializedName("user_id")
        private int user_id;
        @Expose
        @SerializedName("id")
        private int id;

        public PostModel() {
        }

        public PostModel(String updated_at, String created_at, String description, int hit, int admin_role, int publication_status, String type, String thumb, String path, String metaphone, String slug, String title, String country, String category_id, String lang, int user_id, int id) {
            this.updated_at = updated_at;
            this.created_at = created_at;
            this.description = description;
            this.hit = hit;
            this.admin_role = admin_role;
            this.publication_status = publication_status;
            this.type = type;
            this.thumb = thumb;
            this.path = path;
            this.metaphone = metaphone;
            this.slug = slug;
            this.title = title;
            this.country = country;
            this.category_id = category_id;
            this.lang = lang;
            this.user_id = user_id;
            this.id = id;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getHit() {
            return hit;
        }

        public void setHit(int hit) {
            this.hit = hit;
        }

        public int getAdmin_role() {
            return admin_role;
        }

        public void setAdmin_role(int admin_role) {
            this.admin_role = admin_role;
        }

        public int getPublication_status() {
            return publication_status;
        }

        public void setPublication_status(int publication_status) {
            this.publication_status = publication_status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getMetaphone() {
            return metaphone;
        }

        public void setMetaphone(String metaphone) {
            this.metaphone = metaphone;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

}






