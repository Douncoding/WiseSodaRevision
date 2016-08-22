package com.wisesoda.android.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wisesoda.android.R;
import com.wisesoda.android.model.KeywordModel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapter.KeywordHolder> {

    public interface OnItemClickListener {
        void onKeywordItemClicked(KeywordModel keywordModel);
        void onPlaceholderClicked(KeywordModel keywordModel);
    }

    private List<KeywordModel> keywordModelList;
    private final LayoutInflater layoutInflater;
    private final Context context;

    private OnItemClickListener onItemClickListener;

    @Inject
    public KeywordAdapter(Context context) {
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.keywordModelList = Collections.emptyList();
        this.context = context;
    }

    @Override
    public KeywordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = this.layoutInflater.inflate(R.layout.list_item_keyword, parent, false);
        return new KeywordHolder(view);
    }

    @Override
    public void onBindViewHolder(KeywordHolder holder, int position) {
        final KeywordModel keywordModel = this.keywordModelList.get(position);

        holder.order.setText(String.valueOf(position+1));
        holder.name.setText(keywordModel.getTitle());
        holder.placeholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onPlaceholderClicked(keywordModel);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onKeywordItemClicked(keywordModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (this.keywordModelList != null) ? this.keywordModelList.size() : 0;
    }

    public void setKeywordCollection(Collection<KeywordModel> collection) {
        this.keywordModelList = (List<KeywordModel>)collection;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class KeywordHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.order) TextView order;
        @BindView(R.id.name) TextView name;
        @BindView(R.id.placeholder) ImageView placeholder;
        @BindView(R.id.blog_count) TextView count;

        public KeywordHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
