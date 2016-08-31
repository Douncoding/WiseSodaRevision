package com.wisesoda.android.presenter;

import android.util.Log;

import com.wisesoda.android.WiseSodaStateManager;
import com.wisesoda.android.view.SplashView;
import com.wisesoda.domain.interactor.UseCase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;

public class SplashPresenter implements Presenter {
    private static final String TAG = SplashPresenter.class.getSimpleName();
    private static final String POLICY_HTML = "By tapping Accept, you agree to the" +
            " <a href=\"http://www.google.com\">Terms of Service</a> and" +
            " <a href=\"http://www.google.com\">Privacy Policy</a>.";

    private SplashView splashView;

    private UseCase getCityListUseCase;
    private UseCase postMobileInfo;

    private WiseSodaStateManager wiseSodaStateManager;

    @Inject
    public SplashPresenter(@Named("cityList")UseCase getCityListUseCase,
                           @Named("mobileInfo")UseCase postMobileInfo,
                           WiseSodaStateManager wiseSodaStateManager) {
        this.getCityListUseCase = getCityListUseCase;
        this.postMobileInfo = postMobileInfo;
        this.wiseSodaStateManager = wiseSodaStateManager;
    }

    public void setView(SplashView view) {
        this.splashView = view;
    }

    /**
     * 처음 실행하는 경우 사용자번호 생성과 단말기 정보 송신을 가능하도록 유도하며, 처음 실행이 아닌 경우 바로
     * 도시목록을 받아 올 수 있도록 한다.
     */
    @Override
    public void resume() {
        splashView.setComment(POLICY_HTML);
        wiseSodaStateManager.removeUserId();
        if (wiseSodaStateManager.isFirstExecute()) {
            splashView.showAcceptView();
        } else {
            splashView.hideAcceptView();
            initialize();
        }
    }

    @Override
    public void pause() {
        getCityListUseCase.unsubscribe();
        postMobileInfo.unsubscribe();
    }

    @Override
    public void destroy() {
        splashView = null;
        getCityListUseCase = null;
        wiseSodaStateManager = null;
    }

    public void initialize() {
        getCityList();
    }

    public void getCityList() {
        getCityListUseCase.execute(new CityListSubscriber());
    }

    /**
     * 어플리케이션 정책의 사용자 동의 후 초기화수행
     * MCC, MNC, SIM 정보 서버전송
     * 사용자 고유 번호 생성
     */
    public void firstSetting() {
        splashView.showLoading();

        // 고유번호 생성
        wiseSodaStateManager.generateUserPrivateId();

        // 단말기 기본정보 추출
        wiseSodaStateManager.generateMobileCarrierBaseData();

        // 서버 전송
        postMobileInfo.execute(new Subscriber() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "통신사 정보 전송완료");
                initialize();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });
    }

    private class CityListSubscriber extends Subscriber<List<String>> {
        @Override
        public void onCompleted() {
            splashView.hideAcceptView();
            splashView.hideLoading();
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
