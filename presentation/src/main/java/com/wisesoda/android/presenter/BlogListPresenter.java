package com.wisesoda.android.presenter;

import android.support.annotation.NonNull;

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
    private final BlogModelDataMapper blogModelDataMapper;

    private List<BlogModel> blogModelList;

    private boolean isFirstLoad = false;

    @Inject
    public BlogListPresenter(@Named("blogList") UseCase getBlogListUseCase,
                             BlogModelDataMapper blogModelDataMapper) {
        this.getBlogListUseCase = getBlogListUseCase;
        this.blogModelDataMapper = blogModelDataMapper;
        this.blogModelList = new ArrayList<>();
    }

    public void setView(@NonNull BlogListView view) {
        this.blogListView = view;
    }

    @Override
    public void resume() {}

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
        this.blogListView.viewBlog(blogModel);
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
