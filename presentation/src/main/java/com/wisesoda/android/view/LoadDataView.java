package com.wisesoda.android.view;

/**
 * Interface representing a View that will use to load data.
 */
public interface LoadDataView {
    void showLoading();

    void hideLoading();

    void showError(String message);
}
