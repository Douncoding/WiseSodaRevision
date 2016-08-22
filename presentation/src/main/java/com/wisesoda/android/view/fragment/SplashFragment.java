package com.wisesoda.android.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wisesoda.android.R;
import com.wisesoda.android.internal.di.components.SplashComponent;
import com.wisesoda.android.presenter.SplashPresenter;
import com.wisesoda.android.view.SplashView;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 *
 */
public class SplashFragment extends BaseFragment implements SplashView {

    private OnCallback onCallback;
    public interface OnCallback {
        void onLoadFinished(Collection<String> cityList);
    }

    @Inject
    SplashPresenter presenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCallback) {
            onCallback = (OnCallback)context;
        }

        if (onCallback == null) {
            throw new RuntimeException("SplashFragment#OnCallback 정의 필요");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            this.getComponent(SplashComponent.class).inject(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_splash, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            presenter.setView(this);
            presenter.initialize();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.presenter = null;
        this.onCallback = null;
    }

    @Override
    public void viewBlogGroupList(Collection<String> cities) {
        if (onCallback != null) {
            onCallback.onLoadFinished(cities);
        }
    }
}
