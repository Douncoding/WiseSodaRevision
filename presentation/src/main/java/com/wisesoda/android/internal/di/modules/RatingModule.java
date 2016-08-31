package com.wisesoda.android.internal.di.modules;


import com.wisesoda.android.internal.di.PerActivity;
import com.wisesoda.android.model.BlogModel;
import com.wisesoda.domain.interactor.SetRating;
import com.wisesoda.domain.interactor.UseCase;

import dagger.Module;
import dagger.Provides;

@Module
public class RatingModule {

    private BlogModel blog;

    public RatingModule(BlogModel blog) {
        this.blog = blog;
    }

    @Provides
    @PerActivity
    UseCase provideSetRatingUseCase(SetRating setRating) {
        return setRating;
    }
}
