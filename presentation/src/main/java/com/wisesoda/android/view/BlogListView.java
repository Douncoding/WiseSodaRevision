package com.wisesoda.android.view;

import com.wisesoda.android.model.BlogModel;

import java.util.Collection;

/**
 *
 */
public interface BlogListView extends LoadDataView{
    void initialize();
    /**
     * Render a blog list in the UI.
     *
     * @param blogModelCollection The collection of {@link BlogModel} that will be shown.
     */
    void renderBlogList(Collection<BlogModel> blogModelCollection);

    /**
     * View a {@link BlogModel#blogUrl} with Chrome.
     * @param blogModel The blog that will be shown.
     */
    void viewBlog(BlogModel blogModel);

    /**
     * 로그인 요청 화면 출력
     */
    void viewRequireLogin();

    /**
     * 북마크 상태의 따른 화면 설정
     * @param state 북마크 여부
     */
    void showBookmarkStateMessage(boolean state);

    /**
     *
     */
    void viewRequireRating(String jsonBlog);
}
