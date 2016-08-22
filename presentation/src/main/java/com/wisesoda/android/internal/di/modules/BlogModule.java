package com.wisesoda.android.internal.di.modules;

import com.wisesoda.android.internal.di.PerFragment;
import com.wisesoda.domain.executor.PostExecutionThread;
import com.wisesoda.domain.executor.ThreadExecutor;
import com.wisesoda.domain.interactor.GetBlogList;
import com.wisesoda.domain.interactor.UseCase;
import com.wisesoda.domain.repository.BlogRepository;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class BlogModule {

    private String city;
    private String category;
    private String keyword;
    private String sortType;

    public BlogModule() {}

    public BlogModule(String city, String category, String keyword, String sortType) {
        this.city = city;
        this.category = category;
        this.keyword = keyword;
        this.sortType = sortType;
    }

    @Provides
    @PerFragment
    @Named("blogList")
    UseCase provideGetBlogListUseCase(BlogRepository blogRepository,
                                      ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread) {
        return new GetBlogList(city, category, keyword, sortType,
                blogRepository, threadExecutor, postExecutionThread);
    }
}
