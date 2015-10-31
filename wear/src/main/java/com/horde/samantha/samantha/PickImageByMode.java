package com.horde.samantha.samantha;

/**
 * Created by engeng on 11/1/15.
 */
public class PickImageByMode {
    public static int pick(String mode) {
        switch (mode) {
            case "wakeup":
                return R.drawable.time;
            case "navi":
                return R.drawable.car;
            case "lunch":
                return R.drawable.spoon;
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
