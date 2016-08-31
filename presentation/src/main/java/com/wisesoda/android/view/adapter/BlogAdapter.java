package com.wisesoda.android.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;
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

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    public interface OnItemClickListener {
        // 아이템 클릭
        void onUserItemClicked(BlogModel blog);
        // 북마크 클릭
        void onBookmarkClicked(View view, BlogModel blog);
        // 공유 아이콘 클릭
        void onSharedClicked(View view, BlogModel blog);
    }

    private List<BlogModel> mBlogList;
    private final LayoutInflater layoutInflater;
    private final Context context;

    private OnItemClickListener onItemClickListener;

    @Inject
    public BlogAdapter(Context context) {
        this.layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mBlogList = Collections.emptyList();
        this.context = context;
    }

    @Override
    public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = this.layoutInflater.inflate(R.layout.list_item_blog, parent, false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BlogViewHolder holder, final int position) {
        final BlogModel blogModel = this.mBlogList.get(position);

        holder.title.setText(blogModel.getTitle());
        holder.date.setText(blogModel.getDate());

        Picasso.with(context)
                .load(blogModel.getImageUrl())
                .into(holder.repImage);

        holder.viewsCount.setText(String.valueOf(blogModel.getViewsCount()));
        holder.bookmarkCount.setText(String.valueOf(blogModel.getViewsCount()));
        holder.pictureCount.setText(String.valueOf(blogModel.getImageCount()));
        holder.rate.setText(String.valueOf(blogModel.getRate()));

        // 북마크 처리 시작
        holder.bookmarkIcon.setChecked(blogModel.isBookmark());
        holder.bookmarkAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlogModel temp = mBlogList.get(position);
                temp.setBookmark(!blogModel.isBookmark());
                mBlogList.set(position, temp);

                if (BlogAdapter.this.activity != null) {
                    holder.bookmarkIcon.init(activity);
                    holder.bookmarkIcon.performClick();
                }

                if (onItemClickListener != null) {
                    onItemClickListener.onBookmarkClicked(holder.bookmarkIcon, blogModel);
                }
            }
        });

        // 흑백처리
        if (blogModel.isRead()) {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

            holder.repImage.setColorFilter(filter);
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.colorSecondText));
        } else {
            holder.repImage.setColorFilter(null);
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryText));
        }

        // 공유
        holder.sharedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null)
                    onItemClickListener.onSharedClicked(view, blogModel);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //읽음 처리
                BlogModel temp = mBlogList.get(position);
                temp.setRead(true);
                mBlogList.set(position, temp);

                if (onItemClickListener != null) {
                    onItemClickListener.onUserItemClicked(blogModel);
                }
            }
        });
    }


    public void changeBookmarkState(boolean aState) {

    }

    public void changeReadState(boolean aState) {

    }

    @Override
    public int getItemCount() {
        return (this.mBlogList != null) ? this.mBlogList.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    /**
     * ShineButton#init(Activity activity) 의 조건을 수행하기 위해서 외부에서 조건을 만족시켜줄 필요가 있다.
     * 따라서 SineButton 애니메이션을 사용하고자 하는 경우 별로의 메소드를 호출하도록 유도한다.
     */
    private Activity activity;
    public void setBookmarkAnimation(Activity activity) {
        this.activity = activity;
    }

    public void setBlogList(Collection<BlogModel> mBlogList) {
        if (mBlogList == null) {
            throw new IllegalArgumentException("The list cannot be null");
        } else {
            this.mBlogList = (List<BlogModel>) mBlogList;
            this.notifyDataSetChanged();
        }
    }

    public List<BlogModel> getRepresentBlogCollection() {
        List<BlogModel> representList = new ArrayList<>();
        for (BlogModel blog : this.mBlogList) {
            if (blog.isRepresent()) {
                representList.add(blog);
            }
        }
        return representList;
    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    class BlogViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title) TextView title;
        @BindView(R.id.date) TextView date;
        @BindView(R.id.rep_image) ImageView repImage;
        @BindView(R.id.picture_count) TextView pictureCount;
        @BindView(R.id.bookmark_count) TextView bookmarkCount;
        @BindView(R.id.views_count) TextView viewsCount;
        @BindView(R.id.rate) TextView rate;
        @BindView(R.id.bookmark_action) ViewGroup bookmarkAction;
        @BindView(R.id.bookmark_icon) ShineButton bookmarkIcon;
        @BindView(R.id.shared_icon) ImageView sharedIcon;

        public BlogViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
