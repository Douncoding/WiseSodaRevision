package com.wisesoda.android.view.components;

import android.app.Activity;
import android.os.Bundle;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

/**
 * Created by douncoding on 16. 8. 27..
 */
public class KakaoLinkHandleActivity extends Activity {

    KakaoLink mKakaoLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mKakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
            KakaoTalkLinkMessageBuilder builder = mKakaoLink.createKakaoTalkLinkMessageBuilder();
            builder.addText("test");
            mKakaoLink.sendMessage(builder, this);
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        } finally {
            finish();
        }
    }
}
