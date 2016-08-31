package com.wisesoda.android.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class GroupModel {
    private String city;
    private String category;
    private String keyword;

    public GroupModel(String city, String category, String keyword) {
        this.city = city;
        this.category = category;
        this.keyword = keyword;
    }

    public String getCity() {
        return city;
    }

    public String getCategory() {
        return category;
    }

    public String getKeyword() {
        return keyword;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static GroupModel create(String json) {
        return new Gson().fromJson(json, GroupModel.class);
    }
}
