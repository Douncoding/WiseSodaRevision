package com.wisesoda.android.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.kakao.auth.KakaoSDK;
import com.wisesoda.android.R;
import com.wisesoda.android.internal.di.HasComponent;
import com.wisesoda.android.internal.di.components.DaggerOAuthComponent;
import com.wisesoda.android.internal.di.components.OAuthComponent;
import com.wisesoda.android.internal.di.modules.ActivityModule;
import com.wisesoda.android.internal.di.modules.OAuthModule;
import com.wisesoda.android.view.adapter.KakaoSDKAdapter;
import com.wisesoda.android.view.fragment.SignUpFragment;

import butterknife.ButterKnife;

/**
 * 사용자 관리 액티비티
 */
public class UserMgmtActivity extends BaseActivity implements HasComponent<OAuthComponent> {
    private OAuthComponent oAuthComponent;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, UserMgmtActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mgmt);
        ButterKnife.bind(this);

        this.initializeInjector();

        if (savedInstanceState == null) {
            this.initializeActivity();
            addFragment(R.id.fragment_container, SignUpFragment.getInstance());
        }
    }

    private void initializeInjector() {
        this.oAuthComponent = DaggerOAuthComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .oAuthModule(new OAuthModule(getApplication()))
                .build();
    }

    @Override
    public OAuthComponent getComponent() {
        return this.oAuthComponent;
    }

    private void initializeActivity() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        KakaoSDK.init(new KakaoSDKAdapter(getApplicationContext(), this));
    }

}
