package com.wisesoda.android.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wisesoda.android.R;
import com.wisesoda.android.view.fragment.BlogListFragment;
import com.wisesoda.android.view.fragment.BookmarkFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookmarkActivity extends BaseActivity {


    @BindView(R.id.toolbar) Toolbar mToolbar;

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, BookmarkActivity.class);
        return callingIntent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.nav_item_bookmark));
        }

        if (savedInstanceState == null) {
            addFragment(R.id.fragment_container, new BookmarkFragment());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
