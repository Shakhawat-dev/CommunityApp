package com.metacoders.communityapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class allDataResponse {
    @SerializedName("languageList")
    @Expose
    private List<LanguageList> languageList = null;
    @SerializedName("addList")
    @Expose
    private List<AddList> addList = null;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;

    public List<LanguageList> getLanguageList() {
        return languageList;
    }

    public void setLanguageList(List<LanguageList> languageList) {
        this.languageList = languageList;
    }

    public List<AddList> getAddList() {
        return addList;
    }

    public void setAddList(List<AddList> addList) {
        this.addList = addList;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public class Category {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("lang_id")
        @Expose
        private String langId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("name_slug")
        @Expose
        private String nameSlug;
        @SerializedName("parent_id")
        @Expose
        private String parentId;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("keywords")
        @Expose
        private String keywords;
        @SerializedName("color")
        @Expose
        private String color;
        @SerializedName("block_type")
        @Expose
        private String blockType;
        @SerializedName("category_order")
        @Expose
        private String categoryOrder;
        @SerializedName("show_at_homepage")
        @Expose
        private String showAtHomepage;
        @SerializedName("show_on_menu")
        @Expose
        private String showOnMenu;
        @SerializedName("created_at")
        @Expose
        private String createdAt;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNameSlug() {
            return nameSlug;
        }

        public void setNameSlug(String nameSlug) {
            this.nameSlug = nameSlug;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getBlockType() {
            return blockType;
        }

        public void setBlockType(String blockType) {
            this.blockType = blockType;
        }

        public String getCategoryOrder() {
            return categoryOrder;
        }

        public void setCategoryOrder(String categoryOrder) {
            this.categoryOrder = categoryOrder;
        }

        public String getShowAtHomepage() {
            return showAtHomepage;
        }

        public void setShowAtHomepage(String showAtHomepage) {
            this.showAtHomepage = showAtHomepage;
        }

        public String getShowOnMenu() {
            return showOnMenu;
        }

        public void setShowOnMenu(String showOnMenu) {
            this.showOnMenu = showOnMenu;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }

   public  class  AddList{
       @SerializedName("id")
       @Expose
       private String id;
       @SerializedName("name")
       @Expose
       private String name;
       @SerializedName("short_form")
       @Expose
       private String shortForm;
       @SerializedName("language_code")
       @Expose
       private String languageCode;
       @SerializedName("folder_name")
       @Expose
       private String folderName;
       @SerializedName("text_direction")
       @Expose
       private String textDirection;
       @SerializedName("status")
       @Expose
       private String status;
       @SerializedName("language_order")
       @Expose
       private String languageOrder;

       public String getId() {
           return id;
       }

       public void setId(String id) {
           this.id = id;
       }

       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }

       public String getShortForm() {
           return shortForm;
       }

       public void setShortForm(String shortForm) {
           this.shortForm = shortForm;
       }

       public String getLanguageCode() {
           return languageCode;
       }

       public void setLanguageCode(String languageCode) {
           this.languageCode = languageCode;
       }

       public String getFolderName() {
           return folderName;
       }

       public void setFolderName(String folderName) {
           this.folderName = folderName;
       }

       public String getTextDirection() {
           return textDirection;
       }

       public void setTextDirection(String textDirection) {
           this.textDirection = textDirection;
       }

       public String getStatus() {
           return status;
       }

       public void setStatus(String status) {
           this.status = status;
       }

       public String getLanguageOrder() {
           return languageOrder;
       }

       public void setLanguageOrder(String languageOrder) {
           this.languageOrder = languageOrder;
       }}

    public class LanguageList {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("short_form")
        @Expose
        private String shortForm;
        @SerializedName("language_code")
        @Expose
        private String languageCode;
        @SerializedName("folder_name")
        @Expose
        private String folderName;
        @SerializedName("text_direction")
        @Expose
        private String textDirection;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("language_order")
        @Expose
        private String languageOrder;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getShortForm() {
            return shortForm;
        }

        public void setShortForm(String shortForm) {
            this.shortForm = shortForm;
        }

        public String getLanguageCode() {
            return languageCode;
        }

        public void setLanguageCode(String languageCode) {
            this.languageCode = languageCode;
        }

        public String getFolderName() {
            return folderName;
        }

        public void setFolderName(String folderName) {
            this.folderName = folderName;
        }

        public String getTextDirection() {
            return textDirection;
        }

        public void setTextDirection(String textDirection) {
            this.textDirection = textDirection;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLanguageOrder() {
            return languageOrder;
        }

        public void setLanguageOrder(String languageOrder) {
            this.languageOrder = languageOrder;
        }

    }
}


