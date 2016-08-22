package com.wisesoda.android.view.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.wisesoda.android.R;
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

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    SmartTabLayout mTabLayout;
    @BindView(R.id.spinner) Spinner mSpinner;
    @BindView(R.id.header)
    GroupListHeaderView mHeader;

    private List<String> cityList;

    private ViewPagerAdapter viewPagerAdapter;
    private int mLastHeaderPosition = 0;
    private int mLastSpinnerPosition = 0;

    public static final String EXTRA_CITY_LIST = "EXTRA_CITY_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        ButterKnife.bind(this);

        this.initializeActivity(savedInstanceState);

        setUpDrawerLayout();

        setupTabLayout();
    }

    private void initializeActivity(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            cityList = getIntent().getStringArrayListExtra(EXTRA_CITY_LIST);
        } else {
            cityList = getIntent().getStringArrayListExtra(EXTRA_CITY_LIST);
        }
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(), android.R.layout.simple_spinner_item, cityList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        Period[] types = Period.values();
        for (Period type : types) {
            viewPagerAdapter.addFragment(KeywordListFragment.getInstance(), type.name());
        }

        mViewPager.setAdapter(viewPagerAdapter);
        mTabLayout.setViewPager(mViewPager);

        mHeader.setOnItemClickedListener(onHeaderClickListener);
        mSpinner.setOnItemSelectedListener(onSpinnerItemSelectedListener);
    }

    private GroupListHeaderView.OnItemClickedListener onHeaderClickListener
            = new GroupListHeaderView.OnItemClickedListener() {
        @Override
        public void onChanged(int position) {
            if (mLastHeaderPosition != position) {
                mLastHeaderPosition = position;
                viewPagerAdapter.onUpdatePage();
            }
        }
    };

    private AdapterView.OnItemSelectedListener onSpinnerItemSelectedListener
            = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            if (mLastSpinnerPosition != position) {
                mLastSpinnerPosition = position;
                viewPagerAdapter.onUpdatePage();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private boolean isUpdate = false;
        private int mLastActionedPrimaryItemPosition = -1;

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

        public void addFragment(BaseFragment fragment, String title) {
            mFragmentList.add(fragment);
            mTitleList.add(title);
        }

        /**
         * 프라그먼트 업데이트 요청
         * {@link ViewPagerAdapter#finishUpdate(ViewGroup)} 호출 이후 발생하도록 해야만 한다.
         */
        private void onUpdatePage() {
            String city = mSpinner.getSelectedItem().toString();
            String category = mHeader.getSelectedCategoryText();
            String period = Period.values()[mLastActionedPrimaryItemPosition].name();

            for (int i = 0; i < mFragmentList.size(); i++) {
                KeywordListFragment fragment = (KeywordListFragment)mFragmentList.get(i);
                if (mLastActionedPrimaryItemPosition == i) {
                    fragment.onVisiblePage(city, category, period);
                } else {
                    fragment.onInvisiblePage();
                }
            }
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if (mLastActionedPrimaryItemPosition != position) {
                mLastActionedPrimaryItemPosition = position;
                isUpdate = true;
            }
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
            if (isUpdate) {
                isUpdate = false;
                onUpdatePage();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }
}
