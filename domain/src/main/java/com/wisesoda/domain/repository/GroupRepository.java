package com.wisesoda.domain.repository;

import com.wisesoda.domain.Keyword;

import java.util.List;

import rx.Observable;

/**
 *
 */
public interface GroupRepository {

    /**
     * 도시 목록 요청
     *
     * 도시는 문자열 형태로 처리
     */
    Observable<List<String>> cities();

    /**
     * 키워드 목록 요청
     *
     * 도시, 속성, 기간의 기준에 필터링된 키워드 목록
     */
    Observable<List<Keyword>> keywords(String city, String category, String period);
}
