package com.wisesoda.data.repository.datasource;

import com.wisesoda.data.cache.BlogCache;
import com.wisesoda.data.entity.BlogEntity;
import com.wisesoda.data.net.RestApi;

import java.io.InputStream;
import java.util.List;

import rx.Observable;
import rx.functions.Action;
import rx.functions.Action1;

/**
 *
 */
public class BlogCloudDataStore implements BlogDataStore {

    private final RestApi restApi;

    BlogCloudDataStore(RestApi restApi) {
        this.restApi = restApi;
    }

    @Override
    public Observable<List<BlogEntity>> blogEntityList(String city, String category, String keyword, String sortType, int page) {
        return null;
    }
}
