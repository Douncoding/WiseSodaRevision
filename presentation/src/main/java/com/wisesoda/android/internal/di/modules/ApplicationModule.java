package com.wisesoda.android.internal.di.modules;

import android.content.Context;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.util.KakaoParameterException;
import com.wisesoda.android.UIThread;
import com.wisesoda.android.WiseSodaApplication;
import com.wisesoda.android.WiseSodaStateManager;
import com.wisesoda.data.cache.BlogCache;
import com.wisesoda.data.cache.BlogCacheImpl;
import com.wisesoda.data.executor.JobExecutor;
import com.wisesoda.data.repository.BlogDataRepository;
import com.wisesoda.data.repository.GroupDataRepository;
import com.wisesoda.domain.executor.PostExecutionThread;
import com.wisesoda.domain.executor.ThreadExecutor;
import com.wisesoda.domain.repository.BlogRepository;
import com.wisesoda.domain.repository.GroupRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module
public class ApplicationModule {
    private WiseSodaApplication application;

    public ApplicationModule(WiseSodaApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    BlogCache provideBlogCache(BlogCacheImpl blogCache) {
        return blogCache;
    }

    @Provides
    @Singleton
    BlogRepository provideBlogRepository(BlogDataRepository blogDataRepository) {
        return blogDataRepository;
    }

    @Provides
    @Singleton
    GroupRepository provideGroupRepository(GroupDataRepository groupDataRepository) {
        return groupDataRepository;
    }

    @Provides
    @Singleton
    WiseSodaStateManager provideWiseSodaStateManager(WiseSodaStateManager wiseSodaStateManager) {
        return wiseSodaStateManager;
    }
}
