package com.wisesoda.android.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.wisesoda.android.Constants;
import com.wisesoda.android.R;
import com.wisesoda.android.internal.di.components.OAuthComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = SignUpFragment.class.getSimpleName();
    private static final int RC_GOOGLE_SIGN_IN = 9001;

    @BindView(R.id.kakao_login) ViewGroup mKakaoLogin;
    @BindView(R.id.facebook_login) ViewGroup mFacebookLoginFake;
    @BindView(R.id.google_login) Button mGoogleLogin;
    @BindView(R.id.wechat_login) ViewGroup mWechatLogin;

    LoginButton mFacebookLogin;

    @Inject GoogleApiClient mGoogleApiClient;

    private SessionCallback mKakaocallback;
    private CallbackManager mFacebookCallbackManager;

    public static SignUpFragment getInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.getComponent(OAuthComponent.class).inject(this);
        if (savedInstanceState == null) {
            this.initializeFragment();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initializeFragment() {
        mKakaoLogin.setOnClickListener(this);

        mFacebookLoginFake.setOnClickListener(this);

        // 페이스북 인증
        mFacebookLogin = new LoginButton(getContext());
        mFacebookLogin.setFragment(this);
        mFacebookLogin.setReadPermissions("email");
        mFacebookLogin.setReadPermissions("public_profile");
        mFacebookLogin.setReadPermissions("user_friends");

        mFacebookCallbackManager = CallbackManager.Factory.create();
        mFacebookLogin.registerCallback(mFacebookCallbackManager, onFacebookCallback);

        // 구글 인증
        mGoogleLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.kakao_login:
                Log.d(Constants.VIEW_TAG, "카카오톡 로그인 실행");
                mKakaocallback = new SessionCallback();
                com.kakao.auth.Session.getCurrentSession().addCallback(mKakaocallback);
                com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen();
                com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, getActivity());

                break;
            case R.id.facebook_login:
                mFacebookLogin.performClick();
                break;
            case R.id.google_login:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
                break;
            case R.id.wechat_login:
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Log.e("CHECK", "로그아웃: " + status.getStatus());
                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        } else {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(TAG, "" + acct.getEmail());
        } else {
            // Signed out, show unauthenticated UI.
            Log.e(TAG, "" + result.getStatus().getStatus());
        }
    }

    private FacebookCallback<LoginResult> onFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.e("CHECK", "onSuccess:" + loginResult.getAccessToken());
        }

        @Override
        public void onCancel() {
            Log.e("CHECK", "onCancel");
        }

        @Override
        public void onError(FacebookException error) {
            Log.e("CHECK", "onError:" + error.getMessage());
        }
    };

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            Log.d("TAG" , "세션 오픈됨");
            // 사용자 정보를 가져옴, 회원가입 미가입시 자동가입 시킴
            KakaorequestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Log.d("TAG" , exception.getMessage());
            }
        }
    }
    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void KakaorequestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                int ErrorCode = errorResult.getErrorCode();
                int ClientErrorCode = -777;

                if (ErrorCode == ClientErrorCode) {
                    Toast.makeText(getContext(), "카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG" , "오류로 카카오로그인 실패 ");
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("TAG" , "오류로 카카오로그인 실패 ");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                String profileUrl = userProfile.getProfileImagePath();
                String userId = String.valueOf(userProfile.getId());
                String userName = userProfile.getNickname();

                Log.e("CHECK", "profile:" + profileUrl);
                //setLayoutText();
            }

            @Override
            public void onNotSignedUp() {
                // 자동가입이 아닐경우 동의창
            }
        });
    }
}
