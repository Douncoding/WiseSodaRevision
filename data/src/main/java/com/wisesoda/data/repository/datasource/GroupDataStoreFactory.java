package com.wisesoda.data.repository.datasource;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 *
 */
@Singleton
public class GroupDataStoreFactory {

    private Context context;

    @Inject
    public GroupDataStoreFactory(Context context) {
        this.context = context;
    }

    public GroupDataStore createLocalDatabase() {

        return null;
    }

    public GroupDataStore createClouldDataStore() {
        return null;
    }

    public GroupDataStore createSampleDataStore() {
        return new GroupSampleDataStore(context);
    }
}
