package com.wisesoda.android.internal.di.components;

import com.wisesoda.android.internal.di.PerActivity;
import com.wisesoda.android.internal.di.modules.ActivityModule;
import com.wisesoda.android.internal.di.modules.RatingModule;
import com.wisesoda.android.view.activity.RatingActivity;
import com.wisesoda.android.view.fragment.RatingFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, RatingModule.class})
public interface RatingComponent extends ActivityComponent{
    void inject(RatingActivity activity);
}
