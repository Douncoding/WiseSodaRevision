package com.wisesoda.android.presenter;

/**
 * Interface representing a Presenter in a model view presenter (MVP) patter.
 *
 * Method that control the lifecycle of the view. It should be called in the view's
 * (Activity or Fragment) onResume() method.
 */
public interface Presenter {

    void resume();

    void pause();

    void destroy();
}
