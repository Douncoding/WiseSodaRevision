package com.wisesoda.android.presenter;


import android.support.annotation.NonNull;

import com.wisesoda.android.model.BlogModel;
import com.wisesoda.android.model.mapper.BlogModelDataMapper;
import com.wisesoda.domain.interactor.UseCase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class BookmarkPresenter implements Presenter {

    private final UseCase getBookmarkList;
    private final BlogModelDataMapper blogModelDataMapper;
    private List<BlogModel> blogModelList;

    @Inject
    public BookmarkPresenter(@Named("bookmarkList") UseCase getList,
                             BlogModelDataMapper blogModelDataMapper) {
        this.blogModelDataMapper = blogModelDataMapper;
        this.getBookmarkList = getList;
    }

    void setView() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.getBookmarkList.unsubscribe();
        this.blogModelList = null;
    }
}
