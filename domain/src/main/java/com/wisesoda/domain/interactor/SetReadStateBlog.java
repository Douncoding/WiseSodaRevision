package com.wisesoda.domain.interactor;

import com.wisesoda.domain.executor.PostExecutionThread;
import com.wisesoda.domain.executor.ThreadExecutor;
import com.wisesoda.domain.repository.BlogRepository;

import rx.Observable;

/**
 * 블로그 읽음 상태 처리
 */
public class SetReadStateBlog extends UseCase {

    private BlogRepository blogRepository;

    protected SetReadStateBlog(BlogRepository blogRepository, ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.blogRepository = blogRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return null;
    }
}
