package com.wisesoda.android.view.components;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.wisesoda.android.R;
import com.wisesoda.android.model.constant.Category;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by douncoding on 16. 8. 9..
 */
public class GroupListHeaderView extends LinearLayout implements View.OnClickListener {

    @BindView(R.id.place) ImageButton place;
    @BindView(R.id.restaurant) ImageButton restaurant;
    @BindView(R.id.rooms) ImageButton rooms;
    @BindView(R.id.transport) ImageButton transport;
    @BindView(R.id.shopping) ImageButton shopping;
    @BindView(R.id.phone) ImageButton phone;

    private List<ImageButton> buttonList = new ArrayList<>();

    // 선택된 버튼 위치 (1~6)
    private int position = -1;

    public GroupListHeaderView(Context context) {
        super(context);
        init();
    }

    public GroupListHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.header_inner_app_ber_keyword, this);
        ButterKnife.bind(this);

        place.setOnClickListener(this);
        restaurant.setOnClickListener(this);
        rooms.setOnClickListener(this);
        transport.setOnClickListener(this);
        shopping.setOnClickListener(this);
        phone.setOnClickListener(this);

        for (Category category : Category.values()) {
            switch (category) {
                case PL:
                    buttonList.add(place);
                    break;
                case RE:
                    buttonList.add(restaurant);
                    break;
                case AC:
                    buttonList.add(rooms);
                    break;
                case SH:
                    buttonList.add(shopping);
                    break;
                case TR:
                    buttonList.add(transport);
                    break;
                case SM:
                    buttonList.add(phone);
                    break;
            }
        }
        //기본값 설정
        onClick(place);
    }

    @Override
    public void onClick(View view) {
        boolean onChanged = false;
        int focusColor = ContextCompat.getColor(getContext(), R.color.white);
        int idleColor = ContextCompat.getColor(getContext(), R.color.colorDivider);

        for (int i = 0; i < buttonList.size(); i++) {
            ImageButton button = buttonList.get(i);

            if (button == view) {
                if (position != i) {
                    position = i;
                    onChanged = true;
                }

                button.setColorFilter(focusColor, PorterDuff.Mode.SRC_IN);
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_circle_button_focus);
                button.setBackground(drawable);
            } else {
                button.setColorFilter(idleColor, PorterDuff.Mode.SRC_IN);
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_circle_button_normal);
                button.setBackground(drawable);
            }
        }

        if (onChanged) {
            if (onItemClickedListener != null) {
                onItemClickedListener.onChanged(position);
            }
        }
    }

    public String getSelectedCategoryText() {
        for (Category category : Category.values()) {
            if (category.ordinal() == position) {
                return category.name();
            }
        }
        throw new RuntimeException("카테고리 상태를 찾을 수 없음");
    }

    private OnItemClickedListener onItemClickedListener;
    public interface OnItemClickedListener {
        void onChanged(int position);
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        this.onItemClickedListener = listener;
    }
}
