package com.wisesoda.android;

import android.app.Application;

import com.wisesoda.android.internal.di.components.ApplicationComponent;
import com.wisesoda.android.internal.di.components.DaggerApplicationComponent;
import com.wisesoda.android.internal.di.modules.ApplicationModule;


/**
 * Android Main Application
 */
public class WiseSodaApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        this.initializeApplication();
        this.initializeInjector();
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    private void initializeApplication() {

    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }
}
