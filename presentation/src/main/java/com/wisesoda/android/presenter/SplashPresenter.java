package com.wisesoda.android.presenter;

import com.wisesoda.android.view.SplashView;
import com.wisesoda.domain.interactor.UseCase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;

public class SplashPresenter implements Presenter {

    private SplashView splashView;
    private UseCase getCityListUseCase;

    @Inject
    public SplashPresenter(@Named("cityList")UseCase getCityListUseCase) {
        this.getCityListUseCase = getCityListUseCase;
    }

    public void setView(SplashView view) {
        this.splashView = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        getCityListUseCase.unsubscribe();
    }

    @Override
    public void destroy() {

    }

    public void initialize() {
        getCityList();
    }

    public void getCityList() {
        getCityListUseCase.execute(new CityListSubscriber());
    }

    private class CityListSubscriber extends Subscriber<List<String>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<String> strings) {
            splashView.viewBlogGroupList(strings);
        }
    }
}
