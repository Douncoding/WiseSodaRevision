package com.wisesoda.android.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.wisesoda.android.WiseSodaStateManager;
import com.wisesoda.android.model.BlogModel;
import com.wisesoda.android.model.mapper.BlogModelDataMapper;
import com.wisesoda.android.view.BlogListView;
import com.wisesoda.domain.Blog;
import com.wisesoda.domain.interactor.GetBlogList;
import com.wisesoda.domain.interactor.UseCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;

/**
 * {@link Presenter} that controls communication between views and models of the presentation layer.
 */
public class BlogListPresenter implements Presenter {

    private BlogListView blogListView;

    private final UseCase getBlogListUseCase;
    private final UseCase addBookmarkUseCase;
    private final UseCase removeBookmarkUseCase;

    private final BlogModelDataMapper blogModelDataMapper;
    private final WiseSodaStateManager wiseSodaStateManager;

    private List<BlogModel> blogModelList;

    private boolean isFirstLoad = false;

    @Inject
    public BlogListPresenter(@Named("blogList") UseCase getBlogListUseCase,
                             @Named("addBookmark") UseCase addBookmarkUseCase,
                             @Named("removeBookmark") UseCase removeBookmarkUseCase,
                             WiseSodaStateManager wiseSodaStateManager,
                             BlogModelDataMapper blogModelDataMapper) {
        this.getBlogListUseCase = getBlogListUseCase;
        this.addBookmarkUseCase = addBookmarkUseCase;
        this.removeBookmarkUseCase = removeBookmarkUseCase;

        this.wiseSodaStateManager = wiseSodaStateManager;
        this.blogModelDataMapper = blogModelDataMapper;
        this.blogModelList = new ArrayList<>();
    }

    public void setView(@NonNull BlogListView view) {
        this.blogListView = view;
    }

    @Override
    public void resume() {
        String jsonBlog = wiseSodaStateManager.needRating();
        if (jsonBlog != null) {
            blogListView.viewRequireRating(jsonBlog);
        }
    }

    @Override
    public void pause() {}

    @Override
    public void destroy() {
        this.getBlogListUseCase.unsubscribe();
        this.blogListView = null;
        this.blogModelList = null;
    }

    /**
     * @param nextPage 로딩을 원하는 페이지 번호
     */
    public void getNextBlogList(int nextPage) {
        GetBlogList getBlogList = (GetBlogList)getBlogListUseCase;
        getBlogList.setOptions(nextPage);
        getBlogList.execute(new BlogListSubscriber());
    }

    public void getFirstBlogList() {
        blogListView.showLoading();

        GetBlogList getBlogList = (GetBlogList)this.getBlogListUseCase;
        getBlogList.setOptions(0);
        getBlogList.execute(new BlogListSubscriber());
    }

    public void onBlogClicked(BlogModel blogModel) {
        // 블로그 평가 상태 변경
        wiseSodaStateManager.changeStateToRating(blogModel.toString());

        // 블로그 실행
        this.blogListView.viewBlog(blogModel);
    }

    /**
     * 북마크의 추가는 로그인 되어 있는 사용자만 수행할 수 있다.
     * 1. 로그인 확인 -> 로그인 상태 -> 북마크 추가
     * 2. 로그인 확인 -> 로그아웃 상태 -> 회원가입 -> 북마크 추가
     * 3. 로그인 확인 -> 로그아웃 상태 -> 로그인 -> 북마크 추가
     */
    public void addBookmark(BlogModel blog) {
        if (wiseSodaStateManager.isLogin()) {
            addBookmarkUseCase.execute(new BookmarkSubscriber());
        } else {
            blogListView.viewRequireLogin();
        }
    }

    public void removeBookmark() {
        removeBookmarkUseCase.execute(new BookmarkSubscriber());
    }

    private final class BookmarkSubscriber extends Subscriber<Boolean> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Boolean aBoolean) {
            blogListView.showBookmarkStateMessage(aBoolean);
        }
    }


    private final class BlogListSubscriber extends Subscriber<List<Blog>> {
        @Override
        public void onCompleted() {
            blogListView.hideLoading();

            if (!isFirstLoad) {
                isFirstLoad = true;
                blogListView.initialize();
            }
        }

        /**
         * Domain Layer 로 부터 전달된 예외의 종류에 따라 다른 처리를 수행할 수 있다
         */
        @Override
        public void onError(Throwable e) {
            blogListView.hideLoading();
            blogListView.showError(e.getMessage());
        }

        @Override
        public void onNext(List<Blog> blogs) {
            final Collection<BlogModel> blogModelCollection = blogModelDataMapper.transform(blogs);

            blogModelList.addAll(blogModelCollection);
            blogListView.renderBlogList(blogModelList);
        }
    }
}
