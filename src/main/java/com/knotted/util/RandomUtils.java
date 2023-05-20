package com.knotted.util;

import org.modelmapper.internal.bytebuddy.utility.RandomString;

// 랜덤 관련 유틸 모음
public class RandomUtils {
    
    // 20자리 랜덤 문자열 생성
    public static String getToken(){
        return RandomString.make(20);
    }
}
