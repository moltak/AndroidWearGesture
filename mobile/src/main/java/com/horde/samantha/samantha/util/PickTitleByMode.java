package com.horde.samantha.samantha.util;

/**
 * Created by engeng on 11/1/15.
 */
public class PickTitleByMode {
    public static String pick(String mode) {
        switch (mode) {
            case "wakeup":
                return "기상";
            case "navi":
                return "네비게이";
            case "lunch":
                return "점심시간";
            case "vibrate":
                return "진동";
            case "call":
                return "'전화";
            case "workout":
                return "운동중";
            case "sleep":
                return "수면중";
        }

        return "사만다";
    }
}
