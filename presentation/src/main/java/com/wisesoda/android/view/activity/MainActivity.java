package com.wisesoda.android.view.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wisesoda.android.Constants;
import com.wisesoda.android.R;
import com.wisesoda.android.internal.di.HasComponent;
import com.wisesoda.android.internal.di.components.DaggerSplashComponent;
import com.wisesoda.android.internal.di.components.SplashComponent;
import com.wisesoda.android.internal.di.modules.ActivityModule;
import com.wisesoda.android.model.BlogModel;
import com.wisesoda.android.model.GroupModel;
import com.wisesoda.android.view.fragment.SplashFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends BaseActivity implements HasComponent<SplashComponent>,
        SplashFragment.OnCallback {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SplashComponent mSplashComponent;

    private String jsonGroupModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initializeInjector();
        this.initializeActivity(savedInstanceState);
    }

    private void initializeInjector() {
        this.mSplashComponent = DaggerSplashComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    private void initializeActivity(Bundle savedInstanceState) {
        Uri uri = getIntent().getData();
        if (uri != null) {
            String query = uri.getQuery();
            String[] params = query.split("=");
            jsonGroupModel = params[1];

            Log.d(Constants.VIEW_TAG,"카카오톡 공유기능에 의한 앱실행:" + jsonGroupModel);
        }

        if (savedInstanceState == null) {
            addFragment(R.id.fragment_container, new SplashFragment());
        }
    }

    @Override
    public SplashComponent getComponent() {
        return mSplashComponent;
    }

    @Override
    public void onLoadFinished(final Collection<String> cityList) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navigator.navigateToGroupList(MainActivity.this,
                        new ArrayList<>(cityList), jsonGroupModel);
                finish();
            }
        }, 2000);
    }

    @Override
    public void onLoadError() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.title("");
        builder.content("");
        builder.positiveText(android.R.string.ok);
        builder.build().show();
    }
}



