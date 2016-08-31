package com.wisesoda.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.AppActionInfoBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.wisesoda.android.model.BlogModel;
import com.wisesoda.android.model.GroupModel;
import com.wisesoda.android.view.activity.BlogListActivity;
import com.wisesoda.android.view.activity.BookmarkActivity;
import com.wisesoda.android.view.activity.GroupListActivity;
import com.wisesoda.android.view.activity.RatingActivity;
import com.wisesoda.android.view.activity.UserMgmtActivity;
import com.wisesoda.android.view.components.KakaoLinkHandleActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class used to navigate through the application.
 */
public class Navigator {
    private static final String CHROME_PKG = "com.android.chrome";
    private static final String GOOGLE_MAP_PKG = "com.google.android.apps.maps";

    @Inject
    public Navigator(){
    }

    public void navigateToGroupList(Context context, List<String> cityList, String jsonGroup) {
        if (context != null) {
            Intent intent = new Intent(context, GroupListActivity.class);
            intent.putStringArrayListExtra(GroupListActivity.EXTRA_CITY_LIST, new ArrayList<>(cityList));
            intent.putExtra(GroupListActivity.EXTRA_DIRECT_BLOGLIST, jsonGroup);
            context.startActivity(intent);
        }
    }

    public void navigateToBlogList(Context context, String city, String keyword, String category) {
        if (context != null) {
            Intent intent = BlogListActivity.getCallingIntent(context, city, keyword, category);
            context.startActivity(intent);
        }
    }

    public void navigateToBookmark(Context context) {
        if (context != null) {
            Intent intent = BookmarkActivity.getCallingIntent(context);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    |Intent.FLAG_ACTIVITY_CLEAR_TOP
                    |Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
                showRequireInstallDialog(context, R.string.dialog_gmap_install_title,
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
                showRequireInstallDialog(context, R.string.dialog_chrome_install_title,
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

    private void showRequireInstallDialog(final Context context,
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
            intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(intent);
        }
    }

    public void navigateToRating(Context context, String jsonBlog) {
        if (context != null) {
            Intent intent = RatingActivity.getCallingIntent(context, jsonBlog);
            intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(intent);
        }
    }

    public static final String EXTRA_PARMS_KAKAO_SHARE = "EXTRA_PARMS_KAKAO_SHARE";
    public void navigateToKakaoAppShare(final Context context, BlogModel blogModel, GroupModel groupModel) {
        try {
            KakaoLink kakaoLink = KakaoLink.getKakaoLink(context);
            KakaoTalkLinkMessageBuilder builder = kakaoLink.createKakaoTalkLinkMessageBuilder();
            builder.addText(blogModel.getTitle() + "\n" + blogModel.getBlogUrl());
            builder.addImage(blogModel.getImageUrl(), 90, 90);
            builder.addAppButton("앱열기",
                    new AppActionBuilder()
                            .addActionInfo(AppActionInfoBuilder
                                    .createAndroidActionInfoBuilder()
                                    .setExecuteParam(EXTRA_PARMS_KAKAO_SHARE + "=" + groupModel.toString())
                                    .setMarketParam("referrer=kakaotalklink").build()).build());
            kakaoLink.sendMessage(builder, context);
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }

    public void navigateToOtherAppShare(final Context context, String title, String content, String image) {
        if (context != null) {
            Intent sendintent = new Intent(Intent.ACTION_SEND);
            sendintent.setType("text/plain");

            PackageManager packageManager = context.getPackageManager();
            Intent openInChooser = Intent.createChooser(sendintent,context.getString(R.string.share_chooser_text));
            List<LabeledIntent> intentList = new ArrayList<>();
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(sendintent, 0);

            for (ResolveInfo info : resolveInfoList) {
                String packageName = info.activityInfo.packageName.toLowerCase();
                if (packageName.contains("facebook")) {
                    Intent intent = (Intent)sendintent.clone();
                    sendintent.putExtra(Intent.EXTRA_SUBJECT, title);
                    sendintent.putExtra(Intent.EXTRA_TEXT, content);
                    intentList.add(new LabeledIntent(intent, packageName,
                            info.loadLabel(packageManager), info.icon));
                } else if (packageName.contains("kakao")) {
                    //카카오톡 필터
                } else {
                    Intent intent = (Intent)sendintent.clone();
                    sendintent.putExtra(Intent.EXTRA_SUBJECT, title);
                    sendintent.putExtra(Intent.EXTRA_TEXT, content);
                    intentList.add(new LabeledIntent(intent, packageName,
                            info.loadLabel(packageManager), info.icon));
                }
            }

            LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size()]);
            openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
            context.startActivity(openInChooser);
        }
    }
}
