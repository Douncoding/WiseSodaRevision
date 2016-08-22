package com.wisesoda.data.entity.mapper;

import com.wisesoda.data.entity.BlogEntity;
import com.wisesoda.domain.Blog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Mapper class used to transform {@link com.wisesoda.data.entity.BlogEntity} to {@link com.wisesoda.domain.Blog}
 * in the domain layer.
 */
@Singleton
public class BlogEntityDataMapper {

    @Inject
    public BlogEntityDataMapper() {}

    public Blog transform(BlogEntity blogEntity) {
        Blog blog = null;
        if (blogEntity != null) {
            blog = new Blog();
            blog.setId(blogEntity.getId());
            blog.setTitle(blogEntity.getTitle());
            blog.setDate(blogEntity.getDate());
            blog.setBlogUrl(blogEntity.getBlogUrl());
            blog.setImageUrl(blogEntity.getImageUrl());

            blog.setImageCount(blogEntity.getImageCount());
            blog.setBookmarkCount(blogEntity.getBookmarkCount());
            blog.setViewsCount(blogEntity.getViewsCount());
            blog.setRate(blogEntity.getRate());
        }

        return blog;
    }

    public List<Blog> transform(Collection<BlogEntity> blogEntityCollection) {
        List<Blog> blogList = new ArrayList<>();

        Blog blog;
        for (BlogEntity blogEntity : blogEntityCollection) {
            blog = transform(blogEntity);
            if (blog != null) {
                blogList.add(blog);
            }
        }
        return blogList;
    }
}
