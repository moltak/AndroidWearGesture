package com.horde.samantha.samantha.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by engeng on 11/1/15.
 */
public class ThirdpartyExectue {

    public static void runNavigation(Activity activity) {
        String destination = String.format("google.navigation:q=%f,%f&mode=d", 34.011333, -118.495488);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(destination));
        mapIntent.setPackage("com.google.android.apps.maps");
        activity.startActivity(mapIntent);
    }

    public static void killNavigation(Activity activity) {
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.killBackgroundProcesses("com.google.android.apps.maps");
    }

    public static void runRuntastic(Activity activity) {
        Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage("com.runtastic.android");
        activity.startActivity(launchIntent);
    }

    public static void killRuntastic(Activity activity) {
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.killBackgroundProcesses("com.runtastic.android");
    }

    public static void runMangoplate(Activity activity) {
        Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage("com.mangoplate");
        activity.startActivity(launchIntent);
    }

    public static void killMangoplate(Activity activity) {
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.killBackgroundProcesses("com.mangoplate");
    }
}
