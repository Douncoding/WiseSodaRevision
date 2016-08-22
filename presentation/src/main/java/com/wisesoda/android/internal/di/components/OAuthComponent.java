package com.wisesoda.android.internal.di.components;


import com.wisesoda.android.internal.di.PerActivity;
import com.wisesoda.android.internal.di.modules.ActivityModule;
import com.wisesoda.android.internal.di.modules.OAuthModule;
import com.wisesoda.android.view.fragment.SignUpFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, OAuthModule.class})
public interface OAuthComponent extends ActivityComponent {
    void inject(SignUpFragment splashFragment);
}
