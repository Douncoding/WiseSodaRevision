package com.wisesoda.android.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.wisesoda.android.R;
import com.wisesoda.android.model.BlogModel;
import com.wisesoda.android.model.GroupModel;
import com.wisesoda.android.model.constant.BlogSortType;
import com.wisesoda.android.model.constant.Category;
import com.wisesoda.android.view.components.ImageSlider;
import com.wisesoda.android.view.components.TintableImageView;
import com.wisesoda.android.view.fragment.BaseFragment;
import com.wisesoda.android.view.fragment.BlogListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlogListActivity extends BaseActivity implements BlogListFragment.BlogListListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.collaps)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    SmartTabLayout mTabLayout;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.slider) ImageSlider mSliderLayout;
    @BindView(R.id.header_content) ViewGroup mHeaderContent;
    @BindView(R.id.header_city_txt) TextView mCityText;
    @BindView(R.id.header_keyword_txt) TextView mKeywordText;
    @BindView(R.id.header_category_icon)
    FloatingActionButton mCategoryIcon;

    private ViewPagerAdapter mViewPagerAdapter;

    private GroupModel groupModel;

    public static final String EXTRA_BLOG_CITY = "EXTRA_BLOG_CITY";
    public static final String EXTRA_BLOG_KEYWORD = "EXTRA_BLOG_KEYWORD";
    public static final String EXTRA_BLOG_CATEGORY = "EXTRA_BLOG_CATEGORY";
    public static final String EXTRA_BLOG_GROUP = "EXTRA_BLOG_GROUP";
    public static Intent getCallingIntent(Context context, String city, String keyword, String category) {
        Intent callingIntent = new Intent(context, BlogListActivity.class);
        callingIntent.putExtra(EXTRA_BLOG_CITY, city);
        callingIntent.putExtra(EXTRA_BLOG_KEYWORD, keyword);
        callingIntent.putExtra(EXTRA_BLOG_CATEGORY, category);
        return callingIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogs);
        ButterKnife.bind(this);

        this.initializeActivity(savedInstanceState);

        setupActionBar();
        setupTabLayout();
    }

    private void initializeActivity(Bundle savedInstanceState) {
        String city, category, keyword;
        if (savedInstanceState == null) {
            city = getIntent().getStringExtra(EXTRA_BLOG_CITY);
            keyword = getIntent().getStringExtra(EXTRA_BLOG_KEYWORD);
            category = getIntent().getStringExtra(EXTRA_BLOG_CATEGORY);
            groupModel = new GroupModel(city, category, keyword);
        } else {
            groupModel = GroupModel.create(savedInstanceState.getString(EXTRA_BLOG_GROUP));
        }

        mCityText.setText(groupModel.getCity());
        mKeywordText.setText(groupModel.getKeyword());
        mCategoryIcon.setImageResource(Category.getLineIcon(groupModel.getCategory()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_BLOG_GROUP, groupModel.toString());
        super.onSaveInstanceState(outState);
    }

    private void setupActionBar() {
        /**
         * 툴바 속성 관리
         */
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(groupModel.getCity());
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        /**
         * 블로그 목록 스크롤 이벤트 발생 시 UI 처리
         * - Title 관리
         * - Expanded Content View 관리
         * - 상단이동 버튼 관리
         */
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float height = Math.abs(verticalOffset);
                float maxHeight = appBarLayout.getTotalScrollRange();
                float ratio = 1f - (height/maxHeight);

                ViewCompat.setAlpha(mHeaderContent, ratio);

                if (height >= maxHeight) {
                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                    mFab.show();
                } else {
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                    mFab.hide();
                }
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BlogListFragment)mViewPagerAdapter.getItem(mViewPager.getCurrentItem()))
                        .moveFocusTopPosition();
                mAppBarLayout.setExpanded(true);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        Drawable drawable = menu.findItem(R.id.action_search).getIcon();

        if (drawable != null) {
            int color = ContextCompat.getColor(BlogListActivity.this, R.color.white);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            menu.findItem(R.id.action_search).setIcon(drawable);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBlogClicked(BlogModel blogModel) {
        this.navigator.navigateToChrome(this, blogModel.getBlogUrl());
    }

    /**
     * @param blogModelList 이미지 리스트
     */
    @Override
    public void onVisibleToFinished(List<BlogModel> blogModelList) {
        mSliderLayout.setImageCollection(blogModelList);
    }

    private void setupTabLayout() {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        for (BlogSortType sortType : BlogSortType.values()) {
            mViewPagerAdapter.addFragment(BlogListFragment.getInstance(groupModel, sortType));
        }

        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                ViewGroup parent = (ViewGroup)LayoutInflater.from(container.getContext())
                        .inflate(R.layout.tab_blog_icon, container, false);
                TintableImageView icon = (TintableImageView)parent.findViewById(R.id.icon);
                icon.setImageResource(BlogSortType.getIcon(position));
                return parent;
            }
        });

        mTabLayout.setViewPager(mViewPager);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<BaseFragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(BaseFragment fragment) {
            mFragmentList.add(fragment);
        }
    }
}
