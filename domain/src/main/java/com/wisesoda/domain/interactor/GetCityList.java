package com.wisesoda.domain.interactor;

import com.wisesoda.domain.executor.PostExecutionThread;
import com.wisesoda.domain.executor.ThreadExecutor;
import com.wisesoda.domain.repository.GroupRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * 도시목록 비즈니스 로직 정의
 */
public class GetCityList extends UseCase {

    private GroupRepository groupRepository;

    @Inject
    protected GetCityList(ThreadExecutor threadExecutor,
                          PostExecutionThread postExecutionThread,
                          GroupRepository groupRepository) {
        super(threadExecutor, postExecutionThread);
        this.groupRepository = groupRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return groupRepository.cities();
    }
}
