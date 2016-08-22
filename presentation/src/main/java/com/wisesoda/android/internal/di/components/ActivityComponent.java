package com.wisesoda.android.internal.di.components;

import android.app.Activity;


import com.wisesoda.android.internal.di.PerActivity;
import com.wisesoda.android.internal.di.modules.ActivityModule;

import dagger.Component;

/**
 * A base component upon which fragment's components may depend.
 * Activity-level components should extend this component.
 *
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity activity();
}
