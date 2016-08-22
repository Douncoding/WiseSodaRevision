package com.wisesoda.android.internal.di.components;


import com.wisesoda.android.internal.di.PerActivity;
import com.wisesoda.android.internal.di.modules.ActivityModule;
import com.wisesoda.android.internal.di.modules.SplashModule;
import com.wisesoda.android.view.fragment.SplashFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, SplashModule.class})
public interface SplashComponent extends ActivityComponent {
    void inject(SplashFragment splashFragment);
}
