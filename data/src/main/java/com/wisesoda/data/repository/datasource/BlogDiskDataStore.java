package com.wisesoda.data.repository.datasource;

import com.wisesoda.data.entity.BlogEntity;

import java.util.List;

import rx.Observable;

/**
 * based o file system data store.
 */
public class BlogDiskDataStore implements BlogDataStore {

    @Override
    public Observable<List<BlogEntity>> blogEntityList(String city, String category, String keyword, String sortType, int page) {
        return null;
    }
}
