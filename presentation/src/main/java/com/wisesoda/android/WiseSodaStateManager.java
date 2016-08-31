package com.wisesoda.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.inject.Inject;

/**
 * 사용자 상태 정보 관리
 *
 */
public class WiseSodaStateManager {
    private static final String TAG = WiseSodaStateManager.class.getSimpleName();

    private static final String KEY_USER_HASH_ID = "user_hash_id"; // String
    private static final String KEY_MCC = "mcc";
    private static final String KEY_MNC = "mnc";
    private static final String KEY_SIM = "sim";
    private static final String KEY_LOGIN_STATE = "login"; // Boolean
    private static final String KEY_LOGIN_PARAMS_EMAIL = "KEY_LOGIN_PARAMS_EMAIL"; // String
    private static final String KEY_PREVIOUS_BLOG = "KEY_PREVIOUS_BLOG";

    private Context context;
    private SharedPreferences sharedPreferences;

    @Inject
    public WiseSodaStateManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        this.context = context;
    }

    public boolean generateUserPrivateId() {
        TelephonyManager tel = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        int permissionCheck = ContextCompat.
                checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            throw new RuntimeException("READ_PHONE_STATE 권한이 필요합니다.");
        }

        // IMEI
        String tempUserId = tel.getDeviceId();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(tempUserId.getBytes());
            byte data[] = digest.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < data.length; i++) {
                sb.append(Integer.toString((data[i]&0xff)+0x100, 16).substring(1));
            }
            tempUserId = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }

        // 암호화된 IMEI
        Log.d(TAG, "임시 사용자 번호:" + tempUserId);
        return true;
    }

    public boolean generateMobileCarrierBaseData() {
        String mMCC = null;
        String mMNC = null;
        String mSIM = null;

        TelephonyManager tel = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        int permissionCheck = ContextCompat.
                checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            throw new RuntimeException("READ_PHONE_STATE 권한이 필요합니다.");
        }

        String networkOperator = tel.getNetworkOperator();
        if (!TextUtils.isEmpty(networkOperator)) {
            mMCC = networkOperator.substring(0, 3);
            mMNC = networkOperator.substring(3);
        }

        String simOperator = tel.getSimOperator();
        if (!TextUtils.isEmpty(simOperator)) {
            mSIM = simOperator;
        }

        if (TextUtils.isEmpty(mMCC) || TextUtils.isEmpty(mMNC) || TextUtils.isEmpty(mSIM)) {
            Log.w(TAG, "통신사 정보를 추출할 수 없습니다. USIM 카드를 확인하세요.");
            return false;
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_MCC, mMCC);
            editor.putString(KEY_MNC, mMNC);
            editor.putString(KEY_SIM, mSIM);
            editor.apply();
            Log.i(TAG, String.format(Locale.KOREA, "MCC:%s/MNC:%s/SIM:%s", mMCC, mMNC, mSIM));
            return true;
        }
    }

    public boolean isFirstExecute() {
        boolean contains = sharedPreferences.contains(KEY_USER_HASH_ID);
        return !contains;
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_HASH_ID, null);
    }

    public void removeUserId() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER_HASH_ID);
        editor.apply();
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(KEY_LOGIN_STATE, false);
    }

    public String getUserEmail() {
        if (isLogin()) {
            throw new RuntimeException("로그아웃 상태에서는 이메일을 확인할 수 없습니다.");
        }
        return sharedPreferences.getString(KEY_LOGIN_PARAMS_EMAIL, null);
    }

    public boolean changeStateToLogin(String email) {
        if (isLogin()) {
            throw new RuntimeException("이미 로그인 상태입니다.");
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_LOGIN_PARAMS_EMAIL, email);
            editor.putBoolean(KEY_LOGIN_STATE, true);
            editor.apply();
        }

        return true;
    }

    public void changeStateToLogout() {

    }

    /**
     * 블로그 평가가 필요한 상태인지 확인
     */
    public String needRating() {
        String jsonBlog = sharedPreferences.getString(KEY_PREVIOUS_BLOG, null);
        return jsonBlog;
    }

    /**
     * {@code KEY_PREVIOUS_BLOG}가 0 이외의 값을 같는 경우 또는 유효한 블로그 식별자 값을 가지는 경우
     * 필히 평가를 요구하는 {@link com.wisesoda.android.view.activity.RatingActivity} 가 호출될 수
     * 있도록 해야한다.
     * @param jsonBlog 평가가 필요한 블로그 식별자
     */
    public void changeStateToRating(String jsonBlog) {
        if (jsonBlog == null ) {
            throw new IllegalArgumentException("요청된 블로그의 평가상태로 변경할 수 없습니다.");
        } else {
            Log.d(TAG, "블로그 평가필요상태: " + jsonBlog);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_PREVIOUS_BLOG, jsonBlog);
            editor.apply();
        }
    }

    /**
     * 블로그 평가 완료 상태 변경
     */
    public void changeStateToRatingCompleted() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PREVIOUS_BLOG, null);
        editor.apply();
    }
}
