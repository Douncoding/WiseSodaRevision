package com.wisesoda.android.view.components;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wisesoda.android.model.BlogModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by douncoding on 16. 8. 19..
 */
public class ImageSlider extends AdapterViewFlipper {

    List<String> imageList;
    Adapter adapter;

    public ImageSlider(Context context) {
        super(context);
        init();
    }

    public ImageSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.adapter = new Adapter();
        this.setAdapter(adapter);

        this.setFlipInterval(8000);
        this.setAutoStart(true);

        ObjectAnimator inAnimator = ObjectAnimator.ofFloat(null, "alpha", .3f, 1f);
        inAnimator.setDuration(2000);
        this.setInAnimation(inAnimator);

        ObjectAnimator outAnimator = ObjectAnimator.ofFloat(null, "alpha", 1f, .3f);
        outAnimator.setDuration(500);
        this.setOutAnimation(outAnimator);
    }

    public void setImageCollection(Collection<BlogModel> repBlogCollection) {
        imageList = new ArrayList<>();

        for (BlogModel blog : repBlogCollection) {
            imageList.add(blog.getImageRepresent());
        }

        adapter.notifyDataSetChanged();
        setAutoStart(true);
    }

    class Adapter extends BaseAdapter {
        @Override
        public int getCount() {
            return imageList!=null ? imageList.size():0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            String item = imageList.get(i);

            final ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Picasso.with(getContext())
                    .load(item)
                    .into(imageView);
            return imageView;
        }
    }
}
