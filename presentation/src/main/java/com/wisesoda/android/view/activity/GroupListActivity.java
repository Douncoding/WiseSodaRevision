package com.wisesoda.android.view.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.wisesoda.android.Constants;
import com.wisesoda.android.R;
import com.wisesoda.android.model.GroupModel;
import com.wisesoda.android.model.KeywordModel;
import com.wisesoda.android.model.constant.Period;
import com.wisesoda.android.view.components.GroupListHeaderView;
import com.wisesoda.android.view.fragment.BaseFragment;
import com.wisesoda.android.view.fragment.KeywordListFragment;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupListActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, KeywordListFragment.KeywordListListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.app_bar)AppBarLayout mAppBarLayout;
    @BindView(R.id.nav_view) NavigationView mNavigationView;
    @BindView(R.id.viewpager) ViewPager mViewPager;
    @BindView(R.id.tabs) SmartTabLayout mTabLayout;
    @BindView(R.id.spinner) Spinner mSpinner;
    @BindView(R.id.header) GroupListHeaderView mHeader;

    private List<String> cityList;


    private int mLastHeaderPosition = 0;
    private int mLastSpinnerPosition = 0;
    private ViewPagerAdapter mViewPagerAdapter;

    // 로딩된 도시목록
    public static final String EXTRA_CITY_LIST = "EXTRA_CITY_LIST";
    /**
     * 블로그 목록으로 즉시 이동 설정
     * {@link com.wisesoda.android.model.GroupModel} 값이 설정되어 있는 경우 활성화
     */
    public static final String EXTRA_DIRECT_BLOGLIST = "EXTRA_DIRECT_BLOGLIST";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        ButterKnife.bind(this);
        this.initializeActivity(savedInstanceState);
    }

    /**
     * 액티비티가 재활용되는 경우
     * - 카카오톡 공유기능에 의해 발생하는 경우
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        this.initializeActivity(null);
    }

    /**
     * 액티비티 초기화 (화면 구성을 위한 인텐트 처리)
     */
    private void initializeActivity(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            cityList = getIntent().getStringArrayListExtra(EXTRA_CITY_LIST);
        } else {
            Intent intent = getIntent();
            cityList = intent.getStringArrayListExtra(EXTRA_CITY_LIST);

            String jsonGroup = intent.getStringExtra(EXTRA_DIRECT_BLOGLIST);
            Log.d(Constants.VIEW_TAG, "즉시 블로그 목록 활성상태:" + jsonGroup);
            if (jsonGroup != null) {
                GroupModel groupModel = GroupModel.create(jsonGroup);
                navigator.navigateToBlogList(this,
                        groupModel.getCity(), groupModel.getKeyword(), groupModel.getCategory());
            }
        }
        setUpDrawerLayout();
        setupTabLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPagerAdapter = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(
                GroupListActivity.EXTRA_CITY_LIST, new ArrayList<>(cityList));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setUpDrawerLayout() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(
                ContextCompat.getColorStateList(this, R.drawable.nav_item_tintlist));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(), android.R.layout.simple_spinner_item, cityList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        switch (item.getItemId()) {
            case R.id.nav_bookmark:
                navigator.navigateToBookmark(this);
                break;
            case R.id.nav_signup:
                navigator.navigateToUserMgmt(this);
                break;
            case R.id.nav_settings:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        Drawable drawable = menu.findItem(R.id.action_search).getIcon();
        if (drawable != null) {
            int color = ContextCompat.getColor(this, R.color.white);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            menu.findItem(R.id.action_search).setIcon(drawable);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onKeywordClicked(KeywordModel keywordModel) {
        this.navigator.navigateToBlogList(this,
                keywordModel.getCity(), keywordModel.getTitle(), keywordModel.getCategory());
    }

    @Override
    public void onPlaceholderClicked(KeywordModel keywordModel) {
        this.navigator.navigateToGoogleMap(GroupListActivity.this,
                keywordModel.getCity(), keywordModel.getTitle());
    }


    private void setupTabLayout() {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        String city = mSpinner.getSelectedItem().toString();
        String category = mHeader.getSelectedCategoryText();
        Period[] types = Period.values();
        for (Period type : types) {
            mViewPagerAdapter.addFragment(
                    KeywordListFragment.getInstance(city, category, type.name()), type.name());
        }

        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setViewPager(mViewPager);
        mViewPagerAdapter.notifyDataSetChanged();

        mHeader.setOnItemClickedListener(onHeaderClickListener);
        mSpinner.setOnItemSelectedListener(onSpinnerItemSelectedListener);
    }

    private GroupListHeaderView.OnItemClickedListener onHeaderClickListener
            = new GroupListHeaderView.OnItemClickedListener() {
        @Override
        public void onChanged(int position) {
            if (mLastHeaderPosition != position) {
                mLastHeaderPosition = position;
                setupTabLayout();
            }
        }
    };

    private AdapterView.OnItemSelectedListener onSpinnerItemSelectedListener
            = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            if (mLastSpinnerPosition != position) {
                mLastSpinnerPosition = position;
                setupTabLayout();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };

    /**
     * {@link GroupListHeaderView}의 상태에 따라 새로운 프라그먼트를 생성해야 하기 때문에 Fragment 재사용하는
     * {@link FragmentPagerAdapter} 를 사용하지 않는다.
     * */
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<BaseFragment> mFragmentList = new ArrayList<>();
        private final List<String> mTitleList = new ArrayList<>();

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

        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }

        public void addFragment(BaseFragment fragment, String title) {
            mFragmentList.add(fragment);
            mTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }
}
