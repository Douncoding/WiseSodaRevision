package com.wisesoda.data.repository;

import com.wisesoda.data.entity.BlogEntity;
import com.wisesoda.data.entity.mapper.BlogEntityDataMapper;
import com.wisesoda.data.repository.datasource.BlogDataStore;
import com.wisesoda.data.repository.datasource.BlogDataStoreFactory;
import com.wisesoda.domain.Blog;
import com.wisesoda.domain.repository.BlogRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * {@link BlogRepository} for retrieving blog data.
 */
public class BlogDataRepository implements BlogRepository {

    private final BlogDataStoreFactory blogDataStoreFactory;
    private final BlogEntityDataMapper blogEntityDataMapper;

    @Inject
    public BlogDataRepository(BlogDataStoreFactory dataStoreFactory,
                              BlogEntityDataMapper blogEntityDataMapper) {
        this.blogDataStoreFactory = dataStoreFactory;
        this.blogEntityDataMapper = blogEntityDataMapper;
    }

    @SuppressWarnings("Convert@MethodRef")
    @Override
    public Observable<List<Blog>> blogs(String city, String category, String keyword,
                                        String sortType, int page) {
        final BlogDataStore blogDataStore = this.blogDataStoreFactory.createSampleDataStore();
        return blogDataStore.blogEntityList(city, category, keyword, sortType, page)
                .map(new Func1<List<BlogEntity>, List<Blog>>() {
                    @Override
                    public List<Blog> call(List<BlogEntity> blogEntities) {
                        return blogEntityDataMapper.transform(blogEntities);
                    }
                });
    }


    @Override
    public Observable<Blog> saveReadState(int idBlog) {
        return null;
    }
}
