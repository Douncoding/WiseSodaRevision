package com.wisesoda.domain.interactor;

import com.wisesoda.domain.executor.PostExecutionThread;
import com.wisesoda.domain.executor.ThreadExecutor;
import com.wisesoda.domain.repository.BlogRepository;

import javax.inject.Inject;

import rx.Observable;


public class GetBookmarkList extends UseCase {

    private BlogRepository blogRepository;

    @Inject
    protected GetBookmarkList(BlogRepository blogRepository,
                              ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.blogRepository = blogRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return null;
    }
}
