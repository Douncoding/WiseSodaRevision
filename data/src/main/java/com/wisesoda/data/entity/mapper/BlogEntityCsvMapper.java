package com.wisesoda.data.entity.mapper;

import android.support.annotation.NonNull;

import com.wisesoda.data.entity.BlogEntity;

import javax.inject.Inject;

/**
 *
 */
public class BlogEntityCsvMapper {

    public static final int TITLE = 0;
    public static final int URL = 1;
    public static final int DATE = 2;
    public static final int IMAGE = 3;
    public static final int IMAGE_COUNT = 4;


    @Inject
    public BlogEntityCsvMapper() {}

    public BlogEntity transform(@NonNull String record) {
        int index = 0;
        String[] columns = record.split(",");

        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setId(index);

        for (String value : columns) {
            switch (index) {
                case TITLE:
                    blogEntity.setTitle(value);
                    break;
                case URL:
                    blogEntity.setBlogUrl(value);
                    break;
                case DATE:
                    blogEntity.setDate(value);
                    break;
                case IMAGE:
                    blogEntity.setImageUrl(value);
                    break;
                case IMAGE_COUNT:
                    blogEntity.setImageCount(Integer.parseInt(value));
                    break;
            }
            index++;
        }

        return blogEntity;
    }
}
