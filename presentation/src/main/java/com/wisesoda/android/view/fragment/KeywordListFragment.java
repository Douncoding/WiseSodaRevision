package com.wisesoda.android.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wisesoda.android.R;
import com.wisesoda.android.internal.di.components.DaggerGroupComponent;
import com.wisesoda.android.internal.di.modules.GroupModule;
import com.wisesoda.android.model.KeywordModel;
import com.wisesoda.android.model.constant.Category;
import com.wisesoda.android.presenter.GroupListPresenter;
import com.wisesoda.android.view.KeywordListView;
import com.wisesoda.android.view.adapter.KeywordAdapter;
import com.wisesoda.android.view.components.DividerItemDecoration;
import com.wisesoda.android.view.components.EmptyView;

import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class KeywordListFragment extends BaseFragment implements KeywordListView {
    private KeywordListListener keywordListListener;
    public interface KeywordListListener {
        void onKeywordClicked(final KeywordModel keywordModel);
        void onPlaceholderClicked(final KeywordModel keywordModel);
    }

    @BindView(R.id.keyword_list)
    RecyclerView recyclerView;
    @BindView(R.id.empty_view)
    EmptyView emptyView;

    @Inject
    KeywordAdapter keywordAdapter;

    @Inject
    GroupListPresenter groupListPresenter;

    String city;
    String category;
    String period;

    public static KeywordListFragment getInstance() {
        return new KeywordListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof KeywordListListener) {
            this.keywordListListener = (KeywordListListener)context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerGroupComponent.builder()
                .applicationComponent(getApplicationComponent())
                .groupModule(new GroupModule())
                .build().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_group_list, container, false);
        ButterKnife.bind(this, view);

        setupRecyclerView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.groupListPresenter.setView(this);
        this.groupListPresenter.initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.groupListPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.groupListPresenter.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.recyclerView.setAdapter(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.groupListPresenter.destroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.keywordListListener = null;
    }

    /**
     * 액티비티 의존 생존주기 #OnResume()
     */
    public void onVisiblePage(String city, String category, String period) {
        this.city = city;
        this.category = category;
        this.period = period;

        groupListPresenter.getKeywordList(city, category, period);
        recyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * 액티비티 의존 생존주기 #OnPause()
     * 되는 경우 프라그먼트 생성과 이벤트가 동시에 발생함에 따라 UI에 바인드 되어 있지 못할 수 있다. 따라서 해당 부분에 유의하여
     * 작업해야 한다. 다음 기간의 데이터를 미리 로딩하기 원하는 경우 여기서 보완한다.
     *
     * 변경된 카테고리 및 도시에 따라 현재보고있지 않은 프라그먼트에서도 데이터를 갱신하는 부분을 작업하지 않았기 때문에 클리어
     * 하는 동작으로 대체한다.
     */
    public void onInvisiblePage() {
        if (recyclerView != null) {
            recyclerView.setVisibility(View.INVISIBLE);
            keywordAdapter.setKeywordCollection(Collections.<KeywordModel>emptyList());
        }
    }

    private void setupRecyclerView() {
        this.keywordAdapter.setOnItemClickListener(onItemClickListener);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(keywordAdapter);
        this.recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), R.drawable.divider_recyclerview));

    }

    private KeywordAdapter.OnItemClickListener onItemClickListener
            = new KeywordAdapter.OnItemClickListener() {
        @Override
        public void onKeywordItemClicked(KeywordModel keywordModel) {
            if (KeywordListFragment.this.groupListPresenter != null && keywordModel != null) {
                KeywordListFragment.this.groupListPresenter.onKeywordClicked(keywordModel);
            }
        }

        @Override
        public void onPlaceholderClicked(KeywordModel keywordModel) {
            if (KeywordListFragment.this.groupListPresenter != null && keywordModel != null) {
                KeywordListFragment.this.groupListPresenter.onPlaceholderClicked(keywordModel);
            }
        }
    };

    @Override
    public void renderKeywordList(Collection<KeywordModel> keywordModelCollection) {
        if (keywordModelCollection.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            keywordAdapter.setKeywordCollection(keywordModelCollection);
            recyclerView.smoothScrollToPosition(0);
        } else {
            recyclerView.setVisibility(View.GONE);
            switch (Category.valueOf(category)) {
                case PL:
                    emptyView.setIcoSrouceImage(R.drawable.ic_placeholder_line);
                    break;
                case RE:
                    emptyView.setIcoSrouceImage(R.drawable.ic_cutlery);
                    break;
                case AC:
                    emptyView.setIcoSrouceImage(R.drawable.ic_bed_line);
                    break;
                case SH:
                    emptyView.setIcoSrouceImage(R.drawable.ic_shopping_line);
                    break;
                case TR:
                    emptyView.setIcoSrouceImage(R.drawable.ic_bus_line);
                    break;
                case SM:
                    emptyView.setIcoSrouceImage(R.drawable.ic_smartphone_line);
                    break;
            }

            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void viewBlogList(KeywordModel keywordModel) {
        this.keywordListListener.onKeywordClicked(keywordModel);
    }

    @Override
    public void viewGoogleMap(KeywordModel keywordModel) {
        this.keywordListListener.onPlaceholderClicked(keywordModel);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public Context context() {
        return null;
    }
}
