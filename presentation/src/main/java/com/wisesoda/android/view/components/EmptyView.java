package com.wisesoda.android.view.components;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wisesoda.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmptyView extends LinearLayout {

    @BindView(R.id.empty_icon) ImageView mIconView;
    @BindView(R.id.empty_title) TextView mTitleText;

    public EmptyView(Context context) {
        super(context);
        init();
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_empty_data, this);
        ButterKnife.bind(this);
    }

    public void setIcoSrouceImage(@DrawableRes int icon) {
        mIconView.setImageResource(icon);
    }

    public void setTitle(String title) {
        mTitleText.setText(title);
    }
}
