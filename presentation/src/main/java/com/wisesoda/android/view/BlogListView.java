package com.wisesoda.android.view;

import com.wisesoda.android.model.BlogModel;

import java.util.Collection;

/**
 *
 */
public interface BlogListView extends LoadDataView{
    void initialize();
    /**
     * Render a blog list in the UI.
     *
     * @param blogModelCollection The collection of {@link BlogModel} that will be shown.
     */
    void renderBlogList(Collection<BlogModel> blogModelCollection);

    /**
     * View a {@link BlogModel#blogUrl} with Chrome.
     * @param blogModel The blog that will be shown.
     */
    void viewBlog(BlogModel blogModel);
}
