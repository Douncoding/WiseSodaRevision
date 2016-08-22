package com.wisesoda.data.repository.datasource;

import android.content.Context;

import com.wisesoda.data.entity.mapper.BlogEntityCsvMapper;
import com.wisesoda.data.net.RestApi;
import com.wisesoda.data.net.RestApiImpl;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Facotry that creates different implementation of {@link BlogDataStore}.
 */
@Singleton
public class BlogDataStoreFactory {

    private final Context context;

    @Inject
    public BlogDataStoreFactory(Context context) {
        this.context = context;
    }

    public BlogDataStore createLocalDatabase(int blogId) {
        BlogDataStore dataStore = null;

        return dataStore;
    }

    public BlogDataStore createCloudDataStore() {
        RestApi restApi = new RestApiImpl(this.context);
        return new BlogCloudDataStore(restApi);
    }

    public BlogDataStore createSampleDataStore() {
        BlogEntityCsvMapper blogEntityCsvMapper = new BlogEntityCsvMapper();
        return new BlogSampleDataStore(this.context, blogEntityCsvMapper);
    }
}
