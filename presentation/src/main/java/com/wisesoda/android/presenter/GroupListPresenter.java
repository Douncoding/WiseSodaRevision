package com.wisesoda.android.presenter;

import android.support.annotation.NonNull;

//import com.wisesoda.android.model.KeywordModel;
//import com.wisesoda.android.model.mapper.KeywordModelDataMapper;

import com.wisesoda.android.model.KeywordModel;
import com.wisesoda.android.model.mapper.KeywordModelDataMapper;
import com.wisesoda.android.view.KeywordListView;
import com.wisesoda.domain.Keyword;
import com.wisesoda.domain.interactor.GetKeywordList;
import com.wisesoda.domain.interactor.UseCase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;

/**
 * {@link Presenter} that controls communication between views and models of the presentation layer.
 */
public class GroupListPresenter implements Presenter {
    private KeywordListView keywordListView;

    private final UseCase getKeywordListUseCase;
    private final KeywordModelDataMapper keywordModelDataMapper;

    @Inject
    public GroupListPresenter(@Named("keywordList") UseCase getKeywordListUseCase,
                              KeywordModelDataMapper keywordModelDataMapper) {
        this.getKeywordListUseCase = getKeywordListUseCase;
        this.keywordModelDataMapper = keywordModelDataMapper;
    }

    public void setView(@NonNull KeywordListView view) {
        this.keywordListView = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        getKeywordListUseCase.unsubscribe();
    }

    @Override
    public void destroy() {
        this.getKeywordListUseCase.unsubscribe();
        this.keywordListView = null;
    }

    public void onKeywordClicked(KeywordModel keywordModel) {
        this.keywordListView.viewBlogList(keywordModel);
    }

    public void onPlaceholderClicked(KeywordModel keywordModel) {
        this.keywordListView.viewGoogleMap(keywordModel);
    }

    public void initialize() {

    }

    public void getKeywordList(String city, String category, String period) {
        ((GetKeywordList)getKeywordListUseCase).setOptions(city, category, period);
        getKeywordListUseCase.execute(new KeyListSubscriber());
    }

    private class KeyListSubscriber extends Subscriber<List<Keyword>> {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(List<Keyword> keywordList) {
//            Log.e(TAG, "" + keywordList.size());
            keywordListView.renderKeywordList(keywordModelDataMapper.transform(keywordList));
        }
    }
}
