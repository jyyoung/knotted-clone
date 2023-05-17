package com.knotted.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// 시간 형식 관련 유틸 모음
public class TimeUtils {
    public static String addColonToTime(String time){
        return time.substring(0, 2) + ":" + time.substring(2);
    }

    public static String localDateTimeToString(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    public static LocalDateTime stringToLocalDateTime(String string){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(string, formatter);
    }
}
