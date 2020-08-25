package com.metacoders.communityapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public  class Post_Model implements Serializable {

    @SerializedName("ttl_view")
    @Expose
    private String ttlView;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("lang_id")
    @Expose
    private String langId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("title_slug")
    @Expose
    private String titleSlug;
    @SerializedName("title_hash")
    @Expose
    private Object titleHash;
    @SerializedName("keywords")
    @Expose
    private String keywords;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("subcategory_id")
    @Expose
    private String subcategoryId;
    @SerializedName("image_big")
    @Expose
    private String imageBig;
    @SerializedName("image_default")
    @Expose
    private String imageDefault;
    @SerializedName("image_slider")
    @Expose
    private String imageSlider;
    @SerializedName("image_mid")
    @Expose
    private String imageMid;
    @SerializedName("image_small")
    @Expose
    private String imageSmall;
    @SerializedName("image_mime")
    @Expose
    private String imageMime;
    @SerializedName("hit")
    @Expose
    private String hit;
    @SerializedName("optional_url")
    @Expose
    private String optionalUrl;
    @SerializedName("need_auth")
    @Expose
    private String needAuth;
    @SerializedName("is_slider")
    @Expose
    private String isSlider;
    @SerializedName("slider_order")
    @Expose
    private String sliderOrder;
    @SerializedName("is_featured")
    @Expose
    private String isFeatured;
    @SerializedName("featured_order")
    @Expose
    private String featuredOrder;
    @SerializedName("is_recommended")
    @Expose
    private String isRecommended;
    @SerializedName("is_breaking")
    @Expose
    private String isBreaking;
    @SerializedName("is_scheduled")
    @Expose
    private String isScheduled;
    @SerializedName("visibility")
    @Expose
    private String visibility;
    @SerializedName("show_right_column")
    @Expose
    private String showRightColumn;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("video_path")
    @Expose
    private String videoPath;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("video_embed_code")
    @Expose
    private String videoEmbedCode;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("feed_id")
    @Expose
    private Object feedId;
    @SerializedName("post_url")
    @Expose
    private Object postUrl;
    @SerializedName("show_post_url")
    @Expose
    private String showPostUrl;
    @SerializedName("image_description")
    @Expose
    private Object imageDescription;
    @SerializedName("show_item_numbers")
    @Expose
    private String showItemNumbers;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Post_Model() {
    }

    public Post_Model(String ttlView, String id, String langId, String title, String titleSlug, Object titleHash, String keywords, String summary, String content, String categoryId, String subcategoryId, String imageBig, String imageDefault, String imageSlider, String imageMid, String imageSmall, String imageMime, String hit, String optionalUrl, String needAuth, String isSlider, String sliderOrder, String isFeatured, String featuredOrder, String isRecommended, String isBreaking, String isScheduled, String visibility, String showRightColumn, String postType, String videoPath, String imageUrl, String videoEmbedCode, String userId, String status, Object feedId, Object postUrl, String showPostUrl, Object imageDescription, String showItemNumbers, String createdAt) {
        this.ttlView = ttlView;
        this.id = id;
        this.langId = langId;
        this.title = title;
        this.titleSlug = titleSlug;
        this.titleHash = titleHash;
        this.keywords = keywords;
        this.summary = summary;
        this.content = content;
        this.categoryId = categoryId;
        this.subcategoryId = subcategoryId;
        this.imageBig = imageBig;
        this.imageDefault = imageDefault;
        this.imageSlider = imageSlider;
        this.imageMid = imageMid;
        this.imageSmall = imageSmall;
        this.imageMime = imageMime;
        this.hit = hit;
        this.optionalUrl = optionalUrl;
        this.needAuth = needAuth;
        this.isSlider = isSlider;
        this.sliderOrder = sliderOrder;
        this.isFeatured = isFeatured;
        this.featuredOrder = featuredOrder;
        this.isRecommended = isRecommended;
        this.isBreaking = isBreaking;
        this.isScheduled = isScheduled;
        this.visibility = visibility;
        this.showRightColumn = showRightColumn;
        this.postType = postType;
        this.videoPath = videoPath;
        this.imageUrl = imageUrl;
        this.videoEmbedCode = videoEmbedCode;
        this.userId = userId;
        this.status = status;
        this.feedId = feedId;
        this.postUrl = postUrl;
        this.showPostUrl = showPostUrl;
        this.imageDescription = imageDescription;
        this.showItemNumbers = showItemNumbers;
        this.createdAt = createdAt;
    }

    public String getTtlView() {
        return ttlView;
    }

    public void setTtlView(String ttlView) {
        this.ttlView = ttlView;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleSlug() {
        return titleSlug;
    }

    public void setTitleSlug(String titleSlug) {
        this.titleSlug = titleSlug;
    }

    public Object getTitleHash() {
        return titleHash;
    }

    public void setTitleHash(Object titleHash) {
        this.titleHash = titleHash;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getImageBig() {
        return imageBig;
    }

    public void setImageBig(String imageBig) {
        this.imageBig = imageBig;
    }

    public String getImageDefault() {
        return imageDefault;
    }

    public void setImageDefault(String imageDefault) {
        this.imageDefault = imageDefault;
    }

    public String getImageSlider() {
        return imageSlider;
    }

    public void setImageSlider(String imageSlider) {
        this.imageSlider = imageSlider;
    }

    public String getImageMid() {
        return imageMid;
    }

    public void setImageMid(String imageMid) {
        this.imageMid = imageMid;
    }

    public String getImageSmall() {
        return imageSmall;
    }

    public void setImageSmall(String imageSmall) {
        this.imageSmall = imageSmall;
    }

    public String getImageMime() {
        return imageMime;
    }

    public void setImageMime(String imageMime) {
        this.imageMime = imageMime;
    }

    public String getHit() {
        return hit;
    }

    public void setHit(String hit) {
        this.hit = hit;
    }

    public String getOptionalUrl() {
        return optionalUrl;
    }

    public void setOptionalUrl(String optionalUrl) {
        this.optionalUrl = optionalUrl;
    }

    public String getNeedAuth() {
        return needAuth;
    }

    public void setNeedAuth(String needAuth) {
        this.needAuth = needAuth;
    }

    public String getIsSlider() {
        return isSlider;
    }

    public void setIsSlider(String isSlider) {
        this.isSlider = isSlider;
    }

    public String getSliderOrder() {
        return sliderOrder;
    }

    public void setSliderOrder(String sliderOrder) {
        this.sliderOrder = sliderOrder;
    }

    public String getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(String isFeatured) {
        this.isFeatured = isFeatured;
    }

    public String getFeaturedOrder() {
        return featuredOrder;
    }

    public void setFeaturedOrder(String featuredOrder) {
        this.featuredOrder = featuredOrder;
    }

    public String getIsRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(String isRecommended) {
        this.isRecommended = isRecommended;
    }

    public String getIsBreaking() {
        return isBreaking;
    }

    public void setIsBreaking(String isBreaking) {
        this.isBreaking = isBreaking;
    }

    public String getIsScheduled() {
        return isScheduled;
    }

    public void setIsScheduled(String isScheduled) {
        this.isScheduled = isScheduled;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getShowRightColumn() {
        return showRightColumn;
    }

    public void setShowRightColumn(String showRightColumn) {
        this.showRightColumn = showRightColumn;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoEmbedCode() {
        return videoEmbedCode;
    }

    public void setVideoEmbedCode(String videoEmbedCode) {
        this.videoEmbedCode = videoEmbedCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getFeedId() {
        return feedId;
    }

    public void setFeedId(Object feedId) {
        this.feedId = feedId;
    }

    public Object getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(Object postUrl) {
        this.postUrl = postUrl;
    }

    public String getShowPostUrl() {
        return showPostUrl;
    }

    public void setShowPostUrl(String showPostUrl) {
        this.showPostUrl = showPostUrl;
    }

    public Object getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(Object imageDescription) {
        this.imageDescription = imageDescription;
    }

    public String getShowItemNumbers() {
        return showItemNumbers;
    }

    public void setShowItemNumbers(String showItemNumbers) {
        this.showItemNumbers = showItemNumbers;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
