package com.wisesoda.android.internal.di.components;

import android.content.Context;

import com.wisesoda.android.internal.di.modules.ApplicationModule;
import com.wisesoda.android.view.activity.BaseActivity;
import com.wisesoda.domain.executor.PostExecutionThread;
import com.wisesoda.domain.executor.ThreadExecutor;
import com.wisesoda.domain.repository.BlogRepository;
import com.wisesoda.domain.repository.GroupRepository;

import javax.inject.Singleton;

import dagger.Component;

/**
 *
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseActivity baseActivity);

    // Exposed to sub-graphs.
    Context context();
    ThreadExecutor threadExecutor();
    PostExecutionThread postExecutionThread();
    BlogRepository blogRepository();
    GroupRepository groupRepository();
}
