package com.wisesoda.android.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import com.wisesoda.android.R;
import com.wisesoda.android.WiseSodaStateManager;
import com.wisesoda.android.internal.di.components.DaggerRatingComponent;
import com.wisesoda.android.internal.di.components.RatingComponent;
import com.wisesoda.android.internal.di.modules.ActivityModule;
import com.wisesoda.android.internal.di.modules.RatingModule;
import com.wisesoda.android.model.BlogModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 애드몹은 가로의 사이즈가 확보되지 않는다면 출력되지 않는다는것에 주의 해야한다.
 * 별도의 프라그먼트를 구성하지 않음.
 */
public class RatingActivity extends BaseActivity {

    @BindView(R.id.blog_title) TextView mBlogTitleText;
    @BindView(R.id.blog_image) ImageView mBlogImageView;
    @BindView(R.id.blog_ratingbar) RatingBar mBlogRatingBar;
    @BindView(R.id.submit) TextView mSubmit;
    @BindView(R.id.adview) AdView mAdView;

    private BlogModel blogModel;

    @Inject
    WiseSodaStateManager wiseSodaStateManager;

    private static final String EXTRA_PARAMS_JSON_BLOG = "EXTRA_PARAMS_JSON_BLOG";
    public static Intent getCallingIntent(Context context, String jsonBlog) {
        Intent intent = new Intent(context, RatingActivity.class);
        intent.putExtra(EXTRA_PARAMS_JSON_BLOG, jsonBlog);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setFinishOnTouchOutside(false);
        ButterKnife.bind(this);

        initializeInjector();

        initializeActivity(savedInstanceState);
    }

    private void initializeInjector() {
        RatingComponent component = DaggerRatingComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .ratingModule(new RatingModule(blogModel))
                .build();
        component.inject(this);
    }

    private void initializeActivity(Bundle savedInstanceState) {
        String jsonBlog;
        if (savedInstanceState == null) {
            jsonBlog = getIntent().getStringExtra(EXTRA_PARAMS_JSON_BLOG);
            blogModel = BlogModel.create(jsonBlog);
        } else {
            jsonBlog = savedInstanceState.getString(EXTRA_PARAMS_JSON_BLOG);
            blogModel = BlogModel.create(jsonBlog);
        }

        mBlogTitleText.setText(blogModel.getTitle());
        Picasso.with(this).load(blogModel.getImageUrl()).into(mBlogImageView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wiseSodaStateManager.changeStateToRatingCompleted();
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_PARAMS_JSON_BLOG, blogModel.toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
