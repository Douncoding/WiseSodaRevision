package com.wisesoda.android.view;

import android.content.Context;

/**
 * Interface representing a View that will use to load data.
 */
public interface LoadDataView {
    /**
     * Show and Hide a view with a progress bar indicating a loading process.
     */
    void showLoading();
    void hideLoading();

    void showError(String message);

    /**
     * Get a {@link Context}
     */
    Context context();
}
