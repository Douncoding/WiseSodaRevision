package com.wisesoda.android.internal.di.modules;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.wisesoda.android.internal.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class OAuthModule {

    Application application;

    public OAuthModule(Application application) {
        this.application = application;
    }

    @Provides
    @PerActivity
    GoogleSignInOptions provideGoogleSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
    }

    @Provides
    @PerActivity
    GoogleApiClient.OnConnectionFailedListener provideOnConnectionFailedListener() {
        return new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Log.e("CHECK", connectionResult.toString());
            }
        };
    }

    @Provides
    @PerActivity
    GoogleApiClient provideGoogleApiClient(Activity activity, GoogleSignInOptions gso,
                                           GoogleApiClient.OnConnectionFailedListener listener) {
        return new GoogleApiClient.Builder(application)
                .enableAutoManage((AppCompatActivity)activity, listener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
}
