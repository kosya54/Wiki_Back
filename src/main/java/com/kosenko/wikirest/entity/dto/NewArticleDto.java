package com.kosenko.wikirest.entity.dto;

import java.util.List;

public class NewArticleDto {
    private String fileName;
    private Long userId;
    private List<Long> categoryIds;

    public NewArticleDto() {

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
