package com.knotted.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.internal.bytebuddy.utility.RandomString;

// 랜덤 관련 유틸 모음
public class RandomUtils {
    
    // 20자리 랜덤 문자열 생성
    public static String getToken(){
        return RandomString.make(20);
    }

    // 16자리 랜덤 문자열 생성 (아스키코드 32 ~ 126번)
    public static String getPassword(){
        return RandomStringUtils.randomAscii(16);
    }
}
