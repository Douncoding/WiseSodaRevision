package com.wisesoda.android.view.components;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

//        Log.e("CHECK", "visibleItemCount:" + visibleItemCount + "//totalItemCount:" +
//                totalItemCount + "//firstVisibleItem:" + firstVisibleItem);

        // 전체 아이템의 개수가 증가하는 경우 로딩이 완료된것으로 판단하고 loading 값을 변경하여 '잠금' 상태를 해제 한다.
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        /**
         * 현재 남아 있는 항목개수가 최대 보여질수 있는 아이템의 개수보다 적은 경우 다음페이지의 아이템을 로딩하도록 처리
         */
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            onLoadMore(current_page++);

            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}