package com.knotted.util;

// 시간 형식 관련 유틸 모음
public class TimeUtils {
    public static String addColonToTime(String time){
        return time.substring(0, 2) + ":" + time.substring(2);
    }
}
