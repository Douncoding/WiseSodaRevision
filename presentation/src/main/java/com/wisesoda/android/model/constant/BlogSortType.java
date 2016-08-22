package com.wisesoda.android.model.constant;

import com.wisesoda.android.R;

public enum BlogSortType {
    DATE, PHOTO, VIEWS, RATE, BOOKMARK;

    public static int getIcon(BlogSortType type) {
        switch (type) {
            case DATE:
                return R.drawable.ic_calendar_line;
            case PHOTO:
                return R.drawable.ic_photo_line;
            case VIEWS:
                return R.drawable.ic_views_line;
            case RATE:
                return R.drawable.ic_star_line;
            case BOOKMARK:
                return R.drawable.ic_bookmark_line;
        }
        throw new IllegalArgumentException("잘못된 매개변수입니다: " + type);
    }

    public static int getIcon(int pos) {
        switch (BlogSortType.values()[pos]) {
            case DATE:
                return R.drawable.ic_calendar_line;
            case PHOTO:
                return R.drawable.ic_photo_line;
            case VIEWS:
                return R.drawable.ic_views_line;
            case RATE:
                return R.drawable.ic_star_line;
            case BOOKMARK:
                return R.drawable.ic_bookmark_line;
        }
        throw new IllegalArgumentException("잘못된 매개변수입니다: " + pos);
    }
}
