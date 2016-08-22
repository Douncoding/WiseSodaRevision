package com.wisesoda.data.repository;

import android.util.Log;

import com.wisesoda.data.entity.KeywordEntity;
import com.wisesoda.data.entity.mapper.KeywordEntityDataMapper;
import com.wisesoda.data.repository.datasource.GroupDataStore;
import com.wisesoda.data.repository.datasource.GroupDataStoreFactory;
import com.wisesoda.domain.Keyword;
import com.wisesoda.domain.repository.GroupRepository;

import java.security.acl.Group;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 *
 */
public class GroupDataRepository implements GroupRepository {

    private final GroupDataStoreFactory groupDataStoreFactory;
    private final KeywordEntityDataMapper keywordEntityDataMapper;

    @Inject
    public GroupDataRepository(GroupDataStoreFactory dataStoreFactory, KeywordEntityDataMapper keywordEntityDataMapper) {
        this.groupDataStoreFactory = dataStoreFactory;
        this.keywordEntityDataMapper = keywordEntityDataMapper;
    }

    /**
     * City 가 String 형태의 클래스와 동등하기 떄문에
     * Mapper 를 사용하지 않는다는 점에 주의를 기울여야 한다.
     */
    @Override
    public Observable<List<String>> cities() {
        final GroupDataStore groupDataStore = this.groupDataStoreFactory.createSampleDataStore();
        return groupDataStore.cityEntityList();
    }

    @Override
    public Observable<List<Keyword>> keywords(String city, String category, String period) {
        final GroupDataStore groupDataStore = this.groupDataStoreFactory.createSampleDataStore();
        return groupDataStore.keywordEntityList(city, category, period)
                .map(new Func1<List<KeywordEntity>, List<Keyword>>() {
            @Override
            public List<Keyword> call(List<KeywordEntity> keywordEntities) {
                return keywordEntityDataMapper.transform(keywordEntities);
            }
        });
    }
}
