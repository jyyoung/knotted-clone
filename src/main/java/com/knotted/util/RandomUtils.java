package com.knotted.util;

import org.modelmapper.internal.bytebuddy.utility.RandomString;

// 랜덤 관련 유틸 모음
public class RandomUtils {
    public static String getToken(){
        return RandomString.make(16);
    }
}
