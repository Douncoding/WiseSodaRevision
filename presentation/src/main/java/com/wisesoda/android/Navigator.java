package com.wisesoda.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.StringRes;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.wisesoda.android.view.activity.BlogListActivity;
import com.wisesoda.android.view.activity.GroupListActivity;
import com.wisesoda.android.view.activity.UserMgmtActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class used to navigate through the application.
 */
@Singleton
public class Navigator {
    private static final String CHROME_PKG = "com.android.chrome";
    private static final String GOOGLE_MAP_PKG = "com.google.android.apps.maps";

    @Inject
    public Navigator(){}

    public void navigateToBlogGroup(Context context, List<String> cityList) {
        if (context != null) {
            Intent intent = new Intent(context, GroupListActivity.class);
            intent.putStringArrayListExtra(GroupListActivity.EXTRA_CITY_LIST, new ArrayList<>(cityList));
            context.startActivity(intent);
        }
    }

    public void navigateToBlogList(Context context, String city, String keyword, String category) {
        if (context != null) {
            Intent intent = BlogListActivity.getCallingIntent(context, city, keyword, category);
            context.startActivity(intent);
        }
    }

    /**
     * Ref: https://developers.google.com/maps/documentation/android-api/intents?hl=ko
     *
     * @param context
     * @param city
     * @param keyword
     */
    public void navigateToGoogleMap(Context context, String city, String keyword) {
        if (context != null) {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(GOOGLE_MAP_PKG);

            if (intent == null) {
                showRequireAppDialog(context, R.string.dialog_gmap_install_title,
                        R.string.dialog_gmap_install_content, GOOGLE_MAP_PKG);
            } else {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + city + " " + keyword);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage(GOOGLE_MAP_PKG);
                context.startActivity(mapIntent);
            }
        }
    }

    public void navigateToChrome(Context context, String url) {
        if (context != null) {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(CHROME_PKG);

            if (intent == null) {
                showRequireAppDialog(context, R.string.dialog_chrome_install_title,
                        R.string.dialog_chrome_install_content, CHROME_PKG);
            } else {
                Intent chromeLaunch = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setData(Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                chromeLaunch.setPackage(CHROME_PKG);
                context.startActivity(intent);
            }
        }
    }

    private void showRequireAppDialog(final Context context,
                                      @StringRes final int titleId,
                                      @StringRes final int contentId,
                                      final String name) {
        new MaterialDialog.Builder(context)
                .title(titleId)
                .content(contentId)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
                        marketLaunch.setData(Uri.parse("market://details?id=" + name));
                        context.startActivity(marketLaunch);
                    }
                })
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .show();
    }


    public void navigateToUserMgmt(Context context) {
        if (context != null) {
            Intent intent = UserMgmtActivity.getCallingIntent(context);
            context.startActivity(intent);
        }
    }
}
