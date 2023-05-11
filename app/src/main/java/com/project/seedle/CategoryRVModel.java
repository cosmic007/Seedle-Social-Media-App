package com.project.seedle;

public class CategoryRVModel {

    private String category;
    private String getCategoryImageUrl;


    public CategoryRVModel(String category, String getCategoryImageUrl) {
        this.category = category;
        this.getCategoryImageUrl = getCategoryImageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGetCategoryImageUrl() {
        return getCategoryImageUrl;
    }

    public void setGetCategoryImageUrl(String getCategoryImageUrl) {
        this.getCategoryImageUrl = getCategoryImageUrl;
    }
}
