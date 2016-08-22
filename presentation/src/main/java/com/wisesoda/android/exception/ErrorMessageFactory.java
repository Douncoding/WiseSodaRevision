package com.wisesoda.android.exception;

import android.content.Context;

import com.wisesoda.android.R;

/**
 * Created by douncoding on 16. 8. 8..
 */
public class ErrorMessageFactory {

    private ErrorMessageFactory() {}

    public static String create(Context context, Exception exception) {
        String message = context.getString(R.string.exception_message_generic);

        return message;
    }
}
