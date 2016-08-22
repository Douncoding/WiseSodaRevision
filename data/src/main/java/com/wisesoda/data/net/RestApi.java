package com.wisesoda.data.net;

import com.wisesoda.data.entity.BlogEntity;

import java.util.List;

import rx.Observable;

/**
 *
 */
public interface RestApi {

    Observable<List<BlogEntity>> blogEntityList();
}
