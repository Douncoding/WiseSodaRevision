package com.wisesoda.domain.interactor;

import com.wisesoda.domain.executor.PostExecutionThread;
import com.wisesoda.domain.executor.ThreadExecutor;

import rx.Observable;

/**
 * Created by douncoding on 16. 8. 26..
 */
public class LogOut extends UseCase {

    protected LogOut(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return null;
    }
}
