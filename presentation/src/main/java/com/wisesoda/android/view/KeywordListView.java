package com.wisesoda.android.view;


import com.wisesoda.android.model.KeywordModel;

import java.util.Collection;


/**
 *
 */
public interface KeywordListView extends LoadDataView {

    /**
     * Render a keyword list in the UI.
     *
     * @param keywordModelCollection The collection of {@link KeywordModel} that will be shown.
     */
    void renderKeywordList(Collection<KeywordModel> keywordModelCollection);

    /**
     * @param keywordModel
     */
    void viewBlogList(KeywordModel keywordModel);

    /**
     * 구글 맵 현시
     * @param keywordModel
     */
    void viewGoogleMap(KeywordModel keywordModel);
}
