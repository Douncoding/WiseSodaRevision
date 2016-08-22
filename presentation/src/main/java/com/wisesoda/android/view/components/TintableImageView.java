package com.wisesoda.android.view.components;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wisesoda.android.R;


public class TintableImageView extends ImageView {
    private ColorStateList tint;

    public TintableImageView(Context context) {
        super(context);
        init();
    }

    public TintableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        tint = ContextCompat.getColorStateList(getContext(), R.color.tab_icon_color);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (tint != null && tint.isStateful()) {
            updateTintColor();
        }
    }


    private void updateTintColor() {
        int color = tint.getColorForState(getDrawableState(), 0);
        setColorFilter(color);
    }
}
