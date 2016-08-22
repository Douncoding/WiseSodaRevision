package com.wisesoda.data.repository.datasource;

import com.wisesoda.data.entity.BlogEntity;

import java.util.List;

import rx.Observable;

/**
 * Interface that represents a data store from where data is retrieved.
 */
public interface BlogDataStore {
    /**
     * 매개변수의 조건에 따른 블로그 목록 요청
     * @param city 도시
     * @param category 속성
     * @param keyword 키워드
     * @param sortType 정렬방식
     */
    Observable<List<BlogEntity>> blogEntityList(String city,
                                                String category,
                                                String keyword,
                                                String sortType,
                                                int page);
}
