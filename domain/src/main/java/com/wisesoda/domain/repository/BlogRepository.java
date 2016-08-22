package com.wisesoda.domain.repository;

import com.wisesoda.domain.Blog;

import java.util.List;

import rx.Observable;

/**
 * Interface that represents a Repository for getting {@link com.wisesoda.domain.Blog} related data.
 */
public interface BlogRepository {

    /**
     * 입력된 매개변수의 조건에 대한 블로그 목록
     *
     * @param sortType 정렬 방식
     * @param page 로딩할 순번(Sequence)
     */
    Observable<List<Blog>> blogs(String city,
                                 String category,
                                 String keyword,
                                 String sortType,
                                 int page);

    Observable<Blog> blog(final int idBlog);
}
