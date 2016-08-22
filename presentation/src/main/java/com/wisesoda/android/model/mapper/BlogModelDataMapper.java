package com.wisesoda.android.model.mapper;

import android.support.annotation.NonNull;

import com.wisesoda.android.model.BlogModel;
import com.wisesoda.domain.Blog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

/**
 * Mapper class used to transform {@link Blog} to {@link BlogModel}
 */
public class BlogModelDataMapper {

    @Inject
    public BlogModelDataMapper() {}

    public BlogModel transform(@NonNull Blog blog) {
        BlogModel blogModel = new BlogModel();

        blogModel.setId(blog.getId());
        blogModel.setTitle(blog.getTitle());
        blogModel.setDate(blog.getDate());
        blogModel.setBlogUrl(blog.getBlogUrl());
        blogModel.setImageUrl(blog.getImageUrl());

        blogModel.setImageCount(blog.getImageCount());
        blogModel.setBookmarkCount(blog.getBookmarkCount());
        blogModel.setViewsCount(blog.getViewsCount());
        blogModel.setRate(blog.getRate());
        if (blog.isRepresent()) {
            blogModel.setImageRepresent(blog.getImageRepresent());
        }

        return blogModel;
    }

    public Collection<BlogModel> transform(@NonNull Collection<Blog> blogCollection) {
        Collection<BlogModel> blogModelCollection;

        if (!blogCollection.isEmpty()) {
            blogModelCollection = new ArrayList<>();
            for (Blog blog : blogCollection) {
                blogModelCollection.add(transform(blog));
            }
        } else {
            blogModelCollection = Collections.emptyList();
        }
        return blogModelCollection;
    }
}
