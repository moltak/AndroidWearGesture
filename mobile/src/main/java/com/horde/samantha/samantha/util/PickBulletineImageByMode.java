package com.horde.samantha.samantha.util;

import com.horde.samantha.samantha.R;

/**
 * Created by engeng on 11/1/15.
 */
public class PickBulletineImageByMode {
    public static int pick(String mode) {
        switch (mode) {
            case "wakeup":
                return R.drawable.alarm;
            case "navi":
                return R.drawable.navi;
            case "lunch":
                return R.drawable.eating;
            case "vibrate":
                return R.drawable.bangbang;
            case "call":
                return R.drawable.phone;
            case "workout":
                return R.drawable.health;
            case "sleep":
                return R.drawable.sleep;
        }

        return R.drawable.phone;
    }
}
