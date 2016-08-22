package com.wisesoda.android.internal.di.components;

import com.wisesoda.android.internal.di.PerActivity;
import com.wisesoda.android.internal.di.modules.GroupModule;
import com.wisesoda.android.view.fragment.KeywordListFragment;

import dagger.Component;

/**
 * Injects blog specific Fragment.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {GroupModule.class})
public interface GroupComponent {
    void inject(KeywordListFragment keywordListFragment);
}
