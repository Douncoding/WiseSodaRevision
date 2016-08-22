package com.wisesoda.android.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wisesoda.android.R;
import com.wisesoda.android.model.BlogModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    public interface OnItemClickListener {
        void onUserItemClicked(BlogModel blog);
    }

    private List<BlogModel> blogCollection;
    private final LayoutInflater layoutInflater;
    private final Context context;

    private OnItemClickListener onItemClickListener;

    @Inject
    public BlogAdapter(Context context) {
        this.layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.blogCollection = Collections.emptyList();
        this.context = context;
    }

    @Override
    public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = this.layoutInflater.inflate(R.layout.list_item_blog, parent, false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BlogViewHolder holder, int position) {
        final BlogModel blogModel = this.blogCollection.get(position);

        holder.title.setText(blogModel.getTitle());
        holder.date.setText(blogModel.getDate());

        Picasso.with(context)
                .load(blogModel.getImageUrl())
                .into(holder.repImage);

        holder.viewsCount.setText(String.valueOf(blogModel.getViewsCount()));
        holder.bookmarkCount.setText(String.valueOf(blogModel.getViewsCount()));
        holder.pictureCount.setText(String.valueOf(blogModel.getImageCount()));
        holder.rate.setText(String.valueOf(blogModel.getRate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onUserItemClicked(blogModel);
                }
            }
        });

        // 흑백처리
//        ColorMatrix matrix = new ColorMatrix();
//        matrix.setSaturation(0);
//        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
//        holder.repImage.setColorFilter(filter);
    }

    @Override
    public int getItemCount() {
        return (this.blogCollection != null) ? this.blogCollection.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setBlogCollection(Collection<BlogModel> blogCollection) {
        if (blogCollection == null) {
            throw new IllegalArgumentException("The list cannot be null");
        } else {
            this.blogCollection = (List<BlogModel>)blogCollection;
            this.notifyDataSetChanged();
        }
    }

    public List<BlogModel> getRepresentBlogCollection() {
        List<BlogModel> representList = new ArrayList<>();
        for (BlogModel blog : this.blogCollection) {
            if (blog.isRepresent()) {
                representList.add(blog);
            }
        }
        return representList;
    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    static class BlogViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title) TextView title;
        @BindView(R.id.date) TextView date;
        @BindView(R.id.rep_image) ImageView repImage;
        @BindView(R.id.picture_count) TextView pictureCount;
        @BindView(R.id.bookmark_count) TextView bookmarkCount;
        @BindView(R.id.views_count) TextView viewsCount;
        @BindView(R.id.rate) TextView rate;

        public BlogViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
