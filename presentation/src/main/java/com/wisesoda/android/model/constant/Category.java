package com.wisesoda.android.model.constant;

import com.wisesoda.android.R;

/**
 *
 */
public enum Category {
    PL, // 장소
    RE, // 식당
    AC, // 숙소
    SH, // 쇼핑
    TR, // 교통
    SM; // 스마트폰

    public static int getLineIcon(String name) {
        Category category = Category.valueOf(name);

        switch (category) {
            case PL:
                return R.drawable.ic_placeholder_line;
            case RE:
                return R.drawable.ic_cutlery;
            case AC:
                return R.drawable.ic_bed_line;
            case SH:
                return R.drawable.ic_shopping_line;
            case TR:
                return R.drawable.ic_transport_line;
            case SM:
                return R.drawable.ic_smartphone_line;
        }

        // TODO Not found icon 섫정 필요
        return 0;
    }
}
