package com.wisesoda.android.model;

import com.google.gson.Gson;

public class BlogModel {

    int id;
    String title;
    String date;
    String blogUrl;
    String imageUrl;
    int imageCount;
    int bookmarkCount;
    int viewsCount;
    float rate;

    boolean isBookmark;
    boolean isRead;

    /**
     * 블로그 목록 중 대표 블로그 표기
     */
    boolean isRepresent;
    String imageRepresent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBlogUrl() {
        return blogUrl;
    }

    public void setBlogUrl(String blogUrl) {
        this.blogUrl = blogUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public int getBookmarkCount() {
        return bookmarkCount;
    }

    public void setBookmarkCount(int bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void setImageRepresent(String image) {
        this.isRepresent= true;
        this.imageRepresent = image;
    }

    public boolean isRepresent() {
        return isRepresent;
    }

    public String getImageRepresent() {
        return imageRepresent;
    }

    public boolean isBookmark() {
        return isBookmark;
    }

    public void setBookmark(boolean bookmark) {
        isBookmark = bookmark;
    }

    public void setRepresent(boolean represent) {
        isRepresent = represent;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static BlogModel create(String json) {
        return new Gson().fromJson(json, BlogModel.class);
    }
}
