package com.wisesoda.android.view;

import java.util.Collection;

public interface SplashView extends LoadDataView {

    /**
     * 키워드 목록/블로그 그룹화면 전환
     */
    void viewBlogGroupList(Collection<String> cities);

    /**
     * 최초 실행인 경우 화면 출력
     */
    void showAcceptView();

    /**
     * 최초 실행이 아닌 경우 숨김
     */
    void hideAcceptView();

    /**
     * 항상 노출되는 TeamsOfService 와 Privacy Policy 출력
     */
    void setComment(String policy);
}
