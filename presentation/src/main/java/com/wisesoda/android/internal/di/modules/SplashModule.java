package com.wisesoda.android.internal.di.modules;


import com.wisesoda.android.internal.di.PerActivity;
import com.wisesoda.domain.interactor.GetCityList;
import com.wisesoda.domain.interactor.UseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class SplashModule {

    @Provides
    @PerActivity
    @Named("cityList")
    UseCase provideGetCityListUseCase(GetCityList getCityList) {
        return getCityList;
    }

}
