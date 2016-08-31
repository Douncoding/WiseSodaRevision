package com.wisesoda.android.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.wisesoda.android.R;
import com.wisesoda.android.internal.di.components.DaggerBlogComponent;
import com.wisesoda.android.internal.di.modules.BlogModule;
import com.wisesoda.android.model.BlogModel;
import com.wisesoda.android.presenter.BlogListPresenter;
import com.wisesoda.android.view.BlogListView;
import com.wisesoda.android.view.adapter.BlogAdapter;
import com.wisesoda.android.view.components.EmptyView;
import com.wisesoda.android.view.components.EndlessRecyclerOnScrollListener;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class BookmarkFragment extends BaseFragment implements BlogListView {
    public interface BlogListListener {
        /**
         * @param blogModel 선택된 블로그
         */
        void onBlogClicked(final BlogModel blogModel);

        /**
         * {@link BookmarkFragment} 초기화 완료
         * @param blogModelList 이미지 리스트
         */
        void onVisibleToFinished(List<BlogModel> blogModelList);
    }

    @BindView(R.id.blog_list) RecyclerView recyclerView;
    @BindView(R.id.blog_progress_bar) ProgressBar progressBar;
    @BindView(R.id.empty_view) EmptyView emptyView;

    @Inject BlogListPresenter blogListPresenter;
    @Inject BlogAdapter blogAdapter;

    private BlogListListener blogListListener;

    public static BookmarkFragment getInstance() {
        Bundle args = new Bundle();
        BookmarkFragment fragment = new BookmarkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BlogListListener) {
            this.blogListListener = (BlogListListener)context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerBlogComponent.builder()
                .applicationComponent(getApplicationComponent())
                .blogModule(new BlogModule())
                .build()
                .inject(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_bookmark_list, container, false);
        ButterKnife.bind(this, view);

        this.blogAdapter.setOnItemClickListener(onItemClickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setAdapter(blogAdapter);
        this.recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int nextPage) {
                blogListPresenter.getNextBlogList(nextPage);
            }
        });

        emptyView.setIcoSrouceImage(R.drawable.ic_bookmark_line);
        emptyView.setTitle(getString(R.string.empty_bookmark_title));

        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.blogListPresenter.setView(this);
        this.blogListPresenter.getFirstBlogList();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.blogAdapter.notifyDataSetChanged();
        this.blogListPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.blogListPresenter.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.recyclerView.setAdapter(null);
        this.blogListPresenter.destroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.blogListListener = null;
    }

    @Override
    public void initialize() {

    }


    public void moveFocusTopPosition() {
        if (recyclerView != null)
            recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void renderBlogList(Collection<BlogModel> blogModelCollection) {
        if (blogModelCollection != null) {
            this.blogAdapter.setBlogList(blogModelCollection);
        }
    }

    @Override
    public void viewBlog(BlogModel blogModel) {
        if (this.blogListListener != null) {
            this.blogListListener.onBlogClicked(blogModel);
        }
    }

    @Override
    public void showLoading() {
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void viewRequireLogin() {

    }

    @Override
    public void viewRequireRating(String jsonBlog) {

    }

    @Override
    public void showBookmarkStateMessage(boolean state) {

    }

    private BlogAdapter.OnItemClickListener onItemClickListener =
        new BlogAdapter.OnItemClickListener() {
            @Override
            public void onUserItemClicked(BlogModel blog) {
                if (BookmarkFragment.this.blogListPresenter != null && blog != null) {
                    BookmarkFragment.this.blogListPresenter.onBlogClicked(blog);
                }
            }

            @Override
            public void onBookmarkClicked(View view, BlogModel blog) {

            }

            @Override
            public void onSharedClicked(View view, BlogModel blog) {

            }
        };
}
