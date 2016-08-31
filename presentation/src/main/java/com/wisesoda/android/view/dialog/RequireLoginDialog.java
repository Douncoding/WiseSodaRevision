package com.wisesoda.android.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.wisesoda.android.R;

/**
 * 로그인 요청 다이얼로그
 */
public class RequireLoginDialog extends DialogFragment {
    public static final String TAG = RequireLoginDialog.class.getSimpleName();
    public interface OnCallback {
        void onPositive();
        void onNegative();
    }

    private OnCallback onDialogCallback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .title("Sign Up")
                .content(getString(R.string.announce_require_login))
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onPositive(onPositiveCallback)
                .onNegative(onNegativeCallback)
                .build();

        return dialog;
    }

    public void setOnDialogCallback(OnCallback callback) {
        this.onDialogCallback = callback;
    }

    MaterialDialog.SingleButtonCallback onPositiveCallback = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            if (onDialogCallback != null)
                onDialogCallback.onPositive();
        }
    };

    MaterialDialog.SingleButtonCallback onNegativeCallback = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            if (onDialogCallback != null)
                onDialogCallback.onNegative();
        }
    };
}
