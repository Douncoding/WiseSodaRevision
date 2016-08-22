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

import com.google.gson.Gson;
import com.wisesoda.android.R;
import com.wisesoda.android.internal.di.components.DaggerBlogComponent;
import com.wisesoda.android.internal.di.modules.BlogModule;
import com.wisesoda.android.model.BlogModel;
import com.wisesoda.android.model.GroupModel;
import com.wisesoda.android.model.constant.BlogSortType;
import com.wisesoda.android.presenter.BlogListPresenter;
import com.wisesoda.android.view.BlogListView;
import com.wisesoda.android.view.adapter.BlogAdapter;
import com.wisesoda.android.view.components.EndlessRecyclerOnScrollListener;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class BlogListFragment extends BaseFragment implements BlogListView {
    public interface BlogListListener {
        /**
         * @param blogModel 선택된 블로그
         */
        void onBlogClicked(final BlogModel blogModel);

        /**
         * {@link BlogListFragment} 초기화 완료
         * @param blogModelList 이미지 리스트
         */
        void onVisibleToFinished(List<BlogModel> blogModelList);
    }

    @BindView(R.id.blog_list)
    RecyclerView recyclerView;
    @BindView(R.id.blog_progress_bar) ProgressBar progressBar;

    @Inject BlogListPresenter blogListPresenter;
    @Inject BlogAdapter blogAdapter;

    private BlogListListener blogListListener;
    private String sortType;
    private GroupModel groupModel;

    private boolean isVisibleToUser = false;
    private boolean isInitialize = false;

    private static final String PARAMS_BLOG_SORT_TYPE = "PARAMS_BLOG_SORT_TYPE";
    private static final String PARAMS_BLOG_GROUP = "PARAMS_BLOG_GROUP";
    public static BlogListFragment getInstance(GroupModel groupModel, BlogSortType sortType) {
        Bundle args = new Bundle();
        args.putString(PARAMS_BLOG_SORT_TYPE, sortType.name());
        args.putString(PARAMS_BLOG_GROUP, new Gson().toJson(groupModel));

        BlogListFragment fragment = new BlogListFragment();
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

        this.sortType = getArguments().getString(PARAMS_BLOG_SORT_TYPE);
        this.groupModel = GroupModel.create(getArguments().getString(PARAMS_BLOG_GROUP));

        BlogModule blogModule = new BlogModule(
                this.groupModel.getCity(),
                this.groupModel.getCategory(),
                this.groupModel.getKeyword(),
                this.sortType);

        DaggerBlogComponent.builder()
                .applicationComponent(getApplicationComponent())
                .blogModule(blogModule)
                .build().inject(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_blog_list, container, false);
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;

        if (isVisibleToUser && isInitialize) {
            activityDependentView();
        }
    }

    @Override
    public void initialize() {
        isInitialize = true;
        if (isVisibleToUser) {
            activityDependentView();
        }
    }

    public void activityDependentView() {
        this.blogListListener.onVisibleToFinished(blogAdapter.getRepresentBlogCollection());
    }

    public void moveFocusTopPosition() {
        if (recyclerView != null)
            recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void renderBlogList(Collection<BlogModel> blogModelCollection) {
        if (blogModelCollection != null) {
            this.blogAdapter.setBlogCollection(blogModelCollection);
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
    public Context context() {
        return this.getActivity().getApplication();
    }

    private BlogAdapter.OnItemClickListener onItemClickListener =
        new BlogAdapter.OnItemClickListener() {
            @Override
            public void onUserItemClicked(BlogModel blog) {
                if (BlogListFragment.this.blogListPresenter != null && blog != null) {
                    BlogListFragment.this.blogListPresenter.onBlogClicked(blog);
                }
            }
        };
}
