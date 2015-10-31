package com.horde.samantha.samantha.util;

import com.horde.samantha.samantha.R;

/**
 * Created by engeng on 11/1/15.
 */
public class PickBulletineImageByMode {
    public static int pick(String mode) {
        switch (mode) {
            case "wakeup":
                return R.drawable.a_alarm;
            case "navi":
                return R.drawable.f_navi;
            case "lunch":
                return R.drawable.b_eating;
            case "vibrate":
                return R.drawable.c_bangbang;
            case "call":
                return R.drawable.e_calling;
            case "workout":
                return R.drawable.d_health;
            case "sleep":
                return R.drawable.g_sleep;
        }

        return R.drawable.phone;
    }
}
