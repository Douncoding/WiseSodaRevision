package com.wisesoda.android.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wisesoda.android.Constants;
import com.wisesoda.android.R;
import com.wisesoda.android.internal.di.components.SplashComponent;
import com.wisesoda.android.presenter.SplashPresenter;
import com.wisesoda.android.view.SplashView;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class SplashFragment extends BaseFragment implements SplashView {

    private OnCallback onCallback;
    public interface OnCallback {
        // 초기화 정상완료
        void onLoadFinished(Collection<String> cityList);
        // 초기화 실패
        void onLoadError();
    }

    @BindView(R.id.policy_txt) TextView mPolicyText;
    @BindView(R.id.accept_btn) Button mAcceptButton;
    @BindView(R.id.progress) ProgressBar mLoadPRogress;

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
        this.initializePermission();

        this.getComponent(SplashComponent.class).inject(this);
        this.presenter.setView(this);
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

    @Override
    public void showAcceptView() {
        mAcceptButton.setVisibility(View.VISIBLE);
        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.firstSetting();
            }
        });
    }

    @Override
    public void hideAcceptView() {
        mAcceptButton.setVisibility(View.GONE);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mPolicyText.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mPolicyText.setLayoutParams(params);
    }

    @Override
    public void setComment(String html) {
        mPolicyText.setText(Html.fromHtml(html));
        mPolicyText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void showLoading() {
        mLoadPRogress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mLoadPRogress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String message) {

    }

    /**
     * 핸드폰 정보는 앱 실행을 위한 필수권한이며, 임시 사용자 번호를 추출하기 위한 목적으로 사용된다.
     */
    private void initializePermission() {
        int permissionCheck = ContextCompat.
                checkSelfPermission(getActivity(), android.Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    Constants.PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_READ_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    showMessage(getString(R.string.announce_permission_essential));
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
