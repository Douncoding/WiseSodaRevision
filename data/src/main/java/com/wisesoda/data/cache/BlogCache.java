package com.wisesoda.data.cache;

import com.wisesoda.data.entity.BlogEntity;

import java.util.List;

import rx.Observable;

/**
 *
 */
public interface BlogCache {

    Observable<BlogEntity> getList();

    void put(List<BlogEntity> blogEntities);

    /**
     * Checks if an element (User) exists in the cache.
     *
     * @param userId The id used to look for inside the cache.
     * @return true if the element is cached, otherwise false.
     */
    boolean isCached(final int userId);

    /**
     * Checks if the cache is expired.
     *
     * @return true, the cache is expired, otherwise false.
     */
    boolean isExpired();

    /**
     * Evict all elements of the cache.
     */
    void evictAll();
}
