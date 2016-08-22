package com.wisesoda.data.repository.datasource;

import com.wisesoda.data.entity.KeywordEntity;

import java.util.List;

import rx.Observable;

/**
 * Interface that represents a data store from where data is retrieved.
 */
public interface GroupDataStore {
    Observable<List<KeywordEntity>> keywordEntityList(String city, String category, String period);

    Observable<List<String>> cityEntityList();
}