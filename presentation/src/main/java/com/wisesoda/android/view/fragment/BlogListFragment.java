package com.wisesoda.android.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.wisesoda.android.view.dialog.RequireLoginDialog;

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
        // 블로그 선택
        void onBlogClicked(final BlogModel blogModel);
        // 초기화 완료
        void onVisibleToFinished(List<BlogModel> blogModelList);
        // 회원가입 화면 이동필요
        void onSignUpNavigate();
        // 공유 버튼 클릭
        void onShareClicked(BlogModel blogModel);
        // 블로그 평가 화면 요청
        void onNeedsRatingView(String jsonBlog);
    }

    @BindView(R.id.blog_list) RecyclerView recyclerView;
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
        // 북마크 애니메이션 설정
        blogAdapter.setBookmarkAnimation(getActivity());

        this.blogListPresenter.setView(this);
        this.blogListPresenter.getFirstBlogList();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("CHECK", "onResume()");
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

    /**
     * TabLayout 에서 Fragment 의 생존주기를 관리하는 부분의 경험 부족으로 코드가 어려워 지고 있다.
     *
     * TabLayout 에 노출된 프라그먼트의 갱신을 위한 로직이며, 액티비티에서 ViewPager 의 현재 값을 확인 후
     * 호춣하게된다.
     */
    public void userVisibleWithResume() {
        if (isInitialize) {
            this.blogListPresenter.resume();
            this.blogAdapter.notifyDataSetChanged();
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
    public void showBookmarkStateMessage(boolean state) {
        showMessage(""+state);
    }

    @Override
    public void viewRequireLogin() {
        RequireLoginDialog dialog = new RequireLoginDialog();
        dialog.setOnDialogCallback(new RequireLoginDialog.OnCallback() {
            @Override
            public void onPositive() {
                blogListListener.onSignUpNavigate();
            }

            @Override
            public void onNegative() {

            }
        });
        dialog.show(getFragmentManager(), RequireLoginDialog.TAG);
    }

    @Override
    public void viewRequireRating(String jsonBlog) {
        if (blogListPresenter != null) {
            blogListListener.onNeedsRatingView(jsonBlog);
        }
    }

    /**
     * {@link BlogAdapter.OnItemClickListener} 리스너 정의
     */
    private BlogAdapter.OnItemClickListener onItemClickListener =
        new BlogAdapter.OnItemClickListener() {
            @Override
            public void onUserItemClicked(BlogModel blog) {
                if (BlogListFragment.this.blogListPresenter != null && blog != null) {
                    BlogListFragment.this.blogListPresenter.onBlogClicked(blog);
                }
            }

            @Override
            public void onBookmarkClicked(View view, BlogModel blog) {
                // 북마크의 상태가 어댑터에서 갱신되어 있는 상태
                if (blog.isBookmark())
                    blogListPresenter.addBookmark(blog);
                else
                    blogListPresenter.removeBookmark();
            }

            @Override
            public void onSharedClicked(View view, BlogModel blog) {
                if (BlogListFragment.this.blogListListener != null && blog != null) {
                    BlogListFragment.this.blogListListener.onShareClicked(blog);
                }
            }
        };
}
