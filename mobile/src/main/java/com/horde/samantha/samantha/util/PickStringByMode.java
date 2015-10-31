package com.horde.samantha.samantha.util;

/**
 * Created by engeng on 11/1/15.
 */
public class PickStringByMode {
    public static String pick(String mode) {
        switch (mode) {
            case "wakeup":
                return "졸린 3시! 일어나세요!";
            case "navi":
                return "원하는 목적지까지 안전하게 갈 수 있도록 도와줄께요. 지금부터 시작입니다.";
            case "lunch":
                return "즐거운 점심시간이 찾아왔어요! 오늘은 짜고 매운 음식말고 삼삼한 음식을 드셔보세요! \n건강에 좋답니다 :)";
            case "vibrate":
                return "전화가 왔습니다! 누구죠?";
            case "call":
                return "'사만다'에게 전화가 왔어요. 소중한 전화를 놓치지 마세요.";
            case "workout":
                return "우리 몸의 건강이 가장 중요하잖아요! 오늘도 으쌰으쌰 즐겁게 운동을 해봐요. \n하나둘 하나둘.";
            case "sleep":
                return "평화로운 밤이 찾아왔네요. 좋은 꿈 꾸세요. 내일 또 봐요. \n안녕히주무세요.";
        }

        return "사만다와 인사하세요. 안녕? 사만다?";
    }
}
