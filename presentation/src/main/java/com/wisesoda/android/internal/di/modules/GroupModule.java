package com.wisesoda.android.internal.di.modules;

import com.wisesoda.domain.executor.PostExecutionThread;
import com.wisesoda.domain.executor.ThreadExecutor;
import com.wisesoda.domain.interactor.GetKeywordList;
import com.wisesoda.domain.interactor.UseCase;
import com.wisesoda.domain.repository.GroupRepository;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class GroupModule {

    public GroupModule() {}

    @Provides
    @Named("keywordList")
    UseCase provideGetKeywordListUseCase(GroupRepository groupRepository,
                                         ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread) {
        return new GetKeywordList(groupRepository, threadExecutor, postExecutionThread);
    }

}
