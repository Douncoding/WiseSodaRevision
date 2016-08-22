package com.wisesoda.android.internal.di.components;

import com.wisesoda.android.internal.di.PerFragment;
import com.wisesoda.android.internal.di.modules.BlogModule;
import com.wisesoda.android.view.fragment.BlogListFragment;

import dagger.Component;
/**
 * Injects blog specific Fragment.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = {BlogModule.class})
public interface BlogComponent {
    void inject(BlogListFragment blogListFragment);
}
